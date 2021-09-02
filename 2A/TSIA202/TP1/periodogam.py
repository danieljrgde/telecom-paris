import numpy as np
from scipy import signal as sig


def In_λ(X, k, m):

    # Sets constants
    n = len(X)
    π = np.pi
    λ = 2*π*k/m

    # Validate inputs
    if not m >= n:
        raise Exception("Invalid value of m")
    if not (k in [i for i in range(0, m-1)]):
        raise Exception("Invalid value of k")

    # Obtains the DTFT of the centred samples
    ξ = 0
    for t in range(0, n-1):
        ξ += (X[t]*np.e**(complex(0, -λ*t)))/2*π

    # returns In(λ)
    return (2*π/n)*np.abs(ξ)**2
        

# Constants
Nit = 500           # Number of paths
H = 400             # Number of points for covariance
n = 2*H -1          # Number of process' samples
displayRatio = 4     # 1/displayRatio graphs are displayed  increase displayRatio to speed up, in particular if Nit/displayRatio>100
EX     = np.zeros(n)          # Expectations are stored here
gamma  = np.zeros(n)          # ACF's are stored here
tc     = np.arange(-(H-1),H)  # temporal axis for ACF
t      = np.arange(0,n)       # temporal axis for the signal
m = 2*n-1


# white noise
sigma = 1
X = np.random.normal(0, sigma, n)
print(f"White noise: {In_λ(X, 1, m)}")

# AR
Z = np.random.normal(0,1,n) # White noise
phi1  = 0.6
Pcoeffs = np.array([1.])                 
Qcoeffs = np.poly((phi1,))
X = sig.lfilter(Pcoeffs, Qcoeffs, Z)
print(f"AR: {In_λ(X, 1, m)}")

# Seno
A_0 = 1.
omega = np.pi/3.
phi = np.pi + 2*np.pi*np.random.random_sample();
X = A_0 * np.cos(omega*tc+phi) + np.random.normal(0, 1, n)
print(f"SIN: {In_λ(X, 1, m)}")
