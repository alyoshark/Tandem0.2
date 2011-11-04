package w10j1.tandem.tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import w10j1.tandem.logic.commandprocessor.Editor;
import w10j1.tandem.logic.commandprocessor.Editor.Attributes;

public class EditorTest {

	private Editor ed = new Editor();

	@Before
	public void setUp() throws Exception {
		ed.edit("e 3 time 2111 2359", null);
	}

	@Test
	public void testEdit() {
		// Nothing yet
	}

	@Test
	public void testGetIndex() {
		assertEquals(3, ed.getIndex());
	}

	@Test
	public void testGetAttr() {
		assertEquals(Attributes.DUE, ed.getAttr());
	}

	@Test
	public void testGetContent() {
		assertEquals(ed.getContent().compareTo("2111 2359"), 0);
	}
}