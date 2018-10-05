package listeners;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.logging.Level;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

import authoring.GUI;
import authoring.RecordAudio;
import authoring.ThreadRunnable;
import commands.CellCharCommand;
import commands.CellLowerCommand;
import commands.CellRaiseCommand;
import commands.ClearAllCommand;
import commands.ClearCellCommand;
import commands.GoHereCommand;
import commands.PauseCommand;
import commands.RepeatButtonCommand;
import commands.RepeatCommand;
import commands.ResetButtonCommand;
import commands.SetPinsCommand;
import commands.SetStringCommand;
import commands.SetVoiceCommand;
import commands.SkipButtonCommand;
import commands.SkipCommand;
import commands.SoundCommand;
import commands.TTSCommand;
import commands.UserInputCommand;

/**
 * This class is used as an action listener whenever the "New Item" button is
 * clicked. It enables the user to set items from dialog box with their value.
 *
 * @author Dilshad Khatri, Alvis Koshy, Drew Noel, Jonathan Tung
 * @version 1.0
 * @since 4/3/2017
 *
 */
public class NewButtonListener implements ActionListener {

	private GUI gui;
	ThreadRunnable thread = null;
	File file = null;
	private RecordAudio rc = new RecordAudio();
	private static final String FONT_FACE = "Arial";
	private static final int FONT_SIZE = 12;

	private JFrame frame;

	//
	/**
	 * Create the NewButtonListener with a reference to the base GUI object
	 * (required to access the left panel)
	 *
	 * @param gui
	 *            Instance of currently running GUI
	 */
	public NewButtonListener(GUI gui) {
		this.gui = gui;
		file = null;

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		gui.logger.log(Level.INFO, "User has clicked New Item button.");
		// Show the Add Item dialog

		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(6, 1, 5, 3));
		panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Items"));
		frame = new JFrame("Add New Item");
		frame.setPreferredSize(new Dimension(200, 200));
		frame.setMaximumSize(new Dimension(200, 200));
		frame.setResizable(false);
		frame.add(panel, BorderLayout.CENTER);
		frame.pack();
		frame.setVisible(true);

		JButton btnNewButton = new JButton("Text To Speech");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frame.dispose();
				processAnswer("Text-to-speech", "add");
			}
		});
		btnNewButton.setFont(new Font(FONT_FACE, Font.PLAIN, FONT_SIZE));
		panel.add(btnNewButton);

		JButton btnRecordAudio = new JButton("Record Audio");
		btnRecordAudio.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
				processAnswer("Record Audio", "add");
			}
		});
		btnRecordAudio.setFont(new Font(FONT_FACE, Font.PLAIN, FONT_SIZE));
		panel.add(btnRecordAudio);

		JButton btnPlaySound = new JButton("Play Sound");
		btnPlaySound.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
				processAnswer("Play Sound", "add");
			}
		});
		btnPlaySound.setFont(new Font(FONT_FACE, Font.PLAIN, FONT_SIZE));
		panel.add(btnPlaySound);

		JButton btnPause = new JButton("Pause");
		btnPause.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
				processAnswer("Pause", "add");
			}
		});
		btnPause.setFont(new Font(FONT_FACE, Font.PLAIN, FONT_SIZE));
		panel.add(btnPause);

		JButton btnDisplayOnBraille = new JButton("Display on Braille Cell");
		btnDisplayOnBraille.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
				processAnswer("Display on Braille Cell", "add");
			}
		});
		btnDisplayOnBraille.setFont(new Font(FONT_FACE, Font.PLAIN, FONT_SIZE));
		panel.add(btnDisplayOnBraille);

		String[] array = { "Advance Options", "Repeat", "Button Repeat", "Button Location", "User Input",
				"Reset Buttons", "Go To Location", "Clear All", "Clear Cell", "Set Pins", "Set Character", "Raise Pin",
				"Lower Pin", "Set Voice", "Location Tag" };

		JComboBox<Object> comboBox = new JComboBox<Object>(array);
		comboBox.setSelectedItem("Advance Options");
		comboBox.setFont(new Font(FONT_FACE, Font.PLAIN, FONT_SIZE));
		panel.add(comboBox);

		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				//
				// Get the source of the component, which is our combobox.
				Object selected = comboBox.getSelectedItem();
				String answer = selected.toString();
				frame.dispose();
				processAnswer(answer, "add");

			}
		});

	}

	public void processAnswer(String answer, String ID) {
		Object value;
		if (answer != null) {
			switch (answer) {
			case "Pause":
				value = JOptionPane.showInputDialog(gui, "Length of time to wait", "Edit Item Details",
						JOptionPane.PLAIN_MESSAGE, null, null, "");
				if (value != null && value != "") {
					if (ID == "add")
						gui.getLeftPanel().addItem(new PauseCommand((String) value));
					else
						gui.getLeftPanel().addItemAt(new PauseCommand((String) value));
					this.gui.counterMap.put("Pause", gui.counterMap.get("Pause") + 1);
				}
				break;
			case "Text-to-speech":
				value = JOptionPane.showInputDialog(gui, "Text to say", "Edit Item Details", JOptionPane.PLAIN_MESSAGE,
						null, null, "");
				if (value != null && value != "") {
					if (ID == "add")
						gui.getLeftPanel().addItem(new TTSCommand((String) value));
					else
						gui.getLeftPanel().addItemAt(new TTSCommand((String) value));
					gui.counterMap.put("Text-to-speech", gui.counterMap.get("Text-to-speech") + 1);
				}
				break;

			case "Display on Braille Cell":
				value = JOptionPane.showInputDialog(gui, "String to display", "Edit Item Details",
						JOptionPane.PLAIN_MESSAGE, null, null, "");
				if (value != null && value != "") {
					if (ID == "add")
						gui.getLeftPanel().addItem(new SetStringCommand((String) value));
					else
						gui.getLeftPanel().addItemAt(new SetStringCommand((String) value));
					gui.counterMap.put("Display on Braille Cell", gui.counterMap.get("Display on Braille Cell") + 1);
				}
				break;
			case "Repeat":
				value = JOptionPane.showInputDialog(gui, "Text to be repeated", "Edit Item Details",
						JOptionPane.PLAIN_MESSAGE, null, null, "");
				if (value != null && value != "") {
					if (ID == "add")
						gui.getLeftPanel().addItem(new RepeatCommand((String) value));
					else
						gui.getLeftPanel().addItemAt(new RepeatCommand((String) value));
					gui.counterMap.put("Repeat", gui.counterMap.get("Repeat") + 1);
				}
				break;
			case "Button Repeat":
				value = JOptionPane.showInputDialog(gui, "Button to use for repeating", "Edit Item Details",
						JOptionPane.PLAIN_MESSAGE, null, null, "");
				if (value != null && value != "") {
					if (ID == "add")
						gui.getLeftPanel().addItem(new RepeatButtonCommand((String) value));
					else
						gui.getLeftPanel().addItemAt(new RepeatButtonCommand((String) value));
					gui.counterMap.put("Button Repeat", gui.counterMap.get("Button Repeat") + 1);
				}
				break;
			case "Button Location":
				value = JOptionPane.showInputDialog(gui, "Button and identifier (space separated)", "Edit Item Details",
						JOptionPane.PLAIN_MESSAGE, null, null, "");
				if (value != null && value != "") {
					if (ID == "add")
						gui.getLeftPanel().addItem(new SkipButtonCommand((String) value));
					else
						gui.getLeftPanel().addItemAt(new SkipButtonCommand((String) value));
					gui.counterMap.put("Button Location", gui.counterMap.get("Button Location") + 1);
				}
				break;
			case "User Input":
				System.out.println("here");
				if (ID == "add")
					gui.getLeftPanel().addItem(new UserInputCommand());
				else
					gui.getLeftPanel().addItemAt(new UserInputCommand());
				gui.counterMap.put("User Input", gui.counterMap.get("User Input") + 1);
				break;
			case "Play Sound":
				JFileChooser load = new JFileChooser();
				FileNameExtensionFilter wavFileFilter = new FileNameExtensionFilter("wav files (*.wav)", "wav");
				load.addChoosableFileFilter(wavFileFilter);
				load.setFileFilter(wavFileFilter);
				load.showOpenDialog(null);
				file = load.getSelectedFile();
				if (file != null) {
					if (ID == "add")
						gui.getLeftPanel().addItem(new SoundCommand(file.toString(), this.gui));
					else
						gui.getLeftPanel().addItemAt(new SoundCommand(file.toString(), this.gui));
					gui.counterMap.put("Play Sound", gui.counterMap.get("Play Sound") + 1);
				}
				break;
			case "Record Audio":
				rc.recordAudio(this.gui); // calls recordAudio method in
											// RecordAudio class
				if (rc.file != null && rc.recordFlag == true) {
					if (ID == "add")
						gui.getLeftPanel().addItem(new SoundCommand(rc.file.toString(), this.gui));
					else
						gui.getLeftPanel().addItemAt(new SoundCommand(rc.file.toString(), this.gui));
					gui.counterMap.put("Record Audio", gui.counterMap.get("Record Audio") + 1);
				}
				break;
			case "Reset Buttons":
				if (ID == "add")
					gui.getLeftPanel().addItem(new ResetButtonCommand(""));
				else
					gui.getLeftPanel().addItemAt(new ResetButtonCommand(""));
				gui.counterMap.put("Reset Buttons", gui.counterMap.get("Reset Buttons") + 1);
				break;
			case "Go To Location":
				value = JOptionPane.showInputDialog(gui, "Enter location to go to", "Edit Item Details",
						JOptionPane.PLAIN_MESSAGE, null, null, "");
				if (value != null && value != "") {
					if (ID == "add")
						gui.getLeftPanel().addItem(new SkipCommand((String) value));
					else
						gui.getLeftPanel().addItemAt(new SkipCommand((String) value));
					gui.counterMap.put("Go To Location", gui.counterMap.get("Go To Location") + 1);
				}
				break;
			case "Clear All":
				if (ID == "add")
					gui.getLeftPanel().addItem(new ClearAllCommand(""));
				else
					gui.getLeftPanel().addItemAt(new ClearAllCommand(""));
				gui.counterMap.put("Clear All", gui.counterMap.get("Clear All") + 1);
				break;
			case "Clear Cell":
				value = JOptionPane.showInputDialog(gui, "Cell number", "Edit Item Details", JOptionPane.PLAIN_MESSAGE,
						null, null, "");
				if (value != null && value != "") {
					if (ID == "add")
						gui.getLeftPanel().addItem(new ClearCellCommand((String) value));
					else
						gui.getLeftPanel().addItemAt(new ClearCellCommand((String) value));
					gui.counterMap.put("Clear Cell", gui.counterMap.get("Clear Cell") + 1);
				}
				break;
			case "Set Pins":
				value = JOptionPane.showInputDialog(gui, "Cell and pins (space separated)", "Edit Item Details",
						JOptionPane.PLAIN_MESSAGE, null, null, "");
				if (value != null && value != "") {
					if(ID=="add")
						gui.getLeftPanel().addItem(new SetPinsCommand((String) value));
					else
						gui.getLeftPanel().addItemAt(new SetPinsCommand((String) value));
					gui.counterMap.put("Set Pins", gui.counterMap.get("Set Pins") + 1);
				}
				break;
			case "Set Character":
				value = JOptionPane.showInputDialog(gui, "Cell and character (space seperated)", "Edit Item Details",
						JOptionPane.PLAIN_MESSAGE, null, null, "");
				if (value != null && value != "") {
					if(ID=="add")
						gui.getLeftPanel().addItem(new CellCharCommand((String) value));
					else
						gui.getLeftPanel().addItemAt(new CellCharCommand((String) value));
					gui.counterMap.put("Set Character", gui.counterMap.get("Set Character") + 1);
				}
				break;
			case "Raise Pin":
				value = JOptionPane.showInputDialog(gui, "Cell and Pin to raise (space separated)", "Edit Item Details",
						JOptionPane.PLAIN_MESSAGE, null, null, "");
				if (value != null && value != "") {
					if(ID=="add")
						gui.getLeftPanel().addItem(new CellRaiseCommand((String) value));
					else
						gui.getLeftPanel().addItemAt(new CellRaiseCommand((String) value));
					gui.counterMap.put("Raise Pin", gui.counterMap.get("Raise Pin") + 1);
				}
				break;
			case "Lower Pin":
				value = JOptionPane.showInputDialog(gui, "Cell and Pin to lower (space separated)", "Edit Item Details",
						JOptionPane.PLAIN_MESSAGE, null, null, "");
				if (value != null && value != "") {
					if(ID=="add")
						gui.getLeftPanel().addItem(new CellLowerCommand((String) value));
					else
						gui.getLeftPanel().addItemAt(new CellLowerCommand((String) value));
					gui.counterMap.put("Lower Pin", gui.counterMap.get("Lower Pin") + 1);
				}
				break;
			case "Set Voice":

				String[] voices = {"1. male","2. female","3. male","4. male"};
				value = JOptionPane.showInputDialog(gui, "Select a voice", "Edit Item Details",
					JOptionPane.PLAIN_MESSAGE, null, voices, voices[0]);
				if (value != null && value != "") {
					gui.getLeftPanel().addItem(new SetVoiceCommand(value.toString().substring(0, 1)));
					gui.counterMap.put("Set Voice", gui.counterMap.get("Set Voice") + 1);
				}
				break;
			case "Location Tag":
				value = JOptionPane.showInputDialog(gui, "Enter name of location", "Edit Item Details",
						JOptionPane.PLAIN_MESSAGE, null, null, "");
				if (value != null && value != "") {
					if(ID=="add")
						gui.getLeftPanel().addItem(new GoHereCommand((String) value));
					else
						gui.getLeftPanel().addItemAt(new GoHereCommand((String) value));
					gui.counterMap.put("Location Tag", gui.counterMap.get("Location Tag") + 1);
				}
				break;
			default:
				break;
			}
		}
	}
}
