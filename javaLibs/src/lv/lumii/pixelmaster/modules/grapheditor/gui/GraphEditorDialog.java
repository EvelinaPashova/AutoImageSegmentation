/**
 * Add class reference here.
 *
 * @author Andrey Zhmakin
 *
 * Created on Apr 1, 2010 1:43:07 PM
 *
 */

package lv.lumii.pixelmaster.modules.grapheditor.gui;

import lv.lumii.pixelmaster.modules.spw.domain.graph.UGraph;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class GraphEditorDialog extends JDialog
{
    public GraphEditorDialog()
    {
        super( null, "Graph Editor", Dialog.ModalityType.MODELESS );
        this.initialize();
    }



    private void initialize()
    {
        this.setSize( 800, 500 );

        this.filename = null;
        this.setTitle();

        this.setJMenuBar( createMenuBar() );

        this.workPanel = new WorkPanel( new UGraph() );
        this.getContentPane().add( this.workPanel, java.awt.BorderLayout.CENTER );

        this.comboScale = new JComboBox();
        // ATTENTION: If you'll change the string format, i.e. "Scale: x",
        // then check also to the method interfacelisteners.ComboScaleListener.actionPerformed()
        this.comboScale.addItem( "Scale: x1"   );
        this.comboScale.addItem( "Scale: x3"   );
        this.comboScale.addItem( "Scale: x5"   );
        this.comboScale.addItem( "Scale: x10"  );
        this.comboScale.addItem( "Scale: x25"  );
        this.comboScale.addItem( "Scale: x50"  );
        this.comboScale.addItem( "Scale: x100" );
        this.comboScale.addActionListener( new lv.lumii.pixelmaster.modules.grapheditor.gui.interfacelisteners.ComboScaleListener( this ) );
        this.getContentPane().add( this.comboScale, java.awt.BorderLayout.NORTH );
        this.comboScale.setSelectedIndex( 3 );

        this.setVisible( true );
    }



    public void setTitle()
    {
        super.setTitle( "Graph Editor - [" + ((filename != null) ? filename : "Untitled") + "]" );
    }


    
    private JMenuBar createMenuBar()
    {
        JMenuBar    menuBar = new JMenuBar();
        JMenu       menu;
        JMenuItem   item;

        //--- File ----------------------------------------------
        {
            menu = new JMenu( "File" );
            menu.setMnemonic( KeyEvent.VK_F );
            menuBar.add( menu );

            ActionListener fileListener = new lv.lumii.pixelmaster.modules.grapheditor.gui.menulisteners.FileListener( this );

            item = new JMenuItem( "New" );
            item.addActionListener( fileListener );
            item.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_N, ActionEvent.CTRL_MASK ) );
            menu.add( item );

            item = new JMenuItem( "Open" );
            item.addActionListener( fileListener );
            item.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_O, ActionEvent.CTRL_MASK ) );
            menu.add( item );

            item = new JMenuItem( "Save" );
            item.addActionListener( fileListener );
            item.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_S, ActionEvent.CTRL_MASK ) );
            menu.add( item );

            item = new JMenuItem( "Save as..." );
            item.addActionListener( fileListener );
            item.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_A, KeyEvent.CTRL_MASK ) );
            menu.add( item );

            menu.addSeparator();

            item = new JMenuItem( "Exit" );
            item.addActionListener( fileListener );
            item.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_F4, ActionEvent.ALT_MASK ) );
            menu.add( item );
        }

        //--- Edit ----------------------------------------------
        {
            menu = new JMenu( "Edit" );
            menu.setMnemonic( KeyEvent.VK_E );
            menuBar.add( menu );

            ActionListener editListener = new lv.lumii.pixelmaster.modules.grapheditor.gui.menulisteners.EditListener( this );

            item = new JMenuItem( "Add vertex" );
            item.addActionListener( editListener );
            item.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_V, ActionEvent.CTRL_MASK ) );
            menu.add( item );

            item = new JMenuItem( "Remove vertex" );
            item.addActionListener( editListener );
            item.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_W, ActionEvent.CTRL_MASK ) );
            menu.add( item );

            menu.addSeparator();

            item = new JMenuItem( "Add edge" );
            item.addActionListener( editListener );
            item.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_E, ActionEvent.CTRL_MASK ) );
            menu.add( item );

            item = new JMenuItem( "Remove edge" );
            item.addActionListener( editListener );
            item.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_F, ActionEvent.CTRL_MASK ) );
            menu.add( item );

            menu.addSeparator();

            item = new JMenuItem( "Normal mode" );
            item.addActionListener( editListener );
            item.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_M, ActionEvent.CTRL_MASK ) );
            menu.add( item );

            item = new JMenuItem( "Auto connection mode" );
            item.addActionListener( editListener );
            item.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_C, ActionEvent.CTRL_MASK ) );
            menu.add( item );

            item = new JMenuItem( "Drawing mode" );
            item.addActionListener( editListener );
            item.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_D, ActionEvent.CTRL_MASK ) );
            menu.add( item );
        }

        //--- View ----------------------------------------------
        {
            menu = new JMenu( "View" );
            menu.setMnemonic( KeyEvent.VK_E );
            menuBar.add( menu );

            ActionListener viewListener = new lv.lumii.pixelmaster.modules.grapheditor.gui.menulisteners.ViewListener( this );

            item = new JMenuItem( "Bullets on Vertices" );
            item.addActionListener( viewListener );
            item.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_B, ActionEvent.CTRL_MASK ) );
            menu.add( item );

            item = new JMenuItem( "Axes X and Y" );
            item.addActionListener( viewListener );
            menu.add( item );

            item = new JMenuItem( "Center of Mass" );
            item.addActionListener( viewListener );
            menu.add( item );
        }

        //--- Help ----------------------------------------------
        {
            menu = new JMenu( "Help" );
            menu.setMnemonic( KeyEvent.VK_H );
            menuBar.add( menu );

            ActionListener helpListener = new lv.lumii.pixelmaster.modules.grapheditor.gui.menulisteners.HelpListener( this );

            item = new JMenuItem( "About..." );
            item.addActionListener( helpListener );
            item.setAccelerator( KeyStroke.getKeyStroke( "F1" )  );
            menu.add( item );
        }

        return menuBar;
    }

    public String    filename;
    public WorkPanel workPanel;
    public JComboBox comboScale;
}




