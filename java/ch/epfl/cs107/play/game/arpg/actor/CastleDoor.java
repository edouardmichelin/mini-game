package ch.epfl.cs107.play.game.arpg.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Canvas;

public class CastleDoor extends Door {
    private static final String CLOSE_SPRITE = "zelda/castleDoor.close";
    private static final String OPEN_SPRITE = "zelda/castleDoor.open";

    private Sprite sprite;

    public CastleDoor(String destination, DiscreteCoordinates otherSideCoordinates, Logic signal, Area area, Orientation orientation, DiscreteCoordinates position) {
        super(destination, otherSideCoordinates, signal, area, orientation, position);
    }

    public CastleDoor(String destination, DiscreteCoordinates otherSideCoordinates, Logic signal, Area area, Orientation orientation, DiscreteCoordinates position, DiscreteCoordinates... otherCells) {
        super(destination, otherSideCoordinates, signal, area, orientation, position, otherCells);

        this.sprite = this.getSprite();
    }

    private Sprite getSprite() {
        return new Sprite(
                this.isOpen() ? OPEN_SPRITE : CLOSE_SPRITE,
                1,
                1,
                this,
                new RegionOfInterest(0, 0, 32, 32),
                this.getPosition()
        );
    }

    protected void close() {
        this.sprite = this.getSprite();
        this.setSignal(Logic.FALSE);
    }

    protected void open() {
        this.sprite = this.getSprite();
        this.setSignal(Logic.TRUE);
    }

    @Override
    public void draw(Canvas canvas) {
        this.sprite.draw(canvas);
    }

    @Override
    public boolean takeCellSpace() {
        return !this.isOpen();
    }

    @Override
    public boolean isViewInteractable() {
        return !this.isOpen();
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v) {
        ((ARPGInteractionVisitor) v).interactWith(this);
    }

}
