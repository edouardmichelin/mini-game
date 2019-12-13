package ch.epfl.cs107.play.game.arpg.handler;

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
     * @param coin (ARPGPlayer), not null
     */
    default void interactWith(Coin coin){
        // by default the interaction is empty
    }

    /**
     * Simulate and interaction between RPG Interactor and a heart
     * @param heart (ARPGPlayer), not null
     */
    default void interactWith(Heart heart){
        // by default the interaction is empty
    }

}
