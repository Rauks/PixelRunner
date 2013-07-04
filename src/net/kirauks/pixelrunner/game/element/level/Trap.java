/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.kirauks.pixelrunner.game.element.level;

import org.andengine.entity.shape.Shape;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import net.kirauks.pixelrunner.game.Player;
import net.kirauks.pixelrunner.game.Trail;
import net.kirauks.pixelrunner.manager.ResourcesManager;
import org.andengine.util.adt.color.Color;

/**
 *
 * @author Karl
 */
public class Trap extends LevelElement{
    public Trap(int level){
        super(level, ResourcesManager.getInstance().trap.getWidth(), ResourcesManager.getInstance().trap.getHeight());
    }
    
    private Trail trail;

    @Override
    protected Shape buildShape(float pX, float pY, VertexBufferObjectManager pVertexBufferObjectManager, final Player player) {
        AnimatedSprite shape = new AnimatedSprite(pX, pY, ResourcesManager.getInstance().trap, pVertexBufferObjectManager){
            @Override
            public boolean detachSelf(){
                Trap.this.trail.detachSelf();
                return super.detachSelf();
            }
            @Override
            public void dispose(){
                Trap.this.trail.dispose();
                super.dispose();
            }
        };
        this.trail = new Trail(16, 0, 0, 24, TRAIL_MIN_SPEED_X, TRAIL_MAX_SPEED_X, TRAIL_MIN_SPEED_Y, TRAIL_MAX_SPEED_Y, TRAIL_MIN_RATE, TRAIL_MAX_RATE, TRAIL_MAX_PARTICULES, Trail.ColorMode.NORMAL, ResourcesManager.getInstance().trail, pVertexBufferObjectManager);
        this.trail.setColor(COLOR_TRAIL_DEFAULT);
        shape.attachChild(this.trail);
        this.trail.setZIndex(shape.getZIndex() - 1);
        shape.sortChildren();
        shape.animate(new long[]{200, 150, 200, 150}, new int[]{0, 1, 2, 1});
        return shape;
    }

    @Override
    protected void playerAction(Player player) {
        if(player.hasLife()){
            this.trail.setColor(COLOR_DISABLED_DEFAULT);
            this.changeColor(COLOR_DISABLED_DEFAULT);
            player.resetBonus();
        }
        else{
            player.rollBackJump();
            //this.changeColor(Color.RED);
        }
    }
}
