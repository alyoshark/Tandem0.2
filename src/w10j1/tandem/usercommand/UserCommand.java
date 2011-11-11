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
 * @author Chenhong
 */
public class UserCommand {

	private static final String INSTRUCTION = "a for adding, e for edit, "
			+ "s for search, b for back from search result, r for remove, "
			+ "u for undo an add or remove.\r\nFor more info please refer to user manual";
	private CommandParser cpar = new CommandParserImpl();
	private CommandProcessor cpro = new CommandProcessorImpl();
	private String request = "";
	private String command = "";
	private String searchString = "";
	private boolean isAfterSearch = false;
	private String executionResultStr;
	private Log log = Log.getLogger();

	/**
	 * Empty constructor won't work without initCommand so just disable for
	 * safety concern
	 */
	private UserCommand() {
		// Doing nothing and should not be called without initCommand();
	}

	/**
	 * A constructor that has the same effect of initializer initCommand(String
	 * input)
	 * 
	 * @param input
	 *            a user input from console
	 */
	public UserCommand(String input) {
		this();
		this.initCommand(input);
	}

	/**
	 * Initialize an instance of UserCommand with a user input from console and
	 * execute accordingly
	 * 
	 * @param input
	 *            a user input from console
	 */
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

	/**
	 * Return the execution result to UI
	 * 
	 * @return executionResultStr
	 */
	public String getExecutionResults() {
		return executionResultStr;
	}

	/**
	 * Execute according to this.request
	 */
	private void execute() {
		executionResultStr = "";
		if (request == null && (command == null || command.isEmpty())) {
			return;
		}
		switch (this.request) {
		case "a":
			executeAdd();
			break;
		case "b":
			executeBack();
			break;
		case "s":
			executeSearch();
			break;
		case "u":
			executeUndo();
			break;
		case "e":
			executeEdit();
			break;
		case "r":
			executeRemove();
			break;
		case "q": // A part that breaks the abstraction level a bit.
			System.out.println("Thanks for using, bye!");
			log.getMyLogger().info("Normal exit");
			System.exit(0);
		default:
			executionResultStr = INSTRUCTION;
			log.getMyLogger().info("Unknown command" + request);
		}
		if (isAfterSearch) {
			reSearch();
		}
		executionResultStr = (executionResultStr == null) ? cpro
				.getSearchResult() : executionResultStr
				+ cpro.getSearchResult();
	}

	/**
	 * Doing a search again after the list changes.
	 */
	private void reSearch() {
		try {
			cpro.search(searchString);
		} catch (Exception e) {
			log.getMyLogger().error("error", e);
		}
	}

	private void executeRemove() {
		try {
			cpro.remove(command);
		} catch (IndexOutOfBoundsException e0) {
			executionResultStr = "Please give an index within the range, try again...\r\n";
		} catch (NumberFormatException e1) {
			executionResultStr = "Please give an integer as index for the list shown above\r\n";
		}
	}

	private void executeEdit() {
		try {
			cpro.edit(command);
		} catch (NumberFormatException e) {
			log.getMyLogger().error("error", e);
			executionResultStr = "Sorry, edit failed :O\r\n";
		} catch (ParseException e) {
			log.getMyLogger().error("error", e);
			executionResultStr = "Sorry, edit failed :O\r\n";
		}
	}

	private void executeUndo() {
		cpro.undo();
		executionResultStr = "Last operation just canceled.\r\n";
	}

	private void executeSearch() {
		searchString = command;
		try {
			cpro.search(searchString);
		} catch (Exception e) {
			log.getMyLogger().error("error", e);
		}
		isAfterSearch = true;
	}

	private void executeBack() {
		searchString = "";
		if (isAfterSearch) {
			isAfterSearch = false;
			try {
				cpro.search("");
			} catch (Exception e) {
				log.getMyLogger().error("error", e);
			}
		}
	}

	private void executeAdd() {
		try {
			this.cpar.processDue();
			Task tempTask = new TaskImpl(this.cpar.getDue(),
					this.cpar.getCommand());
			this.cpro.add(tempTask);
			executionResultStr = tempTask.toString() + "is added!\r\n";
		} catch (ParseException e0) {
			log.getMyLogger().error("error", e0);
			executionResultStr = "Sorry, adding of the task failed due to "
					+ "failing to parse some fields :O\r\n"
					+ "Please check your input of for the date and time fields\r\n";
		} catch (ArrayIndexOutOfBoundsException e1) {
			log.getMyLogger().error("error", e1);
			executionResultStr = "Sorry, adding of the task failed due to "
					+ "inadequate number of inputs :O\r\n";
		}
	}
}