package ch.epfl.cs107.play.game.tutos;

import ch.epfl.cs107.play.game.areagame.AreaGame;
import ch.epfl.cs107.play.game.tutos.actor.SimpleGhost;
import ch.epfl.cs107.play.game.tutos.area.tuto2.Ferme;
import ch.epfl.cs107.play.game.tutos.area.tuto2.Village;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Button;
import ch.epfl.cs107.play.window.Keyboard;
import ch.epfl.cs107.play.window.Window;

public class Tuto2 extends AreaGame {
    private SimpleGhost player;
    private Keyboard keyboard;

    private void init() {
        this.player = new SimpleGhost(new Vector(19, 5), "ghost.1");

        this.keyboard = this.getWindow().getKeyboard();

        this.createAreas();
        this.setCurrentArea("zelda/Village", true);
        this.getCurrentArea().registerActor(this.player);
        this.getCurrentArea().setViewCandidate(this.player);
    }

    private void switchArea() {
        var nextArea = this.getCurrentArea().getTitle().equals("zelda/Village") ? "zelda/Ferme" : "zelda/Village";
        this.getCurrentArea().unregisterActor(this.player);

        this.player.strengthen();
        this.setCurrentArea(nextArea, false);
        this.getCurrentArea().registerActor(this.player);
        this.getCurrentArea().setViewCandidate(this.player);
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
        /*

        Button ARROW_UP = this.keyboard.get(Keyboard.UP);
        Button ARROW_DOWN = this.keyboard.get(Keyboard.DOWN);
        Button ARROW_LEFT = this.keyboard.get(Keyboard.LEFT);
        Button ARROW_RIGHT = this.keyboard.get(Keyboard.RIGHT);

        if (ARROW_UP.isDown()) this.player.moveUp();
        if (ARROW_DOWN.isDown()) this.player.moveDown();
        if (ARROW_LEFT.isDown()) this.player.moveLeft();
        if (ARROW_RIGHT.isDown()) this.player.moveRight();

        if (this.player.isWeak()) switchArea();

        super.update(deltaTime);

         */
    }

    private void createAreas() {
        super.addArea(new Ferme());
        super.addArea(new Village());
    }
}
