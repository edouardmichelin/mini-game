package ch.epfl.cs107.play.game.arpg.actor;

import ch.epfl.cs107.play.game.actor.TextGraphics;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.area.ARPGBehavior;
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
    private Sprite[][] sprites;
    private Animation[] animations;
    private TextGraphics hpText;
    private float hp = DEFAULT_HEALTH_POINTS;
    private int playerDirection;

    /**
     * Default MovableAreaEntity constructor
     *
     * @param area        (Area): Owner area. Not null
     * @param orientation (Orientation): Initial orientation of the entity. Not null
     * @param position    (Coordinate): Initial position of the entity. Not null
     */
    public ARPGPlayer(Area area, Orientation orientation, DiscreteCoordinates position, String spriteName) {
        super(area, orientation, position);

        sprites = RPGSprite.extractSprites(spriteName, 4, 1, 2,
                this, 16, 32, new Orientation[]{Orientation.DOWN, Orientation.RIGHT,
                        Orientation.UP, Orientation.LEFT});

        animations = RPGSprite.createAnimations(ANIMATION_DURATION / 2, sprites);
        this.hpText = initHpText(Color.WHITE);
        this.interactionHandler = new ARPGPlayerHandler();
        this.keyboard = area.getKeyboard();
        this.playerDirection = 2; //Looking down by default when created

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

        if (this.keyboard.get(Keyboard.UP).isDown()) this.move(Orientation.UP);
        if (this.keyboard.get(Keyboard.DOWN).isDown()) this.move(Orientation.DOWN);
        if (this.keyboard.get(Keyboard.LEFT).isDown()) this.move(Orientation.LEFT);
        if (this.keyboard.get(Keyboard.RIGHT).isDown()) this.move(Orientation.RIGHT);
        this.animations[playerDirection].update(deltaTime);
        super.update(deltaTime);
    }

    @Override
    public void draw(Canvas canvas) {
        if (this.isDisplacementOccurs()) {
            this.animations[playerDirection].draw(canvas);
        } else {
            this.animations[playerDirection].reset();
            this.animations[playerDirection].draw(canvas);
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
        return false;
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
        return false;
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
        this.playerDirection = orientation.ordinal();
        if (this.getOrientation().equals(orientation))
            this.move(ANIMATION_DURATION, 0);
        else
            this.orientate(orientation);
    }

    private class ARPGPlayerHandler implements ARPGInteractionVisitor {

        @Override
        public void interactWith(Door door) {
            setIsPassingADoor(door);
        }

        public void interactWith(ARPGBehavior.ARPGCell cell){
            // by default the interaction is empty
        }

        public void interactWith(ARPGPlayer player){
            // by default the interaction is empty
        }

    }
}
