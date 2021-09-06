try:
	from decision_functions import BuildDecisionTree
	BuildDecisionTree
	print('BuildDecisionTree loaded!')
	print('----')
except Exception as e:
	raise e

try:
	from decision_functions import printDecisionTree
	printDecisionTree
	print('printDecisionTree loaded!')
	print('----')
except Exception as e:
	raise e

try:
	from decision_functions import generalizationError
	generalizationError
	print('generalizationError loaded!')
	print('----')
except Exception as e:
	raise e

try:
	from decision_functions import pruneTree
	pruneTree
	print('pruneTree loaded!')
	print('----')
except Exception as e:
	raise e