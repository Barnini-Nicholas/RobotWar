package graphics;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;

import gui.VueMenuDuJeu;

@SuppressWarnings("serial")
public class PanelChoixRepertoire extends JPanel implements ActionListener {

	/** Vue principale */
	private VueMenuDuJeu vuePrincipale;

	/** Label de texte */
	private JLabel label;

	/** Label indiquant le r�pertoire choisi */
	private JLabel labelRepChoisi;

	/** Texte du label */
	private static final String textLabel = "R�pertoire choisi : ";

	/** Bouton pour choisir un r�pertoire */
	private JButton bouton;

	/**
	 * Constructeur de la classe {@link PanelChoixRepertoire}
	 * 
	 * @param vue
	 */
	public PanelChoixRepertoire(VueMenuDuJeu vue) {
		super();
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

		vuePrincipale = vue;

		// Un label de texte
		label = new JLabel("Veuillez choisir le r�pertoire o� se trouve les plugins :");
		label.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(label);

		// Espace de s�paration
		add(Box.createRigidArea(new Dimension(0, 10)));

		// Un bouton pour choisir un r�pertire
		bouton = new JButton("Choix du r�pertoire");
		bouton.setAlignmentX(Component.CENTER_ALIGNMENT);
		bouton.addActionListener(this);
		add(bouton);

		// Espace de s�paration
		add(Box.createRigidArea(new Dimension(0, 10)));

		// Un label qui indique le r�pertoire choisi
		labelRepChoisi = new JLabel(textLabel);
		labelRepChoisi.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(labelRepChoisi);

	}

	/**
	 * Permet de choisir un r�pertoire d'o� extraire les plugins
	 * 
	 * @return String - Chemin vers le r�pertoire des plugins
	 */
	private String choisirRepertoirePluginsJAR() {
		JFileChooser jfc = new JFileChooser("D:\\TRAVAIL\\S7\\Prog_Avanc�e\\Repertoire_Test_JAR");
		jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int returnVal = jfc.showOpenDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			return jfc.getSelectedFile().getAbsolutePath();
		}
		return null;
	}

	@Override
	public void actionPerformed(ActionEvent action) {
		// Si on a cliqu� sur le bouton
		if (action.getSource() == bouton) {
			// On r�cup�re le r�pertoire choisit
			String repertoireChoisi = choisirRepertoirePluginsJAR();

			if (repertoireChoisi == null) {
				System.err.println("Probl�me de choix de r�pertoire");
			}
			// Modification du texte du label
			labelRepChoisi.setText(textLabel + repertoireChoisi);

			// On indique � la vue principale qu'on a s�lectionn� un r�pertoire
			vuePrincipale.modifierSelectionPlugins(repertoireChoisi);
		}

	}
}
