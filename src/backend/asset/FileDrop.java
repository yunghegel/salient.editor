package backend.asset;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3WindowAdapter;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3WindowListener;

import java.util.ArrayList;
import java.util.Arrays;

public class FileDrop extends Lwjgl3WindowAdapter {

    public ArrayList<String> files = new ArrayList<>();
    public ValidFileDrop validFileDrop;

    public FileDrop() {
        validFileDrop = new ValidFileDrop(this);
    }

    @Override
    public void filesDropped(String[] files) {
        this.files.addAll(new ArrayList<>(files.length));
        validFileDrop.sortFiles(files);
        System.out.println("Files dropped: " + Arrays.toString(files));
        super.filesDropped(files);
    }

    public ArrayList<String> getFiles() {
        return files;
    }
}
