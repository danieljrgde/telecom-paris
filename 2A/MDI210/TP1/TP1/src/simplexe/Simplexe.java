package simplexe;

import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;

import matrice.Matrice;


/**
 * Modelise la methode du simplexe

 */
public class Simplexe implements Runnable {
	private Dictionnaire dico;
	private int phase = -1;
	private double[] zInitial;
	private double z0Initial;
	public static Affichage sortie = new Affichage();
	private Matrice ADebut;
	private double[] zDebut;
	private double z0Debut;
	private double[] bDebut;
	private int[] varHorsBaseInitiale;
	static String cheminDonnees = "pbs";
	private int compteur = 0;
	ControleurScenario controleur;
	Scenario vue;

	public Simplexe() throws IOException {
	}

	public void memoriserDebut() {
		calculerADebut();		
		zDebut = new double[dico.getNbBase() + dico.getNbHorsBase()];
		for (int j = 0; j < dico.getNbHorsBase(); j++)  zDebut[j] = dico.getD()[0][j + 1];
		z0Debut = dico.getD()[0][0];		
		bDebut = new double[dico.getNbBase()];
		for (int i = 0; i < dico.getNbBase(); i++)  bDebut[i] = dico.getD()[i + 1][0];
	} 

	public void memoriserZ() {
		varHorsBaseInitiale = new int[dico.getNbHorsBase() + 1];
		zInitial = new double[dico.getNbHorsBase() + 1];
		for (int j = 1; j <= dico.getNbHorsBase(); j++) {
			zInitial[j] = dico.getD()[0][j];
			varHorsBaseInitiale[j] = dico.getVarHorsBase()[j];
		}
		z0Initial = dico.getD()[0][0];
	}

	public void calculerADebut() {
		ADebut = new Matrice(dico.getNbBase(), dico.getNbHorsBase() + dico.getNbBase());

		for (int i = 0; i < dico.getNbBase(); i++) 
			for (int j = 0; j < dico.getNbHorsBase(); j++)
				ADebut.setValeur(i, j, -dico.getD()[i + 1][j + 1]);
		for (int i = 0; i < dico.getNbBase(); i++) {
			for (int j = dico.getNbHorsBase(); j < dico.getNbHorsBase() + dico.getNbBase(); j++)
				ADebut.setValeur(i, j, 0);
			ADebut.setValeur(i, i + dico.getNbHorsBase(), 1);
		}
	}

	public Dictionnaire choixDico() throws IOException {
		File fichier = null;

		JFileChooser dialogue = new JFileChooser(new File(cheminDonnees));
		if (dialogue.showOpenDialog(null)== JFileChooser.APPROVE_OPTION) {
			fichier = dialogue.getSelectedFile();
		}
		else sortie.println("Pas de choix");

		dico = new Dictionnaire(fichier);
		sortie.println("Le fichier traite est : " + fichier.getName() + "\n");
		dico.setBland(controleur.scenario.bland.isSelected());
		if (controleur.scenario.premier.isSelected())
				dico.setMethode(MethodeEntrante.PREMIERE);
		else if (controleur.scenario.grand.isSelected())
			dico.setMethode(MethodeEntrante.PLUS_GRAND);
		else dico.setMethode(MethodeEntrante.PLUS_AVANTAGEUSE);
		sortie.afficherDico(dico);

		boolean realisable = dico.estRealisable();
		if (dico.isIncomplet()) return dico;
		if (realisable) {
			controleur.scenario.total.setEnabled(true);
			sortie.println("Le dictionnaire est realisable");
			sortie.println("\nPHASE 2");
			phase = 2;
		}
		else {
			memoriserZ();
			sortie.println("Le dictionnaire n'est pas realisable : recherche d'un dictionnaire realisable");
			sortie.println("\nVeuillez cliquer sur \"Effectuer une etape\" pour avoir un premier dictionnaire de la phase 1");
			sortie.println("ou bien essayer d'avoir un dictionnaire realisable en pivotant");
			sortie.println("ou en essayant une autre base");
			new Thread(controleur).start();
			phase = -1;
		}
		memoriserDebut();
		vue.activer();
		return dico;
	}

	public void unPas() {
		if (dico.isIncomplet()){
			Simplexe.sortie.println("Veuillez completer votre programme");
			return; 
		}
		switch (phase) {
		case -1 :
			Simplexe.sortie.println("\nPHASE 1");
			setDico(dico.premierDicoPbAuxiliaire());
			vue.total.setEnabled(true);
			vue.boutonBase.setEnabled(false);
			phase = 0;
			break;
		case 0 : 
			Simplexe.sortie.println("\nOn fait en sorte d'avoir un premier dictionnaire realisable pour la phase 1");
			dico.dicoRealisablePbAuxiliaire();
			phase = 1;
			break;
		case 1 : 
			pivoter();
			if (dico.isIncomplet()) return; 
			if (dico.isOptimal()) {
				if (dico.getD()[0][0] < -Dictionnaire.epsilon) {
					sortie.println("Il n'existe aucune solution realisable");
					vue.desactiver();
					phase = 3;
				}
				else {
					sortie.println("Fin de la premiere phase");
					sortie.println("\nPHASE 2");
					phase = 2;
					controleur.scenario.total.setEnabled(true);
					dico = dico.dicoPbSecondePhase(zInitial, z0Initial, varHorsBaseInitiale);
					vue.boutonBase.setEnabled(true);
				}
			}
			break;
		case 2 :
			pivoter();
			if (dico.isOptimal()) {
				sortie.afficherSolution(dico);
				phase = 3;
				vue.desactiver();
			}
			else if (!dico.isBorne()) {
				sortie.println("Le probleme est non borne");
				phase = 3;
				vue.desactiver();
			}
			break;
		}
	}

	public void pivoter(){
		if (!vue.vE.getText().equals("")) {
			int numE = Integer.parseInt(vue.vE.getText());
			dico.uneEtape(dico.indiceHorsBase(numE));
			vue.vE.setText("");
		}
		else {
			dico.uneEtape();
		}
	}

	public void run() {
		while (phase != 3) {
			if (dico.isIncomplet()) {
				Simplexe.sortie.println("Veuillez completer votre programme");
				return; 
			}
			unPas();
			compteur++;
			if (compteur == 100) {
				sortie.println("Cela semble cycler, on arrete");
				compteur=0;
				return;
			}
		}
	}

	public Dictionnaire getDico() {
		return dico;
	}

	public void setDico(Dictionnaire dico) {
		this.dico = dico;
	}

	public int getPhase() {
		return phase;
	}

	public void setPhase(int phase) {
		this.phase = phase;
	}

	public double[] getzInitial() {
		return zInitial;
	}

	public double getZ0Initial() {
		return z0Initial;
	}

	public Matrice getADebut() {
		return ADebut;
	}

	public double[] getZDebut() {
		return zDebut;
	}

	public double getZ0Debut() {
		return z0Debut;
	}

	public double[] getBDebut() {
		return bDebut;
	}

	public int[] getVarHorsBaseInitiale() {
		return varHorsBaseInitiale;
	}
}
