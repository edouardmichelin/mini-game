package ch.epfl.cs107.play.game.rpg.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.*;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Collections;
import java.util.List;

public abstract class Projectile extends MovableAreaEntity implements FlyableEntity, Interactor {
    private static final int SPEED_SCALE = 50;

    private int range;
    private int speed;

    /**
     * Default MovableAreaEntity constructor
     *
     * @param area        (Area): Owner area. Not null
     * @param orientation (Orientation): Initial orientation of the entity. Not null
     * @param position    (Coordinate): Initial position of the entity. Not null
     */
    public Projectile(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);
        this.range = this.getRange();
        this.speed = this.getSpeed();
    }

    protected abstract Animation getAnimation();

    protected abstract int getRange();

    protected abstract int getSpeed();

    @Override
    public void update(float deltaTime) {

        this.getAnimation().update(deltaTime);
        this.move(SPEED_SCALE / this.getSpeed());
        super.update(deltaTime);

        if (this.isTargetReached()) {
            this.range--;
        }

        if (this.range <= 0 || !this.getOwnerArea().canEnterAreaCells(this, this.getNextCurrentCells())) {
            this.getOwnerArea().unregisterActor(this);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        this.getAnimation().draw(canvas);
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    @Override
    public boolean takeCellSpace() {
        return false;
    }

    @Override
    public boolean isCellInteractable() {
        return false;
    }

    @Override
    public boolean isViewInteractable() {
        return false;
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v) {

    }
}
