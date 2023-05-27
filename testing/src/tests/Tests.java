package tests;

import com.badlogic.gdx.Game;
import tests.imgui.GdxImGui;

public class Tests extends Game {

    private static Tests instance;

    public GdxImGui imgui;

    public static Tests getInstance() {
        return instance;
    }

    public Tests() {
        instance = this;
    }

    @Override
    public void create() {
        imgui = new GdxImGui();
        setScreen(new TestsHomeScreen(this));
    }
}
