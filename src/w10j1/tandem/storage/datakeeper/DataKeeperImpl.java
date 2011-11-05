/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package w10j1.tandem.storage.datakeeper;

import com.mdimension.jchronic.utils.Span;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import static w10j1.tandem.storage.datakeeper.TaskComparator.*;
import w10j1.tandem.logger.Log;
import w10j1.tandem.storage.datakeeper.api.DataKeeper;
import w10j1.tandem.storage.task.TaskImpl;
import w10j1.tandem.storage.task.api.Task;

/**
 * 
 * @author Chris
 */
public class DataKeeperImpl implements DataKeeper {

	private ArrayList<Task> taskList = new ArrayList<Task>();
	private ArrayList<Integer> searchList = new ArrayList<Integer>();
	private Task tempTask;
	private Log log = Log.getLogger();

	private enum undoState {
		NULL, ADD, DEL
	};

	private undoState rollBack = undoState.NULL;
	private SimpleDateFormat formatter = new SimpleDateFormat(
			"dd-MM-yyyy HH:mm");

	public DataKeeperImpl() {
		taskList = new ArrayList<Task>();
	}

	@Override
	public String ascDue() {
		Collections.sort(taskList, ascending(DUE_SORT));
		return resultString();
	}

	@Override
	public String decDue() {
		Collections.sort(taskList, decending(DUE_SORT));
		return resultString();
	}

	@Override
	public String ascPriority() {
		Collections.sort(taskList, ascending(PRIORITY_SORT));
		return resultString();
	}

	@Override
	public String decPriority() {
		Collections.sort(taskList, decending(PRIORITY_SORT));
		return resultString();
	}

	@Override
	public String memToFile() {
		ascDue();
		StringBuilder sb = new StringBuilder();
		for (Task t : getTaskList()) {
			sb.append(t.toString());
		}
		return sb.toString();
	}

	@Override
	public void initDataKeeper() {
		ascDue();
		int len = Math.min(taskList.size(), 10);
		for (int i = 0; i < len; i++)
			searchList.add(taskList.indexOf(taskList.get(i)));
	}
	
	@Override
	public void fileToMem(String fromFile) {
		String[] tempList = fromFile.split("\r\n");
		for (String task : tempList) {
			// Getting due
			String[] taskDetail = task.split("\\|");
			Calendar time = Calendar.getInstance();
			try {
				time.setTime(formatter.parse(taskDetail[0]));
			} catch (ParseException e) {
				log.getMyLogger().error("error", e);
			}
			getTaskList().add(new TaskImpl(time, taskDetail[1]));
		}
	}

	@Override
	public String resultString() {
		assert (searchList != null);
		assert (searchList.size() >= 0);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < searchList.size(); i++) {
			sb.append(i+1).append(". ").append(taskList.get(searchList.get(i)));
		}
		return sb.toString();
	}

	@Override
	public void addTask(Task task) {
		tempTask = task;
		getTaskList().add(task);
		rollBack = undoState.ADD;
	}

	@Override
	public void searchTask(String keywords) {
		String[] kw = keywords.split("\\s");
		searchList.clear();
		for (Task task : getTaskList()) {
			boolean hasAllWords = true;
			for (String word : kw) {
				if (!task.getDesc().contains(word)) {
					hasAllWords = false;
					break;
				}
			}
			if (hasAllWords) {
				searchList.add(taskList.indexOf(task));
			}
		}
	}

	@Override
	public void searchTask(Span interval) {
		searchList.clear();
		for (Task task : getTaskList()) {
			if (task.getDue().compareTo(interval.getBeginCalendar()) >= 0
					&& task.getDue().compareTo(interval.getEndCalendar()) <= 0) {
				searchList.add(taskList.indexOf(task));
			}
		}
	}

	@Override
	public void removeTask(Task task) {
		tempTask = task;
		getTaskList().remove(task);
		rollBack = undoState.DEL;
	}

	@Override
	public boolean undo() {
		switch (rollBack) {
		case NULL:
			return false;
		case ADD:
			removeTask(tempTask);
			rollBack = undoState.NULL;
		case DEL:
			addTask(tempTask);
			rollBack = undoState.NULL;
		}
		assert (rollBack == undoState.NULL);
		return true;
	}

	@Override
	public ArrayList<Task> getTaskList() {
		return taskList;
	}

	@Override
	public ArrayList<Integer> getSearchList() {
		return searchList;
	}

	public undoState getRollBack() {
		return rollBack;
	}

	public Task getTempTask() {
		return tempTask;
	}
}