/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.game.runner.game.element.level;

import org.andengine.entity.IEntity;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.shape.Shape;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.color.Color;
import org.game.runner.game.player.Player;
import org.andengine.util.debug.Debug;
import org.game.runner.game.player.Trail;
import org.game.runner.manager.ResourcesManager;

/**
 *
 * @author Karl
 */
public class Rocket extends LevelElement{
    public Rocket(int level){
        super(level, ResourcesManager.getInstance().rocket.getWidth(), ResourcesManager.getInstance().rocket.getHeight() + 20);
    }
    
    private Trail trail;

    @Override
    protected Shape buildShape(float pX, float pY, VertexBufferObjectManager pVertexBufferObjectManager, final Player player) {
        AnimatedSprite shape = new AnimatedSprite(pX, pY, ResourcesManager.getInstance().rocket, pVertexBufferObjectManager){
            @Override
            public boolean detachSelf(){
                Rocket.this.trail.detachSelf();
                return super.detachSelf();
            }
            @Override
            public void dispose(){
                Rocket.this.trail.dispose();
                super.dispose();
            }
        };
        this.trail = new Trail(72, 8, 0, 20, 150, 120, -2, 2, 15, 20, 15, Trail.ColorMode.NORMAL, shape, ResourcesManager.getInstance().trail, pVertexBufferObjectManager);
        this.trail.setColor(Color.RED);
        shape.animate(150);
        return shape;
    }

    @Override
    protected void playerAction(Player player) {
        player.setColor(this.getColor());
        if(player.hasLife()){
            player.resetBonus();
        }
        else{
            player.rollBackJump();
        }
    }
}
