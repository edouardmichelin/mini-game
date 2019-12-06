package ch.epfl.cs107.play.game.arpg.area;

import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.areagame.actor.Foreground;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

public class Route extends ARPGArea {
    @Override
    protected void createArea() {
        registerActor(new Background(this));
        registerActor(new Foreground(this));
    }

    @Override
    public String getTitle() {
        return "zelda/Route";
    }

    public DiscreteCoordinates getStartingCoordinates() {
        return new DiscreteCoordinates(2,10);
    }
}

