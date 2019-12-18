package ch.epfl.cs107.play.game.arpg.items;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.arpg.actor.ARPGInventory;
import ch.epfl.cs107.play.game.arpg.actor.Coin;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

public class CoinItem {

    public static final String TITLE = "coin";
    public static final int PRICE = 20;
    public static final int WEIGHT = 0;
    public static final ARPGInventory.ARPGItem ITEM = null;
    public static final ARPGInventory.ARPGItem ITEM_TO_CONSUME = null;

    public static void drop(AreaEntity source, Area area) {
        DiscreteCoordinates position = source
                .getCurrentCells()
                .get(0);

        area.registerActor(new Coin(area, Orientation.DOWN, position));
    }
}
