package simplexe;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class Modifier extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	JTextArea saisie = new JTextArea(30, 40);
	JButton modifie = new JButton("Enregistrer");
	JButton sous = new JButton("Enregistrer sous");
	String cheminFichier;

	public Modifier() {
		JFileChooser dialogue = new JFileChooser(Simplexe.cheminDonnees);
		BufferedReader entree;
		File fichier;
		String ligne;
		modifie.addActionListener(this);
		sous.addActionListener(this);
		Box boite = Box.createVerticalBox();
		boite.add(modifie);
		boite.add(sous);
		JPanel panneau = new JPanel();
		panneau.add(boite);
		add(panneau, BorderLayout.NORTH);


		try {
			if (dialogue.showOpenDialog(null) ==  JFileChooser.APPROVE_OPTION) {
				fichier = dialogue.getSelectedFile();
				cheminFichier = fichier.getPath();
				entree = new BufferedReader(new FileReader(fichier.getPath()));
				while ((ligne = entree.readLine()) != null)
					saisie.append(ligne + "\n");
				add(saisie);
				setLocation(200, 200);
				pack();
				setVisible(true);	
				entree.close();
			}
		}
		catch(IOException exc) {
			exc.printStackTrace();
		}	
	}

	public void actionPerformed(ActionEvent evt) {
		PrintWriter sortie;
		File fichier;
		Object source = evt.getSource();
		if (source == modifie) {
			try {
				fichier = new File(cheminFichier);
				sortie = new PrintWriter(new FileWriter(fichier.getPath(), false));
				sortie.println(saisie.getText());
				sortie.close();
				dispose();
			}
			catch(IOException exc) {
				exc.printStackTrace();
			}
		}
		else {

			try {

				JFileChooser dialogue = new JFileChooser(Simplexe.cheminDonnees);
				if (dialogue.showSaveDialog(null) ==  JFileChooser.APPROVE_OPTION) {
					fichier = dialogue.getSelectedFile();
					sortie = new PrintWriter(new FileWriter(fichier.getPath(), false));
					sortie.println(saisie.getText());
					sortie.close();
					dispose();
				}
			}
			catch(IOException exc) {
				exc.printStackTrace();
			}
		}
	}
}
