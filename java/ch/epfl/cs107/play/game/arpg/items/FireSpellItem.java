package ch.epfl.cs107.play.game.arpg.items;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.game.arpg.actor.ARPGInventory.ARPGItem;
import ch.epfl.cs107.play.game.arpg.actor.FireSpell;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

public class FireSpellItem {

    public static final String TITLE = "fire_spell";
    public static final int PRICE = 20;
    public static final int WEIGHT = 0;
    public static final ARPGItem ITEM = null;

    public static ARPGItem consume(AreaEntity consumer, Area area) {
        return consume(consumer, area, -1);
    }

    public static ARPGItem consume(AreaEntity consumer, Area area, float strength) {
        DiscreteCoordinates position = consumer
                .getCurrentCells()
                .get(0)
                .jump(consumer.getOrientation().toVector());

        area.registerActor(new FireSpell(area, consumer.getOrientation(), position, strength));

        return ITEM;
    }
}
