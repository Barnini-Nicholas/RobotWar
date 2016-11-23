package plugins.attaque;

import java.awt.Point;
import java.util.ArrayList;

import interfacesMoteur.IGrille;
import interfacesMoteur.IRobot;
import interfacesPlugins.IPluginAttaque;

/**
 * Classe repr�sentant une case de la grille
 * 
 * @author Barnini Nicholas
 *
 */

public class AttaqueDeBase implements IPluginAttaque{

	private final int degats = 50;

	private final int energie = 50;

	/**
	 * Choisit une cible
	 * 
	 * @param grille
	 * @param robot
	 * @return Robot
	 */
	public IRobot choisirCible(IGrille grille, IRobot robot) {

		// On r�cupere la liste des robots attaquable
		ArrayList<IRobot> listRobotAttaquable = getListeAttaquesPossibles(robot, grille);
		int tailleArray = listRobotAttaquable.size();
		if (tailleArray != 0) {
			// On d�finit le premier robot comme la cible
			IRobot r = listRobotAttaquable.get(0);

			// Si la liste a plus de deux robots on parcours
			if (tailleArray > 1) {
				for (int i = 1; i < tailleArray; i++) {
					if (listRobotAttaquable.get(i).getVie() < r.getVie())
						r = listRobotAttaquable.get(i);
				}
			}
			// Le robot utilise de l'energie pour attaquer
			if (robot.getPtEnergie() >= energie) {
				robot.setPtEnergie(robot.getPtEnergie() - energie);
				// Le robot subit des d�gats
				r.setVie(r.getVie() - degats);
			} else{
				return null;
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

	private ArrayList<IRobot> getListeAttaquesPossibles(IRobot r, IGrille grille) {

		// On r�cup�re les valeurs des attributs du Robot
		int x = r.getPosition().x;
		int y = r.getPosition().y;
		int port�e = r.getPortee();

		// Informations concernant la taille de la grille
		int nbColonneMax = grille.getNbcolonnesmax();
		int nbLigneMax = grille.getNblignesmax();

		// On instancie la liste de robots � retourner
		ArrayList<IRobot> listRobot = new ArrayList<IRobot>();

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
				if (grille.getRobotFromPoint(new Point(row, col)) != null) {
					// On v�rifie qu'il soit � port�e
					if (isAtReachNoDiagonal(row, x, col, y, r.getPortee())) {
						listRobot.add(grille.getRobotFromPoint(new Point(row, col)));
					}
				}
			}
		}
		return listRobot;
	}

	/**
	 * D�finie si un robot est port�e ou non
	 * 
	 * @param x1
	 *            - Position X du robot vis�
	 * @param x2
	 *            - Position X du robot attaquant
	 * @param y1
	 *            - Position Y du robot vis�
	 * @param y2
	 *            - Position Y du robot attaquant
	 * @param port�e
	 *            - Correspond � la port�e du robot attaquant
	 * 
	 * @return boolean - true si � port�e
	 */
	private boolean isAtReachNoDiagonal(int x1, int x2, int y1, int y2, int port�e) {

		int differenceX = 0;
		int differenceY = 0;

		// On r�cup�re la diff�rence entre les X
		if (x1 < x2)
			differenceX = x2 - x1;
		else
			differenceX = x1 - x2;

		// On r�cup�re la diff�rence entre les Y
		if (y1 < y2)
			differenceY = y2 - y1;
		else
			differenceY = y1 - y2;

		// Si l'addition des deux diff�rence (X,Y) est inf�rieur ou �gal � la
		// port�e cela signifie que le robot est � port�e de tir
		if (differenceX + differenceY <= port�e)
			return true;
		else
			return false;
	}
}
