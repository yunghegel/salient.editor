package tests;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import imgui.ImGui;
import tests.imgui.GdxImGui;

public class TestsHomeScreen extends ScreenAdapter {

    Game game;
    public GdxImGui gui;

    public TestsHomeScreen(Game game) {
        this.game = game;
    }

    boolean shaderScreen = false;


    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1f);
       gui=  Tests.getInstance().imgui;
       gui.begin();
        ImGui.begin("Tests");
        if (ImGui.radioButton("Shader Test", shaderScreen)){
            shaderScreen = true;
            game.setScreen(new ShaderTest());
        }

        gui.end();

    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
