package w10j1.tandem.storage.datakeeper.api;

import java.util.ArrayList;

import com.mdimension.jchronic.utils.Span;

import w10j1.tandem.storage.task.api.Task;

/**
 *
 * @author Chris
 */
public interface DataKeeper {

    public String ascDue();

    public String decDue();

    public String ascPriority();

    public String decPriority();

    /**
	 * Return the record in memory into a string writable to data file.
	 * 
	 * @return a string representation of taskList
	 */
    public String memToFile();

    /**
	 * Reset DataKeeper by storing searchList top pending tasks
	 */
	public void initDataKeeper();

	/**
	 * Parse the string of file into memory, storing in taskList
	 */
	public void fileToMem(String fromFile);
    
	/**
	 * Return the search result in memory into a string writable onto both
	 * console and data file
	 * 
	 * @return a string representation of searchList
	 */
    public String resultString();
    
    /**
	 * Add a task into DataKeeper
	 */
    public void addTask(Task task);
    
    /**
	 * Search for tasks with specified keywords in the description
	 * 
	 * @param keywords
	 *            keywords to be searched for
	 */
    public void searchTask(String keywords);
    
    /**
	 * Search for tasks due within the specified interval
	 * 
	 * @param interval
	 *            period return tasks will be due within
	 */
    public void searchTask(Span interval);
    
    /**
	 * Remove a specified task
	 */
    public void removeTask(Task task);
    
    /**
	 * Undo a removal by mistake
	 */
    public boolean undo();

    /**
	 * Get the taskList
	 */
	public ArrayList<Task> getTaskList();

	/**
	 * Get the searchList
	 */
	public ArrayList<Integer> getSearchList();
}