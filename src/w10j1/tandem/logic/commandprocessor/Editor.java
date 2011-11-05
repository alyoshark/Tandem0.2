package w10j1.tandem.logic.commandprocessor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import w10j1.tandem.logger.Log;
import w10j1.tandem.storage.datakeeper.api.DataKeeper;

public class Editor {

	public final Pattern splitter = Pattern.compile(
			"\\s*(\\d+)\\s+((time)|(desc))\\s+(.*)", Pattern.CASE_INSENSITIVE);
	public final SimpleDateFormat df = new SimpleDateFormat("ddMMyy hhmm");

	public enum Attributes {
		DUE, DESC
	};

	private int index;
	private Attributes attr;
	private String content;
	private Log log = Log.getLogger();

	public void edit(String command, DataKeeper dk)
			throws NumberFormatException, ParseException {
		parseCommand(command);
		switch (getAttr()) {
		case DUE:
			Calendar cal = Calendar.getInstance();
			cal.setTime(df.parse(content));
			dk.getTaskList().get(dk.getSearchList().get(index - 1)).setDue(cal);
			break;
		case DESC:
			dk.getTaskList().get(dk.getSearchList().get(index - 1))
					.setDesc(content);
			break;
		}
	}

	private void parseCommand(String command) throws NumberFormatException,
			ParseException {
		try {
			Matcher getter = splitter.matcher(command);
			if (getter.find()) {
				index = Integer.parseInt(getter.group(1));
				String rawAttr = getter.group(2);
				if (rawAttr.compareTo("time") == 0)
					attr = Attributes.DUE;
				else
					attr = Attributes.DESC;
				content = getter.group(5);
			} else {
				ParseException e = new ParseException("Cannot match pattern "
						+ splitter, 0);
				log.getMyLogger().error("error", e);
				throw e;
			}
		} catch (NumberFormatException e) {
			log.getMyLogger().error("error", e);
			throw e;
		}
	}

	public int getIndex() {
		return index;
	}

	public Attributes getAttr() {
		return attr;
	}

	public String getContent() {
		return content;
	}
}