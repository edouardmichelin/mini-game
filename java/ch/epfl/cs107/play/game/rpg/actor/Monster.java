package ch.epfl.cs107.play.game.rpg.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.*;
import ch.epfl.cs107.play.game.arpg.config.Settings;
import ch.epfl.cs107.play.game.rpg.misc.DamageType;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Collections;
import java.util.List;

public abstract class Monster extends MovableAreaEntity implements Destroyable, Interactor {
    private final static int DEATH_ANIMATION_DURATION = Settings.FRAME_RATE / 3;
    private final static int DEATH_ANIMATION_FRAMES = 7;

    private float despawnTime;
    private float hp;

    public Monster(Area area, Orientation orientation, DiscreteCoordinates coordinates) {
        super(area, orientation, coordinates);
        this.despawnTime = Settings.FRAME_RATE / 2f;
        this.hp = this.getMaxHp();
    }

    protected abstract Animation[] getCharacterAnimations();

    public float getHp() {
        return this.hp;
    }

    protected DamageType[] getWeaknesses() {
        return DamageType.values();
    }

    private Animation getAnimation() {
        if(this.isAlive()) {
            return this.getCharacterAnimations()[this.getOrientation().ordinal()];
        }

        return new Animation(
                DEATH_ANIMATION_DURATION / (DEATH_ANIMATION_FRAMES - 1),
                this.getDeathSprites(),
                false
        );
    }

    private Sprite[] getDeathSprites() {
        Sprite[] sprites = new Sprite[DEATH_ANIMATION_FRAMES];

        for (int frame = 0; frame < DEATH_ANIMATION_FRAMES; frame++) {
            sprites[frame] = new RPGSprite(
                    "zelda/vanish",
                    1,
                    1,
                    this,
                    new RegionOfInterest(frame * 32, 0, 32, 32)
            );
        }

        return sprites;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        this.getAnimation().update(deltaTime);

        if (!this.isAlive()) {
            this.despawnTime -= 1;
            if (this.despawnTime <= 0) this.getOwnerArea().unregisterActor(this);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        this.getAnimation().draw(canvas);
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        return Collections.singletonList (getCurrentMainCellCoordinates().jump(getOrientation().toVector()));
    }

    @Override
    public boolean wantsCellInteraction() {
        return false;
    }

    @Override
    public boolean wantsViewInteraction() {
        return false;
    }

    @Override
    public boolean takeCellSpace() {
        return this.isAlive();
    }

    @Override
    public boolean isCellInteractable() {
        return this.isAlive();
    }

    @Override
    public boolean isViewInteractable() {
        return this.isAlive();
    }


}
