package me.kvalbrus.multibans.common.punishment;

public interface Temporary {

    long getDuration();

    long getStartedDate();

    void setDuration(long duration);

    void setStartedDate(long startedDate);
}