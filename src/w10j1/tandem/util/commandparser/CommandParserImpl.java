package w10j1.tandem.util.commandparser;

import com.mdimension.jchronic.Chronic;
import java.text.ParseException;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import w10j1.tandem.logger.Log;
import w10j1.tandem.util.commandparser.api.CommandParser;

/**
 * 
 * @author Chenhong
 */
public class CommandParserImpl implements CommandParser {

	public final String DATE_PATTERN_STR = "^(([0-2]\\d)|(3[0-1])|(\\d))((/|-)?)((0[1-9])|(1[0-2])|([1-9]))";
	public final String COMMAND_ISO_STR = "^([abdeqrsu])(\\s+)(.*)";
	public final Pattern COMMAND_ISO = Pattern.compile(COMMAND_ISO_STR,
			Pattern.CASE_INSENSITIVE);
	public final Pattern DATE_PATTERN = Pattern.compile(DATE_PATTERN_STR);
	private Calendar due;
	private String request = "";
	private String command = "";

	private Log log = Log.getLogger();

	/**
	 * Empty constructor, will have to initalized by readRawInput
	 */
	public CommandParserImpl() {
		// Doing nothing first
	}

	/**
	 * Reading input that needs to be parsed
	 * 
	 * @param input
	 *            a raw input to be parsed
	 */
	@Override
	public void readRawInput(String input) {
		this.command = input.trim();
	}

	/**
	 * Separating request from command
	 * 
	 * @throws ParseException
	 * @throws StringIndexOutOfBoundsException
	 */
	@Override
	public void setRequest() throws ParseException,
			StringIndexOutOfBoundsException {
		Matcher match = COMMAND_ISO.matcher(command);
		if (match.find()) {
			request = match.group(1);
			command = match.group(3);
		} else if (command.split("\\s+").length > 1) {
			request = "a";
		} else {
			request = command.substring(0, 1).toLowerCase();
			command = command.substring(1).trim().toLowerCase();
			if (!Pattern.compile("[abdeqrsu]").matcher(request).find()) {
				ParseException e = new ParseException(
						"Can't parse this command, most likely not enough arguments",
						0);
				log.getMyLogger().error("error", e);
				throw e;
			}
		}
	}

	/**
	 * Facilitating the adding function by retrieving a Calendar from input to
	 * use in attribute Due for Task
	 * 
	 * @throws ParseException
	 * @throws ArrayIndexOutOfBoundsException
	 */
	@Override
	public void processDue() throws ParseException,
			ArrayIndexOutOfBoundsException {
		Matcher match = DATE_PATTERN.matcher(command);
		if (match.find()) {
			try {
				FormattedParser fp = new FormattedParser();
				fp.setRawInput(command);
				fp.processCommand();
				this.due = fp.getDue();
				this.command = fp.getDesc();
			} catch (ArrayIndexOutOfBoundsException e) {
				Logger.getLogger(CommandParserImpl.class.getName()).log(
						Level.WARNING, "Not enough parameters supplied", e);
				throw e;
			}
		} else if (Chronic.parse(command) != null) {
			due = Chronic.parse(command).getEndCalendar();
		} else {
			ParseException e = new ParseException(
					"Can't parse this command, most likely an incorrect input",
					0);
			log.getMyLogger().error("error", e);
			throw e;
		}
	}

	/**
	 * Getting the user request after parsing
	 * 
	 * @return request
	 */
	@Override
	public String getRequest() {
		return this.request;
	}

	/**
	 * Getting the content of a command after parsing
	 * 
	 * @return command
	 */
	@Override
	public String getCommand() {
		return this.command;
	}

	/**
	 * Getting the due of a task if the command is an add
	 * 
	 * @return due
	 */
	@Override
	public Calendar getDue() {
		return this.due;
	}
}