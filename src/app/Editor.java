package app;

import backend.NativeInterface;
import backend.Profiler;
import backend.asset.FileDrop;
import backend.Natives;
import backend.WindowWorker;
import backend.tools.Log;
import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Graphics;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.kotcrab.vis.ui.VisUI;
import com.strongjoshua.console.GUIConsole;
import core.graphics.SalientShapeRenderer;
import ecs.EntityComponentSystem;
import input.InputManager;
import scene.SceneContext;
import ui.EditorUI;
import ui.widgets.ViewportWidget;
import utils.GLUtils;

public class Editor extends Game {

    private static Editor editor;
    public Natives natives;
    public WindowWorker worker;
    public InputManager inputManager;
    public SceneContext sceneContext;
    public EntityComponentSystem ecs;
    public ViewportWidget viewportWidget;
    public Skin skin;
    public Skin altSkin;
    public Stage stage;
    public EditorUI editorUI;
    public InputMultiplexer inputMultiplexer = new InputMultiplexer();
    public Engine engine = new Engine();
    public SalientShapeRenderer drawer;
    public FileDrop fileDrop;
    public GUIConsole console;

    public NativeInterface nativeInterface;

    public Profiler profiler;



    public static Editor i(){
        return editor;
    }

    public Editor(NativeInterface nativeInterface) {
        this.nativeInterface = nativeInterface;
        editor = this;
    }

    @Override
    public void create() {
        profiler = new Profiler();

        Lwjgl3Graphics g = (Lwjgl3Graphics) Gdx.graphics;
        long windowHandle = g.getWindow().getWindowHandle();
        GLUtils.overwriteWindowProc2(windowHandle);

        skin = new Skin(Gdx.files.internal("skin/salient/salient_ui.json"));
//        altSkin = new Skin(Gdx.files.internal("skin/uiskin.json"));
        altSkin= new Skin();
        if (!VisUI.isLoaded()){
            VisUI.load(skin);
        }
        Bullet.init();
        Log.info("PhysicsSystem" , "Bullet initialized");
        inputManager = new InputManager();
        stage = new Stage(new ScreenViewport());
        inputManager.addInputProcessor(stage);

        console = new GUIConsole(skin,false, Input.Keys.ENTER,Window.class,Table.class,"gray", TextField.class, TextButton.class, Label.class, ScrollPane.class);







        sceneContext = new SceneContext();
        drawer = new SalientShapeRenderer(sceneContext.cam);
        ecs = new EntityComponentSystem(sceneContext.sceneGraph,engine);


        viewportWidget = new ViewportWidget(sceneContext.viewport);
        editorUI = new EditorUI(stage, viewportWidget);



        setScreen(new EditorScreen(this));
    }
}
