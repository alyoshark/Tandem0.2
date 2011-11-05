package w10j1.tandem.usercommand;

import java.text.ParseException;
import w10j1.tandem.logger.Log;
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

	private static final String INSTRUCTION = "a for adding, e for edit, "
			+ "s for search, b for back from search result, r for remove"
			+ "u for undo an add or remove \r\nFor more info please refer to user manual";
	private CommandParser cpar = new CommandParserImpl();
	private CommandProcessor cpro = new CommandProcessorImpl();
	private String request = "";
	private String command = "";
	private boolean isAfterSearch = false;
	private String executionResultStr;
	private Log log = Log.getLogger();

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
		this();
		this.initCommand(input);
	}

	public void initCommand(String input) {
		cpar.readRawInput(input);
		try {
			cpar.setRequest();
		} catch (ParseException e0) {
			executionResultStr = INSTRUCTION;
			log.getMyLogger().error("error", e0);
		} catch (StringIndexOutOfBoundsException e1) {
			log.getMyLogger().info("Empty input", e1);
			return;
		}
		this.request = cpar.getRequest();
		this.command = cpar.getCommand();
		execute();
	}

	public String getExecutionResults() {
		return executionResultStr;
	}

	public void execute() {
		executionResultStr = "";
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
				log.getMyLogger().error("error", e0);
				executionResultStr = "Sorry, adding of the task failed due to "
						+ "failing to parse some fields :O\n"
						+ " Please check your input of for the date and time fields\n";
			} catch (ArrayIndexOutOfBoundsException e1) {
				log.getMyLogger().error("error", e1);
				executionResultStr = "Sorry, adding of the task failed due to "
						+ "inadequate number of inputs :O\n";
			}
			break;
		case "b":
			if (isAfterSearch) {
				isAfterSearch = false;
				try {
					cpro.search("");
				} catch (Exception e) {
					log.getMyLogger().error("error", e);
				}
				break;
			}
			break;
		case "q":  // A part that breaks the abstraction level a bit.
			System.out.println("Thanks for using, bye!");
			log.getMyLogger().info("Normal exit");
			System.exit(0);
		case "s":
			try {
				cpro.search(command);
			} catch (Exception e) {
				log.getMyLogger().error("error", e);
			}
			isAfterSearch = true;
			// executionResultStr += "\r\nEnter d followed by an index to "
			// + "delete a task or else to go back";
			break;
		case "u":
			cpro.undo();
			executionResultStr = "Last operation just canceled.";
			break;
		case "e":
			try {
				cpro.edit(command);
			} catch (NumberFormatException e) {
				log.getMyLogger().error("error", e);
				executionResultStr = "Sorry, edit failed :O";
			} catch (ParseException e) {
				log.getMyLogger().error("error", e);
				executionResultStr = "Sorry, edit failed :O";
			}
			executionResultStr = "";
			break;
		case "r":
			cpro.remove(command);
			executionResultStr = "";
			break;
		default:
			executionResultStr = INSTRUCTION;
			log.getMyLogger().info("Unknown command" + request);
		}
		executionResultStr = (executionResultStr == null) ?
				cpro.getSearchResult() :
					executionResultStr + cpro.getSearchResult();
	}
}