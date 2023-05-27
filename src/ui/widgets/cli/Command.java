package ui.widgets.cli;

import com.badlogic.gdx.utils.Array;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

public class Command {

    private String command;
    private Console console;
    private String description;

    public Method method;
    public Array<Parameter> parameters=new Array<>();
    public Map<Integer,ParamType> paramTypes=new HashMap<>();

    public Command(Console console,Method method,Parameter ...parameters){
        this.console=console;
        this.method=method;
        if (parameters != null) {
            for (int i = 0; i < parameters.length; i++) {
                this.parameters.add(parameters[i]);
                if (parameters[i].getAnnotation(ConsoleCommandParameter.class) != null) {
                    paramTypes.put(i,parameters[i].getAnnotation(ConsoleCommandParameter.class).type());
                }

            }
        }


        command=method.getAnnotation(ConsoleCommand.class).command();
        description=method.getAnnotation(ConsoleCommand.class).description();

    }

    public void invoke(Object ...args){
        try {
            method.invoke(console,args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getCommand() {
        return command;
    }

    public void submitCommand(){

    }


    public String getDescription() {
        return description;
    }
}
