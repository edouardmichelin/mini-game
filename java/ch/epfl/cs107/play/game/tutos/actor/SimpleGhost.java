package ch.epfl.cs107.play.game.tutos.actor;

import ch.epfl.cs107.play.game.actor.Entity;
import ch.epfl.cs107.play.game.actor.TextGraphics;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

import java.awt.*;

public class SimpleGhost extends Entity {
    private static final float DEFAULT_HEALTH_POINTS = 10f;
    private static final float DEFAULT_SPEED = 0.05f;

    private float hp;
    private Sprite sprite;
    private TextGraphics hpText;

    /**
     * Default Entity constructor
     *
     * @param position (Coordinate): Initial position of the entity. Not null
     */
    public SimpleGhost(Vector position, String spriteName) {
        super(position);
        this.sprite = new Sprite(spriteName, 1, 1.f, this);
        this.hp = DEFAULT_HEALTH_POINTS;
        this.hpText = new TextGraphics(Integer.toString((int) this.hp), 0.4f, Color.BLUE);
        this.hpText.setParent(this);
        this.hpText.setAnchor(new Vector(-0.3f, 0.1f));
    }

    @Override
    public void draw(Canvas canvas) {
        this.sprite.draw(canvas);
        this.hpText.draw(canvas);
    }

    public void update(float deltaTime) {
        this.hp = this.hp - deltaTime <= 0 ? 0 : this.hp - deltaTime;
        this.hpText.setText(Integer.toString((int) this.hp));

    }

    public boolean isWeak() {
        return this.hp <= 0;
    }

    public void strengthen() {
        this.hp = DEFAULT_HEALTH_POINTS;
    }

    public void moveUp() {
        this.setCurrentPosition(this.getPosition().add(0.f, DEFAULT_SPEED));
    }

    public void moveDown() {
        this.setCurrentPosition(this.getPosition().add(0.f, -DEFAULT_SPEED));
    }

    public void moveLeft() {
        this.setCurrentPosition(this.getPosition().add(-DEFAULT_SPEED, 0f));
    }

    public void moveRight() {
        this.setCurrentPosition(this.getPosition().add(DEFAULT_SPEED, 0f));
    }
}
