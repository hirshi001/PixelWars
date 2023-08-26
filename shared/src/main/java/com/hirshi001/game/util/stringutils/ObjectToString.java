package com.hirshi001.game.util.stringutils;

@FunctionalInterface
public interface ObjectToString<T> {


    public String toString(T object);

}
