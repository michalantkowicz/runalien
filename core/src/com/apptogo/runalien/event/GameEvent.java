package com.apptogo.runalien.event;

import lombok.Getter;

import static java.lang.String.format;

public abstract class GameEvent<T> {
    static final String DEFAULT_TOPIC = "default_topic";
    private static final int IMMORTAL = Integer.MIN_VALUE;

    @Getter
    private String topic = DEFAULT_TOPIC;
    @Getter
    private final GameEventType gameEventType;
    @Getter
    private final T eventObject;

    private int age;
    private int maxAge;

    /**
     * @param maxAge how much loops event lives, must be greater or equal 0
     * @throws IllegalArgumentException if maxAge < 0
     */
    protected GameEvent(GameEventType gameEventType, T eventObject, int maxAge) {
        this.gameEventType = gameEventType;
        this.eventObject = eventObject;
        if (maxAge != IMMORTAL && maxAge < 0) {
            throw new IllegalArgumentException("The maxAge must be at least 0");
        }
        this.maxAge = maxAge;
        this.age = 0;
    }

    /**
     * creates immortal game event (will be deleted on demand)
     */
    protected GameEvent(GameEventType gameEventType, T eventObject, String topic) {
        this(gameEventType, eventObject, IMMORTAL);

        if (DEFAULT_TOPIC.equals(topic)) {
            throw new IllegalArgumentException(format("The topic [%s] cannot have the same value as default topic [%s]", topic, DEFAULT_TOPIC));
        } else {
            this.topic = topic;
        }
    }

    void increaseAge() {
        age++;
    }

    boolean isTooOld() {
        return (maxAge != IMMORTAL) && (age >= maxAge);
    }
}
