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
import org.game.runner.scene.GameLevelScene;

/**
 *
 * @author Karl
 */
public class BonusJump extends LevelElement{
    @Override
    public Color getColor() {
        return Color.BLUE;
    }

    @Override
    public IEntity createEntity(float pX, float pY, float pWidth, float pHeight, VertexBufferObjectManager pVertexBufferObjectManager, final LevelElementCollisionCallback action) {
        IEntity entity = new Rectangle(pX, pY, pWidth, pHeight, pVertexBufferObjectManager){
            @Override
            protected void onManagedUpdate(float pSecondsElapsed){
                super.onManagedUpdate(pSecondsElapsed);
                if(action.playerCollideWith(this)){
                    action.getPlayer().setColor(this.getColor());
                    //Not implemented yet
                    action.onPlayerCollided();
                }
            }
        };
        entity.setColor(this.getColor());
        return entity;
    }
}
