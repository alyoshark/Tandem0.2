package w10j1.tandem.tests;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import w10j1.tandem.logic.commandprocessor.CommandProcessorImpl;
import w10j1.tandem.logic.commandprocessor.api.CommandProcessor;
import w10j1.tandem.storage.datakeeper.DataKeeperImpl;
import w10j1.tandem.storage.task.TaskImpl;

public class CommandProcessorTest {

	private CommandProcessor cp = new CommandProcessorImpl();

	@Before
	public void setUp() throws Exception {
		for (int i = 0; i < 10; i++)
			cp.add(new TaskImpl());
	}

	@Test
	public void testCommandProcessorImpl() {
		CommandProcessor cp = new CommandProcessorImpl();
		assertNotNull(cp);
	}

	@Test
	public void testAdd() {
		int oldSize = cp.getDataKeeper().getTaskList().size();
		cp.add(new TaskImpl());
		assertEquals(oldSize + 1, cp.getDataKeeper().getTaskList().size());
	}

	@Test
	public void testRemove() {
		int oldSize = cp.getDataKeeper().getTaskList().size();
		cp.remove("1");
		assertEquals(oldSize - 1, cp.getDataKeeper().getTaskList().size());
	}

	@Test
	public void testUndo() {
		int oldSize = ((DataKeeperImpl) cp.getDataKeeper()).getTaskList()
				.size();
		cp.add(new TaskImpl());
		assertEquals(oldSize + 1, cp.getDataKeeper().getTaskList().size());
		cp.undo();
		assertEquals(oldSize, cp.getDataKeeper().getTaskList().size());
		cp.remove("1");
		assertEquals(oldSize - 1, cp.getDataKeeper().getTaskList().size());
		cp.undo();
		assertEquals(oldSize, cp.getDataKeeper().getTaskList().size());
	}

	@Test
	public void testGetDataKeeper() {
		assertNotNull(cp.getDataKeeper());
	}

}
