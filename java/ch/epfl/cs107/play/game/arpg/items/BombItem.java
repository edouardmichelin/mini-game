package ch.epfl.cs107.play.game.arpg.items;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.arpg.actor.ARPGInventory.ARPGItem;
import ch.epfl.cs107.play.game.arpg.actor.Bomb;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

import java.util.List;

public class BombItem {

    public static final String TITLE = "bomb";
    public static final int PRICE = 20;
    public static final int WEIGHT = 0;
    public static final ARPGItem ITEM = ARPGItem.BOMB;
    public static final ARPGItem ITEM_TO_CONSUME = null;

    public static void consume(AreaEntity consumer, Area area) {
        DiscreteCoordinates position = consumer
                .getCurrentCells()
                .get(0);

        Bomb bomb = new Bomb(area, Orientation.DOWN, position);

        if (area.canEnterAreaCells(bomb, List.of(position.jump(consumer.getOrientation().toVector()))))
            area.registerActor(bomb);
        else {
            area.registerActor(new Bomb(area, Orientation.DOWN, position));
        }
    }
}
