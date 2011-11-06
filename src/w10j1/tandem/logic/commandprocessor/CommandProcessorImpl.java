package w10j1.tandem.logic.commandprocessor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import w10j1.tandem.logger.Log;
import w10j1.tandem.logic.commandprocessor.api.CommandProcessor;
import w10j1.tandem.storage.datakeeper.DataKeeperImpl;
import w10j1.tandem.storage.datakeeper.api.DataKeeper;
import w10j1.tandem.storage.task.api.Task;
import w10j1.tandem.util.fileoprator.FileOperator;
import w10j1.tandem.util.fileoprator.FileOperatorAPI;

import com.mdimension.jchronic.Chronic;
import com.mdimension.jchronic.Options;
import com.mdimension.jchronic.utils.Span;

/**
 * 
 * @author Chris
 */
public class CommandProcessorImpl implements CommandProcessor {

	private FileOperatorAPI fo = new FileOperator();
	private DataKeeper dk = new DataKeeperImpl();
	private Log log = Log.getLogger();

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
	public void back() {
		getDataKeeper().initDataKeeper();
	}

	@Override
	public void search(String command) throws Exception {
		if (command == null || command.isEmpty()) {
			getDataKeeper().initDataKeeper();
			return;
		}
		try {
			Span sp = createSearchSpan(command);
			getDataKeeper().searchTask(sp);
		} catch (ParseException e) {
			nonFormattedSearch(command);
		} catch (Exception e1) {
			log.getMyLogger().error("error", e1);
			throw e1;
		}
	}

	@Override
	public String getSearchResult() {
		return getDataKeeper().resultString();
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

	private void nonFormattedSearch(String command) throws Exception {
		try {
			Span interval = Chronic.parse(command, new Options(false));
			getDataKeeper().searchTask(interval);
		} catch (Exception e0) {
			try {
				getDataKeeper().searchTask(command);
			} catch (Exception e1) {
				throw e0;
			}
		}
	}

	@Override
	public void edit(String command) throws NumberFormatException,
			ParseException {
		Editor ed = new Editor();
		ed.edit(command, getDataKeeper());
		getFileOperator().writeFile(getDataKeeper().memToFile());
	}

	@Override
	public void remove(String command) {
		getDataKeeper().removeTask(
				getDataKeeper().getTaskList().get(
						getDataKeeper().getSearchList().get(
								Integer.parseInt(command) - 1)));
		getFileOperator().writeFile(getDataKeeper().memToFile());
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