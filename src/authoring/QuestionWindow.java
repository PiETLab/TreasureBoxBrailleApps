package authoring;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.logging.Level;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import commands.QuestionCommand;
import org.apache.commons.io.FilenameUtils;


public class QuestionWindow extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public JFrame frame= new JFrame("Enter New Question");
	private JComboBox<String> buttons = new JComboBox<String>();
	private JTextArea introField = new JTextArea(5,18);
	private JTextField brailleField  = new JTextField();
	private JTextArea repeatField= new JTextArea(5,18);
	private JTextArea correctField= new JTextArea(5,18);
	private JTextField buttonField = new JTextField();
	private JLabel introLabel = new JLabel("Introduction :");
	private JLabel brailleLabel= new JLabel("Braille Text:");
	private JLabel correctButton= new JLabel("Correct Button:");
	private JLabel correctLabel= new JLabel("For Correct Answer:");
	private JLabel incorrectLabel= new JLabel("For Incorrect Answer:");
	JButton text= new JButton("Enter Text");
	JButton play= new JButton("Select Audio");
	JButton record= new JButton("Record Audio");
	JButton textIncorrect= new JButton("Enter Text");
	JButton recordIncorrect= new JButton("Record Audio");
	JButton playIncorrect= new JButton("Select Audio");
	JButton textCorrect= new JButton("Enter Text");
	JButton recordCorrect= new JButton("Record Audio");
	JButton playCorrect= new JButton("Select Audio");
	private JButton ok;
	private JButton cancel;
	
	private GUI gui;
	private String strNumOfButtons;
	private int index=0;
	private boolean isRecording = false;
	private boolean noRecording = true;
	private boolean recordFlag= false;
	private String introAudio= "none";
	private String correctAudio= "none";
	private String incorrectAudio= "none";
	private String introSound= "none";
	private String correctSound= "none";
	private String incorrectSound= "none";
	
	ThreadRunnable thread = null;
	File file = null;
	

	/**
	 * Create the application.
	 * This constructor is used for NewQuestionListener
	 * @param gui
	 *            the gui object to be used
	 */
	public QuestionWindow(GUI gui){
		this.gui= gui;
		strNumOfButtons = gui.getSettingsPanel().getButtonField();
		initialize();
	}
	
	
	/**
	 * Create the application.
	 * This constructor is used for Edit Question
	 * @param a
	 *          the question command to be evaluated
	 */
	public QuestionWindow(QuestionCommand a){
		this.introField.setText(a.getIntroField());
		if(a.getIntroField().length()>0 && a.getIntroField() != "none")
		{
			text.setText("Edit Text");
			text.setForeground(Color.magenta);
		}
		else if(a.getIntroAudio()!= "none")
		{
			record.setText("Rerecord Audio");
			record.setForeground(Color.magenta);
		}
		else if(a.getIntroSound()!="none")
		{
			play.setText("Reselect Audio");
			play.setForeground(Color.magenta);
		}
		
		if(a.getRepeatField().length()>0 && a.getRepeatField() != "none")
		{
			textIncorrect.setText("Edit Text");
			textIncorrect.setForeground(Color.magenta);
		}
		else if(a.getIncorrectAudio()!= "none")
		{
			recordIncorrect.setText("Rerecord Audio");
			recordIncorrect.setForeground(Color.magenta);
		}
		else if(a.getIncorrectSound()!="none")
		{
			playIncorrect.setText("Reselect Audio");
			playIncorrect.setForeground(Color.magenta);
		}
		if(a.getCorrectField().length()>0 && a.getCorrectField() != "none")
		{
			textCorrect.setText("Edit Text");
			textCorrect.setForeground(Color.magenta);
		}
		else if(a.getCorrectAudio()!= "none")
		{
			recordCorrect.setText("Rerecord Audio");
			recordCorrect.setForeground(Color.magenta);
		}
		else if(a.getCorrectSound()!="none")
		{
			playCorrect.setText("Reselect Audio");
			playCorrect.setForeground(Color.magenta);
		}
		
		
		
		this.buttons.setEnabled(false);
		this.brailleField.setText(a.getBrailleField());
		this.repeatField.setText(a.getRepeatField());
		this.correctField.setText(a.getCorrectField());
		this.index=a.getCorrectButton()-1;
		strNumOfButtons = a.getTotalButtons();
		this.gui= a.getGUI();
		initialize();
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	public void initialize()  {

		//frame.setDefaultCloseOperation(frame.DISPOSE_ON_CLOSE);	
		frame.setPreferredSize(new Dimension(630,280));
		frame.setMaximumSize(new Dimension(630,280));
		frame.setMinimumSize(new Dimension(630,280));
	//	frame.setLocation(300,300);
		frame.setResizable(false);
		frame.pack();
		frame.setVisible(true);
		
		
		// confirmation dialog when frame is closed
	frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
	frame.addWindowListener(new WindowAdapter() {
	    @Override
	    public void windowClosing(WindowEvent we)
	    { 
	        String ObjButtons[] = {"Yes","No"};
	        int PromptResult = JOptionPane.showOptionDialog(null,"Do you want to add this question in scenario?","Confirmation",JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE,null,ObjButtons,ObjButtons[1]);
	        if(PromptResult==JOptionPane.YES_OPTION)
	        {
	        	frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	        	//frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSED));
	        }
	        else
	        	frame.setVisible(false);
	    }
	});
			
		
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
	   
		frame.getContentPane().setLayout(gridBagLayout);
		
		/**
		 * Introduction label
		 */
		GridBagConstraints gbc_introLabel = new GridBagConstraints();
		gbc_introLabel.anchor = GridBagConstraints.WEST;
		gbc_introLabel.insets = new Insets(10, 10, 5, 5);
		gbc_introLabel.gridx = 3;
		gbc_introLabel.gridy = 1;
		frame.getContentPane().add(introLabel, gbc_introLabel);
		
		
		/**
		 * Braille Text label
		 */	
		GridBagConstraints gbc_BrailleText = new GridBagConstraints();
		gbc_BrailleText.anchor = GridBagConstraints.WEST;
		gbc_BrailleText.insets = new Insets(10, 10, 0, 5);
		gbc_BrailleText.gridx = 3;
		gbc_BrailleText.gridy = 3;
		frame.getContentPane().add(brailleLabel, gbc_BrailleText);
		
		/**
		 * Correct Button label
		 */
		GridBagConstraints gbc_correctLabel = new GridBagConstraints();
		gbc_correctLabel.anchor = GridBagConstraints.WEST;
		gbc_correctLabel.insets = new Insets(10, 10, 5, 5);
		gbc_correctLabel.gridx = 3;
		gbc_correctLabel.gridy = 5;
		frame.getContentPane().add(correctButton, gbc_correctLabel);
		
		/**
		 * Incorrect Button label
		 */	
		GridBagConstraints gbc_incorrectLabel  = new GridBagConstraints();
		gbc_incorrectLabel.anchor = GridBagConstraints.WEST;
		gbc_incorrectLabel .insets = new Insets(10, 10, 5, 5);
		gbc_incorrectLabel .gridx = 3;
		gbc_incorrectLabel .gridy = 7;
		frame.getContentPane().add(incorrectLabel , gbc_incorrectLabel );
		
		
		/**
		 * Correct Button label
		 */
		GridBagConstraints gbc_correctLabel1  = new GridBagConstraints();
		gbc_correctLabel1.anchor = GridBagConstraints.WEST;
		gbc_correctLabel1 .insets = new Insets(10, 10, 5, 5);
		gbc_correctLabel1 .gridx = 3;
		gbc_correctLabel1 .gridy = 8;
		frame.getContentPane().add(correctLabel , gbc_correctLabel1 );
		
		
		/**
		 * Enter Text Button for Introduction
		 */
		text.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			
				// Creates a text area that wraps properly and scrolls vertically only
				introField.setAutoscrolls(true);
				introField.setWrapStyleWord(true); 
				JScrollPane introPane = new JScrollPane(introField);
				introField.getAccessibleContext().setAccessibleDescription("This is where you ask the question");
				patch(introField);
			

				//Set initial focus to introField
				introField.addAncestorListener(new AncestorListener(){

					@Override
					public void ancestorAdded(AncestorEvent event) {
						// TODO Auto-generated method stub
						introField.requestFocusInWindow();			
					}

					@Override
					public void ancestorMoved(AncestorEvent event) {
						// TODO Auto-generated method stub

					}

					@Override
					public void ancestorRemoved(AncestorEvent event) {
						// TODO Auto-generated method stub

					}

				});
				
				//Open intro field
		//	 JOptionPane.showMessageDialog(null, introPane, "Enter introduction text",  
		//			JOptionPane.PLAIN_MESSAGE);	

		int result = JOptionPane.showConfirmDialog(null, introPane, "Enter introduction text",
						JOptionPane.OK_CANCEL_OPTION);
		if (result == JOptionPane.OK_OPTION) {
			record.setEnabled(false);
			play.setEnabled(false);
		}

			}
		});
			
		GridBagConstraints gbc_text = new GridBagConstraints();
		gbc_text.fill = GridBagConstraints.HORIZONTAL;
		gbc_text.insets = new Insets(10, 10, 5, 5);
		gbc_text.gridx = 4;
		gbc_text.gridy = 1;
		frame.getContentPane().add(text, gbc_text);
		
		/**
		 * Record Audio Button
		 */
		record.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				recordAudio();
				if (file != null && recordFlag==true)
				{
					introAudio= file.toString();
					String basename = FilenameUtils.getBaseName(introAudio);
					record.setText(basename+".wav");
					introField.setText("");
					introSound="none";
					text.setEnabled(false);
					play.setEnabled(false);
				//	gui.getLeftPanel().addItem(new SoundCommand(file.toString()));
				//	gui.counterMap.put("Record Audio", gui.counterMap.get("Record Audio") + 1);
				}	
			}
		});
		GridBagConstraints gbc_record = new GridBagConstraints();
		gbc_record.anchor = GridBagConstraints.WEST;
		gbc_record.insets = new Insets(10, 10, 5, 5);
		gbc_record.gridx = 5;
		gbc_record.gridy = 1;
		frame.getContentPane().add(record, gbc_record);
		
		/**
		 * Play Audio Button
		 */
		play.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				JFileChooser load = new JFileChooser();
				FileNameExtensionFilter wavFileFilter = new FileNameExtensionFilter("wav files (*.wav)", "wav");
				load.addChoosableFileFilter(wavFileFilter);
				load.setFileFilter(wavFileFilter);				
				load.showOpenDialog(null);
				file = load.getSelectedFile();
				if (file != null)
				{
					introSound = file.toString();
					String basename = FilenameUtils.getBaseName(introSound);
					play.setText(basename+".wav");
					introField.setText("");
					introAudio="none";
					text.setEnabled(false);
					record.setEnabled(false);
				//	gui.getLeftPanel().addItem(new SoundCommand(file.toString()));
				//	gui.counterMap.put("Sound", gui.counterMap.get("Sound") + 1);					
				}

			}
		});
		GridBagConstraints gbc_play = new GridBagConstraints();
		gbc_play.anchor = GridBagConstraints.WEST;
		gbc_play.insets = new Insets(10, 10, 5, 5);
		gbc_play.gridx = 6;
		gbc_play.gridy = 1;
		frame.getContentPane().add(play, gbc_play);
		

		/**
		 * Enter Text Button for incorrect text
		 */
		textIncorrect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				repeatField.setLineWrap(true);
				repeatField.setWrapStyleWord(true);
				repeatField.setAutoscrolls(true);
				JScrollPane repeatPane= new JScrollPane(repeatField);
				repeatField.getAccessibleContext().setAccessibleDescription("This is what is said everytime an incorrect answer is given");
				patch(repeatField);
				
	
				//Set initial focus to repeatField
				repeatField.addAncestorListener(new AncestorListener(){

					@Override
					public void ancestorAdded(AncestorEvent event) {
						// TODO Auto-generated method stub
						repeatField.requestFocusInWindow();			
					}

					@Override
					public void ancestorMoved(AncestorEvent event) {
						// TODO Auto-generated method stub

					}

					@Override
					public void ancestorRemoved(AncestorEvent event) {
						// TODO Auto-generated method stub

					}

				});
				
			
			//	JOptionPane.showMessageDialog(null,repeatPane, "Enter Text for Incorrect Answer", JOptionPane.PLAIN_MESSAGE);
			
				
		int result = JOptionPane.showConfirmDialog(null, repeatPane, "Enter Text for Incorrect Answer",
						JOptionPane.OK_CANCEL_OPTION);
		if (result == JOptionPane.OK_OPTION) {
			recordIncorrect.setEnabled(false);
			playIncorrect.setEnabled(false);
		}

			
			}
		});
		GridBagConstraints gbc_textIncorrect = new GridBagConstraints();
		gbc_textIncorrect.fill = GridBagConstraints.HORIZONTAL;
		gbc_textIncorrect.insets = new Insets(10, 10, 5, 5);
		gbc_textIncorrect.gridx = 4;
		gbc_textIncorrect.gridy = 7;
		frame.getContentPane().add(textIncorrect, gbc_textIncorrect);
		
		/**
		 * Record Audio Button
		 */	
		recordIncorrect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				recordAudio();
				if (file != null && recordFlag==true)
				{
					incorrectAudio= file.toString();
					String basename = FilenameUtils.getBaseName(incorrectAudio);
					recordIncorrect.setText(basename+".wav");
					textIncorrect.setEnabled(false);
					playIncorrect.setEnabled(false);
				//	gui.getLeftPanel().addItem(new SoundCommand(file.toString()));
				//	gui.counterMap.put("Record Audio", gui.counterMap.get("Record Audio") + 1);
				}	
			}
		});
		GridBagConstraints gbc_recordIncorrect = new GridBagConstraints();
		gbc_recordIncorrect.anchor = GridBagConstraints.WEST;
		gbc_recordIncorrect.insets = new Insets(10, 10, 5, 5);
		gbc_recordIncorrect.gridx = 5;
		gbc_recordIncorrect.gridy = 7;
		frame.getContentPane().add(recordIncorrect, gbc_recordIncorrect);
		
		/**
		 * Play Audio Button
		 */
		
		playIncorrect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				
				
				JFileChooser load = new JFileChooser();
				FileNameExtensionFilter wavFileFilter = new FileNameExtensionFilter("wav files (*.wav)", "wav");
				load.addChoosableFileFilter(wavFileFilter);
				load.setFileFilter(wavFileFilter);				
				load.showOpenDialog(null);
				file = load.getSelectedFile();
				if (file != null)
				{
					incorrectSound = file.toString();
					String basename = FilenameUtils.getBaseName(incorrectSound);
					playIncorrect.setText(basename+".wav");
					textIncorrect.setEnabled(false);
					recordIncorrect.setEnabled(false);
				//	gui.getLeftPanel().addItem(new SoundCommand(file.toString()));
				//	gui.counterMap.put("Sound", gui.counterMap.get("Sound") + 1);					
				}
				
			}
		});
		GridBagConstraints gbc_playIncorrect = new GridBagConstraints();
		gbc_playIncorrect.anchor = GridBagConstraints.WEST;
		gbc_playIncorrect.insets = new Insets(10, 10, 5, 5);
		gbc_playIncorrect.gridx = 6;
		gbc_playIncorrect.gridy = 7;
		frame.getContentPane().add(playIncorrect, gbc_playIncorrect);
		
		/**
		 * Enter Text Button for correct text
		 */
		
		textCorrect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				correctField.setLineWrap(true);
				correctField.setWrapStyleWord(true);
				correctField.setAutoscrolls(true);
				JScrollPane correctPane= new JScrollPane(correctField);
				correctField.getAccessibleContext().setAccessibleDescription("This is what is said everytime an correct answer is given");
				patch(correctField);
				

				//Set initial focus to repeatField
				correctField.addAncestorListener(new AncestorListener(){

					@Override
					public void ancestorAdded(AncestorEvent event) {
						// TODO Auto-generated method stub
						correctField.requestFocusInWindow();			
					}

					@Override
					public void ancestorMoved(AncestorEvent event) {
						// TODO Auto-generated method stub

					}

					@Override
					public void ancestorRemoved(AncestorEvent event) {
						// TODO Auto-generated method stub

					}

				});
			
		//	JOptionPane.showMessageDialog(null,correctPane, "Enter Text for Correct Answer", JOptionPane.PLAIN_MESSAGE);
			
		int result = JOptionPane.showConfirmDialog(null, correctPane, "Enter Text for Correct Answer",
						JOptionPane.OK_CANCEL_OPTION);
		if (result == JOptionPane.OK_OPTION) {
			recordCorrect.setEnabled(false);
			playCorrect.setEnabled(false);
		}
		
			
			}
		});
		GridBagConstraints gbc_textCorrect = new GridBagConstraints();
		gbc_textCorrect.fill = GridBagConstraints.HORIZONTAL;
		gbc_textCorrect.insets = new Insets(10, 10, 5, 5);
		gbc_textCorrect.gridx = 4;
		gbc_textCorrect.gridy = 8;
		frame.getContentPane().add(textCorrect, gbc_textCorrect);
		
		
		/**
		 * Record Audio Button
		 */
		recordCorrect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				
				
				recordAudio();
				if (file != null && recordFlag==true)
				{
					correctAudio= file.toString();
					String basename = FilenameUtils.getBaseName(correctAudio);
					recordCorrect.setText(basename+".wav");
					textCorrect.setEnabled(false);
					playCorrect.setEnabled(false);
				//	gui.getLeftPanel().addItem(new SoundCommand(file.toString()));
				//	gui.counterMap.put("Record Audio", gui.counterMap.get("Record Audio") + 1);
				}
				
			}
		});
		GridBagConstraints gbc_recordCorrect = new GridBagConstraints();
		gbc_recordCorrect.anchor = GridBagConstraints.WEST;
		gbc_recordCorrect.insets = new Insets(10, 10, 5, 5);
		gbc_recordCorrect.gridx = 5;
		gbc_recordCorrect.gridy = 8;
		frame.getContentPane().add(recordCorrect, gbc_recordCorrect);
		
		
		/**
		 * Play Audio Button
		 */
	
		playCorrect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				
				JFileChooser load = new JFileChooser();
				FileNameExtensionFilter wavFileFilter = new FileNameExtensionFilter("wav files (*.wav)", "wav");
				load.addChoosableFileFilter(wavFileFilter);
				load.setFileFilter(wavFileFilter);				
				load.showOpenDialog(null);
				file = load.getSelectedFile();
				if (file != null)
				{
					correctSound = file.toString();
					String basename = FilenameUtils.getBaseName(correctSound);
					playCorrect.setText(basename+".wav");
					textCorrect.setEnabled(false);
					recordCorrect.setEnabled(false);
				//	gui.getLeftPanel().addItem(new SoundCommand(file.toString()));
				//	gui.counterMap.put("Sound", gui.counterMap.get("Sound") + 1);					
				}
				
				
			}
		});
		GridBagConstraints gbc_playCorrect = new GridBagConstraints();
		gbc_playCorrect.anchor = GridBagConstraints.WEST;
		gbc_playCorrect.insets = new Insets(10, 10, 5, 5);
		gbc_playCorrect.gridx = 6;
		gbc_playCorrect.gridy = 8;
		frame.getContentPane().add(playCorrect, gbc_playCorrect);
		
		
		/**
		 * This is what will be displayed on the braille cells
		 */
		brailleField.getAccessibleContext().setAccessibleDescription("This is what will be displayed on the braille cells");
		GridBagConstraints gbc_brailleField = new GridBagConstraints();
		gbc_brailleField.fill = GridBagConstraints.HORIZONTAL;
		gbc_brailleField.insets = new Insets(0, 10, 5, 5);
		gbc_brailleField.gridx = 4;
		gbc_brailleField.gridy = 3;
		frame.getContentPane().add(brailleField, gbc_brailleField);
		brailleField.setColumns(10);
		
		
		/**
		 * This is the correct answer button for this question
		 */
		buttonField = new JTextField();
		
		
	//	String strNumOfButtons = gui.getSettingsPanel().getButtonField();
    	if (strNumOfButtons == null || strNumOfButtons.isEmpty()) {
    		return;
    	}

    	int numOfButtons = Integer.parseInt(strNumOfButtons);
    	
    	buttons.removeAllItems();
    	for (int i = 0; i <numOfButtons; i++) {
    		buttons.addItem("Button " + (i + 1));
    	}
    	buttons.setSelectedIndex(this.index);
    
		this.buttons.getAccessibleContext().setAccessibleDescription("This is the correct answer button for this question");
		GridBagConstraints gbc_comboBox = new GridBagConstraints();
		gbc_comboBox.insets = new Insets(0, 10, 5, 5);
		gbc_comboBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBox.gridx = 4;
		gbc_comboBox.gridy = 5;
		frame.getContentPane().add(buttons, gbc_comboBox);
		
		
		brailleField.setMinimumSize(new Dimension(200, 15));
		buttonField.setMinimumSize(new Dimension(200, 15));
		buttons.setMinimumSize(new Dimension(200, 15));

	ok = new JButton("OK");
	ok.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent arg0) {
		
			frame.dispose();	
		}
	});
	GridBagConstraints gbc_ok = new GridBagConstraints();
	gbc_ok.insets = new Insets(10, 10, 5, 5);
	gbc_ok.gridx = 4;
	gbc_ok.gridy = 12;
	frame.getContentPane().add(ok, gbc_ok);
	
	cancel = new JButton("Cancel");
	cancel.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent arg0) {
			frame.setVisible(false);
		}
	});
	
	GridBagConstraints gbc_cancel = new GridBagConstraints();
	gbc_cancel.insets = new Insets(10, 10, 5, 5);
	gbc_cancel.gridx = 5;
	gbc_cancel.gridy = 12;
	frame.getContentPane().add(cancel, gbc_cancel);	
}
	
	public static void patch(Component c) {
		c.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, null);
		c.setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, null);
	}
		
	
	
	//-------------------------------------------------------------------------------------------------------------------
	////////////////////////////////////////////////// GETTERS/////////////////////////////////////////////////////////
	//---------------------------------------------------------------------------------------------------------------------
	public JComboBox<String> getButton()
	{
		return buttons;
	}
	
	public JTextField getBrailleField()
	{
		return brailleField;
	}
	
	
	public JTextArea getRepeatField()
	{
		return repeatField;
	}
	
	public JTextArea getCorrectField()
	{
		return correctField;
	}
	
	
	public JTextArea getIntroField()
	{
		return introField;
	}
	
	public JTextField getButtonField()
	{
		return buttonField;
	}
	
	//get recorded audio field for introduction text
	public String getIntroAudio()
	{
		return introAudio;
	}
	
	//get selected audio field for introduction text
	public String getIntroSound()
	{
		return introSound;
	}
	
	
	//get recorded audio field for incorrect text
	public String getIncorrectAudio()
	{
		return incorrectAudio;
	}
	
	//get selected audio field for incorrect text
	public String getIncorrectSound()
	{
		return incorrectSound;
	}
	
	
	//get recorded audio field for correct text
	public String getCorrectAudio()
	{
		return correctAudio;
	}
	
	//get selected audio field for correct text
	public String getCorrectSound()
	{
		return correctSound;
	}
	
	
	

	//-------------------------------------------------------------------------------------------------------------------
	////////////////////////////////////////////////// SETTERS/////////////////////////////////////////////////////////
	//---------------------------------------------------------------------------------------------------------------------
		
	//set intoField
	public void setIntroField(String intro)
	{
		getIntroField().setText(intro);;
	}
	//set repeatField
	public void getRepeatField(String repeat)
	{
		getRepeatField().setText(repeat);;
	}
	//set brailleField
	public void getBrailleField(String braille)
	{
		getBrailleField().setText(braille);;
	}
	//set buttonField
	public void getButtonField(String button)
	{
		getButtonField().setText(button);;
	}

	
	
	////////////////////////////RECORD AUDIO////////////////////////////////////////////////////
	
	
	private void recordAudio()
	{
		JDialog recordDialog = new JDialog(gui, "Record Audio");
		recordDialog.setModal(true);
		JPanel panel = new JPanel();
		panel.setBorder(new EmptyBorder(10, 10, 10, 10));
		recordDialog.setSize(200, 180);
		recordDialog.setResizable(false);
		recordDialog.setLocationRelativeTo(gui);
	//	JLabel label = new JLabel("Press Record button to start recording, Stop button to stop and save, and Cancel button to canel recording");
		JButton recordButton = new JButton("Start Recording");
		recordButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				gui.logger.log(Level.INFO, "Recording Started");
				isRecording=true;
				noRecording = false;
				recordButton.setForeground(Color.RED);
				recordButton.setText("Recording...");
				thread = new ThreadRunnable();
				thread.start();			
			}
		});
		
		JButton stopButton = new JButton("Stop Recording");
		stopButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				if(isRecording==true){
				gui.logger.log(Level.INFO, "Recording Stopped");
				isRecording=false;
				recordButton.setForeground(Color.BLACK);
				recordButton.setText("Start Recording");
				file = thread.stopRecording();
				//recordDialog.setVisible(false);	
				}
			}	
		});
		
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				if(noRecording)
					recordDialog.setVisible(false);
				else
					{
						gui.logger.log(Level.INFO, "Recording Cancelled");
						isRecording=false;
						thread.cancel();
						recordFlag=false;
						recordDialog.setVisible(false);	
					}
				}
				
		});
		
		
		JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				if(noRecording)
					recordDialog.setVisible(false);
				else
					{
						gui.logger.log(Level.INFO, "Recording Done");
						isRecording=false;
						thread.cancel();
						recordFlag=true;
						recordDialog.setVisible(false);	
					}
				}
				
		});
				
		recordDialog.setLayout(new BorderLayout());
		panel.add(recordButton);
		panel.add(stopButton);
		panel.add(okButton);
		panel.add(cancelButton);
		recordDialog.add(panel, BorderLayout.CENTER);
		recordDialog.setVisible(true);		
	}

	
	
}
