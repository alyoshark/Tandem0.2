/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package w10j1.tandem.util.commandparser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * @author Thanh, Chenhong
 */
public class FormattedParser {

	/**
	 * Regular expressions that will be used to get the Date and Time out of the
	 * raw input
	 */
	public final String DATE_PATTERN_STR = "^(([0-2]\\d)|(3[0-1])|(\\d))((/|-)?)((0[1-9])|(1[0-2])|([1-9]))$";
	public final String DATE_YEAR_PATTERN_STR = "^(([0-2]\\d)|(3[0-1])|(\\d))((/|-)?)((0[1-9])|(1[0-2])|([1-9]))(/|-)*(\\d{4})$";
	public final String TIME_VALID_STR = "^(([0-1]\\d)|(2[0-3])|(\\d))((:)?)(([0-5]\\d)|(\\d))$";
	public final String TIME_EXISTS_PATTERN_STR = "^(\\d{1,2})((:)?)(\\d{1,2})$";
	/**
	 * Patterns generated base on the regular expressions
	 */
	public final Pattern DATE_PATTERN = Pattern.compile(DATE_PATTERN_STR);
	public final Pattern DATE_YEAR_PATTERN = Pattern
			.compile(DATE_YEAR_PATTERN_STR);
	public final Pattern TIME_VALID_PATTERN = Pattern.compile(TIME_VALID_STR);
	public final Pattern TIME_EXISTS_PATTERN = Pattern
			.compile(TIME_EXISTS_PATTERN_STR);
	public final int DATE_INDEX = 0;
	public final int TIME_INDEX = 1;
	public final int DATE_MATCHER_INDEX = 1;
	public final int MONTH_MATCHER_INDEX = 7;
	public final int YEAR_MATCHER_INDEX = 12;
	public final String DATE_FORMAT = "dd/MM/yyyy";
	public final int HOUR_MATCHER_INDEX = 1;
	public final int MINUTE_MATCHER_INDEX = 7;
	/**
	 * Attributes of the class
	 */
	private String rawInput;
	private String[] commandDetails;
	private boolean dateIsValid; // indicate whether date is valid
	private boolean timeIsValid; // indicate whether time value is valid
	private boolean timeExists; // indicate whether the user entered the time
								// but its value is invalid
	/**
	 * Essential attributes of the program, which will be used by other classes
	 */
	private short date;
	private short month;
	private short year;
	private short hour;
	private short minute;
	private String description;

	/**
	 * Receives the input from the user
	 */
	public void setRawInput(String input) {
		rawInput = input;
	}

	private void resetAttributes() {
		dateIsValid = false;
		date = month = year = hour = minute = 0;
		description = "";
	}

	public void processCommand() {
		resetAttributes();
		tokenizeInput(rawInput);
		processDate();
		processTime();
		processDesc();
	}

	private void tokenizeInput(String rawInput) {
		commandDetails = rawInput.split("\\s");
	}

	private boolean dateChecker(String dateStr) {
		Date parseResult = null;
		SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
		format.setLenient(false);
		try {
			parseResult = format.parse(dateStr);
			if (parseResult != null) {
				return true;
			}
		} catch (ParseException e) {
			ParseException ex = new ParseException(
					"Can't parse this command, most likely an incorrect input",
					0);
			Logger.getLogger(FormattedParser.class.getName()).log(Level.SEVERE,
					null, ex);
		}
		return false;
	}

	private void processDate() {
		String dateStr = "";
		boolean withoutYear = false;
		Matcher dateMatcher = DATE_PATTERN.matcher(commandDetails[DATE_INDEX]);
		Matcher date_yearMatcher = DATE_YEAR_PATTERN
				.matcher(commandDetails[DATE_INDEX]);
		if (dateMatcher.find()) {
			withoutYear = true;
			dateStr += (dateMatcher.group(1) + "/");
			dateStr += dateMatcher.group(7);
			getDate(get_dateComponents(dateStr), withoutYear);
			dateStr = concatYear(dateStr, year);
			dateIsValid = dateChecker(dateStr);
		} else if (date_yearMatcher.find()) {
			dateStr += (date_yearMatcher.group(1) + "/");
			dateStr += (date_yearMatcher.group(7) + "/");
			dateStr += date_yearMatcher.group(12);
			getDate(get_dateComponents(dateStr), withoutYear);
			dateIsValid = dateChecker(dateStr);
		}
	}

	private void processTime() throws NumberFormatException,
			ArrayIndexOutOfBoundsException {
		try {
			Matcher timeIsValidMatcher = TIME_VALID_PATTERN
					.matcher(commandDetails[TIME_INDEX]);
			Matcher timeExistsMatcher = TIME_EXISTS_PATTERN
					.matcher(commandDetails[TIME_INDEX]);
			if (timeIsValidMatcher.find()) {
				hour = (short) Integer.parseInt(timeIsValidMatcher
						.group(HOUR_MATCHER_INDEX));
				minute = (short) Integer.parseInt(timeIsValidMatcher
						.group(MINUTE_MATCHER_INDEX));
				timeIsValid = true;
			} else if (timeExistsMatcher.find()) {
				timeExists = true;
				System.out
						.println("Time value is invalid. It will be set to the default value 00:00");
			} else {
				System.out
						.println("Time not entered. It will be set to the default value 00:00");
			}
		} catch (NumberFormatException e1) {
			Logger.getLogger(FormattedParser.class.getName()).log(Level.SEVERE,
					null, e1);
			throw e1;
		} catch (ArrayIndexOutOfBoundsException e2) {
			Logger.getLogger(FormattedParser.class.getName()).log(Level.SEVERE,
					null, e2);
			throw e2;
		}
	}

	private void processDesc() {
		if (dateIsValid) {
			int start = 1;
			if ((timeIsValid) || (timeExists)) {
				start = TIME_INDEX + 1;
			}
			for (int i = start; i < commandDetails.length; i++)
				description += commandDetails[i] + " ";
		}
	}

	private void getDate(String[] dateComponents, boolean withoutYear) {
		date = (short) Integer.parseInt(dateComponents[0]);
		month = (short) Integer.parseInt(dateComponents[1]);
		Calendar c = Calendar.getInstance();
		if (withoutYear) {
			if (((month < c.get(Calendar.MONTH) + 1))
					|| (month == c.get(Calendar.MONTH) + 1)
					&& (date < c.get(Calendar.DATE))) {
				year = (short) (c.get(Calendar.YEAR) + 1);
			} else {
				year = (short) c.get(Calendar.YEAR);
			}
		} else {
			year = (short) Integer.parseInt(dateComponents[2]);
		}
	}

	private String concatYear(String dateStr, int year) {
		dateStr += "/" + Integer.toString(year);
		return dateStr;
	}

	private String[] get_dateComponents(String dateStr) {
		String[] results;
		results = dateStr.split("/");
		return results;
	}

	public Calendar getDue() throws ArrayIndexOutOfBoundsException {
		Calendar cal = new GregorianCalendar();
		try {
			cal.set(Calendar.YEAR, year);
			cal.set(Calendar.MONTH, month - 1);
			cal.set(Calendar.DATE, date);
			cal.set(Calendar.HOUR_OF_DAY, hour);
			cal.set(Calendar.MINUTE, minute);
		} catch (ArrayIndexOutOfBoundsException e) {
			Logger.getLogger(FormattedParser.class.getName()).log(Level.SEVERE,
					null, e);
			throw e;
		}
		return cal;
	}

	public String getDesc() {
		return description;
	}
}