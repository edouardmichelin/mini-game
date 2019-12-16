package ch.epfl.cs107.play.game.arpg.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.*;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.Projectile;
import ch.epfl.cs107.play.game.rpg.misc.DamageType;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

import java.util.List;

public class SwordSlash extends Projectile {
    private final static DamageType DAMAGE_TYPE = DamageType.PHYSICAL;
    private final static float DEFAULT_DAMAGE = 0.5f;

    private ARPGSwordSlashHandler interactionHandler;
    private Animation animations;

    static void consume(AreaEntity consumer, Area area) {
        DiscreteCoordinates position = consumer
                .getCurrentCells()
                .get(0)
                .jump(consumer.getOrientation().toVector());

        area.registerActor(new SwordSlash(area, consumer.getOrientation(), position));
    }

    public SwordSlash(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);

        Sprite[] sprite = {new Sprite("transparent", 1, 1, this)};
        this.animations = new Animation(1, sprite, false);

        this.interactionHandler = new ARPGSwordSlashHandler();
    }

    @Override
    protected Animation getAnimation() {
        return this.animations;
    }

    @Override
    protected int getRange() {
        return 0;
    }

    @Override
    protected int getSpeed() {
        return 1;
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

    private class ARPGSwordSlashHandler implements ARPGInteractionVisitor {

        @Override
        public void interactWith(Grass grass) {
            SwordSlash.this.inflictDamage(grass);
            SwordSlash.this.getOwnerArea().unregisterActor(SwordSlash.this);
        }

        @Override
        public void interactWith(DarkLord darkLord) {
            SwordSlash.this.inflictDamage(darkLord);
            SwordSlash.this.getOwnerArea().unregisterActor(SwordSlash.this);
        }

        @Override
        public void interactWith(LogMonster logMonster) {
            SwordSlash.this.inflictDamage(logMonster);
            SwordSlash.this.getOwnerArea().unregisterActor(SwordSlash.this);
        }

        @Override
        public void interactWith(FlameSkull flameSkull) {
            SwordSlash.this.inflictDamage(flameSkull);
            SwordSlash.this.getOwnerArea().unregisterActor(SwordSlash.this);
        }

        @Override
        public void interactWith(Bomb bomb) {
            bomb.explode();
            SwordSlash.this.getOwnerArea().unregisterActor(SwordSlash.this);
        }
    }
}
