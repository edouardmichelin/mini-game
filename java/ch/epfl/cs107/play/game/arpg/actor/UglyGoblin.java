package ch.epfl.cs107.play.game.arpg.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.*;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.config.Settings;
import ch.epfl.cs107.play.game.arpg.config.SpriteNames;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.items.BowItem;
import ch.epfl.cs107.play.game.rpg.actor.Monster;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.game.rpg.misc.DamageType;
import ch.epfl.cs107.play.game.rpg.misc.Helpers;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RandomGenerator;
import ch.epfl.cs107.play.math.Vector;

import java.util.List;

/**
 * Will give us the bow once killed
 */
public class UglyGoblin extends Monster {
    private final static float DAMAGE_AMOUNT = 1.5f / Settings.FRAME_RATE;
    private final static DamageType DAMAGE_TYPE = DamageType.FIRE;
    private static final int ANIMATION_DURATION = Settings.FRAME_RATE / 10;

    private Animation[] animations;
    private ARPGFlameSkullHandler interactionHandler;

    public UglyGoblin(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);

        this.interactionHandler = new ARPGFlameSkullHandler();

        this.animations = RPGSprite.createAnimations(ANIMATION_DURATION, RPGSprite.extractSprites(
                SpriteNames.GOBLIN,
                4,
                1,
                2,
                this,
                16,
                32,
                new Orientation[]{Orientation.UP, Orientation.RIGHT, Orientation.DOWN, Orientation.LEFT}
        ));
    }

    @Override
    public void update(float deltaTime) {
        if (this.isAlive()) {

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
        return 2.5f;
    }

    @Override
    public List<DamageType> getWeaknesses() {
        return List.of(DamageType.PHYSICAL);
    }

    @Override
    public void onDying() {
        BowItem.drop(this, this.getOwnerArea());
    }

    @Override
    public boolean takeCellSpace() {
        return true;
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
        destroyable.damage(DAMAGE_AMOUNT, DAMAGE_TYPE);
    }

    private class ARPGFlameSkullHandler implements ARPGInteractionVisitor {

        @Override
        public void interactWith(ARPGPlayer player) {
            inflictDamage(player);
        }

    }
}
