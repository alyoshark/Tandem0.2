package w10j1.tandem.ui.main;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import w10j1.tandem.logger.Log;
import w10j1.tandem.usercommand.UserCommand;

public class Main {
	public static UserCommand userCommand;
	public static Scanner sc = new Scanner(System.in);
	public final static SimpleDateFormat formatter = new SimpleDateFormat(
			"dd/MM/yyyy hh:mm");
	public static Log log = Log.getLogger();
		
	public static void main(String[] args) {
		System.out.println("Welcome to Tandem! It is now "
				+ formatter.format(new Date()));
		userCommand = new UserCommand("s");
		System.out.println("Your top pending tasks:");
		System.out.println(userCommand.getExecutionResults());
		while (true) {
			System.out.printf(">>");
			String input = sc.nextLine();
			log.getMyLogger().info("User Input: " + input);
			userCommand.initCommand(input);
			if (userCommand.getExecutionResults() != null)
				System.out.println(userCommand.getExecutionResults());
		}
	}
}
