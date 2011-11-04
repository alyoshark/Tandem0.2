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

    public void add(Task task);

    public String search(String command) throws Exception;

    public void edit(String command) throws NumberFormatException, ParseException;

    public void remove(String command);

    public void setDone(String command);

    public void undo();
    
    public DataKeeper getDataKeeper();

	public FileOperatorAPI getFileOperator();
    
}
