package me.kvalbrus.multibans.api.punishment;

public interface TemporaryPunishment extends Punishment, Cancelable {

    long getDuration();

    long getStartedDate();

    void setDuration(long duration);

    void setStartedDate(long startedDate);
}