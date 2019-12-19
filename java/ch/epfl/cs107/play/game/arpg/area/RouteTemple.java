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
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.signal.logic.Logic;

public class RouteTemple extends ARPGArea {

    @Override
    protected void createArea() {
        this.registerActor(new Background(this));
        this.registerActor(new Foreground(this));
        this.registerDoors();

        NPCProperties prop1 = new NPCProperties(XMLTexts.getText("npcRouteTemple1"), false);
        this.registerActor(new FlowerBlue(new Vector(1, 6)));

        this.registerActor(new NPC(this, Orientation.LEFT, new DiscreteCoordinates(7, 5), prop1));
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
