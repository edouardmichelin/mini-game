package ch.epfl.cs107.play.game.arpg.actor;

import ch.epfl.cs107.play.game.actor.Entity;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.arpg.config.Settings;
import ch.epfl.cs107.play.game.arpg.config.SpriteNames;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

public class FlowerRed extends Entity {
    public static final int ANIMATION_DURATION = Settings.FRAME_RATE * 2 / 3;
    private Animation animation;

    public FlowerRed(Vector position) {
        super(position);
        RPGSprite[] sprites = new RPGSprite[4];
        for (int frame = 0; frame < 4; frame++)
            sprites[frame] = new RPGSprite(
                    SpriteNames.FLOWER_RED,
                    1,
                    1,
                    this,
                    new RegionOfInterest(frame * 24, 0, 24, 32),
                    new Vector(0,0),
                    1,
                    1
            );
        this.animation = new Animation(ANIMATION_DURATION, sprites, true);
    }

    @Override
    public void draw(Canvas canvas) {
        this.animation.draw(canvas);
    }

    @Override
    public void update(float deltaTime) {
        this.animation.update(deltaTime);
        super.update(deltaTime);
    }
}
