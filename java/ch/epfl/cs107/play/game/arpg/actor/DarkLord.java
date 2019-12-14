package ch.epfl.cs107.play.game.arpg.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.Monster;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.game.rpg.misc.DamageType;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RandomGenerator;
import ch.epfl.cs107.play.math.Vector;

import java.util.List;

public class DarkLord extends Monster {
    private static final int MIN_SPELL_WAIT_DURATION = 100;
    private static final int MAX_SPELL_WAIT_DURATION = 300;
    private static final int TELEPORTATION_RADIUS = 3;

    private Animation[] idleAnimations;
    private Animation[] spellAnimations;
    private State state;
    private ARPGDarkLordHandler interactionHandler;
    private int simulationCyle;

    private int simulationStep = 0;

    public DarkLord(Area area, Orientation orientation, DiscreteCoordinates coordinates) {
        super(area, orientation, coordinates);

        this.idleAnimations = RPGSprite.createAnimations(4, this.getSprites("zelda/darkLord"));
        this.spellAnimations = RPGSprite.createAnimations(4, this.getSprites("zelda/darkLord.spell"));
        this.state = State.IDLE;
        this.interactionHandler = new ARPGDarkLordHandler();
        this.simulationCyle = Math.round((RandomGenerator.getInstance().nextFloat() * (MAX_SPELL_WAIT_DURATION - MIN_SPELL_WAIT_DURATION))) + MIN_SPELL_WAIT_DURATION;
    }

    private Sprite[][] getSprites(String spriteName) {
        return RPGSprite.extractSprites(
                spriteName,
                4,
                1,
                2,
                this,
                16,
                32,
                new Orientation[] {Orientation.DOWN, Orientation.RIGHT, Orientation.UP, Orientation.LEFT}
        );
    }

    // WIP
    private Orientation whereToThrowFireSpell() {
        DiscreteCoordinates position = this.getCurrentMainCellCoordinates();
        Area area = this.getOwnerArea();
        for (Orientation orientation : Orientation.values()) {
            if (area.canEnterAreaCells(this, List.of(position.jump(orientation.toVector())))) {
                return orientation;
            }
        }

        return this.getOrientation();
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        this.simulationStep++;

        if (this.simulationStep % this.simulationCyle == 0) {
            double random = RandomGenerator.getInstance().nextDouble();
            this.state = random > 0.7 ? State.ATTACKING : State.INVOKING;
        }
    }

    @Override
    protected Animation[] getCharacterAnimations() {
        switch (this.state) {
            case IDLE:
            case TELEPORTING: return this.idleAnimations;
            case INVOKING:
            case ATTACKING:
            case PREPARING_TELEPORTATION: return this.spellAnimations;
        }

        return this.idleAnimations;
    }

    @Override
    protected List<DamageType> getWeaknesses() {
        return List.of(DamageType.MAGICAL);
    }

    @Override
    public float getMaxHp() {
        return 10f;
    }

    @Override
    public void onDying() {
        CastleKey.drop(this, this.getOwnerArea());
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v) {
        ((ARPGInteractionVisitor) v).interactWith(this);
    }

    @Override
    public boolean wantsCellInteraction() {
        return false;
    }

    @Override
    public boolean wantsViewInteraction() {
        return this.isAlive();
    }

    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        return getCurrentMainCellCoordinates().getNeighbours();
    }

    @Override
    public void interactWith(Interactable other) {
        other.acceptInteraction(this.interactionHandler);
    }

    private class ARPGDarkLordHandler implements ARPGInteractionVisitor {
        @Override
        public void interactWith(Interactable other) {
            //
        }

        @Override
        public void interactWith(ARPGPlayer player){
            DarkLord.this.state = State.PREPARING_TELEPORTATION;
        }

    }

    private enum State {
        IDLE,
        ATTACKING,
        INVOKING,
        PREPARING_TELEPORTATION,
        TELEPORTING
    }
}
