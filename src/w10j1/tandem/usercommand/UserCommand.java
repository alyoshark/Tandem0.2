package w10j1.tandem.usercommand;

import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import w10j1.tandem.logic.commandprocessor.CommandProcessorImpl;
import w10j1.tandem.logic.commandprocessor.api.CommandProcessor;
import w10j1.tandem.storage.task.TaskImpl;
import w10j1.tandem.storage.task.api.Task;
import w10j1.tandem.util.commandparser.CommandParserImpl;
import w10j1.tandem.util.commandparser.api.CommandParser;

/**
 * 
 * @author Chris
 */
public class UserCommand {

	public CommandParser cpar = new CommandParserImpl();
	public CommandProcessor cpro = new CommandProcessorImpl();
	public String request = "";
	public String command = "";
	private boolean isAfterSearch = false;
	String executionResultStr;

	public UserCommand() {
		// Doing nothing and should not be called without initCommand();
	}

	/**
	 * A constructor that has the same effect of initializer initCommand(String
	 * input)
	 * 
	 * @param input
	 */
	public UserCommand(String input) {
		cpar.readRawInput(input);
		cpar.setRequest();
		this.request = cpar.getRequest();
		this.command = cpar.getCommand();
		execute();
	}

	public void initCommand(String input) {
		cpar.readRawInput(input);
		cpar.setRequest();
		this.request = cpar.getRequest();
		this.command = cpar.getCommand();
		execute();
	}

	public String getExecutionResults() {
		return executionResultStr;
	}

	public void execute() {
		if (this.command == null || this.command.isEmpty()) {
			return;
		}
		switch (this.request) {
		case "a":
			try {
				this.cpar.processDue();
				Task tempTask = new TaskImpl(this.cpar.getDue(),
						this.cpar.getCommand());
				this.cpro.add(tempTask);
				executionResultStr = tempTask.toString() + "is added!";
				break;
			} catch (ParseException e0) {
				Logger.getLogger(UserCommand.class.getName()).log(Level.SEVERE,
						null, e0);
				executionResultStr = "Sorry, adding of the task failed due to failing to parse some fields :O"
						+ " Please check your input of for the date and time fields\n";
			} catch (ArrayIndexOutOfBoundsException e1) {
				Logger.getLogger(UserCommand.class.getName()).log(Level.SEVERE,
						null, e1);
				executionResultStr = "Sorry, adding of the task failed due to inadequate number of inputs :O\n";
			}
		case "s":
			try {
				executionResultStr = cpro.search(command);
			} catch (Exception e) {
				Logger.getLogger(UserCommand.class.getName()).log(Level.SEVERE,
						null, e);
				e.printStackTrace();
			}
			if (executionResultStr.isEmpty()) {
				executionResultStr = "No result found.";
				break;
			}
			isAfterSearch = true;
			executionResultStr += "\r\nEnter d followed by an index to delete a task or else to go back";
			break;
		case "u":
			cpro.undo();
			executionResultStr = "Last operation just canceled.";
			break;
		case "e":
			cpro.edit(command);
			executionResultStr = "";
			break;
		case "r":
			cpro.remove(command);
			executionResultStr = "";
			break;
		default:
			// Print an error message in the GUI.
			// May be done by throwing an exception.
		}
	}
}