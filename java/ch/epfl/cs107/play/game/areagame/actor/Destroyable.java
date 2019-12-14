package ch.epfl.cs107.play.game.areagame.actor;

import ch.epfl.cs107.play.game.rpg.misc.DamageType;

public interface Destroyable {
    float getHp();

    float getMaxHp();

    default boolean isAlive() {
        return this.getHp() > 0;
    }

    /**
     * Returns true if the entity is under a certain health points threshold
     * @return (boolean)
     */
    boolean isWeak();


    /** Gives health points to the entity */
    void strengthen();

    /**
     * Damages the entity by subtracting the specified amounts to its health points
     * @param  damage (int) : The amount of damage the entity will take
     * @param type (DamageType) : The type of the damage
     * @return (int): the remaining health points of the entity
     */
    int damage(int damage, DamageType type);

    /**
     * Destroys the entity
     */
    void destroy();

    void onDying();

}
