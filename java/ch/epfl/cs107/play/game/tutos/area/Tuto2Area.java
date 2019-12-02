package ch.epfl.cs107.play.game.tutos.area;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.io.ResourcePath;
import ch.epfl.cs107.play.game.tutos.Tuto2Behavior;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.window.Image;
import ch.epfl.cs107.play.window.Window;

public abstract class Tuto2Area extends Area {
    private Window window;

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
            setBehavior(new Tuto2Behavior(window, getTitle()));
            createArea();
            return true;
        }
        return false;
    }

    @Override
    public String getTitle() {
        return null;
    }

}