package org.microg.safeparcel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface SafeParceled {
    int value();

    boolean mayNull() default false;

    @Deprecated String subType() default "undefined";

    Class subClass() default SafeParceled.class;
}
