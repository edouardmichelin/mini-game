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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DarkLord extends Monster {
    private static final int ACTION_RADIUS = 3;
    private static final int MIN_SPELL_WAIT_DURATION = 200;
    private static final int MAX_SPELL_WAIT_DURATION = 500;
    private static final int TELEPORTATION_RADIUS = 3;

    private Animation[] idleAnimations;
    private Animation[] spellAnimations;
    private State state;
    private ARPGDarkLordHandler interactionHandler;
    private int simulationCyle;

    private int simulationStep = 0;

    public DarkLord(Area area, Orientation orientation, DiscreteCoordinates coordinates) {
        super(area, orientation, coordinates);

        this.idleAnimations = RPGSprite.createAnimations(8, this.getSprites("zelda/darkLord"));
        this.spellAnimations = RPGSprite.createAnimations(8, this.getSprites("zelda/darkLord.spell"));
        this.state = State.IDLE;
        this.interactionHandler = new ARPGDarkLordHandler();
        this.simulationCyle = Math.round((RandomGenerator.getInstance().nextFloat() * (MAX_SPELL_WAIT_DURATION - MIN_SPELL_WAIT_DURATION))) + MIN_SPELL_WAIT_DURATION;
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

    private void act() {
        switch (this.state) {
            case ATTACKING: {
                this.state = State.IDLE;
            } break;
            case INVOKING: {
                Bomb.consume(this, this.getOwnerArea());
                this.state = State.IDLE;
            } break;
            case PREPARING_TELEPORTATION: {
                this.state = State.TELEPORTING;
            } break;
            case TELEPORTING: {
                DiscreteCoordinates position = null;
                Random prng = RandomGenerator.getInstance();
                List<DiscreteCoordinates> v = this.getFieldOfViewCells();
                int attemptsLimit = 30;
                int attempt = 0;

                do {
                    position = v.get(prng.nextInt(v.size()));
                    attempt++;
                } while(
                        attempt < attemptsLimit &&
                        !this.getOwnerArea().canEnterAreaCells(this, List.of(position))
                );

                if (position != null) this.setCurrentPosition(position.toVector());
                this.state = State.IDLE;
            }
        }
    }

    @Override
    public void update(float deltaTime) {
        this.simulationStep++;

        if (this.isMoving())
            this.move(30);

        if (!this.getOwnerArea().canEnterAreaCells(this, this.getNextCurrentCells()))
            this.whereToThrowFireSpell();

        if (this.simulationStep % this.simulationCyle == 0) {
            double random = RandomGenerator.getInstance().nextDouble();
            this.state = random > 0.7 ? State.ATTACKING : State.INVOKING;
            this.orientate(this.whereToThrowFireSpell());
        }

        this.act();

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
    protected List<DamageType> getWeaknesses() {
        return List.of(DamageType.MAGICAL);
    }

    @Override
    protected boolean isMoving() {
        return !this.state.equals(State.IDLE);
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
        DiscreteCoordinates dc = this.getCurrentMainCellCoordinates();

        List<DiscreteCoordinates> fowc = new ArrayList<>();
        for (int x = -ACTION_RADIUS; x < ACTION_RADIUS + 1; x++)
            for (int y = -ACTION_RADIUS; y < ACTION_RADIUS + 1; y++)
                fowc.add(dc.jump(x, y));

        return fowc;
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
