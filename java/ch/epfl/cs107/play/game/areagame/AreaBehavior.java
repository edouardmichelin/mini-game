package ch.epfl.cs107.play.game.areagame;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Interactor;
import ch.epfl.cs107.play.game.areagame.io.ResourcePath;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Image;
import ch.epfl.cs107.play.window.Window;


/**
 * AreaBehavior is a basically a map made of Cells. Those cells are used for the game behavior
 */
public abstract class AreaBehavior //implements Interactable.Listener, Interactor.Listener
{
    /// The behavior is an Image of size height x width
    private final Image behaviorMap;
    private final int width, height;
    /// We will convert the image into an array of cells
    private final Cell[][] cells;

    /**
     * AreaBehavior Constructor
     * @param window (Window): graphic context, not null
     * @param name (String): name of the behavior image, not null
     */
    public AreaBehavior(Window window, String name){
        // Load the image
        behaviorMap = window.getImage(ResourcePath.getBehaviors(name), null, false);
        // Get the corresponding dimension and init the array
        height = behaviorMap.getHeight();
        width = behaviorMap.getWidth();
        cells = new Cell[width][height];
    }

    
    /**
     * Contact interaction between interactor and all the cells of the grid
     *
     */
    protected void cellInteractionOf(Interactor interactor){
        for(DiscreteCoordinates dc : interactor.getCurrentCells()){
            if(dc.x < 0 || dc.y < 0 || dc.x >= width || dc.y >= height)
                continue;
            cells[dc.x][dc.y].cellInteractionOf(interactor);
        }
    }

    /**
     * Distance interaction between interactor and all the cells of the grid
     *
     */
    protected void viewInteractionOf(Interactor interactor){
        for(DiscreteCoordinates dc : interactor.getFieldOfViewCells()){
            if(dc.x < 0 || dc.y < 0 || dc.x >= width || dc.y >= height)
                continue;
            cells[dc.x][dc.y].viewInteractionOf(interactor);
        }
    }

    /**
     * Returns true in entity can leave the cells located at coordinates
     * false otherwise
     */
    protected boolean canLeave(Interactable entity, List<DiscreteCoordinates> coordinates) {

        for(DiscreteCoordinates c : coordinates){
            if(c.x < 0 || c.y < 0 || c.x >= width || c.y >= height)
                return false;
            if(!cells[c.x][c.y].canLeave(entity))
                return false;
        }
        return true;
    }

    /**
     * Returns true in entity can enter the cells located at coordinates
     * false otherwise
     */
    protected boolean canEnter(Interactable entity, List<DiscreteCoordinates> coordinates) {
        for(DiscreteCoordinates c : coordinates){
            if(c.x < 0 || c.y < 0 || c.x >= width || c.y >= height)
                return false;
            if(!cells[c.x][c.y].canEnter(entity))
                return false;
        }
        return true;
    }

    /**
     * Removes entity from cells located at coordinates
     */
    protected void leave(Interactable entity, List<DiscreteCoordinates> coordinates) {

        for(DiscreteCoordinates c : coordinates){
            cells[c.x][c.y].leave(entity);
        }

    }

    /**
     * add  entity in cells located at coordinates
     */
    protected void enter(Interactable entity, List<DiscreteCoordinates> coordinates) {
        for(DiscreteCoordinates c : coordinates){
            cells[c.x][c.y].enter(entity);
        }
    }
    
    /**
     * Returns RGB code of cell at (r,c)
     */
    protected int getRGB(int r, int c) {
    	return behaviorMap.getRGB(r, c);
    }
    
    protected int getHeight() {
    	return height;
    }
    
    protected int getWidth() {
    	return width;
    }
    
    protected void setCell(int x,int y, Cell cell) {
    	cells[x][y] = cell;
    }
    
    protected Cell getCell(int x, int y) {
    	return cells[x][y];
    }

    public abstract class Cell implements Interactable{

        /// Content of the cell as a set of Interactable
        private Set<Interactable> entities;
        private DiscreteCoordinates coordinates;


        /**
         * Default Cell constructor
         * @param x (int): x-coordinate of this cell
         * @param y (int): y-coordinate of this cell
         */
        public Cell(int x, int y){
            entities = new HashSet<>();
            coordinates = new DiscreteCoordinates(x, y);
        }


        /**
         * Do the given interactor interacts with all Interactable sharing the same cell
         * @param interactor (Interactor), not null
         */
        private void cellInteractionOf(Interactor interactor){ // REFACTOR: must become private with inner class
            interactor.interactWith(this);
            for(Interactable interactable : entities){
                if(interactable.isCellInteractable())
                    interactor.interactWith(interactable);
            }
        }

        /**
         * Do the given interactor interacts with all Interactable sharing the same cell
         * @param interactor (Interactor), not null
         */
        private void viewInteractionOf(Interactor interactor){ // REFACTOR: must become private with inner class
            interactor.interactWith(this);
            for(Interactable interactable : entities){
                if(interactable.isViewInteractable())
                    interactor.interactWith(interactable);
            }
        }

        /**
         * Do the given interactable enter into this Cell
         * @param entity (Interactable), not null
         */
        protected void enter(Interactable entity) {
            entities.add(entity);
        }

        /**
         * Do the given Interactable leave this Cell
         * @param entity (Interactable), not null
         */
        protected void leave(Interactable entity) {
            entities.remove(entity);
        }

        /**
         * Indicate if the given Interactable can leave this Cell
         * @param entity (Interactable), not null
         * @return (boolean): true if entity can leave
         */
        protected abstract boolean canLeave(Interactable entity);

        /**
         * Indicate if the given Interactable can enter this Cell
         * @param entity (Interactable), not null
         * @return (boolean): true if entity can enter
         */
        protected abstract boolean canEnter(Interactable entity);

        protected boolean hasNonTraversableContent() {
            for (Interactable entity : entities) {
                if (entity.takeCellSpace())
                    return true;
            }
            return false;
        }
        /// Cell implements Interactable

        @Override
        public boolean takeCellSpace(){
            return false;
        }

        @Override
        public List<DiscreteCoordinates> getCurrentCells() {
            return Collections.singletonList(coordinates);
        }
    }
}
