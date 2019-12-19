package ch.epfl.cs107.play.game.arpg.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.*;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.config.Settings;
import ch.epfl.cs107.play.game.arpg.config.SpriteNames;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.items.CoinItem;
import ch.epfl.cs107.play.game.arpg.items.HeartItem;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.game.rpg.misc.DamageType;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RandomGenerator;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Grass extends AreaEntity implements Destroyable {
    private static final double PROBABILITY_TO_DROP_ITEM = 0.3;
    private static final double PROBABILITY_TO_DROP_HEART = 0.15;

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
                SpriteNames.GRASS,
                1,
                1,
                this,
                new RegionOfInterest(0, 0, 16, 16)
        );

        this.animation = new Animation(Settings.FRAME_RATE / 12, this.getSlicedSprites(), false);
    }

    private Sprite[] getSlicedSprites() {
        Sprite[] s = new Sprite[4];
        for (int frame = 0; frame < 4; frame++)
            s[frame] = new RPGSprite(
                    SpriteNames.GRASS_SLICED,
                    1,
                    1,
                    this,
                    new RegionOfInterest(frame * 32, 0, 32, 32)
            );

        return s;
    }

    @Override
    public void draw(Canvas canvas) {
        if (this.isAlive())
            this.sprite.draw(canvas);
        else
            this.animation.draw(canvas);
    }

    @Override
    public void update(float deltaTime) {
        if (!this.isAlive())
            this.animation.update(deltaTime);

        super.update(deltaTime);
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    @Override
    public boolean takeCellSpace() {
        return this.isAlive();
    }

    @Override
    public boolean isCellInteractable() {
        return this.isAlive();
    }

    @Override
    public boolean isViewInteractable() {
        return this.isAlive();
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v) {
        ((ARPGInteractionVisitor) v).interactWith(this);
    }

    @Override
    public float getHp() {
        return 0;
    }

    @Override
    public float getMaxHp() {
        return 0;
    }

    @Override
    public boolean isWeak() {
        return false;
    }

    @Override
    public void strengthen() {

    }

    @Override
    public List<DamageType> getWeaknesses() {
        return List.of(DamageType.FIRE, DamageType.PHYSICAL);
    }

    @Override
    public float damage(float damage, DamageType type) {
        if (this.getWeaknesses().contains(type))
            this.destroy();

        return 0;
    }

    @Override
    public void destroy() {
        this.onDying();
    }

    @Override
    public boolean isAlive() {
        return !this.isSliced;
    }

    @Override
    public void onDying() {
        this.isSliced = true;

        Random prng = RandomGenerator.getInstance();

        if (prng.nextDouble() < PROBABILITY_TO_DROP_ITEM)
            if (prng.nextDouble() < PROBABILITY_TO_DROP_HEART)
                HeartItem.drop(this, this.getOwnerArea());
            else
                CoinItem.drop(this, this.getOwnerArea());
    }
}
