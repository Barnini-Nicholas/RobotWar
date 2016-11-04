package plugins.graphisme;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

import gui.Case;

@SuppressWarnings("serial")
public class Image extends Graphisme_de_Base {

	public void dessiner(Case c, Graphics g) {
		paint(g, c);
	}

	public Color getCouleur() {
		Random rand = new Random();
		float r = rand.nextFloat();
		float g = rand.nextFloat();
		float b = rand.nextFloat();

		return new Color(r, g, b);
	}

	/**
	 * M�thode paintComponent de {@link Case}
	 * 
	 * @param c
	 */
	public void paint(Graphics g, Case c) {
		super.paint(g);

		c.setOpaque(true);
		c.setBackground(c.getRobot().getCouleur());

		g.setColor(Color.black);

		 try{
             
	            BufferedImage img = ImageIO.read(new File("src/ressources/bb8.png"));
	            g.drawImage(img, c.getWidth()/3, c.getHeight()/3, null);
	        } catch(IOException e){
	            e.printStackTrace();
	        }
	             }
		

	}

