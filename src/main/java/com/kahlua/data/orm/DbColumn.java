package com.kahlua.data.orm;

/**
 * Interface for db columns holding name, id column and sequence. 
 * Created by TTCGOKCEL on 8/4/2017.
 * @author TTCGOKCEL, TCETEKDEMIR
 */
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = {ElementType.METHOD, ElementType.FIELD})
public @interface DbColumn {

    public String name();

    public boolean idColumn() default false;

    public String sequence() default "";
}