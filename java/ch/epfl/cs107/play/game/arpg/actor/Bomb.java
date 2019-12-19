package ch.epfl.cs107.play.game.arpg.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.*;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.config.Settings;
import ch.epfl.cs107.play.game.arpg.config.SpriteNames;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.game.rpg.misc.DamageType;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Collections;
import java.util.List;

public class Bomb extends AreaEntity implements Interactor, Dropable {
    private final static int TIME_BEFORE_DETONATION = 5;
    private final static int EXPLOSION_FRAMES_NUMBER = 7;
    private final static float DAMAGE = 2.5f;
    private final static DamageType DAMAGE_TYPE = DamageType.PHYSICAL;

    private ARPGBombHandler interactionHandler;
    private Animation animation;
    private int simulationStep;
    private boolean detonate;
    private boolean exploded;

    /**
     * Default AreaEntity constructor
     *
     * @param area        (Area): Owner area. Not null
     * @param orientation (Orientation): Initial orientation of the entity in the Area. Not null
     * @param position    (DiscreteCoordinate): Initial position of the entity in the Area. Not null
     */
    public Bomb(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);

        this.interactionHandler = new ARPGBombHandler();
        this.animation = new Animation(Settings.FRAME_RATE / 3, getDefaultSprites(), true);
    }

    private Sprite[] getDefaultSprites() {
        return new Sprite[]{
                new RPGSprite(
                        SpriteNames.BOMB,
                        1,
                        1,
                        this,
                        new RegionOfInterest(0, 0, 16, 16),
                        new Vector(0, 0.3f)),
                new RPGSprite(
                        SpriteNames.BOMB,
                        1,
                        1,
                        this,
                        new RegionOfInterest(16, 0, 16, 16),
                        new Vector(0, 0.3f))
        };
    }

    private Sprite[] getExplosionSprites() {
        Sprite[] s = new Sprite[EXPLOSION_FRAMES_NUMBER];
        for (int frame = 0; frame < EXPLOSION_FRAMES_NUMBER; frame++)
            s[frame] = new RPGSprite(
                    SpriteNames.EXPLOSION,
                    1,
                    1,
                    this,
                    new RegionOfInterest(frame * 32, 0, 32, 32),
                    new Vector(0, 0.2f)
            );

        return s;
    }

    private void onExplode() {
        if (this.exploded) return;

        this.animation = new Animation(1, this.getExplosionSprites(), false);
        this.exploded = true;
    }

    void explode() {
        this.detonate = true;
    }

    @Override
    public void update(float deltaTime) {
        this.simulationStep++;

        if (!this.detonate && simulationStep == TIME_BEFORE_DETONATION * Settings.FRAME_RATE)
            this.explode();

        if (this.animation.isCompleted())
            this.getOwnerArea().unregisterActor(this);
        else
            this.animation.update(deltaTime);

        super.update(deltaTime);
    }

    @Override
    public void draw(Canvas canvas) {
        this.animation.draw(canvas);
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        return getCurrentMainCellCoordinates().getNeighbours();
    }

    @Override
    public boolean wantsCellInteraction() {
        return this.detonate;
    }

    @Override
    public boolean wantsViewInteraction() {
        return this.detonate;
    }

    @Override
    public void interactWith(Interactable other) {
        this.onExplode();
        other.acceptInteraction(this.interactionHandler);
    }

    @Override
    public boolean takeCellSpace() {
        return true;
    }

    @Override
    public boolean isCellInteractable() {
        return true;
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
        destroyable.damage(DAMAGE / (EXPLOSION_FRAMES_NUMBER + 1), DAMAGE_TYPE);
    }

    private class ARPGBombHandler implements ARPGInteractionVisitor {

        @Override
        public void interactWith(ARPGPlayer player) {
            Bomb.this.inflictDamage(player);
        }

        @Override
        public void interactWith(FlameSkull flameSkull) {
            Bomb.this.inflictDamage(flameSkull);
        }

        @Override
        public void interactWith(LogMonster logMonster) {
            Bomb.this.inflictDamage(logMonster);
        }

        @Override
        public void interactWith(Grass grass) {
            Bomb.this.inflictDamage(grass);
        }

        @Override
        public void interactWith(CaveDoor caveDoor) {
            caveDoor.open();
        }

    }
}
