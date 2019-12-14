package ch.epfl.cs107.play.game.arpg.gui;

import ch.epfl.cs107.play.window.Canvas;

public interface ARPGStatusGUI {

    void draw(Canvas canvas);

    void setCurrentItem(int itemId);

    void setHealthPoints(float hp);
}
