package ui.widgets.cli;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ConsoleCommand {
    public String command();
    public String description() default "";

}
