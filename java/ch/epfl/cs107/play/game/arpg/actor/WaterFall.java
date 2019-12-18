package ch.epfl.cs107.play.game.arpg.actor;

import ch.epfl.cs107.play.game.actor.Entity;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.arpg.config.Settings;
import ch.epfl.cs107.play.game.arpg.config.SpriteNames;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

public class WaterFall extends Entity {
    public static final int ANIMATION_DURATION = Settings.FRAME_RATE / 6;
    private Animation animation;

    public WaterFall(Vector position) {
        super(position);
        Sprite[] sprites = new Sprite[3];
        for (int frame = 0; frame < 3; frame++)
            sprites[frame] = new RPGSprite(
                    SpriteNames.WATERFALL,
                    4,
                    4,
                    this,
                    new RegionOfInterest(frame * 64, 0, 64, 64)
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
