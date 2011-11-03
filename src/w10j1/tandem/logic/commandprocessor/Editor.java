package w10j1.tandem.logic.commandprocessor;

import w10j1.tandem.storage.datakeeper.api.DataKeeper;

public class Editor {

	public enum Attributes {
		DUE, DESC
	};

	private int index;
	private Attributes attr;
	private String content;

	public void edit(String command, DataKeeper dk) {
		parseAttribute(command);
		parseIndex(command);
		switch (getAttr()) {
		case DUE:
			break;
		case DESC:
			break;
		}
		dk.getSearchList().get(getIndex());
	}

	private void parseAttribute(String command) {
		// TODO Auto-generated method stub
	}

	private void parseIndex(String command) {
		// TODO Auto-generated method stub
	}

	public int getIndex() {
		return index;
	}

	public Attributes getAttr() {
		return attr;
	}

	public String getContent() {
		return content;
	}
}