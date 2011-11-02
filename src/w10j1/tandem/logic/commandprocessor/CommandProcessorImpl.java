package w10j1.tandem.logic.commandprocessor;

import com.mdimension.jchronic.Chronic;
import com.mdimension.jchronic.utils.Span;

import w10j1.tandem.logic.commandprocessor.api.CommandProcessor;
import w10j1.tandem.storage.datakeeper.DataKeeperImpl;
import w10j1.tandem.storage.datakeeper.api.DataKeeper;
import w10j1.tandem.storage.task.api.Task;
import w10j1.tandem.util.fileoprator.FileOperator;

/**
 * 
 * @author Chris
 */
public class CommandProcessorImpl implements CommandProcessor {

	private FileOperator fo = new FileOperator();
	private DataKeeper dk = new DataKeeperImpl();

	public CommandProcessorImpl() {
		this.getFileOperator().createFile();
		String dataFromFile = this.getFileOperator().readFile();
		if (dataFromFile.isEmpty()) {
			return;
		}
		this.getDataKeeper().fileToMem(getFileOperator().readFile());
	}

	@Override
	public void add(Task task) {
		this.getDataKeeper().addTask(task);
		String updateList = getDataKeeper().memToFile();
		this.getFileOperator().writeFile(updateList);
	}

	@Override
	public String search(String command) {
		try {
			Span interval = Chronic.parse(command);
			getDataKeeper().searchTask(interval);
			return getDataKeeper().resultString();
		} catch (Exception e0) {
			try {
				getDataKeeper().searchTask(command);
				return getDataKeeper().resultString();
			} catch (Exception e1) {
				throw e0;
			}
		}
	}

	@Override
	public void edit(String command) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void remove(String command) {
		this.getDataKeeper().removeTask(
				((DataKeeperImpl) this.getDataKeeper()).getSearchList().get(
						Integer.parseInt(command)));
	}

	@Override
	public void setDone(String command) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void undo() {
		if (getDataKeeper().undo())
			getFileOperator().writeFile(getDataKeeper().memToFile());
	}

	@Override
	public DataKeeper getDataKeeper() {
		return dk;
	}

	@Override
	public FileOperator getFileOperator() {
		return fo;
	}
}