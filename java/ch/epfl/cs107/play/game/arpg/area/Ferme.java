package ch.epfl.cs107.play.game.arpg.area;

import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.areagame.actor.Foreground;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.arpg.actor.*;
import ch.epfl.cs107.play.game.arpg.config.AreaNames;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.game.rpg.misc.NPCProperties;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.signal.logic.Logic;

public class Ferme extends ARPGArea {

    @Override
    protected void createArea() {
        this.registerActor(new Background(this));
        this.registerActor(new Foreground(this));
        this.registerDoors();
        this.registerFlowers();

        NPCProperties npcProps_1 = new NPCProperties();
        npcProps_1.canMove = false;
        npcProps_1.message = "Coucou, je suis votre premier NPC !";

        NPCProperties npcProps_2 = new NPCProperties();
        npcProps_2.canMove = true;
        npcProps_2.message = "Coucou, je suis votre second NPC !";

        this.registerActor(new LogMonster(this, Orientation.DOWN, new DiscreteCoordinates(9, 3)));
        this.registerActor(new LogMonster(this, Orientation.RIGHT, new DiscreteCoordinates(16, 12)));
        this.registerActor(new NPC(this, Orientation.RIGHT, new DiscreteCoordinates(16, 12), npcProps_1));
        this.registerActor(new NPC(this, Orientation.RIGHT, new DiscreteCoordinates(13, 6), npcProps_2));
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
        this.registerActor(new Door(
                AreaNames.MAISON_FERME,
                new DiscreteCoordinates(4, 2),
                Logic.TRUE,
                this,
                Orientation.UP,
                new DiscreteCoordinates(6,11)
        ));
    }

    private void registerFlowers() {
        this.registerActor(new FlowerBlue(new Vector(15, 3)));
        this.registerActor(new FlowerRed(new Vector(6, 7)));
    }

    @Override
    public String getTitle() {
        return AreaNames.FERME;
    }

}
