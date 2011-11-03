package w10j1.tandem.logic.commandprocessor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import com.mdimension.jchronic.Chronic;
import com.mdimension.jchronic.utils.Span;

import w10j1.tandem.logic.commandprocessor.api.CommandProcessor;
import w10j1.tandem.storage.datakeeper.DataKeeperImpl;
import w10j1.tandem.storage.datakeeper.api.DataKeeper;
import w10j1.tandem.storage.task.api.Task;
import w10j1.tandem.util.fileoprator.FileOperator;
import w10j1.tandem.util.fileoprator.FileOperatorAPI;

/**
 * 
 * @author Chris
 */
public class CommandProcessorImpl implements CommandProcessor {

	private FileOperatorAPI fo = new FileOperator();
	private DataKeeper dk = new DataKeeperImpl();

	public CommandProcessorImpl() {
		getFileOperator().createFile();
		String dataFromFile = getFileOperator().readFile();
		if (dataFromFile.isEmpty()) {
			return;
		}
		getDataKeeper().fileToMem(getFileOperator().readFile());
	}

	@Override
	public void add(Task task) {
		getDataKeeper().addTask(task);
		String updateList = getDataKeeper().memToFile();
		getFileOperator().writeFile(updateList);
	}

	@Override
	public String search(String command) throws Exception {
		try {
			Span sp = createSearchSpan(command);
			getDataKeeper().searchTask(sp);
			return getDataKeeper().resultString();
		} catch (ParseException e) {
			return nonFormattedSearch(command);
		} catch (Exception e1) {
			throw e1;
		}
	}

	private Span createSearchSpan(String command) throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyy");
		Calendar start = Calendar.getInstance(); 
		start.setTime(formatter.parse(command));
		start.set(Calendar.HOUR_OF_DAY, 0);
		start.set(Calendar.MINUTE, 0);
		Calendar end = Calendar.getInstance();
		end.setTime(start.getTime());
		end.add(Calendar.DATE, 1);
		Span sp = new Span(start, end);
		return sp;
	}

	private String nonFormattedSearch(String command) throws Exception {
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
		Editor ed = new Editor();
		ed.edit(command, getDataKeeper());
	}

	@Override
	public void remove(String command) {
		getDataKeeper().removeTask(
				((DataKeeperImpl) getDataKeeper()).getSearchList().get(
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
	public FileOperatorAPI getFileOperator() {
		return fo;
	}
}