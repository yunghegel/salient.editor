package tools;

import com.badlogic.gdx.InputAdapter;
import app.Editor;
import input.InputManager;

public abstract class AbstractTool<T> extends InputAdapter implements Tool<T> {

    public InputManager inputManager;
    public Editor editor;
    public T target;

    public AbstractTool(){
        editor=Editor.i();
        inputManager = editor.inputManager;
    }

    public void setTarget(T target){
        this.target = target;
    }

    public T getTarget(){
        return target;
    }

}
