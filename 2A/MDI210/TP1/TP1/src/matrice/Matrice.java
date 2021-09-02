package matrice;

import java.util.ArrayList;

/** 
 * @author ASLAN Hikmet
 * @version 1.1
 */
public class Matrice
{
	private double[][] coeff = null;
	//----------------------------------------------//
	// CONSTRUCTOR //
	//----------------------------------------------//
	/** Constructeur Matrice
	 * @param
	 * i - ligne
	 * j - colonne
	 */
	public Matrice(int i, int j)
	{
		this.setDimension(i,j);
	}
	public Matrice()
	{
		this(0,0);
	}
	public Matrice(double[][] mat)
	{
		this.coeff = mat;
	}

	// définit une matrice de type double[][]
	public void setMatrice(double[][] mat)
	{
		this.coeff = mat;
	}
	// définit une valeur à  la position i et j
	// i - ligne
	// j - col
	public void setValeur(int i, int j, double valeur)
	{
		this.coeff[i][j] = valeur;
	}
	// on définit la taille de la mtrice
	public void setDimension(int i, int j)
	{
		this.coeff = new double[i][j];
	}
	//----------------------------------------------//
	// GETTER //
	//----------------------------------------------//
	// retourne la matrice sous forme du type double[][]
	public double[][] getMatrice()
	{
		return this.coeff;
	}
	// retourne le nombre de lignes
	public int getNbLignes()
	{
		return this.coeff.length;
	}
	// retourne le nombre de colonnes
	public int getNbColonnes()
	{
		return this.coeff[0].length;
	}
	
	public double[] getColonne(int j) {
		double[] col = new double[getNbLignes()];
		
		for (int i = 0; i < getNbLignes(); i++) col[i] = getValeur(i, j);
		return col;
	}

	// retourne la valeur a la position i et j
	public double getValeur(int i, int j) {
		return this.coeff[i][j];
	}

	// retourne le déterminant d'une matrice
	public double getDeterminant()
	{
		Matrice a = null;
		double value = 0;
		if (this.getNbLignes() == 2)
			return (this.getValeur(0,0) * this.getValeur(1,1) - this.getValeur(1,0)* this.getValeur(0,1));
		else if (this.getNbLignes() == 1) return this.getValeur(0,0);
		for (int j=0; j<this.getNbColonnes(); j++)
		{
			a = this.getMatriceExtraite(0,j);
			value += (int)Math.pow(-1,j)*(this.getValeur(0,j)*a.getDeterminant());
		}
		return value;
	}

	// retourne la matrice inverse de la matrice this
	public Matrice getMatriceInverse()
	{
		Matrice a = new Matrice(this.getNbLignes(), this.getNbColonnes());
		Matrice tmp = null;
		double det = this.getDeterminant();
		for (int i=0; i<this.getNbLignes(); i++)
			for (int j=0; j< this.getNbColonnes(); j++)
			{
				tmp = this.getMatriceExtraite(i,j);
				a.setValeur(i,j,(int)Math.pow(-1,i+j)*(tmp.getDeterminant()/det));
			}
		// on transpose la matrice les coeffcients seront positionnÃ© de faÃ§on incorrect
		return a.getMatriceTranspose();
	}

	/* Retourne une nouvelle matrice mais en supprimant
	 * la ligne row et la colonne columns
	 */
	private Matrice getMatriceExtraite(int ligne, int colonne)
	{
		Matrice a = new Matrice(this.getNbLignes()-1, this.getNbColonnes()-1);
		int k = -1, m = 0;
		for (int i=0; i<this.getNbLignes(); i++)
		{
			k++;
			if (i == ligne)
			{
				k--;
				continue;
			}
			m = -1;
			for (int j=0; j<this.getNbColonnes(); j++)
			{
				m++;
				if (j==colonne)
				{
					m--;
					continue;
				}
				a.setValeur(k,m,this.getValeur(i,j));
			}
		}
		return a;
	}

	// transpose la matrice
	public Matrice getMatriceTranspose()
	{
		Matrice a = new Matrice(this.getNbColonnes(), this.getNbLignes());
		double tmp = 0;
		for (int i=0; i<a.getNbLignes(); i++)
			for (int j=0; j<a.getNbColonnes(); j++)
			{
				tmp = this.getValeur(j,i);
				a.setValeur(i,j,tmp);
			}
		return a;
	}

	// multiplication
	public Matrice multiply(final Matrice matrice)
	{
		Matrice a = new Matrice(this.getNbLignes(), this.getNbColonnes());
		int k,i,j;
		double value = 0;
		for (k=0; k<this.getNbColonnes(); k++)
		{
			for (i=0; i<this.getNbLignes(); i++)
			{
				for (j=0; j<this.getNbColonnes(); j++)
					value += this.getValeur(i,j)*matrice.getValeur(j,k);
				a.setValeur(i,k,value);
				value = 0;
			}
		}
		return a;
	}

	// addition
	public Matrice sommeMatrice(Matrice matrice)
	{
		Matrice a = new Matrice(this.getNbLignes(), this.getNbColonnes());
		for (int i=0; i<this.getNbLignes(); i++)
			for (int j=0; j<this.getNbColonnes(); j++)
				a.setValeur(i,j,this.getValeur(i,j)+matrice.getValeur(i,j));
		return a;
	}

	// soustraction
	public Matrice soustraction(final Matrice matrice)
	{
		Matrice a = new Matrice(this.getNbLignes(), this.getNbColonnes());
		for (int i=0; i<this.getNbLignes(); i++)
			for (int j=0; j<this.getNbColonnes(); j++)
				a.setValeur(i,j,this.getValeur(i,j)-matrice.getValeur(i,j));
		return a;
	}

	// détermine si la matrice est inversible
	public boolean isInversible()
	{
		return (this.getDeterminant() != 0);
	}


	// le fameux toString() :)
	@Override
	public String toString()
	{
		String out = "";
		for (int i=0; i<this.getNbLignes(); i++)
		{
			for (int j=0; j< this.getNbColonnes(); j++)
				out += this.coeff[i][j] + "\t ";
			out += "\n";
		}
		return out;
	}
	

	
	public Matrice extraire(ArrayList<Integer> colonnes) {
		int num;
		int nb = getNbLignes();
		Matrice B = new Matrice(nb, nb);

		for (int j = 0; j < nb; j++) {
			num = colonnes.get(j);
			for (int i = 0; i < nb; i++) 
				B.setValeur(i, j , getValeur(i, num));
		}
		return B;
	}
	
	public double[] produitGauche(double[] ligne) {
		double P[] = new double[getNbLignes()];
		
		for (int j = 0; j < this.getNbColonnes(); j++) {
			P[j] = 0;
			for (int i = 0; i < getNbColonnes(); i++) P[j] += ligne[i] * getValeur(i, j);
		}
		return P;
	}
	

	public double[] produitDroit(double[] col) {
		double P[] = new double[getNbLignes()];
		
		for (int i = 0; i < this.getNbLignes(); i++) {
			P[i] = 0;
			for (int j = 0; j < getNbColonnes(); j++) P[i] += getValeur(i, j) * col[j];
		}
		return P;
	}
	
	public static double produit(double[] ligne, double[] col) {
		double P = 0;
		for (int i = 0; i < ligne.length; i++) P += ligne[i] * col[i];
		return P;
	}
}
