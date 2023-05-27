package ui.widgets.cli;

import com.badlogic.gdx.utils.Array;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.Arrays;

public class CliCommandExecutor {

    public Array<Command> commands = new Array<Command>();
    public Console console;


    public CliCommandExecutor(Console console, Class ...classes){
        ConsoleClassScanner scanner = new ConsoleClassScanner(console,commands,classes);
        this.console = console;
    }

    public void execCommand(Command command, String ...args) throws InvocationTargetException, IllegalAccessException {
        Array<Object> argsArray = new Array<Object>();


        if(args!=null){


        for (int i = 0; i < command.parameters.size; i++) {
            ParamType type = command.paramTypes.get(i);
            switch (type){
                case STRING:
                    argsArray.add(args[i]);
                    break;
                case INT:
                    argsArray.add(Integer.parseInt(args[i]));
                    break;
                case FLOAT:
                    argsArray.add(Float.parseFloat(args[i]));
                    break;
                case DOUBLE:
                    argsArray.add(Double.parseDouble(args[i]));
                    break;
                case BOOLEAN:
                    argsArray.add(Boolean.parseBoolean(args[i]));
                    break;
            }
        }
            System.out.println("Invoking method: " + command.method.getName() + " with args: " + Arrays.toString(argsArray.toArray()));
            command.method.invoke(new CliCommands(console),argsArray.toArray());
        } else {
            System.out.println("Invoking method: " + command.method.getName() + " with args: " + Arrays.toString(argsArray.toArray()));
            command.method.invoke(new CliCommands(console));
        }


    }

    public void execCommand(String string) throws InvocationTargetException, IllegalAccessException {
        for (Command command : commands) {

            //command without args if it has no spaces
            String cmd;
            String[] args;
            //if string has () then it has args
            if(string.contains("(")) {
                args = string.substring(string.indexOf("(") + 1, string.indexOf(")")).split(",");
                System.out.println("Args: " + Arrays.toString(args));
                cmd = string.substring(0, string.indexOf("("));
                if(command.getCommand().equals(cmd)){
                    System.out.println("Executing command: " + command.getCommand());

                    execCommand(command,args);
                }
            }else{
                cmd = string;
                if(command.getCommand().equals(cmd)){
                    System.out.println("Executing command: " + command.getCommand());

                    execCommand(command);
                }
            }

        }
    }

    private String[] parseArgs(String command){
        //format: "command(arg1,arg2,arg3)"
        String[] args = command.substring(command.indexOf("(")+1,command.indexOf(")")).split(",");








        for (int i = 0; i < args.length ; i++) {
            System.out.println("Arg: " + args[i]);
        }


        return args;
    }


}
