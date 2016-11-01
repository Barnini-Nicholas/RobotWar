package main;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

/**
 * @author Barnini
 *
 */
public class Launcher {

	
	/**
	 *  Constructeur de la classe Launcher
	 */
	public Launcher() {

//		String repertoireChoisi = choisirRepertoirePluginsJAR();
//		if(repertoireChoisi == null) {
//			System.err.println("Probl�me de choix de r�pertoire");
//			//System.exit(-1);
//		}
//		System.out.println("R�pertoire choisi: " + repertoireChoisi);
		//initJARPluginsChooser(repertoireChoisi);
		new Moteur(2, 10, 10);
	}
	
	public void initJARPluginsChooser(String repertoireChoisi){
		
		JFrame jf = new JFrame();
		jf.setLocation(100, 100);
		jf.setSize(100, 100);
		jf.setTitle("Choisir les plugins � activer: ");
		jf.setVisible(true);
		jf.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
				new Moteur(2, 4, 4);
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				jf.setVisible(false);
			}
		});
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

	
	public static void main(String[] args) {
		new Launcher();
	}
}
