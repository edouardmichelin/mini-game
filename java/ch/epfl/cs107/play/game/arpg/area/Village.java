package ch.epfl.cs107.play.game.arpg.area;

import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.areagame.actor.Foreground;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.arpg.actor.*;
import ch.epfl.cs107.play.game.arpg.config.AreaNames;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.game.rpg.misc.NPCProperties;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.Logic;

public class Village extends ARPGArea {

    @Override
    protected void createArea() {
        this.registerActor(new Background(this));
        this.registerActor(new Foreground(this));
        this.registerDoors();

        NPCProperties prop1 = new NPCProperties("J'vendrais bien d'la limonade mais mes parents m'ont pas implémenté la fonction. Tu peux éventuellement sucer tes pièces d'or, ça a ", false);
        NPCProperties prop2 = new NPCProperties("La légende", false);

        this.registerActor(new NPC(this, Orientation.DOWN, new DiscreteCoordinates(17, 11), prop1));

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

    @Override
    public String getTitle() {
        return AreaNames.VILLAGE;
    }

}
