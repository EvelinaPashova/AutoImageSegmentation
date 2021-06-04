
package lv.lumii.pixelmaster.modules.spw.gui;

import java.awt.Frame;
import java.io.File;
import java.io.IOException;
import javax.swing.*;

import lv.lumii.pixelmaster.modules.spw.domain.graph.GraphIO;
import lv.lumii.pixelmaster.modules.spw.domain.graph.UGraph;

/**
 * This class provides methods for 
 */
public class GraphIoDialog {
	private static JFileChooser openDLG=null, saveDLG=null;

	/** Creates file dialog in load mode */
	private static void createOpenDialog() {
		if (openDLG!=null) return;
		openDLG = new JFileChooser();
		openDLG.setLocation(400, 300);
	}

	/** Creates file dialog in save mode */
	private static void createSaveDialog() {
		if (saveDLG!=null) return;
		saveDLG = new JFileChooser();
		saveDLG.setLocation(400, 300);
	}

	/**
	 * Opens a file dialog in LOAD mode and loads graph from file.
	 * If coordinates of any vertex are not in bounds [0..maxX] and [0..maxY],
	 * null will be returned.
	 * If both maxX and maxY have value of 0, any coordinates are allowed.
	 *
	 * @param parent The parent frame. Can be null.
	 * @param maxX maximum x coordinate
	 * @param maxY maximum y coordinate
	 * @pre (maxX==0 and maxY==0) or (maxX>0 and maxY>0)
	 * @return Pointer to UGraph object, or null if failed to load graph
	 */
	public static UGraph loadGraph(Frame parent, int maxX, int maxY) {
		createOpenDialog();
		openDLG.setMultiSelectionEnabled(false);
		int returnVal = openDLG.showOpenDialog(parent);
		try {
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File f=openDLG.getSelectedFile();
				if (f==null) return null;
				UGraph tmp=GraphIO.read(f, maxX, maxY);
				if (tmp==null) throw new IOException();
				else return tmp;
			}
			else return null;
		}
		catch (IOException exc) {
			JOptionPane.showMessageDialog(parent,
				"Error occurred while reading from file."
			);
			return null;
		}
	}

	/**
	 * Opens a file dialog in SAVE mode and saves graph to file.
	 * @param parent The parent frame. Can be null.
	 * @param g graph to save
	 */
	public static void saveGraph(Frame parent, UGraph g) {
		createSaveDialog();
		saveDLG.setMultiSelectionEnabled(false);
		int returnVal = saveDLG.showSaveDialog(parent);
		if (returnVal != JFileChooser.APPROVE_OPTION) return;
		File f=saveDLG.getSelectedFile();
		if (f==null) return;
		String pathName=f.getPath();

		try {
			if (!GraphIO.write(g, f)) throw new IOException();
		}
		catch (IOException exc) {
			JOptionPane.showMessageDialog(parent,
				"Error occurred while writing to file."
			);
		}
	}
}
