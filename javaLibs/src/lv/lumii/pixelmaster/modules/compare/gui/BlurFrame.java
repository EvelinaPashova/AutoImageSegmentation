package lv.lumii.pixelmaster.modules.compare.gui;

import lv.lumii.pixelmaster.core.api.domain.ImageConverter;
import lv.lumii.pixelmaster.core.api.domain.RasterImage;
import lv.lumii.pixelmaster.core.api.domain.SizeConstraintViolationException;
import lv.lumii.pixelmaster.core.api.framework.IWorkbench;
import lv.lumii.pixelmaster.modules.compare.domain.BlurFilter;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

/**
 * Created by IntelliJ IDEA.
 * User: murcielago
 * Date: Dec 20, 2010
 * Time: 9:42:38 PM
 * To change this template use File | Settings | File Templates.
 */
public class BlurFrame extends JFrame {

    private ActionListener aListener;
    private ChangeListener cListener;
    private RasterImage sourceIm;
    private JFrame thisFrame;
    private JPanel sliderPanel, bottomPanel;
    private JSlider radius, sides, bloom, bloomThreshold;
    private JButton applyButton;
    private BlurFilter blurFilter;

    private IWorkbench workbench;
    

    static final int R_MIN = 0;
    static final int R_MAX = 50;
    static final int R_INIT = 0;    //initial radius
    static final int S_MIN = 0;  //3
    static final int S_MAX = 12;
    static final int S_INIT = 0;    //initial slides
    static final int B_MIN = 0;  //1
    static final int B_MAX = 8;
    static final int B_INIT = 0;    //initial bloom
    static final int BT_MIN = 0;
    static final int BT_MAX = 255;
    static final int BT_INIT = 0;    //initial bloom threshold

    public BlurFrame(IWorkbench workbench) {
        super("Blur Filter");
        try {
            setVisible(false);
            thisFrame = this;
            setSize(300, 500);
            setMinimumSize(new Dimension(300,500));

            this.workbench = workbench;
            //sourceImg = new RasterImage(sourceImg);

            createPanels();
            createComponents();
            addComponents();
            addListener();
            setVisible(true);
        }
        catch (Exception e) {
            e.printStackTrace();
            //JOptionPane.showMessageDialog(this, "No image loaded.");
        }
    }


    protected void createPanels() {
        setLayout(new BorderLayout());

        sliderPanel = new JPanel();
        sliderPanel.setMinimumSize(new Dimension(300,400));
        sliderPanel.setMaximumSize(new Dimension(300,400));
        sliderPanel.setPreferredSize(new Dimension(300,400));
        sliderPanel.setSize(new Dimension(300,400));
        sliderPanel.setAutoscrolls(false);
        sliderPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        add(sliderPanel, BorderLayout.NORTH);

        bottomPanel = new JPanel();
        bottomPanel.setMinimumSize(new Dimension(300,100));
        bottomPanel.setMaximumSize(new Dimension(300,100));
        bottomPanel.setPreferredSize(new Dimension(300,100));
        bottomPanel.setSize(new Dimension(300,100));
        bottomPanel.setAutoscrolls(false);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        add(bottomPanel, BorderLayout.SOUTH);
    }

    protected void createComponents() {
        aListener=new aListener();

        radius = new JSlider(JSlider.HORIZONTAL, R_MIN, R_MAX, R_INIT);
        radius.setBorder(BorderFactory.createTitledBorder("Radius"));
        //Turn on labels at major tick marks.
        radius.setMajorTickSpacing(10);
        radius.setMinorTickSpacing(1);
        radius.setPaintTicks(true);
        radius.setPaintLabels(true);

        sides = new JSlider(JSlider.HORIZONTAL, S_MIN, S_MAX, S_INIT);
        sides.setBorder(BorderFactory.createTitledBorder("Slides"));
        //Turn on labels at major tick marks.
        sides.setMinimum(3);
        sides.setMaximum(12);
        sides.setMajorTickSpacing(9);
        //slides.setMinorTickSpacing(1);
        sides.setPaintTicks(true);
        sides.setPaintLabels(true);

       /* bloom = new JSlider(JSlider.HORIZONTAL, B_MIN, B_MAX, B_INIT);
        bloom.setBorder(BorderFactory.createTitledBorder("Bloom"));
        //Turn on labels at major tick marks.
        bloom.setMinimum(1);
        bloom.setMaximum(8);
        bloom.setMajorTickSpacing(7);
        //bloom.setMinorTickSpacing(1);
        bloom.setPaintTicks(true);
        bloom.setPaintLabels(true);

        bloomThreshold = new JSlider(JSlider.HORIZONTAL, BT_MIN, BT_MAX, BT_INIT);
        bloomThreshold.setBorder(BorderFactory.createTitledBorder("Bloom Threshold"));
        //Turn on labels at major tick marks.
        bloomThreshold.setMajorTickSpacing(255);
        //bloomThreshold.setMinorTickSpacing(16);
        bloomThreshold.setPaintTicks(true);
        bloomThreshold.setPaintLabels(true);      */


        applyButton = new JButton("Apply");
		applyButton.setActionCommand("Apply");
		applyButton.setAlignmentX(CENTER_ALIGNMENT);

    }

    protected void addComponents() {
        sliderPanel.add(radius);
        sliderPanel.add(sides);
        //sliderPanel.add(bloom);
        //sliderPanel.add(bloomThreshold);

        bottomPanel.add(applyButton);
    }

    protected void addListener() {

        //shitos laikam nevajadzēs
        radius.addChangeListener(cListener);
        sides.addChangeListener(cListener);
        //bloom.addChangeListener(cListener);
        //bloomThreshold.addChangeListener(cListener);

        applyButton.addActionListener(aListener);
    }

    private class aListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand().equalsIgnoreCase("Close")) {
				dispose();
			}

            if (e.getActionCommand().equalsIgnoreCase("Apply")) {
               //workbench.setActiveImage(blurFilter(workbench.getActiveImage()),1);
                BlurFilter blurFilter = new BlurFilter();
                blurFilter.setRadius(radius.getValue());
                blurFilter.setSides(sides.getValue());
                //blurFilter.setBloom(bloom.getValue());
                //blurFilter.setBloomThreshold(bloomThreshold.getValue());

                BufferedImage tmpImg = ImageConverter.toBuffered(workbench.getActiveImage());


                tmpImg = blurFilter.filter(tmpImg, tmpImg, 2);


                RasterImage activeImg = new RasterImage(tmpImg.getWidth(), tmpImg.getHeight());
                ImageConverter.convertBufImage(tmpImg,activeImg);

                try {
                    workbench.setActiveImage(activeImg, IWorkbench.OWNERSHIP_CALLEE);
                } catch (SizeConstraintViolationException e1) {
                    e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
        }     
    }
}
