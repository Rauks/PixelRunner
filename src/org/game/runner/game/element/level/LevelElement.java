/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.game.runner.game.element.level;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.IEntity;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.color.Color;
import org.game.runner.game.player.Player;

/**
 *
 * @author Karl
 */
public abstract class LevelElement {
    public static final int BONUS_HEIGHT = 30;
    public static final int BONUS_WIDTH = 30;
    public static final int TRAP_HEIGHT = 30;
    public static final int TRAP_WIDTH = 30;
    public static final int PLATFORM_THICKNESS = 10;
    public static final int PLATFORM_WIDTH = 50;
    
    
    public abstract Color getColor();
    protected abstract IEntity createEntity(float pX, float pY, VertexBufferObjectManager pVertexBufferObjectManager, final Player player);
    protected abstract void palyerAction(Player player, IEntity entity);
    
    public IEntity getEntity(float pX, float pY, VertexBufferObjectManager pVertexBufferObjectManager, final Player player){
        final IEntity entity = this.createEntity(pX, pY, pVertexBufferObjectManager, player);
        entity.setColor(this.getColor());
        entity.registerUpdateHandler(new IUpdateHandler() {
            @Override
            public void reset() { }

            @Override
            public void onUpdate(final float pSecondsElapsed) {
                if(entity.collidesWith(player)) {
                    LevelElement.this.palyerAction(player, entity);
                }
            }
        });
        return entity;
    }
}
