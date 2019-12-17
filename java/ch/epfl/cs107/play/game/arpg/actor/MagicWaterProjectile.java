package ch.epfl.cs107.play.game.arpg.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.*;
import ch.epfl.cs107.play.game.arpg.config.Settings;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.Projectile;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.game.rpg.misc.DamageType;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;

import java.util.List;

public class MagicWaterProjectile extends Projectile {
    private final static DamageType DAMAGE_TYPE = DamageType.MAGICAL;
    private final static float DEFAULT_DAMAGE = 1f;
    private final static int ANIMATION_DURATION = 4;

    private ARPGMagicWaterProjectileHandler interactionHandler;
    private Animation animations;

    public MagicWaterProjectile(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);

        this.interactionHandler = new ARPGMagicWaterProjectileHandler();

        Sprite[] sprites = new Sprite[4];
        for (int frame = 0; frame < 4; frame++) {
            sprites[frame] = new RPGSprite(
                    "zelda/magicWaterProjectile",
                    1,
                    1,
                    this,
                    new RegionOfInterest(frame * 32, 0, 32, 32)
            );
        }
        this.animations = new Animation(ANIMATION_DURATION, sprites, true);
    }

    @Override
    protected Animation getAnimation() {
        return animations;
    }

    @Override
    protected int getRange() {
        return 8;
    }

    @Override
    protected int getSpeed() {
        return 4;
    }

    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        return null;
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
        destroyable.damage(DEFAULT_DAMAGE, DAMAGE_TYPE);
    }

    private class ARPGMagicWaterProjectileHandler implements ARPGInteractionVisitor {

        @Override
        public void interactWith(Grass grass) {
            MagicWaterProjectile.this.inflictDamage(grass);
        }

        @Override
        public void interactWith(FireSpell fireSpell) {
            MagicWaterProjectile.this.inflictDamage(fireSpell);
        }

        @Override
        public void interactWith(DarkLord darkLord) {
            MagicWaterProjectile.this.inflictDamage(darkLord);
            MagicWaterProjectile.this.getOwnerArea().unregisterActor(MagicWaterProjectile.this);
        }

        @Override
        public void interactWith(LogMonster logMonster) {
            MagicWaterProjectile.this.inflictDamage(logMonster);
            MagicWaterProjectile.this.getOwnerArea().unregisterActor(MagicWaterProjectile.this);
        }

        @Override
        public void interactWith(FlameSkull flameSkull) {
            MagicWaterProjectile.this.inflictDamage(flameSkull);
            MagicWaterProjectile.this.getOwnerArea().unregisterActor(MagicWaterProjectile.this);
        }
    }
}
