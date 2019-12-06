package ch.epfl.cs107.play.game.arpg.handler;

import ch.epfl.cs107.play.game.arpg.actor.ARPGPlayer;
import ch.epfl.cs107.play.game.arpg.area.ARPGBehavior.ARPGCell;
import ch.epfl.cs107.play.game.rpg.handler.RPGInteractionVisitor;

public interface ARPGInteractionVisitor extends RPGInteractionVisitor {

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

}
