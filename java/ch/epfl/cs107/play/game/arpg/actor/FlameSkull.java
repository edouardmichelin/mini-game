package ch.epfl.cs107.play.game.arpg.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.*;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.config.Settings;
import ch.epfl.cs107.play.game.arpg.config.SpriteNames;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.Monster;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.game.rpg.misc.DamageType;
import ch.epfl.cs107.play.game.rpg.misc.Helpers;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RandomGenerator;
import ch.epfl.cs107.play.math.Vector;

import java.util.List;

public class FlameSkull extends Monster implements FlyableEntity {
    private final static int MIN_LIFE_TIME = Settings.FRAME_RATE * 2;
    private final static int MAX_LIFE_TIME = Settings.FRAME_RATE * 10;
    private final static DamageType DAMAGE_TYPE = DamageType.FIRE;

    private Animation[] animations;
    private int lifeTime;
    private int simulationStep;
    private ARPGFlameSkullHandler interactionHandler;

    public FlameSkull(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);

        this.interactionHandler = new ARPGFlameSkullHandler();
        this.lifeTime = Helpers.random(MIN_LIFE_TIME, MAX_LIFE_TIME);
        this.animations = RPGSprite.createAnimations(12, getSprites());
    }

    private Sprite[][] getSprites() {
        return RPGSprite.extractSprites(
                SpriteNames.FLAME_SKULL,
                3,
                1.8f,
                1.8f,
                this,
                32,
                32,
                new Vector(-0.45f, 0.2f),
                new Orientation[] {Orientation.UP, Orientation.LEFT, Orientation.DOWN, Orientation.RIGHT}
        );
    }

    @Override
    public void update(float deltaTime) {
        if (this.isAlive()) {
            this.simulationStep++;

            if (this.simulationStep == this.lifeTime)
                this.destroy();

            if (this.shouldSwitchOrientation() && this.isTargetReached())
                this.switchOrientation();
            else
                this.move(25);

            if (this.isTargetReached())
                if (!this.getOwnerArea().canEnterAreaCells(this, this.getNextCurrentCells()))
                    this.switchOrientation();
        }

        super.update(deltaTime);
    }

    @Override
    protected Animation[] getCharacterAnimations() {
        return this.animations;
    }

    @Override
    public float getMaxHp() {
        return 1;
    }

    @Override
    public List<DamageType> getWeaknesses() {
        return List.of(DamageType.MAGICAL, DamageType.PHYSICAL);
    }

    @Override
    public void onDying() {

    }

    @Override
    public boolean takeCellSpace() {
        return false;
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v) {
        ((ARPGInteractionVisitor) v).interactWith(this);
    }

    @Override
    public boolean wantsCellInteraction() {
        return true;
    }

    @Override
    public boolean wantsViewInteraction() {
        return false;
    }

    @Override
    public void interactWith(Interactable other) {
        other.acceptInteraction(this.interactionHandler);
    }

    private void inflictDamage(Destroyable destroyable) {
        destroyable.damage(5f / Settings.FRAME_RATE, DAMAGE_TYPE);
    }

    private class ARPGFlameSkullHandler implements ARPGInteractionVisitor {

        @Override
        public void interactWith(ARPGPlayer player) {
            inflictDamage(player);
        }

        @Override
        public void interactWith(LogMonster logMonster) {
            inflictDamage(logMonster);
        }

        @Override
        public void interactWith(Grass grass) {
            inflictDamage(grass);
        }

        @Override
        public void interactWith(Bomb bomb) {
            bomb.explode();
        }

    }
}
