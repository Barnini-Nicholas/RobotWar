package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;

import main.Robot;
import plugins.graphisme.Graphisme_de_Base;

/**
 * Classe représentant une case de la grille
 * 
 * @author Barnini Nicholas
 *
 */
@SuppressWarnings("serial")
public class Case extends JLabel {

	private Robot robot;

	/**
	 * Constructeur de la classe {@link Case}
	 */
	public Case() {
		super();
		this.setRobot(null);

		setBorder(BorderFactory.createLineBorder(Color.black));
	}

	public Robot getRobot() {
		return robot;
	}

	public void setRobot(Robot robot) {
		this.robot = robot;
	}

	/**
	 * Méthode paint de {@link Case}
	 */
	@Override
	public void paint(Graphics g) {
		super.paint(g);

		if (this.getRobot() != null) {
			robot.dessiner(g, this);
		}

		else {
			setBackground(null);
		}
	}

}
