package ch.epfl.cs107.play.game.arpg.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.*;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.config.Settings;
import ch.epfl.cs107.play.game.rpg.actor.Monster;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.game.rpg.misc.DamageType;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RandomGenerator;
import ch.epfl.cs107.play.math.Vector;

import java.util.List;

public class FlameSkull extends Monster {
    private static final int ANIMATION_DURATION = Settings.FRAME_RATE / 2;
    private static final int MAX_LIFE_TIME = 350;
    private static final int MIN_LIFE_TIME = 100;

    private Animation[] animations;

    public static void consume(AreaEntity consumer, Area area) {
        DiscreteCoordinates position = consumer
                .getCurrentCells()
                .get(0)
                .jump(consumer.getOrientation().toVector());

        area.registerActor(new FlameSkull(area, consumer.getOrientation(), position));
    }

    public FlameSkull(Area area, Orientation orientation, DiscreteCoordinates coordinates) {
        super(area, orientation, coordinates);
        this.animations = RPGSprite.createAnimations(4, this.getSprites("zelda/flameSkull"));
    }

    private Orientation randomDirection() {
        int random = RandomGenerator.getInstance().nextInt(100);
        if (random >= 60) {
            random = RandomGenerator.getInstance().nextInt(4);
            Orientation orientation = random == 4 ? Orientation.UP : random == 3 ? Orientation.LEFT : random == 2 ?
                    Orientation.RIGHT : Orientation.DOWN;
            return orientation;
        }
        return this.getOrientation();
    }

    private Sprite[][] getSprites(String spriteName) {
        Sprite[][] sprites = RPGSprite.extractSprites(
                "zelda/flameSkull",
                3,
                1.8f,
                1.8f,
                this,
                32,
                32,
                new Vector(-0.45f, 0.2f),
                new Orientation[]{Orientation.UP, Orientation.LEFT, Orientation.DOWN, Orientation.RIGHT}
        );
        return sprites;
    }

    @Override
    public void update(float deltaTime) {
        this.damage(1, DamageType.MAGICAL);
        this.getOwnerArea().canEnterAreaCells(this, this.getNextCurrentCells());
        this.move(ANIMATION_DURATION);
        super.update(deltaTime);
    }

    @Override
    protected Animation[] getCharacterAnimations() {
        return this.animations;
    }

    @Override
    public List<DamageType> getWeaknesses() {
        return List.of(DamageType.MAGICAL, DamageType.PHYSICAL);
    }

    @Override
    public float getMaxHp() {
        return RandomGenerator.getInstance().nextInt(MAX_LIFE_TIME - MIN_LIFE_TIME) + MIN_LIFE_TIME;
    }

    @Override
    public void onDying() {
        // TODO add condition
        Coin.drop(this, this.getOwnerArea());
    }

    @Override
    public boolean takeCellSpace() {
        return false;
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v) {

    }

    @Override
    public boolean wantsCellInteraction() {
        return false;
    }

    @Override
    public boolean wantsViewInteraction() {
        return false;
    }

    @Override
    public void interactWith(Interactable other) {

    }
}
