import pandas as pd
import numpy as np
from decision_functions import BuildDecisionTree, printDecisionTree, generalizationError, pruneTree

data = pd.read_csv('data.csv', delimiter=',')
data.head()

records = data[['Sex', 'Pclass', 'Embarked']]
attributes = data['Survived']

minNum = 5
alpha = 0.5

print('Decision Tree:')
tree = BuildDecisionTree(records[0:20], attributes[0:20], minNum)
printDecisionTree(tree, records)

print('\n\ngeneralizationError:')
error = generalizationError(records, attributes, tree, alpha)
print(error)

print('\n\nPruned Decision Tree:')
prunedTree = pruneTree(records, attributes, tree, alpha)
printDecisionTree(prunedTree, records)
