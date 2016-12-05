package plugins.deplacement;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

import interfacesMoteur.IGrille;
import interfacesMoteur.IRobot;
import interfacesPlugins.IPluginDeplacement;

// TODO changer le nom
public class DeplacementDeBase implements IPluginDeplacement {

	/**
	 * Choisit un d�placement al�atoire
	 * 
	 * @param grille
	 * @param robot
	 * @return
	 */
	public Point choisirDeplacement(IRobot robot, IGrille grille) {

		// List des deplacements possibles
		ArrayList<Point> listDeplacements = getListeDeplacementsPossibles(robot, grille);

		// Choix du d�placement al�atoirement
		Random rand = new Random();
		Point positionChoisie = listDeplacements.get(rand.nextInt(listDeplacements.size()));

		return positionChoisie;

	}

	/**
	 * Retourne la liste des d�placements possibles du robot <br>
	 * 
	 * On laisse la possibilit� au robot de ne pas se d�placer (de se d�placer
	 * sur sa position actuelle)
	 *
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public ArrayList<Point> getListeDeplacementsPossibles(IRobot robot, IGrille grille) {

		// Position actuelle du robot
		Point posActuelle = robot.getPosition();
		int x = posActuelle.x;
		int y = posActuelle.y;

		// Nombre de points de mouvement du robot
		int pm = robot.getPtMouvement();

		// Informations concernant la taille de la grille
		int nbColonneMax = grille.getNbcolonnesmax();
		int nbLigneMax = grille.getNblignesmax();

		// Liste des diff�rents points (d�placement) possible
		ArrayList<Point> listPoints = new ArrayList<>();

		// Calcul de toutes les positions possibles :
		// - en foncton de la pos de d�part
		// - et du nombre de pm
		for (int row = x - pm; row <= x + pm; row++) {

			for (int col = y - pm; col <= y + pm; col++) {

				// Cas o� le robot se trouve dans un coin
				if (row < 0 || col < 0 || row >= nbLigneMax || col >= nbColonneMax)
					continue;

				// Si on n'est pas sur le point o� se trouve le robot
				// initialement
				if (!(row == x && col == y)) {
					// On regarde s'il n'y a pas d�ja un robot sur cette case
					if (grille.getRobotFromPoint(new Point(row, col)) != null)
						continue;
				}

				if (isAtReachNoDiagonal(row, x, col, y, pm)) {
					// Un d�placement possible
					Point casePossible = new Point(row, col);

					// On l'ajoute � la liste
					listPoints.add(casePossible);
				}
			}
		}
		return listPoints;
	}

	/**
	 * D�finie si un robot est � port�e d'une case
	 * 
	 * @param x1
	 *            - Position X de la case � test�
	 * @param x2
	 *            - Position X du robot
	 * @param y1
	 *            - Position Y de la case � test�
	 * @param y2
	 *            - Position Y du robot
	 * @param pm
	 *            - Correspond aux points de mouvement du robot
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
		// port�e cela signifie que le robot est � port�e de mouvement
		if (differenceX + differenceY <= port�e)
			return true;
		else
			return false;
	}

}
