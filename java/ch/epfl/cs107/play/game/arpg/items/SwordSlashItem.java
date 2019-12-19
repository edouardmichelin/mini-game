package ch.epfl.cs107.play.game.arpg.items;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.game.arpg.actor.ARPGInventory.ARPGItem;
import ch.epfl.cs107.play.game.arpg.actor.MagicWaterProjectile;
import ch.epfl.cs107.play.game.arpg.actor.SwordSlash;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

import java.util.List;

public class SwordSlashItem {
    public static final String TITLE = "sword_slash";
    public static final int PRICE = 20;
    public static final int WEIGHT = 0;
    public static final ARPGItem ITEM = null;
    public static final ARPGItem ITEM_TO_CONSUME = null;

    public static void consume(AreaEntity consumer, Area area) {
        DiscreteCoordinates position = consumer
                .getCurrentCells()
                .get(0)
                .jump(consumer.getOrientation().toVector());

        SwordSlash swordSlash = new SwordSlash(area, consumer.getOrientation(), position);

        if (area.canEnterAreaCells(swordSlash, List.of(position)))
            area.registerActor(swordSlash);
    }
}
