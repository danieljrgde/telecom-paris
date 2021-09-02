package simplexe;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class Menu extends JMenuBar implements ActionListener {
	private static final long serialVersionUID = 1L;
	JMenuItem nouveau = new JMenuItem("Nouveau dictionnaire");
	JMenuItem modifier = new JMenuItem("Modifier un dictionnaire");
	JMenuItem quitter = new JMenuItem("Quitter");
	 
	 public Menu() {
		 JMenu menu = new JMenu("Fichier");
		 nouveau.addActionListener(this);
		 menu.add(nouveau);
		 modifier.addActionListener(this);
		 menu.add(modifier);
		 quitter.addActionListener(this);
		 menu.add(quitter);
		 add(menu);
	 }
		
	 public void actionPerformed(ActionEvent evt) {
		Object source = evt.getSource();
		if (source == nouveau) {
			new Saisie();
		}
		else if (source == modifier) {
			new Modifier();
		}
		else if (source == quitter) {
			System.exit(0);
		}
	 }
}
