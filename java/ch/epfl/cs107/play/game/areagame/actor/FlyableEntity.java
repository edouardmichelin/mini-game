package ch.epfl.cs107.play.game.areagame.actor;

/**
 * classes that implements this will be able to "walk" on impassable cells
 */
public interface FlyableEntity {
    default boolean canFly() {
        return true;
    }
}
