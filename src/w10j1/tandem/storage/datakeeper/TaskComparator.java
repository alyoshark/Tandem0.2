package w10j1.tandem.storage.datakeeper;

import java.util.Comparator;

import w10j1.tandem.storage.task.api.Task;

/**
 *
 * @author Chenhong
 */
enum TaskComparator implements Comparator<Task> {

    DUE_SORT {

        @Override
        public int compare(Task o1, Task o2) {
            return (int) (o1.getDue().compareTo(o2.getDue()));
        }
    },
    PRIORITY_SORT {

        @Override
        public int compare(Task o1, Task o2) {
            return (o1.getPriority().getVal() - o2.getPriority().getVal());
        }
    };

    public static Comparator<Task> ascending(final Comparator<Task> other) {
        return new Comparator<Task>() {

            @Override
            public int compare(Task o1, Task o2) {
                return other.compare(o1, o2);
            }
        };
    }

    public static Comparator<Task> decending(final Comparator<Task> other) {
        return new Comparator<Task>() {

            @Override
            public int compare(Task o1, Task o2) {
                return -1 * other.compare(o1, o2);
            }
        };
    }

    public static Comparator<Task> getComparator(final TaskComparator... multipleOptions) {
        return new Comparator<Task>() {

            @Override
            public int compare(Task o1, Task o2) {
                for (TaskComparator option : multipleOptions) {
                    int result = option.compare(o1, o2);
                    if (result != 0) {
                        return result;
                    }
                }
                return 0;
            }
        };
    }
}