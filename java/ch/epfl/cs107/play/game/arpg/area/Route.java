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
import ch.epfl.cs107.play.signal.Signal;
import ch.epfl.cs107.play.signal.logic.Logic;

public class Route extends ARPGArea {
    @Override
    protected void createArea() {
        this.registerActor(new Background(this));
        this.registerActor(new Foreground(this));
        this.registerActor(new WaterFall(new Vector(15, 3)));
        this.registerDoors();
        this.plantGrasses();
        this.registerFlowers();

        NPCProperties prop1 = new NPCProperties(XMLTexts.getText("npcRoute1"), false);
        NPCProperties prop2 = new NPCProperties(XMLTexts.getText("npcRoute2"), true);

        this.registerActor(new NPC(this, Orientation.DOWN, new DiscreteCoordinates(1, 16), prop1));
        this.registerActor(new NPC(this, Orientation.RIGHT, new DiscreteCoordinates(7, 17), prop2));

        Orb orb = new Orb(this, Orientation.UP, new DiscreteCoordinates(18, 8));
        this.registerActor(new Bridge(this, Orientation.RIGHT, new DiscreteCoordinates(16, 10), orb));
        this.registerActor(orb);

        this.registerActor(new DefusedBomb(this, Orientation.DOWN, new DiscreteCoordinates(13, 13)));

        this.registerActor(new LogMonster(this, Orientation.RIGHT, new DiscreteCoordinates(9, 9)));
        this.registerActor(new LogMonster(this, Orientation.DOWN, new DiscreteCoordinates(12, 7)));
    }

    private void registerDoors() {
        this.registerActor(new Door(
                AreaNames.FERME,
                new DiscreteCoordinates(18, 15),
                Logic.TRUE,
                this,
                Orientation.UP,
                new DiscreteCoordinates(0, 15),
                new DiscreteCoordinates(0, 16)));
        this.registerActor(new Door(
                AreaNames.VILLAGE,
                new DiscreteCoordinates(29, 18),
                Logic.TRUE,
                this,
                Orientation.DOWN,
                new DiscreteCoordinates(9, 0),
                new DiscreteCoordinates(10, 0)));
        this.registerActor(new Door(
                AreaNames.ROUTE_CHATEAU,
                new DiscreteCoordinates(9, 1),
                Logic.TRUE,
                this,
                Orientation.UP,
                new DiscreteCoordinates(10, 19),
                new DiscreteCoordinates(9, 19)));
        this.registerActor(new Door(
                AreaNames.ROUTE_TEMPLE,
                new DiscreteCoordinates(1, 5),
                Logic.TRUE,
                this,
                Orientation.RIGHT,
                new DiscreteCoordinates(19, 11),
                new DiscreteCoordinates(19, 10),
                new DiscreteCoordinates(19, 9),
                new DiscreteCoordinates(19, 8)));
    }

    private void plantGrasses() {
        for (int i = 5; i < 8; i++) {
            for (int j = 6; j < 12; j++)
                plantGrass(new DiscreteCoordinates(i, j));
        }

        plantGrass(new DiscreteCoordinates(2, 14));
        plantGrass(new DiscreteCoordinates(2, 15));
        plantGrass(new DiscreteCoordinates(2, 16));

        plantGrass(new DiscreteCoordinates(8, 2));
        plantGrass(new DiscreteCoordinates(8, 3));
        plantGrass(new DiscreteCoordinates(9, 3));
        plantGrass(new DiscreteCoordinates(10, 3));
        plantGrass(new DiscreteCoordinates(11, 3));
        plantGrass(new DiscreteCoordinates(11, 2));
    }

    private void plantGrass(DiscreteCoordinates coordinates) {
        this.registerActor(new Grass(this, Orientation.DOWN, coordinates));
    }

    private void registerFlowers() {
        this.registerActor(new FlowerBlue(new Vector(12, 7)));
        this.registerActor(new FlowerRed(new Vector(5, 17)));
        this.registerActor(new FlowerRed(new Vector(3, 10)));
    }


    @Override
    public String getTitle() {
        return AreaNames.ROUTE;
    }

}

