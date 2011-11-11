package w10j1.tandem.logger;

import org.apache.log4j.*;

/**
 * 
 * @author Chenhong
 */
public class Log {
	private Logger myLogger;
	private static Log log;

	private Log() {
		String filePath = this.getClass().getResource("/").getPath();
		filePath = filePath.substring(1).replace("bin", "src");
		myLogger = Logger.getLogger(this.getClass());
		PropertyConfigurator.configure(filePath + "log4j.property");
	}

	public static Log getLogger() {
		if (log != null)
			return log;
		else
			return new Log();
	}

	public Logger getMyLogger() {
		return myLogger;
	}
}
