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
		assertNotNull(dk.getTaskList());
	}
	
	@Test
	public void testAddTask() {
		int oldSize = dk.getTaskList().size();
		Calendar time = Calendar.getInstance();
		dk.addTask(new TaskImpl(time, "dummy msg"));
		assertEquals(oldSize+1, dk.getTaskList().size());
		for (int i = 0; i < 5; i++) {
			int old = dk.getTaskList().size();
			dk.addTask(new TaskImpl(Calendar.getInstance(), "dummy msg"));
			assertEquals(old+1, dk.getTaskList().size());
		}
		assertEquals(oldSize + 6, dk.getTaskList().size());
	}

	@Test
	public void testAscDue() {
		dk.ascDue();
		assertTrue(dk.getTaskList().get(0).getDue().compareTo(dk.getTaskList().get(1).getDue()) > 0);
	}

	@Test
	public void testDecDue() {
		assertEquals(3, dk.getTaskList().size());
		dk.decDue();
		assertTrue(dk.getTaskList().get(0).getDue().compareTo(dk.getTaskList().get(1).getDue()) > 0);
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
		int oldSize = dk.getTaskList().size();
		for (int i = 0; i < 5; i++) {
			int old = dk.getTaskList().size();
			dk.removeTask(dk.getTaskList().get(i));
			assertEquals(old-1, dk.getTaskList().size());
		}
		assertEquals(oldSize - 5, dk.getTaskList().size());
	}

	@Test
	public void testUndo() {
		int oldSize = dk.getTaskList().size();
		dk.removeTask(dk.getTaskList().get(0));
		assertEquals(oldSize-1, dk.getTaskList().size());
		dk.undo();
		assertEquals(oldSize, dk.getTaskList().size());
		
		Calendar time = Calendar.getInstance();
		dk.addTask(new TaskImpl(time, "dummy msg"));
		assertEquals(oldSize+1, dk.getTaskList().size());
		dk.undo();
		assertEquals(oldSize, dk.getTaskList().size());
	}
}