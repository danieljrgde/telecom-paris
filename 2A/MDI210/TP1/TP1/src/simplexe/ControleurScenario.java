package simplexe;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;

import matrice.Matrice;

public class ControleurScenario extends WindowAdapter 
	implements ActionListener, ItemListener, Runnable {
	Scenario scenario;
	private Simplexe simplexe;
	int nbDecision;
	private boolean cestdit;


	public ControleurScenario(Scenario scenario) {
		this.scenario = scenario;
	}

	public ControleurScenario(Scenario scenario, Simplexe simplexe) {
		this.scenario = scenario;
		this.simplexe = simplexe;
	}

	public void actionPerformed(ActionEvent evt) {
		Object source = evt.getSource();
		
		if (source == scenario.boutonRecommencer) {
			try {
				Simplexe.sortie.setText("");
				simplexe.choixDico();
				scenario.autoriserBoutons();
				nbDecision = simplexe.getDico().getNbHorsBase();
			}
			catch(IOException exc) {
				exc.printStackTrace();
			}
		}
		
		else if (source == scenario.unPas) {
			simplexe.unPas();
		} 

		else if (source == scenario.boutonPivoter) {
			int jE, iS, numE, numS;

			if (simplexe.getDico().isIncomplet()){
				Simplexe.sortie.println("Veuillez completer votre programme");
				return; 
			}
			if (simplexe.getPhase() == 3) {
				Simplexe.sortie.println("C'est fini...");
				return;
			}
			
			try {
				if (scenario.vE.getText().equals("")) {
					Simplexe.sortie.println("Pas de variable entrante indiquee");
					return;
				}
				if (scenario.vS.getText().equals("")) {
					Simplexe.sortie.println("Pas de variable sortante indiquee");
					return;
				}
				numE = Integer.parseInt(scenario.vE.getText());
				numS = Integer.parseInt(scenario.vS.getText());
				jE = simplexe.getDico().indiceHorsBase(numE);
				iS = simplexe.getDico().indiceBase(numS);			
				if (jE == 0)Simplexe.sortie.println("Numero de variable entrante non valide");
				if (iS == 0)Simplexe.sortie.println("Numero de variable sortante non valide");
				if ((jE != 0) && (iS != 0)) {
					if (Dictionnaire.estNul(simplexe.getDico().getD()[iS][jE])) {
						Simplexe.sortie.println("Impossible : division par zero");
						return;
					}
					simplexe.getDico().uneEtape(jE, iS);
					scenario.vE.setText("");
					scenario.vS.setText("");	
					traiterPivotOuBase();
				}
			}
			catch(NumberFormatException exc) {
				Simplexe.sortie.println("Erreur sur les numeros");
			}
		}

		else if ((source == scenario.boutonBase) || (source == scenario.choixBase)){
			if (simplexe.getDico().isIncomplet()){
				Simplexe.sortie.println("Veuillez completer votre programme");
				return; 
			}
			if (simplexe.getPhase() == 3) {
				Simplexe.sortie.println("C'est fini...");
				return;
			}
			java.util.Scanner scan = new java.util.Scanner(scenario.choixBase.getText());
			ArrayList<Integer> listeBase = new ArrayList<Integer>();
			Matrice B;
			ArrayList<Integer> colonnes = new ArrayList<Integer>();
			int num;

			try {
				while (scan.hasNext()) {
					num = Integer.parseInt(scan.next());
					if (!simplexe.getDico().contient(num)) {
						Simplexe.sortie.println("Mauvaise variable");
						scan.close();
						return;
					}
					if (!listeBase.contains(num))listeBase.add(num);
				}
				if (listeBase.size() != simplexe.getDico().getNbBase()) {
					Simplexe.sortie.println("Il n'y a pas le bon nombre de variables");
					scan.close();
					return;
				}
				scan.close();
			}
			catch(NumberFormatException exc) {
				Simplexe.sortie.println("erreur d'ecriture");
			}
			for (int i : listeBase) colonnes.add(i - 1);
			B = simplexe.getADebut().extraire(colonnes);
			if (!B.isInversible()) {
				Simplexe.sortie.println("Ce n'est pas une base");
				return;
			}
			Simplexe.sortie.println("\nNouveau dictionnaire");
			simplexe.setDico(new Dictionnaire(simplexe.getADebut(), B, listeBase, simplexe.getBDebut(),
					simplexe.getZDebut(), simplexe.getZ0Debut()));
			traiterPivotOuBase();
		}
		
		else if (source == scenario.total) {	
			if (simplexe.getDico().isIncomplet()){
				Simplexe.sortie.println("Veuillez completer votre programme");
				return; 
			}
			if (simplexe.getPhase() == 3) {
				Simplexe.sortie.println("C'est fini...");
				return;	
			}
			new Thread(simplexe).start();
		}
		
		else if (source == scenario.listeTailles) {
			if (simplexe.getDico() == null) return;
			Simplexe.sortie.setFont(new Font("Courier New", Font.BOLD, (Integer)scenario.listeTailles.getSelectedItem()));
		}
		(new Thread(this)).start();
	}


	public void traiterPivotOuBase() {
		switch (simplexe.getPhase()) {
		case -1 :	
			if (simplexe.getDico().estRealisable()) {
				if (simplexe.getDico().isIncomplet()) return;
				if (!simplexe.getDico().isOptimal()) {
					Simplexe.sortie.println("Bravo, le dictionnaire est realisable");
					Simplexe.sortie.println("\nPHASE 2");
					simplexe.setPhase(2);
				}
				else{
					Simplexe.sortie.println("Bravo, le dictionnaire est optimal");
					Simplexe.sortie.afficherSolution(simplexe.getDico());
					simplexe.setPhase(3);
				}
			}
			else Simplexe.sortie.println("Le dictionnaire n'est toujours pas realisable");
			break;	
		case 0 : 
			if (!simplexe.getDico().estRealisable()) {
				scenario.unPas.setEnabled(false);
				scenario.total.setEnabled(false);
				Simplexe.sortie.println("Erreur : le dictionnaire n'est pas realisable");
				Simplexe.sortie.println("Il faut retablir la situation en pivotant correctement");
				cestdit = true;
				simplexe.setPhase(1);
			}
			else traiterRealisable0_1();
			break;
		case 1: 
			if (!simplexe.getDico().estRealisable()) {
				if (!cestdit) {
					scenario.unPas.setEnabled(false);
					scenario.total.setEnabled(false);
					Simplexe.sortie.println("Erreur : le dictionnaire n'est pas realisable");
					Simplexe.sortie.println("Il faut retablir la situation en pivotant correctement");
					cestdit = true;
				}
				else {	
					Simplexe.sortie.println("Le dictionnaire n'est toujours pas realisable");
				}
			}
			else this.traiterRealisable0_1(); 
			break;
		case 2 : 
			if(!simplexe.getDico().estRealisable()) {
				Simplexe.sortie.println("Erreur : le dictionnaire n'est plus realisable");
				Simplexe.sortie.println("On tombe en phase 1");
				simplexe.setPhase(-1);
				simplexe.memoriserZ();
			}
			else {
				if (simplexe.getDico().isOptimal()) {
					Simplexe.sortie.println("Bravo, le dictionnaire est optimal");
					Simplexe.sortie.afficherSolution(simplexe.getDico());
					simplexe.setPhase(3);
					scenario.desactiver();
				}
				else if (!simplexe.getDico().borne) {
					Simplexe.sortie.println("Le probleme est non borne");
					simplexe.setPhase(3);
					scenario.desactiver();
				}
			}	
		}
		scenario.choixBase.setText("");
		scenario.vS.setText("");
		scenario.vE.setText("");
	}
	
	public void traiterRealisable0_1() {		
		scenario.unPas.setEnabled(true);
		scenario.total.setEnabled(true);
		cestdit = false;
		if (simplexe.getDico().isOptimal()) {
			Simplexe.sortie.println("Bravo, le dictionnaire de la premiere phase est optimal");
			if (simplexe.getDico().getD()[0][0] < -Dictionnaire.epsilon) {
				Simplexe.sortie.println("Il n'existe aucune solution realisable");
				simplexe.setPhase(3);
			}
			else {
				Simplexe.sortie.println("Fin de la premiere phase");
				Simplexe.sortie.println("\nPHASE 2");
				scenario.boutonBase.setEnabled(true);
				simplexe.setPhase(2);
				simplexe.setDico(simplexe.getDico().dicoPbSecondePhase
						(simplexe.getzInitial(), simplexe.getZ0Initial(), simplexe.getVarHorsBaseInitiale()));
			}
		}		
	}
	
	public void windowOpeneds(WindowEvent evt) {
		try {
			Simplexe simplexe = new Simplexe();
			scenario.setSimplexe(simplexe);
			this.simplexe = simplexe;
		}
		catch(IOException exc) {
			exc.printStackTrace();
		}
	}

	public void itemStateChanged(ItemEvent evt) {
		Object source = evt.getSource();
		MethodeEntrante methode = simplexe.getDico().getMethode();
		if (source == scenario.premier) {
			if (methode != MethodeEntrante.PREMIERE)
				simplexe.getDico().setMethode(MethodeEntrante.PREMIERE);
		}
		else if (source == scenario.grand) {
			if (methode != MethodeEntrante.PLUS_GRAND)
			simplexe.getDico().setMethode(MethodeEntrante.PLUS_GRAND);
		}
		else if (source == scenario.avantageux) {
			if (methode != MethodeEntrante.PLUS_AVANTAGEUSE)
				simplexe.getDico().setMethode(MethodeEntrante.PLUS_AVANTAGEUSE);
		}
			
		else if (source == scenario.bland) 
			simplexe.getDico().setBland(!simplexe.getDico().isBland());
	}
	
	public void run() {
		try {
			Thread.sleep(1000);
		}
		catch (InterruptedException exc) {
			exc.printStackTrace();
		}
		scenario.repaint();
	}
}
