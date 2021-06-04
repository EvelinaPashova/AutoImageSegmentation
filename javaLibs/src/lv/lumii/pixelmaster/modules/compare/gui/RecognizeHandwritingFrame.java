package lv.lumii.pixelmaster.modules.compare.gui;

//import gui.components.forRidge.*;

import lv.lumii.pixelmaster.modules.compare.domain.WriteTextFile;
import lv.lumii.pixelmaster.core.api.gui.ImageViewer;
import lv.lumii.pixelmaster.core.api.gui.ImageIoDialog;
import lv.lumii.pixelmaster.modules.compare.domain.RecognizeText;
import lv.lumii.pixelmaster.modules.compare.domain.graph.OGraph;
import lv.lumii.pixelmaster.modules.recognition.domain.CutCharacter;
import lv.lumii.pixelmaster.core.api.domain.RasterImage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import lv.lumii.pixelmaster.core.api.domain.ImageConverter;

/**
 * Recognized handwriting text frame. Show recognized text.
 * @author: Madara Augstkalne
 * @version: 1.5
 * @since: 2010.27.03
 */

public class RecognizeHandwritingFrame extends JFrame{


	private ActionListener aListener;
	private RasterImage rOrigImage, rModImage, compImg;
	private JFrame thisFrame;

	private ImageViewer iv;
	private JPanel recTextPanel, bottomPanel, buttonPanel;
	private JButton exportButton, closeButton, changeButton, compareButton;	// ID: 1-3

    private JTextArea textArea;

	public RecognizeHandwritingFrame(RasterImage rImage) {
        super("Recognize handwriting text");
        try{
            assert !(rImage==null);
            setVisible(false);
            thisFrame=this;
            setSize(550, 500);
            setMinimumSize(new Dimension(300, 300));
            setLocation(200, 100);

            rOrigImage=new RasterImage(rImage);
          //  rOrigImage =ImageProcessor.printLine(rOrigImage, 11);
            createPanels();
            createComponents();
            addComponents();
            createListeners();
            addListeners();


            setResizable(true);
            setVisible(true);

            rModImage= CutCharacter.findBorders(rOrigImage);
        }

		catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "No image loaded.");
		}

	}

	protected void createPanels() {
		setLayout(new BorderLayout());

		iv=new ImageViewer(rOrigImage);
        add(iv, BorderLayout.CENTER);

        bottomPanel=new JPanel();
		bottomPanel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
		bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.PAGE_AXIS));
		add(bottomPanel, BorderLayout.SOUTH);

        recTextPanel=new JPanel();
        recTextPanel.setLayout(new BoxLayout(recTextPanel, BoxLayout.PAGE_AXIS));
        recTextPanel.setBorder(BorderFactory.createTitledBorder("Recognized text:"));
        bottomPanel.add(recTextPanel);

        buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
        bottomPanel.add(buttonPanel);

	}

	protected void createComponents() {
        compareButton = new JButton("Recognize text");
        compareButton.setActionCommand("Recognize text");

		exportButton=new JButton("Export text");
		exportButton.setActionCommand("Export text");

        changeButton=new JButton("Load picture");
		changeButton.setActionCommand("Load image");

		closeButton=new JButton("Close");
		closeButton.setActionCommand("Close");

        textArea = new JTextArea(" ", 5, 5);
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

	}

	protected void addComponents() {
        buttonPanel.add(compareButton);        
		buttonPanel.add(exportButton);
		buttonPanel.add(closeButton);
        buttonPanel.add(changeButton);
        recTextPanel.add(textArea);
	}

	protected void createListeners() {
		aListener=new axnListener();
	}

	protected void addListeners() {
		exportButton.addActionListener(aListener);
		closeButton.addActionListener(aListener);
        compareButton.addActionListener(aListener);
        changeButton.addActionListener(aListener);
	}

    private final class axnListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			if (e==null || e.getActionCommand()==null) return;
			if (e.getActionCommand().equalsIgnoreCase("Export text")) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setMultiSelectionEnabled(false);
				int returnVal = fileChooser.showSaveDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fileChooser.getSelectedFile();
					WriteTextFile output = new WriteTextFile(file);
					output.writeInFile(textArea.getText());
					output.closeFile();
				}
			}
			else if (e.getActionCommand().equalsIgnoreCase("Close")) {
				dispose();
			}
            else if (e.getActionCommand().equalsIgnoreCase("Recognize text")) {

               // RecognizeText recText = new RecognizeText();
                //recText.CompareLetter(rOrigImage, textArea, 9);

                try {
					StringBuffer info = new StringBuffer();
                    rOrigImage = RecognizeText.CompareSymbol(rOrigImage, info);
					textArea.setText(info.toString());
                } catch (OGraph.EdgelessGraphException e1) {
                    e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                } catch (OGraph.WeightlessGraphException e1) {
                    e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }

                iv.setImage(rOrigImage);
            }
            else if (e.getActionCommand().equalsIgnoreCase("Load image")) {
                rOrigImage=null;
                BufferedImage tmpBufImage=ImageIoDialog.loadImage(thisFrame);
                if (tmpBufImage==null) return;
                if (tmpBufImage.getWidth()<1 || tmpBufImage.getHeight()<1) {
                    JOptionPane.showMessageDialog(thisFrame, "Image too small");
                    return;
                }
                rOrigImage=new RasterImage(tmpBufImage.getWidth(), tmpBufImage.getHeight());

                ImageConverter.convertBufImage(tmpBufImage, rOrigImage);

              //  rModImage = CutCharacter.findBorders(rOrigImage);

                iv.setImage(rOrigImage);
                textArea.setText(null);
            }
		}
	}

}
       