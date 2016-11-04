package gui;

import java.awt.GridLayout;
import java.awt.Point;

import javax.swing.JLabel;

import main.Robot;

/**
 * Classe repr�sentant une grille de jeu
 * 
 * @author Karl
 *
 */
@SuppressWarnings("serial")
public class Grille extends JLabel {

	/** Nombre max de cases par colonnes */
	private int nbColonnesMax;
	/** Nombre max de cases par lignes */
	private int nbLignesMax;

	private Case[][] elementsGrille;

	/**
	 * Constructeur de la classe {@link Grille}
	 */
	public Grille(int xGrille, int yGrille) {
		super();
		this.nbColonnesMax = xGrille;
		this.nbLignesMax = yGrille;
		// Layout utilis�
		setLayout(new GridLayout(nbLignesMax, nbColonnesMax));

		elementsGrille = new Case[nbLignesMax][nbColonnesMax];

		// Construction de la grille :
		for (int row = 0; row < nbLignesMax; row++) {
			for (int col = 0; col < nbColonnesMax; col++) {
				// Cr�ation d'une case
				Case caseGrille = new Case();

				// Ajout de la case � la grille
				add(caseGrille, row, col);

				elementsGrille[row][col] = caseGrille;
			}
		}

	}

	public Case[][] getElementsGrille() {
		return elementsGrille;
	}

	public int getNbcolonnesmax() {
		return nbColonnesMax;
	}

	public int getNblignesmax() {
		return nbLignesMax;
	}

	/**
	 * M�thode qui d�place un robot sur une nouvelle case
	 * 
	 * @param robot
	 * @param posChoisie
	 */
	public void deplacerRobot(Robot robot, Point posChoisie) {
		// Position actuelle du robot
		Point posActuelle = robot.getPosition();

		// Si ce n'est pas la 1ere fois qu'on ajoute ce robot
		if (posActuelle != null) {
			// On vide la case o� il �tait avant
			elementsGrille[posActuelle.x][posActuelle.y].setRobot(null);
			elementsGrille[posActuelle.x][posActuelle.y].setText("");
		}

		// On ajoute le robot dans la case
		elementsGrille[posChoisie.x][posChoisie.y].setRobot(robot);

		// On sauvegarde sa position
		robot.setPosition(posChoisie);
	}

	/**
	 * M�thode qui retire un robot de la grille
	 * 
	 * @param robot
	 */
	public void retirerRobot(Robot robot) {
		// Position actuelle du robot
		Point posActuelle = robot.getPosition();

		// On le retire de la grille
		elementsGrille[posActuelle.x][posActuelle.y].setRobot(null);
		
		// On mets le setText �galement � null
		elementsGrille[posActuelle.x][posActuelle.y].setText("");
	}

	/**
	 * M�thode qui renvoie le robot plac� au point demand�
	 * 
	 * @param point
	 * @return
	 */
	public Robot getRobotFromPoint(Point point) {
		return elementsGrille[point.x][point.y].getRobot();
	}

}
