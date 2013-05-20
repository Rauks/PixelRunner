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
public class Trap extends LevelElement{
    @Override
    public Color getColor() {
        return Color.WHITE;
    }

    @Override
    public IEntity createEntity(float pX, float pY, VertexBufferObjectManager pVertexBufferObjectManager, final Player player) {
        return new Rectangle(pX, pY, TRAP_WIDTH, TRAP_HEIGHT, pVertexBufferObjectManager);
    }

    @Override
    protected void palyerAction(Player player, IEntity entity) {
        player.hit(entity);
        player.setColor(entity.getColor());
        if(player.hasLife()){
            player.resetBonus();
        }
        else{
            player.rollBackJump();
        }
    }
}
