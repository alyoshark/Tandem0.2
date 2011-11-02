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
		int oldSize = ((DataKeeperImpl) cp.getDataKeeper()).taskList.size();
		cp.add(new TaskImpl());
		assertEquals(oldSize + 1,
				((DataKeeperImpl) cp.getDataKeeper()).taskList.size());
	}

	@Test
	public void testSearch() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testEdit() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testRemove() {
		int oldSize = ((DataKeeperImpl) cp.getDataKeeper()).taskList.size();
		cp.remove("1");
		assertEquals(oldSize - 1,
				((DataKeeperImpl) cp.getDataKeeper()).taskList.size());
	}

	@Test
	public void testSetDone() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testUndo() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testGetDataKeeper() {
		fail("Not yet implemented"); // TODO
	}

}
