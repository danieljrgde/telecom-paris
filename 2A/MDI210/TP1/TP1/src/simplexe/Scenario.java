package simplexe;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

/**
 * Modelise l'interface graphique.
 */
public class Scenario extends JFrame {
	private static final long serialVersionUID = 1L;
	JButton total = new JButton("Execution jusqu'a la fin");
	JButton unPas = new JButton("Effectuer une etape");
	JButton boutonPivoter = new JButton("Pivoter");
	JButton boutonRecommencer = new JButton("Choisir un dictionnaire");
	JButton boutonBase = new JButton("Appliquer");
	JCheckBox bland = new JCheckBox("Appliquer la regle de Bland", false);
	JRadioButton premier = new JRadioButton("La premiere candidate rencontree", true);
	JRadioButton grand = new JRadioButton("Le plus grand coefficient", false);
	JRadioButton avantageux = new JRadioButton("La plus avantageuse", false);
	JTextField vE = new JTextField(3);
	JTextField vS = new JTextField(3);
	JComboBox listeTailles;
	JTextField choixBase = new JTextField(8);
	JLabel labelChoix;
	ControleurScenario controleur;
	Simplexe simplexe;
	
	public Scenario(Simplexe simplexe) {
		this.simplexe = simplexe;
		controleur = new ControleurScenario(this, simplexe);
		addWindowListener(controleur);
		JPanel panneauHaut = new JPanel();
		Box panneauGauche = Box.createVerticalBox();
		Box panneauDroit = Box.createVerticalBox();
		JPanel panneau;
		ButtonGroup groupe = new ButtonGroup();

		JScrollPane ascenseurs = new JScrollPane(Simplexe.sortie);

		Simplexe.sortie.ascenseurs = ascenseurs;

		panneauHaut.setLayout(new BoxLayout(panneauHaut, BoxLayout.X_AXIS));

		panneau = new JPanel();
		panneau.add(new JLabel("Methode de choix de la variable entrante"));
		panneauGauche.add(panneau);

		groupe.add(premier);
		groupe.add(grand);
		groupe.add(avantageux);

		Box choixMethode = Box.createVerticalBox();
		choixMethode.add(premier);
		choixMethode.add(grand);
		choixMethode.add(avantageux);
		panneauGauche.add(choixMethode);

		premier.addItemListener(controleur);

		grand.addItemListener(controleur);

		avantageux.addItemListener(controleur);

		panneauGauche.add(Box.createVerticalStrut(10));

		bland.addItemListener(controleur);
		panneauGauche.add(bland);

		panneauGauche.add(Box.createVerticalStrut(10));

		panneau = new JPanel();
		panneau.add(total);
		panneauGauche.add(panneau);
		total.addActionListener(controleur);

		panneauGauche.add(Box.createVerticalStrut(20));

		panneau = new JPanel();
		panneau.add(unPas);
		panneauGauche.add(panneau);
		unPas.addActionListener(controleur);
		panneauHaut.add(panneauGauche);

		panneauDroit.add(Box.createVerticalStrut(10));

		boutonRecommencer.addActionListener(controleur);
		panneauDroit.add(boutonRecommencer);

		panneauDroit.add(Box.createVerticalStrut(10));

		Integer[] tailles = {10, 12, 14, 16, 18, 20, 22, 24, 30, 36};
		listeTailles = new JComboBox(tailles);
		listeTailles.addActionListener(controleur);
		listeTailles.setSelectedItem(16);
		JScrollPane listeTaillesAscenseur = new JScrollPane(listeTailles);
		panneau = new JPanel();
		panneau.add(new JLabel("Taille de la fonte "));
		panneau.add(listeTaillesAscenseur);
		panneauDroit.add(panneau);

		Box boite = Box.createVerticalBox();
		panneau = new JPanel();
		panneau.add(new JLabel("Numero variable entrante"));
		vE.setText("");
		panneau.add(vE);
		boite.add(panneau);
		panneau = new JPanel();
		panneau.add(new JLabel("Numero variable sortante"));
		vS.setText("");
		panneau.add(vS);
		boite.add(panneau);
		
		boutonPivoter.addActionListener(controleur);
		boite.add(boutonPivoter);
		
		boite. setBorder(BorderFactory.createEtchedBorder());
		panneau = new JPanel();
		panneau.add(boite);
		panneauDroit.add(panneau);
		
		boite = Box.createVerticalBox();
		boutonBase.addActionListener(controleur);
		choixBase.addActionListener(controleur);
		labelChoix = new JLabel("Choix d'une base (donner les numeros)");
		panneau = new JPanel();
		panneau.add(labelChoix);
		panneau.add(choixBase);
		boite.add(panneau);
		boite.add(boutonBase);

		boite. setBorder(BorderFactory.createEtchedBorder());
		panneau = new JPanel();
		panneau.add(boite);
		panneauDroit.add(panneau);
		
		
		panneauHaut.add(panneauDroit);

		add(panneauHaut, BorderLayout.NORTH);
		add(ascenseurs, BorderLayout.CENTER);
		

		boutonPivoter.setEnabled(false);
		boutonBase.setEnabled(false);
		
		griserBoutons();
		setJMenuBar(new Menu());
		pack();
		setLocation(200, 20);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	public void autoriserBoutons() {
		total.setEnabled(true);
		unPas.setEnabled(true);
		boutonPivoter.setEnabled(true);
		boutonBase.setEnabled(true);	
		premier.setEnabled(true);
		grand.setEnabled(true);
		avantageux.setEnabled(true);
		bland.setEnabled(true);
	}
	
	public void griserBoutons() {
		total.setEnabled(false);
		unPas.setEnabled(false);
		boutonPivoter.setEnabled(false);
		boutonBase.setEnabled(false);	
		premier.setEnabled(false);
		grand.setEnabled(false);
		avantageux.setEnabled(false);
		bland.setEnabled(false);
	}
	
	public void setSimplexe(Simplexe simplexe) {
		this.simplexe = simplexe;
		controleur = new ControleurScenario(this, simplexe);
		controleur.nbDecision = simplexe.getDico().getNbHorsBase();
		
	}

	public void desactiver() {
		boutonBase.setEnabled(false);
		boutonPivoter.setEnabled(false);
		unPas.setEnabled(false);
		total.setEnabled(false);
	}
	
	public void activer() {
		boutonBase.setEnabled(true);
		boutonPivoter.setEnabled(true);
		unPas.setEnabled(true);
		total.setEnabled(true);
	}
}

