package ch.epfl.cs107.play.game.arpg.actor;

import ch.epfl.cs107.play.game.actor.ImageGraphics;
import ch.epfl.cs107.play.game.areagame.io.ResourcePath;
import ch.epfl.cs107.play.game.arpg.config.SpriteNames;
import ch.epfl.cs107.play.game.arpg.gui.ARPGStatusGUI;
import ch.epfl.cs107.play.game.rpg.equipment.Inventory;
import ch.epfl.cs107.play.game.rpg.equipment.InventoryContentAccessor;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

public class ARPGPlayerStatusGUI implements ARPGStatusGUI {
    private final static int DEPTH = 600;

    private boolean displayFortune = false;
    private float healthPoint;
    private Inventory.InventoryItem currentItem;
    private InventoryContentAccessor inventory;

    public ARPGPlayerStatusGUI(InventoryContentAccessor inventory, int currentItemId, float healthPoint) {
        this.healthPoint = healthPoint;
        this.inventory = inventory;
        this.currentItem = inventory != null && currentItemId != -1 ?
                inventory.getItem(currentItemId) :
                null;
    }

    @Override
    public void draw(Canvas canvas) {
        float width = canvas.getScaledWidth();
        float height = canvas.getScaledHeight();

        this.drawCoinsDisplay(canvas, width, height);
        this.drawCoinsDigits(canvas, width, height);
        this.drawHearts(canvas, width, height);
        this.drawGearDisplay(canvas, width, height);
        this.drawCurrentEquipment(canvas, width, height);
    }

    @Override
    public void setCurrentItem(int itemId) {
        this.currentItem = this.inventory.getItem(itemId);
    }

    @Override
    public void setHealthPoints(float hp) {
        this.healthPoint = hp;
    }

    public void switchCoinsDisplay() {
        this.displayFortune = !this.displayFortune;
    }

    private void drawGearDisplay(Canvas canvas, float width, float height) {
        Vector anchor = canvas.getTransform().getOrigin().sub(new Vector(width / 2, height / 2));
        ImageGraphics gearDisplay = new ImageGraphics(
                ResourcePath.getSprite(SpriteNames.GEAR_DISPLAY),
                3f,
                3f,
                new RegionOfInterest(0, 0, 32, 32),
                anchor.add(new Vector(0f, height - 3f)),
                1,
                DEPTH);

        gearDisplay.draw(canvas);
    }

    private void drawCurrentEquipment(Canvas canvas, float width, float height) {
        Vector anchor = canvas.getTransform().getOrigin().sub(new Vector(width / 2, height / 2));
        ImageGraphics currentEquipment = new ImageGraphics(
                ResourcePath.getSprite(this.currentItem.getSpriteName()),
                1f,
                1f,
                this.currentItem.getTextureRoi(),
                anchor.add(new Vector(1.075f, height - 2f)),
                1,
                DEPTH * 2);

        currentEquipment.draw(canvas);
    }

    private void drawCoinsDisplay(Canvas canvas, float width, float height) {
        Vector anchor = canvas.getTransform().getOrigin().sub(new Vector(width / 2, height / 2));
        ImageGraphics coinsDisplay = new ImageGraphics(
                ResourcePath.getSprite(SpriteNames.COINS_DISPLAY),
                5f,
                2.5f,
                new RegionOfInterest(0, 0, 64, 64),
                anchor.add(new Vector(0.2f, 0f)),
                1,
                DEPTH);

        coinsDisplay.draw(canvas);
    }

    private void drawCoinsDigits(Canvas canvas, float width, float height) {
        final float OFFSET = 0.75f;
        final float SIZE = 1f;
        final float X = 2.25f, Y = 0.8f;

        String[] data = Integer.toString(
                this.displayFortune ?
                        this.inventory.getFortune() :
                        this.inventory.getWealth()
        ).split("");

        Vector anchor = canvas.getTransform().getOrigin().sub(new Vector(width / 2, height / 2));

        for (int i = 2; i >= 0; i--) {
            int digit = i < data.length ? Integer.parseInt(data[data.length - (i + 1)]) : 0;
            new ImageGraphics(
                    ResourcePath.getSprite(SpriteNames.DIGITS),
                    SIZE,
                    SIZE,
                    DigitsRegionOfInterest.fromInt(digit).regionOfInterest,
                    anchor.add(new Vector(X + (2 - i) * OFFSET, Y)),
                    1,
                    DEPTH * 2
            ).draw(canvas);
        }
    }

    private void drawHearts(Canvas canvas, float width, float height) {
        final float OFFSET = 1.5f;
        final float SIZE = 1.5f;
        final float X = 3f, Y = height - 2.25f;

        Vector anchor = canvas.getTransform().getOrigin().sub(new Vector(width / 2, height / 2));

        // 5 -> MAXIMUM PLAYER'S HP
        // TODO - PLAYER CAN FIND HEARTS THAT INCREASE HIS MAX HP
        // TODO - 2 HP: HEARTS START SHAKING / 1 HP: HEARTS SHAKE STRONGER
        for (int i = 0; i < 5; i++) {
            float data = (this.healthPoint - i) > 0f ? this.healthPoint - i > 0.5f ? 1f : 0.5f : 0f;

            new ImageGraphics(
                    ResourcePath.getSprite(SpriteNames.HEART_DISPLAY),
                    SIZE,
                    SIZE,
                    HeartsRegionOfInterest.fromFloat(data).regionOfInterest,
                    anchor.add(new Vector(X + i * OFFSET, Y)),
                    1,
                    DEPTH * 2
            ).draw(canvas);
        }
    }

    private enum DigitsRegionOfInterest {
        ONE(1, 0, 0),
        TWO(2, 16, 0),
        THREE(3, 32, 0),
        FOUR(4, 48, 0),
        FIVE(5, 0, 16),
        SIX(6, 16, 16),
        SEVEN(7, 32, 16),
        EIGHT(8, 48, 16),
        NINE(9, 0, 32),
        NIL(0,16, 32)
        ;

        private final float value;
        public final RegionOfInterest regionOfInterest;

        DigitsRegionOfInterest(int value, int x, int y) {
            this.value = value;
            this.regionOfInterest = new RegionOfInterest(x, y, 16, 16);
        }

        public static DigitsRegionOfInterest fromInt(int value) {
            for (DigitsRegionOfInterest droi : DigitsRegionOfInterest.values()) {
                if (droi.value == value) return droi;
            }

            return NIL;
        }
    }

    public enum HeartsRegionOfInterest {
        EMPTY(0f, 0, 0),
        HALF(0.5f, 16, 0),
        FULL(1f, 32, 0)
        ;

        private final float value;
        public final RegionOfInterest regionOfInterest;

        HeartsRegionOfInterest(float value, int x, int y) {
            this.value = value;
            this.regionOfInterest = new RegionOfInterest(x, y, 16, 16);
        }

        public static HeartsRegionOfInterest fromFloat(float value) {
            for (HeartsRegionOfInterest hroi : HeartsRegionOfInterest.values()) {
                if (hroi.value == value) return hroi;
            }

            return EMPTY;
        }
    }
}
