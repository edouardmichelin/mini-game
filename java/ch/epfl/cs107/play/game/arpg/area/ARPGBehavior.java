package ch.epfl.cs107.play.game.arpg.area;

import ch.epfl.cs107.play.game.areagame.AreaBehavior;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Window;

public class ARPGBehavior extends AreaBehavior {
    /**
     * AreaBehavior Constructor
     *
     * @param window (Window): graphic context, not null
     * @param name   (String): name of the behavior image, not null
     */
    public ARPGBehavior(Window window, String name) {
        super(window, name);
        for (int x = 0; x < getWidth(); x++)
            for (int y = 0; y < getHeight(); y++)
                this.setCell(x, y, new ARPGCell(x, y, Tuto2CellType.toType(getRGB((getHeight() - 1 - y), x))));
    }

    public ARPGCell getCell(DiscreteCoordinates coordinates) {
        return ((ARPGCell) this.getCell(coordinates.x, coordinates.y));
    }

    public enum Tuto2CellType {
        NULL(0, false),
        WALL(-16777216, false),
        IMPASSABLE(-8750470, false),
        INTERACT(-256, true),
        DOOR(-195580, true),
        WALKABLE(-1, true);

        final int type;
        final boolean isWalkable;

        Tuto2CellType(int type, boolean isWalkable) {
            this.type = type;
            this.isWalkable = isWalkable;
        }

        public static Tuto2CellType toType(int type){
            for (Tuto2CellType ct : Tuto2CellType.values()){
                if (ct.type == type)
                    return ct;
            }

            return NULL;
        }
    }


    public class ARPGCell extends Cell {
        private Tuto2CellType type;

        public ARPGCell(int x, int y, Tuto2CellType type) {
            super(x, y);
            this.type = type;
        }

        public Tuto2CellType getType() {
            return this.type;
        }

        @Override
        protected boolean canLeave(Interactable entity) {
            return true;
        }

        @Override
        protected boolean canEnter(Interactable entity) {
            return this.type.isWalkable;
        }

        @Override
        public boolean isCellInteractable() {
            return true;
        }

        @Override
        public boolean isViewInteractable() {
            return false;
        }

        @Override
        public void acceptInteraction(AreaInteractionVisitor v) {

        }
    }
}
