package ch.epfl.cs107.play.game.areagame.actor;

public interface FlyableEntity {
    default boolean canFly() {
        return true;
    }
}
