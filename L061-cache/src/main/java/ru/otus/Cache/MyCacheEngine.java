package ru.otus.Cache;

public interface MyCacheEngine<K, V> {

    void put(K key, V value);

    V get(K key);

    int getHitCount();

    int getMissCount();

    int getMaxElements();

    void dispose();

}
