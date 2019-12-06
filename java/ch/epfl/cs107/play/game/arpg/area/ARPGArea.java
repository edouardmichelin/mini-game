package ch.epfl.cs107.play.game.arpg.area;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.AreaBehavior;
import ch.epfl.cs107.play.game.areagame.io.ResourcePath;
import ch.epfl.cs107.play.game.tutos.Tuto2Behavior;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Image;
import ch.epfl.cs107.play.window.Window;

public abstract class ARPGArea extends Area {
    private Window window;
    private Tuto2Behavior ab;

    /**
     * Create the area by adding all its actors
     * called by the begin method, when the area starts to play
     */
    protected abstract void createArea();

    @Override
    public float getCameraScaleFactor() {
        return 20f;
    }

    @Override
    public boolean begin(Window window, FileSystem fileSystem) {
        this.window = window;
        if (super.begin(window, fileSystem)) {
            this.ab = new Tuto2Behavior(window, getTitle());
            setBehavior(this.ab);

            createArea();
            return true;
        }
        return false;
    }

    @Override
    public String getTitle() {
        return null;
    }

    public Tuto2Behavior.Tuto2Cell getCell(DiscreteCoordinates coordinates) {
        return this.ab.getCell(coordinates);
    }

}