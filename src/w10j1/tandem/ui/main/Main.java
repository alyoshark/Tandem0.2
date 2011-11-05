package w10j1.tandem.ui.main;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import w10j1.tandem.usercommand.UserCommand;

public class Main {
	public static UserCommand userCommand;
	public static Scanner sc = new Scanner(System.in);
	public final static SimpleDateFormat formatter = new SimpleDateFormat(
			"dd/MM/yyyy hh:mm");
	
	public static void main(String[] args) {
		System.out.println("Welcome to Tandem! It is now "
				+ formatter.format(new Date()));
		userCommand = new UserCommand();
		
		while (true) {
			userCommand.initCommand(sc.nextLine());
			if (userCommand.getExecutionResults() != null)
				System.out.println(userCommand.getExecutionResults());
		}
	}
}
