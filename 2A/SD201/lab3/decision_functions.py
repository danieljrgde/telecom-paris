import pandas as pd
import numpy as np

# this function generates all the possible splits for a set of records
def generate_splits(records):
    splits = []
    
    for column in records.columns:
        uniques = list(np.unique(records[column]))
        [splits.append((column,unique)) for unique in uniques[1:]]
    
    return splits

# this function calculates the gini value for a node
def compute_gini_node(attributes):
    counts = attributes.value_counts(normalize = True).to_list()
    
    gini = 1
    for count in counts:
            gini -= count**2
    return gini

# this function calculates the gini split values for a given set of records/attributes
def compute_gini_splits(records, attributes, splits):
    
    gini_splits = {}
    
    for split in splits:
    
        column, condition = split
        mask = records[column] < condition

        counts_1 = attributes[mask].value_counts(normalize = True).to_list()

        gini_1 = 1
        for count in counts_1:
            gini_1 -= count**2

        w_1 = len(attributes[mask])/len(attributes)

        counts_2 = attributes[~mask].value_counts(normalize = True).to_list()

        gini_2 = 1
        for count in counts_2:
            gini_2 -= count**2

        w_2 = len(attributes[~mask])/len(attributes)

        gini_split = w_1*gini_1 + w_2*gini_2

        gini_splits[split] = gini_split

    gini_splits_ordered = {key:gini_splits[key] for key in sorted(gini_splits, key=gini_splits.get, reverse=True)}
    
    return gini_splits_ordered

# this function checks if all attributes in the node belong to the same class
def is_pure(attributes):
    
    if len(pd.unique(attributes)) == 1:
        return True
    
    else:
        return False

# this function classifies the leaf node based on the attribute with the highest count
def classify_node(attributes):
    classes, classes_counts = np.unique(attributes, return_counts=True)
    
    index = classes_counts.argmax()
    prediction = classes[index]
    
    return prediction

def BuildDecisionTree(records, attributes, minNum, level = 0):
    
    '''
    This function builds a decision tree object using the smallest gini split criteria. 
    
    If the dataset generates a tree with only one node, the algorithm returns a tuple containing the node level and gini value
    as well as the attribute's value. 
    
    Otherwise, it returns a tree dictionary containing the following tree structure:
    
    - key: a tuple (level, gini, column, condition), such that column < condition. 
    E.g.: (0, 0.5, 'Sex', 1) indicates that the node is in level 0, has a 0.5 gini value and constraint 'Sex' < 1.

    - value: a dictionary of two other trees, where key 'y' is paired with the tree that respects the condition
    column < condition and key 'n' is paired with the tree that respects column <= condition.
    '''
    
    # increase level count
    level += 1
    
    # generates all possible record splits
    splits = generate_splits(records)
    
    # calculates the gini value for that node
    gini_node = compute_gini_node(attributes)
    
    # node is a leaf
    if is_pure(attributes) or len(records) < minNum or len(splits) == 0:
        
        # return leaf value
        return (level-1, gini_node, 'Leaf', classify_node(attributes))
    
    # node is not a leaf
    else:
        
        # create empty tree structure
        tree = {}
        
        # calculate gini split values
        gini_splits = compute_gini_splits(records, attributes, splits)
        
        # get the split with the lowest gini split value
        (column, condition), min_gini = gini_splits.popitem()
        
        # split the data
        mask = records[column] < condition
        
        # calculate the resulting trees recursively
        tree_y = BuildDecisionTree(records[mask], attributes[mask], minNum, level)
        tree_n = BuildDecisionTree(records[~mask], attributes[~mask], minNum, level)
        
        # append to the the tree dict
        tree[(level-1, gini_node, column, condition)] = {'y' : tree_y, 'n' : tree_n}
    
    return tree

# this function returns a list containing all the nodes in a tree
def getTreeNodes(tree):
    nodes = []
    
    # tree is already leaf, therefore it doesnt contain any divisions
    if not isinstance(tree, dict):
        level, gini, leaf_string, value = tree
        nodes.append(tree)
    
    # tree contains divisions
    else:
        for key, value in tree.items():

            # key is a condition node
            if isinstance(key, tuple):
                nodes.append(key)

            # value is a leaf node
            if isinstance(value, tuple):
                nodes.append(value)

            # value is another dictionary, recursive call
            if isinstance(value, dict):
                nodes += getTreeNodes(value)

    return nodes

# this function prints a decision tree
def printDecisionTree(tree, records):
    
    # gets list of all nodes
    node_list = getTreeNodes(tree)
    
    # sorts list of all nodes based on level
    node_list_sorted = sorted(node_list, key = lambda node_list: node_list[0])
    
    # prints the tree by level
    last_level = -1
    for level, gini, column, condition in node_list_sorted:
        
        if level == last_level:
            print('*****')
        else:
            if level != 0:
                print('')
            
        if column == 'Leaf':
            print('Leaf')
            print('Level', level)
            print('Class', condition)
            
        else:
            if(level == 0):
                print('Root')
            else:
                print('Intermediate')
            print('Level', level)
            
            features = np.unique(records[column][records[column] < condition])
            
            print('Feature', column, ' '.join(map(str, features)))
            
        print('Gini', round(gini,4))
        
        last_level = level
        
# this function classifies one record according to a given decision tree
def classify(record, tree):
    
    # tree is already leaf, therefore it doesnt contain any divisions
    if not isinstance(tree, dict):
        level, gini, leaf_string, value = tree
        return value
    
    # tree contains divisions
    else:
        node = list(tree.keys())[0]

        level, gini, column, condition = node
        
        # chooses the correct tree split for the record
        if int(record[column]) < condition:
            answer = tree[node]['y']
        else:
            answer = tree[node]['n']
    
        # answer is a leaf, return the tree classification value
        if not isinstance(answer, dict):
            level, gini, leaf_string, value = answer
            return value
        
        # answer is not a leaf, recursively find the correct classification
        else:
            return classify(record, answer)

# this function calculates the complexity of a tree
def complexity(tree, alpha):
    node_list = getTreeNodes(tree)
    
    num_leaves = 0
    for node in node_list:
        if node[2] == 'Leaf':
            num_leaves += 1
    
    complexity = alpha*num_leaves
    return complexity

# this function calculates the normalized generalization error for a set of records and attributes given a tree
def generalizationError(records, attributes, tree, alpha):
    
    prediction = records.apply(classify, axis=1, args=(tree,))
    error = (prediction != attributes).sum()
    
    generalization_error = (error + complexity(tree, alpha))/len(records)
    return generalization_error

# this function post prunes a given tree
def pruneTree(records, attributes, tree, alpha):
    node = list(tree.keys())[0]
    
    tree_y = tree[node]['y']
    tree_n = tree[node]['n']
    
    # node splits into two leaves
    if (not isinstance(tree_y, dict)) and (not isinstance(tree_n, dict)):
        
        # create new leaf
        new_leaf_level = tree_y[0]
        new_leaf_gini = compute_gini_node(attributes)
        new_leaf_answer = classify_node(attributes)
        new_leaf = (new_leaf_level-1, new_leaf_gini, 'Leaf', new_leaf_answer)
        
        # calculate errors for pruned and unpruned trees
        tree_error = generalizationError(records, attributes, tree, alpha)
        pruned_tree_error = generalizationError(records, attributes, new_leaf, alpha)
        
        # returns the version with lowest error
        if  pruned_tree_error <= tree_error:
            return new_leaf
        
        else:
            return tree
    
    else:
        # obtain node column < condition
        column = node[2]
        condition = node[3]
        
        # split records and attributes according to restriction
        mask = records[column] < condition
        
        records_y = records[mask]
        attributes_y = attributes[mask]
        
        records_n = records[~mask]
        attributes_n = attributes[~mask]
        
        # recursively prune trees for both split trees
        if isinstance(tree_y, dict):
            tree_y = pruneTree(records_y, attributes_y, tree_y, alpha)
            
        if isinstance(tree_n, dict):
            tree_n = pruneTree(records_n, attributes_n, tree_n, alpha)
        
        # build current tree
        tree = {node: {'y' : tree_y, 'n' : tree_n}}
        
        # return the pruned tree
        return pruneTree(records, attributes, tree, alpha)