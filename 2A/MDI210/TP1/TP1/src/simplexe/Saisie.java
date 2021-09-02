package simplexe;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class Saisie extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	JTextArea saisie = new JTextArea(30, 40);
	JButton sauvegarde = new JButton("Sauvegarder");
	
	public Saisie() {
		sauvegarde.addActionListener(this);
		JPanel panneau = new JPanel();
		panneau.add(sauvegarde);
		add(panneau, BorderLayout.NORTH);
		
		saisie.setText("Attention : ecrire en debut de fichier\n");
		saisie.append("nombre de variables puis nombre de contraintes\n");
		saisie.append("puis les contraintes, si a1 x1 + a2 x2 <= b : ecrire a1 a2 b\n");
		saisie.append("puis la fonction objectif, si z = c1 x1 + c2 x2 : ecrire c1 c2\n");
		add(saisie);
		setLocation(200, 200);
		pack();
		setVisible(true);		
	}

	public void actionPerformed(ActionEvent evt) {
		JFileChooser dialogue = new JFileChooser(Simplexe.cheminDonnees);
		PrintWriter sortie;
		File fichier;

		try {
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
