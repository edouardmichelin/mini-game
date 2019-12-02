package ch.epfl.cs107.play.game.tutos.area.tuto1;

import ch.epfl.cs107.play.game.tutos.actor.SimpleGhost;
import ch.epfl.cs107.play.game.tutos.area.SimpleArea;
import ch.epfl.cs107.play.math.Vector;

public class Village extends SimpleArea {
    @Override
    protected void createArea() {
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
}
