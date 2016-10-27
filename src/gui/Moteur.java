package gui;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;

/**
 * Moteur du jeu
 * 
 * @author Karl,Leonard
 *
 */
public class Moteur {

	ArrayList<Robot> listeRobots = new ArrayList<>();

	Grille grille;
	Robot robot;

	public Moteur(int i) {

		// Instantiation de la grille de jeu
		creationDeLaGrilleDeJeu();

		placementDesRobots(i);

		// D�marrage des tours
		gestionDesTours();
	}

	/**
	 * M�thode qui cr�e un grille de jeu
	 */
	private void creationDeLaGrilleDeJeu() {
		grille = new Grille();

		// TEST : On place la grille dans une JFrame
		JFrame frame = new JFrame("Test de la classe Grille");
		frame.setContentPane(grille);
		frame.setVisible(true);
		frame.setSize(500, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	/**
	 * M�thode qui place les robots al�atoirement sur la grille
	 * 
	 * @param nbRobot
	 */
	private void placementDesRobots(int nbRobot) {
		// Liste des toutes les positions possibles :
		ArrayList<Point> listePositions = new ArrayList<>();

		// Remplissage de cette liste
		for (int row = 0; row < Grille.getNblignesmax(); row++) {
			for (int col = 0; col < Grille.getNbcolonnesmax(); col++) {
				listePositions.add(new Point(row, col));
			}
		}

		// Cr�ation des robots :
		for (int i = 1; i <= nbRobot; i++) {
			// Cr�ation d'un robot
			robot = new Robot();
			robot.setIndice(i);
			robot.setPtMouvement(1);
			robot.setPortee(1);

			// Choix de sa position de d�part
			Random rand = new Random();
			// Un random dans la liste des positions
			Point positionChoisie = listePositions.get(rand.nextInt(listePositions.size()));

			// On affecte cette position au robot
			robot.setPoint(positionChoisie);
			// On ajoute le robot � la grille
			grille.getElementsGrille()[positionChoisie.x][positionChoisie.y].setRobot(robot);

			// Ajout du robot � la liste des robots
			listeRobots.add(robot);

			// On enl�ve la position de la liste des positions possibles
			listePositions.remove(positionChoisie);
		}

	}

	/**
	 * M�thode qui s'occupe de la gestion des tours
	 */
	public void gestionDesTours() {

		// Boucle pour chaque tour de jeu :
		for (int i = 0; i < 10; i++) {
			try {
				System.out.println("Tour : " + (i + 1));
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			for (Robot robot : listeRobots) {
				// On demande au robot de se d�placer
				robot.seDeplacer(grille);
				robot.attaquer(grille);
			}
		}

	}

	public static void main(String[] args) {

		Moteur m = new Moteur(10);

	}
}
