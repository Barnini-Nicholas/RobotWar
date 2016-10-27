package plugins;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

import gui.Grille;
import gui.Robot;

// TODO changer le nom
public class Deplacement_Random {

	/**
	 * Choisit un d�placement al�atoire
	 * 
	 * @param grille
	 * @param robot
	 * @return
	 */
	public Point choisirDeplacement(Grille grille, Robot robot) {
		// Position actuelle du robot
		Point posActuelle = robot.getPoint();
		int x = posActuelle.x;
		int y = posActuelle.y;

		// Nombre de points de mouvement du robot
		int pm = robot.getPtMouvement();

		// List des deplacements possibles
		ArrayList<Point> listDeplacements = getListeDeplacementsPossibles(x, y, pm);

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
	 * TODO emplacement d�ja prit par un robot
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public ArrayList<Point> getListeDeplacementsPossibles(int x, int y, int pm) {

		// Informations concernant la taille de la grille
		int nbColonneMax = Grille.getNbcolonnesmax();
		int nbLigneMax = Grille.getNblignesmax();

		// Liste des diff�rents points (d�placement) possible
		ArrayList<Point> listPoints = new ArrayList<>();

		// Calcul de toutes les positions possibles :
		// - en foncton de la pos de d�part
		// - et du nombre de pm
		for (int row = y - pm; row <= y + pm; row++) {
			for (int col = x - pm; col <= x + pm; col++) {
				// Cas o� le robot se trouve dans un coin
				if (row < 0 || col < 0 || row >= nbLigneMax || col >= nbColonneMax)
					continue;
				// Un d�placement possible
				Point casePossible = new Point(row, col);

				// On l'ajoute � la liste
				listPoints.add(casePossible);
			}
		}
		return listPoints;
	}

}
