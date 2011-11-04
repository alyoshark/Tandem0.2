package w10j1.tandem.storage.task;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import w10j1.tandem.storage.task.api.Task;

/**
 *
 * @author Chris
 */
public class TaskImpl implements Task {

    private static final boolean UNDONE = false;
    private static final boolean DONE = true;
    
    private Calendar due = Calendar.getInstance();

    private String desc = "";
    private boolean status = UNDONE;
    private Priority priority;
    private PropertyChangeSupport pcs;
    
    private SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
    
    public TaskImpl() {
        this(null, "", UNDONE, Priority.LOW);
    }
    
    public TaskImpl(Calendar due, String desc) {
        this.due = due;
        this.desc = desc;
        this.pcs = new PropertyChangeSupport(this);
    }
    
    public TaskImpl(Calendar due, String desc, boolean status, Priority prio) {
        this.due = due;
        this.desc = desc;
        this.status = status;
        this.priority = prio;
        this.pcs = new PropertyChangeSupport(this);
    }

    @Override
    public String getDesc() {
        return this.desc;
    }

    @Override
    public Calendar getDue() {
        assert(this.due != null);
        return this.due;
    }

    @Override
    public boolean getStatus() {
        return this.status;
    }

    @Override
    public Priority getPriority() {
        return this.priority;
    }

    @Override
    public void setStatus(boolean status) {
        this.status = status;
    }
    
    @Override
    public void setDue(Calendar due) {
        Calendar old = this.due;
        this.due = due;
        this.pcs.firePropertyChange(PROP_DUE, old, due);
    }
    
    @Override
    public void setDesc(String desc) {
        String old = this.desc;
        this.desc = desc;
        this.pcs.firePropertyChange(PROP_DESC, old, desc);
    }

    @Override
    public void setPriority(Priority priority) {
        Priority old = this.priority;
        this.priority = priority;
        this.pcs.firePropertyChange(PROP_PRIORITY, old, priority);
    }

    @Override
    public String toString() {
        return formatter.format(this.getDue().getTime()) + "|" + this.getDesc() /* + "|" + this.getPriority() +
                "|" + (this.getStatus() ? "Done" : "Undone") */ + "\r\n";
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        this.pcs.addPropertyChangeListener(listener);
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        this.pcs.removePropertyChangeListener(listener);
    }
}