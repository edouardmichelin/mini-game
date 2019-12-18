package ch.epfl.cs107.play.game.arpg.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.*;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.area.ARPGBehavior;
import ch.epfl.cs107.play.game.arpg.config.Settings;
import ch.epfl.cs107.play.game.arpg.config.SpriteNames;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.items.FireSpellItem;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.game.rpg.misc.DamageType;
import ch.epfl.cs107.play.game.rpg.misc.Helpers;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RandomGenerator;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class FireSpell extends AreaEntity implements Interactor, Dropable, Destroyable {
    private final static int MIN_LIFE_TIME = 150;
    private final static int MAX_LIFE_TIME = 300;
    private final static int PROPAGATION_TIME_FIRE_CYCLE = 150;
    private final static float STRENGTH_UNIT = 0.1f;
    private final static DamageType DAMAGE_TYPE = DamageType.FIRE;

    private Animation animation;
    private int lifeTime;
    private int simulationStep;
    private float strength;
    private ARPGFireSpellHandler interactionHandler;

    /**
     * Default AreaEntity constructor
     *
     * @param area        (Area): Owner area. Not null
     * @param orientation (Orientation): Initial orientation of the entity in the Area. Not null
     * @param position    (DiscreteCoordinate): Initial position of the entity in the Area. Not null
     */
    public FireSpell(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);

        this.interactionHandler = new ARPGFireSpellHandler();
        this.lifeTime = Helpers.random(MIN_LIFE_TIME, MAX_LIFE_TIME);
        this.strength = RandomGenerator.getInstance().nextFloat() + STRENGTH_UNIT;
        this.animation = new Animation(Settings.FRAME_RATE / 8, getDefaultSprites(), true);
    }

    public FireSpell(Area area, Orientation orientation, DiscreteCoordinates position, float strength) {
        this(area, orientation, position);

        if (strength != -1) this.strength = strength;
    }

    private Sprite[] getDefaultSprites() {
        Sprite[] s = new Sprite[7];
        for (int frame = 0; frame < 7; frame++)
            s[frame] = new RPGSprite(
                    SpriteNames.FIRE_SPELL,
                    1,
                    1,
                    this,
                    new RegionOfInterest(frame * 16, 0, 16, 16)
            );

        return s;
    }

    @Override
    public void update(float deltaTime) {
        this.simulationStep++;

        if (this.simulationStep == this.lifeTime)
            this.getOwnerArea().unregisterActor(this);

        if (this.simulationStep % PROPAGATION_TIME_FIRE_CYCLE == 0 && this.strength > 0)
            if (
                    this.getOwnerArea().canEnterAreaCells(
                            this,
                            List.of(this.getCurrentMainCellCoordinates().jump(this.getOrientation().toVector()))
                    )
            )
                FireSpellItem.consume(this, this.getOwnerArea(), this.strength - STRENGTH_UNIT);

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

    @Override
    public boolean takeCellSpace() {
        return false;
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
        destroyable.damage(0.5f / Settings.FRAME_RATE, DAMAGE_TYPE);
    }

    @Override
    public float getHp() {
        return 0;
    }

    @Override
    public float getMaxHp() {
        return 0;
    }

    @Override
    public boolean isWeak() {
        return false;
    }

    @Override
    public void strengthen() {

    }

    @Override
    public List<DamageType> getWeaknesses() {
        return List.of(DamageType.MAGICAL, DamageType.PHYSICAL);
    }

    @Override
    public float damage(float damage, DamageType type) {
        if (this.getWeaknesses().contains(type))
            this.destroy();

        return 0;
    }

    @Override
    public void destroy() {
        this.onDying();
    }

    @Override
    public void onDying() {
        this.getOwnerArea().unregisterActor(this);
    }

    private class ARPGFireSpellHandler implements ARPGInteractionVisitor {

        @Override
        public void interactWith(ARPGPlayer player) {
            inflictDamage(player);
        }

        @Override
        public void interactWith(Grass grass) {
            inflictDamage(grass);
        }

        @Override
        public void interactWith(Bomb bomb) {
            bomb.explode();
        }

    }
}
