/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.game.runner.game.element.level;

import org.andengine.entity.IEntity;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.color.Color;
import org.game.runner.game.player.Player;

/**
 *
 * @author Karl
 */
public class BonusSpeed extends LevelElement{
    @Override
    public Color getColor() {
        return Color.RED;
    }

    @Override
    public IEntity createEntity(float pX, float pY, float pWidth, float pHeight, VertexBufferObjectManager pVertexBufferObjectManager, final Player player) {
        IEntity entity = new Rectangle(pX, pY, pWidth, pHeight, pVertexBufferObjectManager){
            @Override
            protected void onManagedUpdate(float pSecondsElapsed){
                super.onManagedUpdate(pSecondsElapsed);
                if(player.collidesWith(this)){
                    player.hit(this);
                    player.resetBonus();
                    player.setColor(this.getColor());
                    player.setSpeed(1.4f);
                }
            }
        };
        entity.setColor(this.getColor());
        return entity;
    }
}
