package ch.epfl.cs107.play.game.arpg.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.rpg.actor.Projectile;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;

public class Arrow extends Projectile {

    private Animation[] animations;

    static void consume(AreaEntity consumer, Area area) {
        DiscreteCoordinates position = consumer
                .getCurrentCells()
                .get(0)
                .jump(consumer.getOrientation().toVector());

        area.registerActor(new Arrow(area, consumer.getOrientation(), position));
    }

    public Arrow(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);

        this.animations = new Animation[4];

        for (Orientation direction : Orientation.values()) {
            this.animations[direction.ordinal()] = new Animation(this.getMovingFrameDuration(), new RPGSprite[] {
                    new RPGSprite(
                            "zelda/arrow",
                            1,
                            1,
                            this,
                            new RegionOfInterest(direction.ordinal() * 32, 0, 32, 32)
                    )
            });
        }
    }

    protected Animation getAnimation() {
        return animations[this.getOrientation().ordinal()];
    }


    protected int getMovingFrameDuration() {
        return 6;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
    }

    @Override
    protected int getRange() {
        return 6;
    }

    @Override
    protected int getSpeed() {
        return 3;
    }

}
