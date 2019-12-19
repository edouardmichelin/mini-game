package ch.epfl.cs107.play.game.arpg.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.*;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.misc.DamageType;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Collections;
import java.util.List;

/**
 * When hitting with a sword, we actually use an invisible projectile that takes effect one cell in front of the player.
 */
public class SwordSlash extends AreaEntity implements Interactor, NotClipable {
    private final static DamageType DAMAGE_TYPE = DamageType.PHYSICAL;
    private final static float DEFAULT_DAMAGE = 0.5f;

    private ARPGSwordSlashHandler interactionHandler;
    private boolean consumed;

    public SwordSlash(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);

        this.interactionHandler = new ARPGSwordSlashHandler();
    }

    @Override
    public void update(float deltaTime) {
        if (this.consumed) this.getOwnerArea().unregisterActor(this);

        super.update(deltaTime);
    }

    @Override
    public void draw(Canvas canvas) {

    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        return List.of(this.getCurrentMainCellCoordinates().jump(this.getOrientation().toVector()));
    }

    @Override
    public boolean wantsCellInteraction() {
        return !this.consumed;
    }

    @Override
    public boolean wantsViewInteraction() {
        return false;
    }

    @Override
    public void interactWith(Interactable other) {
        other.acceptInteraction(this.interactionHandler);
    }


    @Override
    public boolean takeCellSpace() {
        return false;
    }

    @Override
    public boolean isCellInteractable() {
        return false;
    }

    @Override
    public boolean isViewInteractable() {
        return false;
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v) {
        ((ARPGInteractionVisitor) v).interactWith(this);
    }

    private void inflictDamage(Destroyable destroyable) {
        destroyable.damage(DEFAULT_DAMAGE, DAMAGE_TYPE);
    }

    private class ARPGSwordSlashHandler implements ARPGInteractionVisitor {

        @Override
        public void interactWith(Grass grass) {
            SwordSlash.this.inflictDamage(grass);
            SwordSlash.this.consumed = true;
        }

        @Override
        public void interactWith(DarkLord darkLord) {
            SwordSlash.this.inflictDamage(darkLord);
            SwordSlash.this.consumed = true;
        }

        @Override
        public void interactWith(LogMonster logMonster) {
            SwordSlash.this.inflictDamage(logMonster);
            SwordSlash.this.consumed = true;
        }

        @Override
        public void interactWith(FlameSkull flameSkull) {
            SwordSlash.this.inflictDamage(flameSkull);
            SwordSlash.this.consumed = true;
        }

        @Override
        public void interactWith(UglyGoblin goblin) {
            SwordSlash.this.inflictDamage(goblin);
            SwordSlash.this.consumed = true;
        }

        @Override
        public void interactWith(Bomb bomb) {
            bomb.explode();
            SwordSlash.this.consumed = true;
        }
    }
}
