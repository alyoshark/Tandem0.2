package w10j1.tandem.util.commandparser;

import com.mdimension.jchronic.Chronic;
import java.text.ParseException;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import w10j1.tandem.util.commandparser.api.CommandParser;

/**
 * 
 * @author Chris
 */
public class CommandParserImpl implements CommandParser {

	public final String DATE_PATTERN_STR = "^(([0-2]\\d)|(3[0-1])|(\\d))((/|-)?)((0[1-9])|(1[0-2])|([1-9]))";
	public final String COMMAND_ISO_STR = "^([abdersu])(\\s+)(.*)";
	public final Pattern COMMAND_ISO = Pattern.compile(COMMAND_ISO_STR,
			Pattern.CASE_INSENSITIVE);
	public final Pattern DATE_PATTERN = Pattern.compile(DATE_PATTERN_STR);
	private Calendar due;
	private String request = "";
	private String command = "";

	public CommandParserImpl() {
		// Doing nothing first
	}

	@Override
	public void readRawInput(String input) {
		this.command = input.trim();
	}

	@Override
	public void setRequest() {
		Matcher match = COMMAND_ISO.matcher(command);
		if (match.find()) {
			request = match.group(1);
			command = match.group(3);
		} else {
			request = "a";
		}
	}

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
						Level.SEVERE, null, e);
				throw e;
			}
		} else if ((this.due = Chronic.parse(command).getEndCalendar()) != null) {
		} else {
			ParseException e = new ParseException(
					"Can't parse this command, most likely an incorrect input",
					0);
			Logger.getLogger(CommandParserImpl.class.getName()).log(
					Level.SEVERE, null, e);
			throw e;
		}
	}

	@Override
	public String getRequest() {
		return this.request;
	}

	@Override
	public String getCommand() {
		return this.command;
	}

	@Override
	public Calendar getDue() {
		return this.due;
	}
}