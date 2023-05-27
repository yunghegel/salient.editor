package ui.widgets;

import app.Editor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.github.ykrasik.jaci.cli.libgdx.LibGdxCli;
import com.github.ykrasik.jaci.cli.libgdx.LibGdxCliBuilder;
import com.github.ykrasik.jaci.cli.libgdx.reflection.LibGdxReflectionAccessor;
import com.github.ykrasik.jaci.cli.output.CliOutput;
import com.github.ykrasik.jaci.reflection.JavaReflectionAccessor;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.util.TableUtils;
import com.kotcrab.vis.ui.widget.ScrollableTextArea;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisWindow;
import com.strongjoshua.console.GUIConsole;
import ecs.systems.PhysicsSystem;
import scene.env.EnvironmentCommands;
import scene.env.FogConfig;
import scene.env.LightConfig;
import scene.env.SalientEnvironment;

public class ConsoleWidget extends VisTable implements CliOutput {

    public GUIConsole console;
    public CliConsoleImpl cli;
    ConsoleBuilder builder = new ConsoleBuilder();
    public TextButton button;
    public Table table;
    public ScrollPane scrollPane;
    public TextField textField;
    public ConsoleWidget(){
        super();
        VisWindow consoleWindow = new VisWindow("Console");
        Table consoleTable = new Table();
        JavaReflectionAccessor.install();

//        add(cli).expand().fill().grow();
//        setFillParent(true);
        console = Editor.i().console;
                System.out.println(console.getWindow().toString());
        System.out.println("-----");

        table = (Table) console.getWindow().getChild(1);

        scrollPane = (ScrollPane) table.getChild(0);


        textField = (TextField) table.getChild(1);

        button = (TextButton) table.getChild(2);

//        add(scrollPane).expand().fill().grow().colspan(2).row();
//        add(textField).expandX().fillX().pad(4);
//        add(button).pad(4);


        console.enableSubmitButton(true);
        console.getWindow().debug();
        console.getWindow().getTitleTable().debug();


        cli = new ConsoleBuilder().processClasses(EnvironmentCommands.class, LightConfig.class, FogConfig.class, PhysicsSystem.class).setSkin(VisUI.getSkin()).setConsoleWidget(this).build();
        add(cli).expand().fill().grow();

        addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                return true;
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                if(toActor==Editor.i().viewportWidget){
                getStage().setScrollFocus(null);
                getStage().setKeyboardFocus(null);}
                super.exit(event, x, y, pointer, toActor);
            }
        });

    }

    @Override
    public void println(String text) {
        console.log(text);


    }



    static class ConsoleBuilder extends CliConsoleImpl.AbstractBuilder {



    }



}
