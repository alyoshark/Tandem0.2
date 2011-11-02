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

    public static CommandParser cpar = new CommandParserImpl();
    public static CommandProcessor cpro = new CommandProcessorImpl();
    public String request = "";
    public String command = "";
    private boolean isAfterSearch = false;
    String executionResultStr;

    public UserCommand() {
        // Doing nothing and should not be called without initCommand();
    }

    /**
     * A constructor that has the same effect of initializer initCommand(String input)
     * @param input
     */
    public UserCommand(String input) {
        UserCommand.cpar.readRawInput(input);
        UserCommand.cpar.setRequest();
        this.request = UserCommand.cpar.getRequest();
        this.command = UserCommand.cpar.getCommand();
    }

    public void initCommand(String input) {
        UserCommand.cpar.readRawInput(input);
        UserCommand.cpar.setRequest();
        this.request = UserCommand.cpar.getRequest();
        this.command = UserCommand.cpar.getCommand();
    }

    public String getExecutionResults() {
        return executionResultStr;
    }

    public void execute() {
        if (this.command == null || this.command.isEmpty()) {return;}
        switch (this.request) {
            case "a":
                try {
                    this.cpar.processDue();
                    Task tempTask = new TaskImpl(this.cpar.getDue(), this.command);
                    this.cpro.add(tempTask);
                    executionResultStr = tempTask.toString() + " is added!";
                    break;
                } catch (ParseException ex) {
                    Logger.getLogger(UserCommand.class.getName()).log(Level.SEVERE, null, ex);
                    executionResultStr = "Sorry, adding of the task failed :O";
                }
            case "s":
                this.cpro.search(this.command);
                executionResultStr = cpro.search(command);
                if (executionResultStr.isEmpty()) {
                    executionResultStr = "No result found.";
                }
                isAfterSearch = true;
                executionResultStr += "\r\nEnter d followed by an index to delete a task or else to go back";
                break;
            case "u":
                this.cpro.undo();
                executionResultStr = "Last operation just canceled.";
                break;
            // case "e":
            case "r":
            case "d":
                if (isAfterSearch) {
                    /*
                     * if (this.request.compareTo("e") == 0) {
                     * this.cpro.edit(this.command); executionResultStr =""; }
                     * else
                     */ if (this.request.compareTo("r") == 0) {
                        this.cpro.remove(this.command);
                        executionResultStr = "";
                    } else if (this.request.compareTo("d") == 0) {
                        this.cpro.setDone(this.command);
                        executionResultStr = "Marked Done :D";
                    }
                } else {
                    executionResultStr = "Please perform a search first: s {keywords/date}";
                }
                break;
            default:
            // Print an error message in the GUI.
            // May be done by throwing an exception.
        }
    }
}