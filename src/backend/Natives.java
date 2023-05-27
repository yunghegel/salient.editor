package backend;

public interface Natives
{
    public static enum WindowState {
        ICONIFIED, RESTORED, MAXIMIZED
    }

    void setPosition(int x, int y);

    void setFullscreen();

    void setWindowedMode(int width, int height);

    void resizeWindow(int width, int height);

    void restoreWindow();

    void dragWindow(int x, int y);

    int getWindowX();

    int getWindowY();


}
