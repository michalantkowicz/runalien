package com.apptogo.runalien.app42;

@FunctionalInterface
public interface Callback {
    void success();
    default void failure(Exception e) {
        System.out.println(e.getMessage());
    }
}
