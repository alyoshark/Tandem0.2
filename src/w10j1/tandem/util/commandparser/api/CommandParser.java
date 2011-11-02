package w10j1.tandem.util.commandparser.api;

import java.text.ParseException;
import java.util.Calendar;

/**
 *
 * @author Chris
 */
public interface CommandParser {

    void processDue() throws ParseException;

    void readRawInput(String input);

    void setRequest();

    public String getRequest();

    public String getCommand();
    
    public Calendar getDue();
    
}