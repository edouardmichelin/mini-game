package ch.epfl.cs107.play.game.arpg.handler;

import ch.epfl.cs107.play.game.areagame.actor.Destroyable;
import ch.epfl.cs107.play.game.arpg.actor.*;
import ch.epfl.cs107.play.game.arpg.area.ARPGBehavior.ARPGCell;
import ch.epfl.cs107.play.game.rpg.handler.RPGInteractionVisitor;

public interface ARPGInteractionVisitor extends RPGInteractionVisitor {

    /**
     * Simulate and interaction between RPG Interactor and a grass
     * @param grass (ARPGCell), not null
     */
    default void interactWith(Grass grass){
        // by default the interaction is empty
    }

    /**
     * Simulate and interaction between RPG Interactor and a cell
     * @param cell (ARPGCell), not null
     */
    default void interactWith(ARPGCell cell){
        // by default the interaction is empty
    }

    /**
     * Simulate and interaction between RPG Interactor and the player
     * @param player (ARPGPlayer), not null
     */
    default void interactWith(ARPGPlayer player){
        // by default the interaction is empty
    }

    /**
     * Simulate and interaction between RPG Interactor and a coin
     * @param coin (Coin), not null
     */
    default void interactWith(Coin coin){
        // by default the interaction is empty
    }

    /**
     * Simulate and interaction between RPG Interactor and a bomb
     * @param bomb (Bomb), not null
     */
    default void interactWith(Bomb bomb){
        // by default the interaction is empty
    }

    /**
     * Simulate and interaction between RPG Interactor and a castle key
     * @param castleKey (CastleKey), not null
     */
    default void interactWith(CastleKey castleKey){
        // by default the interaction is empty
    }

    /**
     * Simulate and interaction between RPG Interactor and a heart
     * @param heart (Heart), not null
     */
    default void interactWith(Heart heart){
        // by default the interaction is empty
    }

    /**
     * Simulate and interaction between RPG Interactor and a castle door
     * @param castleDoor (CastleDoor), not null
     */
    default void interactWith(CastleDoor castleDoor){
        // by default the interaction is empty
    }

    /**
     * Simulate and interaction between RPG Interactor and a darkLord
     * @param darkLord (DarkLord), not null
     */
    default void interactWith(DarkLord darkLord){
        // by default the interaction is empty
    }

    /**
     * Simulate and interaction between RPG Interactor and a destroyable
     * @param destroyable (Destroyable), not null
     */
    default void interactWith(Destroyable destroyable){
        // by default the interaction is empty
    }

}
