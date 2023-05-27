package app;

import backend.DesktopNatives;
import backend.NativeInterface;
import backend.asset.FileDrop;
import backend.WindowWorker;
import backend.settings.Settings;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.*;

import java.util.Arrays;

public class Launcher  {

    private static NativeInterface nativeInterface;

    public static void main(String[] args) {
        App.init();
        createApplication();
    }


    private static Lwjgl3Application createApplication() {
        nativeInterface = new NativeInterface();
        Lwjgl3ApplicationConfiguration configuration = getDefaultConfiguration();
        configuration.setWindowListener(nativeInterface);

        return new Lwjgl3Application(new Editor(nativeInterface), configuration);
    }

    private static Lwjgl3ApplicationConfiguration getDefaultConfiguration() {
        Lwjgl3ApplicationConfiguration configuration = new Lwjgl3ApplicationConfiguration();
        configuration.setOpenGLEmulation(Lwjgl3ApplicationConfiguration.GLEmulation.GL30, 3, 0);
        configuration.setTitle(Settings.Window.TITLE);
        configuration.setWindowIcon(Settings.FilePaths.ICON);
        configuration.setWindowedMode(Settings.Window.WIDTH, Settings.Window.HEIGHT);
        configuration.setBackBufferConfig(1, 1, 1, 1, Settings.Graphics.DEPTH, Settings.Graphics.STENCIL, Settings.Graphics.ANTIALIASING);
        configuration.useVsync(Settings.Graphics.VSYNC);
        configuration.setDecorated(false);
        configuration.setResizable(true);
        configuration.setForegroundFPS(0);


        return configuration;
    }

}

