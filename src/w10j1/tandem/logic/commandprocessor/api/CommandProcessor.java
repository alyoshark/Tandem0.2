package w10j1.tandem.logic.commandprocessor.api;

import java.util.ArrayList;

import w10j1.tandem.storage.datakeeper.api.DataKeeper;
import w10j1.tandem.storage.task.api.Task;

/**
 *
 * @author Chris
 */
public interface CommandProcessor {

    public void add(Task task);

    public String search(String command);

    public void edit(String command);

    public void remove(String command);

    public void setDone(String command);

    public void undo();
    
    public DataKeeper getDataKeeper();
    
}
