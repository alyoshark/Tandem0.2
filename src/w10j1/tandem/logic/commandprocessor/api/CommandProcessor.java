package w10j1.tandem.logic.commandprocessor.api;

import java.text.ParseException;

import w10j1.tandem.storage.datakeeper.api.DataKeeper;
import w10j1.tandem.storage.task.api.Task;
import w10j1.tandem.util.fileoprator.FileOperatorAPI;

/**
 *
 * @author Chris
 */
public interface CommandProcessor {

	/**
	 * Adding a task into data
	 * 
	 * @param task
	 *            the instance of Task to be added
	 */
    public void add(Task task);
    
    /**
	 * Getting back from a search operation to top pending tasks
	 */
	public void back();

	/**
	 * Searching in DataKeeper by time or keywords in task description
	 * 
	 * @param command
	 *            a String to be searched against
	 * @throws Exception
	 *             various exceptions can be thrown for this method
	 */
    public void search(String command) throws Exception;

    /**
	 * Edit a task by specifying its index from the searchList and the attribute
	 * to edit on
	 * 
	 * @param command
	 *            an instance of String in a form of <index> <attribute name>
	 *            <content>
	 */
    public void edit(String command) throws NumberFormatException, ParseException;

    /**
	 * Remove a task by specifying its index from the searchList
	 * 
	 * @param command
	 *            an instance of String that can be parsed into integer
	 */
    public void remove(String command);

    public void setDone(String command);

    /**
	 * Undo last faulty removal
	 */
    public void undo();
    
    /**
	 * Getting the DataKeeper instance storing in it
	 * 
	 * @return dk the DataKeeper instance storing as a field in it
	 */
    public DataKeeper getDataKeeper();

    /**
	 * Getting the FileOperator instance storing in it
	 * 
	 * @return fo the FileOperator instance storing as a field in it
	 */
	public FileOperatorAPI getFileOperator();

	/**
	 * Getting the search result in a string form
	 * 
	 * @return search result outputable in console
	 */
	public String getSearchResult();
}
