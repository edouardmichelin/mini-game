package ch.epfl.cs107.play.game.arpg;

import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.arpg.actor.ARPGPlayer;
import ch.epfl.cs107.play.game.arpg.area.Areas;
import ch.epfl.cs107.play.game.arpg.area.Ferme;
import ch.epfl.cs107.play.game.arpg.area.Village;
import ch.epfl.cs107.play.game.arpg.area.Route;
import ch.epfl.cs107.play.game.rpg.RPG;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Window;

public class ARPG extends RPG {
    private void init() {
        this.createAreas();
        this.setCurrentArea(Areas.FERME.getTitle(), true);
        this.initPlayer(new ARPGPlayer(
                (this.getCurrentArea()),
                Orientation.DOWN,
                new DiscreteCoordinates(6, 10),
                "zelda/player"
        ));
    }

    @Override
    public String getTitle() {
        return "Step1";
    }

    @Override
    public int getFrameRate() {
        return 60;
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
    }
}
