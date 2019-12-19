package ch.epfl.cs107.play.game.rpg.misc;

public class NPCProperties {
    private String message;
    private boolean canMove;

    public NPCProperties (String message, boolean canMove) {
        this.message = message;
        this.canMove = canMove;
    }

    public String getMessage() {
        return this.message;
    }

    public boolean canMove() {
        return this.canMove;
    }
}
