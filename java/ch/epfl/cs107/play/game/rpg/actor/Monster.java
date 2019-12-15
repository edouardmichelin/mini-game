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
    private boolean onDyingExecuted;
    private float hp;
    private float maxHp;
    private Animation deathAnimation;

    public Monster(Area area, Orientation orientation, DiscreteCoordinates coordinates) {
        super(area, orientation, coordinates);

        this.despawnTime = Settings.FRAME_RATE / 2f;
        this.maxHp = this.getMaxHp();
        this.hp = this.maxHp;
        this.deathAnimation = new Animation(
                DEATH_ANIMATION_DURATION / (DEATH_ANIMATION_FRAMES - 1),
                this.getDeathSprites(),
                false
        );
    }

    protected abstract Animation[] getCharacterAnimations();

    protected abstract List<DamageType> getWeaknesses();

    public float getHp() {
        return this.hp;
    }

    private Animation getAnimation() {
        if(this.isAlive()) {
            return this.getCharacterAnimations()[this.getOrientation().ordinal()];
        }

        return this.deathAnimation;
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
    public float damage(float damage, DamageType type) {
        if (this.getWeaknesses().contains(type))
            this.hp -= damage;

        return this.hp;
    }

    @Override
    public void destroy() {
        this.hp = 0;
    }

    @Override
    public boolean isWeak() {
        return this.hp < this.maxHp / 5;
    }

    @Override
    public void strengthen() {
        this.hp = this.maxHp;
    }

    @Override
    public void update(float deltaTime) {
        if (this.isDisplacementOccurs())
            this.getAnimation().update(deltaTime);

        if (!this.isAlive()) {
            if (!this.onDyingExecuted) {
                this.onDying();
                this.onDyingExecuted = true;
            }
            this.despawnTime -= 1;
            if (this.despawnTime <= 0) this.getOwnerArea().unregisterActor(this);
        }

        super.update(deltaTime);
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
        return Collections.singletonList(getCurrentMainCellCoordinates().jump(getOrientation().toVector()));
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
