package ch.epfl.cs107.play.game.tutos;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.AreaGame;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.tutos.actor.GhostPlayer;
import ch.epfl.cs107.play.game.tutos.area.tuto2.Ferme;
import ch.epfl.cs107.play.game.tutos.area.tuto2.Village;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.window.Window;

import java.util.ArrayList;
import java.util.List;

public class Tuto2 extends AreaGame {
    private GhostPlayer player;
    private List<String> areas = new ArrayList<>();
    private int currentAreaIndex = 0;

    private void init() {
        this.createAreas();
        this.setCurrentArea(areas.get(this.currentAreaIndex), true);
        this.player = new GhostPlayer(
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
        return "Tuto 2";
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

    @Override
    public void update(float deltaTime) {

        if (this.player.getIsNextToDoor()) switchArea();

        super.update(deltaTime);
    }

    private void createAreas() {
        Area ferme = new Ferme();
        Area village = new Village();

        super.addArea(ferme);
        super.addArea(village);

        areas.add(ferme.getTitle());
        areas.add(village.getTitle());
    }
}
