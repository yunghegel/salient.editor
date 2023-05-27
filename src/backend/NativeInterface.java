package backend;

import backend.asset.FileDrop;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Graphics;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Window;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3WindowListener;

import java.util.Arrays;

public class NativeInterface implements Lwjgl3WindowListener,WindowWorker {

    private WindowState windowState;
    private WindowState previousState;
    public FileDrop fileDrop;

    public NativeInterface() {
        fileDrop = new FileDrop();
    }

    @Override
    public void created(Lwjgl3Window window) {

    }

    @Override
    public void iconified(boolean isIconified) {
        if (isIconified) {
            previousState = windowState;
            windowState = WindowState.ICONIFIED;
        } else {
            if (previousState == WindowState.RESTORED) {
                previousState = windowState;
                windowState = WindowState.RESTORED;
            } else if (previousState == WindowState.MAXIMIZED) {
                previousState = windowState;
                windowState = WindowState.MAXIMIZED;
            }
        }
    }

    @Override
    public void maximized(boolean isMaximized) {
        if (isMaximized) {
            previousState = windowState;
            windowState = WindowState.MAXIMIZED;
        } else {
            previousState = windowState;
            windowState = WindowState.RESTORED;
        }
    }

    @Override
    public void focusLost() {
    }

    @Override
    public void focusGained() {
    }

    @Override
    public boolean closeRequested() {
        return true;
    }

    @Override
    public void filesDropped(String[] files) {
        Arrays.stream(files).forEach(System.out::println);
    }

    @Override
    public void refreshRequested() {
    }

    @Override
    public void minimize() {
        Lwjgl3Graphics g = (Lwjgl3Graphics) Gdx.graphics;
        g.getWindow().iconifyWindow();
    }

    @Override
    public void maximize() {
        Lwjgl3Graphics g = (Lwjgl3Graphics) Gdx.graphics;
        g.getWindow().maximizeWindow();
    }

    @Override
    public void restore() {
        Lwjgl3Graphics g = (Lwjgl3Graphics) Gdx.graphics;
        g.getWindow().restoreWindow();
    }

    @Override
    public WindowState getWindowState() {
        return windowState;
    }

    @Override
    public void setPosition(int x, int y) {
        Lwjgl3Graphics g = (Lwjgl3Graphics) Gdx.graphics;
        g.getWindow().setPosition(x, y);
    }

    @Override
    public void center() {
        Lwjgl3Graphics g = (Lwjgl3Graphics) Gdx.graphics;
        int diffWidth = g.getDisplayMode().width - g.getWidth();
        int diffHeight = g.getDisplayMode().height - g.getHeight();
        g.getWindow().setPosition(diffWidth / 2, diffHeight / 2);
    }

    @Override
    public int getPositionX() {
        Lwjgl3Graphics g = (Lwjgl3Graphics) Gdx.graphics;
        return g.getWindow().getPositionX();
    }

    @Override
    public int getPositionY() {
        Lwjgl3Graphics g = (Lwjgl3Graphics) Gdx.graphics;
        return g.getWindow().getPositionY();
    }
}
