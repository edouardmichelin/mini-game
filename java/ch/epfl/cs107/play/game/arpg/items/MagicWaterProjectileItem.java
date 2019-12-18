package ch.epfl.cs107.play.game.arpg.items;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.game.arpg.actor.ARPGInventory.ARPGItem;
import ch.epfl.cs107.play.game.arpg.actor.MagicWaterProjectile;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

public class MagicWaterProjectileItem {

    public static final String TITLE = "magic_water_projectile";
    public static final int PRICE = 20;
    public static final int WEIGHT = 0;
    public static final ARPGItem ITEM = null;
    public static final ARPGItem ITEM_TO_CONSUME = null;

    public static ARPGItem consume(AreaEntity consumer, Area area) {
        DiscreteCoordinates position = consumer
                .getCurrentCells()
                .get(0)
                .jump(consumer.getOrientation().toVector());

        area.registerActor(new MagicWaterProjectile(area, consumer.getOrientation(), position));

        return ITEM;
    }
}
