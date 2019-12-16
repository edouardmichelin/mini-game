package ch.epfl.cs107.play.game.arpg.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.config.Settings;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.Monster;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.game.rpg.misc.DamageType;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RandomGenerator;
import ch.epfl.cs107.play.math.Vector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LogMonster extends Monster {
    private final static DamageType DAMAGE_TYPE = DamageType.PHYSICAL;
    private final static float DAMAGE_AMOUNT = 1.5f;
    private final static int FIELD_OF_VIEW_RANGE = 8;
    private final static int MIN_SLEEPING_DURATION = 3 * Settings.FRAME_RATE;
    private final static int MAX_SLEEPING_DURATION = 10 * Settings.FRAME_RATE;
    private final static int SIMULATION_CYLE = Settings.FRAME_RATE;

    private State state;
    private Animation[] animations;
    private ARPGLogMonsterHandler interactionHandler;
    private int sleepingDuration;
    private int simulationStep;

    public LogMonster(Area area, Orientation orientation, DiscreteCoordinates coordinates) {
        super(area, orientation, coordinates);

        this.state = State.IDLE;
        this.animations = this.getAnimations();
        this.interactionHandler = new ARPGLogMonsterHandler();
        this.simulationStep = SIMULATION_CYLE;
    }

    private List<DiscreteCoordinates> getCellsInRange(int range) {
        List<DiscreteCoordinates> dc = new ArrayList<>();
        Vector orientation = this.getOrientation().toVector();

        dc.add(this.getCurrentMainCellCoordinates().jump(orientation));

        for (int i = 1; i < range; i++)
            dc.add(dc.get(i - 1).jump(orientation));

        return dc;
    }

    private Animation[] getAnimations() {
        String spriteName = this.state.associatedSpriteName;

        return RPGSprite.createAnimations(8, RPGSprite.extractSprites(
                spriteName,
                3,
                1f,
                1f,
                this,
                32,
                32,
                new Orientation[] {Orientation.DOWN, Orientation.UP, Orientation.RIGHT, Orientation.LEFT}
        ));
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
            case IDLE: ;
            break;
            case ATTACKING: ;
            break;
            case FALLING_ASLEEP: {
                this.sleepingDuration = RandomGenerator
                        .getInstance()
                        .nextInt(MAX_SLEEPING_DURATION - MIN_SLEEPING_DURATION) + MIN_SLEEPING_DURATION;

                this.state = State.SLEEPING;
                this.animations = this.getAnimations();
            } break;
            case SLEEPING: {
                this.sleepingDuration--;
                if (this.sleepingDuration == 0)
                    this.state = State.WAKING_UP;
            } break;
            case WAKING_UP: {
                this.state = State.IDLE;
                this.animations = this.getAnimations();
            }
        }
    }

    @Override
    public void update(float deltaTime) {
        if (this.isAlive()) {
            this.simulationStep++;

            if (this.simulationStep % SIMULATION_CYLE != 0)
                if (this.shouldSwitchOrientation() && this.isTargetReached())
                    this.switchOrientation();
                else
                    this.move(40);

            if (this.isTargetReached())
                if (!this.getOwnerArea().canEnterAreaCells(this, this.getNextCurrentCells()))
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
        if (LogMonster.this.state.equals(State.ATTACKING))
            return Collections.singletonList(getCurrentMainCellCoordinates().jump(getOrientation().toVector()));
        else
            return this.getCellsInRange(FIELD_OF_VIEW_RANGE);
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
        return true;
    }

    @Override
    public void interactWith(Interactable other) {
        other.acceptInteraction(this.interactionHandler);
    }

    private enum State {
        IDLE("zelda/logMonster"),
        ATTACKING("zelda/logMonster"),
        FALLING_ASLEEP("zelda/logMonster"),
        SLEEPING("zelda/logMonster.sleeping"),
        WAKING_UP("zelda/logMonster.wakingUp")
        ;

        final String associatedSpriteName;

        State(String associatedSpriteName) {
            this.associatedSpriteName = associatedSpriteName;
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
            } else if (LogMonster.this.state.equals(State.ATTACKING)) {

            }
        }

    }
}
