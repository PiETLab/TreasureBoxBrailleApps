package commands;

import java.awt.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Scanner;
import java.util.logging.Level;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import commands.PlayerCommand;
import org.apache.commons.io.FilenameUtils;
import authoring.GUI;
import authoring.QuestionWindow;



	public class QuestionCommand implements PlayerCommand{
		
		ArrayList<PlayerCommand> questionCommands = new ArrayList<>(); // list to store all commands
		private String textToSay = "";
		private String question="";
		private String display="";
		private String wrongText="";
		private String rightText="";
		private String introAudio= "";
		private String introSound= "";
		private String correctAudio= "";
		private String correctSound= "";
		private String incorrectAudio= "";
		private String incorrectSound= "";
		private String noButton="";
		private int correctButton;
		public String a ="";
		private GUI gui;
	
	//	private GUI gui = new GUI();
	
	/*public QuestionCommand(String question,String introAudio, String introSound, String display, String wrongText, String incorrectAudio, String incorrectSound,  String rightText, String correctAudio, String correctSound, int correctButton, String button, GUI gui) {
			this.question = question;
			this.display = display;
			this.introAudio= introAudio;
			this.introSound= introSound;
			this.incorrectAudio = incorrectAudio;
			this.incorrectSound= incorrectSound;
			this.correctAudio= correctAudio;
			this.correctSound = correctSound;
			this.wrongText = wrongText;
			this.rightText= rightText;
			this.correctButton= correctButton+1;
			this.noButton= button;
			this.gui= gui;
		}*/
		public QuestionCommand(QuestionWindow ques, GUI gui)
		{
			  
        	if(ques.getIntroField().getText().length()>0)
	        	{	
        		this.question =ques.getIntroField().getText();
	        	}
        	if(ques.getRepeatField().getText().length()>0)
	        	{	
        		this.wrongText = ques.getRepeatField().getText();
	        	}
        	if(ques.getCorrectField().getText().length()>0)
	        	{	
        		this.rightText= ques.getCorrectField().getText();
	        	}
        	this.display = ques.getBrailleField().getText();
        	
        	this.introAudio= ques.getIntroAudio();
        	this.introSound= ques.getIntroSound();
        	
        	this.incorrectAudio = ques.getIncorrectAudio();
        	this.correctAudio = ques.getCorrectAudio();
        	
        	this.incorrectSound = ques.getIncorrectSound();
        	this.correctSound= ques.getCorrectSound();
        	
        	this.correctButton = ques.getButton().getSelectedIndex()+1;
        	this.gui=gui;
        	this.noButton = gui.getSettingsPanel().getButtonField();
        	
        	
		}
	
		public void addCommand(PlayerCommand q) // add commands in list
		{
			questionCommands.add(q);
		}
		@Override
		public String toString() {  // this will be printed on Left Panel at one index

			String result;
			result = "<html>"+  "<html><font color=\"green\">"+ "Question to Ask" +"</font>";
			if(question!="none" && question.length()>0)
				
				result=result + "<br>" + "Text: " +"<html><font color=\"red\">"+ question + "</font>";
			else if (introAudio != "none")
				result= result + "<br>" + "Recorded Audio: "+ "<html><font color=\"red\">"+  introAudio+ "</font>";
			else if(introSound != "none")
				result= result+ "<br>" + "Selected Audio: "+ "<html><font color=\"red\">"+  introSound+ "</font>";
			
			result = result + "<br>"+ "<html><font color=\"blue\">" + "Display on Braille cells: " + "</font>" + "<html><font color=\"red\">"+ display + "</font>" +
				"<br>" + "<html><font color=\"blue\">"+ "Correct button: " + "</font>"+"<html><font color=\"red\">"+  correctButton + "</font>" +
				"<br>"+"<html><font color=\"green\">"+  "Wrong Answer" + "</font>";
			
			
			if(wrongText!="none" && wrongText.length()>0)
				result=result + "<br>"+ "Text: " + "<html><font color=\"red\">"+ wrongText + "</font>";
			else if (incorrectAudio != "none")
				result= result + "<br>" + "Recorded Audio: "+ "<html><font color=\"red\">"+  incorrectAudio+ "</font>";
			else if(incorrectSound != "none")
				result= result+ "<br>" + "Selected Audio: "+ "<html><font color=\"red\">"+  incorrectSound+ "</font>";
			
			
			result = result + "<br>"+"<html><font color=\"green\">"+ "Right Answer" + "</font>" ;
			if(rightText!="none" && rightText.length()>0)
				result=result + 	"<br>" + "Text: "+ "<html><font color=\"red\">"+  rightText +"</font>";
			else if (correctAudio != "none")
				result= result + "<br>" + "Recorded Audio: "+ "<html><font color=\"red\">"+  correctAudio+ "</font>";
			else if(correctSound != "none")
				result= result+ "<br>" + "Selected Audio: "+ "<html><font color=\"red\">"+  correctSound+ "</font>";
			/*
			return 	"<html>"+ "<html><font color=\"blue\">" + "Display on Braille cells: " + "</font>" + "<html><font color=\"red\">"+ display + "</font>"+
					"<br>" + "<html><font color=\"blue\">"+ "Correct button: " + "</font>"+"<html><font color=\"red\">"+  correctButton + "</font>"+
					"<br>"+ "<html><font color=\"green\">"+ "____Question to Ask____" +"</font>"+ 
					"<br>" + "Text: " +"<html><font color=\"red\">"+ question + "</font>"+
					"<br>" + "Recorded Audio: "+ "<html><font color=\"red\">"+  introAudio+ "</font>"+
					"<br>" + "Selected Audio: "+ "<html><font color=\"red\">"+  introSound+ "</font>"+
				//	"<br>" + "Pause for seconds: "+ "<html><font color=\"red\">"+  "1"+ "</font>"+
					//"<br>" + "Wait for user input" + 
					"<br>"+"<html><font color=\"green\">"+  "___On Wrong Answer____" + "</font>"+
					"<br>"+ "Text: " + "<html><font color=\"red\">"+ wrongText + "</font>"+
					"<br>" + "Recorded Audio: "+ "<html><font color=\"red\">"+  incorrectAudio+ "</font>"+
					"<br>" + "Selected Audio: "+ "<html><font color=\"red\">"+  incorrectSound+ "</font>"+
					"<br>"+"<html><font color=\"green\">"+ "___On Right Answer____" + "</font>" +
					"<br>" + "Text: "+ "<html><font color=\"red\">"+  rightText +"</font>"+
					"<br>" + "Recorded Audio: "+ "<html><font color=\"red\">"+  correctAudio+ "</font>"+
					"<br>" + "Selected Audio: "+ "<html><font color=\"red\">"+  correctSound+ "</font>";*/
			return result;
		}
	
		@Override
		public String serialize() { // this will be stored in file
			a="";
			for (PlayerCommand pc : questionCommands) {
				
			//	System.out.println(pc.getClass()+"\n");
			a =  a + pc.serialize() + "\n";
			}
			a = a.substring(0, a.length() - 1);
			System.out.println(a);
			return a;
			
		}
	
		@Override
		public String getEditLabel() {
			return "question";
		}
	
		@Override
		public String getCurrentValue() {
			return question;
		}
	
		@Override
		public void setCurrentValue(String textToSay) {
			this.question = textToSay;
		}
		
	
		public void display()
		{
			for (PlayerCommand pc : questionCommands) {
				this.a= pc.serialize();
				this.toString();
			}
		}
		
		@Override
		public void editCommand() {
		QuestionWindow ques= new QuestionWindow(this);
			ques.frame.addWindowListener(new WindowAdapter()
			{

			@Override
		        public void windowClosed(WindowEvent e)
	        	{
	        	
	    		setAll(ques.getIntroField().getText(), ques.getIntroAudio(), ques.getIntroSound(), ques.getBrailleField().getText(),ques.getCorrectField().getText(), ques.getCorrectAudio(), ques.getCorrectSound(), ques.getRepeatField().getText(), ques.getIncorrectAudio(), ques.getIncorrectSound(),ques.getButton().getSelectedIndex());
		        }
		    });
			
			
		}
			
		public String getIntroField()
		{
			return this.question;
		}
		
		public String getBrailleField()
		{
			return this.display;
		}
		
		public String getCorrectField()
		{
			return this.rightText;
		}
		
		public String getRepeatField()
		{
			return this.wrongText;
		}
		
		public int getCorrectButton()
		{
			return this.correctButton;
		}
		
		public String getTotalButtons()
		{
			return this.noButton;
		}
		public String getIntroAudio()
		{
			return this.introAudio;
		}
		public String getIntroSound()
		{
			return this.introAudio;
		}
		
		
		
		
		public String getIncorrectAudio()
		{
			return this.incorrectAudio;
		}
		public String getIncorrectSound()
		{
			return this.incorrectSound;
		}
		
		public String getCorrectAudio()
		{
			return this.correctAudio;
		}
		public String getCorrectSound()
		{
			return this.correctSound;
		}
		
		public GUI getGUI()
		{
			return this.gui;
		}
		public void setAll(String question,String introAudio, String introSound, String display, String rightText,String correctAudio, String correctSound,  String wrongText, String incorrectAudio, String incorrectSound, int correctButton)
		{
			
			if(!(question.equals("none")) && question.length()>0)
				questionCommands.set(1, new TTSCommand(question));			
			else if(!(introAudio.equals("none")) && introAudio.length()>0)
				questionCommands.set(1, new SoundCommand(introAudio));
			else if(!(introSound.equals("none")) && introSound.length()>0)
				questionCommands.set(1, new SoundCommand(introSound));	
			
			
			if(display != this.display)
				questionCommands.set(3, new SetStringCommand(display));
			
			
			if(!(wrongText.equals("none")) && wrongText.length()>0)
				questionCommands.set(8, new TTSCommand(wrongText));			
			else if(!(incorrectAudio.equals("none")) && incorrectAudio.length()>0)
				questionCommands.set(8, new SoundCommand(incorrectAudio));
			else if(!(incorrectSound.equals("none")) && incorrectSound.length()>0)
				questionCommands.set(8, new SoundCommand(incorrectSound));
			
			

			if(!(rightText.equals("none")) && rightText.length()>0)
				questionCommands.set(11, new TTSCommand(rightText));			
			else if(!(correctAudio.equals("none")) && correctAudio.length()>0)
				questionCommands.set(11, new SoundCommand(correctAudio));
			else if(!(correctSound.equals("none")) && correctSound.length()>0)
				questionCommands.set(11, new SoundCommand(correctSound));
			
			
			for (PlayerCommand pc : questionCommands) {
        		System.out.println(pc);
			}
			this.introAudio= introAudio;
			this.introSound= introSound;
			this.incorrectAudio= incorrectAudio;
			this.incorrectSound= incorrectSound;
			this.correctAudio= correctAudio;
			this.correctSound= correctSound;
			this.question= question;
			this.display= display;
			this.rightText= rightText;
			this.wrongText=wrongText;
			this.correctButton= correctButton + 1;
		}
}
