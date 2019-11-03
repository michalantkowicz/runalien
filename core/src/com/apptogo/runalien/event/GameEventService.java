package com.apptogo.runalien.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.apptogo.runalien.event.GameEvent.DEFAULT_TOPIC;
import static java.util.Optional.empty;
import static java.util.Optional.of;

public class GameEventService {
    private Map<Class<? extends GameEvent>, Map<String, List<GameEvent>>> queue = new HashMap<>();

    public <T extends GameEvent> void pushEvent(T gameEvent) {
        prepareQueueForEvent(gameEvent.getClass(), gameEvent.getTopic());
        queue.get(gameEvent.getClass()).get(gameEvent.getTopic()).add(gameEvent);
    }

    private <T extends GameEvent> void prepareQueueForEvent(Class<T> eventClass, String eventTopic) {
        queue.computeIfAbsent(eventClass, event -> new HashMap<>());
        queue.get(eventClass).computeIfAbsent(eventTopic, event -> new ArrayList<>());
    }

    public <T extends GameEvent> Optional<T> getEvent(Class<T> type) {
        return getEvent(type, DEFAULT_TOPIC);
    }

    @SuppressWarnings("unchecked")
    public <T extends GameEvent> Optional<T> getEvent(Class<T> type, String topic) {
        if (queue.containsKey(type) && queue.get(type).containsKey(topic)) {
            return of((T) queue.get(type).get(topic));
        } else {
            return empty();
        }
    }

    /**
     * this method should be called at the end of main loop of the application to provide housekeeping
     */
    public void update() {
        for (Class<? extends GameEvent> eventType : queue.keySet()) {
            for (String eventTopic : queue.get(eventType).keySet()) {
                updateEvents(eventType, eventTopic);
            }
        }
    }

    private void updateEvents(Class<? extends GameEvent> eventType, String eventTopic) {
        Iterator<GameEvent> iterator = queue.get(eventType).get(eventTopic).iterator();

        while (iterator.hasNext()) {
            GameEvent gameEvent = iterator.next();
            gameEvent.increaseAge();
            if (gameEvent.isTooOld()) {
                iterator.remove();
            }
        }
    }
}
