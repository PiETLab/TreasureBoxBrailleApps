package commands;

/**
 * Command wrapper to represent the /~say-score and /~say-total in the player. There are no
 * input values from the user. The values are automatically assigned if the user chooses
 * to use to output the user score or score total.
 * 
 * @author Dyllan Bertrand
 * @version 1.0
 * @since 2019-03-21
 */
public class SayScoreCommand implements PlayerCommand {

	boolean scoreType;
	
	/**
	 * Constructs a SayScoreCommand with the scoreType value as the input value
	 * 
	 * @param scoreType
	 * 			Determines whether the command type is Score or Total
	 */
	public SayScoreCommand(boolean scoreType) {
		this.scoreType = scoreType;
	}
	
	@Override
	public String toString() {
		if(this.scoreType)
			return "Say user score";
		else
			return "Say score total";
	}
	
	@Override
	public String serialize() {
		if(this.scoreType)
			return "/~say-score";
		else
			return "/~say-total";
	}

	@Override
	public String getEditLabel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCurrentValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setCurrentValue(String newValue) {
		// TODO Auto-generated method stub

	}

	@Override
	public void editCommand() {
		// TODO Auto-generated method stub

	}

}
