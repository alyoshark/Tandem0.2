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
 * @author Chenhong
 */
public class CommandProcessorImpl implements CommandProcessor {

	private FileOperatorAPI fo = new FileOperator();
	private DataKeeper dk = new DataKeeperImpl();
	private Log log = Log.getLogger();

	/**
	 * Creating a DataKeeper and a FileOperator and initialize by reading from
	 * the data file
	 */
	public CommandProcessorImpl() {
		getFileOperator().createFile();
		String dataFromFile = getFileOperator().readFile();
		if (dataFromFile.isEmpty()) {
			return;
		}
		getDataKeeper().fileToMem(getFileOperator().readFile());
	}

	/**
	 * Adding a task into data
	 * 
	 * @param task
	 *            the instance of Task to be added
	 */
	@Override
	public void add(Task task) {
		getDataKeeper().addTask(task);
		String updateList = getDataKeeper().memToFile();
		getFileOperator().writeFile(updateList);
	}

	/**
	 * Getting back from a search operation to top pending tasks
	 */
	@Override
	public void back() {
		getDataKeeper().initDataKeeper();
	}

	/**
	 * Searching in DataKeeper by time or keywords in task description
	 * 
	 * @param command
	 *            a String to be searched against
	 * @throws Exception
	 *             various exceptions can be thrown for this method
	 */
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

	/**
	 * Getting the search result in a string form
	 * 
	 * @return search result outputable in console
	 */
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

	/**
	 * Edit a task by specifying its index from the searchList and the attribute
	 * to edit on
	 * 
	 * @param command
	 *            an instance of String in a form of <index> <attribute name>
	 *            <content>
	 */
	@Override
	public void edit(String command) throws NumberFormatException,
			ParseException {
		Editor ed = new Editor();
		ed.edit(command, getDataKeeper());
		getFileOperator().writeFile(getDataKeeper().memToFile());
	}

	/**
	 * Remove a task by specifying its index from the searchList
	 * 
	 * @param command
	 *            an instance of String that can be parsed into integer
	 */
	@Override
	public void remove(String command) throws NumberFormatException {
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

	/**
	 * Undo last faulty removal
	 */
	@Override
	public void undo() {
		if (getDataKeeper().undo())
			getFileOperator().writeFile(getDataKeeper().memToFile());
	}

	/**
	 * Getting the DataKeeper instance storing in it
	 * 
	 * @return dk the DataKeeper instance storing as a field in it
	 */
	@Override
	public DataKeeper getDataKeeper() {
		return dk;
	}

	/**
	 * Getting the FileOperator instance storing in it
	 * 
	 * @return fo the FileOperator instance storing as a field in it
	 */
	@Override
	public FileOperatorAPI getFileOperator() {
		return fo;
	}
}