package com.apptogo.runalien.game;

import java.util.HashMap;
import java.util.Map;

import com.apptogo.runalien.scene2d.Animation;
import com.apptogo.runalien.screen.GameScreen;
import com.apptogo.runalien.tools.UnitConverter;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;

public class GameActor extends Actor {

    private Body body;
    private Map<String, Animation> animations = new HashMap<String, Animation>();
    private Animation currentAnimation;
    private float customOffsetX, customOffsetY;

    public GameActor(String name) {
        setName(name);
        setDebug(true);
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        setPosition(body.getPosition().x + customOffsetX, body.getPosition().y + customOffsetY);
        setSize(currentAnimation.getWidth(), currentAnimation.getHeight());

        currentAnimation.position(getX(), getY());
        currentAnimation.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        currentAnimation.draw(batch, parentAlpha);
    }

    public void createBoxBody(BodyType bodyType, Vector2 size) {
        customOffsetX = -size.x;
        customOffsetY = -size.y;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = bodyType;

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(size.x, size.y);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;

        body = GameScreen.getWorld().createBody(bodyDef);
        body.createFixture(fixtureDef);

        body.setUserData(getName());
    }

    public void modifyCustomOffsets(float deltaX, float deltaY) {
        customOffsetX += deltaX;
        customOffsetY += deltaY;
    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public Map<String, Animation> getAnimations() {
        return animations;
    }

    public void setAnimations(Map<String, Animation> animations) {
        animations.forEach((k, v) -> v.scaleFrames(1 / UnitConverter.PPM));
        this.animations = animations;
    }

    public Animation getCurrentAnimation() {
        return currentAnimation;
    }

    public void setCurrentAnimation(String currentAnimationName) {
        this.currentAnimation = animations.get(currentAnimationName);
        this.currentAnimation.start();
    }

    public void setImage(TextureRegion texture) {
        this.animations.put("default", new Animation(0, new Array<TextureRegion>(new TextureRegion[]{texture})));
        setCurrentAnimation("default");
    }

}
