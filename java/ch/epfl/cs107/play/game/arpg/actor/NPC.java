package ch.epfl.cs107.play.game.arpg.actor;

import ch.epfl.cs107.play.game.actor.ImageGraphics;
import ch.epfl.cs107.play.game.actor.TextGraphics;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.*;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.areagame.io.ResourcePath;
import ch.epfl.cs107.play.game.arpg.config.Settings;
import ch.epfl.cs107.play.game.arpg.config.SpriteNames;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.Dialog;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.game.rpg.misc.Helpers;
import ch.epfl.cs107.play.game.rpg.misc.NPCProperties;
import ch.epfl.cs107.play.math.*;
import ch.epfl.cs107.play.window.Canvas;

import java.awt.*;
import java.util.Collections;
import java.util.List;

public class NPC extends MovableAreaEntity implements Interactor {
    private static final int DEPTH = 99999999;
    private static final String[] SPRITES = {SpriteNames.NPC_1, SpriteNames.NPC_2};
    private static final int ANIMATION_DURATION = Settings.FRAME_RATE / 10;
    private static final int MIN_SPEED = 2;
    private static final int MAX_SPEED = 4;
    private static final int MAX_INACTIVITY_TIME = Settings.FRAME_RATE / 2;

    private NPCProperties properties;
    private Animation[] animations;
    private int speed;
    private boolean isTalking;
    private Dialog dialog;
    private NPCHandler interactionHandler;
    private State state;
    private int inactivityCounter;

    /**
     * Default MovableAreaEntity constructor
     *
     * @param area        (Area): Owner area. Not null
     * @param orientation (Orientation): Initial orientation of the entity. Not null
     * @param position    (Coordinate): Initial position of the entity. Not null
     */
    public NPC(Area area, Orientation orientation, DiscreteCoordinates position, NPCProperties properties) {
        super(area, orientation, position);

        this.properties = properties;
        this.speed = Settings.FRAME_RATE / Helpers.random(MIN_SPEED, MAX_SPEED);
        this.interactionHandler = new NPCHandler();
        this.state = State.NORMAL;

        this.animations = RPGSprite.createAnimations(ANIMATION_DURATION, RPGSprite.extractSprites(
                SPRITES[RandomGenerator.getInstance().nextInt(SPRITES.length)],
                4,
                1,
                2,
                this,
                16,
                32,
                new Orientation[]{Orientation.UP, Orientation.RIGHT, Orientation.DOWN, Orientation.LEFT}
        ));

        this.dialog = new Dialog(this.properties.getMessage(), SpriteNames.DIALOG, this.getOwnerArea() );
    }

    private void switchOrientation() {
        int randomIndex = RandomGenerator.getInstance().nextInt(Orientation.values().length);
        this.orientate(Orientation.values()[randomIndex]);
    }

    private boolean shouldSwitchOrientation() {
        return RandomGenerator.getInstance().nextDouble() < 0.4f;
    }

    public void talk(AreaEntity source) {
        this.orientate(source.getOrientation().opposite());
        this.isTalking = true;
    }

    public void stopTalking() {
        this.isTalking = false;
    }

    private Animation getAnimation() {
        return this.animations[this.getOrientation().ordinal()];
    }

    private void drawDialogBox(Canvas canvas) {
        float width = canvas.getScaledWidth();
        float height = canvas.getScaledHeight();

        Vector anchor = canvas.getTransform().getOrigin().sub(new Vector(width / 2 - ((width  / 20)), height / 2 - height / 20));

       this.dialog.draw(canvas);
    }

    @Override
    public void update(float deltaTime) {
        if (this.state.equals(State.INACTIVE)) {
            this.inactivityCounter++;
            this.getAnimation().reset();
        }

        if (this.properties.canMove() && !this.state.equals(State.INACTIVE)) {
            this.move(this.speed);

            if (this.isTargetReached())
                if (
                        this.shouldSwitchOrientation() ||
                        !this.getOwnerArea().canEnterAreaCells(this, this.getNextCurrentCells())
                )
                    this.switchOrientation();

            this.getAnimation().update(deltaTime);
        }

        if (this.inactivityCounter % MAX_INACTIVITY_TIME == 0) this.state = State.NORMAL;

        super.update(deltaTime);
    }

    @Override
    public void draw(Canvas canvas) {
        this.getAnimation().draw(canvas);

        if (this.isTalking) drawDialogBox(canvas);
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(this.getCurrentMainCellCoordinates());
    }

    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        return this.getCurrentMainCellCoordinates().getNeighbours();
    }

    @Override
    public boolean wantsCellInteraction() {
        return false;
    }

    @Override
    public boolean wantsViewInteraction() {
        return this.properties.canMove();
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
        ((ARPGInteractionVisitor) v).interactWith(this);
    }

    private enum State {
        NORMAL,
        INACTIVE,
    }

    private class NPCHandler implements ARPGInteractionVisitor {
        @Override
        public void interactWith(ARPGPlayer player) {
            if (NPC.this.state.equals(State.NORMAL)) {
                NPC.this.orientate(player.getOrientation().opposite());
                NPC.this.state = State.INACTIVE;
            }
        }
    }
}
