package app;

import java.io.File;

public class App {

    private static boolean initialized;

    public static final String USER_HOME_PATH = System.getProperty("user.home") + File.separator;

    public static final String APP_FOLDER_PATH = USER_HOME_PATH + ".salient" + File.separator;

    public static final String PROJECTS_FOLDER_PATH = APP_FOLDER_PATH + "projects" + File.separator;

    public static final String DEFAULT_PROJECT_PATH = PROJECTS_FOLDER_PATH + "default" + File.separator;

    /**
     * Called when the application is first created.
     */
    public static void init() {
        if (initialized) throw new IllegalStateException("App cannot be initialized twice!");
        new File(APP_FOLDER_PATH).mkdir();
        new File(PROJECTS_FOLDER_PATH).mkdir();
        new File(DEFAULT_PROJECT_PATH).mkdir();
        new File(DEFAULT_PROJECT_PATH + "assets").mkdir();
        new File(DEFAULT_PROJECT_PATH + "assets" + File.separator + "textures").mkdir();
        new File(DEFAULT_PROJECT_PATH + "assets" + File.separator + "models").mkdir();



    }

    private static void initProjectFolder() {

    }

}
