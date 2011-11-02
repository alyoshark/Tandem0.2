package w10j1.tandem.tests;

import static org.junit.Assert.*;

import java.util.Calendar;

import org.junit.Before;
import org.junit.Test;

import w10j1.tandem.storage.datakeeper.DataKeeperImpl;
import w10j1.tandem.storage.task.TaskImpl;

public class DataKeeperTest {

	private DataKeeperImpl dk = new DataKeeperImpl();
	
	@Before
	public void setUp() throws Exception {
		for (int i = 10; i > 0; i--) {
			Calendar time = Calendar.getInstance();
			time.set(2011, i, i, i, i);
			dk.addTask(new TaskImpl(time, "dummy msg"));
		}
	}
	
	@Test
	public void testDataKeeperImpl() {
		assertNotNull(dk);
		assertNotNull(dk.taskList);
	}
	
	@Test
	public void testAddTask() {
		int oldSize = dk.taskList.size();
		Calendar time = Calendar.getInstance();
		dk.addTask(new TaskImpl(time, "dummy msg"));
		assertEquals(oldSize+1, dk.taskList.size());
		for (int i = 0; i < 5; i++) {
			int old = dk.taskList.size();
			dk.addTask(new TaskImpl(Calendar.getInstance(), "dummy msg"));
			assertEquals(old+1, dk.taskList.size());
		}
		assertEquals(oldSize + 6, dk.taskList.size());
	}

	@Test
	public void testAscDue() {
		dk.ascDue();
		assertTrue(dk.taskList.get(0).getDue().compareTo(dk.taskList.get(1).getDue()) > 0);
	}

	@Test
	public void testDecDue() {
		assertEquals(3, dk.taskList.size());
		dk.decDue();
		assertTrue(dk.taskList.get(0).getDue().compareTo(dk.taskList.get(1).getDue()) > 0);
	}
	
	@Test
	public void testMemToFile() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testFileToMem() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testResultString() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testSearchTaskSpan() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testRemoveTask() {
		int oldSize = dk.taskList.size();
		for (int i = 0; i < 5; i++) {
			int old = dk.taskList.size();
			dk.removeTask(dk.taskList.get(i));
			assertEquals(old-1, dk.taskList.size());
		}
		assertEquals(oldSize - 5, dk.taskList.size());
	}

	@Test
	public void testUndo() {
		fail("Not yet implemented"); // TODO
	}

}