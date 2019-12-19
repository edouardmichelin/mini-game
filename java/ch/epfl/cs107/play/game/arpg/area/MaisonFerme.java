package ch.epfl.cs107.play.game.arpg.area;

import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.arpg.config.AreaNames;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.Logic;

public class MaisonFerme extends ARPGArea {
    @Override
    protected void createArea() {
        this.registerActor(new Background(this));

        registerDoors();
    }

    private void registerDoors() {
        this.registerActor(new Door(
                AreaNames.FERME,
                new DiscreteCoordinates(6, 10),
                Logic.TRUE,
                this,
                Orientation.DOWN,
                new DiscreteCoordinates(3,0),
                new DiscreteCoordinates(4, 0)
        ));
    }


    @Override
    public String getTitle() {
        return AreaNames.MAISON_FERME;
    }

}

