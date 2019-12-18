package ch.epfl.cs107.play.game.arpg.area;

import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.areagame.actor.Foreground;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.arpg.actor.CastleDoor;
import ch.epfl.cs107.play.game.arpg.config.AreaNames;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.Logic;

public class Chateau extends ARPGArea {

    @Override
    protected void createArea() {
        this.registerActor(new Background(this));
        this.registerActor(new Foreground(this));
        this.registerDoors();
    }

    private void registerDoors() {
        this.registerActor(new Door(
                AreaNames.ROUTE_CHATEAU,
                new DiscreteCoordinates(9, 12),
                Logic.TRUE,
                this,
                Orientation.DOWN,
                new DiscreteCoordinates(7, 0),
                new DiscreteCoordinates(8, 0)));
    }

    @Override
    public String getTitle() {
        return AreaNames.CHATEAU;
    }

}