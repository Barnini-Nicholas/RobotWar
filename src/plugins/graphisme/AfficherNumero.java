package plugins.graphisme;

import java.awt.Graphics;

import javax.swing.JLabel;

import gui.Case;

/**
 * Classe permettant de diff�rencier les robot en affichant dans un coin de la
 * case le num�ro
 * 
 * @author Nicho
 *
 */
@SuppressWarnings("serial")
public class AfficherNumero extends Graphisme_de_Base {

	public void paint(Graphics g, Case c) {
		super.paint(g);

		// Si la case contient un robot on ajoute son indice et on
		// sp�cifie la position du texte
		if (c.getRobot() != null) {
			c.setText(c.getRobot().getIndice() + "");
			c.setHorizontalTextPosition(JLabel.LEFT);
			c.setVerticalTextPosition(JLabel.BOTTOM);
		}
	}
}
