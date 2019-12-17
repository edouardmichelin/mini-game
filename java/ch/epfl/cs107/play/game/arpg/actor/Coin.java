package ch.epfl.cs107.play.game.arpg.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.*;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RandomGenerator;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

public class Coin extends CollectibleAreaEntity {
    private static final int DEFAULT_VALUE = 20;

    private Animation animation;
    private int value;

    /**
     * Default AreaEntity constructor
     *
     * @param area        (Area): Owner area. Not null
     * @param orientation (Orientation): Initial orientation of the entity in the Area. Not null
     * @param position    (DiscreteCoordinate): Initial position of the entity in the Area. Not null
     */
    public Coin(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);

        double random = RandomGenerator.getInstance().nextDouble();
        float coefficient = random > 0.9f ? 2 : random > 0.6f ? 1 : 0.5f;

        this.value = Math.round(DEFAULT_VALUE * coefficient);

        this.animation = new Animation(
                10,
                this.getSprites(coefficient),
                true
        );
    }

    private Sprite[] getSprites(float coefficient) {
        Vector anchor = coefficient == 2 ?
                new Vector(-0.4f, 0) :
                coefficient == 1 ?
                        new Vector(0.1f, 0.1f) :
                        new Vector(0.3f, 0.2f);

        Sprite[] s = new Sprite[4];

        for (int frame = 0; frame < 4; frame++) {
            s[frame] = new RPGSprite("zelda/coin", coefficient, coefficient, this,
                    new RegionOfInterest(frame * 16, 0, 16, 16), anchor);
        }
        
        return s;
    }

    public int getValue() {
        return this.value;
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
    public void acceptInteraction(AreaInteractionVisitor v) {
        ((ARPGInteractionVisitor) v).interactWith(this);
    }
}
