package ch.epfl.cs107.play.game.arpg.area;

public enum Areas {
    FERME("zelda/Ferme"),
    VILLAGE("zelda/Village"),
    ROUTE("zelda/Route"),
    ;

    Areas(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }

    private String title;
}
