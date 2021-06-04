package lv.lumii.pixelmaster.modules.recognition.gui;

import lv.lumii.pixelmaster.modules.recognition.domain.CharacterDatabase;
import lv.lumii.pixelmaster.modules.recognition.domain.CharacterInfo;
import lv.lumii.pixelmaster.modules.recognition.domain.database.DatabaseProcessor;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.swing.*;

/**
 * Izveido neatkarīgu logu, kurā izvēlās teksta zīmi, kura ir jāpievieno datubāzei.
 * Nodrošina jaunu ierakstu datubāzē
 * @author Sandra Rivare
 * @since 01.04.2009
 */
public class AddToDatabaseFrame extends JFrame{
    private CharacterInfo table;
    private CharacterDatabase cDB;
    private ActionListener aListener;
    private JComboBox letterList;
    private JComboBox numberList;

	private JPanel mainPanel;
    private JPanel adjustPanel;
    private JButton OKButton;
    private JButton CancelButton;

    String[] ChoseLetter = {
         "-",
         "A","a","B","b","C","c","D","d","E","e",
         "F","f","G","g","H","h","I","i","J","j",
         "K","k","L","l","M","m","N","n","O","o",
         "P","p","Q","q","R","r","S","s","T","t",
         "U","u","V","v","W","w","X","x","Y","y",
         "Z","z"
    };
    String[] ChoseNumber = {"-","0","1","2","3","4","5","6","7","8","9"};

    /**
     * Konstruktors
     */
    public AddToDatabaseFrame(CharacterInfo tableData, CharacterDatabase charDB) {
        super("Add character");
		try {
            table = tableData;
            cDB = charDB;
			setVisible(false);
			setSize(200, 100);
			setMinimumSize(new Dimension(200, 200));
			setLocation(450, 200);
			setResizable(true);
			aListener=new axnListener();

			mainPanel=new JPanel(new BorderLayout(10,10));
			add(mainPanel);

            letterList = new JComboBox(ChoseLetter);
            letterList.setMaximumRowCount(10);
            letterList.setEditable(false);
            mainPanel.add(letterList,BorderLayout.NORTH);

            numberList = new JComboBox(ChoseNumber);
            numberList.setMaximumRowCount(10);
            numberList.setEditable(true);
            mainPanel.add(numberList,BorderLayout.CENTER);

            adjustPanel=new JPanel(new GridLayout(1, 3));
			mainPanel.add(adjustPanel, BorderLayout.SOUTH);

			OKButton=new JButton("OK");
			OKButton.setActionCommand("OK");
			OKButton.addActionListener(aListener);
			adjustPanel.add(OKButton);

			CancelButton=new JButton("Cancel");
			CancelButton.setActionCommand("Cancel");
			CancelButton.addActionListener(aListener);
			adjustPanel.add(CancelButton);
			setVisible(true);
		}
		catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "No image loaded.");
		}
    }

	/**
	 * Klase klausās kādu darbību lietotājs grib veikt ar pogu palīdzību
	 */
	private class axnListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand().equalsIgnoreCase("OK")) {
				String result = DatabaseProcessor.addToDatabase(
					(String) letterList.getSelectedItem(),
					(String) numberList.getSelectedItem(),
					table,
					new File(System.getProperty("user.home") +
						("/.pixelmaster/modules/recognition/charDB.txt")),
					cDB
				);
				JOptionPane.showMessageDialog(null, result);
				if (result.startsWith("You have jut added")) {
					dispose();
				}
			}
            if (e.getActionCommand().equalsIgnoreCase("Cancel")) {
				dispose();
			}
		}
	}
}