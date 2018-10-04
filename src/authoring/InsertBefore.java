package authoring;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import commands.SoundCommand;
import commands.TTSCommand;
import listeners.NewButtonListener;
import listeners.NewQuestionListener;

public class InsertBefore {

	private static final String FONT_FACE = "Arial";
	private static final int FONT_SIZE = 12;
	private JFrame frame;
	private RecordAudio rc= new RecordAudio();
	private GUI gui;
	ColourMapper mapper;
	Object value;
	
	
	public InsertBefore(GUI gui)
	{
	
	this.gui=gui;
	// Show the Add Item dialog
	JPanel panel = new JPanel();
	panel.setLayout(new GridLayout(7, 1,5,3 ));
	panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Items"));
	frame = new JFrame("Add New Item");
	frame.setPreferredSize(new Dimension(250,250));
	frame.setMaximumSize(new Dimension(250,250));
	frame.setResizable(false);
	frame.add(panel,BorderLayout.CENTER);
	frame.pack();
	frame.setVisible(true);
	
	
	NewButtonListener lis= new NewButtonListener(gui);

	JButton btnQuestion = new JButton("New Question");
	btnQuestion.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent arg0) {
			NewQuestionListener newQ= new NewQuestionListener(gui, mapper, 1);
			frame.dispose();
			
		}
	});
	btnQuestion.setFont(new Font(FONT_FACE, Font.PLAIN, FONT_SIZE));
	panel.add(btnQuestion);
	
	
	JButton btnNewButton = new JButton("Text To Speech");
	btnNewButton.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent arg0) {
		/* = JOptionPane.showInputDialog(gui, "Text to say", "Edit Item Details",
					JOptionPane.PLAIN_MESSAGE, null, null, "");
			if (value != null && value != "") {
				gui.getLeftPanel().addItemAt(new TTSCommand((String) value));
				gui.counterMap.put("Text-to-speech", gui.counterMap.get("Text-to-speech") + 1);
			}*/
			lis.processAnswer("Text-to-speech", "insert");
			
			frame.dispose();
			
		}
	});
	btnNewButton.setFont(new Font(FONT_FACE, Font.PLAIN, FONT_SIZE));
	panel.add(btnNewButton);

	
	
	JButton btnRecordAudio = new JButton("Record Audio");
	btnRecordAudio.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			
			lis.processAnswer("Record Audio", "insert");
		/*	rc.recordAudio(gui); //  calls recordAudio method in RecordAudio class
			if (rc.file != null && rc.recordFlag==true)
			{
				gui.getLeftPanel().addItemAt(new SoundCommand(rc.file.toString(), gui));
				gui.counterMap.put("Record Audio", gui.counterMap.get("Record Audio") + 1);
			}	*/
			
			frame.dispose();
		
		}
	});
	btnRecordAudio.setFont(new Font(FONT_FACE, Font.PLAIN, FONT_SIZE));
	panel.add(btnRecordAudio);

	
	
	
	JButton btnPlaySound = new JButton("Play Sound");
	btnPlaySound.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			
			
			lis.processAnswer("Play Sound", "insert");
			frame.dispose();
		
		}
	});
	btnPlaySound.setFont(new Font(FONT_FACE, Font.PLAIN, FONT_SIZE));
	panel.add(btnPlaySound);

	
	
	JButton btnPause = new JButton("Pause");
	btnPause.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			lis.processAnswer("Pause", "insert");
			frame.dispose();
		
		}
	});
	btnPause.setFont(new Font(FONT_FACE, Font.PLAIN, FONT_SIZE));
	panel.add(btnPause);

	
	JButton btnDisplayOnBraille = new JButton("Display on Braille Cell");
	btnDisplayOnBraille.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			lis.processAnswer("Display on Braille Cell", "insert");
			frame.dispose();
	
		}
	});
	btnDisplayOnBraille.setFont(new Font(FONT_FACE, Font.PLAIN, FONT_SIZE));
	panel.add(btnDisplayOnBraille);

	
	String[] array= {"Advance Options","Repeat", "Button Repeat", "Button Location", "User Input", "Reset Buttons", "Go To Location", "Clear All", "Clear Cell",
			"Set Pins", "Set Character", "Raise Pin", "Lower Pin", "Set Voice", "Location Tag"};
	

	JComboBox comboBox = new JComboBox<Object>(array);
	comboBox.setSelectedItem("Advance Options");
	comboBox.setFont(new Font(FONT_FACE, Font.PLAIN, FONT_SIZE));
	panel.add(comboBox);
	
	
	  comboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                //
                // Get the source of the component, which is our combobox.
                Object selected = comboBox.getSelectedItem();
                String answer = selected.toString();
                System.out.println(answer);
                lis.processAnswer(answer, "insert");
                frame.dispose();

            }
        });


	}

	
	
	
}
