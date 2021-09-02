# -*- coding: utf-8 -*-
"""
Created on Thu Mar 16 09:30:19 2017

@author: cagnazzo
"""
import matplotlib.pyplot as plt
import numpy as np


        
        

def wtView(coeffs,title='', verbose=0):

    arr=coeffs_to_array(coeffs)
    rows,cols =  arr.shape 

    nLevels = len(coeffs)-1
    
    scaled_wt = np.zeros([rows,cols])
    for levIdx in range(nLevels):
        if verbose:
            print("Processing level %d %dx%d" % ( levIdx, rows, cols))
        rows=np.int(rows/2)
        cols=np.int(cols/2)
        
        sb = abs(arr[0:rows,cols:2*cols])
        mm = sb.min()
        MM = sb.max()
        if verbose:
            print( "Max %5.2f " % MM)
            print ("Min %5.2f " % mm)
        scaled_wt[0:rows,cols:2*cols]= 1- (sb-mm)/(MM-mm)
      
        
        sb = abs(arr[rows:2*rows,0:cols])

        mm = sb.min()
        MM = sb.max()
        if verbose:
            print( "Max %5.2f " % MM)
            print ("Min %5.2f " % mm)
        scaled_wt[rows:2*rows,0:cols]= 1- (sb-mm)/(MM-mm)

        
        sb = abs(arr[rows:2*rows,cols:2*cols])

        mm = sb.min()
        MM = sb.max()
        if verbose:
            print ("Max %5.2f " % MM)
            print( "Min %5.2f " % mm)
        scaled_wt[rows:2*rows,cols:2*cols]= 1- (sb-mm)/(MM-mm)
       
    
    if  verbose:
        print ("Processing approximation %dx%d" % ( rows, cols))
    sb = arr[0:rows,0:cols]

    mm = sb.min()
    MM = sb.max()
    if verbose:
        print( "Max %5.2f " % MM)
        print ("Min %5.2f " % mm)
        print( sb)
    scaled_wt[0:rows,0:cols]= (sb-mm)/(MM-mm)
    
    plt.imshow(scaled_wt,cmap='gray')
    plt.title(title)
    plt.show()
    

def sbVariances(arr,nLevels,verbose=0):
    arr= np.float64(arr)
    rows,cols = arr.shape   
    variances = np.zeros(3*nLevels+1)
    for levIdx in range(nLevels):
        if verbose:
            print ("Processing level %d" % levIdx)
        rows=np.int(rows/2)
        cols=np.int(cols/2)
        
        sb = arr[0:rows,cols:2*cols]
        if verbose:
            print ("Var LH %5.2f " % sb.var())
        variances[levIdx*3]= sb.var()
      
        
        sb = arr[rows:2*rows,0:cols]
        if verbose:
            print ("Var LH %5.2f " % sb.var())
        variances[levIdx*3+1]= sb.var()

        
        sb = arr[rows:2*rows,cols:2*cols]
        if verbose:
            print ("Var LH %5.2f " % sb.var())
        variances[levIdx*3+2]= sb.var()       
    
    sb = arr[0:rows,0:cols] 
    if  verbose:
        print ("Processing approximation")
        print ("Var LL %5.2f " % sb.var())
    variances[3*nLevels]= sb.var()
    return variances   
    
    
def coeffs_to_array(coeffs, verbose=0):
    nLevels = len(coeffs)    
    R,C = coeffs[len(coeffs)-1][0].shape    
    arr = np.zeros([2*R,2*C])
    rows,cols = coeffs[0].shape
    arr[0:rows,0:cols] = coeffs[0]
    if verbose:
        print ('Level %d rows %3d cols %3d' % (0,rows,cols))

    
    for levIdx in range(1,nLevels):
        rows,cols = coeffs[levIdx][0].shape
        
        if verbose:
            print ('Level %d rows %3d cols %3d' % (levIdx,rows,cols))
       
        LH = coeffs[levIdx][0]
        HL = coeffs[levIdx][1]
        HH = coeffs[levIdx][2]
        
        arr[0:rows,cols:2*cols] = LH
        arr[rows:2*rows,0:cols] = HL
        arr[rows:2*rows,cols:2*cols] = HH       

    return arr
    
    
def array_to_coeffs(arr, nLevels, verbose=0):
        
    R,C = arr.shape    
    rows = R/2**nLevels
    cols = C/2**nLevels
    coeffs = []
    coeffs.append( arr[0:rows,0:cols] )
    if verbose:
        print ('Approx rows %3d cols %3d' % (rows,cols))

    
    for levIdx in range(0,nLevels):
                        
        if verbose:
            print ('Level %d rows %3d cols %3d' % (levIdx,rows,cols))
       
        lev = []
        lev.append(arr[0:rows,cols:2*cols] )
        lev.append(arr[rows:2*rows,0:cols])
        lev.append(arr[rows:2*rows,cols:2*cols])
        coeffs.append(lev)     
        
        rows = rows*2
        cols = cols*2

    return coeffs
    
 
import cv2   
def wtViewCV(arr,nLevels=0,name='Scaled WT',verbose=0):
    rows,cols = arr.shape
    
    scaled_wt = np.zeros([rows,cols])
    for levIdx in range(nLevels):
        if verbose:
            print ("Processing level %d" % levIdx)
        rows=np.int(rows/2)
        cols=np.int(cols/2)
        
        sb = abs(arr[0:rows,cols:2*cols])
        mm = sb.min()
        MM = sb.max()
        if verbose:
            print( "Max %5.2f " % MM)
            print ("Min %5.2f " % mm)
        scaled_wt[0:rows,cols:2*cols]= 1- (sb-mm)/(MM-mm)
      
        
        sb = abs(arr[rows:2*rows,0:cols])

        mm = sb.min()
        MM = sb.max()
        if verbose:
            print ("Max %5.2f " % MM)
            print ("Min %5.2f " % mm)
        scaled_wt[rows:2*rows,0:cols]= 1- (sb-mm)/(MM-mm)

        
        sb = abs(arr[rows:2*rows,cols:2*cols])

        mm = sb.min()
        MM = sb.max()
        if verbose:
            print( "Max %5.2f " % MM)
            print ("Min %5.2f " % mm)
        scaled_wt[rows:2*rows,cols:2*cols]= 1- (sb-mm)/(MM-mm)
       
    
    if  verbose:
        print ("Processing approximation")
    sb = arr[0:rows,0:cols]

    mm = sb.min()
    MM = sb.max()
    if verbose:
        print( "Max %5.2f " % MM)
        print( "Min %5.2f " % mm)
    scaled_wt[0:rows,0:cols]= (sb-mm)/(MM-mm)

        
    cv2.namedWindow(name,cv2.WINDOW_NORMAL)
    cv2.imshow(name,scaled_wt)   
    cv2.waitKey(0)
    cv2.destroyAllWindows()
    
    
    
    