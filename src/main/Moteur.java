package main;

import java.awt.Point;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;

import gui.Grille;
import plugins.Gestionnaire_Plugins;
import plugins.TypePlugin;

/**
 * Moteur du jeu
 * 
 * @author Karl,Leonard
 *
 */
public class Moteur {

	/** Liste des robots */
	ArrayList<Robot> listeRobots = new ArrayList<>();

	/** Grille de jeu */
	Grille grille;

	/** Fen�tre de jeu */
	JFrame frame;

	/** Chemin vers le fichier pour stocker l'�tat des plugins */
	private final String PATH_TO_FILE = "src/ressources/test.txt";

	/** Gestionnaire des plugins */
	private Gestionnaire_Plugins gestionnairePlugins;

	/**
	 * Constructeur de la classe Moteur
	 * 
	 * @param nbRobots
	 *            nombre de robots � placer
	 */
	public Moteur(int nbRobots, int xGrille, int yGrille) {

		// - PREPARATION DU JEU :

		// 1) Initialisation des plugins
		initialisationDesPlugins();

		// 2) Instantiation de la grille de jeu
		creationDeLaGrilleDeJeu(xGrille, yGrille);

		// 3) Placement des robots
		placementDesRobots(nbRobots);

		// - DEBUT DU JEU :

		// Lancement du jeu
		gestionDesTours();
	}

	/**
	 * M�thode qui initialise tous les diff�rents plugins du jeu
	 */
	private void initialisationDesPlugins() {
		gestionnairePlugins = new Gestionnaire_Plugins();

		// Chargement des plugins GRAPHISME :
		gestionnairePlugins.chargerPlugin("plugins.graphisme.Graphisme_de_Base", TypePlugin.GRAPHISME);
		gestionnairePlugins.chargerPlugin("plugins.graphisme.Barre_de_vie", TypePlugin.GRAPHISME);

		// Chargement du plugin ATTAQUE :
		gestionnairePlugins.chargerPlugin("plugins.attaque.Attaque_de_Base", TypePlugin.ATTAQUE);

		// Chargement du plugin DEPLACEMENT :
		gestionnairePlugins.chargerPlugin("plugins.deplacement.Deplacement_Random", TypePlugin.DEPLACEMENT);

	}

	/**
	 * M�thode qui cr�e un grille de jeu
	 */
	private void creationDeLaGrilleDeJeu(int xGrille, int yGrille) {
		grille = new Grille(xGrille, yGrille);

		// TEST : On place la grille dans une JFrame
		frame = new JFrame("Test de la classe Grille");
		frame.setContentPane(grille);
		frame.setVisible(true);
		frame.setSize(500, 500);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
				sauvegardeEtatPlugin();
				System.exit(0);
			}
		});

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
		for (int row = 0; row < grille.getNblignesmax(); row++) {
			for (int col = 0; col < grille.getNbcolonnesmax(); col++) {
				listePositions.add(new Point(row, col));
			}
		}

		// Cr�ation des robots :
		for (int i = 1; i <= nbRobot; i++) {
			// Cr�ation d'un robot
			Robot robot = new Robot(gestionnairePlugins);
			robot.setIndice(i);

			// Choix de sa position de d�part
			Random rand = new Random();
			// Un random dans la liste des positions
			Point positionChoisie = listePositions.get(rand.nextInt(listePositions.size()));

			// On ajoute le robot � la grille
			grille.deplacerRobot(robot, positionChoisie);

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

		boolean finDePartie = false;

		// Boucle pour chaque tour de jeu :
		while (finDePartie != true) {
			// System.out.println("Tour : " + (i + 1));

			// Temps entre chaque tour
			// try {
			// TimeUnit.SECONDS.sleep(1);
			// } catch (InterruptedException e) {
			// e.printStackTrace();
			// }
			// Pour chaque robots :
			for (Robot robot : listeRobots) {
				// Temps entre chaque tour
				try {
					TimeUnit.MILLISECONDS.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				// Il faut que le robot soit vivant pour jouer
				if (!robot.isVivant()) {
					continue;
				}

				// On demande au robot de se d�placer
				robot.seDeplacer(grille);

				// On demande au robot d'attaquer une cible
				Robot cible = robot.attaquer(grille);

				// On regarde si le robot est mort
				gestionMortRobot(cible);
			}

			frame.repaint();

			finDePartie = VerifFinDePartie();
		}
		frame.repaint();
	}

	private boolean VerifFinDePartie() {
		int nbRobotVivant = 0;
		for (Robot robot : listeRobots) {
			if (robot.isVivant()) {
				nbRobotVivant += 1;
			}
		}

		if (nbRobotVivant == 1) {
			System.out.println("Fin de la partie");
			return true;
		}

		return false;
	}

	private void gestionMortRobot(Robot cible) {
		if (cible != null) {
			// Si le robot est mort
			if (!cible.isVivant()) {
				// On le retire de la grille
				grille.retirerRobot(cible);
			}
		}
	}

	private void sauvegardeEtatPlugin() {
		File f = new File(PATH_TO_FILE);
		PrintWriter writer;
		try {
			writer = new PrintWriter(f, "UTF-8");
			writer.println("Fin de partie");
			writer.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new Moteur(2, 4, 4);
	}
}
