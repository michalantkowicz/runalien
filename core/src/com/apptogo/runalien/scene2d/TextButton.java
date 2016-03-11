package com.apptogo.runalien.scene2d;

import com.apptogo.runalien.manager.ResourcesManager;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class TextButton extends com.badlogic.gdx.scenes.scene2d.ui.TextButton {
    public static TextButton get(String label, String buttonName) {
        return new TextButton(label, ResourcesManager.getInstance().skin, buttonName);
    }

    public static TextButton get(String label) {
        return new TextButton(label, ResourcesManager.getInstance().skin);
    }

    public TextButton(String label, Skin skin, String buttonName) {
        super(label, skin, buttonName);
        this.getLabelCell().padLeft(90).padBottom(14);
    }

    public TextButton(String label, Skin skin) {
        super(label, skin);
    }

    public TextButton position(float x, float y)
    {
        this.setPosition(x, y);
        return this;
    }

    public TextButton size(float width, float height)
    {
        this.setSize(width, height);

        return this;
    }

    public TextButton centerX()
    {
        this.setPosition(-this.getWidth() / 2f, this.getY());
        return this;
    }

    public TextButton centerY()
    {
        this.setPosition(this.getX(), -this.getHeight() / 2f);
        return this;
    }

    public TextButton setListener(EventListener listener)
    {
        this.addListener(listener);
        return this;
    }
}
