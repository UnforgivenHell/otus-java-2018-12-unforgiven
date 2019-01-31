package ru.otus.hw041;

public interface BenchmarkMBean
{
    int getSize();

    void setSize(int size);

    void subscribeToGcEvents();
}
