package lv.lumii.pixelmaster.modules.compare.gui;

import lv.lumii.pixelmaster.core.api.gui.ImageViewer;
import lv.lumii.pixelmaster.core.api.gui.ImageIoDialog;
import lv.lumii.pixelmaster.modules.filters.domain.MeanFilter;
import lv.lumii.pixelmaster.modules.filters.domain.MedianFilter;
import lv.lumii.pixelmaster.modules.compare.domain.CompareByPixelBlocks;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import javax.swing.*;
import lv.lumii.pixelmaster.core.api.domain.ImageConverter;
import lv.lumii.pixelmaster.core.api.domain.RasterImage;

/**
 *
 * @author Onty
 */
public class CompareByPixelBlocksFrame extends JFrame {


    private ActionListener aListener;
	private RasterImage compareIm=null, resultIm=null, sourceIm;
	private JFrame thisFrame;
	private ImageViewer iv;
	private JPanel rightPanel, rightTopPanel, comparePanel, ImagePanel;
	private JButton closeButton, compareButton, loadButton, saveButton, filterButton;
	private ButtonGroup buttonGroup;									
	private JRadioButton radioButton0, radioButton1, radioButton2;
    private JTextArea CompareInfo;

    public CompareByPixelBlocksFrame(RasterImage source) {
        super("Compare Image");
		try {
            setVisible(false);
            thisFrame=this;
            setMinimumSize(new Dimension(700, 450));
            setExtendedState(JFrame.MAXIMIZED_BOTH);

            
            sourceIm=new RasterImage(source);
            createPanels();
            createComponents();
            addComponents();
            addaListener();
            setVisible(true);

            }
		catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "No image loaded.");
		}
    }

    protected void createPanels() {
		
        setLayout(new BorderLayout());
		iv=new ImageViewer(sourceIm);
		rightPanel=new JPanel(new BorderLayout());
		add(iv, BorderLayout.CENTER);
		add(rightPanel, BorderLayout.EAST);

		rightTopPanel=new JPanel();
		rightTopPanel.setLayout(new BoxLayout(rightTopPanel, BoxLayout.PAGE_AXIS));
		rightTopPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		rightPanel.add(rightTopPanel, BorderLayout.NORTH);

		ImagePanel = new JPanel(new GridLayout(0, 1));
        ImagePanel.setBorder(BorderFactory.createTitledBorder("Image Selection"));
		rightTopPanel.add(ImagePanel,BorderLayout.CENTER);

        comparePanel=new JPanel();
		comparePanel.setLayout(new BoxLayout(comparePanel, BoxLayout.PAGE_AXIS));
		comparePanel.setBorder(BorderFactory.createTitledBorder("Compare"));
		rightTopPanel.add(comparePanel);
		
	}


	protected void createComponents() {
		aListener=new aListener();

		buttonGroup=new ButtonGroup();
		radioButton0=new JRadioButton("Source Image", true);
        radioButton1=new JRadioButton("Compare Image", false);
		radioButton2=new JRadioButton("Result Image", false);
		buttonGroup.add(radioButton0);
        buttonGroup.add(radioButton1);
		buttonGroup.add(radioButton2);
        
        loadButton = new JButton("Load");
        loadButton.setAlignmentX(CENTER_ALIGNMENT);
        saveButton = new JButton("Save result");
        saveButton.setAlignmentX(CENTER_ALIGNMENT);

        CompareInfo = new JTextArea("Results will be \nshown here:");
        CompareInfo.setEditable(false);
        CompareInfo.setColumns(10);
        CompareInfo.setRows(10);
        CompareInfo.setAlignmentX(CENTER_ALIGNMENT);

        compareButton=new JButton("Compare");
		compareButton.setActionCommand("Compare");
		compareButton.setAlignmentX(CENTER_ALIGNMENT);

        filterButton = new JButton("Filter");
        filterButton.setActionCommand("Filter");
        filterButton.setAlignmentX(CENTER_ALIGNMENT);
        
        closeButton=new JButton("Close");
		closeButton.setActionCommand("Close");
        closeButton.setAlignmentX(CENTER_ALIGNMENT);
        
		
	}

	protected void addComponents() {
				
		ImagePanel.add(radioButton0);
        ImagePanel.add(radioButton1);
		ImagePanel.add(radioButton2);
        ImagePanel.add(loadButton);
        ImagePanel.add(saveButton);
		
        comparePanel.add(CompareInfo);
        comparePanel.add(compareButton);
        comparePanel.add(filterButton);
        comparePanel.add(closeButton);

	}

    protected void addaListener(){

        radioButton0.addActionListener(aListener);
        radioButton1.addActionListener(aListener);
        radioButton2.addActionListener(aListener);

        loadButton.addActionListener(aListener);
        saveButton.addActionListener(aListener);

        compareButton.addActionListener(aListener);
        filterButton.addActionListener(aListener);
        closeButton.addActionListener(aListener);
       
    }

    private class aListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand().equalsIgnoreCase("Close")) {
				dispose();
			}

            if (e.getActionCommand().equalsIgnoreCase("Compare")) {
				if (compareIm!=null){
					StringBuffer info = new StringBuffer();
                    resultIm = CompareByPixelBlocks.CompareObj(sourceIm,compareIm, info);
					CompareInfo.setText(info.toString());
                    iv.setImage(resultIm);
                    radioButton2.setSelected(true);
                }else{
                    JOptionPane.showMessageDialog(null, "Load secondary image first");
                    radioButton0.setSelected(true);
                }
			}
            
            if ( e.getSource() == radioButton0 )
            {
                if (sourceIm!=null){
                    iv.setImage(sourceIm);                   
                }else{
                    JOptionPane.showMessageDialog(null, "Image not loaded");
                    iv.setImage(sourceIm);
                    radioButton0.setSelected(true);
                }                   
                
            }

            if ( e.getSource() == radioButton1 )
            {
                if (compareIm!=null){
                    iv.setImage(compareIm);                   
                }else{

                    iv.setImage(sourceIm);
                    radioButton0.setSelected(true);
                    JOptionPane.showMessageDialog(null, "Image not loaded");
                }
                
            }

            if ( e.getSource() == radioButton2 )
            {
                if (resultIm!=null){
                    iv.setImage(resultIm);
                }else{
                    iv.setImage(sourceIm);
                    radioButton0.setSelected(true);
                    JOptionPane.showMessageDialog(null, "Compare result can't be shown before compare function is called");
                }

            }

            if (e.getActionCommand().equalsIgnoreCase("Load")){
                compareIm=null;
				String[] a = {null};
                BufferedImage tmpBufImage=ImageIoDialog.loadImage(null);
                if (tmpBufImage==null) return;
                if (tmpBufImage.getWidth()<1 || tmpBufImage.getHeight()<1) {
                    JOptionPane.showMessageDialog(thisFrame, "Image too small");
                    return;
                }
                compareIm=new RasterImage(tmpBufImage.getWidth(), tmpBufImage.getHeight());
                ImageConverter.convertBufImage(tmpBufImage, compareIm);
                iv.setImage(compareIm);
                radioButton1.setSelected(true);

            }
            if (e.getActionCommand().equalsIgnoreCase("Save result")){
                if (resultIm!=null){
                    ImageIoDialog.saveImage(thisFrame, resultIm);
                }else{
                    JOptionPane.showMessageDialog(thisFrame, "Result can't be saved before acquired");
                }
            }
            if (e.getActionCommand().equalsIgnoreCase("Filter")){
                RasterImage tmp=null;
                if (radioButton0.isSelected()==true){
                    tmp = new RasterImage(sourceIm);
                    lv.lumii.pixelmaster.modules.compare.domain.medFilter.medFilter(sourceIm, tmp, 1, 1);
                    iv.setImage(tmp);
                }else if (radioButton1.isSelected()==true){
                    tmp=new RasterImage(compareIm);
                    lv.lumii.pixelmaster.modules.compare.domain.medFilter.medFilter(compareIm, tmp, 1, 1);
                    iv.setImage(tmp);
                }else if (radioButton2.isSelected()==true){
                    tmp=new RasterImage(resultIm);
                    lv.lumii.pixelmaster.modules.compare.domain.medFilter.medFilter(resultIm, tmp, 1, 1);
                    iv.setImage(tmp);
                }
            }



		}
        public void windowClosing(WindowEvent e) {
        System.exit(0);
      }
	}



}
