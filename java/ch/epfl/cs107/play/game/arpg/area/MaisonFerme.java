package ch.epfl.cs107.play.game.arpg.area;

import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.areagame.actor.Foreground;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.arpg.actor.NPC;
import ch.epfl.cs107.play.game.arpg.config.AreaNames;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.game.rpg.misc.NPCProperties;
import ch.epfl.cs107.play.io.XMLTexts;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.Logic;

public class MaisonFerme extends ARPGArea {
    @Override
    protected void createArea() {
        this.registerActor(new Background(this));
        this.registerActor(new Foreground(this));

        NPCProperties prop1 = new NPCProperties(XMLTexts.getText("npcMaisonFerme1"), false);
        NPCProperties prop2 = new NPCProperties(XMLTexts.getText("npcMaisonFerme2"), false);

        this.registerActor(new NPC(this, Orientation.LEFT, new DiscreteCoordinates(7, 3), prop1));
        this.registerActor(new NPC(this, Orientation.LEFT, new DiscreteCoordinates(7, 4), prop2));

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

