package tests;

import backend.settings.Settings;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

public class TestLauncher {
    public static void main(String[] args) {

        createApplication().setLogLevel(Application.LOG_DEBUG);
    }


    private static Lwjgl3Application createApplication() {

        Lwjgl3ApplicationConfiguration configuration = getDefaultConfiguration();


        return new Lwjgl3Application(new Tests(), configuration);
    }

    private static Lwjgl3ApplicationConfiguration getDefaultConfiguration() {
        Lwjgl3ApplicationConfiguration configuration = new Lwjgl3ApplicationConfiguration();
        configuration.setOpenGLEmulation(Lwjgl3ApplicationConfiguration.GLEmulation.GL30, 3, 0);
        configuration.setTitle(Settings.Window.TITLE);
        configuration.setWindowIcon(Settings.FilePaths.ICON);
        configuration.setWindowedMode(Settings.Window.WIDTH, Settings.Window.HEIGHT);
        configuration.setBackBufferConfig(1, 1, 1, 1, Settings.Graphics.DEPTH, Settings.Graphics.STENCIL, Settings.Graphics.ANTIALIASING);
        configuration.useVsync(Settings.Graphics.VSYNC);
        configuration.setResizable(true);
        configuration.setForegroundFPS(0);


        return configuration;
    }
}
