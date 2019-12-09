package ch.epfl.cs107.play.game.arpg.actor;

import ch.epfl.cs107.play.game.actor.ImageGraphics;
import ch.epfl.cs107.play.game.areagame.io.ResourcePath;
import ch.epfl.cs107.play.game.arpg.gui.ARPGStatusGUI;
import ch.epfl.cs107.play.game.rpg.equipment.InventoryContentAccessor;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

public class ARPGPlayerStatusGUI implements ARPGStatusGUI {
    private final static int DEPTH = 100000;

    private float healthPoint;
    private String itemSpriteName;
    private InventoryContentAccessor inventory;

    public ARPGPlayerStatusGUI() {
        this(null, -1, 0);
    }

    public ARPGPlayerStatusGUI(InventoryContentAccessor inventory, int currentItemId, float healthPoint) {
        this.healthPoint = healthPoint;
        this.inventory = inventory;
        this.itemSpriteName = inventory != null && currentItemId != -1 ?
                ResourcePath.getSprite(inventory.getItem(currentItemId).getSpriteName()) :
                null;
    }

    @Override
    public void draw(Canvas canvas) {
        float width = canvas.getScaledWidth();
        float height = canvas.getScaledHeight();

        this.drawCoinsDisplay(canvas, width, height);
        this.drawCoinsDigits(canvas, width, height);
        this.drawHeartDisplay(canvas, width, height);
        this.drawGearDisplay(canvas, width, height);
        this.drawCurrentEquipment(canvas, width, height);
    }

    @Override
    public void setCurrentItem(int itemId) {
        this.itemSpriteName = ResourcePath.getSprite(inventory.getItem(itemId).getSpriteName());
    }

    @Override
    public void setHealthPoints(float hp) {

    }

    private void drawGearDisplay(Canvas canvas, float width, float height) {
        Vector anchor = canvas.getTransform().getOrigin().sub(new Vector(width / 2, height / 2));
        ImageGraphics gearDisplay = new ImageGraphics(
                ResourcePath.getSprite("zelda/gearDisplay"),
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
                this.itemSpriteName,
                1f,
                1f,
                new RegionOfInterest(0, 0, 32, 32),
                anchor.add(new Vector(1.075f, height - 2f)),
                1,
                DEPTH * 2);

        currentEquipment.draw(canvas);
    }

    private void drawCoinsDisplay(Canvas canvas, float width, float height) {
        Vector anchor = canvas.getTransform().getOrigin().sub(new Vector(width / 2, height / 2));
        ImageGraphics coinsDisplay = new ImageGraphics(
                ResourcePath.getSprite("zelda/coinsDisplay"),
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
        String[] money = ("" + this.inventory.getWealth()).split("");
        Vector anchor = canvas.getTransform().getOrigin().sub(new Vector(width / 2, height / 2));

        for (int i = 2; i >= 0; i--) {
            int digit = i < money.length ? Integer.parseInt(money[money.length - (i + 1)]) : 0;
            new ImageGraphics(
                    ResourcePath.getSprite("zelda/digits"),
                    SIZE,
                    SIZE,
                    DigitsRegionOfInterest.fromInt(digit).getROI(),
                    anchor.add(new Vector(X + (2 - i) * OFFSET, Y)),
                    1,
                    DEPTH * 2
            ).draw(canvas);
        }
    }

    private void drawHeartDisplay(Canvas canvas, float width, float height) {
        //
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

        private int value;
        private RegionOfInterest roi;

        DigitsRegionOfInterest(int value, int x, int y) {
            this.value = value;
            this.roi = new RegionOfInterest(x, y, 16, 16);
        }

        public RegionOfInterest getROI() {
            return this.roi;
        }

        public static DigitsRegionOfInterest fromInt(int value) {
            for (DigitsRegionOfInterest droi : DigitsRegionOfInterest.values()) {
                if (droi.value == value) return droi;
            }

            return NIL;
        }
    }
}
