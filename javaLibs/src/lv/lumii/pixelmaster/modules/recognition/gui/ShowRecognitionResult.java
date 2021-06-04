package lv.lumii.pixelmaster.modules.recognition.gui;

import lv.lumii.pixelmaster.core.api.gui.ImageViewer;
import lv.lumii.pixelmaster.modules.recognition.domain.CharacterDatabase;
import lv.lumii.pixelmaster.modules.recognition.domain.CharacterInfo;
import lv.lumii.pixelmaster.core.api.domain.RasterImage;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import lv.lumii.pixelmaster.core.api.domain.RGB;

/**
 * Pēc teksta zīmes atpazīšanas attēlo rezultātus dotā logā
 * @author Sandra Rivare
 * @since 14.04.2009
 */
public class ShowRecognitionResult extends JFrame{

    private ActionListener aListener;
	private JFrame thisFrame;
	private JPanel imagePanel;
    private JPanel menuPanel;
    private JPanel resultPanel;
    private JButton CorrectButton;
    private JButton AddButton;
    private JButton CancelButton;

    private JLabel textCharIs;
    private JLabel correlationCoefficient;

    private CharacterInfo table;
    private CharacterDatabase cDB;

    ImageViewer iv;
    
    public ShowRecognitionResult(RasterImage rImage, int record, CharacterDatabase charDB, CharacterInfo tableData){
        super("Recognition results");
        table = tableData;
        cDB = charDB;

        setVisible(false);
		setSize(400, 163);
		setLocation(450, 200);
		setResizable(false);
		thisFrame=this;        
		aListener=new axnListener();

		imagePanel=new JPanel(new BorderLayout());
        add(imagePanel);

        //uzzīmē līnijas
        int color = RGB.getRGB(238,122,233);
        int color2 = RGB.getRGB(123, 104, 238);
        //System.out.println("table.centerX "+table.centerX+" table.V3 "+table.V3);
        rImage = drawLine(rImage, table,color,color2);

        iv=new ImageViewer(rImage);
		imagePanel.add(iv, BorderLayout.CENTER);
      

        resultPanel=new JPanel (new GridLayout(2, 2));
        add(resultPanel, BorderLayout.EAST);

        textCharIs = new JLabel("Possible text character :  ");
        resultPanel.add(textCharIs);
        JLabel Char = new JLabel(cDB.letter[record]);
        resultPanel.add(Char);

        correlationCoefficient = new JLabel("Correlation coefficient :  ");
        resultPanel.add(correlationCoefficient);
        double var = cDB.variance[record];
        var = var*10000;
        int varInt = (int)var;
        var = varInt;
        var = var/10000;
        String varString = Double.toString(var);
        JLabel Var = new JLabel(varString);
        resultPanel.add(Var);

        menuPanel=new JPanel(new GridLayout(1, 3));
		add(menuPanel, BorderLayout.SOUTH);

		CorrectButton=new JButton("Correct");
		CorrectButton.setActionCommand("Correct");
		CorrectButton.addActionListener(aListener);
		menuPanel.add(CorrectButton);

        AddButton=new JButton("Add to Database");
		AddButton.setActionCommand("Add to Database");
		AddButton.addActionListener(aListener);
		menuPanel.add(AddButton);

		CancelButton=new JButton("Cancel");
		CancelButton.setActionCommand("Cancel");
		CancelButton.addActionListener(aListener);
		menuPanel.add(CancelButton);
		setVisible(true);
    }

    private RasterImage drawLine(RasterImage workingImage, CharacterInfo table, int color, int color2) {
        int startX = 0, startY, endX = 0, endY;
            for(int tmpX=0; tmpX<=workingImage.getWidth(); tmpX++){
                workingImage.setRGB(workingImage.getWidth() * table.centerY + tmpX, color);
                workingImage.setRGB(workingImage.getWidth() * (table.centerY+1) + tmpX, color);
            }

            for(int tmpY=0; tmpY<workingImage.getHeight(); tmpY++){
                workingImage.setRGB(workingImage.getWidth() * tmpY + table.centerX, color);
                workingImage.setRGB(workingImage.getWidth() * tmpY + table.centerX + 1, color);
            }

            int x=0,y=0;
            for(int tmp=0; tmp<workingImage.getHeight()-1; tmp++){
                workingImage.setRGB(workingImage.getWidth() * y + x, color);
                workingImage.setRGB(workingImage.getWidth() * (y+1) + x, color);
                x++;y++;
            }

            x=workingImage.getWidth(); y=0;
            for(int tmp=0; tmp<workingImage.getHeight()-1; tmp++){
                workingImage.setRGB(workingImage.getWidth() * y + x, color);
                workingImage.setRGB(workingImage.getWidth() * (y+1) + x, color);
                x--;y++;
            }

            int count=0;
            x=workingImage.getWidth()/4; y=0;
            for(int tmp=0; tmp<workingImage.getHeight()-1; tmp++){
                workingImage.setRGB(workingImage.getWidth() * y + x, color2);
                workingImage.setRGB(workingImage.getWidth() * (y+1) + x, color2);
                count++;
                if(count==2){ x++; count=0;}
                y++;
            }

            count=0;
            x=workingImage.getWidth()/4; x=x*3;
            y=0;
            for(int tmp=0; tmp<workingImage.getHeight()-1; tmp++){
                workingImage.setRGB(workingImage.getWidth() * y + x, color2);
                workingImage.setRGB(workingImage.getWidth() * (y+1) + x, color2);
                count++;
                if(count==2){ x--; count=0;}
                y++;
            }

            count=0;
            x=0;
            y=workingImage.getHeight()/4;
            for(int tmp=0; tmp<workingImage.getWidth(); tmp++){
                workingImage.setRGB(workingImage.getWidth() * y + x, color2);
                workingImage.setRGB(workingImage.getWidth() * (y+1) + x, color2);
                count++;
                if(count==2){ y++; count=0;}
                x++;
            }

            count=0;
            x=0;
            y=workingImage.getHeight()/4; y = y*3;
            for(int tmp=0; tmp<workingImage.getWidth(); tmp++){
                workingImage.setRGB(workingImage.getWidth() * y + x, color2);
                workingImage.setRGB(workingImage.getWidth() * (y+1) + x, color2);
                count++;
                if(count==2){ y--; count=0;}
                x++;
            }
        return workingImage;

        
    }

    /**
	 * Klase klausās kādu darbību lietotājs grib veikt ar pogu palīdzību
	 */
	private class axnListener implements ActionListener {
        @Override
		public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand().equalsIgnoreCase("Correct")
                    || e.getActionCommand().equalsIgnoreCase("Cancel")) {
				dispose();
			}
            if(e.getActionCommand().equalsIgnoreCase("Add to Database")){
                AddToDatabaseFrame addframe = new AddToDatabaseFrame(table,cDB);
                dispose();
            }
		}
	}
}
