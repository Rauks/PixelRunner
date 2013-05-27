/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.game.runner.game.element.level;

import org.andengine.entity.modifier.ColorModifier;
import org.andengine.entity.shape.Shape;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.color.Color;
import org.game.runner.game.player.Player;
import org.game.runner.game.player.Trail;
import org.game.runner.manager.ResourcesManager;

/**
 *
 * @author Karl
 */
public class Wall extends LevelElement{
    public Wall(int level){
        super(level, ResourcesManager.getInstance().wall.getWidth(), ResourcesManager.getInstance().wall.getHeight());
    }
    
    private Trail trail;

    @Override
    protected Shape buildShape(float pX, float pY, VertexBufferObjectManager pVertexBufferObjectManager, final Player player) {
        Sprite shape = new Sprite(pX, pY, ResourcesManager.getInstance().wall, pVertexBufferObjectManager);
        this.trail = new Trail(8, 0, 0, 120, TRAIL_MIN_SPEED_X, TRAIL_MAX_SPEED_X, TRAIL_MIN_SPEED_Y, TRAIL_MAX_SPEED_Y, TRAIL_MIN_RATE, TRAIL_MAX_RATE, TRAIL_MAX_PARTICULES, Trail.ColorMode.NORMAL, ResourcesManager.getInstance().trail, pVertexBufferObjectManager);
        this.trail.setColor(COLOR_TRAIL_DEFAULT);
        shape.attachChild(this.trail);
        this.trail.setZIndex(shape.getZIndex() - 1);
        shape.sortChildren();
        return shape;
    }

    @Override
    protected void playerAction(Player player) {
        if(!player.isRolling()){
            if(player.hasLife()){
                player.resetBonus();
            }
            else{
                player.rollBackJump();
            }
        }
        else{
            this.getBuildedShape().registerEntityModifier(new ColorModifier(.2f, this.getColor(), new Color(0.2f, 0.2f, 0.2f)));
        }
    }
}
