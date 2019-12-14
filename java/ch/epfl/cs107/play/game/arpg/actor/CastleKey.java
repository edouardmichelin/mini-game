package ch.epfl.cs107.play.game.arpg.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.CollectibleAreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.window.Canvas;

public class CastleKey extends CollectibleAreaEntity {
    private RPGSprite sprite;

    public static void drop(AreaEntity source, Area area) {
        DiscreteCoordinates position = source
                .getCurrentCells()
                .get(0);

        area.registerActor(new CastleKey(area, Orientation.DOWN, position));
    }

    /**
     * Default AreaEntity constructor
     *
     * @param area        (Area): Owner area. Not null
     * @param orientation (Orientation): Initial orientation of the entity in the Area. Not null
     * @param position    (DiscreteCoordinate): Initial position of the entity in the Area. Not null
     */
    public CastleKey(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);

        this.sprite = new RPGSprite(
                "zelda/key",
                1,
                1,
                this,
                new RegionOfInterest(0, 0, 16, 16)
        );
    }

    @Override
    public void draw(Canvas canvas) {
        this.sprite.draw(canvas);
    }

    public ARPGInventory.ARPGItem collect() {
        super.collect();

        return ARPGInventory.ARPGItem.CASTLE_KEY;
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v) {
        ((ARPGInteractionVisitor) v).interactWith(this);
    }
}
