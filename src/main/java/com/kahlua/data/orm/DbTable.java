package com.kahlua.data.orm;

/**
 * Interface for db tables holding name and schema. 
 * Created by TTCGOKCEL on 8/4/2017.
 * @author TTCGOKCEL
 */
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = {ElementType.TYPE})
public @interface DbTable {

    public String name();

    public String schema();
}