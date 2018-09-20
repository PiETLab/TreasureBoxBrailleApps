package commands;

import javax.swing.JOptionPane;

/**
 * A class to represent button repeating commands.
 *
 * @author Dilshad Khatri, Alvis Koshy, Drew Noel, Jonathan Tung
 * @version 1.0
 * @since 2017-04-01
 *
 */
public class RepeatButtonCommand implements PlayerCommand {

	private String buttonNum;

	/***
	 * Constructor for PauseCommand.
	 *
	 * @param waitTime
	 *            The number of seconds for the pause to wait
	 */
	public RepeatButtonCommand(String waitTime) {
		this.buttonNum = waitTime;
	}

	@Override
	public String serialize() {
		return "/~repeat-button:" + buttonNum;
	}

	@Override
	public String toString() {
		return "Press this button to repeat TTS: " + buttonNum;
	}

	@Override
	public String getEditLabel() {
		return "Button to use for repeating";
	}

	@Override
	public String getCurrentValue() {
		return buttonNum;
	}

	@Override
	public void setCurrentValue(String waitTime) {
		this.buttonNum = waitTime;
	}
	
	@Override
	public void editCommand() {
		Object value;
		value = JOptionPane.showInputDialog(null, "Button to use for repeating", "Edit Item Details",
				JOptionPane.PLAIN_MESSAGE, null, null, this.buttonNum);
		if (value != null && value != "") {
			this.buttonNum = (String) value;
			}
		}
	

}
