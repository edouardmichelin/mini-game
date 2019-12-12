package ch.epfl.cs107.play.game.arpg.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.*;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.area.ARPGBehavior;
import ch.epfl.cs107.play.game.arpg.config.Settings;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Collections;
import java.util.List;

public class Bomb extends AreaEntity implements Interactor {
    private final static int DEFAULT_COUNTDOWN = 5 * Settings.FRAME_RATE;

    private int countdown;
    private Animation animation;
    private ARPGBombHandler interactionHandler;

    /**
     * Default AreaEntity constructor
     *
     * @param area        (Area): Owner area. Not null
     * @param orientation (Orientation): Initial orientation of the entity in the Area. Not null
     * @param position    (DiscreteCoordinate): Initial position of the entity in the Area. Not null
     */
    public Bomb(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);

        Sprite sprites[] = new Sprite[2];
        for (int frame = 0; frame < 2; frame++) {
            sprites[frame] = new RPGSprite("zelda/bomb", 1, 1, this,
                    new RegionOfInterest(frame * 16, 0, 16, 16));
        }

        this.animation = new Animation(
                Settings.FRAME_RATE / 3,
                sprites,
                true);

        this.countdown = DEFAULT_COUNTDOWN;

        this.interactionHandler = new ARPGBombHandler();
    }

    private void explode() {
        this.animation.reset();
        Sprite sprites[] = new Sprite[7];
        for (int frame = 0; frame < 7; frame++) {
            sprites[frame] = new RPGSprite("zelda/explosion", 1, 1, this,
                    new RegionOfInterest(frame * 32, 0, 32, 32));
        }
        this.animation = new Animation(Settings.FRAME_RATE / 18, sprites, true);
    }

    @Override
    public void update(float deltaTime) {
        if (this.countdown < - (Settings.FRAME_RATE / 4)) {
            this.getOwnerArea().unregisterActor(this);
            return;
        }

        this.countdown = this.countdown - 1;
        this.animation.update(deltaTime);
        super.update(deltaTime);
    }

    @Override
    public void draw(Canvas canvas) {
        this.animation.draw(canvas);
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        return getCurrentMainCellCoordinates().getNeighbours();
    }

    @Override
    public boolean wantsCellInteraction() {
        return countdown == 0;
    }

    @Override
    public boolean wantsViewInteraction() {
        return countdown == 0;
    }

    @Override
    public void interactWith(Interactable other) {
        this.explode();
        other.acceptInteraction(this.interactionHandler);
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

    private class ARPGBombHandler implements ARPGInteractionVisitor {

        @Override
        public void interactWith(Door door) {
        }

        @Override
        public void interactWith(ARPGBehavior.ARPGCell cell) {
        }

        @Override
        public void interactWith(ARPGPlayer player) {
        }

        @Override
        public void interactWith(Grass grass) {
            grass.cut();
        }

    }
}
