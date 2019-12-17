package ch.epfl.cs107.play.game.arpg.items;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.game.arpg.actor.FlameSkull;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

public class FlameSkullItem {

    public static final String TITLE = "flame_skull";
    public static final int PRICE = 20;
    public static final int WEIGHT = 0;

    public static void consume(AreaEntity consumer, Area area) {
        DiscreteCoordinates position = consumer
                .getCurrentCells()
                .get(0)
                .jump(consumer.getOrientation().toVector());

        area.registerActor(new FlameSkull(area, consumer.getOrientation(), position));
    }
}
