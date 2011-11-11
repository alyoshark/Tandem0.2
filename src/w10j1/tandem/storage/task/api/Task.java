package w10j1.tandem.storage.task.api;

import java.beans.PropertyChangeListener;
import java.util.Calendar;

/**
 * 
 * @author Chenhong
 */
public interface Task {

    public enum Priority {

        LOW(0), MEDIUM(1), HIGH(2);
        private final int val;

        Priority(int arg) {
            val = arg;
        }

        public int getVal() {
            return val;
        }
    };

    public String getDesc();

    public Calendar getDue();

    public boolean getStatus();

    public Priority getPriority();

    public void setDue(Calendar due);
    
    public void setDesc(String content);

    public void setStatus(boolean status);

    public void setPriority(Priority priority);

    @Override
    public String toString();

    public void addPropertyChangeListener(PropertyChangeListener listener);

    public void removePropertyChangeListener(
            PropertyChangeListener listener);
    
    public static final String PROP_DUE = "due";
    public static final String PROP_DESC = "desc";
    public static final String PROP_PRIORITY = "priority";
    public static final String PROP_STATUS = "status";
    public static final String PROP_DESCRIPTION = "description";
}