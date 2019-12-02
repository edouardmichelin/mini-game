package ch.epfl.cs107.play.game.tutos.area.tuto2;

import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.tutos.actor.SimpleGhost;
import ch.epfl.cs107.play.game.tutos.area.SimpleArea;
import ch.epfl.cs107.play.game.tutos.area.Tuto2Area;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Vector;

public class Village extends Tuto2Area {
    @Override
    protected void createArea() {
        registerActor(new Background(this));

        SimpleGhost ghost1 = new SimpleGhost(new Vector(18, 7), "ghost.2");
        SimpleGhost ghost2 = new SimpleGhost(new Vector(14, 9), "ghost.2");
        SimpleGhost ghost3 = new SimpleGhost(new Vector(22, 5), "ghost.2");
        registerActor(ghost1);
        registerActor(ghost2);
        registerActor(ghost3);
    }

    @Override
    public String getTitle() {
        return "zelda/Village";
    }

    public DiscreteCoordinates getStartingCoordinates() {
        return new DiscreteCoordinates(5, 15    );
    }
}
