package ch.epfl.cs107.play.game.areagame.actor;

import ch.epfl.cs107.play.game.rpg.misc.DamageType;

public interface Destroyable {
    /**
     * Returns true if the entity is under a certain health points threshold
     * @return (boolean)
     */
    boolean isWeak();


    /** Gives health points to the entity */
    void strengthen();

    /**
     * Damages the entity by substracting the specified ammounts to its health points
     * @param  damage (int) : The ammount of damage the entity will take
     * @param type (DamageType) : The type of the damage
     * @return (int): the remaining health points of the entity
     */
    int damage(int damage, DamageType type);

    /** Destroys the entity */
    void destroy();

}
