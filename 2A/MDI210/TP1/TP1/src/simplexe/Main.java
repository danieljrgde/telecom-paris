package simplexe;

import java.io.IOException;


/**
 * Contient la methode main
 */
public class Main { 
	public static void main(String[] arg) throws IOException {
		Simplexe simplexe = new Simplexe();
		Scenario scenario = new Scenario(simplexe);
		simplexe.vue = scenario;
		simplexe.controleur = scenario.controleur;
	}
}
