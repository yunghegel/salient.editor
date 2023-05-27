package ui.widgets.cli;

import com.badlogic.gdx.utils.Array;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class ConsoleClassScanner {

    public ConsoleClassScanner(Console console, Array<Command> commands,Class ...classes){
        for (Class classToScan : classes) {
            scan(classToScan,console,commands);
        }

    }

    public Array<Command> scan(Class classToScan,Console console, Array<Command> cmdOut){

        if(cmdOut==null)cmdOut=new Array<Command>();

        for (Method method : classToScan.getMethods()) {
            if(method.isAnnotationPresent(ConsoleCommand.class)){


                Parameter[] params = method.getParameters();
                Array<Parameter> parameters = new Array<Parameter>();
                for (int i = 0; i < method.getParameters().length; i++) {
                    parameters.add(method.getParameters()[i]);
                }

                Command command = new Command(console,method, method.getParameters());

                System.out.println("Found command: " + command.getCommand());

                cmdOut.add(command);
            }
        }
        return cmdOut;
    };

}
