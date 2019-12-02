package ch.epfl.cs107.play.game.tutos.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.MovableAreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.tutos.Tuto2Behavior;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Button;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;

import java.util.Collections;
import java.util.List;

public class GhostPlayer extends MovableAreaEntity {
    private static final int DEFAULT_SPEED = 1;
    private final static int ANIMATION_DURATION = 8;

    private Keyboard keyboard;
    private Sprite sprite;
    private boolean isNextToDoor = false;

    /**
     * Default MovableAreaEntity constructor
     *
     * @param area        (Area): Owner area. Not null
     * @param orientation (Orientation): Initial orientation of the entity. Not null
     * @param position    (Coordinate): Initial position of the entity. Not null
     */
    public GhostPlayer(Area area, Orientation orientation, DiscreteCoordinates position, String spriteName) {
        super(area, orientation, position);
        this.sprite = new Sprite(spriteName, 1f, 1f, this);
        this.enterArea(area, position);
    }

    private void initKeyboard(Area area) {
        this.keyboard = area.getKeyboard();
    }

    public void enterArea(Area area, DiscreteCoordinates position) {
        initKeyboard(area);
        area.registerActor(this);
        this.setOwnerArea(area);
        this.setCurrentPosition(position.toVector());
        area.setViewCandidate(this);
        resetMotion();
    }

    public void leaveArea(Area area) {
        area.unregisterActor(this);
        // this.setOwnerArea(null);
    }

    public void update(float deltaTime) {

        if (this.keyboard.get(Keyboard.UP).isDown()) this.moveUp();
        if (this.keyboard.get(Keyboard.DOWN).isDown()) this.moveDown();
        if (this.keyboard.get(Keyboard.LEFT).isDown()) this.moveLeft();
        if (this.keyboard.get(Keyboard.RIGHT).isDown()) this.moveRight();

        // this.isNextToDoor = this.getCurrentCells().contains(this.).get(0).toVector();

        super.update(deltaTime);
    }

    public boolean getIsNextToDoor() {
        return this.isNextToDoor;
    }

    @Override
    public void draw(Canvas canvas) {
        this.sprite.draw(canvas);
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
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

    public void moveUp() {
        if (!this.getOrientation().equals(Orientation.UP))
            this.orientate(Orientation.UP);
        else
            this.move(ANIMATION_DURATION, DEFAULT_SPEED);
    }

    public void moveDown() {
        if (!this.getOrientation().equals(Orientation.DOWN))
            this.orientate(Orientation.DOWN);
        else
            this.move(ANIMATION_DURATION, DEFAULT_SPEED);
    }

    public void moveLeft() {
        if (!this.getOrientation().equals(Orientation.LEFT))
            this.orientate(Orientation.LEFT);
        else
            this.move(ANIMATION_DURATION, DEFAULT_SPEED);
    }

    public void moveRight() {
        if (!this.getOrientation().equals(Orientation.RIGHT))
            this.orientate(Orientation.RIGHT);
        else
            this.move(ANIMATION_DURATION, DEFAULT_SPEED);
    }
}
