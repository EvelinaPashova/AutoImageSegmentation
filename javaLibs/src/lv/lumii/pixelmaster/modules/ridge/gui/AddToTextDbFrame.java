
package lv.lumii.pixelmaster.modules.ridge.gui;



import lv.lumii.pixelmaster.modules.ridge.domain.SymbolInfo;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import java.io.*;
/**
 * Izveido neatkarīgu logu, kurā izvēlās teksta zīmi, kura ir jāpievieno datubāzei.
 * Nodrošina jaunu ierakstu datubāzē
 * @author Sandra Rivare izmainijis Ainārs Kumpins
 * @since 01.04.2009
 */
public class AddToTextDbFrame extends JFrame{
    private ActionListener aListener;
    private JComboBox letterList;
    private JComboBox numberList;

	private JPanel mainPanel;
    private JPanel adjustPanel;
    private JButton OKButton;
    private JButton CancelButton;

    File file;
    ArrayList<SymbolInfo> symbols;

    String[] ChoseLetter = {
         "Chars",
         "A","a","B","b","C","c","D","d","E","e",
         "F","f","G","g","H","h","I","i","J","j",
         "K","k","L","l","M","m","N","n","O","o",
         "P","p","Q","q","R","r","S","s","T","t",
         "U","u","V","v","W","w","X","x","Y","y",
         "Z","z"
    };
    String[] ChoseNumber = {"Digits","0","1","2","3","4","5","6","7","8","9"};

    /**
     * Konstruktors
     */
    public AddToTextDbFrame(ArrayList<SymbolInfo> symbols, File file) {
        super("Add Symbols");
		try {
            this.symbols = symbols;
            this.file = file;
			setVisible(false);
			setSize(200, 100);
			setMinimumSize(new Dimension(200, 200));
			setLocation(450, 200);
			setResizable(true);
			aListener=new axnListener();

           // warningPanel = new JPanel();
           // warningLable = new JLabel(" Caution all symbols will be added as selected");
           // warningPanel.add(warningLable);

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

            adjustPanel=new JPanel(new GridLayout(1, 2));
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
                String defaultChar = "Chars";
                String defaultDigit = "Digits";
                String newName = null;
                String line1 = (String) letterList.getSelectedItem();
                String line2 = (String) numberList.getSelectedItem();
                if((line1 == defaultChar && line2 != defaultDigit) ||
                    (line1 != defaultChar && line2 == defaultDigit)){
                    if(line1 == defaultChar) newName = line2;
                    if(line2 == defaultDigit) newName = line1;
                        try {
                            for (int index = 0; index < symbols.size(); index++) {
                                SymbolInfo data = symbols.get(index);
                                if (data != null) {
System.out.println(data);
                                    data.setName(newName);
                                    SymbolInfo.fileWrite(data, file, true);
                                }
                            }
                        }
                        catch (IOException ioe) {
                            System.out.println("IOException in AddToDbFrame");
                        }


                JOptionPane.showMessageDialog(null, "Added");
                dispose();
			}
            }
            if (e.getActionCommand().equalsIgnoreCase("Cancel")) {
				dispose();
			}
		}
	}
}













