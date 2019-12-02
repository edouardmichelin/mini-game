package ch.epfl.cs107.play.game.tutos;

import ch.epfl.cs107.play.game.areagame.AreaBehavior;
import ch.epfl.cs107.play.game.areagame.Cell;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Window;

import java.lang.annotation.Inherited;

public class Tuto2Behavior extends AreaBehavior {
    /**
     * AreaBehavior Constructor
     *
     * @param window (Window): graphic context, not null
     * @param name   (String): name of the behavior image, not null
     */
    public Tuto2Behavior(Window window, String name) {
        super(window, name);
        for (int x = 0; x < getWidth(); x++)
            for (int y = 0; y < getHeight(); y++)
                this.setCell(x, y, new Tuto2Cell(x, y, Tuto2CellType.toType(getRGB((getHeight() - 1 - y), x))));
    }

    public Tuto2CellType getCellType(Vector position) {
        return ((Tuto2Cell) this.getCell((int) position.x, (int) position.y)).getCellType();
    }

    public enum Tuto2CellType {
        NULL(0, false),
        WALL(-16777216, false), // #000000 RGB code of black
        IMPASSABLE(-8750470, false), // #7A7A7A, RGB color of grey
        INTERACT(-256, true), // #FFFF00, RGB color of yellow
        DOOR(-195580, true), // #FD0404, RGB color of red
        WALKABLE(-1, true),; // #FFFFFF, RGB color of white

        final int type;
        final boolean isWalkable;

        Tuto2CellType(int type, boolean isWalkable) {
            this.type = type;
            this.isWalkable = isWalkable;
        }

        static Tuto2CellType toType(int type) {
            switch (type) {
                case -16777216: return WALL;
                case -8750470: return IMPASSABLE;
                case -256: return INTERACT;
                case -195580: return DOOR;
                case -1: return WALKABLE;
                case 0:
                default: return NULL;
            }
        }
    }


    class Tuto2Cell extends Cell {
        private Tuto2CellType cellType;

        public Tuto2Cell(int x, int y, Tuto2CellType type) {
            super(x, y);
            this.cellType = type;
        }

        public Tuto2CellType getCellType() {
            return this.cellType;
        }

        @Override
        protected boolean canLeave(Interactable entity) {
            return true;
        }

        @Override
        protected boolean canEnter(Interactable entity) {
            return this.cellType.isWalkable;
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
