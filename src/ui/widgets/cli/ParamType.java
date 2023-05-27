package ui.widgets.cli;

public enum ParamType {
    STRING,FLOAT,INT,DOUBLE,BOOLEAN;

    public Class val(){
        return switch (this) {
            case STRING -> String.class;
            case FLOAT -> Float.class;
            case INT -> Integer.class;
            case DOUBLE -> Double.class;
            case BOOLEAN -> Boolean.class;
        };
    }
}
