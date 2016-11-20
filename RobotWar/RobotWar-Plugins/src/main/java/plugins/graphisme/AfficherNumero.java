package plugins.graphisme;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JLabel;

import interfacesMoteur.ICase;
import interfacesPlugins.IPluginGraphisme;

/**
 * Classe permettant de diff�rencier les robot en affichant dans un coin de la
 * case le num�ro
 * 
 * @author Nicho
 *
 */
public class AfficherNumero implements IPluginGraphisme {

	public void paint(Graphics g, ICase c) {

		// Si la case contient un robot on ajoute son indice et on
		// sp�cifie la position du texte
		if (c.getRobot() != null) {
			c.setText(c.getRobot().getIndice() + "");
			c.setVerticalAlignment(JLabel.BOTTOM);
			c.setHorizontalAlignment(JLabel.LEFT);
			
			// On r�cup�re la couleur du robot
			Color background = c.getRobot().getCouleur();

			// Suivant la couleur du background on modifie la couleur du text
			if ((background.getRed() + background.getGreen() + background.getBlue()) < 383) {
				c.setForeground(Color.white);
			} else {
				c.setForeground(Color.black);
			}

		}
	}

}
