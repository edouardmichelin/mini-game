package ch.epfl.cs107.play.game.arpg.items;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.arpg.actor.ARPGInventory.ARPGItem;
import ch.epfl.cs107.play.game.arpg.actor.Bomb;
import ch.epfl.cs107.play.game.arpg.actor.FireSpell;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

import java.util.List;

public class FireSpellItem {

    public static final String TITLE = "fire_spell";
    public static final int PRICE = 20;
    public static final int WEIGHT = 0;
    public static final ARPGItem ITEM = null;
    public static final ARPGItem ITEM_TO_CONSUME = null;

    public static void consume(AreaEntity consumer, Area area) {
        consume(consumer, area, -1);
    }

    public static void consume(AreaEntity consumer, Area area, float strength) {
        DiscreteCoordinates position = consumer
                .getCurrentCells()
                .get(0)
                .jump(consumer.getOrientation().toVector());

        FireSpell fireSpell = new FireSpell(area, consumer.getOrientation(), position);

        if (area.canEnterAreaCells(fireSpell, List.of(position)))
            area.registerActor(fireSpell);
    }
}
