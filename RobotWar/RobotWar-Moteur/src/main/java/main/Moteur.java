package main;

import java.awt.Point;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import javax.swing.JFrame;
import javax.swing.filechooser.FileSystemView;

import graphics.Grille;
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

	/** Regain d'energie par tour **/
	private final int energie = 50;

	/** Chemin vers le fichier pour stocker les logs */
	private final String LOG_FILE = "Log_File_RobotWar.txt";

	/** Gestionnaire des plugins */
	private Gestionnaire_Plugins gestionnairePlugins;

	/**
	 * Constructeur de la classe Moteur
	 * 
	 * @param nbRobots
	 *            nombre de robots � placer
	 * @param gestionnairePlugins2
	 */
	public Moteur(int nbRobots, int xGrille, int yGrille, boolean isLog, Gestionnaire_Plugins gestionnairePlugins) {

		// On instancie le gestionnaire de plugins
		this.gestionnairePlugins = gestionnairePlugins;

		// Si on active les logs on redirige la sortie console
		if (isLog) {
			try {
				// Utilisation de param�tres permettant la portabilit�s de
				// l'application
				System.setOut(
						new PrintStream(new File(FileSystemView.getFileSystemView().getDefaultDirectory().getPath()
								+ File.separator + LOG_FILE)));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

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

		// On r�cup�re la liste des plugins qui ont �t� choisis
		ArrayList<File> listPluginsChoisis = gestionnairePlugins.getListPlugins();

		ArrayList<String> pluginsAActiver = new ArrayList<>();

		for (File fichier : listPluginsChoisis) {
			String chemin = fichier.getAbsolutePath();
			
			String pattern = Pattern.quote(System.getProperty("file.separator"));
			String tab[] = chemin.split(pattern);
			
			TypePlugin typePlugin = TypePlugin.valueOf(tab[tab.length - 2].toUpperCase());

			String ligne = fichier.getAbsolutePath() + " " + typePlugin + " true";

			pluginsAActiver.add(ligne);
		}

		// Sauvegarde des plugins qui ont bien �t� instanci�s
		gestionnairePlugins.sauvegardeEtatPlugin(pluginsAActiver);

		// On appelle lectureFichier qui permet de renvoyer le
		// fichier sous forme d'arraylist de String
		// ArrayList<String> resultatFichier = lectureFichier();

		// On instancie les plugins, et on a en retour les plugins activ�s
		ArrayList<String> pluginActiver = gestionnairePlugins.parserLigneFichier(pluginsAActiver);

		// Sauvegarde des plugins qui ont bien �t� instanci�s
		gestionnairePlugins.sauvegardeEtatPlugin(pluginActiver);

	}


	/**
	 * M�thode qui cr�e un grille de jeu
	 */
	private void creationDeLaGrilleDeJeu(int xGrille, int yGrille) {
		grille = new Grille(xGrille, yGrille);

		// TEST : On place la grille dans une JFrame
		frame = new JFrame("RobotWar");
		frame.setContentPane(grille);
		frame.setVisible(true);
		frame.setSize(500, 500);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
				// sauvegardeEtatPlugin();
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
		int compteur = 0;

		// Boucle pour chaque tour de jeu :
		while (finDePartie != true) {
			compteur++;
			frame.setTitle("RobotWar - Tours : " + compteur);
			// Pour chaque robots :
			for (Robot robot : listeRobots) {
				// Temps entre chaque tour
				try {
					TimeUnit.MILLISECONDS.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				// Il faut que le robot soit vivant pour jouer
				if (!robot.isVivant()) {
					continue;
				}

				// Boucle pour le regain d'�nergie
				int x;
				if ((x = robot.getPtEnergie() + energie) > 100)
					robot.setPtEnergie(100);
				else
					robot.setPtEnergie(x);

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
			frame.setTitle("RobotWar - Fin de partie");
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
				System.out.println("Mort du robot : " + cible.getIndice());
			}
		}
	}


}
