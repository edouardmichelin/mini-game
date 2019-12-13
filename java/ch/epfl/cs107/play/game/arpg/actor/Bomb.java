package ch.epfl.cs107.play.game.arpg.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Interactor;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.areagame.io.ResourcePath;
import ch.epfl.cs107.play.game.arpg.area.ARPGBehavior;
import ch.epfl.cs107.play.game.arpg.config.Settings;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Collections;
import java.util.List;

public class Bomb extends AreaEntity implements Interactor {
    private final static int DEFAULT_COUNTDOWN = 5 * Settings.FRAME_RATE;
    private final static float DEFAULT_DAMAGE = 2.5f;

    private int countdown;
    private RPGSprite sprite;
    private ARPGBombHandler interactionHandler;

    public static void consume(AreaEntity consumer, Area area) {
        DiscreteCoordinates position = consumer
                .getCurrentCells()
                .get(0)
                .jump(consumer.getOrientation().toVector());

        area.registerActor(new Bomb(area, Orientation.DOWN, position));
    }

    /**
     * Default AreaEntity constructor
     *
     * @param area        (Area): Owner area. Not null
     * @param orientation (Orientation): Initial orientation of the entity in the Area. Not null
     * @param position    (DiscreteCoordinate): Initial position of the entity in the Area. Not null
     */
    public Bomb(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);

        this.countdown = DEFAULT_COUNTDOWN;

        this.sprite = new RPGSprite("zelda/bomb", 1, 1, this);
        this.interactionHandler = new ARPGBombHandler();
    }

    private void explode() {
        this.sprite.setName(ResourcePath.getSprite("zelda/explosion"));
    }

    @Override
    public void update(float deltaTime) {
        if (this.countdown < -1 * Settings.FRAME_RATE) {
            this.getOwnerArea().unregisterActor(this);
            return;
        }

        this.countdown = this.countdown - 1;
        super.update(deltaTime);
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
        return true;
    }

    @Override
    public boolean isCellInteractable() {
        return true;
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
        public void interactWith(ARPGBehavior.ARPGCell cell){
        }

        @Override
        public void interactWith(ARPGPlayer player){
            player.damage(DEFAULT_DAMAGE);
        }

        @Override
        public void interactWith(Grass grass) {
                grass.cut();
        }

    }
}
