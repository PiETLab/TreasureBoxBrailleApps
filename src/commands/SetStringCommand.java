package commands;

import javax.swing.JOptionPane;

/**
 * Command wrapper to represent the /~disp-string command in the player. Values
 * are given as a single string, which is the string which should be shown on
 * the player.
 *
 * @author Dilshad Khatri, Alvis Koshy, Drew Noel, Jonathan Tung
 * @version 1.0
 * @since 2017-04-01
 */
public class SetStringCommand implements PlayerCommand {

	private String str = "";

	/**
	 * Create a new SetStringCommand which has the internal value set to an
	 * input value
	 * 
	 * @param str
	 *            Desired input value for the command to have
	 */
	public SetStringCommand(String str) {
		this.str = str;
	}

	@Override
	public String toString() {
		return "Display on Braille cells: " + str;
	}

	@Override
	public String serialize() {
		return "/~disp-string:" + str;
	}

	@Override
	public String getEditLabel() {
		return "String to display";
	}

	@Override
	public String getCurrentValue() {
		return str;
	}

	@Override
	public void setCurrentValue(String str) {
		this.str = str;
	}

	@Override
	public void editCommand() {
		Object value;
		value = JOptionPane.showInputDialog(null, "String to display", "Edit Item Details",
				JOptionPane.PLAIN_MESSAGE, null, null, this.str);
		if (value != null && value != "") {
			this.str=(String) value;
	}
	}
	
}
