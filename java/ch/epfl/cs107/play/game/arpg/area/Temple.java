package ch.epfl.cs107.play.game.arpg.area;

import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.areagame.actor.Foreground;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.arpg.actor.*;
import ch.epfl.cs107.play.game.arpg.config.AreaNames;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.game.rpg.misc.NPCProperties;
import ch.epfl.cs107.play.io.XMLTexts;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.Logic;

public class Temple extends ARPGArea {

    @Override
    protected void createArea() {
        this.registerActor(new Background(this));
        this.registerActor(new Foreground(this));

        NPCProperties prop1 = new NPCProperties(XMLTexts.getText("npcTemple1"), false);

        this.registerActor(new NPC(this, Orientation.RIGHT, new DiscreteCoordinates(2, 3), prop1));

        this.registerActor(new Staff(this, Orientation.DOWN, new DiscreteCoordinates(4, 3)));

        this.registerDoors();
    }

    private void registerDoors() {
        this.registerActor(new Door(
                AreaNames.ROUTE_TEMPLE,
                new DiscreteCoordinates(5, 5),
                Logic.TRUE,
                this,
                Orientation.DOWN,
                new DiscreteCoordinates(4, 0)));
    }

    @Override
    public String getTitle() {
        return AreaNames.TEMPLE;
    }

}
