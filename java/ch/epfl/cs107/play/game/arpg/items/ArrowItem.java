package ch.epfl.cs107.play.game.arpg.items;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.game.arpg.actor.ARPGInventory.ARPGItem;
import ch.epfl.cs107.play.game.arpg.actor.Arrow;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

import java.util.List;

public class ArrowItem {

    public static final String TITLE = "arrow";
    public static final int PRICE = 20;
    public static final int WEIGHT = 0;
    public static final ARPGItem ITEM = ARPGItem.ARROW;
    public static final ARPGItem ITEM_TO_CONSUME = ARPGItem.ARROW;

    public static void consume(AreaEntity consumer, Area area) {
        DiscreteCoordinates position = consumer
                .getCurrentCells()
                .get(0)
                .jump(consumer.getOrientation().toVector());

        Arrow arrow = new Arrow(area, consumer.getOrientation(), position);

        if (area.canEnterAreaCells(arrow, List.of(position)))
            area.registerActor(arrow);
    }
}
