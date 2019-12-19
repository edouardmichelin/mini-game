package ch.epfl.cs107.play.game.arpg.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.*;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.config.Settings;
import ch.epfl.cs107.play.game.arpg.config.SpriteNames;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.items.CastleKeyItem;
import ch.epfl.cs107.play.game.arpg.items.FireSpellItem;
import ch.epfl.cs107.play.game.arpg.items.FlameSkullItem;
import ch.epfl.cs107.play.game.rpg.actor.Monster;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.game.rpg.misc.DamageType;
import ch.epfl.cs107.play.game.rpg.misc.Helpers;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RandomGenerator;
import ch.epfl.cs107.play.math.Vector;

import java.util.List;
import java.util.Random;


/**
 * TODO - On ne voit jamais la seconde animation
 * TODO - Problème des blocs invisibles
 */
public class DarkLord extends Monster {
    private static final int ANIMATION_DURATION = Settings.FRAME_RATE / 4;
    private static final int ACTION_RADIUS = 3;
    private static final int TELEPORTATION_RADIUS = 3;
    private static final int MIN_SPELL_WAIT_DURATION = 200;
    private static final int MAX_SPELL_WAIT_DURATION = 500;
    private final static int MIN_INACTIVITY_DURATION = 2 * Settings.FRAME_RATE;
    private final static int MAX_INACTIVITY_DURATION = 8 * Settings.FRAME_RATE;
    private final static int SIMULATION_CYCLE = 12 * Settings.FRAME_RATE;

    private Animation[] idleAnimations;
    private Animation[] spellAnimations;
    private State state;
    private ARPGDarkLordHandler interactionHandler;
    private int simulationCyle;
    private int simulationStep;
    private boolean isInactive;
    private int inactivityDuration;

    public DarkLord(Area area, Orientation orientation, DiscreteCoordinates coordinates) {
        super(area, orientation, coordinates);

        this.idleAnimations = RPGSprite.createAnimations(ANIMATION_DURATION, this.getSprites(SpriteNames.DARK_LORD));
        this.spellAnimations = RPGSprite.createAnimations(ANIMATION_DURATION, this.getSprites(SpriteNames.DARK_LORD_SPELL), false);
        this.state = State.IDLE;
        this.simulationStep = SIMULATION_CYCLE;
        this.interactionHandler = new ARPGDarkLordHandler();
        this.simulationCyle = Helpers.random(MIN_SPELL_WAIT_DURATION, MAX_SPELL_WAIT_DURATION);
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
                new Vector(-0.5f, 0.3f),
                new Orientation[]{Orientation.UP, Orientation.LEFT, Orientation.DOWN, Orientation.RIGHT}
        );
    }

    private Orientation whereToThrowFireSpell() {
        DiscreteCoordinates position = this.getCurrentMainCellCoordinates();
        Area area = this.getOwnerArea();
        Random prng = RandomGenerator.getInstance();
        int maxAttemptAmount = 50;
        int attempt = 0;

        while (attempt < maxAttemptAmount) {
            int random = prng.nextInt(Orientation.values().length);
            for (Orientation orientation : Orientation.values()) {
                if (
                        orientation.ordinal() == random &&
                        area.canEnterAreaCells(this, List.of(position.jump(orientation.toVector())))
                ) {
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
        this.inactivityDuration--;

        if (this.inactivityDuration == 0)
            this.isInactive = false;

        if (this.isInactive) return;

        switch (this.state) {
            case IDLE: {
                if (this.simulationStep % SIMULATION_CYCLE == 0) {
                    this.isInactive = true;
                    this.inactivityDuration = Helpers.random(MIN_INACTIVITY_DURATION, MAX_INACTIVITY_DURATION);
                }
            }
            break;
            case ATTACKING: {
                Animation currentAnim = this.getCharacterAnimations()[this.getOrientation().ordinal()];
                if (currentAnim.isCompleted()) {
                    currentAnim.reset();
                    FireSpellItem.consume(this, this.getOwnerArea());
                    this.state = State.IDLE;
                }
            }
            break;
            case INVOKING: {
                Animation currentAnim = this.getCharacterAnimations()[this.getOrientation().ordinal()];
                if (currentAnim.isCompleted()) {
                    currentAnim.reset();
                    FlameSkullItem.consume(this, this.getOwnerArea());
                    this.state = State.IDLE;
                }
            }
            break;
            case PREPARING_TELEPORTATION: {
                Animation currentAnim = this.getCharacterAnimations()[this.getOrientation().ordinal()];
                if (currentAnim.isCompleted()) {
                    currentAnim.reset();
                    this.state = State.TELEPORTING;
                }
            }
            break;
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
                } while (
                        attempt < attemptsLimit &&
                                !this.getOwnerArea().canEnterAreaCells(this, List.of(position))
                );

                if (attempt < attemptsLimit) {
                    this.getOwnerArea().leaveAreaCells(this, this.getCurrentCells());
                    this.setCurrentPosition(position.toVector());
                    this.getOwnerArea().enterAreaCells(this, List.of(position));
                }
                this.state = State.IDLE;
            }
            break;
        }
    }

    @Override
    protected boolean isAnimationPaused() {
        return this.isInactive;
    }

    @Override
    public void update(float deltaTime) {
        if (this.isAlive()) {
            this.simulationStep++;

            if (!this.isInactive) {
                if (this.state.equals(State.IDLE))
                    this.move(45);

                if (this.isTargetReached()) {
                    if (!this.getOwnerArea().canEnterAreaCells(this, this.getNextCurrentCells()))
                        this.orientate(this.whereToThrowFireSpell());

                    if (this.shouldSwitchOrientation() && this.state.equals(State.IDLE))
                        this.switchOrientation();
                }

                if (!this.state.equals(State.INVOKING) && !this.state.equals(State.ATTACKING))
                    if (this.simulationStep % this.simulationCyle == 0) {
                        double random = RandomGenerator.getInstance().nextDouble();
                        this.state = random > 0.7 ? State.ATTACKING : State.INVOKING;
                        this.orientate(this.whereToThrowFireSpell());
                    }
            }

            this.act();
        }

        super.update(deltaTime);
    }

    @Override
    protected Animation[] getCharacterAnimations() {
        switch (this.state) {
            case IDLE:
            case TELEPORTING:
                return this.idleAnimations;
            case INVOKING:
            case ATTACKING:
            case PREPARING_TELEPORTATION:
                return this.spellAnimations;
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
        return Helpers.getCellsInRadius(this.getCurrentMainCellCoordinates(), ACTION_RADIUS);
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
            if (DarkLord.this.isInactive) return;

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
