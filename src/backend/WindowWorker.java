package backend;

public interface WindowWorker {
    public static enum WindowState {
        ICONIFIED{

        }, RESTORED, MAXIMIZED
    }
    public void minimize();
    public void maximize();
    public void restore();
    public void setPosition(int x, int y);
    public void center();
    public int getPositionX();
    public int getPositionY();
    public WindowState getWindowState();
}
