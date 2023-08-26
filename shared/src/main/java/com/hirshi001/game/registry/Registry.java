package com.hirshi001.game.registry;

public interface Registry<T extends ID> {

    void register(T obj, int id);

    T get(int id);



}
