package ui.widgets.cli;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ConsoleCommandParameter {

    String name();

    String description();

    ParamType type();

}
