package ch.epfl.cs107.play.game.tutos.area.tuto1;

import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.tutos.actor.SimpleGhost;
import ch.epfl.cs107.play.game.tutos.area.SimpleArea;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Vector;

public class Ferme extends SimpleArea {
    @Override
    protected void createArea() {
        registerActor(new Background(this));
    }

    @Override
    public String getTitle() {
        return "zelda/Ferme";
    }
}
