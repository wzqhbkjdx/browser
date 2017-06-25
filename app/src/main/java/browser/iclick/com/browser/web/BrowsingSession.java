package browser.iclick.com.browser.web;

/**
 * Created by bym on 2017/6/24.
 */

public class BrowsingSession {

    private static BrowsingSession instance;

    private boolean isActive;

    public static synchronized BrowsingSession getInstance() {
        if(instance == null) {
            instance = new BrowsingSession();
        }
        return instance;
    }

    public boolean isActive() {
        return isActive;
    }


}
