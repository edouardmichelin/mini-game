package ch.epfl.cs107.play.game.arpg.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
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
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;

import java.util.Collections;
import java.util.List;

public class LogMonster extends Monster {
    private final static DamageType DAMAGE_TYPE = DamageType.PHYSICAL;
    private final static float DAMAGE_AMOUNT = 1.5f;
    private final static int FIELD_OF_VIEW_RANGE = 8;
    private final static int MIN_INACTIVITY_DURATION = 2 * Settings.FRAME_RATE;
    private final static int MAX_INACTIVITY_DURATION = 8 * Settings.FRAME_RATE;
    private final static int MIN_SLEEPING_DURATION = 3 * Settings.FRAME_RATE;
    private final static int MAX_SLEEPING_DURATION = 10 * Settings.FRAME_RATE;
    private final static int SIMULATION_CYCLE = 12 * Settings.FRAME_RATE;
    private final static int ANIMATION_DURATION = 8;

    private State state;
    private Animation[] animations;
    private ARPGLogMonsterHandler interactionHandler;
    private int sleepingDuration;
    private int simulationStep;
    private boolean isAlreadyWakingUp;
    private boolean isInactive;
    private int inactivityDuration;
    private boolean canAttack;

    public LogMonster(Area area, Orientation orientation, DiscreteCoordinates coordinates) {
        super(area, orientation, coordinates);

        this.state = State.IDLE;
        this.simulationStep = SIMULATION_CYCLE;
        this.animations = this.getAnimations();
        this.interactionHandler = new ARPGLogMonsterHandler();
    }

    private Animation[] getAnimations() {
        String spriteName = this.state.associatedSpriteName;
        int nbFrames = this.state.numberOfFrames;

        return this.state.hasSingleOrientation ?
                this.getSingleOrientationAnimations(spriteName, nbFrames) :
                RPGSprite.createAnimations(ANIMATION_DURATION, RPGSprite.extractSprites(
                        spriteName,
                        nbFrames,
                        2,
                        2,
                        this,
                        32,
                        32,
                        new Vector(-0.5f, 0),
                        new Orientation[]{Orientation.DOWN, Orientation.UP, Orientation.RIGHT, Orientation.LEFT
                        }
                ));
    }

    private Animation[] getSingleOrientationAnimations(String spriteName, int nbFrames) {
        int nbOrientation = Orientation.values().length;

        Sprite[] s = new RPGSprite[nbFrames];
        Animation anim;
        Animation[] anims = new Animation[nbOrientation];

        for (int i = 0; i < nbFrames; i++) {
            s[i] = new RPGSprite(
                    spriteName,
                    2,
                    2,
                    this,
                    new RegionOfInterest(0, i * 32, 32, 32),
                    new Vector(-0.5f, 0)
            );
        }

        anim = new Animation(ANIMATION_DURATION, s, this.state.isLooping);

        for (int i = 0; i < nbOrientation; i++)
            anims[i] = anim;

        return anims;
    }

    private void act() {
        this.inactivityDuration--;

        if (this.inactivityDuration == 0)
            this.isInactive = false;

        if (this.simulationStep % SIMULATION_CYCLE == 0) {
            this.isInactive = true;
            this.inactivityDuration = Helpers.random(MIN_INACTIVITY_DURATION, MAX_INACTIVITY_DURATION);
        }

        if (this.isInactive) return;

        switch (this.state) {
            case IDLE:
                ;
                break;
            case ATTACKING: this.canAttack = true;
                break;
            case FALLING_ASLEEP: {
                this.sleepingDuration = Helpers.random(MIN_SLEEPING_DURATION, MAX_SLEEPING_DURATION);

                this.state = State.SLEEPING;
                this.animations = this.getAnimations();
            }
            break;
            case SLEEPING: {
                this.sleepingDuration--;
                if (this.sleepingDuration == 0) {
                    this.state = State.WAKING_UP;
                    this.animations = this.getAnimations();
                }
            }
            break;
            case WAKING_UP: {
                if (!this.isAlreadyWakingUp) this.animations = this.getAnimations();

                this.isAlreadyWakingUp = true;

                if (this.animations[this.getOrientation().ordinal()].isCompleted()) {
                    this.state = State.IDLE;
                    this.isAlreadyWakingUp = false;
                    this.animations = this.getAnimations();
                }
            }
            break;
        }
    }

    private int getFramesForMove() {
        return this.state.equals(State.ATTACKING) ? 20 : 40;
    }

    @Override
    protected boolean isAnimationPaused() {
        return this.isInactive && !this.state.equals(State.SLEEPING);
    }

    @Override
    public void update(float deltaTime) {
        if (this.isAlive()) {
            this.simulationStep++;

            if (!this.isInactive && !this.state.equals(State.WAKING_UP))
                if (this.shouldSwitchOrientation() && this.isTargetReached() && !this.state.equals(State.ATTACKING))
                    this.switchOrientation();
                else if (!this.state.equals(State.SLEEPING))
                    this.move(this.getFramesForMove());

            if (this.isTargetReached())
                if (!this.getOwnerArea().canEnterAreaCells(this, this.getNextCurrentCells()))
                    if (this.state.equals(State.ATTACKING))
                        this.state = State.FALLING_ASLEEP;
                    else
                        this.switchOrientation();

            this.act();
        }

        super.update(deltaTime);
    }

    @Override
    protected Animation[] getCharacterAnimations() {
        return this.animations;
    }

    @Override
    public float getMaxHp() {
        return 2f;
    }

    @Override
    public List<DamageType> getWeaknesses() {
        return List.of(DamageType.FIRE, DamageType.PHYSICAL);
    }

    @Override
    public void onDying() {

    }

    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        if (this.state.equals(State.ATTACKING))
            return Collections
                    .singletonList(getCurrentMainCellCoordinates().jump(getOrientation().toVector()));
        else
            return Helpers
                    .getCellsInRange(this.getCurrentMainCellCoordinates(), this.getOrientation(), FIELD_OF_VIEW_RANGE);
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
        return !this.state.equals(State.SLEEPING);
    }

    @Override
    public void interactWith(Interactable other) {
        other.acceptInteraction(this.interactionHandler);
    }

    private enum State {
        IDLE(SpriteNames.LOG_MONSTER, false, true, 4),
        ATTACKING(SpriteNames.LOG_MONSTER, false, true, 4),
        FALLING_ASLEEP(SpriteNames.LOG_MONSTER, false, true, 4),
        SLEEPING(SpriteNames.LOG_MONSTER_SLEEPING, true, true, 4),
        WAKING_UP(SpriteNames.LOG_MONSTER_WAKING_UP, true, false, 3);

        public final String associatedSpriteName;
        public final boolean hasSingleOrientation;
        public final boolean isLooping;
        public final int numberOfFrames;

        State(String associatedSpriteName, boolean hasSingleOrientation, boolean isLooping, int numberOfFrames) {
            this.associatedSpriteName = associatedSpriteName;
            this.hasSingleOrientation = hasSingleOrientation;
            this.isLooping = isLooping;
            this.numberOfFrames = numberOfFrames;
        }
    }

    private class ARPGLogMonsterHandler implements ARPGInteractionVisitor {
        @Override
        public void interactWith(Interactable other) {
            //
        }

        @Override
        public void interactWith(ARPGPlayer player) {
            if (LogMonster.this.state.equals(State.IDLE)) {
                LogMonster.this.state = State.ATTACKING;
            } else if (LogMonster.this.state.equals(State.ATTACKING) && LogMonster.this.canAttack) {
                player.damage(DAMAGE_AMOUNT, DAMAGE_TYPE);
                LogMonster.this.canAttack = false;
                LogMonster.this.state = State.FALLING_ASLEEP;
            }
        }

    }
}
