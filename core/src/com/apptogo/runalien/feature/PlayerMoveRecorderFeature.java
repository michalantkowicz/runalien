package com.apptogo.runalien.feature;

import com.apptogo.runalien.event.GameEventService;
import com.apptogo.runalien.event.implementation.JumpMovementEvent;
import com.apptogo.runalien.event.implementation.SlideMovementEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class PlayerMoveRecorderFeature implements Feature {
    private final GameEventService eventService;
    private final String playerName;

    @Getter
    private List<MoveRecord> records = new LinkedList<>();

    @Override
    public void run(float delta) {
        Optional<JumpMovementEvent> jumpEvent = eventService.getEvent(JumpMovementEvent.class, playerName);
        Optional<SlideMovementEvent> slideEvent = eventService.getEvent(SlideMovementEvent.class, playerName);

        if (jumpEvent.isPresent()) {
            records.add(new MoveRecord(1, jumpEvent.get().getEventObject()));
        } else {
            if (slideEvent.isPresent()) {
                records.add(new MoveRecord(2, slideEvent.get().getEventObject()));
            }
        }
    }
}
