package ch.epfl.cs107.play.game.arpg.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Canvas;

public class CastleDoor extends Door {
    private static final String CLOSE_SPRITE = "zelda/castleDoor.close";
    private static final String OPEN_SPRITE = "zelda/castleDoor.open";

    private RPGSprite sprite;

    public CastleDoor(String destination, DiscreteCoordinates otherSideCoordinates, Logic signal, Area area, Orientation orientation, DiscreteCoordinates position) {
        super(destination, otherSideCoordinates, signal, area, orientation, position);
    }

    public CastleDoor(String destination, DiscreteCoordinates otherSideCoordinates, Logic signal, Area area, Orientation orientation, DiscreteCoordinates position, DiscreteCoordinates... otherCells) {
        super(destination, otherSideCoordinates, signal, area, orientation, position, otherCells);

        this.sprite = this.getSprite();
    }

    private RPGSprite getSprite() {
        return new RPGSprite(
                this.isOpen() ? OPEN_SPRITE : CLOSE_SPRITE,
                2,
                2,
                this,
                new RegionOfInterest(0, 0, 32, 32)
        );
    }

    protected void close() {
        this.setSignal(Logic.FALSE);
        this.sprite = this.getSprite();
    }

    protected void open() {
        this.setSignal(Logic.TRUE);
        this.sprite = this.getSprite();
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
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
