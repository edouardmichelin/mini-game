package ch.epfl.cs107.play.game.arpg.area;

import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.areagame.actor.Foreground;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.arpg.actor.*;
import ch.epfl.cs107.play.game.arpg.config.AreaNames;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.Logic;

public class Ferme extends ARPGArea {

    @Override
    protected void createArea() {
        this.registerActor(new Background(this));
        this.registerActor(new Foreground(this));
        this.registerDoors();

        this.registerActor(new LogMonster(this, Orientation.DOWN, new DiscreteCoordinates(9, 3)));
        this.registerActor(new LogMonster(this, Orientation.RIGHT, new DiscreteCoordinates(16, 12)));
    }

    private void registerDoors() {
        this.registerActor(new Door(
                AreaNames.ROUTE,
                new DiscreteCoordinates(1, 15),
                Logic.TRUE,
                this,
                Orientation.RIGHT,
                new DiscreteCoordinates(19, 15),
                new DiscreteCoordinates(19, 16)));
        this.registerActor(new Door(
                AreaNames.VILLAGE,
                new DiscreteCoordinates(4, 18),
                Logic.TRUE,
                this,
                Orientation.DOWN,
                new DiscreteCoordinates(4, 0),
                new DiscreteCoordinates(5, 0)));
        this.registerActor(new Door(
                AreaNames.VILLAGE,
                new DiscreteCoordinates(14, 18),
                Logic.TRUE,
                this,
                Orientation.DOWN,
                new DiscreteCoordinates(13, 0),
                new DiscreteCoordinates(14, 0)));
    }

    @Override
    public String getTitle() {
        return AreaNames.FERME;
    }

}
