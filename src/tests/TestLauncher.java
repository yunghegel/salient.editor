package tests;

import app.Editor;
import backend.NativeInterface;
import backend.settings.Settings;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

public class TestLauncher {
    private static Lwjgl3Application createApplication() {
        return new Lwjgl3Application(new RotateGizmoMeshTest(),getDefaultConfiguration());
    }

    private static Lwjgl3ApplicationConfiguration getDefaultConfiguration() {
        Lwjgl3ApplicationConfiguration configuration = new Lwjgl3ApplicationConfiguration();
        configuration.setOpenGLEmulation(Lwjgl3ApplicationConfiguration.GLEmulation.GL30, 3, 0);
        configuration.setTitle(Settings.Window.TITLE);
        configuration.setWindowIcon(Settings.FilePaths.ICON);
        configuration.setWindowedMode(Settings.Window.WIDTH, Settings.Window.HEIGHT);
        configuration.setBackBufferConfig(1, 1, 1, 1, Settings.Graphics.DEPTH, Settings.Graphics.STENCIL, Settings.Graphics.ANTIALIASING);
        configuration.useVsync(Settings.Graphics.VSYNC);
        configuration.setDecorated(true);
        configuration.setResizable(true);
        configuration.setForegroundFPS(0);
        configuration.enableGLDebugOutput(true, System.out);

        return configuration;
    }

    public static void main(String[] args) {
        createApplication();
    }
}
