package ca.courseplanner.model;

/**
 * WatcherContents has the information for which to make a watcher for.
 */
public class WatcherContents {
    private long deptId;
    private long courseId;

    public WatcherContents() {
    }


    public long getDeptId() {
        return deptId;
    }

    public long getCourseId() {
        return courseId;
    }
}
