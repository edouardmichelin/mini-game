package ch.epfl.cs107.play.game.arpg.items;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.arpg.actor.CastleKey;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

public class CastleKeyItem {

    public static final String TITLE = "castle_key";
    public static final int PRICE = 20;
    public static final int WEIGHT = 0;

    public static void drop(AreaEntity source, Area area) {
        DiscreteCoordinates position = source
                .getCurrentCells()
                .get(0);

        area.registerActor(new CastleKey(area, Orientation.DOWN, position));
    }
}
