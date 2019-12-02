package ch.epfl.cs107.play.game.tutos;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.AreaGame;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.tutos.actor.GhostPlayer;
import ch.epfl.cs107.play.game.tutos.actor.SimpleGhost;
import ch.epfl.cs107.play.game.tutos.area.Tuto2Area;
import ch.epfl.cs107.play.game.tutos.area.tuto2.Ferme;
import ch.epfl.cs107.play.game.tutos.area.tuto2.Village;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Button;
import ch.epfl.cs107.play.window.Keyboard;
import ch.epfl.cs107.play.window.Window;

public class Tuto2 extends AreaGame {
    private GhostPlayer player;

    private void init() {
        this.createAreas();
        this.setCurrentArea("zelda/Village", true);
        this.player = new GhostPlayer(
                (this.getCurrentArea()),
                Orientation.DOWN,
                new DiscreteCoordinates(18, 7), "ghost.1"
        );
    }

    private void switchArea() {
        String nextArea = this.getCurrentArea().getTitle().equals("zelda/Village") ?
                "zelda/Ferme" :
                "zelda/Village";

        this.player.leaveArea(this.getCurrentArea());
        this.setCurrentArea(nextArea, false);
        this.player.enterArea(this.getCurrentArea(), this.getCurrentArea().getStartingCoordinates());
    }

    @Override
    public String getTitle() {
        return "Tuto2";
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

        if (((Tuto2Area) this.getCurrentArea())
                .getCellType(this.player.getPosition())
                .equals(Tuto2Behavior.Tuto2CellType.DOOR) ) switchArea();

        super.update(deltaTime);
    }

    private void createAreas() {
        super.addArea(new Ferme());
        super.addArea(new Village());
    }
}
