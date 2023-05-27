package ui.widgets.cli;

import app.Editor;
import com.badlogic.gdx.graphics.Color;

public class CliCommands {

    Console console;

    private static CliCommands instance;

    public static CliCommands getInstance() {
        if (instance == null) {
            instance = new CliCommands(Editor.i().editorUI.console);
        }
        return instance;
    }

    public CliCommands(Console console){
        this.console = console;
    }

    @ConsoleCommand(command = "sayHello", description = "Print hello to the console")
    public void sayHello(){
        System.out.println("Hello");
    }

    @ConsoleCommand(command = "sayHelloTo", description = "Print hello to the console")
    public void sayHelloTo(@ConsoleCommandParameter(name = "name", description = "The name of the person to say hello to", type = ParamType.STRING) String name){
        System.out.println("Hello " + name);
    }

    @ConsoleCommand(command = "help", description = "Print hello to the console")
    public void help(){
        for (int i = 0; i <console.commandExecutor.commands.size ; i++) {
            Command command = console.commandExecutor.commands.get(i);
           console.println(command.getCommand() + " - [ " + command.getDescription() + " ]", Color.CYAN);
        }
    }

}
