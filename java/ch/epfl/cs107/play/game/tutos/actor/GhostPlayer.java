package ch.epfl.cs107.play.game.tutos.actor;

import ch.epfl.cs107.play.game.actor.TextGraphics;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.MovableAreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.tutos.Tuto2Behavior;
import ch.epfl.cs107.play.game.tutos.area.Tuto2Area;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;

import java.awt.*;
import java.util.Collections;
import java.util.List;

public class GhostPlayer extends MovableAreaEntity {
    private static final int DEFAULT_SPEED = 1;
    private final static int ANIMATION_DURATION = 8;
    private final static float DEFAULT_HEALTH_POINTS = 100f;

    private Keyboard keyboard;
    private Sprite sprite;
    private TextGraphics hpText;
    private float hp = DEFAULT_HEALTH_POINTS;
    private boolean isNextToDoor = false;

    /**
     * Default MovableAreaEntity constructor
     *
     * @param area        (Area): Owner area. Not null
     * @param orientation (Orientation): Initial orientation of the entity. Not null
     * @param position    (Coordinate): Initial position of the entity. Not null
     */
    public GhostPlayer(Area area, Orientation orientation, DiscreteCoordinates position, String spriteName) {
        super(area, orientation, position);

        this.sprite = new Sprite(spriteName, 1f, 1f, this);
        this.hpText = initHpText(Color.WHITE);
        this.enterArea(area, position);
    }

    private void initKeyboard(Area area) {
        this.keyboard = area.getKeyboard();
    }

    private TextGraphics initHpText(Color color) {
        TextGraphics hpt = new TextGraphics(Integer.toString((int) this.hp), 0.4f, color);
        hpt.setParent(this);
        hpt.setAnchor(new Vector(-0.3f, 0.1f));

        return hpt;
    }

    private void decreaseHp(float damage) {
        this.hp = this.hp - damage <= 0 ? 0 : this.hp - damage;
        this.hpText.setText(Integer.toString((int) this.hp));
    }

    public void enterArea(Area area, DiscreteCoordinates position) {
        initKeyboard(area);
        area.registerActor(this);
        this.setOwnerArea(area);
        this.setCurrentPosition(position.toVector());
        area.setViewCandidate(this);
        resetMotion();
    }

    public void leaveArea(Area area) {
        this.isNextToDoor = false;
        area.unregisterActor(this);
    }

    public void update(float deltaTime) {
        this.decreaseHp(deltaTime);

        if (this.keyboard.get(Keyboard.UP).isDown()) this.move(Orientation.UP);
        if (this.keyboard.get(Keyboard.DOWN).isDown()) this.move(Orientation.DOWN);
        if (this.keyboard.get(Keyboard.LEFT).isDown()) this.move(Orientation.LEFT);
        if (this.keyboard.get(Keyboard.RIGHT).isDown()) this.move(Orientation.RIGHT);

        for (DiscreteCoordinates dc : getCurrentCells())
            if (((Tuto2Area) getOwnerArea()).getCell(dc).getType().equals(Tuto2Behavior.Tuto2CellType.DOOR))
                this.isNextToDoor = true;

        super.update(deltaTime);
    }

    @Override
    public void draw(Canvas canvas) {
        this.sprite.draw(canvas);
        this.hpText.draw(canvas);
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    @Override
    public boolean takeCellSpace() {
        return true;
    }

    @Override
    public boolean isCellInteractable() {
        return true;
    }

    @Override
    public boolean isViewInteractable() {
        return true;
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v) {

    }

    public boolean isWeak() {
        return this.hp <= 0;
    }

    public void strengthen() {
        this.hp = DEFAULT_HEALTH_POINTS;
    }

    public boolean getIsNextToDoor() {
        return this.isNextToDoor;
    }

    private void move(Orientation orientation) {
        if (this.getOrientation().equals(orientation))
            this.move(ANIMATION_DURATION, DEFAULT_SPEED);
        else
            this.orientate(orientation);
    }
}
