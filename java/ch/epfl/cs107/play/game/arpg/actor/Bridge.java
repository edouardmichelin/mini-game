package ch.epfl.cs107.play.game.arpg.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.*;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.config.Settings;
import ch.epfl.cs107.play.game.arpg.config.SpriteNames;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.items.CoinItem;
import ch.epfl.cs107.play.game.arpg.items.HeartItem;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.game.rpg.misc.DamageType;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RandomGenerator;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Bridge extends AreaEntity {
    public static final int ANIMATION_DURATION = Settings.FRAME_RATE / 6;
    private RPGSprite sprite;
    private Logic signal;

    public Bridge(Area area, Orientation orientation, DiscreteCoordinates position, Logic signal) {
        super(area, orientation, position);

        this.signal = signal;

        this.sprite = new RPGSprite(
                SpriteNames.BRIDGE,
                4f,
                2f,
                this,
                new RegionOfInterest(0, 0, 64, 48),
                new Vector(-1f, -0.5f)
        );

    }

    public boolean isActivated() {
        return this.signal.isOn();
    }

    public void activate() {
        this.signal = Logic.TRUE;
    }

    @Override
    public void draw(Canvas canvas) {
        if (this.isActivated()) this.sprite.draw(canvas);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return List.of(this.getCurrentMainCellCoordinates(), this.getCurrentMainCellCoordinates().right());
    }

    @Override
    public boolean takeCellSpace() {
        return !this.isActivated();
    }

    @Override
    public boolean isCellInteractable() {
        return false;
    }

    @Override
    public boolean isViewInteractable() {
        return false;
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v) {

    }
}