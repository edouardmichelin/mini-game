package ch.epfl.cs107.play.game.rpg.misc;

import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RandomGenerator;
import ch.epfl.cs107.play.math.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class Helpers {
    private static Random RANDOM = RandomGenerator.getInstance();

    public static int random(int lowerBound, int upperBound) {
        return RANDOM.nextInt(upperBound - lowerBound + 1) + lowerBound;
    }

    public static List<DiscreteCoordinates> getCellsInRadius(DiscreteCoordinates center, int radius) {
        List<DiscreteCoordinates> cir = new ArrayList<>();
        for (int x = -radius; x < radius + 1; x++)
            for (int y = -radius; y < radius + 1; y++)
                cir.add(center.jump(x, y));

        return cir;
    }

    public static List<DiscreteCoordinates> getCellsInRange(DiscreteCoordinates startingPoint, Orientation orientation, int range) {
        List<DiscreteCoordinates> dc = new ArrayList<>();
        Vector direction = orientation.toVector();

        dc.add(startingPoint.jump(direction));

        for (int i = 1; i < range; i++)
            dc.add(dc.get(i - 1).jump(direction));

        return dc;
    }
}
