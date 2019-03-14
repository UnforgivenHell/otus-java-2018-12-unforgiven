package ru.otus.Cache;

import java.lang.ref.SoftReference;

public class CacheElement<T> extends SoftReference<T> {

    private long lastAccessTime;

    protected long getCurrentTime() {
        return System.currentTimeMillis();
    }

    public CacheElement(T ref) {
        super(ref);
        setAccessed();
    }

    public long getLastAccessTime() {
        return this.lastAccessTime;
    }

    public void setAccessed() {
        this.lastAccessTime = getCurrentTime();
    }

    public long getLastAccessMs() {
        return (getCurrentTime() - this.lastAccessTime);
    }


}
