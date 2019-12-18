package ch.epfl.cs107.play.game.arpg.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.*;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.config.SpriteNames;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.items.CastleKeyItem;
import ch.epfl.cs107.play.game.arpg.items.FlameSkullItem;
import ch.epfl.cs107.play.game.rpg.actor.Monster;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.game.rpg.misc.DamageType;
import ch.epfl.cs107.play.game.rpg.misc.Helpers;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RandomGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * TODO - On ne voit jamais la seconde animation
 * TODO - Problème des blocs invisibles
 */
public class DarkLord extends Monster {
    private static final int ANIMATION_DURATION = 8;
    private static final int ACTION_RADIUS = 3;
    private static final int TELEPORTATION_RADIUS = 3;
    private static final int MIN_SPELL_WAIT_DURATION = 200;
    private static final int MAX_SPELL_WAIT_DURATION = 500;

    private Animation[] idleAnimations;
    private Animation[] spellAnimations;
    private State state;
    private ARPGDarkLordHandler interactionHandler;
    private int simulationCyle;

    private int simulationStep = 0;

    public DarkLord(Area area, Orientation orientation, DiscreteCoordinates coordinates) {
        super(area, orientation, coordinates);

        this.idleAnimations = RPGSprite.createAnimations(ANIMATION_DURATION, this.getSprites(SpriteNames.DARK_LORD));
        this.spellAnimations = RPGSprite.createAnimations(ANIMATION_DURATION, this.getSprites(SpriteNames.DARK_LORD_SPELL), false);
        this.state = State.IDLE;
        this.interactionHandler = new ARPGDarkLordHandler();
        this.simulationCyle = Math.round((
                RandomGenerator
                        .getInstance()
                        .nextFloat() * (MAX_SPELL_WAIT_DURATION - MIN_SPELL_WAIT_DURATION)
        )) + MIN_SPELL_WAIT_DURATION;
    }

    private Sprite[][] getSprites(String spriteName) {
        return RPGSprite.extractSprites(
                spriteName,
                3,
                2,
                2,
                this,
                32,
                32,
                new Orientation[] {Orientation.UP, Orientation.LEFT, Orientation.DOWN, Orientation.RIGHT}
        );
    }

    private Orientation whereToThrowFireSpell() {
        DiscreteCoordinates position = this.getCurrentMainCellCoordinates();
        Area area = this.getOwnerArea();
        Random prng = RandomGenerator.getInstance();
        int maxAttemptAmount = 50;
        int attempt = 0;

        while(attempt < maxAttemptAmount) {
            int random = prng.nextInt(Orientation.values().length);
            for (Orientation orientation : Orientation.values()) {
                if (orientation.ordinal() == random && area.canEnterAreaCells(this, List.of(position.jump(orientation.toVector())))) {
                    return orientation;
                }
            }
            attempt++;
        }

        return this.getOrientation();
    }

    private void switchOrientation() {
        int randomIndex = RandomGenerator.getInstance().nextInt(Orientation.values().length);
        this.orientate(Orientation.values()[randomIndex]);
    }

    private boolean shouldSwitchOrientation() {
        return RandomGenerator.getInstance().nextDouble() < 0.4f;
    }

    private void act() {
        switch (this.state) {
            case ATTACKING: {
                this.state = State.IDLE;
            } break;
            case INVOKING: {
                Animation currentAnim = this.getCharacterAnimations()[this.getOrientation().ordinal()];
                if (currentAnim.isCompleted()) {
                    currentAnim.reset();
                    FlameSkullItem.consume(this, this.getOwnerArea());
                    this.state = State.IDLE;
                }
            } break;
            case PREPARING_TELEPORTATION: {
                Animation currentAnim = this.getCharacterAnimations()[this.getOrientation().ordinal()];
                if (currentAnim.isCompleted()) {
                    currentAnim.reset();
                    this.state = State.TELEPORTING;
                }
            } break;
            case TELEPORTING: {
                if (!this.isTargetReached()) return;
                DiscreteCoordinates position;
                Random prng = RandomGenerator.getInstance();
                List<DiscreteCoordinates> v = Helpers
                        .getCellsInRadius(this.getCurrentMainCellCoordinates(), TELEPORTATION_RADIUS);
                int attemptsLimit = 30;
                int attempt = 0;

                do {
                    position = v.get(prng.nextInt(v.size()));
                    attempt++;
                } while(
                        attempt < attemptsLimit &&
                        !this.getOwnerArea().canEnterAreaCells(this, List.of(position))
                );

                // TODO - position will never be null
                if (position != null) this.setCurrentPosition(position.toVector());
                this.state = State.IDLE;
            }
        }
    }

    @Override
    public void update(float deltaTime) {
        if (this.isAlive()) {
            this.simulationStep++;

            this.act();

            if (!this.state.equals(State.TELEPORTING))
                this.move(35);

            if (this.isTargetReached()) {
                if (!this.getOwnerArea().canEnterAreaCells(this, this.getNextCurrentCells()))
                    this.orientate(this.whereToThrowFireSpell());

                if (this.shouldSwitchOrientation() && !this.state.equals(State.PREPARING_TELEPORTATION))
                    this.switchOrientation();
            }

            if (this.simulationStep % this.simulationCyle == 0) {
                double random = RandomGenerator.getInstance().nextDouble();
                this.state = random > 0.7 ? State.ATTACKING : State.INVOKING;
                this.orientate(this.whereToThrowFireSpell());
            }
        }

        super.update(deltaTime);
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
    public List<DamageType> getWeaknesses() {
        return List.of(DamageType.MAGICAL);
    }

    @Override
    public float getMaxHp() {
        return 10f;
    }

    @Override
    public void onDying() {
        CastleKeyItem.drop(this, this.getOwnerArea());
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
        return Helpers.getCellsInRadius(this.getCurrentMainCellCoordinates(), TELEPORTATION_RADIUS);
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
        public void interactWith(ARPGPlayer player) {
            if (!DarkLord.this.state.equals(State.TELEPORTING)) {
                DarkLord.this.state = State.PREPARING_TELEPORTATION;
            }
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
