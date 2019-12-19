package ch.epfl.cs107.play.game.arpg.area;

import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.arpg.actor.Bow;
import ch.epfl.cs107.play.game.arpg.actor.UglyGoblin;
import ch.epfl.cs107.play.game.arpg.config.AreaNames;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.Logic;

public class Grotte extends ARPGArea {
    @Override
    protected void createArea() {
        this.registerActor(new Background(this));
        registerDoors();

        this.registerActor(new UglyGoblin(this, Orientation.DOWN, new DiscreteCoordinates(8, 7)));
    }

    private void registerDoors() {
        this.registerActor(new Door(
                AreaNames.VILLAGE,
                new DiscreteCoordinates(25, 17),
                Logic.TRUE,
                this,
                Orientation.UP,
                new DiscreteCoordinates(8,2)
        ));
    }


    @Override
    public String getTitle() {
        return AreaNames.GROTTE;
    }

}

