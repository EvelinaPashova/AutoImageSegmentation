package lv.lumii.pixelmaster.modules.ridge.gui;

//import gui.components.forRidge.*;
import lv.lumii.pixelmaster.modules.ridge.domain.SymbolInfo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

/**
 * Pēc teksta atpazīšanas attēlo rezultātus dotā logā
 * @author Sandra Rivare modifiem by Ainars Kumpins
 * @since 14.04.2009
 */
public class ShowRecognitionText extends JFrame{

    private ActionListener aListener;
	private JFrame thisFrame;
    private JPanel menuPanel;
    private JPanel resultPanel;
    private JButton CancelButton;
    private JButton AddButton;

    private JScrollPane jScrollPane1;
    private JTextArea jTextArea1;

    private JLabel textIs;

    ArrayList<SymbolInfo> symbols;
    File file;

JPanel labePanel;

    //ImageViewer iv;

    public ShowRecognitionText( String text, ArrayList<SymbolInfo> symbols, File file){
        super("Recognition results");
        this.symbols = symbols;
        this.file = file;

        setVisible(false);
		setSize(400, 163);
		setLocation(450, 200);
		setResizable(false);
		thisFrame=this;
		aListener=new axnListener();

        labePanel = new JPanel();
        textIs = new JLabel("Possible text :  ");
        labePanel.add(textIs);
         add(labePanel, BorderLayout.NORTH);

        jScrollPane1 = new JScrollPane();
       
        jTextArea1 = new JTextArea(text);
        jTextArea1.setEditable(false);
        jTextArea1.setLineWrap(true);
        jTextArea1.setWrapStyleWord(true);
        jScrollPane1.setViewportView(jTextArea1);
        add (jScrollPane1, BorderLayout.CENTER);



        menuPanel=new JPanel(new GridLayout(1, 3));
		add(menuPanel, BorderLayout.SOUTH);

        AddButton=new JButton("Add to Database");
		AddButton.setActionCommand("Add to Database");
		AddButton.addActionListener(aListener);
		menuPanel.add(AddButton);

		CancelButton=new JButton("Ok");
		CancelButton.setActionCommand("Ok");
		CancelButton.addActionListener(aListener);
		menuPanel.add(CancelButton);
		setVisible(true);
    }

  
    /**
	 * Klase klausās kādu darbību lietotājs grib veikt ar pogu palīdzību
	 */
	private class axnListener implements ActionListener {
        @Override
		public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand().equalsIgnoreCase("Ok")) {
				dispose();
			}
            if(e.getActionCommand().equalsIgnoreCase("Add to Database")){
                JOptionPane.showMessageDialog(null, "Caution all symbols will be added as selected char");
                AddToTextDbFrame addFrame = new AddToTextDbFrame(symbols, file);
                
                dispose();
            }
		}
	}
}
