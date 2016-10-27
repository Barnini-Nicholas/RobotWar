package plugins;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

import gui.Grille;
import gui.Robot;

/**
 * Classe repr�sentant une case de la grille
 * 
 * @author Barnini Nicholas
 *
 */

public class Attaque_de_Base {

	/**
	 * Choisit une cible
	 * 
	 * @param grille
	 * @param robot
	 * @return Robot
	 */
	public Robot choisirCible(Grille grille, Robot robot) {

		// On r�cupere la liste des robots attaquable
		ArrayList<Robot> listRobotAttaquable = getListeAttaquesPossibles(robot.getPoint().x, robot.getPoint().y,
				robot.getPortee(), grille);
		int tailleArray = listRobotAttaquable.size();
		if (tailleArray != 0) {
			// On d�finit le premier robot comme la cible
			Robot r = listRobotAttaquable.get(0);

			// Si la liste a plus de deux robots on parcours
			if (tailleArray > 1) {
				for (int i = 1; i < tailleArray; i++) {
					if (listRobotAttaquable.get(i).getVie() < r.getVie())
						r = listRobotAttaquable.get(i);
				}
			}
			return r;
		} else
			return null;
	}

	/**
	 * Retourne la liste des robots dans la port�e du robot attaquant <br>
	 * 
	 * @param x
	 * @param y
	 * @param port�e
	 * @return ArrayList<Robot>
	 */

	public ArrayList<Robot> getListeAttaquesPossibles(int x, int y, int port�e, Grille grille) {

		// Informations concernant la taille de la grille
		int nbColonneMax = Grille.getNbcolonnesmax();
		int nbLigneMax = Grille.getNblignesmax();

		// On instancie la liste de robots � retourner
		ArrayList<Robot> listRobot = new ArrayList<Robot>();

		// On parcours la zone entourant le robot et si il y a un robot on
		// ajoute
		for (int row = x - port�e; row <= x + port�e; row++) {
			for (int col = y - port�e; col <= y + port�e; col++) {

				// Cas o� le robot se trouve dans un coin
				if (row < 0 || col < 0 || row >= nbLigneMax || col >= nbColonneMax)
					continue;

				if (row == x && col == y)
					continue;

				// On regarde s'il y a un robot sur cette case
				if (grille.getElementsGrille()[row][col].getRobot() != null)
					listRobot.add(grille.getElementsGrille()[row][col].getRobot());
			}
		}
		return listRobot;
	}
}
