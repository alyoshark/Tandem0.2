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

    public FileOperator fo = new FileOperator();
    public DataKeeper dk = new DataKeeperImpl();

    public CommandProcessorImpl() {
        this.fo.createFile();
        String dataFromFile = this.fo.readFile();
        if (dataFromFile.isEmpty()) {
            return;
        }
        this.dk.fileToMem(fo.readFile());
    }

    @Override
    public void add(Task task) {
        this.dk.addTask(task);
        String updateList = dk.memToFile();
        this.fo.writeFile(updateList);
    }

    @Override
    public String search(String command) {
        try {
            Span interval = Chronic.parse(command);
            dk.searchTask(interval);
            return dk.resultString();
        } catch (Exception e0) {
            try {
                dk.searchTask(command);
                return dk.resultString();
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
        this.dk.removeTask(((DataKeeperImpl) this.dk).searchList.get(Integer.parseInt(command)));
    }

    @Override
    public void setDone(String command) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void undo() {
        dk.undo();
        fo.writeFile(dk.memToFile());
    }

    @Override
    public DataKeeper getDataKeeper() {
        return this.dk;
    }
}