package ch.epfl.cs107.play.game.arpg.area;

import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.areagame.actor.Foreground;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.arpg.actor.CastleKey;
import ch.epfl.cs107.play.game.arpg.actor.Coin;
import ch.epfl.cs107.play.game.arpg.actor.DarkLord;
import ch.epfl.cs107.play.game.arpg.actor.FlameSkull;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.Logic;

public class Ferme extends ARPGArea {

    @Override
    protected void createArea() {
        this.registerActor(new Background(this));
        this.registerActor(new Foreground(this));
        this.registerDoors();

        this.registerActor(new CastleKey(this, Orientation.DOWN, new DiscreteCoordinates(6, 6)));
        this.registerActor(new DarkLord(this, Orientation.RIGHT, new DiscreteCoordinates(6, 6)));
        this.registerActor(new FlameSkull(this, Orientation.DOWN, new DiscreteCoordinates(6, 7)));
    }

    private void registerDoors() {
        this.registerActor(new Door(
                Areas.ROUTE.getTitle(),
                new DiscreteCoordinates(1, 15),
                Logic.TRUE,
                this,
                Orientation.RIGHT,
                new DiscreteCoordinates(19, 15),
                new DiscreteCoordinates(19, 16)));
        this.registerActor(new Door(
                Areas.VILLAGE.getTitle(),
                new DiscreteCoordinates(4, 18),
                Logic.TRUE,
                this,
                Orientation.DOWN,
                new DiscreteCoordinates(4, 0),
                new DiscreteCoordinates(5, 0)));
        this.registerActor(new Door(
                Areas.VILLAGE.getTitle(),
                new DiscreteCoordinates(14, 18),
                Logic.TRUE,
                this,
                Orientation.DOWN,
                new DiscreteCoordinates(13, 0),
                new DiscreteCoordinates(14, 0)));
    }

    @Override
    public String getTitle() {
        return Areas.FERME.getTitle();
    }

}
