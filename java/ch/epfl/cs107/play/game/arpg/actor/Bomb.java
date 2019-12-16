package ch.epfl.cs107.play.game.arpg.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.*;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.config.Settings;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.game.rpg.misc.DamageType;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Collections;
import java.util.List;

public class Bomb extends AreaEntity implements Interactor, Dropable {
    private final static DamageType DAMAGE_TYPE = DamageType.PHYSICAL;
    private final static int DEFAULT_COUNTDOWN = 5 * Settings.FRAME_RATE;
    private final static float DEFAULT_DAMAGE = 2.5f;
    private final static String TEX = "zelda/bomb";
    private final static String EXPL_TEX = "zelda/explosion";

    private int countdown;
    private ARPGBombHandler interactionHandler;
    private Animation animation;

    static void consume(AreaEntity consumer, Area area) {
        DiscreteCoordinates position = consumer
                .getCurrentCells()
                .get(0)
                .jump(consumer.getOrientation().toVector());

        area.registerActor(new Bomb(area, Orientation.DOWN, position));
    }

    /**
     * Default AreaEntity constructor
     *
     * @param area        (Area): Owner area. Not null
     * @param orientation (Orientation): Initial orientation of the entity in the Area. Not null
     * @param position    (DiscreteCoordinate): Initial position of the entity in the Area. Not null
     */
    public Bomb(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);

        this.animation = new Animation(Settings.FRAME_RATE / 3, getDefaultSprites(), true);
        this.interactionHandler = new ARPGBombHandler();
        this.countdown = DEFAULT_COUNTDOWN;
    }

    private Sprite[] getDefaultSprites() {
        return new Sprite[] {
                new RPGSprite(TEX, 1, 1, this, new RegionOfInterest(0, 0, 16, 16)),
                new RPGSprite(TEX, 1, 1, this, new RegionOfInterest(16, 0, 16, 16))
        };
    }

    private Sprite[] getExplosionSprites() {
        Sprite[] s = new Sprite[7];
        for (int frame = 0; frame < 7; frame++)
            s[frame] = new RPGSprite(
                    EXPL_TEX,
                    1,
                    1,
                    this,
                    new RegionOfInterest(frame * 32, 0, 32, 32)
            );

        return s;
    }

    private void onExplode() {
        this.animation.reset();
        this.animation = new Animation(Settings.FRAME_RATE / 18, this.getExplosionSprites(), true);
    }

    void explode() {
        this.countdown = 0;
    }

    @Override
    public void update(float deltaTime) {
        if (this.countdown < -(Settings.FRAME_RATE / 4)) {
            this.getOwnerArea().unregisterActor(this);
            return;
        }

        this.countdown = this.countdown - 1;
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
        return countdown == 0;
    }

    @Override
    public boolean wantsViewInteraction() {
        return countdown == 0;
    }

    @Override
    public void interactWith(Interactable other) {
        this.onExplode();
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

    }

    private void inflictDamage(Destroyable destroyable) {
        destroyable.damage(DEFAULT_DAMAGE, DAMAGE_TYPE);
    }

    private class ARPGBombHandler implements ARPGInteractionVisitor {

        @Override
        public void interactWith(ARPGPlayer player) {
            Bomb.this.inflictDamage(player);
        }

        @Override
        public void interactWith(Grass grass) {
            Bomb.this.inflictDamage(grass);
        }

    }
}
