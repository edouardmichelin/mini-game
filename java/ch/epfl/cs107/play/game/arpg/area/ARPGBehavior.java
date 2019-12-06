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
                this.setCell(x, y, new ARPGCell(x, y, ARPGCellType.toType(getRGB((getHeight() - 1 - y), x))));
    }

    public ARPGCell getCell(DiscreteCoordinates coordinates) {
        return ((ARPGCell) this.getCell(coordinates.x, coordinates.y));
    }

    public enum ARPGCellType {
        NULL(0, false),
        WALL(-16777216, false),
        IMPASSABLE(-8750470, false),
        INTERACT(-256, true),
        DOOR(-195580, true),
        WALKABLE(-1, true);

        final int type;
        final boolean isWalkable;

        ARPGCellType(int type, boolean isWalkable) {
            this.type = type;
            this.isWalkable = isWalkable;
        }

        public static ARPGCellType toType(int type){
            for (ARPGCellType ct : ARPGCellType.values()){
                if (ct.type == type)
                    return ct;
            }

            return NULL;
        }
    }


    public class ARPGCell extends Cell {
        private ARPGCellType type;

        public ARPGCell(int x, int y, ARPGCellType type) {
            super(x, y);
            this.type = type;
        }

        public ARPGCellType getType() {
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
