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

public class Village extends ARPGArea {

    @Override
    protected void createArea() {
        this.registerActor(new Background(this));
        this.registerActor(new Foreground(this));
        this.registerDoors();
        registerFlowers();

        NPCProperties prop1 = new NPCProperties(XMLTexts.getText("npcVillage1"), false);
        NPCProperties prop2 = new NPCProperties(XMLTexts.getText("npcVillage2"), false);
        NPCProperties prop3 = new NPCProperties(XMLTexts.getText("npcVillage3"), true);

        this.registerActor(new NPC(this, Orientation.DOWN, new DiscreteCoordinates(17, 11), prop1));
        this.registerActor(new NPC(this, Orientation.DOWN, new DiscreteCoordinates(21, 3), prop2));
        this.registerActor(new NPC(this, Orientation.DOWN, new DiscreteCoordinates(11, 6), prop3));

        this.registerActor(new Sword(this, Orientation.DOWN, new DiscreteCoordinates(25, 5)));
    }

    private void registerDoors() {
        this.registerActor(new Door(
                AreaNames.FERME,
                new DiscreteCoordinates(4, 1),
                Logic.TRUE,
                this,
                Orientation.UP,
                new DiscreteCoordinates(4, 19),
                new DiscreteCoordinates(5, 19)));
        this.registerActor(new Door(
                AreaNames.FERME,
                new DiscreteCoordinates(14, 1),
                Logic.TRUE,
                this,
                Orientation.UP,
                new DiscreteCoordinates(13, 19),
                new DiscreteCoordinates(14, 19),
                new DiscreteCoordinates(15, 19)));
        this.registerActor(new Door(
                AreaNames.ROUTE,
                new DiscreteCoordinates(9, 1),
                Logic.TRUE,
                this,
                Orientation.UP,
                new DiscreteCoordinates(29, 19),
                new DiscreteCoordinates(30, 19)));
        this.registerActor(new CaveDoor(
                AreaNames.GROTTE,
                new DiscreteCoordinates(8, 3),
                Logic.FALSE,
                this,
                Orientation.UP,
                new DiscreteCoordinates(25, 18)));
    }

    private void registerFlowers() {
        this.registerActor(new FlowerBlue(new Vector(27, 10)));
        this.registerActor(new FlowerRed(new Vector(5, 6)));
        this.registerActor(new FlowerBlue(new Vector(9, 15)));
    }

    @Override
    public String getTitle() {
        return AreaNames.VILLAGE;
    }

}
