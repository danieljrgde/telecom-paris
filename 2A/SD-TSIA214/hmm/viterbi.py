#!/usr/bin/env python2
# -*- coding: utf-8 -*-
"""
Created on Wed Jun  6 10:03:27 2018

@author: chloeclavel
"""

import numpy as np

def viterbi(X,Pi0,A,P):
    """
        Viterbi Algorithm Implementation

        Keyword arguments:
            - obs: sequence of observation
            - states:list of states
            - start_prob:vector of the initial probabilities
            - trans: transition matrix
            - emission_prob: emission probability matrix
        Returns:
            - seq: sequence of state
    """

    #pour eviter d avoir des valeurs nulles dans le log
    realmin = np.finfo(np.double).tiny
    A = np.log(A+realmin)
    Pi0 = np.log(Pi0+realmin)
    P = np.log(P+realmin)
    taille = np.shape(X) #X.shape[0]
    T = taille[0] #nombre d observations
    N = Pi0.shape[0]#nombre des etats du modele
    print T,N
    #Initialisation
    logl = np.zeros((T,N))
    bcktr = np.zeros((T-1,N))
    
    logl[0,:]=Pi0+P[X[0],:]
    for t in range(1,(T-1)):
        print t


    
    path =1
    return logl, path
