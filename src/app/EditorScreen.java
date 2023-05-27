package app;

import backend.Profiler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import app.Editor;

public class EditorScreen implements Screen {

    public Editor editor;

    public EditorScreen(Editor game) {
        this.editor = game;

    }

    @Override
    public void render(float delta) {
        Profiler.reset();
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
//        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1f);
//        Editor.i().sceneContext.sceneRenderer.render(Gdx.graphics.getDeltaTime());
        editor.editorUI.render(delta);
        editor.ecs.update(delta);

    }

    @Override
    public void resize(int width, int height) {
        editor.stage.getViewport().update(width, height, true);
        System.out.println("Resized to: " + width + "x" + height);

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void show() {

    }
    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
