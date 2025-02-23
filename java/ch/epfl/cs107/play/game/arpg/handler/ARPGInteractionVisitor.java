package ch.epfl.cs107.play.game.arpg.handler;

import ch.epfl.cs107.play.game.areagame.actor.Destroyable;
import ch.epfl.cs107.play.game.arpg.actor.*;
import ch.epfl.cs107.play.game.arpg.area.ARPGBehavior.ARPGCell;
import ch.epfl.cs107.play.game.rpg.actor.Monster;
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
     * Simulate and interaction between RPG Interactor and a defused bomb
     * @param defusedBomb (DefusedBomb), not null
     */
    default void interactWith(DefusedBomb defusedBomb){
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
     * Simulate and interaction between RPG Interactor and a sword
     * @param sword (Sword), not null
     */
    default void interactWith(Sword sword){
        // by default the interaction is empty
    }

    /**
     * Simulate and interaction between RPG Interactor and a staff
     * @param staff (Staff), not null
     */
    default void interactWith(Staff staff){
        // by default the interaction is empty
    }

    /**
     * Simulate and interaction between RPG Interactor and a orb
     * @param orb (Orb), not null
     */
    default void interactWith(Orb orb){
        // by default the interaction is empty
    }

    /**
     * Simulate and interaction between RPG Interactor and a bow
     * @param bow (Bow), not null
     */
    default void interactWith(Bow bow){
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
     * Simulate and interaction between RPG Interactor and a cave door
     * @param caveDoor (CaveDoor), not null
     */
    default void interactWith(CaveDoor caveDoor){
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
     * Simulate and interaction between RPG Interactor and a logMonster
     * @param logMonster (LogMonster), not null
     */
    default void interactWith(LogMonster logMonster){
        // by default the interaction is empty
    }

    /**
     * Simulate and interaction between RPG Interactor and a fireSpell
     * @param fireSpell (FireSpell), not null
     */
    default void interactWith(FireSpell fireSpell){
        // by default the interaction is empty
    }

    /**
     * Simulate and interaction between RPG Interactor and a flameSkull
     * @param flameSkull (FlameSkull), not null
     */
    default void interactWith(FlameSkull flameSkull){
        // by default the interaction is empty
    }

    /**
     * Simulate and interaction between RPG Interactor and a uglyGoblin
     * @param uglyGoblin (UglyGoblin), not null
     */
    default void interactWith(UglyGoblin uglyGoblin){
        // by default the interaction is empty
    }

    /**
     * Simulate and interaction between RPG Interactor and a arrow
     * @param arrow (Arrow), not null
     */
    default void interactWith(Arrow arrow){
        // by default the interaction is empty
    }

    /**
     * Simulate and interaction between RPG Interactor and a arrow
     * @param magicWaterProjectile (MagicWaterProjectile), not null
     */
    default void interactWith(MagicWaterProjectile magicWaterProjectile){
        // by default the interaction is empty
    }

    /**
     * Simulate and interaction between RPG Interactor and a arrow
     * @param swordSlash (SwordSlash), not null
     */
    default void interactWith(SwordSlash swordSlash){
        // by default the interaction is empty
    }

    /**
     * Simulate and interaction between RPG Interactor and a npc
     * @param npc (NPC), not null
     */
    default void interactWith(NPC npc){
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
