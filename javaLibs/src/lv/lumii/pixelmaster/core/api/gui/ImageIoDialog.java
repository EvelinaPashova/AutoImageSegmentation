
package lv.lumii.pixelmaster.core.api.gui;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.util.zip.DataFormatException;
import java.util.Locale;
import lv.lumii.pixelmaster.core.api.domain.ImageConverter;

import lv.lumii.pixelmaster.core.api.domain.RasterImage;

/**
 * Class providing methods for image I/O.
 *
 * @author Jevgeny Jonas
 */
public final class ImageIoDialog {
	private static JFileChooser openDLG=null, saveDLG=null;
	
	/**
	 * Creates file dialog in load mode.
	 */
	private static void createOpenDialog() {
		if (openDLG!=null) return;
		openDLG = new JFileChooser();
		openDLG.setLocation(400, 300);
		openDLG.addChoosableFileFilter(new FileNameExtensionFilter("bmp", "bmp"));
		openDLG.addChoosableFileFilter(new FileNameExtensionFilter("jpg", "jpg", "jpeg"));
		openDLG.addChoosableFileFilter(new FileNameExtensionFilter("png", "png"));
		openDLG.addChoosableFileFilter(new FileNameExtensionFilter("gif", "gif"));
		openDLG.addChoosableFileFilter(new FileNameExtensionFilter("Images", "jpg", "jpeg", "bmp", "png", "gif"));
		//Locale locale=new Locale("eng");
		//openDLG.setLocale(locale.ENGLISH);
	}

	/**
	 * Returns file extension for specified file format.
	 * @param descr description of file format. Cannot be null. Must be equal to
	 *		"bmp", "grayscale bmp", "jpg", "jpeg", "gif" or "png".
	 * @return file extension. Cannot be null.
	 */
	private static String getExtByDescr(String descr) {
		if (descr.equals("bmp")) return "bmp";
		else if (descr.equals("grayscale bmp")) return("bmp");
		else if (descr.equals("jpg")) return("jpg");
		else if (descr.equals("jpeg")) return("jpeg");
		else if (descr.equals("gif")) return("gif");
		else if (descr.equals("png")) return("png");
		assert(false);
		return null;
	}

	/**
	 * Creates file dialog in save mode.
	 */
	private static void createSaveDialog() {
		if (saveDLG!=null) return;
		saveDLG = new JFileChooser();
		saveDLG.setLocation(400, 300);
		saveDLG.addChoosableFileFilter(new FileNameExtensionFilter("bmp", "bmp"));
		saveDLG.addChoosableFileFilter(new FileNameExtensionFilter("grayscale bmp", "bmp"));
		saveDLG.addChoosableFileFilter(new FileNameExtensionFilter("jpg", "jpg"));
		saveDLG.addChoosableFileFilter(new FileNameExtensionFilter("jpeg", "jpeg"));
		saveDLG.addChoosableFileFilter(new FileNameExtensionFilter("png", "png"));
		saveDLG.addChoosableFileFilter(new FileNameExtensionFilter("gif", "gif"));
		saveDLG.removeChoosableFileFilter(saveDLG.getAcceptAllFileFilter());
		//Locale locale=new Locale("eng");
		//saveDLG.setLocale(locale.ENGLISH);
	}

	/**
	 * Checks if file extension is supported by the program.
	 * @param extension file extension.
	 * @return false if file extension is not supported or is null.
	 */
	public static boolean supported(String extension) {
		if (extension != null) {
		if (extension.equals("gif") ||
			extension.equals("jpeg") ||
			extension.equals("jpg") ||
			extension.equals("bmp") ||
			extension.equals("png")) {
				return true;
			}
			else {
				return false;
			}
		}
		return false;
	}

	/**
	 * Opens a file dialog in LOAD mode and loads image from file.
	 * @param parent The parent frame. Can be null.
	 * @return Pointer to BufferedImage object, or null if failed to load image.
	 */
	public static BufferedImage loadImage(Frame parent) {
		createOpenDialog();
		openDLG.setMultiSelectionEnabled(false);
		int returnVal = openDLG.showOpenDialog(parent);
		try {
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File f=openDLG.getSelectedFile();
				if (f==null) return null;
				BufferedImage tmpBufImage=ImageIO.read(f);
				if (tmpBufImage==null) throw new IOException();
				else return tmpBufImage;
			}
			else return null;
		}
		catch (IOException exc) {
			//System.out.println("IOException caught in ImageIoDialog.loadImage: "+exc.getMessage());
			JOptionPane.showMessageDialog(parent,
				"Error occurred while reading from file."
			);
			return null;
		}
	}

	/**
	 * Opens a file dialog in LOAD mode and allows user to load multiple images from files.
	 * @return Pointer to BufferedImage object, or null if failed to load image.
	 */
	public static BufferedImage [] loadMultiple() {
		createOpenDialog();
		openDLG.setMultiSelectionEnabled(true);
		int returnVal = openDLG.showOpenDialog(null);
		File [] f = openDLG.getSelectedFiles();
		BufferedImage [] tmp=new BufferedImage [f.length];
		for (int i=0; i<f.length; i++)
			try {
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					if (f==null) return null;
					tmp[i]=ImageIO.read(f[i]);
					if (tmp[i]==null) throw new IOException();
				}
			}
			catch (IOException exc) {
				//System.out.println("IOException caught in ImageIoDialog.loadImage: "+exc.getMessage());
			/*	JOptionPane.showMessageDialog(null,
					"Error occured while reading from file."
				);*/
				tmp[i]=null;
			}
		return tmp;
	}

	/**
	 * Opens a file dialog in LOAD mode and allows user to load multiple images from files.
	 * @return Pointer to BufferedImage object, or null if failed to load image.
	 */
	public static File [] loadMultipleFile() {
		createOpenDialog();
		openDLG.setMultiSelectionEnabled(true);
		int returnVal = openDLG.showOpenDialog(null);
		File [] tmp = openDLG.getSelectedFiles();
		/*BufferedImage [] tmp=new BufferedImage [f.length];
		for (int i=0; i<f.length; i++)
			try {
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					if (f==null) return null;
					tmp[i]=ImageIO.read(f[i]);
					if (tmp[i]==null) throw new IOException();
				}
			}
			catch (IOException exc) {
				//System.out.println("IOException caught in ImageIoDialog.loadImage: "+exc.getMessage());
			/*	JOptionPane.showMessageDialog(null,
					"Error occured while reading from file."
				);*/
				/*tmp[i]=null;
			}   */
		return tmp;
	}

	/**
	 * Returns file extension.
	 * @param fileName File name.
	 * @return File extension (null if pathName is null or pathName does not contain character '.').
	 */
	public static String getExtension(String fileName) {
		if (fileName==null) return null;
		int dot=fileName.lastIndexOf('.');
		if (dot==-1) return null;
		return fileName.substring(dot+1);
	}

	/**
	 * Opens a file dialog in SAVE mode and saves image to file.
	 * @param parent The parent frame.
	 * @param rImage Image to save.
	 */
	public static void saveImage(Frame parent, RasterImage rImage) {
		createSaveDialog();
		saveDLG.setMultiSelectionEnabled(false);
		int returnVal = saveDLG.showSaveDialog(parent);
		if (returnVal != JFileChooser.APPROVE_OPTION) return;
		File f=saveDLG.getSelectedFile();
		if (f==null) return;
		String ext = getExtension(f.getPath());
		String descr=saveDLG.getFileFilter().getDescription();
		String pathName=f.getPath();

		if (ext==null) {
			pathName=pathName.concat("."+getExtByDescr(descr));
		}
		else if ("".equals(ext)) {
			pathName=pathName.concat(getExtByDescr(descr));
		}
		else if ("bmp".equals(ext)) {
			if (!(descr.equals("bmp") || descr.equals("grayscale bmp"))) {
				pathName=pathName.substring(0, pathName.length()-3);
				pathName=pathName.concat(getExtByDescr(descr));
			}
		}
		else if ("jpg".equals(ext)) {
			if (!descr.equals("jpg")) {
				pathName=pathName.substring(0, pathName.length()-3);
				pathName=pathName.concat(getExtByDescr(descr));
			}
		}
		else if ("jpeg".equals(ext)) {
			if (!descr.equals("jpeg")) {
				pathName=pathName.substring(0, pathName.length()-4);
				pathName=pathName.concat(getExtByDescr(descr));
			}
		}
		else if ("png".equals(ext)) {
			if (!descr.equals("png")) {
				pathName=pathName.substring(0, pathName.length()-3);
				pathName=pathName.concat(getExtByDescr(descr));
			}
		}
		else if ("gif".equals(ext)) {
			if (!descr.equals("gif")) {
				pathName=pathName.substring(0, pathName.length()-3);
				pathName=pathName.concat(getExtByDescr(descr));
			}
		}
		else pathName=pathName.concat("."+getExtByDescr(descr));
		f=new File(pathName);
		ext=getExtension(pathName);
		
		try {
			BufferedImage tmp;
			if (saveDLG.getFileFilter().getDescription().equals("grayscale bmp"))
				tmp=ImageConverter.toGrayscaleBuffered(rImage);
			else tmp=ImageConverter.toBuffered(rImage);
		//	System.out.println(pathName);
			if (!ImageIO.write(tmp, ext, f)) throw new DataFormatException();
		}
		catch (DataFormatException exc) {
			JOptionPane.showMessageDialog(parent,
				"Unknown file format."
			);
		}
		catch (IOException exc) {
			JOptionPane.showMessageDialog(parent,
				"Error occurred while writing to file."
			);
		}
	}
}
