package ch.epfl.cs107.play.game.arpg.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.config.Settings;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RandomGenerator;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Grass extends AreaEntity {
    private static final double PROBABILITY_TO_DROP_ITEM = 0.5;
    private static final double PROBABILITY_TO_DROP_HEART = 0.3;
    private static final String TEX = "zelda/Grass";
    private static final String CUT_TEX = "zelda/grass.sliced";


    private RPGSprite sprite;
    private Animation animation;
    private boolean isSliced = false;

    /**
     * Default AreaEntity constructor
     *
     * @param area        (Area): Owner area. Not null
     * @param orientation (Orientation): Initial orientation of the entity in the Area. Not null
     * @param position    (DiscreteCoordinate): Initial position of the entity in the Area. Not null
     */
    public Grass(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);

        this.sprite = new RPGSprite(
                TEX,
                1,
                1,
                this,
                new RegionOfInterest(0, 0, 16, 16)
        );

        this.animation = new Animation(Settings.FRAME_RATE / 12, this.getCutSprites(), false);
    }

    private Sprite[] getCutSprites() {
        Sprite[] s = new Sprite[4];
        for (int frame = 0; frame < 4; frame++)
            s[frame] = new RPGSprite(
                    CUT_TEX,
                    1,
                    1,
                    this,
                    new RegionOfInterest(frame * 32, 0, 32, 32)
            );

        return s;
    }

    void cut() {
        this.isSliced = true;

        Random prng = RandomGenerator.getInstance();

        if (prng.nextDouble() < PROBABILITY_TO_DROP_ITEM)
            if (prng.nextDouble() < PROBABILITY_TO_DROP_HEART)
                Heart.drop(this, this.getOwnerArea());
            else
                Coin.drop(this, this.getOwnerArea());
    }

    @Override
    public void draw(Canvas canvas) {
        if (this.isSliced)
            this.animation.draw(canvas);
        else
            this.sprite.draw(canvas);
    }

    @Override
    public void update(float deltaTime) {
        if (this.isSliced)
            this.animation.update(deltaTime);

        super.update(deltaTime);
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    @Override
    public boolean takeCellSpace() {
        return !this.isSliced;
    }

    @Override
    public boolean isCellInteractable() {
        return !this.isSliced;
    }

    @Override
    public boolean isViewInteractable() {
        return !this.isSliced;
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v) {
        ((ARPGInteractionVisitor) v).interactWith(this);
    }
}
