package com.apptogo.runalien.event;

import java.util.*;

public class GameEventQueue {

    private Map<GameEventType, List<GameEvent>> queueMap = new HashMap<>();

    private static GameEventQueue instance;

    public static GameEventQueue getInstance() {
        if(instance == null) {
            instance = new GameEventQueue();
        }
        return instance;
    }

    private GameEventQueue() {
    }

    public void put(GameEvent event) {
        GameEventType eventType = event.getGameEventType();
        if(!queueMap.containsKey(eventType)) {
            queueMap.put(eventType, new LinkedList<>());
        }
        queueMap.get(eventType).add(event);
    }

    public GameEvent get(GameEventType eventType) {
        if(!queueMap.containsKey(eventType) || queueMap.get(eventType).isEmpty()) {
            return null;
        }
        return queueMap.get(eventType).remove(0);
    }
}
