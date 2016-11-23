package plugins;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;

import graphics.Case;
import graphics.Grille;
import interfacesMoteur.ICase;
import interfacesMoteur.IGrille;
import interfacesMoteur.IRobot;
import interfacesPlugins.IPluginAttaque;
import interfacesPlugins.IPluginDeplacement;
import interfacesPlugins.IPluginGraphisme;
import main.Moteur;
import main.Robot;

/**
 * La classe qui permet de charger et d'utiliser les diff�rents plugins
 * 
 * @author Karl
 *
 */
public class Gestionnaire_Plugins {

	/** L'instance du plugin d'attaque */
	IPluginAttaque pluginAttaque;
	/** L'instance du plugin de d�placement */
	IPluginDeplacement pluginDeplacement;
	/** La liste des instances des plugins graphismes */
	ArrayList<IPluginGraphisme> listPluginsGraphisme;

	ArrayList<File> listPlugins;

	/** Chemin vers le fichier pour stocker l'�tat des plugins */
	public final File PATH_TO_FILE = new File(
			Moteur.class.getClassLoader().getResource("Sauvegarde_Etat_Plugins.txt").getFile());
	
	/**
	 * Constructeur de la classe {@link Gestionnaire_Plugins}
	 */
	public Gestionnaire_Plugins() {
		listPluginsGraphisme = new ArrayList<IPluginGraphisme>();
		listPlugins = new ArrayList<>();
	}

	public ArrayList<File> getListePluginsFromJar(File file) {
		// Liste des plugins
		listPlugins = new ArrayList<>();

		remplirListePlugins(file);
		//
		// // Si c'est un .jar ou un .zip
		// if (file.getAbsolutePath().endsWith(".jar") ||
		// file.getAbsolutePath().endsWith(".zip")) {
		//
		// ZipInputStream zip;
		//
		// try {
		// zip = new ZipInputStream(new FileInputStream(file));
		//
		// ZipEntry entry;
		// // Parcours de tous les �l�ments du jar
		// while ((entry = zip.getNextEntry()) != null) {
		// // Si c'est un .class
		// if (entry.getName().endsWith(".class")) {
		// // On ajoute le nom du plugins � la liste
		// listPlugins.add(entry.getName());
		// }
		// }
		// } catch (FileNotFoundException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

		// }
		return listPlugins;

	}

	private void remplirListePlugins(File file) {
		// Liste des fichiers dans le r�pertoire :
		File[] listFichiers = file.listFiles();

		// On parcourt cette liste de fichiers :
		for (File fichier : listFichiers) {
			// Si le fichier est un r�pertoire :
			if (fichier.isDirectory()) {
				// On rappelle la m�thode
				remplirListePlugins(fichier);
			} else
			// Si le fichier est un .class
			if (fichier.getAbsolutePath().endsWith(".class")) {
				// On ajoute le fichier � la liste des plugins
				listPlugins.add(fichier);
			}
		}

	}

	/**
	 * M�thode qui charge un plugin
	 * 
	 * @param chemin
	 *            nom du plugin
	 * @param typePlugin
	 *            type du plugin
	 */
	public boolean chargerPlugin(String chemin, TypePlugin typePlugin) {
		try {
			// On instantie le classeLoader
			MyClassLoader cl = new MyClassLoader(chemin);

			//
			File fichier = new File(chemin);

			// Nom du fichier
			String name = "plugins." + typePlugin.toString().toLowerCase() + "."
					+ fichier.getName().replace(".class", "");

			// On charge la classe
			Class<?> pluginClass = cl.findClass(name);

			// Si c'est un plugin d'attaque :
			if (typePlugin == TypePlugin.ATTAQUE) {
				// On stocke le plugin d'attaque
				pluginAttaque = (IPluginAttaque) pluginClass.newInstance();
			}

			// Si c'est un plugin de d�placement :
			if (typePlugin == TypePlugin.DEPLACEMENT) {
				// On stocke le plugin de d�placement
				pluginDeplacement = (IPluginDeplacement) pluginClass.newInstance();
			}

			// Si c'est un plugin de graphisme :
			if (typePlugin == TypePlugin.GRAPHISME) {
				// On stocke le plugin Graphisme avec les autres
				IPluginGraphisme plugin = (IPluginGraphisme) pluginClass.newInstance();

				// On l'ajoute dans la liste des plugins Graphisme
				listPluginsGraphisme.add(plugin);
			}
			return true;

		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			System.out.println("Flingu� barnini");
			e.printStackTrace();
			return false;
		}

	}

	/**
	 * M�thode qui retourne une couleur
	 * 
	 * @return
	 */
	public Color getCouleurRobot() {

		for (IPluginGraphisme plugin : listPluginsGraphisme) {
			// La m�thode du plugin qui permet de choisir la couleur du robot
			try {
				// On r
				Method[] methods = listPluginsGraphisme.get(0).getClass().getMethods();

				for (Method m : methods) {
					if (m.getName().equals("getCouleur")) {
						Color couleur = (Color) m.invoke(listPluginsGraphisme.get(0));
						return couleur;
					}
				}

			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}

		}

		return Color.WHITE;
	}

	/**
	 * M�thode qui permet au robot d'attaquer un autre robot
	 * 
	 * @param grille
	 */
	public Robot attaquer(Grille grille, Robot robot) {
		try {
			// La m�thode du plugin qui permet de choisir une cible
			Method m = pluginAttaque.getClass().getMethod("choisirCible", IGrille.class, IRobot.class);

			// Cette m�thode retourne un robot "cible"
			Robot robotCible = (Robot) m.invoke(pluginAttaque, grille, robot);

			// On retourne le robot choisit (null si aucun)
			return robotCible;

		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
				| SecurityException e) {
			e.printStackTrace();
		}
		// Le robot n'attaque pas
		return null;
	}

	/**
	 * M�thode qui permet au robot de se d�placer sur la grille
	 * 
	 * @param grille
	 */
	public Point seDeplacer(Grille grille, Robot robot) {
		try {
			// La m�thode du plugin qui permet de choisir un d�placement
			Method m = pluginDeplacement.getClass().getMethod("choisirDeplacement", IGrille.class, IRobot.class);

			// Point choisie par le plugin
			Point posChoisie = (java.awt.Point) m.invoke(pluginDeplacement, grille, robot);

			return posChoisie;
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			e.printStackTrace();
		}
		// On retourne sa position initiale
		return robot.getPosition();
	}

	/**
	 * M�thode qui permet de dessiner le robot
	 * 
	 * @param g
	 * @param caseRobot
	 */
	public void dessiner(Graphics g, Case caseRobot) {
		try {
			for (IPluginGraphisme plugin : listPluginsGraphisme) {
				Method m = plugin.getClass().getMethod("paint", Graphics.class, ICase.class);

				// M�thode qui va d�cider elle m�me le robot
				m.invoke(plugin, g, caseRobot);
			}
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}

	}

	public void setListPlugins(ArrayList<File> listPluginsChoisis) {
		listPlugins = listPluginsChoisis;

	}

	public ArrayList<File> getListPlugins() {
		return listPlugins;

	}
	
	/**
	 * Instancie les plugins et retourne une liste dans le meme format que le
	 * fichier de persistance avec uniquement les plugins s'�tant bien
	 * instanci�s
	 * 
	 * @return ArrayList<String>
	 */
	public ArrayList<String> parserLigneFichier(ArrayList<String> resultatFichier) {
		// On cr�� l'arraylist des plugins qui sont bien instanci�s
		ArrayList<String> pluginActivated = new ArrayList<String>();

		// On parcours ligne par ligne pour en suite parser
		for (String ligne : resultatFichier) {
			ArrayList<String> splitLigne = new ArrayList<String>(Arrays.asList(ligne.split(" ")));

			// Si le plugin a �t� sauvegard� � "true" on l'active
			if (Boolean.parseBoolean(splitLigne.get(2))) {

				// Chemin o� se trouve le plugin
				String chemin = splitLigne.get(0);

				// Type de plugins
				String tab[] = chemin.split("\\\\");
				TypePlugin typePlugin = TypePlugin.valueOf(tab[tab.length - 2].toUpperCase());

				// On charge le plugin suivant son type choisi pr�c�demment
				if (this.chargerPlugin(splitLigne.get(0), typePlugin)) {
					pluginActivated.add(ligne);
				} else {
					pluginActivated.add(splitLigne.get(0) + " " + splitLigne.get(1) + " false");
				}
				// Si le plugin est � false, on remet la m�me ligne dans le
				// fichier
			} else {
				pluginActivated.add(ligne);
			}
		}
		return pluginActivated;
	}

	/**
	 * Permet de retourner sous forme d'arraylist de string le contenu du
	 * fichier
	 * 
	 * @return ArrayList<String>
	 */
	public ArrayList<String> lectureFichier() {

		ArrayList<String> resultatFichier = new ArrayList<String>();

		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(this.PATH_TO_FILE));
			String line = null;
			// Tant qu'il y a de nouvelles lignes on continue
			while ((line = br.readLine()) != null) {
				System.out.println("#" + line);
				resultatFichier.add(line);
			}

			br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return resultatFichier;
	}
	
	/**
	 * Permet de sauvegarder l'�tat des plugins
	 * 
	 */
	public void sauvegardeEtatPlugin(ArrayList<String> toWrite) {
		PrintWriter writer;
		try {
			writer = new PrintWriter(this.PATH_TO_FILE, "UTF-8");

			// On �crit ligne par ligne les plugins dans le fichier
			for (String ligne : toWrite) {
				writer.println(ligne);
			}
			writer.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
