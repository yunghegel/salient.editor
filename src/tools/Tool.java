package tools;

import com.badlogic.gdx.InputAdapter;

public interface Tool<T> {

    void render(float delta);

    void update(float delta);

    void enable();

    void disable();

}
