package commands;

/**
 * Command wrapper to represent the /~point and /~offset in the Scenario Parser.
 * Offset represents the total score, compared to what the user has scored.
 * This command allows tracking of users correct answers, and total answers
 * from QuestionCommand.
 * 
 * This command is not implemented to be added in the Authoring App.
 * 
 * @author Dyllan Bertrand
 * @version 1.0
 * @since 2019-03-08
 */
public class PointCommand implements PlayerCommand {

	
	String type;
	/**
	 * Default Constructor that used to instantiate the /~point section
	 */
	public PointCommand() {
		type = "";
	}
	
	/**
	 * This constructor is used to instantiate the point command with the
	 * "offset" value to add to the total score
	 * 
	 * @param type - This is passed a specific value of offset.
	 */
	public PointCommand(String type) {
		this.type = type;
	}
	
	@Override
	public String toString() {
		return "";
	}
	
	@Override
	public String serialize() {
		if(!type.equals("offset"))
			return "/~point";
		else
			return "/~offset";
	}

	@Override
	public String getEditLabel() {
		return "Ignored";
	}

	@Override
	public String getCurrentValue() {
		return "";
	}

	@Override
	public void setCurrentValue(String newValue) {
		
	}

	@Override
	public void editCommand() {
		

	}

}
