package graphics;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import gui.VueMenuDuJeu;

@SuppressWarnings("serial")
public class PanelLancementDuJeu extends JPanel implements ActionListener {

	/** Vue principale */
	private VueMenuDuJeu vuePrincipale;

	/** Label de texte explicatif */
	private JLabel label;

	/** Panel pour choisir les plugins */
	private PanelChoixPlugins panelChoixPlugins;

	/** Bouton pour lancer la partie */
	private JButton bouton;

	/**
	 * Constructeur de la classe {@link PanelLancementDuJeu}
	 * 
	 * @param vue
	 */
	public PanelLancementDuJeu(VueMenuDuJeu vue) {
		super();
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

		vuePrincipale = vue;

		// Espace de s�paration
		add(Box.createRigidArea(new Dimension(0, 10)));

		// Label de texte
		label = new JLabel("Choisir les plugins : ");
		label.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(label);

		// Espace de s�paration
		add(Box.createRigidArea(new Dimension(0, 10)));

		// Panel pour choisir les plugins
		panelChoixPlugins = new PanelChoixPlugins(vuePrincipale);
		panelChoixPlugins.setAlignmentX(Component.CENTER_ALIGNMENT);
		JScrollPane sp = new JScrollPane(panelChoixPlugins);
		sp.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(sp);

		// Espace de s�paration
		add(Box.createRigidArea(new Dimension(0, 10)));

		// Bouton pour lancer la partie
		bouton = new JButton("Lancer la partie");
		bouton.addActionListener(this);
		bouton.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(bouton);

		// Espace de s�paration
		add(Box.createRigidArea(new Dimension(0, 10)));
	}

	@Override
	public void actionPerformed(ActionEvent action) {
		// Si on a cliqu� sur le bouton
		if (action.getSource() == bouton) {
			// Retourne la liste des plugins choisis
			ArrayList<String> listPluginsChoisis = panelChoixPlugins.getListPluginsChoisis();

			// On lance la partie avec les plugins choisis
			vuePrincipale.lancerLaPartie(listPluginsChoisis);
		}

	}

	/**
	 * Permet de modifier la liste des plugins affich�s
	 * 
	 * @param listPlugins
	 */
	public void modifierSelectionPlugins(ArrayList<String> listPlugins) {
		panelChoixPlugins.modifierListePlugins(listPlugins);

	}

}
