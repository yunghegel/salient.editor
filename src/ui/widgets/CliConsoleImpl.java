package ui.widgets;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.github.ykrasik.jaci.cli.Cli;
import com.github.ykrasik.jaci.cli.CliShell;
import com.github.ykrasik.jaci.cli.commandline.CommandLineManager;
import com.github.ykrasik.jaci.cli.gui.CliGui;
import com.github.ykrasik.jaci.cli.hierarchy.CliCommandHierarchy;
import com.github.ykrasik.jaci.cli.hierarchy.CliCommandHierarchyImpl;
import com.github.ykrasik.jaci.cli.libgdx.LibGdxCliInputListener;
import com.github.ykrasik.jaci.cli.libgdx.VisibleListener;
import com.github.ykrasik.jaci.cli.libgdx.commandline.LibGdxCommandLineManager;
import com.github.ykrasik.jaci.cli.libgdx.gui.LibGdxCliGui;
import com.github.ykrasik.jaci.cli.libgdx.log.ApplicationLoggingDecorator;
import com.github.ykrasik.jaci.cli.libgdx.output.LibGdxCliOutput;
import com.github.ykrasik.jaci.cli.libgdx.output.LibGdxCliOutputBuffer;
import com.github.ykrasik.jaci.cli.output.CliPrinter;
import com.github.ykrasik.jaci.hierarchy.CommandHierarchyDef;
import com.kotcrab.vis.ui.widget.ScrollableTextArea;
import com.kotcrab.vis.ui.widget.VisTextArea;

import java.util.Objects;

public class CliConsoleImpl extends Table {
    private final Array<VisibleListener> visibleListeners = new Array<>(2);

    private final Cli cli;
    public ConsoleWidget consoleWidget;
    public LibGdxCliOutputBuffer buffer;

    /**
     * @param skin Skin to use.
     * @param hierarchy Command hierarchy.
     * @param maxBufferEntries Maximum amount of line entries in the buffer to keep.
     * @param maxCommandHistory Maximum amount of command history entries to keep.
     */
    private CliConsoleImpl(Skin skin, CliCommandHierarchy hierarchy, int maxBufferEntries, int maxCommandHistory,ConsoleWidget consoleWidget) {
        super(Objects.requireNonNull(skin, "skin"));
        this.consoleWidget = consoleWidget;

        // CLI GUI controller.
        final Label workingDirectory = new Label("", skin, "workingDirectory");
        workingDirectory.setName("workingDirectory");
        final CliGui gui = new LibGdxCliGui(workingDirectory);

        // Buffer for cli output.
        buffer = new LibGdxCliOutputBuffer(skin, maxBufferEntries);
        ScrollableTextArea textArea = new ScrollableTextArea("");
        ScrollPane scrollPane = textArea.createCompatibleScrollPane();
        scrollPane.setActor(textArea);
        buffer.setName("buffer");
        buffer.bottom().left();
        buffer.setFillParent(true);
        CliOutput output = new CliOutput(maxBufferEntries);

        // LibGdx doesn't like '\t', so we replace it with 4 spaces.
        final String tab = "    ";
        final CliPrinter out = new CliPrinter(consoleWidget, tab);
        final CliPrinter err = new CliPrinter(consoleWidget, tab);

        output.setFillParent(true);

        // TextField as command line.
        final TextField commandLine = consoleWidget.textField;
        commandLine.setName("commandLine");
        final CommandLineManager commandLineManager = new LibGdxCommandLineManager(commandLine);

        // Create the shell and the actual CLI.
        final CliShell shell = new CliShell.Builder(hierarchy, gui, out, err)
                .setMaxCommandHistory(maxCommandHistory)
                .build();
        cli = new Cli(shell, commandLineManager);


        // Hook input events to CLI events.
        this.addListener(new LibGdxCliInputListener(cli));

        // A close button.
        // TODO: Make this a graphical button, not an ugly text button.
        final Button closeButton = new TextButton("X", skin, "closeCliButton");
        closeButton.padRight(15).padLeft(15);
        closeButton.setName("closeButton");
        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setVisible(false);
            }
        });

        // Some layout.

        final Table workingDirectoryTable = new Table(skin);
        workingDirectoryTable.setName("workingDirectoryTable");
        workingDirectoryTable.setBackground("workingDirectoryBackground");
        workingDirectoryTable.add(workingDirectory).fill().padLeft(3).padRight(5);

        // The bottom row contains the current path, command line and a close button.
        final Table bottomRow = new Table(skin);
        bottomRow.setName("bottomRow");
        bottomRow.setBackground("bottomRowBackground");
        bottomRow.add(workingDirectoryTable).fill();
        bottomRow.add(commandLine).fill().expandX();
        bottomRow.add(closeButton).fill();

        this.setName("cli");
        this.setBackground("cliBackground");

        // TODO: This should operate on it's own stage.
//        this.addVisibleListener(new VisibleListener() {
//            @Override
//            public void onVisibleChange(boolean wasVisible, boolean isVisible) {
//                if (!wasVisible && isVisible) {
//                    setKeyboardFocus(commandLine);
//                } else {
//                    setKeyboardFocus(null);
//                }
//            }
//        });

//        output.add(bottomRow).fill();
        this.pad(0);
        this.add(consoleWidget.scrollPane).fill().expand();
//        this.add(buffer).fill().expand();
        this.row();
        this.add(bottomRow).fill();
        this.top().left();
//        this.setFillParent(true);
    }

    private void setKeyboardFocus(TextField commandLine) {
        final Stage stage = getStage();
        if (stage != null) {
            stage.setKeyboardFocus(commandLine);
        }
    }

    /**
     * Add a {@link VisibleListener} that will be called when this actor's visibility state changes -
     * it either was visible and became invisible, or the other way.
     *
     * @param listener Listener to add.
     */
    public void addVisibleListener(VisibleListener listener) {
        visibleListeners.add(listener);
    }

    /**
     * Remove a {@link VisibleListener} from this actor.
     *
     * @param listener Listener to remove.
     */
    public void removeVisibleListener(VisibleListener listener) {
        visibleListeners.removeValue(listener, true);
    }

    /**
     * Toggle the visibility of this CLI on or off.
     */
    public void toggleVisibility() {
        this.setVisible(!this.isVisible());
    }

    @Override
    public void setVisible(boolean visible) {
        final boolean wasVisible = isVisible();
        for (VisibleListener listener : visibleListeners) {
            listener.onVisibleChange(wasVisible, visible);
        }
        super.setVisible(visible);
    }

    @Override
    protected void setStage(Stage stage) {
        super.setStage(stage);

        // Call the listeners when we're first added to a stage.
        final boolean isVisible = isVisible();
        final boolean wasVisible = !isVisible;
        for (VisibleListener listener : visibleListeners) {
            listener.onVisibleChange(wasVisible, isVisible);
        }
    }

    public void setConsoleWidget(ConsoleWidget consoleWidget) {
        this.consoleWidget = consoleWidget;
    }

    /**
     * @return CLI stdOut.
     */
    public CliPrinter getOut() {
        return cli.getOut();
    }

    /**
     * @return CLI stdErr.
     */
    public CliPrinter getErr() {
        return cli.getErr();
    }

    /**
     * A builder for a {@link com.github.ykrasik.jaci.cli.libgdx.LibGdxCli}.
     * Builds a CLI with a default skin, unless a custom skin is specified via {@link #setSkin(Skin)}.<br>
     * The main methods to use are {@link #processClasses(Class[])} and {@link #process(Object[])} which process
     * a class and add any annotated methods as commands to this builder.
     */
    public abstract static class AbstractBuilder {
        private final CommandHierarchyDef.Builder hierarchyBuilder = new CommandHierarchyDef.Builder();

        private Skin skin;
        private int maxBufferEntries = 1000;
        private int maxCommandHistory = 30;
        private boolean decorateApplicationLog = false;
        private ConsoleWidget consoleWidget;

        /**
         * Process the classes and add any commands defined through annotations to this builder.
         * Each class must have a no-args constructor.
         *
         * @param classes Classes to process.
         * @return {@code this}, for chaining.
         */
        public CliConsoleImpl.AbstractBuilder processClasses(Class<?>... classes) {
            hierarchyBuilder.processClasses(classes);
            return this;
        }

        /**
         * Process the objects' classes and add any commands defined through annotations to this builder.
         *
         * @param instances Objects whose classes to process.
         * @return {@code this}, for chaining.
         */
        public CliConsoleImpl.AbstractBuilder process(Object... instances) {
            hierarchyBuilder.process(instances);
            return this;
        }

        /**
         * Set the maximum amount of output buffer entries to keep.
         *
         * @param maxBufferEntries Max output buffer entries to keep.
         * @return {@code this}, for chaining.
         */
        public CliConsoleImpl.AbstractBuilder setMaxBufferEntries(int maxBufferEntries) {
            this.maxBufferEntries = maxBufferEntries;
            return this;
        }

        /**
         * Set the maximum amount of command history entries to keep.
         *
         * @param maxCommandHistory Max command history entries to keep.
         * @return {@code this}, for chaining.
         */
        public CliConsoleImpl.AbstractBuilder setMaxCommandHistory(int maxCommandHistory) {
            this.maxCommandHistory = maxCommandHistory;
            return this;
        }

        /**
         * Set the skin to use.<br>
         * A custom skin must have the following:
         * <ul>
         *     <li>
         *         A {@link Label.LabelStyle} called 'workingDirectory' that will be used to style the 'workingDirectory' label.
         *     </li>
         *     <li>
         *         A {@link com.badlogic.gdx.scenes.scene2d.utils.Drawable} called 'workingDirectoryBackground'
         *         that will be used as the background of the 'workingDirectory' label.
         *     </li>
         *     <li>
         *         A {@link TextField.TextFieldStyle} called 'commandLine' that will be used to style the command line text field.
         *     </li>
         *     <li>
         *         A {@link TextButton.TextButtonStyle} called 'closeCliButton' that will be used to style the close button.
         *     </li>
         *     <li>
         *         A {@link com.badlogic.gdx.scenes.scene2d.utils.Drawable} called 'bottomRowBackground'
         *         that will be used as the background of the 'bottom row' (working directory, command line, close button).
         *     </li>
         *     <li>
         *         A {@link Label.LabelStyle} called 'outputEntry' that will be used to style output buffer entry lines.
         *     </li>
         *     <li>
         *         A {@link com.badlogic.gdx.scenes.scene2d.utils.Drawable} called 'cliBackground'
         *         that will be used as the background of the whole widget.
         *     </li>
         * </ul>
         *
         * @param skin Skin to use.
         * @return {@code this}, for chaining.
         */
        public CliConsoleImpl.AbstractBuilder setSkin(Skin skin) {
            this.skin = skin;
            return this;
        }

        public CliConsoleImpl.AbstractBuilder setConsoleWidget(ConsoleWidget consoleWidget) {
            this.consoleWidget = consoleWidget;
            return this;
        }

        // FIXME: JavaDoc - whether to log Gdx.App.log
        public CliConsoleImpl.AbstractBuilder setDecorateApplicationLog(boolean decorateApplicationLog) {
            this.decorateApplicationLog = decorateApplicationLog;
            return this;
        }

        /**
         * @return A {@link com.github.ykrasik.jaci.cli.libgdx.LibGdxCli} built out of this builder's parameters.
         */
        public CliConsoleImpl build() {
            final Skin skin = getSkin();
            final CliCommandHierarchy hierarchy = CliCommandHierarchyImpl.from(hierarchyBuilder.build());
            final CliConsoleImpl cli = new CliConsoleImpl(skin, hierarchy, maxBufferEntries, maxCommandHistory, consoleWidget);

            if (decorateApplicationLog) {
                decorateApplication(cli);
            }
            return cli;
        }

        private Skin getSkin() {
            if (skin != null) {
                return skin;
            }

            // Default skin.
            return new Skin(Gdx.files.classpath("com/github/ykrasik/jaci/cli/libgdx/default_cli.cfg"));
        }

        private void decorateApplication(CliConsoleImpl cli) {
            final Application currentApplication = Objects.requireNonNull(Gdx.app, "Gdx.app is null?! This should not be called before onCreate()!");
            if (currentApplication instanceof ApplicationLoggingDecorator) {
                throw new IllegalStateException("Gdx.app is already decorated for logging!");
            }
//            Gdx.app = new ApplicationLoggingDecorator(currentApplication, cli);
        }
    }
}
