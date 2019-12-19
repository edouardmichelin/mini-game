package ch.epfl.cs107.play.game.arpg.area;

import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.areagame.actor.Foreground;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.arpg.actor.CastleDoor;
import ch.epfl.cs107.play.game.arpg.actor.DarkLord;
import ch.epfl.cs107.play.game.arpg.actor.LogMonster;
import ch.epfl.cs107.play.game.arpg.config.AreaNames;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.Logic;

public class RouteChateau extends ARPGArea {

    @Override
    protected void createArea() {
        this.registerActor(new Background(this));
        this.registerActor(new Foreground(this));
        this.registerDoors();

        this.registerActor(new DarkLord(this, Orientation.DOWN, new DiscreteCoordinates(10, 10)));
    }

    private void registerDoors() {
        this.registerActor(new Door(
                AreaNames.ROUTE,
                new DiscreteCoordinates(9, 18),
                Logic.TRUE,
                this,
                Orientation.DOWN,
                new DiscreteCoordinates(9, 0),
                new DiscreteCoordinates(10, 0)));
        this.registerActor(new CastleDoor(
                AreaNames.CHATEAU,
                new DiscreteCoordinates(7, 1),
                Logic.FALSE,
                this,
                Orientation.UP,
                new DiscreteCoordinates(9, 13),
                new DiscreteCoordinates(10, 13)));
    }

    @Override
    public String getTitle() {
        return AreaNames.ROUTE_CHATEAU;
    }

}
