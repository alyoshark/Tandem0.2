package w10j1.tandem.tests;

import static org.junit.Assert.*;

import java.text.ParseException;

import org.junit.Before;
import org.junit.Test;

import w10j1.tandem.util.commandparser.CommandParserImpl;
import w10j1.tandem.util.commandparser.api.CommandParser;

public class CommandParserTest {

	private CommandParser cp = new CommandParserImpl();
	
	@Before
	public void setUp() throws Exception {
		cp.readRawInput("s exam");
	}

	@Test
	public void testCommandParserImpl() {
		assertNotNull(cp);
	}

	@Test
	public void testSetRequest() throws ParseException {
		cp.readRawInput("s     	 ");
		cp.setRequest();
		System.out.println(cp.getRequest());
		assertEquals("s", cp.getRequest());
	}
}