package ch.epfl.cs107.play.game.arpg;

import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.arpg.actor.ARPGPlayer;
import ch.epfl.cs107.play.game.arpg.area.*;
import ch.epfl.cs107.play.game.arpg.config.AreaNames;
import ch.epfl.cs107.play.game.arpg.config.Settings;
import ch.epfl.cs107.play.game.rpg.RPG;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Window;

public class ARPG extends RPG {
    private void init() {
        this.createAreas();
        this.setCurrentArea(AreaNames.FERME, true);
        this.initPlayer(new ARPGPlayer(
                (this.getCurrentArea()),
                Orientation.DOWN,
                new DiscreteCoordinates(6, 10)
        ));
    }

    @Override
    public String getTitle() {
        return Settings.GAME_TITLE;
    }

    @Override
    public int getFrameRate() {
        return Settings.FRAME_RATE;
    }

    @Override
    public boolean begin(Window window, FileSystem fileSystem) {
        if (super.begin(window, fileSystem)) {
            init();
            return true;
        } else return false;
    }

    private void createAreas() {
        this.addArea(new Ferme());
        this.addArea(new Village());
        this.addArea(new Route());
        this.addArea(new RouteChateau());
        this.addArea(new Chateau());
        this.addArea(new MaisonFerme());
    }
}
