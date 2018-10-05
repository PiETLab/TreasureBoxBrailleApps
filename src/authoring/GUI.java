package authoring;


import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;


import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.KeyEventDispatcher;
import javax.swing.SwingUtilities;

import listeners.NewButtonListener;


/**
 * This class represents the user interface for the authoring program. It is
 * responsible for creating all the user interface panels. In order to create a
 * new authoring session, simply create a (single) instance of this class.
 * 
 * @author Dilshad Khatri, Alvis Koshy, Drew Noel, Jonathan Tung
 * @version 1.0
 * @since 2017-03-15
 */
public class GUI extends JFrame {
	private static final long serialVersionUID = -1291725446662111704L;
	private transient ThreadRunnable audioThread;
	private LeftPanel leftPanel;
	private RightPanel rightPanel;
	private SettingsPanel settingsPanel;
	private NewButtonListener newItem = new NewButtonListener(this);
	
	private HashMap<KeyStroke, Action> actionMap = new HashMap<KeyStroke, Action>();
	private HashMap<KeyStroke, String> newItemMap = new HashMap<KeyStroke, String>();
	public  HashMap<String, Integer> counterMap = new HashMap<String, Integer>();
	public Logger logger = Logger.getLogger(this.getClass().getName());
	
	public File loadedFile = null;
	
	//This file keeps track of how many times each function was used, the counter persists through different instances of the JVM.
	public File functionCounter = new File(
			System.getProperty("user.dir") + File.separator + "logs" + File.separator + "functionCounter.txt"); 
	
	


	/**
	 * Create a new default authoring GUI. Accepts no arguments, and runs until
	 * the UI is closed.
	 */
	public GUI() {
		this.setTitle("Authoring App");
		this.getAccessibleContext().setAccessibleName("Authoring App");
		this.getAccessibleContext().setAccessibleDescription(
				"Welcome to the Treasure Box Braille Authoring App. To scroll through the options, use the tab key. Press the space bar to select an option.");

		this.setDefaultCloseOperation(HIDE_ON_CLOSE);
		
		//User Actions log created
		FileHandler fileHandler = null;
		try {
			Path path = Paths.get(System.getProperty("user.dir") + File.separator + "logs");
			if(!Files.exists(path)) Files.createDirectory(path);
			fileHandler = new FileHandler(System.getProperty("user.dir") + File.separator + "logs" + File.separator + "userActions.log.txt", 0, 1);
//		} catch (SecurityException e) {
//			e.printStackTrace();
//			System.err.println("An error has occurred while creating the log files, please contact an administrator." + System.getProperty("line.separator") + "Error type: SecurityException.");
		} catch (IOException e) {
			e.printStackTrace();
	         System.err.println("An error has occurred while creating the log files, please contact an administrator." + System.getProperty("line.separator") + "Error type: IOException.");
		}
    	
        fileHandler.setFormatter(new Formatter() {
    		private String format = "[%1$s] [%2$s] %3$s %n";
			private SimpleDateFormat dateWithMillis = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss.SSS");
			@Override
			public String format(LogRecord record) {
				return String.format(format, dateWithMillis.format(new Date()), record.getSourceClassName(), formatMessage(record));
			}
    	});
    	logger.addHandler(fileHandler);
    	logger.setUseParentHandlers(false);
		
		// Create the colour mapper
		ColourMapper mapper = new ColourMapper();

		// Create the root panel
		JPanel rootContainer = new JPanel();
		rootContainer.setLayout(new BoxLayout(rootContainer, BoxLayout.Y_AXIS));

		// Add the top settings panel
		settingsPanel = new SettingsPanel();
		settingsPanel.setMaximumSize(settingsPanel.getMinimumSize());
		//rootContainer.add(settingsPanel);

		JPanel bottomContainer = new JPanel();
		bottomContainer.setLayout(new BoxLayout(bottomContainer, BoxLayout.X_AXIS));

		rootContainer.add(bottomContainer);

		// Create the command list pane
		leftPanel = new LeftPanel(this, mapper);
		bottomContainer.add(leftPanel);

		// Create the buttons pane
		rightPanel = new RightPanel(this, mapper);
		bottomContainer.add(rightPanel);

		// Add the root container to the JFrame
		add(rootContainer);

		// Recalculate the button statuses
		leftPanel.recalculateButtonStatus();
		
		
		setup();
	}
	
	public void upd()
	{
	     //  File functionCounter = new File(functionCounter.toString());
	        BufferedWriter wr = null;
	        try {
	            functionCounter.createNewFile();
	            wr = new BufferedWriter(new FileWriter(functionCounter));
	            wr.write(counterMap.toString());
	            wr.close();
	        } catch (IOException e1) {
	            // TODO Auto-generated catch block
	            e1.printStackTrace();
	            System.err.println("File creation failed, please contact an administrator.");
	        }
	}
	
	private void setup() {
	  
		KeyStroke key1 = KeyStroke.getKeyStroke(KeyEvent.VK_1, 0);
		actionMap.put(key1, new AbstractAction("action1") {
			private static final long serialVersionUID = 1L;
			@Override
		    public void actionPerformed(ActionEvent e) {
				leftPanel.commandList.requestFocus();
			}
		});
	  
		KeyStroke key2 = KeyStroke.getKeyStroke(KeyEvent.VK_2, 0);
		actionMap.put(key2, new AbstractAction("action2") {
			private static final long serialVersionUID = 1L;
			@Override
		    public void actionPerformed(ActionEvent e) {
				if (rightPanel.btnNew.isEnabled()) {
					rightPanel.btnNew.requestFocus();
				}
			}
		});
		
		KeyStroke key5 = KeyStroke.getKeyStroke(KeyEvent.VK_3, 0);
		actionMap.put(key5, new AbstractAction("action5") {
			private static final long serialVersionUID = 1L;
			@Override
		    public void actionPerformed(ActionEvent e) {
				settingsPanel.cellField.requestFocus();
			}
		});
	 
		KeyStroke key3 = KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK + InputEvent.SHIFT_DOWN_MASK);
		actionMap.put(key3, new AbstractAction("New Scenario") {
			private static final long serialVersionUID = 1L;
			@Override
		    public void actionPerformed(ActionEvent e) {
				rightPanel.btnNewScenario.doClick();
			}
		});
		
		KeyStroke key4 = KeyStroke.getKeyStroke(KeyEvent.VK_I, InputEvent.CTRL_DOWN_MASK);
		actionMap.put(key4, new AbstractAction("New Item") {
			private static final long serialVersionUID = 1L;
			@Override
		    public void actionPerformed(ActionEvent e) {
				if (rightPanel.btnNew.isEnabled()) {
					rightPanel.btnNew.doClick();
				}
			}
		});
		
		KeyStroke key6 = KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_DOWN_MASK);
		actionMap.put(key6, new AbstractAction("New Question") {
			private static final long serialVersionUID = 1L;
			@Override
		    public void actionPerformed(ActionEvent e) {
				if (rightPanel.btnNewQuestion.isEnabled()) {
					rightPanel.btnNewQuestion.doClick();
				}
			}
		});
		
	//	KeyStroke key8 = KeyStroke.getKeyStroke(KeyEvent.VK_W, KeyEvent.CTRL_DOWN_MASK);
		KeyStroke key8 = KeyStroke.getKeyStroke("UP");
		actionMap.put(key8, new AbstractAction("Move Up") {
			private static final long serialVersionUID = 1L;
			@Override
		    public void actionPerformed(ActionEvent e) {
				if (rightPanel.btnMoveUp.isEnabled()) {
					rightPanel.btnMoveUp.doClick();
				}
			}
		});
		
	//	KeyStroke key9 = KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK);
		KeyStroke key9 = KeyStroke.getKeyStroke("DOWN");
		actionMap.put(key9, new AbstractAction("Move Down") {
			private static final long serialVersionUID = 1L;
			@Override
		    public void actionPerformed(ActionEvent e) {
				if (rightPanel.btnMoveDown.isEnabled()) {
					rightPanel.btnMoveDown.doClick();
				}
			}
		});
		
		KeyStroke key10 = KeyStroke.getKeyStroke(KeyEvent.VK_D, InputEvent.CTRL_DOWN_MASK);
		actionMap.put(key10, new AbstractAction("Delete") {
			private static final long serialVersionUID = 1L;
			@Override
		    public void actionPerformed(ActionEvent e) {
				if (rightPanel.btnDelete.isEnabled()) {
					rightPanel.btnDelete.doClick();
				}
			}
		});
		
		KeyStroke key11 = KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK);
		actionMap.put(key11, new AbstractAction("Save") {
			private static final long serialVersionUID = 1L;
			@Override
		    public void actionPerformed(ActionEvent e) {
				if (rightPanel.btnSave.isEnabled()) {
					rightPanel.btnSave.doClick();
				}
			}
		});
		
		KeyStroke key12 = KeyStroke.getKeyStroke(KeyEvent.VK_L, InputEvent.CTRL_DOWN_MASK);
		actionMap.put(key12, new AbstractAction("Load") {
			private static final long serialVersionUID = 1L;
			@Override
		    public void actionPerformed(ActionEvent e) {
				if (rightPanel.btnLoad.isEnabled()) {
					rightPanel.btnLoad.doClick();
				}
			}
		});
		
		KeyStroke key32 = KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_DOWN_MASK);
		actionMap.put(key32, new AbstractAction("Edit") {
			private static final long serialVersionUID = 1L;
			@Override
		    public void actionPerformed(ActionEvent e) {
				if (rightPanel.btnEdit.isEnabled()) {
					rightPanel.btnEdit.doClick();
				}
			}
		});
		
		KeyStroke key31 = KeyStroke.getKeyStroke(KeyEvent.VK_T, InputEvent.CTRL_DOWN_MASK);
		actionMap.put(key31, new AbstractAction("Test") {
			private static final long serialVersionUID = 1L;
			@Override
		    public void actionPerformed(ActionEvent e) {
				if (rightPanel.btnTestScenario.isEnabled()) {
					rightPanel.btnTestScenario.doClick();
				}
			}
		});
		
		KeyStroke key13 = KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.ALT_DOWN_MASK);
		newItemMap.put(key13,  "Pause");
		
		KeyStroke key14 = KeyStroke.getKeyStroke(KeyEvent.VK_T, InputEvent.ALT_DOWN_MASK);
		newItemMap.put(key14,  "Text-to-speech");
		
		KeyStroke key15 = KeyStroke.getKeyStroke(KeyEvent.VK_D, InputEvent.ALT_DOWN_MASK);
		newItemMap.put(key15,  "Display on Braille Cell");
		
		KeyStroke key16 = KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.ALT_DOWN_MASK);
		newItemMap.put(key16, "Repeat");
		
		KeyStroke key17 = KeyStroke.getKeyStroke(KeyEvent.VK_B, InputEvent.ALT_DOWN_MASK);
		newItemMap.put(key17, "Button Repeat");
		
		KeyStroke key18 = KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.ALT_DOWN_MASK);
		newItemMap.put(key18, "Button Location");
		
		KeyStroke key19 = KeyStroke.getKeyStroke(KeyEvent.VK_U, InputEvent.ALT_DOWN_MASK);
		newItemMap.put(key19,  "User Input");
		
		KeyStroke key20 = KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.ALT_DOWN_MASK);
		newItemMap.put(key20,  "Play Sound");
		
		KeyStroke key21 = KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.ALT_DOWN_MASK);
		newItemMap.put(key21,  "Reset Buttons");
		
		KeyStroke key22 = KeyStroke.getKeyStroke(KeyEvent.VK_G, InputEvent.ALT_DOWN_MASK);
		newItemMap.put(key22,  "Go To Location");
		
		KeyStroke key23 = KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.ALT_DOWN_MASK);
		newItemMap.put(key23,  "Clear All");
		
		KeyStroke key24 = KeyStroke.getKeyStroke(KeyEvent.VK_H, InputEvent.ALT_DOWN_MASK);
		newItemMap.put(key24,  "Clear Cell");
		
		KeyStroke key25 = KeyStroke.getKeyStroke(KeyEvent.VK_I, InputEvent.ALT_DOWN_MASK);
		newItemMap.put(key25,  "Set Pins");
		
		KeyStroke key26 = KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.ALT_DOWN_MASK);
		newItemMap.put(key26,  "Set Character");
		
		KeyStroke key27 = KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.ALT_DOWN_MASK);
		newItemMap.put(key27,  "Raise Pin");
		
		KeyStroke key28 = KeyStroke.getKeyStroke(KeyEvent.VK_L, InputEvent.ALT_DOWN_MASK);
		newItemMap.put(key28,  "Lower Pin");
		
		KeyStroke key29 = KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.ALT_DOWN_MASK);
		newItemMap.put(key29,  "Set Voice");
		
		KeyStroke key30 = KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.ALT_DOWN_MASK);
		newItemMap.put(key30,  "Location Tag");
		
		
		
				
		counterMap.put("New Scenario", 0);
		counterMap.put("New Item", 0);
		counterMap.put("New Question", 0);
		counterMap.put("Move Up", 0);
		counterMap.put("Move Down", 0);
		counterMap.put("Delete", 0);
		counterMap.put("Edit", 0);
		counterMap.put("Save", 0);
		counterMap.put("Load", 0);
		counterMap.put("Pause", 0);
		counterMap.put("Text-to-speech", 0);
		counterMap.put("Display on Braille Cell", 0);
		counterMap.put("Repeat", 0);
		counterMap.put("Button Repeat", 0);
		counterMap.put("Button Location", 0);
		counterMap.put("User Input", 0);
		counterMap.put("Play Sound", 0);
		counterMap.put("Record Audio",  0);
		counterMap.put("Reset Buttons", 0);
		counterMap.put("Go To Location", 0);
		counterMap.put("Clear All", 0);
		counterMap.put("Clear Cell", 0);
		counterMap.put("Set Pins", 0);
		counterMap.put("Set Character", 0);
		counterMap.put("Raise Pin", 0);
		counterMap.put("Lower Pin", 0);
		counterMap.put("Set Voice", 0);
		counterMap.put("Location Tag", 0);
		counterMap.put("Test",  0);
		
		
		
		if (functionCounter.exists())
		{
			Scanner sc = null;
			try {
				sc = new Scanner(functionCounter);
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
			
			if (sc.hasNext())
			{
			    loadCounter(functionCounter, sc);
			}
		}
		
		
		
		
		
		// add more actions..

	  KeyboardFocusManager kfm = KeyboardFocusManager.getCurrentKeyboardFocusManager();
	  kfm.addKeyEventDispatcher( new KeyEventDispatcher() {

	    @Override
	    public boolean dispatchKeyEvent(KeyEvent e) {
	      KeyStroke keyStroke = KeyStroke.getKeyStrokeForEvent(e);
	      if ( actionMap.containsKey(keyStroke) ) {
	        final Action a = actionMap.get(keyStroke);
	        final ActionEvent ae = new ActionEvent(e.getSource(), e.getID(), null );
	        SwingUtilities.invokeLater( new Runnable() {
	          @Override
	          public void run() {
	            a.actionPerformed(ae);
	          }
	        } ); 
	        return true;
	      } else if (newItemMap.containsKey(keyStroke) && rightPanel.btnNew.isEnabled())
	      {
	    	  String value = newItemMap.get(keyStroke);
	    	  newItem.processAnswer(value);
	    	  logger.log(Level.INFO, "User has used the " + value + " hotkey.");
	    	  return true;
	      }
	      return false;
	    }
	  });
	}
	
	
	
	
	

	/**
	 * Set the value of the audio thread in order for the main thread to access
	 * it and run various methods on it.
	 * 
	 * @param audioThread
	 *            The instance of the audio thread that the GUI should attempt
	 *            to control
	 */
	public void setAudioThread(ThreadRunnable audioThread) {
		this.audioThread = audioThread;
	}

	/**
	 * Retrieve the last known instance of the audio thread. In the even that
	 * there was never a known instance, this will return null.
	 * 
	 * @return Either the last known instance (if it exists) or null
	 *         (otherwise).
	 */
	public ThreadRunnable getAudioThread() {
		return this.audioThread;
	}

	/**
	 * Obtain the shared reference to the Left Panel. The Left Panel contains
	 * the backend list which is both shown to the authoring user (as a text
	 * list) and used for generating the final output file (via serialization)
	 * 
	 * @return The single instance of the left panel
	 */
	public LeftPanel getLeftPanel() {
		return this.leftPanel;
	}

	/**
	 * Obtain the shared reference to the Right Panel. The Right Panel contains
	 * the buttons for the authoring user to control the application
	 * 
	 * @return The single instance of the right panel
	 */
	public RightPanel getRightPanel() {
		return this.rightPanel;
	}

	/**
	 * Obtain the shared reference to the Settings Panel. The Settings Panel
	 * contains the values which are used to generate the output file header
	 * information
	 * 
	 * @return The single instance of the settings panel
	 */
	public SettingsPanel getSettingsPanel() {
		return this.settingsPanel;
	}
	
	
	/**
	 * Resets the counter for each function. 
	 */
	public void resetCounter()
	{
		counterMap.clear();
		counterMap.put("New Scenario", 0);
		counterMap.put("New Item", 0);
		counterMap.put("New Question", 0);
		counterMap.put("Move Up", 0);
		counterMap.put("Move Down", 0);
		counterMap.put("Delete", 0);
		counterMap.put("Edit", 0);
		counterMap.put("Save", 0);
		counterMap.put("Load", 0);
		counterMap.put("Pause", 0);
		counterMap.put("Text-to-speech", 0);
		counterMap.put("Display on Braille Cell", 0);
		counterMap.put("Repeat", 0);
		counterMap.put("Button Repeat", 0);
		counterMap.put("Button Location", 0);
		counterMap.put("User Input", 0);
		counterMap.put("Play Sound", 0);
		counterMap.put("Record Audio",  0);
		counterMap.put("Reset Buttons", 0);
		counterMap.put("Go To Location", 0);
		counterMap.put("Clear All", 0);
		counterMap.put("Clear Cell", 0);
		counterMap.put("Set Pins", 0);
		counterMap.put("Set Character", 0);
		counterMap.put("Raise Pin", 0);
		counterMap.put("Lower Pin", 0);
		counterMap.put("Set Voice", 0);
		counterMap.put("Location Tag", 0);
		counterMap.put("Test", 0);
		
	}
	
	
	
	private void loadCounter(File file, Scanner sc)
	{
		
		
		for (String i : this.counterMap.keySet())
		{
			sc.findInLine(i);
			String token = sc.next();
			int count = Integer.parseInt(token.charAt(1) + "");
			this.counterMap.put(i, count);
			sc.reset();
			
		}
				
		
	}
	
}