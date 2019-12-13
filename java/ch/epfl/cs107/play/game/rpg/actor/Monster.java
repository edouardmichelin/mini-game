package ch.epfl.cs107.play.game.rpg.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.*;
import ch.epfl.cs107.play.game.arpg.config.Settings;
import ch.epfl.cs107.play.game.rpg.misc.DamageType;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.window.Canvas;

public abstract class Monster extends MovableAreaEntity implements Destroyable, Interactor {
    private final static int ANIMATION_DURATION = Settings.FRAME_RATE / 3;
    private int maxHp;
    private float hp;
    private boolean isDead;
    private int despawnTime;
    private DamageType[] weakness;
    private Animation[] animations;
    private Animation deathAnimation;

    public Monster(Area area, Orientation orientation, DiscreteCoordinates coordinates) {
        super(area, orientation, coordinates);
        this.hp = maxHp;
        this.despawnTime = Settings.FRAME_RATE / 2;
        this.isDead = false;
        this.weakness = DamageType.values();

        Sprite sprites[] = new Sprite[7];
        for (int frame = 0; frame < 7; frame++) {
            sprites[frame] = new RPGSprite("zelda/vanish", 1, 1, this,
                    new RegionOfInterest(frame * 32, 0, 32, 32));
        }
        this.deathAnimation = new Animation(
                ANIMATION_DURATION / 6,
                sprites,
                true)
        ;
    }

    public void die() {
        this.isDead = true;
    }

    private Animation getAnimation() {
        if(this.isDead) {
            return this.deathAnimation;
        }
        return this.animations[this.getOrientation().ordinal()];
    }

    public void update(float deltaTime) {
        super.update(deltaTime);
        this.getAnimation().update(deltaTime);
    }

    @Override
    public boolean isWeak() {
        return this.hp <= ((float) this.maxHp / 10);
    }

    @Override
    public void draw(Canvas canvas) {
        if (this.isDead) {
            this.deathAnimation.draw(canvas);
        } else {
            this.animations[this.getOrientation().ordinal()].draw(canvas);
        }
    }

    @Override
    public void strengthen() {
        this.hp = this.maxHp;
    }

    @Override
    public int damage(int damage, DamageType type) {
        this.hp = this.hp - damage;
        return (int) this.hp;
    }

    @Override
    public void destroy() {
        this.hp = 0;
    }
}
