package com.apptogo.runalien.event;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameEventQueue {
    private Map<Class<? extends GameEvent>, Map<String, List<GameEvent>>> queue = new HashMap<>();


}
