package ch.epfl.cs107.play.game.arpg.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.*;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.config.SpriteNames;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.signal.Signal;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Canvas;

import java.util.List;

public class Orb extends AreaEntity implements Signal {
    private Animation animation;
    private boolean value;


    /**
     * Will take care of the bridge opening
     *
     * @param area        (Area): Owner area. Not null
     * @param orientation (Orientation): Initial orientation of the entity in the Area. Not null
     * @param position    (DiscreteCoordinate): Initial position of the entity in the Area. Not null
     */
    public Orb(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);

        this.animation = new Animation(10, this.getSprites());
    }

    private Sprite[] getSprites() {
        Sprite[] s = new Sprite[6];

        for (int frame = 0; frame < 6; frame++) {
            s[frame] = new RPGSprite(
                    SpriteNames.ORB,
                    1,
                    1,
                    this,
                    new RegionOfInterest(frame * 32, this.value ? 0 : 64, 32, 32)
            );
        }
        
        return s;
    }

    protected void turnOn() {
        this.value = true;
        this.animation = new Animation(10, this.getSprites());
    }

    @Override
    public void update(float deltaTime) {
        this.animation.update(deltaTime);
    }

    @Override
    public void draw(Canvas canvas) {
        this.animation.draw(canvas);
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return List.of(this.getCurrentMainCellCoordinates());
    }

    @Override
    public boolean takeCellSpace() {
        return false;
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
        ((ARPGInteractionVisitor) v).interactWith(this);
    }

    @Override
    public float getIntensity(float t) {
        return this.value ? 1 : 0;
    }
}
