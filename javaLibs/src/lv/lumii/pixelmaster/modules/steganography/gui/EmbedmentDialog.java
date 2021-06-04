/**
 * <code>EmbedmentDialog</code> implements message embedment dialog.
 *
 * @author Andrey Zhmakin
 *
 * Created on May 12, 2010 11:41:28 PM
 *
 */

package lv.lumii.pixelmaster.modules.steganography.gui;

import lv.lumii.pixelmaster.modules.steganography.domain.BitsOfImageBySignificance;
import lv.lumii.pixelmaster.modules.steganography.domain.ConformityTable;
import lv.lumii.pixelmaster.modules.steganography.domain.Util;
import lv.lumii.pixelmaster.modules.steganography.domain.BitString;
import lv.lumii.pixelmaster.modules.steganography.domain.Message;
import lv.lumii.pixelmaster.core.api.gui.ImageViewer;
import lv.lumii.pixelmaster.core.api.domain.RasterImage;
import lv.lumii.pixelmaster.modules.steganography.domain.FinalActions;
import lv.lumii.pixelmaster.modules.steganography.domain.ZhaoAndKoch;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.text.DecimalFormat;
import java.util.Arrays;



public class EmbedmentDialog extends JDialog implements ActionListener, AncestorListener
{
    /**
     * Provides dialog for convenient embedment of message into image. 
     *
     * @param owner     Parent frame.
     * @param viewer    ImageViewer used to visualize preview.
     * @param image     Container image.
     */

    public EmbedmentDialog( Frame owner, RasterImage image, Action apply )
    {
        super( owner, "Embed message"/*, Dialog.ModalityType.APPLICATION_MODAL*/ );

//        assert ( owner != null ) : "Provide a non-null owner, please!";

		assert image != null;

//		if ( image == null )
//        {
//            JOptionPane.showMessageDialog( owner,
//                                           "Load image, please!", "Error",
//                                           JOptionPane.ERROR_MESSAGE, null );
//            this.dispose();
//            return;
//        }

        this.owner = owner;
//        this.imageViewer = viewer;
		this.apply = apply;

        this.originalImage = (RasterImage) image.clone();
        this.previewImage  = (RasterImage) image.clone();

        this.zhaoAndKoch = new ZhaoAndKoch              ( this.previewImage );
        this.bitsOfImage = new BitsOfImageBySignificance( this.previewImage ); 

        this.setSize( 600, 700 );
        this.setMinimumSize( new Dimension( 600, 700 ) );

//        this.addWindowListener( new WindowAdapter()
//                                    {
//                                        public void windowClosing( WindowEvent e )
//                                        {
//                                            // TODO: Optimize this
//                                            imageViewer.setImage( originalImage );
//                                        }
//                                    }
//                                );

        this.placeControls();

        this.setVisible( true );
    }


	/**
	 * 
	 * @author Jevgeny Jonas
	 */
	public RasterImage getImage() {
		return previewImage;
	}

    
    /**
     * Places controls in the dialog.
     *
     */

    private void placeControls()
    {
        JTabbedPane tabDataChoice = new JTabbedPane();
        JPanel pnlMessagePanel = new JPanel();

        pnlMessagePanel.setLayout( new BorderLayout() );
        pnlMessagePanel.setBorder( BorderFactory.createTitledBorder( "Select message:" ) );


        this.lblMessageSize = new JLabel();
        this.notifyAboutMessageSize( 0 );
        pnlMessagePanel.add( this.lblMessageSize, BorderLayout.SOUTH );

        this.txtMessageText = new JTextArea( "Copyright © by Andrey Zhmakin", 8, 0 );
        this.txtMessageText.getDocument().addDocumentListener( new MessageTextUpdateListener() );
        
        MessageSourceListener messageSourceListener = new MessageSourceListener();

        this.panTextInput = new JScrollPane( this.txtMessageText );
        this.panTextInput.addComponentListener( messageSourceListener );
        tabDataChoice.addTab( "Unicode Text", null, this.panTextInput, "Conceal text" );
        tabDataChoice.setMnemonicAt( 0, KeyEvent.VK_1 );

        this.pnlFileChoicePanel = new JPanel();
        this.pnlFileChoicePanel.addComponentListener( messageSourceListener );


        this.pnlFileChoicePanel.setLayout( new BoxLayout( this.pnlFileChoicePanel, BoxLayout.X_AXIS ) );

        this.txtMessageFilePath      = new JTextField( "", 1 );
        this.cmdBrowseForMessageFile = new JButton( "Browse" );
        this.cmdLoadMessageFile      = new JButton( "Load" );

        this.cmdBrowseForMessageFile.addActionListener( this );
        this.cmdLoadMessageFile     .addActionListener( this );


        this.txtMessageFilePath.setMaximumSize( new Dimension( Integer.MAX_VALUE, 25 ) );

        this.pnlFileChoicePanel.add( Box.createRigidArea( new Dimension( 5, 5 ) ) );
        this.pnlFileChoicePanel.add( this.txtMessageFilePath );
        this.pnlFileChoicePanel.add( Box.createRigidArea( new Dimension( 5, 5 ) ) );
        this.pnlFileChoicePanel.add( this.cmdBrowseForMessageFile );
        this.pnlFileChoicePanel.add( Box.createRigidArea( new Dimension( 5, 5 ) ) );
        this.pnlFileChoicePanel.add( this.cmdLoadMessageFile );
        this.pnlFileChoicePanel.add( Box.createRigidArea( new Dimension( 5, 5 ) ) );

        tabDataChoice.addTab( "File", null, pnlFileChoicePanel, "Conceal entire file" );
        tabDataChoice.setMnemonicAt( 1, KeyEvent.VK_2 );
        pnlMessagePanel.add( tabDataChoice );


        JPanel pnlSteganography = new JPanel();
        pnlSteganography.setLayout( new BoxLayout( pnlSteganography, BoxLayout.PAGE_AXIS ) );


        JPanel pnlErrorCorrection = new JPanel();
        pnlErrorCorrection.setLayout( new BoxLayout( pnlErrorCorrection, BoxLayout.PAGE_AXIS ) );
        pnlErrorCorrection.setBorder( BorderFactory.createTitledBorder( "Provide strength of error correction:" ) );

        JLabel lblErrorCorrection = new JLabel();
        lblErrorCorrection.setAlignmentX( (float)0.5 );
        pnlErrorCorrection.add( lblErrorCorrection );

        this.sldErrorCorrection = new JSlider( JSlider.HORIZONTAL, 0, 5, 1 );

        this.sldErrorCorrection.setMajorTickSpacing( 1 );
        this.sldErrorCorrection.setMinorTickSpacing( 0 );
        this.sldErrorCorrection.setPaintTrack( true );
        this.sldErrorCorrection.setPaintLabels( true );
        pnlErrorCorrection.add( this.sldErrorCorrection );
        this.sldErrorCorrection.addChangeListener( new SliderToLabelWriterListener( this.sldErrorCorrection,
                                                                                    lblErrorCorrection,
                                                                                    new DecimalFormat( "Level #" ),
                                                                                    new Actions()
                                                                                    {
                                                                                        public void perform()
                                                                                        {
                                                                                            updateCapacityStatistics();
                                                                                        }
                                                                                    }
                                                                                    )
                                                   );

        pnlSteganography.add( pnlErrorCorrection );

        JPanel pnlCapacitySummary = new JPanel();
        pnlCapacitySummary.setLayout( new GridLayout( 0, 1 ) );
        pnlCapacitySummary.setBorder( BorderFactory.createTitledBorder( "Capacity statistics:" ) );

        this.lblStatistics_Service      = new JLabel();
        this.lblStatistics_Message      = new JLabel();
        this.lblStatistics_ECC          = new JLabel();
        this.lblStatistics_Available    = new JLabel();

        pnlCapacitySummary.add( this.lblStatistics_Service );
        pnlCapacitySummary.add( this.lblStatistics_Message );
        pnlCapacitySummary.add( this.lblStatistics_ECC );
        pnlCapacitySummary.add( this.lblStatistics_Available );

        pnlSteganography.add( pnlCapacitySummary );


        JPanel pnlPassword = new JPanel();

        pnlPassword.setBorder( BorderFactory.createTitledBorder( "Enter password:" ) );

        pnlPassword.setLayout( new FlowLayout() );

        pnlPassword.add( new JLabel( "Password: " ) );
        this.txtPassword = new JPasswordField( RECOMMENDED_PASSWORD_LENGTH );
        pnlPassword.add( this.txtPassword );
        pnlPassword.add( new JLabel( "Confirm password: " ) );
        this.txtConfirmPassword = new JPasswordField( RECOMMENDED_PASSWORD_LENGTH );
        pnlPassword.add( this.txtConfirmPassword );

        pnlSteganography.add( pnlPassword );


        JPanel pnlAlgorithmPanel = new JPanel();
        pnlAlgorithmPanel.setLayout( new BorderLayout() );
        pnlAlgorithmPanel.setBorder( BorderFactory.createTitledBorder( "Choose algorithm:" ) );

        this.tabAlgorithmChoiceTabs = new JTabbedPane();

        JComponent LsbInputPanel = new JPanel();

        this.lblLsbInfluence = new JLabel();
        LsbInputPanel.add( this.lblLsbInfluence );

        LsbInputPanel.add( new JLabel( "This method provides you with maximum "
                                            + this.bitsOfImage.getSize()
                                            + " bit(s) to conceal your message." ) );

        
        tabAlgorithmChoiceTabs.addTab( "LSB", null, LsbInputPanel, "Conceal in Least Significant Bits of image" );
        tabAlgorithmChoiceTabs.setMnemonicAt( 0, KeyEvent.VK_1 );


        JPanel pnlZnKInput = new JPanel();
        //pnlZnKInput.setLayout( new GridLayout( 0, 1 ) );
        pnlZnKInput.setLayout( new BoxLayout( pnlZnKInput, BoxLayout.PAGE_AXIS ) );

        JPanel pnlZnKRobustness = new JPanel();
        pnlZnKRobustness.setLayout( new BoxLayout( pnlZnKRobustness, BoxLayout.PAGE_AXIS ) );
        pnlZnKRobustness.setBorder( BorderFactory.createTitledBorder( "Set level of robustness:" ) );

        JLabel lblZnKRobustness = new JLabel();
        lblZnKRobustness.setAlignmentX( (float)0.5 );
        pnlZnKRobustness.add( lblZnKRobustness );
        
        sldZnKRobustness = new JSlider( JSlider.HORIZONTAL, 0, 100, 25 );
        sldZnKRobustness.setMajorTickSpacing( 10 );
        sldZnKRobustness.setMinorTickSpacing(  5 );
        sldZnKRobustness.setPaintTrack( true );
        sldZnKRobustness.setPaintTicks( true );
        sldZnKRobustness.setPaintLabels( true );
        pnlZnKRobustness.add( sldZnKRobustness );

        sldZnKRobustness.addChangeListener( new SliderToLabelWriterListener( sldZnKRobustness,
                                                                             lblZnKRobustness,
                                                                             new DecimalFormat( "#'%'" )
                                                                             )
                                            );

        pnlZnKInput.add( pnlZnKRobustness );

        pnlZnKInput.add( new JLabel( "This method provides you with maximum "
                                        + this.zhaoAndKoch.getSize()
                                        + " bit(s) to conceal your message." ) );

        tabAlgorithmChoiceTabs.addTab( "Zhao & Koch", null, pnlZnKInput,
                                       "Conceal according to method proposed by Zhao & Koch" );
        tabAlgorithmChoiceTabs.setMnemonicAt( 1, KeyEvent.VK_2 );

        tabAlgorithmChoiceTabs.addChangeListener( new ChangeListener()
                                                    {
                                                        public void stateChanged( ChangeEvent e )
                                                        {
                                                            updateCapacityStatistics();
                                                        }
                                                    }
                                                  );

        pnlAlgorithmPanel.add( tabAlgorithmChoiceTabs );

        
        pnlSteganography.add( pnlAlgorithmPanel );

        
        JPanel pnlCommandPanel = new JPanel();
        pnlCommandPanel.setLayout( new FlowLayout()   );

//        this.cmdPreview  = new JButton( "Preview" );
        this.cmdApply    = new JButton( "Apply"   );
//        this.cmdCancel   = new JButton( "Cancel"  );

//        pnlCommandPanel.add( this.cmdPreview );
        pnlCommandPanel.add( this.cmdApply   );
//        pnlCommandPanel.add( this.cmdCancel  );

//        this.cmdPreview .addActionListener( this );
        this.cmdApply   .addActionListener( this );
//        this.cmdCancel  .addActionListener( this );
       
        this.getContentPane().setLayout( new BorderLayout() );

        JSplitPane splInputPane = new JSplitPane( JSplitPane.VERTICAL_SPLIT, true, pnlMessagePanel, pnlSteganography );

        splInputPane.setDividerLocation( 0.5 );

        this.getContentPane().add( splInputPane );
        this.getContentPane().add( pnlCommandPanel, BorderLayout.SOUTH );
    }



    public void actionPerformed( ActionEvent e )
    {
        // Three buttons in the bottom of my dialog
//        if ( e.getSource() == this.cmdCancel )
//        {
//            // TODO: Set condition of restoration
//            this.imageViewer.setImage( this.originalImage );
//
//            this.dispose();
//        }
        /*else*/ if ( e.getSource() == this.cmdApply )
        {
            this.performPreview();

			this.previewImage.copyTo( this.originalImage );
//            this.imageViewer.setImage( this.originalImage );
        }
//        else if ( e.getSource() == this.cmdPreview )
//        {
//            this.performPreview();
//        }
        // Buttons at File tab of message input section
        else if ( e.getSource() == this.cmdBrowseForMessageFile )
        {
            final JFileChooser fc = new JFileChooser();
            fc.setFileSelectionMode( JFileChooser.FILES_ONLY );

            if ( fc.showOpenDialog( this.owner ) == JFileChooser.APPROVE_OPTION )
            {
                this.txtMessageFilePath.setText( fc.getSelectedFile().getAbsolutePath() );
            }
        }
        else if ( e.getSource() == this.cmdLoadMessageFile )
        {
            this.setEnabled( false );
            
            String filename = this.txtMessageFilePath.getText();
            this.message = new BitString( 0 );
            BitStringLoader.loadFile( new File(filename), this.message, this, new RecoilOfFileLoading(this) );
        }
    }



    /**
     * Actions to do on push of Perform button
     */

    private void performPreview()
    {
        //this.imageViewer.setImage( this.originalImage );
		this.originalImage.copyTo( this.previewImage );

        assert ( this.previewImage != null );

        if ( this.message == null )
        {
            JOptionPane.showMessageDialog( this,
                                           "Load a file, please!",
                                           "Error",
                                           JOptionPane.ERROR_MESSAGE,
                                           null );
            return;
        }

        // read and check password

        if ( !Arrays.equals( this.txtPassword.getPassword(), this.txtConfirmPassword.getPassword() ) )
        {
            JOptionPane.showMessageDialog( this,
                                           "Password doesn't match its confirmation!",
                                           "Error",
                                           JOptionPane.ERROR_MESSAGE,
                                           null );
            this.txtPassword.requestFocus();
            return;
        }

        long key = Util.getKeyFromPassword( this.txtPassword.getPassword() );

        // Determinate algorithm, initialize it and choose its metrics

        String algorithmTabTitle = this.tabAlgorithmChoiceTabs.getTitleAt( this.tabAlgorithmChoiceTabs.getSelectedIndex() );

        int errorCorrectionLevel = this.sldErrorCorrection.getValue() * 2 * 10;

        BitString msg = Message.produceLowLevelMessage( this.message, errorCorrectionLevel );

        if ( algorithmTabTitle.equals( "LSB" ) )
        {
            // TODO: Move to separate thread
            {
                assert ( msg.size() <= this.bitsOfImage.getSize() ) : "Message too long!";

                this.bitsOfImage.initConformityTables( key );

                try
                {
                    for ( int i = 0; i < msg.size(); i++ )
                    {
                        this.bitsOfImage.set( i, msg.get(i) );
                    }
                }
                catch ( BitsOfImageBySignificance.IndexOutOfBoundsException x )
                {
                    // Do nothing, as this couldn't happen!
                }

                // show preview
//                this.imageViewer.setImage( this.previewImage );
				apply.actionPerformed(null);
            }
        }
        else if ( algorithmTabTitle.equals( "Zhao & Koch" ) )
        {
            // TODO: Move to separate thread
            {
                assert ( msg.size() <= this.zhaoAndKoch.getSize() ) : "Message too long!";

                ConformityTable table = new ConformityTable( 0, this.zhaoAndKoch.getSize(), key );

                this.zhaoAndKoch.setRobustness( this.sldZnKRobustness.getValue() * 0.01 );

                for ( int i = 0; i < msg.size(); i++ )
                {
                    this.zhaoAndKoch.set( msg.get(i), table.get(i) );
                }

                // show preview
//                this.imageViewer.setImage( this.previewImage );
				apply.actionPerformed(null);
            }
        }
        else
        {
            assert false : "Choice of algorithm failed!";
        }
    }


    /**
     * Actions to do when the file is loaded. 
     */

    private class RecoilOfFileLoading extends FinalActions
    {
        RecoilOfFileLoading( EmbedmentDialog belovedDialog )
        {
            this.belovedDialog = belovedDialog;
        }

        

        public void doOnSuccess()
        {
            this.belovedDialog.notifyAboutMessageSize( this.belovedDialog.message.size() );
            this.belovedDialog.updateCapacityStatistics();

            this.belovedDialog.setEnabled( true );
            this.belovedDialog.bringOnTop();
        }



        public void doOnFailure()
        {
            JOptionPane.showMessageDialog( this.belovedDialog,
                                           "Filename you provided is wrong!\nMessage not loaded!",
                                           "Error",
                                           JOptionPane.ERROR_MESSAGE,
                                           null );
            
            this.belovedDialog.message = null;
            this.belovedDialog.notifyAboutMessageSize( null );

            this.belovedDialog.setEnabled( true );
            this.belovedDialog.bringOnTop();
        }


        public void doOnAbort()
        {
            this.belovedDialog.message = null;
            this.belovedDialog.lblMessageSize.setText( "Loading aborted! No message loaded!" );
            
            this.belovedDialog.setEnabled( true );
            this.belovedDialog.bringOnTop();
        }

        private EmbedmentDialog belovedDialog;
    }



    private void updateCapacityStatistics()
    {
        if ( !this.isVisible() )
        { // We need all elements to be shown
            return;
        }

        if ( this.message != null )
        {
            int capacity;

            String algorithmTabTitle
                    = this.tabAlgorithmChoiceTabs.getTitleAt( this.tabAlgorithmChoiceTabs.getSelectedIndex() );

            if      ( algorithmTabTitle.equals( "LSB" ) )           capacity = this.bitsOfImage.getSize();
            else if ( algorithmTabTitle.equals( "Zhao & Koch" ) )   capacity = this.zhaoAndKoch.getSize();
            else                                                    capacity = -1;

            int eccOverhead = Message.predictEccOverhead( this.message.size(), this.sldErrorCorrection.getValue() * 2 * 10 );


            this.lblStatistics_Service  .setText( "Size of service data is "
                                                  + Message.SERVICE_DATA_SIZE + " bit(s)" );

            this.lblStatistics_Message  .setText( "Size of your message is " + this.message.size() + " bit(s)" );

            int total = Message.SERVICE_DATA_SIZE + this.message.size() + eccOverhead;

            this.lblStatistics_ECC      .setText( "Error correction adds " + eccOverhead + " bit(s) to this, bringing "
                                                    + "total data size to "
                                                    + total + " bit(s)" );
            
            int available = capacity - total;

            if ( available >= 0 )
            {
                this.lblStatistics_Available.setText( available + " bit(s) available." );
                this.lblStatistics_Available.setForeground( (new JLabel()).getForeground() );
                this.cmdApply  .setEnabled( true );
//                this.cmdPreview.setEnabled( true );
            }
            else
            {
                this.lblStatistics_Available.setText( "Message exceeds maximum size for " + (-available) + " bit(s)." );
                this.lblStatistics_Available.setForeground( Color.RED );
                this.cmdApply  .setEnabled( false );
//                this.cmdPreview.setEnabled( false );
            }

            int layersOccupied = total / this.bitsOfImage.getLayerSize();

            String influence;

            if      ( layersOccupied <= 3 ) influence = "Negligible";
            else if ( layersOccupied <= 5 ) influence = "Severe";
            else if ( layersOccupied <= 7 ) influence = "Devastating";
            else                            influence = "N/A";

            this.lblLsbInfluence.setText( "Under this amount of data, influence on the image will be: " + influence );
        }
        else
        {
            this.lblStatistics_Service  .setText( "   " );
            this.lblStatistics_Message  .setText( "   " );
            this.lblStatistics_ECC      .setText( "   " );
            this.lblStatistics_Available.setText( "   " );

            this.lblLsbInfluence.setText( "Under this amount of data, influence on the image will be: N/A" );

            this.cmdApply  .setEnabled( false );
//            this.cmdPreview.setEnabled( false );
        }
    }



    private void notifyAboutMessageSize( Integer size )
    {
        if ( size != null )
        {
            this.lblMessageSize.setText( "Message size is " + size + " bit(s)" );
            this.lblMessageSize.setForeground( (new JLabel()).getForeground() );
        }
        else
        {
            this.lblMessageSize.setText( "No message available! Load file, please!" );
            this.lblMessageSize.setForeground( Color.RED );
        }
    }



    public void bringOnTop()
    {
        this.setAlwaysOnTop( true );
        this.setAlwaysOnTop( false );
    }



    public void ancestorAdded  ( AncestorEvent event ) {}
    public void ancestorMoved  ( AncestorEvent event ) {}
    public void ancestorRemoved( AncestorEvent event ) {}



    RasterImage originalImage,
                previewImage;

    
    ZhaoAndKoch               zhaoAndKoch;
    BitsOfImageBySignificance bitsOfImage;


    private Frame owner;

//    private ImageViewer imageViewer;
	private Action apply;

    private BitString message;

    private JTabbedPane     tabAlgorithmChoiceTabs;
    
    private JPanel          pnlFileChoicePanel;
    private JScrollPane     panTextInput;
    
    private JLabel          lblMessageSize;

    private JTextArea       txtMessageText;


    private JPasswordField  txtPassword,
                            txtConfirmPassword;

    private JSlider         sldErrorCorrection;

    private JLabel          lblStatistics_Service;
    private JLabel          lblStatistics_Message;
    private JLabel          lblStatistics_ECC;
    private JLabel          lblStatistics_Available;

    private JLabel          lblLsbInfluence;

    private JSlider         sldZnKRobustness;

    private JButton     //    cmdPreview,
                            cmdApply;
                        //    cmdCancel;
    
    private JTextField      txtMessageFilePath;

    private JButton         cmdBrowseForMessageFile,
                            cmdLoadMessageFile;


    public final static int RECOMMENDED_PASSWORD_LENGTH = 16;



    class MessageTextUpdateListener implements DocumentListener
    {
        public MessageTextUpdateListener()
        {
            this.doMyBusiness();
        }
        
        public void insertUpdate ( DocumentEvent e ) { this.doMyBusiness(); }
        public void removeUpdate ( DocumentEvent e ) { this.doMyBusiness(); }
        public void changedUpdate( DocumentEvent e ) { this.doMyBusiness(); }

        private void doMyBusiness()
        {
            message = BitString.encodeString( txtMessageText.getText() );
            notifyAboutMessageSize( message.size() );

            updateCapacityStatistics();
        }
    }



    class MessageSourceListener implements ComponentListener
    {
        public void componentShown(ComponentEvent e)
        {
            if ( e.getSource() == panTextInput )
            {
                message = BitString.encodeString( txtMessageText.getText() );
                notifyAboutMessageSize( message.size() );
            }
            else if ( e.getSource() == pnlFileChoicePanel )
            {
                notifyAboutMessageSize( null );
                message = null;
            }

            updateCapacityStatistics();
        }
        
        public void componentHidden ( ComponentEvent e ) { }
        public void componentMoved  ( ComponentEvent e ) { }
        public void componentResized( ComponentEvent e ) { }
    }



    abstract class Actions
    {
        abstract public void perform();
    }


    
    class SliderToLabelWriterListener implements ChangeListener
    {
        public SliderToLabelWriterListener( JSlider slider, JLabel result, DecimalFormat format )
        {
            this.slider  = slider;
            this.result  = result;
            this.format  = format;
            this.actions = null;
            
            this.updateResult();
        }


        public SliderToLabelWriterListener( JSlider slider, JLabel result, DecimalFormat format, Actions actions )
        {
            this.slider  = slider;
            this.result  = result;
            this.format  = format;
            this.actions = actions;

            this.updateResult();
        }

        public void stateChanged( ChangeEvent e )
        {
            this.updateResult();
        }

        private void updateResult()
        {
            this.result.setText( format.format( this.slider.getValue() ) );

            if ( this.actions != null )
            {
                this.actions.perform();
            }
        }

        private JSlider         slider;
        private JLabel          result;
        private DecimalFormat   format;
        private Actions         actions;
    }
}




