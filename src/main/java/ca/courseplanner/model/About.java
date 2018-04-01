package ca.courseplanner.model;

/**
 * About implementation that describes what the application is about.
 */
public class About {
    private String appName;
    private String authorName;

    public About(String appName, String authorName){
        this.appName = appName;
        this.authorName = authorName;
    }

    public String getAppName() {
        return appName;
    }

    public String getAuthorName() {
        return authorName;
    }


}
