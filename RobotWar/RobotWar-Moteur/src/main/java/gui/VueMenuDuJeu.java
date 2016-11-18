package gui;

import java.awt.BorderLayout;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

import graphics.PanelChoixRepertoire;
import graphics.PanelLancementDuJeu;
import main.Moteur;
import plugins.Gestionnaire_Plugins;

@SuppressWarnings("serial")
public class VueMenuDuJeu extends JFrame {

	/** Titre de la fenetre */
	private static final String titreFenetre = "Menu du jeu";

	/** Panel principal */
	private JPanel panelPrincipal;

	/** Panel pour choisir le r�pertoire des plugins */
	private PanelChoixRepertoire panelChoixRep;

	/** Panel pour choisir les plugins � utiliser */
	private PanelLancementDuJeu panelLancementDuJeu;

	private Gestionnaire_Plugins gestionnairePlugins;

	/**
	 * Constructeur de la classe {@link VueMenuDuJeu}
	 */
	public VueMenuDuJeu() {
		super(titreFenetre);

		gestionnairePlugins = new Gestionnaire_Plugins();

		initialisationDuPanelPrincipal();

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(500, 500);
		setVisible(true);

	}

	/**
	 * M�thode qui initialise le panelPrincipal et ses �l�ments
	 */
	private void initialisationDuPanelPrincipal() {
		// Cr�ation du panel principal
		panelPrincipal = new JPanel(new BorderLayout());

		// Ajout de ce panel � la JFrame
		setContentPane(panelPrincipal);

		initialisationDuPanelChoixRepertoire();

		initialisationDuPanelSelectionDesPlugins();

	}

	/**
	 * M�thode qui initialise le panel pour choisir un r�pertoire
	 */
	private void initialisationDuPanelChoixRepertoire() {
		panelChoixRep = new PanelChoixRepertoire(this);

		panelPrincipal.add(panelChoixRep, BorderLayout.NORTH);

	}

	/**
	 * M�thode qui initialise le panel pour choisir les plugins � utiliser
	 */
	private void initialisationDuPanelSelectionDesPlugins() {
		panelLancementDuJeu = new PanelLancementDuJeu(this);

		panelPrincipal.add(panelLancementDuJeu, BorderLayout.CENTER);

	}

	/**
	 * M�thode qui modifie le panel de s�lection des plugins quand l'utilisateur
	 * � choisi un r�pertoire
	 * 
	 * @param repertoireChoisi
	 */
	public void modifierSelectionPlugins(File jarChoisi) {
		ArrayList<File> listPlugins = gestionnairePlugins.getListePluginsFromJar(jarChoisi);

		panelLancementDuJeu.modifierSelectionPlugins(listPlugins);

	}

	/**
	 * M�thode qui lance la partie
	 * 
	 * @param listPluginsChoisis
	 */
	public void lancerLaPartie(ArrayList<String> listPluginsChoisis) {

		ArrayList<File> listFile = new ArrayList();

		// On transforme la liste des noms en list de File
		for (String chemin : listPluginsChoisis) {
			listFile.add(new File(chemin));
		}

		// On indique au gestionnaire les plugins � charger
		gestionnairePlugins.setListPlugins(listFile);

		// On ferme cette fenetre
		dispose();

		new Thread(new Runnable() {

			@Override
			public void run() {
				// On lance le moteur de jeu
				new Moteur(90, 10, 10, false, gestionnairePlugins);

			}
		}).start();
		;

	}

}
