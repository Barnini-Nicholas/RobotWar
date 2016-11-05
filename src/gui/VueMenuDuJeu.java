package gui;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import graphics.PanelChoixRepertoire;
import graphics.PanelSelectionPlugins;
import main.Moteur;

@SuppressWarnings("serial")
public class VueMenuDuJeu extends JFrame {

	/** Titre de la fenetre */
	private static final String titreFenetre = "Menu du jeu";

	/** Panel principal */
	private JPanel panelPrincipal;

	/** Panel pour choisir le r�pertoire des plugins */
	private PanelChoixRepertoire panelChoixRep;

	/** Panel pour choisir les plugins � utiliser */
	private PanelSelectionPlugins panelSelectPlugins;

	/**
	 * Constructeur de la classe {@link VueMenuDuJeu}
	 */
	public VueMenuDuJeu() {
		super(titreFenetre);

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
		panelSelectPlugins = new PanelSelectionPlugins(this);

		panelPrincipal.add(panelSelectPlugins, BorderLayout.SOUTH);

	}

	/**
	 * M�thode qui modifie le panel de s�lection des plugins quand l'utilisateur
	 * � choisi un r�pertoire
	 * 
	 * @param repertoireChoisi
	 */
	public void modifierSelectionPlugins(String repertoireChoisi) {
		// TODO Auto-generated method stub

	}

	/**
	 * M�thode qui lance la partie
	 */
	public void lancerLaPartie() {

		// On ferme cette fenetre
		dispose();

		new Thread(new Runnable() {

			@Override
			public void run() {
				// On lance le moteur de jeu
				new Moteur(5, 10, 10, false);

			}
		}).start();
		;

	}

}
