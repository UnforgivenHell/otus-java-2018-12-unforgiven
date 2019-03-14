package ru.otus.Cache;

import java.util.*;
import java.util.function.Function;

public class MyCacheEngineImpl<K, V> implements MyCacheEngine<K, V> {

    Map<K, CacheElement<V>> storage = new LinkedHashMap<>();

    private final Timer timer = new Timer();
    private final int maxElements;
    private final long idleTimeMs;

    private int hit = 0;
    private int miss = 0;

    MyCacheEngineImpl(int maxElements, long idleTimeMs) {
        this.maxElements = maxElements;
        this.idleTimeMs = idleTimeMs;
    }

    @Override
    public void put(K key, V value) {

        if (storage.size() == maxElements) {
            K firstKey = storage.keySet().iterator().next();
            storage.remove(firstKey);
        }

        storage.put(key, new CacheElement<>(value));

        timer.schedule(getTimerTask(key), idleTimeMs, idleTimeMs /2 );
    }

    @Override
    public V get(K key) {
        V value = null;

        CacheElement<V> cacheElement = storage.get(key);
        if (cacheElement != null) {
            value = cacheElement.get();
            if (value != null) {
                hit++;
                cacheElement.setAccessed();
            } else {
                miss++;
            }
        } else {
            miss++;
        }

        return value;
    }

    @Override
    public int getHitCount() {
        return hit;
    }

    @Override
    public int getMissCount() {
        return miss;
    }

    @Override
    public int getMaxElements() {
        return maxElements;
    }

    @Override
    public void dispose() {
        timer.cancel();
    }

    private TimerTask getTimerTask(final K key) {
        return new TimerTask() {
            @Override
            public void run() {
                CacheElement<V> element = storage.get(key);
                if (element == null || element.get() == null || isNeedRemove(element)) {
                    storage.remove(key);
                    this.cancel();
                }
            }
        };
    }

    private boolean isNeedRemove(CacheElement<V> element) {
        return (element.getLastAccessMs() >= this.idleTimeMs);
    }

}
