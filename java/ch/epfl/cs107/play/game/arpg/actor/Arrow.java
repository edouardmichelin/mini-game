package ch.epfl.cs107.play.game.arpg.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.*;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.Monster;
import ch.epfl.cs107.play.game.rpg.actor.Projectile;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.game.rpg.misc.DamageType;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;

import java.util.List;

public class Arrow extends Projectile {
    private final static DamageType DAMAGE_TYPE = DamageType.PHYSICAL;
    private final static float DEFAULT_DAMAGE = 0.5f;

    private Animation[] animations;
    private ARPGArrowHandler interactionHandler;

    public Arrow(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);

        this.animations = new Animation[4];
        this.interactionHandler = new ARPGArrowHandler();

        for (Orientation direction : Orientation.values()) {
            this.animations[direction.ordinal()] = new Animation(1, new RPGSprite[]{
                    new RPGSprite(
                            "zelda/arrow",
                            1,
                            1,
                            this,
                            new RegionOfInterest(direction.ordinal() * 32, 0, 32, 32)
                    )
            });
        }
    }

    protected Animation getAnimation() {
        return animations[this.getOrientation().ordinal()];
    }


    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
    }

    @Override
    protected int getRange() {
        return 10;
    }

    @Override
    protected int getSpeed() {
        return 8;
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

    private class ARPGArrowHandler implements ARPGInteractionVisitor {

        @Override
        public void interactWith(Grass grass) {
            Arrow.this.inflictDamage(grass);
        }

        @Override
        public void interactWith(Bomb bomb) {
            bomb.explode();
            Arrow.this.getOwnerArea().unregisterActor(Arrow.this);
        }

        @Override
        public void interactWith(FireSpell fireSpell) {
            Arrow.this.inflictDamage(fireSpell);
            Arrow.this.getOwnerArea().unregisterActor(Arrow.this);
        }

        @Override
        public void interactWith(DarkLord darkLord) {
            Arrow.this.inflictDamage(darkLord);
            Arrow.this.getOwnerArea().unregisterActor(Arrow.this);
        }

        @Override
        public void interactWith(FlameSkull flameSkull) {
            Arrow.this.inflictDamage(flameSkull);
            Arrow.this.getOwnerArea().unregisterActor(Arrow.this);
        }

        @Override
        public void interactWith(LogMonster logMonster) {
            Arrow.this.inflictDamage(logMonster);
            Arrow.this.getOwnerArea().unregisterActor(Arrow.this);
        }
    }

}
