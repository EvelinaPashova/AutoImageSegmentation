package lv.lumii.pixelmaster.modules.compare.gui;


import lv.lumii.pixelmaster.core.api.gui.ImageViewer;
import lv.lumii.pixelmaster.core.api.gui.ImageIoDialog;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.*;

import lv.lumii.pixelmaster.modules.compare.domain.CompareUsingGraphs;
import lv.lumii.pixelmaster.core.api.domain.ImageConverter;
import lv.lumii.pixelmaster.core.api.domain.RasterImage;

/**
 *
 * @author Onty
 */
public class CompareUsingGraphsFrame extends JFrame {


    private ActionListener aListener;
	private RasterImage compareIm = null, resultIm = null, sourceIm = null;
    private double resultD;
	private JFrame thisFrame;
	private ImageViewer ivsource, ivtarget, ivresult;
	private JPanel rightPanel, rightTopPanel, comparePanel, ImagePanel, leftPanel;
    private JToolBar sourceBar, compareBar, resultBar;
    private JComboBox ridgeComboBox;
	private JButton closeButton, compareButton, loadSourceButton, loadCompareButton, saveButton, filterButton;
	private ButtonGroup buttonGroup;
	private JRadioButton radioButton0, radioButton1, radioButton2;
    private JTextArea CompareInfo, compareInfoBar;
    private JScrollPane scrollPane;
    private MissingIcon placeholderIcon = new MissingIcon();
    private File tmpBufImage[], tmpCompareBufImage[];
    private RasterImage resultTmp[];

    private String[] imageCaptions = {};

    private String imagedir = "";

    private String[] imageFileNames = {};

    public CompareUsingGraphsFrame() {
        super("Compare Image");
		try {
            setVisible(false);
            thisFrame=this;
            setMinimumSize(new Dimension(700, 450));
            setExtendedState(JFrame.MAXIMIZED_BOTH);


            //sourceIm=new RasterImage(source);
            createPanels();
            createComponents();
            addComponents();
            addaListener();
            setVisible(true);

            }
		catch (Exception e) {
			e.printStackTrace();
			//JOptionPane.showMessageDialog(this, "No image loaded.");
		}
    }

    protected void createPanels() {

        setLayout(new BorderLayout());

        leftPanel = new JPanel(new GridLayout(3,1));
        leftPanel.setBorder(BorderFactory.createTitledBorder("Images"));
        leftPanel.setMinimumSize(new Dimension(1000,700));
        leftPanel.setMaximumSize(new Dimension(1000,700));
        leftPanel.setPreferredSize(new Dimension(1000,700));
        leftPanel.setSize(new Dimension(1000,700));
        leftPanel.setAutoscrolls(true);
        add(leftPanel, BorderLayout.WEST);

		//ivsource = new ImageViewer(sourceIm, true);
        //ivtarget = new ImageViewer(compareIm, true);
        //ivresult = new ImageViewer(resultIm, true);

        //leftPanel.add(ivsource);
        //leftPanel.add(ivtarget);
        //leftPanel.add(ivresult);

        sourceBar = new JToolBar();
        sourceBar.setBorder(BorderFactory.createTitledBorder("Source image(s)"));
        sourceBar.setMaximumSize(new Dimension(800,600));
        sourceBar.setSize(new Dimension(800,200));
        sourceBar.setAutoscrolls(true);

        sourceBar.add(Box.createGlue());
        sourceBar.add(Box.createGlue());

        JScrollPane sourceScrollPane = new JScrollPane(sourceBar);

        leftPanel.add(sourceScrollPane);

        compareBar = new JToolBar();
        compareBar.setBorder(BorderFactory.createTitledBorder("Compare image(s)"));
        compareBar.setMaximumSize(new Dimension(800,600));
        compareBar.setSize(new Dimension(800,200));
        compareBar.setAutoscrolls(true);

        compareBar.add(Box.createGlue());
        compareBar.add(Box.createGlue());

        JScrollPane compareScrollPane = new JScrollPane(compareBar);

        leftPanel.add(compareScrollPane);

        resultBar = new JToolBar();
        resultBar.setBorder(BorderFactory.createTitledBorder("Result image(s)"));
        resultBar.setMaximumSize(new Dimension(800,600));
        resultBar.setSize(new Dimension(800,200));
        resultBar.setAutoscrolls(true);

        resultBar.add(Box.createGlue());
        resultBar.add(Box.createGlue());

        JScrollPane resultScrollPane = new JScrollPane(resultBar);

        leftPanel.add(resultScrollPane);

        rightPanel = new JPanel(new BorderLayout());
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

        loadSourceButton = new JButton("Load source");
        loadSourceButton.setAlignmentX(CENTER_ALIGNMENT);
        loadCompareButton = new JButton("Load compare");
        loadCompareButton.setAlignmentX(CENTER_ALIGNMENT);
        saveButton = new JButton("Save result");
        saveButton.setAlignmentX(CENTER_ALIGNMENT);

        CompareInfo = new JTextArea("Results will be \nshown here:");
        CompareInfo.setEditable(false);
        CompareInfo.setColumns(10);
        CompareInfo.setRows(10);
        CompareInfo.setAlignmentX(CENTER_ALIGNMENT);
        scrollPane = new JScrollPane(CompareInfo);
        scrollPane.setAlignmentX(CENTER_ALIGNMENT);


        String[] ridgeMenu = {
            "TraceLines Ridge",
            "TraceLines Valley",
            "TraceLines Edge",
            "Draw line segments: Ridge",
            "Draw line segments: Valley",
            "Draw line segments: Edge",
            "Concatenate segments: Ridge",
            "Concatenate segments: Valley",
            "Concatenate segments: Edge",
            "Cluster: Ridge",
            "Cluster: Valley",
            "Cluster: Edge",
        };
        ridgeComboBox = new JComboBox(ridgeMenu);
        ridgeComboBox.setActionCommand("ridgeMenu");
        ridgeComboBox.setAlignmentX(CENTER_ALIGNMENT);

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
        ImagePanel.add(loadSourceButton);
        ImagePanel.add(loadCompareButton);
        ImagePanel.add(saveButton);

        comparePanel.add(scrollPane);
        comparePanel.add(ridgeComboBox);
        comparePanel.add(compareButton);
        comparePanel.add(filterButton);
        comparePanel.add(closeButton);

	}

    protected void addaListener(){

        radioButton0.addActionListener(aListener);
        radioButton1.addActionListener(aListener);
        radioButton2.addActionListener(aListener);

        loadSourceButton.addActionListener(aListener);
        loadCompareButton.addActionListener(aListener);
        saveButton.addActionListener(aListener);

        ridgeComboBox.addActionListener(aListener);
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
                
				if (sourceIm != null && (tmpCompareBufImage.length != 0)) {

                    //TODO: make compare via lines
                    int command = ridgeComboBox.getSelectedIndex();

                    BufferedImage [] tmp = new BufferedImage [tmpCompareBufImage.length];

                    resultTmp = new RasterImage [tmpCompareBufImage.length];

                    CompareInfo.setText(null);
                    //foreach compare image, do:

                    for (int i=0; i<(tmpCompareBufImage.length); i++)
                    {
                        try {
                            tmp[i] = ImageIO.read(tmpCompareBufImage[i]);
                            compareIm = new RasterImage(tmp[i].getWidth(), tmp[i].getHeight());
                            ImageConverter.convertBufImage(tmp[i], compareIm);
							StringBuffer info = new StringBuffer();
                            resultTmp[i] = CompareUsingGraphs.CompareObj(sourceIm, compareIm, info, command+1);
							CompareInfo.setText(info.toString());
                            //i++;
                            //resultTmp[i] = CompareUsingGraphs.CompareObj2(sourceIm, compareIm, CompareInfo, command+1);
                        } catch (IOException e1) {
                            e1.printStackTrace(); 
                        }
                    }

                    resultBar.setVisible(false);
                    resultBar.removeAll();
                    resultBar.add(Box.createGlue());
                    resultBar.add(Box.createGlue());

                    try {
                        loadResultImages(0,5);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }

                    resultBar.setVisible(true);
                    //loadResultImages.execute();
                    //resultIm = CompareUsingGraphs.CompareObj(sourceIm,compareIm, CompareInfo, command+1);
                    //TODO: compare visualization
                    //resultIm = ImageProcessor.printLine(sourceIm, command-3);
                    //resultIm = ImageProcessor.printLine(compareIm, command-3);
                    //ivresult.setImage(resultIm);
                    radioButton2.setSelected(true);
                }else{
                    JOptionPane.showMessageDialog(null, "Select source image first");
                    radioButton0.setSelected(true);
                }
			}

            if ( e.getSource() == radioButton0 )
            {
                if (sourceIm!=null){
                    ivsource.setImage(sourceIm);
                }else{
                    JOptionPane.showMessageDialog(null, "Image not loaded");
                    ivsource.setImage(sourceIm);
                    radioButton0.setSelected(true);
                }

            }

            if ( e.getSource() == radioButton1 )
            {
                if (compareIm!=null){
                    ivtarget.setImage(compareIm);
                }else{

                    ivtarget.setImage(sourceIm);
                    radioButton0.setSelected(true);
                    JOptionPane.showMessageDialog(null, "Image not loaded");
                }

            }

            if ( e.getSource() == radioButton2 )
            {
                if (resultIm!=null){
                    ivresult.setImage(resultIm);
                }else{
                    ivresult.setImage(sourceIm);
                    radioButton0.setSelected(true);
                    JOptionPane.showMessageDialog(null, "Compare result can't be shown before compare function is called");
                }

            }



            if (e.getActionCommand().equalsIgnoreCase("Load source")){

                tmpBufImage = null;
                tmpBufImage = ImageIoDialog.loadMultipleFile();

                sourceBar.setVisible(false);

                //remove old images
                sourceBar.removeAll();
                for (int i = 1; i < sourceBar.getComponentCount()-1; i++)
                {
                    sourceBar.remove(i);
                }

                //set source image to null
                sourceIm=null;

                //add new images to sourceBar
                try {
                    loadSourceImages(0, 5);
                } catch (IOException e1) {
                    e1.printStackTrace();  
                }

                sourceBar.setVisible(true);

                /*if (tmpBufImage==null) return;
                if (tmpBufImage.getWidth()<1 || tmpBufImage.getHeight()<1) {
                    JOptionPane.showMessageDialog(thisFrame, "Image too small");
                    return;
                }
                sourceIm=new RasterImage(tmpBufImage.getWidth(), tmpBufImage.getHeight());
                ImageConverter.convertBufImage(tmpBufImage, sourceIm);
                ivsource.setImage(sourceIm); */
                
                radioButton0.setSelected(true);
            }
            if (e.getActionCommand().equalsIgnoreCase("Load compare")){

                tmpCompareBufImage = null;
                tmpCompareBufImage = ImageIoDialog.loadMultipleFile();
                compareBar.setVisible(false);
                compareBar.removeAll();
                compareBar.add(Box.createGlue());
                compareBar.add(Box.createGlue());
                compareIm=null;


                //loadCompareImages.execute();
                try {
                    loadCompareImages(0, 5);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

                //System.out.println(sourceBar.getComponentCount()) ;
                compareBar.setVisible(true);
                /*compareIm=null;
                BufferedImage tmpBufImage=ImageIoDialog.loadImage(thisFrame);
                if (tmpBufImage==null) return;
                if (tmpBufImage.getWidth()<1 || tmpBufImage.getHeight()<1) {
                    JOptionPane.showMessageDialog(thisFrame, "Image too small");
                    return;
                }
                compareIm=new RasterImage(tmpBufImage.getWidth(), tmpBufImage.getHeight());
                ImageConverter.convertBufImage(tmpBufImage, compareIm);
                ivtarget.setImage(compareIm);  */
                radioButton1.setSelected(true);

            }
            if (e.getActionCommand().equalsIgnoreCase("Save result")){
                if (resultTmp!=null){
                    for (RasterImage tmp: resultTmp)
                    {
                        ImageIoDialog.saveImage(thisFrame, tmp);
                    }

                }else{
                    JOptionPane.showMessageDialog(thisFrame, "Result can't be saved before acquired");
                }
            }
            if (e.getActionCommand().equalsIgnoreCase("Filter")){
                RasterImage tmp;
                if (radioButton0.isSelected()){
                    tmp = new RasterImage(sourceIm);
                    lv.lumii.pixelmaster.modules.compare.domain.medFilter.medFilter(sourceIm, tmp, 1, 1);
                    ivsource.setImage(tmp);
                }else if (radioButton1.isSelected()){
                    tmp=new RasterImage(compareIm);
                    lv.lumii.pixelmaster.modules.compare.domain.medFilter.medFilter(compareIm, tmp, 1, 1);
                    ivtarget.setImage(tmp);
                }else if (radioButton2.isSelected()){
                    tmp=new RasterImage(resultIm);
                    lv.lumii.pixelmaster.modules.compare.domain.medFilter.medFilter(resultIm, tmp, 1, 1);
                    ivresult.setImage(tmp);
                }
            }



		}
        public void windowClosing(WindowEvent e) {
        System.exit(0);
      }



}   private void loadSourceImages(int start, int end) throws IOException {

        if (tmpBufImage.length < end)
        {
            end = tmpBufImage.length;
        }

        for (int i = start; i < end; i++) {
                ImageIcon icon;
                icon = createImageIcon(tmpBufImage[i].getCanonicalPath(), tmpBufImage[i].getName());

                ThumbnailAction thumbAction;
                if(icon != null){

                    ImageIcon thumbnailIcon = new ImageIcon(getScaledImage(icon.getImage(), 200, 200));

                    thumbAction = new ThumbnailAction(icon, thumbnailIcon, tmpBufImage[i].getName());

                }else{
                    // the image failed to load for some reason
                    // so load a placeholder instead
                    thumbAction = new ThumbnailAction(placeholderIcon, placeholderIcon, tmpBufImage[i].getName());
                }

                JButton thumbButton = new JButton(thumbAction);
                thumbButton.setActionCommand("Switch source");
                // add the new button BEFORE the last glue
                // this centers the buttons in the toolbar
                sourceBar.add(thumbButton, sourceBar.getComponentCount() - 1);
                //publish(thumbAction);
            }
    }


    private void loadCompareImages(int start, int end) throws IOException
    {
        if (tmpCompareBufImage.length < end)
        {
            end = tmpCompareBufImage.length;
        }

        for (int i = start; i < end; i++) {
                ImageIcon icon;
                icon = createImageIcon(tmpCompareBufImage[i].getCanonicalPath(), tmpCompareBufImage[i].getName());

                ThumbnailAction thumbAction;
                if(icon != null){

                    ImageIcon thumbnailIcon = new ImageIcon(getScaledImage(icon.getImage(), 200, 200));

                    thumbAction = new ThumbnailAction(icon, thumbnailIcon, tmpCompareBufImage[i].getName());

                }else{
                    // the image failed to load for some reason
                    // so load a placeholder instead
                    thumbAction = new ThumbnailAction(placeholderIcon, placeholderIcon, tmpBufImage[i].getName());
                }
                JButton thumbButton = new JButton(thumbAction);
                // add the new button BEFORE the last glue
                // this centers the buttons in the toolbar
                compareBar.add(thumbButton, compareBar.getComponentCount() - 1);
            }

    }


    private void loadResultImages(int start, int end) throws IOException
    {
        if (resultTmp.length < end)
        {
            end = resultTmp.length;
        }

        for (int i = start; i < end; i++) {
                ImageIcon icon;
                icon = createImageIcon("test" + i, "test" + i);

                ThumbnailAction thumbAction;
                if(icon != null){

                    ImageIcon thumbnailIcon = new ImageIcon(getScaledImage(ImageConverter.toBuffered(resultTmp[i]), 200, 200));

                    thumbAction = new ThumbnailAction(icon, thumbnailIcon, "test");

                }else{
                    // the image failed to load for some reason
                    // so load a placeholder instead
                    thumbAction = new ThumbnailAction(placeholderIcon, placeholderIcon, "test");
                }
                JButton thumbButton = new JButton(thumbAction);
                thumbButton.setActionCommand("Switch result");
                // add the new button BEFORE the last glue
                // this centers the buttons in the toolbar
                resultBar.add(thumbButton, resultBar.getComponentCount() - 1);
            }
    }


    /**
     * Creates an ImageIcon if the path is valid.
     * @param path String - resource path
     * @param description String - description of the file
     * @return ImageIcon test;
     */
    protected ImageIcon createImageIcon(String path,
            String description) {
        java.net.URL imgURL = getClass().getResource(path);
        if (imgURL == null) {
            return new ImageIcon(path, description);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

    /**
     * Resizes an image using a Graphics2D object backed by a BufferedImage.
     * @param srcImg - source image to scale
     * @param w - desired width
     * @param h - desired height
     * @return - the new resized image
     */
    private Image getScaledImage(Image srcImg, int w, int h){
        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = resizedImg.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(srcImg, 0, 0, w, h, null);
        g2.dispose();
        return resizedImg;
    }


    /**
     * Resizes an image using a Graphics2D object backed by a BufferedImage.
     * @param srcImg - source image to scale
     * @param w - desired width
     * @param h - desired height
     * @return - the new resized image
     */
    private Image getScaledImage(BufferedImage srcImg, int w, int h){
        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = resizedImg.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(srcImg, 0, 0, w, h, null);
        g2.dispose();
        return resizedImg;
    }

    /**
     * Action class that shows the image specified in it's constructor.
     */
    private class ThumbnailAction extends AbstractAction{

        /**
         *The icon if the full image we want to display.
         */
        private Icon displayPhoto;

        /**
         * @param photo Icon - The full size photo to show in the button.
         * @param thumb Icon - The thumbnail to show in the button.
         * @param desc String - The descriptioon of the icon.
         */
        public ThumbnailAction(Icon photo, Icon thumb, String desc){
            displayPhoto = photo;

            // The short description becomes the tooltip of a button.
            putValue(SHORT_DESCRIPTION, desc);

            // The LARGE_ICON_KEY is the key for setting the
            // icon when an Action is applied to a button.
            putValue(LARGE_ICON_KEY, thumb);
        }

        /**
         * Shows the full image in the main area and sets the application title.
         */
        public void actionPerformed(ActionEvent e) {
            //photographLabel.setIcon(displayPhoto);
            setTitle("Image: " + getValue(SHORT_DESCRIPTION).toString());

            if (e.getActionCommand().equalsIgnoreCase("Switch source")){

                int i = 0;
                while (!tmpBufImage[i].getName().equals(getValue(SHORT_DESCRIPTION).toString()))
                {
                    i++;
                }

                BufferedImage sourceTmp;
                try {
                    sourceTmp = ImageIO.read(tmpBufImage[i]);
                    sourceIm = new RasterImage(sourceTmp.getWidth(), sourceTmp.getHeight());
                    ImageConverter.convertBufImage(sourceTmp, sourceIm);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }  
            }

            /*if (e.getActionCommand().equalsIgnoreCase("Switch result")){

                int i = 0;
                while (!tmpBufImage[i].getName().equals(getValue(SHORT_DESCRIPTION).toString()))
                {
                    i++;
                }

                BufferedImage sourceTmp;
                try {
                    sourceTmp = ImageIO.read(tmpBufImage[i]);
                    resultIm = new RasterImage(sourceTmp.getWidth(), sourceTmp.getHeight());
                    ImageConverter.convertBufImage(sourceTmp, resultIm);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }   */
        }
    }
}
