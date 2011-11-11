package w10j1.tandem.util.commandparser.api;

import java.text.ParseException;
import java.util.Calendar;

/**
 * 
 * @author Chenhong
 */
public interface CommandParser {

	/**
	 * Facilitating the adding function by retrieving a Calendar from input to
	 * use in attribute Due for Task
	 * 
	 * @throws ParseException
	 * @throws ArrayIndexOutOfBoundsException
	 */
    void processDue() throws ParseException;

    /**
	 * Reading input that needs to be parsed
	 * 
	 * @param input
	 *            a raw input to be parsed
	 */
    void readRawInput(String input);

    /**
	 * Separating request from command
	 * 
	 * @throws ParseException
	 * @throws StringIndexOutOfBoundsException
	 */
    void setRequest() throws ParseException;

    /**
	 * Getting the user request after parsing
	 * 
	 * @return request
	 */
    public String getRequest();

    /**
	 * Getting the content of a command after parsing
	 * 
	 * @return command
	 */
    public String getCommand();
    
    /**
	 * Getting the due of a task if the command is an add
	 * 
	 * @return due
	 */
    public Calendar getDue();
    
}