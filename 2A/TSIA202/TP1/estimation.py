# -*- coding: utf-8 -*-
"""
Created on Fri Sep 13 16:42:18 2019

@author: cagnazzo
"""

import numpy as np
from scipy import signal as sig
from scipy import linalg as la
import matplotlib.pyplot as plt
import randproc as rp
import time

#%%
plt.close('all')
n = 1000
p = 4

std = 1
X, phi = rp.genAR(p,n,std)
coeff= np.concatenate(([1], -phi))
rp.drawZ_DTFT_AR(X,phi)
#%%

gamma = rp.acovb(X) 
Gamma_hat = la.toeplitz(gamma[0:p+1])  

v1 = np.zeros(p+1)
v1[0] = 1
c = np.dot(la.inv(Gamma_hat), v1)

 
sigma2Est = 1/c[0] 
estimated_coeff = c * sigma2Est 
estimated_coeff[0] = 1                  


print(estimated_coeff) 
print(coeff)            
#%%
err= coeff -     estimated_coeff
rel_err =  np.linalg.norm(err)/np.linalg.norm(coeff) 
print('Relative error {0:.2%} '.format(rel_err))


#% Show spectra
plt.figure()
nPoints = np.int(np.exp2( np.ceil(np.log2(X.size))))
nu = np.linspace(-0.5, 0.5 - 1/nPoints, nPoints)
omega = 2*np.pi*nu
w1, H1 = sig.freqz([1], coeff, omega)
w2, H2 = sig.freqz([1], estimated_coeff, omega)
plt.plot(w1,abs(H1))
plt.plot(w2,abs(H2))
plt.legend(['Amplitude Frequency Response (FR) of the AR filter','Estimated Amplitude FR' ])

#%%Display poles
plt.figure()
# Draw the unit circle

plt.axis('equal')
plt.grid()
zk    = np.roots(coeff)
plt.plot(np.real(zk),np.imag(zk), 'x' )
zk2 = np.roots(estimated_coeff)
plt.plot(np.real(zk2),np.imag(zk2), '+' )
plt.legend(['Poles of the AR transfer function','Poles of the estimated AR'])
t = np.linspace(-np.pi,np.pi,1000)
plt.plot(np.sin(t),np.cos(t), 'black')

plt.show()









