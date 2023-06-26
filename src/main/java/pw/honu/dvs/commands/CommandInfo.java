package pw.honu.dvs.commands;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface CommandInfo {

    String name();

    String pattern();

    String usage();

    String desc();

    String permission();

}
