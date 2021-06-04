/**
 * <code>ExtractionDialog</code> implements message extraction dialog.
 *
 * @author Andrey Zhmakin
 *
 * Created on May 19, 2010 12:09:07 PM
 *
 */

package lv.lumii.pixelmaster.modules.steganography.gui;

import lv.lumii.pixelmaster.modules.steganography.domain.BitsOfImageBySignificance;
import lv.lumii.pixelmaster.modules.steganography.domain.ConformityTable;
import lv.lumii.pixelmaster.modules.steganography.domain.Util;
import lv.lumii.pixelmaster.modules.steganography.domain.BitString;
import lv.lumii.pixelmaster.modules.steganography.domain.Message;
import lv.lumii.pixelmaster.core.api.domain.RasterImage;
import lv.lumii.pixelmaster.modules.steganography.domain.ecc.ReedSolomon;
import lv.lumii.pixelmaster.modules.steganography.domain.ZhaoAndKoch;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;


public class ExtractionDialog extends JDialog implements ActionListener
{
    public ExtractionDialog( Frame owner, RasterImage image )
    {
        super( owner, "Extract message", Dialog.ModalityType.APPLICATION_MODAL );

		assert image != null;

//        if ( image == null )
//        {
//            JOptionPane.showMessageDialog( owner,
//                                           "Load image, please!", "Error",
//                                           JOptionPane.ERROR_MESSAGE, null );
//            this.dispose();
//            return;
//        }

        this.message = null;

        this.image = image;

        this.setSize( 600, 500 );
        this.setMinimumSize( new Dimension( 600, 500 ) );

//        this.addWindowListener(
//                (owner == null) ? new WindowAdapter()
//                                    {
//                                        public void windowClosing( WindowEvent e )
//                                        {
//                                            System.exit( 0 );
//                                        }
//                                    }
//                                : new WindowAdapter()
//                                    {
//                                        public void windowClosing( WindowEvent e ) { }
//                                    }
//        );

        this.placeControls();

        this.setVisible( true );
    }



    private void placeControls()
    {
        //this.getContentPane().setLayout( new BoxLayout( this.getContentPane(), BoxLayout.Y_AXIS ) );
        this.getContentPane().setLayout( new BorderLayout() );

        JPanel pnlOptions = new JPanel();
        pnlOptions.setLayout( new BoxLayout( pnlOptions, BoxLayout.Y_AXIS ) );

        JPanel pnlAlgorithm = new JPanel();
        pnlAlgorithm.setBorder( BorderFactory.createTitledBorder( "Choose algorithm:" ) );
        pnlOptions.add( pnlAlgorithm );


        ButtonGroup algorithms = new ButtonGroup();

        this.optLSB = new JRadioButton( "Least Significant Bits" );
        this.optLSB.setSelected( true ); 
        algorithms.add( this.optLSB );

        this.optZhaoAndKoch = new JRadioButton( "Zhao & Koch" );
        algorithms.add( this.optZhaoAndKoch );

        pnlAlgorithm.add( this.optLSB );
        pnlAlgorithm.add( this.optZhaoAndKoch );


        JPanel pnlDecryption = new JPanel();
        pnlDecryption.setBorder( BorderFactory.createTitledBorder( "Decryption:" ) );

        pnlDecryption.add( new JLabel( "Password: " ) );

        this.txtPassword = new JPasswordField( "", lv.lumii.pixelmaster.modules.steganography.gui.EmbedmentDialog.RECOMMENDED_PASSWORD_LENGTH );
        pnlDecryption.add( this.txtPassword );

        pnlOptions.add( pnlDecryption );


        JPanel pnlPreview = new JPanel();
        pnlPreview.setLayout( new BorderLayout() );
        pnlPreview.setBorder( BorderFactory.createTitledBorder( "Message preview:" ) );

        this.txtPreview = new JTextArea( "", 8, 0 );
        this.txtPreview.setEditable( false );
        this.txtPreview.setLineWrap( true );

        pnlPreview.add( new JScrollPane( this.txtPreview ) );

        JSplitPane splMetrics = new JSplitPane( JSplitPane.VERTICAL_SPLIT, true, pnlOptions, pnlPreview );
        splMetrics.setOneTouchExpandable( true );
        splMetrics.setDividerLocation( Integer.MIN_VALUE/*Integer.MAX_VALUE*/ );
        this.getContentPane().add( splMetrics );
        

        JPanel pnlCommand = new JPanel();

        this.cmdPreview = new JButton( "Preview" );
        this.cmdPreview.addActionListener( this );
        pnlCommand.add( this.cmdPreview );

        this.cmdSave = new JButton( "Save" );
        this.cmdSave.addActionListener( this );
        pnlCommand.add( this.cmdSave );

        this.cmdCancel = new JButton( "Cancel" );
        this.cmdCancel.addActionListener( this );
        pnlCommand.add( this.cmdCancel );

        this.getContentPane().add( pnlCommand, BorderLayout.SOUTH );
    }



    public void actionPerformed( ActionEvent e )
    {
        if ( e.getSource() == this.cmdPreview )
        {
            ProgressBox pb = new ProgressBox( this );
            pb.show();
            pb.setIndeterminate( true );
            MessageExtraction me = new MessageExtraction( pb, this );

            ExtractionAbortionMonitor ea = new ExtractionAbortionMonitor( me, pb );

            me.start();
            ea.start();
        }
        else if ( e.getSource() == this.cmdSave )
        {
            if ( this.message == null )
            {
                 JOptionPane.showMessageDialog( this,
                                                "Preview message, please!", "Error",
                                                JOptionPane.ERROR_MESSAGE, null );
                return;
            }

            final JFileChooser fc = new JFileChooser();
            fc.setFileSelectionMode( JFileChooser.FILES_ONLY );

            if ( fc.showSaveDialog( this ) == JFileChooser.APPROVE_OPTION )
            {
                try
                {
                    Util.save( fc.getSelectedFile(), this.message );
                }
                catch ( IOException x )
                {
                    JOptionPane.showMessageDialog( this,
                                                "Can\'t save the file!", "Error",
                                                JOptionPane.ERROR_MESSAGE, null );
                }
            }
        }
        else if ( e.getSource() == this.cmdCancel )
        {
            this.dispose();
        }
    }



    private class ExtractionAbortionMonitor extends Thread
    {
        public ExtractionAbortionMonitor( MessageExtraction extractionThread, ProgressBox monitor )
        {
            this.extractionThread   = extractionThread;
            this.monitor            = monitor;
        }

        public void run()
        {
            while ( !this.extractionThread.isInterrupted() )
            {
                if ( this.monitor.isCanceled() )
                {
                    this.monitor.close();
                    this.extractionThread.stop();
                    return;
                }
            }
        }

        MessageExtraction   extractionThread;
        ProgressBox         monitor;
    }


    private class MessageExtraction extends Thread
    {
        public MessageExtraction( ProgressBox monitor, ExtractionDialog owner )
        {
            this.monitor    = monitor;
            this.owner      = owner;
        }

        public void run()
        {
            try
            {
                this.monitor.setNote( "Extracting..." );

                int method;

                if      ( optLSB.isSelected()         )    method = METHOD_LSB;
                else if ( optZhaoAndKoch.isSelected() )    method = METHOD_ZNK;
                else
                {
                    method = 0;
                    this.finalActions();
                    assert false : "GUI error!";
                }

                long key = Util.getKeyFromPassword( txtPassword.getPassword() );

                BitsOfImageBySignificance bitsOfImage = new BitsOfImageBySignificance( image, key );
                ZhaoAndKoch               zhaoAndKoch = new ZhaoAndKoch( image );
                ConformityTable table;

                // Read service data from the container

                BitString strengthClause = new BitString( Message.SIZE_OF_ECC_STRENGTH );
                BitString sizeClause     = new BitString( Message.SIZE_OF_SIZE_CLAUSE  );

                int containerMaxSize;

                if ( method == METHOD_LSB )
                {
                    table = null; // just to prevent error
                    containerMaxSize = bitsOfImage.getSize();

                    try
                    {
                        for ( int i = 0; i < Message.SIZE_OF_ECC_STRENGTH; i++ )
                        {
                            strengthClause.set( bitsOfImage.get(i), i );
                        }

                        for ( int i = 0; i < Message.SIZE_OF_SIZE_CLAUSE; i++ )
                        {
                            sizeClause.set( bitsOfImage.get(Message.SIZE_OF_ECC_STRENGTH + i), i );
                        }

                    }
                    catch ( BitsOfImageBySignificance.IndexOutOfBoundsException x )
                    {
                        assert false : "This should not happen!";
                    }
                }
                else// if ( method == METHOD_ZNK )
                {
                    containerMaxSize = zhaoAndKoch.getSize();

                    table = new ConformityTable( 0, zhaoAndKoch.getSize(), key );

                    for ( int i = 0; i < Message.SIZE_OF_ECC_STRENGTH; i++ )
                    {
                        strengthClause.set( zhaoAndKoch.get(table.get(i)), i );
                    }

                    for ( int i = 0; i < Message.SIZE_OF_SIZE_CLAUSE; i++ )
                    {
                        sizeClause.set( zhaoAndKoch.get(table.get(Message.SIZE_OF_ECC_STRENGTH + i)), i );
                    }
                }

                int strengthOfEcc  = Message.getEccStrength( strengthClause );

                if ( strengthOfEcc == -1 )
                {
                    JOptionPane.showMessageDialog( owner,
                                                   "Container is either empty or message is significantly damaged!",
                                                   "Error",
                                                   JOptionPane.ERROR_MESSAGE, null );
                    message = null;
                    txtPreview.setText( "" );

                    this.finalActions();
                    return;
                }

                int rawMessageSize = Message.decodeMessageSize( sizeClause );

                // Read coded message from the container

                int codedSize = rawMessageSize + Message.predictEccOverhead( rawMessageSize, strengthOfEcc * 10 );

                if ( codedSize + Message.SERVICE_DATA_SIZE > containerMaxSize )
                {
                    codedSize = containerMaxSize - Message.SERVICE_DATA_SIZE;
                }

                BitString body = new BitString( codedSize );

                if ( method == METHOD_LSB )
                {
                    int i = Message.SERVICE_DATA_SIZE;

                    for ( int j = 0; j < codedSize; i++, j++ )
                    {
                        try
                        {
                            body.set( bitsOfImage.get(i), j );
                        }
                        catch ( BitsOfImageBySignificance.IndexOutOfBoundsException x )
                        {
                            this.finalActions();
                            assert false : "This couldn't happen!";
                        }
                    }
                }
                else// if ( method == METHOD_ZNK )
                {
                    int i = Message.SERVICE_DATA_SIZE;

                    for ( int j = 0; j < codedSize; i++, j++ )
                    {
                        body.set( zhaoAndKoch.get(table.get(i)), j );
                    }
                }


                // Decode message

                message = new BitString();
                int diagnosis = Message.decodeBody( body, strengthOfEcc, rawMessageSize, message );

                switch ( diagnosis )
                {
                    case ReedSolomon.DIAGNOSIS_HEALTHY:
                        break;

                    case ReedSolomon.DIAGNOSIS_REPAIRED:
                        JOptionPane.showMessageDialog( this.owner,
                                                       "Container was altered!\n"
                                                            + "Though it was possible to restore all the data!",
                                                       "Error",
                                                        JOptionPane.ERROR_MESSAGE, null );
                        break;

                    case ReedSolomon.DIAGNOSIS_UNREPAIRABLE:
                        JOptionPane.showMessageDialog( this.owner,
                                                       "Container was altered!\n"
                                                            + "It was impossible to restore all the data!",
                                                       "Error",
                                                        JOptionPane.ERROR_MESSAGE, null );
                        break;
                }

                if ( message != null )
                {
                    try
                    {
                        txtPreview.setText( Util.toString( message ) );
                    }
                    catch ( NullPointerException x )
                    {
                        JOptionPane.showMessageDialog( this.owner,
                                                       "Message loaded though preview not available!"
                                                        + "\nYou can save your message though"
                                                        + " and view it with any other program!",
                                                       "Error",
                                                        JOptionPane.ERROR_MESSAGE, null );
                    }
                }
                else
                {
                    JOptionPane.showMessageDialog( this.owner,
                                                   "Error loading message!",
                                                   "Error",
                                                   JOptionPane.ERROR_MESSAGE, null );
                }

                this.monitor.setNote( "Message successfully extracted!" );
                this.monitor.setIndeterminate( false );
                this.monitor.setProgress( 100 );
                this.monitor.setString( "100%" );

                this.sleep( 1000 );

                this.finalActions();
            }
            catch ( InterruptedException e )
            {
                // TODO: Do something here!
                ProgressMonitor pm = new ProgressMonitor( null, "", "", 0, 100);
                
            }
        }


        private void finalActions()
        {
            this.monitor.close();
        }

        
        ProgressBox         monitor;
        ExtractionDialog    owner;
    }



    private BitString message;

    private RasterImage image;

    private JPasswordField  txtPassword;

    private JRadioButton    optLSB,
                            optZhaoAndKoch;


    private JTextArea       txtPreview;
    
    private JButton         cmdPreview,
                            cmdSave,
                            cmdCancel;

    private final static int METHOD_LSB = 1;
    private final static int METHOD_ZNK = 2;
}




