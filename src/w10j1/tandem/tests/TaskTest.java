package w10j1.tandem.tests;

import static org.junit.Assert.*;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.junit.Before;
import org.junit.Test;

import w10j1.tandem.storage.task.TaskImpl;
import w10j1.tandem.storage.task.api.Task;

public class TaskTest {

	private Task t = new TaskImpl();

	@Before
	public void setUp() throws Exception {
		t = new TaskImpl(Calendar.getInstance(), "dummy msg");
	}

	@Test
	public void testTaskImpl() {
		assertNotNull(t);
	}

	@Test
	public void testGetDesc() {
		assertEquals(t.getDesc().compareTo("dummy msg"), 0);
	}

	@Test
	public void testGetDue() {
		assertEquals(t.getDue().compareTo(Calendar.getInstance()), 0);
	}

	@Test
	public void testToString() {
		assertEquals(
				t.toString(),
				(new SimpleDateFormat("dd-MM-yyyy HH:mm")).format(t.getDue()
						.getTime()) + "|dummy msg\r\n");
	}

}