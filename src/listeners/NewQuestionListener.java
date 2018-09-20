package listeners;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;

import javax.accessibility.Accessible;
import javax.swing.JPanel;

import authoring.ColourMapper;
import authoring.GUI;
import authoring.QuestionWindow;        
import commands.GoHereCommand;
import commands.PauseCommand;
import commands.PlayerCommand;
import commands.ResetButtonCommand;
import commands.SetStringCommand;
import commands.SkipButtonCommand;
import commands.SkipCommand;
import commands.SoundCommand;
import commands.TTSCommand;
import commands.UserInputCommand;
import commands.QuestionCommand;
/**
 * This class is used as an action listener whenever the "New Question" button
 * is clicked. It serves as a way to create question by asking user about
 * introduction text, braille text, repeating text, correct button, text for
 * incorrect.
 *
 * @author Dilshad Khatri, Alvis Koshy, Drew Noel, Jonathan Tung
 * @version 1.0
 * @since 4/3/2017
 *
 */
public class NewQuestionListener extends JPanel implements ActionListener, Accessible{
	private static final long serialVersionUID = 7443038348707836054L;
	private GUI gui;
	public static Thread threadObject;
	private QuestionWindow ques;

	/**
 * Create the NewQuestionListener with a reference to the current GUI
 * object. Access to this object is required in order to access the left
 * panel
 *
 * @param gui
 *            Instance of currently running GUI
 * @param mapper
 *            Reference to the common instance of the colourmapper
 */
public NewQuestionListener(GUI gui, ColourMapper mapper) {
	this.gui = gui;
	this.getAccessibleContext().setAccessibleDescription("");}


@Override
public void actionPerformed(ActionEvent arg0) {
	gui.logger.log(Level.INFO, "User has clicked New Question button.");
	gui.counterMap.put("New Question", gui.counterMap.get("New Question") + 1);
	
	// create new Question Window to open frame for new question
	ques= new QuestionWindow(this.gui);
	
	//This section of code will execute when Jframe window will be closed in QuestionWindow
	ques.frame.addWindowListener(new WindowAdapter()
    {
        @Override
        public void windowClosed(WindowEvent e)
        {
          
        	Random r = new Random();
        	String randomLabel = "" + r.nextInt(500);
        	
        	String strNumOfButtons = gui.getSettingsPanel().getButtonField();
        	if (strNumOfButtons == null || strNumOfButtons.isEmpty()) {
        		return;
        	}

        	int numOfButtons = Integer.parseInt(strNumOfButtons);

        	ArrayList<PlayerCommand> questionCommands = new ArrayList<>();

      
        	
        	
        	// qc is object of QuestionCommand class
        	QuestionCommand qc = new QuestionCommand(ques, gui);
        	//QuestionCommand qc= new QuestionCommand(introText, ques.getIntroAudio(), ques.getIntroSound(), ques.getBrailleField().getText(),incorrectText, ques.getIncorrectAudio(), ques.getIncorrectSound(), correctText, ques.getCorrectAudio(), ques.getCorrectSound(), ques.getButton().getSelectedIndex(), gui.getSettingsPanel().getButtonField(), gui);
        	
        	
        	qc.addCommand(new ResetButtonCommand(""));
        	if(ques.getIntroField().getText().length()>0 && ques.getIntroField().getText()!="none")
        	{qc.addCommand(new TTSCommand(ques.getIntroField().getText()));
        	
        	}
        	else if(ques.getIntroAudio()!="none")
        	{
        		qc.addCommand(new SoundCommand(ques.getIntroAudio()));
				
        	}
        	else if(ques.getIntroSound()!="none")
        	{
        		qc.addCommand(new SoundCommand(ques.getIntroSound()));
        		
        	}
        	qc.addCommand(new PauseCommand("1"));
        	qc.addCommand(new SetStringCommand(ques.getBrailleField().getText()));
        //	qc.addCommand(new GoHereCommand(randomLabel + "-start"));
    

        	// Loop through all the buttons defined

        	PlayerCommand holder;
        for (int i = 0; i < numOfButtons; i++) {
        		if (i != ques.getButton().getSelectedIndex()) {
        			// All buttons that are wrong will just repeat the question
        			// (bad)
        			holder = new SkipButtonCommand("" + i + " " + randomLabel + "-bad");
        			qc.addCommand(holder);
        		} else {
        			// The correct button skips to the end
        			holder = new SkipButtonCommand("" + i + " " + randomLabel + "-good");
        			qc.addCommand(holder);
        		}
        	}

        	// Adds UserInputCommand to wait for button presses
        		qc.addCommand(new UserInputCommand());
        	// Labels for bad
        		qc.addCommand(new GoHereCommand("" + randomLabel + "-bad"));
        		if(ques.getRepeatField().getText().length()>0 && ques.getIntroField().getText()!="none")
        			qc.addCommand(new TTSCommand(ques.getRepeatField().getText()));
        		else if(ques.getIncorrectAudio()!="none")
        			qc.addCommand(new SoundCommand(ques.getIncorrectAudio()));
        		else if(ques.getIncorrectSound()!="none")
        			qc.addCommand(new SoundCommand(ques.getIncorrectSound()));
        		holder = new SkipCommand("" + randomLabel + "-next");
    			qc.addCommand(holder);
        	//	qc.addCommand(new SkipCommand(randomLabel + "-start"));
        	// Label for good
        		holder = new GoHereCommand("" + randomLabel + "-good");
        		qc.addCommand(holder);
        		
        		
        		if(ques.getCorrectField().getText().length()>0)
        			qc.addCommand(new TTSCommand(ques.getCorrectField().getText()));
        		else if(ques.getCorrectAudio()!="none")
        			qc.addCommand(new SoundCommand(ques.getCorrectAudio()));
        		else if(ques.getCorrectSound()!="none")
        			qc.addCommand(new SoundCommand(ques.getCorrectSound()));
        		
        		holder = new SkipCommand("" + randomLabel + "-next");
        		qc.addCommand(holder);
        	//	qc.addCommand(new PauseCommand("1"));
      
        	
        		qc.addCommand(new GoHereCommand("" + randomLabel + "-next"));
        		qc.addCommand(new PauseCommand("1"));
        		
        		questionCommands.add(qc);
            	
        		for (PlayerCommand pc : questionCommands) {
        		gui.getLeftPanel().addItem(pc);
        	}
            
        }
        
    });

		
	}
}




