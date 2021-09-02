package simplexe;

import java.awt.Font;
import java.awt.Point;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;


public class Affichage extends JTextArea implements Runnable {
	private static final long serialVersionUID = 1L;
	JScrollPane ascenseurs;
	boolean aFaire;

	public Affichage() {
		super(35, 100);
		setFont(new Font("Courier New", Font.BOLD, 16));
		ascenseurs = new JScrollPane(this);
		setEditable(false);
		new Thread(this).start();
	}

	public void print(String s) {
		append(s);
		int hauteur = getHeight();
		ascenseurs.getViewport().setViewPosition(new Point(0, hauteur));
	}

	synchronized public void println(String s) {
		append(s + "\n");
		int hauteur = getHeight();
		ascenseurs.getViewport().setViewPosition(new Point(0, hauteur));
	}

	public void println(Object obj) {
		println(obj.toString());
	}

	synchronized public void afficherDico(Dictionnaire dico) {		
		char obj ='z';
		if ((dico.indiceBase(0) != 0 ) || (dico.indiceHorsBase(0) != 0)) obj = 'w';
		for (int i = 1; i <= dico.getNbBase(); i++) {
			append("x" + dico.getVarBase()[i] + " = ");
			if (Dictionnaire.estNul(dico.getD()[i][0]))
				append(String.format("%6s", " "));
			else append(String.format("%6.2f", dico.getD()[i][0]));
			for (int j = 1; j <= dico.getNbHorsBase(); j++ ) {
				if (Dictionnaire.estNul(dico.getD()[i][j]))
					append(String.format("%11s", " "));
				else {
					if(dico.getD()[i][j] >=  0) {
						append(" + ");
						append(String.format("%5.2f", dico.getD()[i][j]));
					}
					else {
						append(" - ");
						append(String.format("%5.2f", -dico.getD()[i][j]));
					}
					append(" ");
					append("x" + dico.getVarHorsBase()[j]);
				}
			}
			append("\n");
		}
		append(obj + "  = ");
		if (Dictionnaire.estNul(dico.getD()[0][0]))
			append(String.format("%6s", " "));
		else
			append(String.format("%6.2f", dico.getD()[0][0]));
		for (int j = 1; j <= dico.getNbHorsBase(); j++ ){
			if (Dictionnaire.estNul(dico.getD()[0][j]))
				append(String.format("%11s", " "));
			else {
				if(dico.getD()[0][j] >=  0) {
					append(" + ");
					append(String.format("%5.2f", dico.getD()[0][j]));
				}
				else {
					append(" - ");
					append(String.format("%5.2f", -dico.getD()[0][j]));
				}
				append(" ");
				append("x" + dico.getVarHorsBase()[j]);
			}
		}
		append("\n");
		aFaire = true;
		notify();
	}

	/**
	 * Affiche la solution du problème ; est utilisé si le dictionnaire est optimal.
	 */
	synchronized public void afficherSolution(Dictionnaire dico) {

		double[] solution = new double[dico.getNbHorsBase() + dico.getNbBase() + 1];
		for(int j = 1; j <= dico.getNbHorsBase(); j++) solution[dico.getVarHorsBase()[j]] = 0;
		for(int i = 1; i <= dico.getNbBase(); i++) solution[dico.getVarBase()[i]] = dico.getD()[i][0];
		append("\nLa solution optimale est obtenue pour : \n");
		for (int i = 1; i <= dico.getNbHorsBase(); i++) {
			append("x" + i + " = ");
			append(String.format("%.2f\n", solution[i]));
		}

		println("Les variables d'ecart valent : ");
		for (int i = dico.getNbHorsBase() + 1; i <= dico.getNbHorsBase() + dico.getNbBase(); i++) {
			append("x" + i + " = ");
			append(String.format("%.2f\n", solution[i]));
		}
		append("La valeur optimale de l'objectif vaut : ");
		append(String.format("%.2f\n", dico.getD()[0][0]));
		aFaire = true;
		notifyAll();
	}

	synchronized public void run() {
		while (true) {
			try {
				while (!aFaire) wait();	
				Thread.sleep(10);
				int hauteur = getHeight();
				if (ascenseurs.getViewport().getHeight() > getLineCount() * getRowHeight()) {
					ascenseurs.getViewport().setViewPosition(new Point(0, 0));
				}
				else {
					ascenseurs.getViewport().setViewPosition(new Point(0, hauteur));
					append("\n");
				}
				aFaire = false;
			}
			catch (Exception exc) {
				exc.printStackTrace();
			}
		}
	}
}
