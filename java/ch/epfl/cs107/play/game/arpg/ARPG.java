package ch.epfl.cs107.play.game.arpg;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.arpg.actor.ARPGPlayer;
import ch.epfl.cs107.play.game.arpg.area.Ferme;
import ch.epfl.cs107.play.game.arpg.area.Village;
import ch.epfl.cs107.play.game.arpg.area.Route;
import ch.epfl.cs107.play.game.rpg.RPG;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.window.Window;

import java.util.ArrayList;
import java.util.List;

public class ARPG extends RPG {
    private ARPGPlayer player;
    private List<String> areas = new ArrayList<>();
    private int currentAreaIndex = 0;

    private void init() {
        this.createAreas();
        this.setCurrentArea(areas.get(this.currentAreaIndex), true);
        this.player = new ARPGPlayer(
                (this.getCurrentArea()),
                Orientation.DOWN,
                this.getCurrentArea().getStartingCoordinates(), "ghost.1"
        );
    }

    private void switchArea() {
        this.currentAreaIndex = (this.currentAreaIndex + 1) % this.areas.size();
        String nextArea = areas.get(this.currentAreaIndex);

        this.player.leaveArea(this.getCurrentArea());
        this.setCurrentArea(nextArea, false);
        this.player.enterArea(this.getCurrentArea(), this.getCurrentArea().getStartingCoordinates());
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
        createArea(new Ferme());
        createArea(new Village());
        createArea(new Route());
    }

    private void createArea(Area area) {
        super.addArea(area);
        areas.add(area.getTitle());
    }
}
