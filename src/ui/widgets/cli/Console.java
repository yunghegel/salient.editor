package ui.widgets.cli;

import app.Editor;
import backend.OutputStreamInterceptor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.kotcrab.vis.ui.widget.VisSplitPane;

import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.Queue;

public class Console extends Table {

    PrintStream sysOut;

    private Table cliBuffer;
    private Table logBuffer;
    private ScrollPane cliScrollPane;
    private ScrollPane logScrollPane;

    private Queue<Label> bufferEntries;
    private int maxBufferEntries=100;
    private int numBufferEntries;

    private OutputStreamInterceptor outputStreamInterceptor;

    private TextField inputField;
    private TextButton sendButton;
    CliCommandExecutor commandExecutor;

    public Console() {
        super(Editor.i().skin);
        cliBuffer = new Table();
        logBuffer = new Table();
        sysOut = System.out;
        outputStreamInterceptor = new OutputStreamInterceptor(System.out);
        outputStreamInterceptor.setOnPrintListener(s -> log(s));
        bufferEntries= new LinkedList<>();
        commandExecutor = new CliCommandExecutor(this, CliCommands.class);
        setSkin(Editor.i().skin);
        initElements();
        initListeners();

        println("Welcome!", Color.WHITE);
        println("Type 'help' for a list of commands.", Color.WHITE);


    }

    private void initListeners() {
        sendButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                String input = inputField.getText();
                inputField.setText("");
                if(input.length()>0) {
                    try {
                        submitCommand(input);
                    } catch (InvocationTargetException | IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }

    private void initElements() {

        cliBuffer.setSkin(getSkin());
        cliBuffer.bottom().left();
        cliBuffer.padLeft(3);
        cliScrollPane = new ScrollPane(cliBuffer);
        cliScrollPane.setName("outputBufferScrollPane");
        cliScrollPane.setScrollingDisabled(true, false);
//        scrollPane.setupOverscroll(0, 0, 0);
        cliScrollPane.setFillParent(true);

        logBuffer.setSkin(getSkin());
        logBuffer.bottom().left();
        logBuffer.padLeft(3);
        logScrollPane = new ScrollPane(logBuffer);
        logScrollPane.setName("outputBufferScrollPane");
        logScrollPane.setScrollingDisabled(true, false);
//        scrollPane.setupOverscroll(0, 0, 0);
        logScrollPane.setFillParent(true);

        Table cli = new Table();
        cli.setSkin(getSkin());
        Table cliTitle = new Table();
        cliTitle.setSkin(getSkin());
        cliTitle.add("CLI").expandX().fillX().pad(5).row();
//        cli.add(cliTitle).expandX().fillX().pad(5).row();
        cli.add(cliScrollPane).expand().fill().row();







        inputField = new TextField("", getSkin());
        inputField.setName("inputField");
        inputField.setDisabled(false);
        inputField.setMaxLength(100);
        inputField.setBlinkTime(0.5f);

        sendButton = new TextButton("Send", getSkin());
        sendButton.setName("sendButton");
        sendButton.setDisabled(false);

        Table inputTable = new Table();
        inputTable.setSkin(getSkin());
        inputTable.bottom().left();
        inputTable.add(inputField).expandX().fillX();
        inputTable.add(sendButton).pad(3);
        inputTable.pad(1);
        cli.add(inputTable).expandX().fillX().row();
        Table log = new Table();
        Table logTitle = new Table();

        logTitle.setSkin(getSkin());
        logTitle.setBackground("highlighted");
        logTitle.add("Log").expandX().fillX().pad(5).row();
        log.setSkin(getSkin());
//        log.add(logTitle).expandX().fillX().row();
        log.add(logScrollPane).expand().fill().row();

        VisSplitPane splitPane = new VisSplitPane(cli, log, false);

        pad(1);
        add(splitPane).expand().fill().row();
//        add(inputTable).expandX().fillX().row();

        layout();
    }

    private void updateScroll() {
        if (cliScrollPane == null) {
            return;
        }
        // Set the scroll to the bottom of the pane.
        cliScrollPane.layout();
        cliScrollPane.setScrollPercentY(1);
        cliScrollPane.updateVisualScroll();

        logScrollPane.layout();
        logScrollPane.setScrollPercentY(1);
        logScrollPane.updateVisualScroll();

    }

    private void addLabel(Label newEntry) {
        if (numBufferEntries == maxBufferEntries) {
            final Label lastEntry = bufferEntries.poll();
            cliBuffer.removeActor(lastEntry);
            numBufferEntries--;
        }

        bufferEntries.add(newEntry);
        numBufferEntries++;
        Table labelTable = new Table();
        labelTable.setSkin(getSkin());
        labelTable.add(newEntry).left().expandX().fillX().pad(1).row();
        if(numBufferEntries%2==0) {
            labelTable.setBackground(getSkin().getDrawable("dark-highlighted"));
        }


        cliBuffer.add(labelTable).expandX().fillX().row();

        updateScroll();
    }

    public void println(String text, Color color) {
        final Label label = new Label(text, getSkin(), "default");
        label.setColor(color);
        label.setWrap(true);
        addLabel(label);
    }

    int numLogEntries=0;

    public void log(String text){
        numLogEntries++;
        final Label label = new Label(text, getSkin(), "default");
        label.setColor(Color.WHITE);
        label.setWrap(true);
        Table labelTable = new Table();
        labelTable.setSkin(getSkin());
        labelTable.add(label).left().expandX().fillX().pad(1).row();
        if(numLogEntries%2==0) {
            labelTable.setBackground(getSkin().getDrawable("dark-highlighted"));
        }
        logBuffer.add(labelTable).expandX().fillX().row();
        updateScroll();
    }

    public void submitCommand(String command) throws InvocationTargetException, IllegalAccessException {
        println(command, Color.GREEN);
        commandExecutor.execCommand(command);

    }
}
