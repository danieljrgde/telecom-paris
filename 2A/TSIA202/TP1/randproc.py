# -*- coding: utf-8 -*-
"""
Created on Fri Sep 13 09:38:00 2019

Module for TSIA202a Practical works

@author: cagnazzo
"""

import numpy as np
from scipy import signal as sig
import matplotlib.pyplot as plt

#%%
def acovb(X=None):

    Xc = X - np.mean(X)    
    m = 2 * X.size - 1
    I = np.abs(np.fft.fft(Xc,m)) ** 2 /  X.size
    gamma = np.real(np.fft.ifft(I))    
    gamma = gamma[0:X.size]
    return gamma

#%%
def myhann(M,flag='sym'):
    if flag=='periodic':
        om = 2.0*np.pi/M
        l  = np.arange(0,M)       
    else:
        om = 2.0*np.pi/(M+1)
        l  = np.arange(1,M+1)
    w =  0.5-0.5*np.cos(l*om)
    return w
    
#%%
def genAR(p,n,noiseSTD=1):
# Returns    
# X :   Generated AR process 
# phi : coefficients or recurrence equation 
#       X(t) = phi(1)X(t-1)+...+phi(p)X(t-p) + Z(t);
#       Z(t) is WN(0,1).
# p :   Order of AR filter
# n :   Number of output samples
# noiseSTD : STD of the white noise. Default = 1

    # random draw of int(p/2) complex roots inside the unit circle 
    nrc = int(p/2) #% number of complex roots

    # Set the module of the poles in the (0.5 0.999) interval
    rho = .5+0.499*np.sqrt(np.random.random_sample(nrc)) 
    theta = 2*np.pi*np.random.random_sample(nrc)
    zk = rho *np.exp(1.j *theta);
    #
    # Compute transient lenght
    zmax = zk[np.abs(zk)==np.max(np.abs(zk))]
    rhomax = np.abs(zmax)
    tau = -1./np.log(rhomax) 
    transient = int(5*tau)    #  we allow for some room
    
    zk = np.concatenate((zk, np.conjugate(zk)))
    if np.remainder(p,2): # we need to add one real positive root in the interval (0,1)
        realRoot = np.random.random_sample()
        zk =  np.concatenate(([realRoot], zk))
                                      
    coeff = np.poly(zk) # coefficients of polynomial p(z) = prod_k( z-z_k);
    coeff = np.real(coeff) # remove possible imag residuals due to limited precision
    
    
    phi = -coeff[1:];
                
    # White noise generation. Lenght = n+transient
    noise = noiseSTD*np.random.randn(n+transient)

    # AR signal generation
    X =  sig.lfilter([1], coeff, noise)
    
    # Removing the transient
    X=X[transient:]

    return X, phi
 
#%%    
def drawZ_DTFT_AR(X,phi):
    # Draws the poles of the AR transfer function
    # Draws the DTFT and the positions of the poles
    coeff = np.concatenate(([1], -phi))
    zk    = np.roots(coeff)
    theta = np.angle(zk)
    
    #%%
    plt.figure()
    # Draw the unit circle
    t = np.linspace(-np.pi,np.pi,1024)
    plt.plot(np.sin(t),np.cos(t))
    plt.axis('equal')
    plt.grid()
    plt.plot(np.real(zk),np.imag(zk), 'x' )
    plt.title('Poles of the AR transfer function')
    
    #%%
    plt.figure()    
    # the smallest integer power of two that is not less than the size of X
    nPoints = np.int(np.exp2( np.ceil(np.log2(X.size))) )
    nu = np.linspace(-0.5, 0.5 - 1/nPoints, nPoints)
    omega = 2*np.pi*nu
    acf = acovb(X)
    PSD = np.real(np.fft.fft(acf,int(nPoints))) #
    plt.plot(omega,np.fft.fftshift(PSD)/max(PSD))
    plt.plot(theta,np.zeros_like(theta),'x')
    plt.title('PSD and phases of the poles')
       
    # Draw the filter energy transfer function |H(nu)|^2
    w, H = sig.freqz([1], coeff, omega)
    H2 = np.real( H*np.conj(H))
    plt.plot(omega,H2/max(H2))
    
    plt.legend(['Normalized PSD of the AR signal','Phases of the poles','Normalized Power transfer function |H|^2'])
    
    
#%%
def  detectPitch(X,minT,maxT)   :
# Returns the estimanted period of the input speech sample
# If the sample is not voiced, it returns 0
# Parameters : 
    # X input signal
    # minT minimum period
    # maxX maximum period
    
    gamma = acovb(X)
    rho   = gamma/gamma[0]     # autocorrelation function
    rho_bar = (rho[minT:])
    M = rho_bar.max()
    indexes = np.nonzero((rho)==M)
    T = indexes[0] 
    T=np.squeeze(T) 
    threshold = 0.5
    if M*X.size/(X.size-T)< threshold or T> maxT:
        T=0
        
    return T

#%%
               
    
                       
                       
                       
                       
                       
                       
                       
                       
                       
                       
                       
                       
                       
                       
                       

    
    
    