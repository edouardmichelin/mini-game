package ch.epfl.cs107.play.game.arpg.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.config.Settings;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Collections;
import java.util.List;

public class Grass extends AreaEntity {
    private final static int ANIMATION_DURATION = Settings.FRAME_RATE / 2;

    private RPGSprite sprite;
    private Animation[] animation;
    private boolean isSliced;
    private int despawnTime;

    /**
     * Default AreaEntity constructor
     *
     * @param area        (Area): Owner area. Not null
     * @param orientation (Orientation): Initial orientation of the entity in the Area. Not null
     * @param position    (DiscreteCoordinate): Initial position of the entity in the Area. Not null
     */
    public Grass(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);

        this.sprite = new RPGSprite(
                "zelda/Grass",
                1,
                1,
                this,
                new RegionOfInterest(0, 0, 16, 16)
        );

        Sprite sprites[] = new Sprite[4];
        for (int frame = 0; frame < 4; frame++) {
            sprites[frame] = new RPGSprite(
                    "zelda/grass.sliced",
                    1,
                    1,
                    this,
                    new RegionOfInterest(frame * 32, 0, 32, 32)
            );
        }
        this.animation = new Animation[]{new Animation(
                ANIMATION_DURATION / 3,
                sprites,
                true)
        };

        this.isSliced = false;
        this.despawnTime = ANIMATION_DURATION;
    }

    public void cut() {
        this.isSliced = true;
    }

    public boolean getIsSliced() {
        return this.isSliced;
    }

    @Override
    public void update(float deltaTime) {
        if(despawnTime <= 0) {
            this.getOwnerArea().unregisterActor(this);
        }
        if (this.getIsSliced()) {
            this.despawnTime = despawnTime - 1;
            this.animation[0].update(deltaTime);
        }
        super.update(deltaTime);
    }

    @Override
    public void draw(Canvas canvas) {
        if (!isSliced) {
            this.sprite.draw(canvas);
        } else {
            this.animation[0].draw(canvas);
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
        return true;
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
