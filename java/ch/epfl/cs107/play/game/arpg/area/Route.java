package ch.epfl.cs107.play.game.arpg.area;

import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.areagame.actor.Foreground;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.arpg.actor.Bomb;
import ch.epfl.cs107.play.game.arpg.actor.FlameSkull;
import ch.epfl.cs107.play.game.arpg.actor.Grass;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.Signal;
import ch.epfl.cs107.play.signal.logic.Logic;

public class Route extends ARPGArea {
    @Override
    protected void createArea() {
        this.registerActor(new Background(this));
        this.registerActor(new Foreground(this));
        this.registerActor(new FlameSkull(this, Orientation.DOWN, new DiscreteCoordinates(5,8)));
        this.registerDoors();
        this.plantGrasses();
    }

    private void registerDoors() {
        this.registerActor(new Door(
                Areas.FERME.getTitle(),
                new DiscreteCoordinates(18, 15),
                Logic.TRUE,
                this,
                Orientation.UP,
                new DiscreteCoordinates(0, 15),
                new DiscreteCoordinates(0, 16)));
        this.registerActor(new Door(
                Areas.VILLAGE.getTitle(),
                new DiscreteCoordinates(29, 18),
                Logic.TRUE,
                this,
                Orientation.DOWN,
                new DiscreteCoordinates(9, 0),
                new DiscreteCoordinates(10, 0)));
        this.registerActor(new Door(
                Areas.ROUTE_CHATEAU.getTitle(),
                new DiscreteCoordinates(9, 1),
                Logic.TRUE,
                this,
                Orientation.UP,
                new DiscreteCoordinates(10, 19),
                new DiscreteCoordinates(9, 19)));
    }

    private void plantGrasses() {
        for (int i = 5; i < 8; i++)
            for (int j = 6; j < 12; j++)
                plantGrass(new DiscreteCoordinates(i, j));
    }

    private void plantGrass(DiscreteCoordinates coordinates) {
        this.registerActor(new Grass(this, Orientation.DOWN, coordinates));
    }

    @Override
    public String getTitle() {
        return Areas.ROUTE.getTitle();
    }

}

