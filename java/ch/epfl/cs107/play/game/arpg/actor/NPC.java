package ch.epfl.cs107.play.game.arpg.actor;

import ch.epfl.cs107.play.game.actor.ImageGraphics;
import ch.epfl.cs107.play.game.actor.TextGraphics;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.MovableAreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.areagame.io.ResourcePath;
import ch.epfl.cs107.play.game.arpg.config.Settings;
import ch.epfl.cs107.play.game.arpg.config.SpriteNames;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.game.rpg.misc.Helpers;
import ch.epfl.cs107.play.game.rpg.misc.NPCProperties;
import ch.epfl.cs107.play.math.*;
import ch.epfl.cs107.play.window.Canvas;

import java.awt.*;
import java.util.Collections;
import java.util.List;

public class NPC extends MovableAreaEntity {
    private static final String[] SPRITES = {SpriteNames.NPC_1, SpriteNames.NPC_2};
    private static final int ANIMATION_DURATION = Settings.FRAME_RATE / 4;
    private static final int MIN_SPEED = 4;
    private static final int MAX_SPEED = 6;

    private NPCProperties properties;
    private Animation[] animations;
    private int speed;
    private boolean isTalking;
    private ImageGraphics dialogBox;

    /**
     * Default MovableAreaEntity constructor
     *
     * @param area        (Area): Owner area. Not null
     * @param orientation (Orientation): Initial orientation of the entity. Not null
     * @param position    (Coordinate): Initial position of the entity. Not null
     */
    public NPC(Area area, Orientation orientation, DiscreteCoordinates position, NPCProperties properties) {
        super(area, orientation, position);

        this.properties = properties;
        this.speed = Settings.FRAME_RATE / Helpers.random(MIN_SPEED, MAX_SPEED);

        this.animations = RPGSprite.createAnimations(ANIMATION_DURATION, RPGSprite.extractSprites(
                SPRITES[RandomGenerator.getInstance().nextInt(SPRITES.length)],
                4,
                1,
                2,
                this,
                16,
                32,
                new Orientation[]{Orientation.UP, Orientation.RIGHT, Orientation.DOWN, Orientation.LEFT}
        ));
    }

    public void talk(AreaEntity source) {
        this.orientate(source.getOrientation().opposite());
        this.isTalking = true;
    }

    public void stopTalking() {
        this.isTalking = false;
    }

    private Animation getAnimation() {
        return this.animations[this.getOrientation().ordinal()];
    }

    private void drawDialogBox(Canvas canvas) {
        final int DEPTH = 99999999;
        float width = canvas.getScaledWidth();
        float height = canvas.getScaledHeight();

        Vector anchor = canvas.getTransform().getOrigin().sub(new Vector(width / 2 - ((width  / 20)), height / 2 - height / 20));

        this.dialogBox = new ImageGraphics(
                ResourcePath.getSprite(SpriteNames.DIALOG),
                width * 9 / 10,
                height / 4,
                new RegionOfInterest(0, 0, 240, 80),
                anchor,
                1,
                DEPTH
        );

        TextGraphics hpt = new TextGraphics(this.properties.message, 0.3f, Color.RED);
        // hpt.setParent(this);
        hpt.setDepth(DEPTH + 1);
        hpt.setAnchor(anchor);

        // canvas.drawText(this.properties.message, 1, Transform.I.rotated(0), Color.BLACK, Color.white, 1, false, false, anchor, TextAlign.Horizontal.LEFT, TextAlign.Vertical.BOTTOM, 1, DEPTH + 1);

        this.dialogBox.draw(canvas);
        hpt.draw(canvas);
    }

    private void drawDialog(Canvas canvas) {
    }

    @Override
    public void update(float deltaTime) {
        if (this.properties.canMove)
            this.move(this.speed);

        super.update(deltaTime);
    }

    @Override
    public void draw(Canvas canvas) {
        this.getAnimation().draw(canvas);

        if (this.isTalking) {
            drawDialogBox(canvas);
        } else {
        }
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
        return false;
    }

    @Override
    public boolean isViewInteractable() {
        return true;
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v) {
        ((ARPGInteractionVisitor) v).interactWith(this);
    }
}
