package ch.epfl.cs107.play.game.arpg.actor;

import ch.epfl.cs107.play.game.actor.TextGraphics;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.area.ARPGBehavior;
import ch.epfl.cs107.play.game.arpg.config.Keys;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.game.rpg.actor.Player;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;

import java.awt.*;
import java.util.Collections;
import java.util.List;

public class ARPGPlayer extends Player {
    private final static int ANIMATION_DURATION = 8;
    private final static float DEFAULT_HEALTH_POINTS = 100f;

    private ARPGPlayerHandler interactionHandler;
    private Keyboard keyboard;
    private Animation[] animations;
    private TextGraphics hpText;
    private float hp = DEFAULT_HEALTH_POINTS;

    /**
     * Default MovableAreaEntity constructor
     *
     * @param area        (Area): Owner area. Not null
     * @param orientation (Orientation): Initial orientation of the entity. Not null
     * @param position    (Coordinate): Initial position of the entity. Not null
     */
    public ARPGPlayer(Area area, Orientation orientation, DiscreteCoordinates position, String spriteName) {
        super(area, orientation, position);

        Sprite[][] sprites = RPGSprite.extractSprites(
                spriteName,
                4,
                1,
                2,
                this,
                16,
                32,
                new Orientation[] {Orientation.DOWN, Orientation.RIGHT, Orientation.UP, Orientation.LEFT}
        );

        this.animations = RPGSprite.createAnimations(ANIMATION_DURATION / 2, sprites);

        this.keyboard = area.getKeyboard();
        this.hpText = initHpText(Color.WHITE);
        this.interactionHandler = new ARPGPlayerHandler();
    }

    private Animation getAnimation() {
        return this.animations[this.getOrientation().ordinal()];
    }

    private TextGraphics initHpText(Color color) {
        TextGraphics hpt = new TextGraphics(Integer.toString((int) this.hp), 0.4f, color);
        hpt.setParent(this);
        hpt.setAnchor(new Vector(-0.3f, 0.1f));

        return hpt;
    }

    private void decreaseHp(float damage) {
        this.hp = this.hp - damage <= 0 ? 0 : this.hp - damage;
        this.hpText.setText(Integer.toString((int) this.hp));
    }

    public void update(float deltaTime) {
        this.decreaseHp(deltaTime);

        if (this.keyboard.get(Keys.MOVE_UP).isDown()) this.move(Orientation.UP);
        if (this.keyboard.get(Keys.MOVE_DOWN).isDown()) this.move(Orientation.DOWN);
        if (this.keyboard.get(Keys.MOVE_LEFT).isDown()) this.move(Orientation.LEFT);
        if (this.keyboard.get(Keys.MOVE_RIGHT).isDown()) this.move(Orientation.RIGHT);

        this.getAnimation().update(deltaTime);

        super.update(deltaTime);
    }

    @Override
    public void draw(Canvas canvas) {
        Animation currentAnimation = this.getAnimation();

        if (this.isDisplacementOccurs()) {
            currentAnimation.draw(canvas);
        } else {
            currentAnimation.reset();
            currentAnimation.draw(canvas);
        }

        this.hpText.draw(canvas);
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        return Collections.singletonList (getCurrentMainCellCoordinates().jump(getOrientation().toVector()));
    }

    @Override
    public boolean wantsCellInteraction() {
        return true;
    }

    @Override
    public boolean wantsViewInteraction() {
        return true;
    }

    @Override
    public void interactWith(Interactable other) {
        other.acceptInteraction(this.interactionHandler);
    }

    @Override
    public boolean takeCellSpace() {
        return true;
    }

    @Override
    public boolean isCellInteractable() {
        return true;
    }

    @Override
    public boolean isViewInteractable() {
        return true;
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v) {
    }

    public boolean isWeak() {
        return this.hp <= 0;
    }

    public void strengthen() {
        this.hp = DEFAULT_HEALTH_POINTS;
    }

    private void move(Orientation orientation) {
        if (this.getOrientation().equals(orientation))
            if (keyboard.get(Keys.RUN).isDown())
                this.move(ANIMATION_DURATION / 2, 0);
            else
                this.move(ANIMATION_DURATION, 0);
        else
            this.orientate(orientation);
    }

    private boolean isInteractionKeyPressed() {
        return this.keyboard.get(Keys.INTERACTION_KEY)
                .isPressed();
    }

    private class ARPGPlayerHandler implements ARPGInteractionVisitor {

        @Override
        public void interactWith(Door door) {
            setIsPassingADoor(door);
        }

        @Override
        public void interactWith(ARPGBehavior.ARPGCell cell){
        }

        @Override
        public void interactWith(ARPGPlayer player){
        }

        @Override
        public void interactWith(Grass grass) {
            if (isInteractionKeyPressed())
                grass.cut();
        }

    }
}
