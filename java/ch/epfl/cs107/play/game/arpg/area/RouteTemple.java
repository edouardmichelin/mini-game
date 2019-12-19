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

public class RouteTemple extends ARPGArea {

    @Override
    protected void createArea() {
        this.registerActor(new Background(this));
        this.registerActor(new Foreground(this));
        this.registerDoors();
    }

    private void registerDoors() {
        this.registerActor(new Door(
                AreaNames.ROUTE,
                new DiscreteCoordinates(18, 10),
                Logic.TRUE,
                this,
                Orientation.LEFT,
                new DiscreteCoordinates(0, 6),
                new DiscreteCoordinates(0, 5),
                new DiscreteCoordinates(0, 4)));
        this.registerActor(new Door(
                AreaNames.TEMPLE,
                new DiscreteCoordinates(4, 1),
                Logic.TRUE,
                this,
                Orientation.UP,
                new DiscreteCoordinates(5, 6)));
    }

    @Override
    public String getTitle() {
        return AreaNames.ROUTE_TEMPLE;
    }

}
