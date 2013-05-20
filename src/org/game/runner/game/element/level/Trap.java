/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.game.runner.game.element.level;

import org.andengine.entity.IEntity;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.shape.Shape;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.color.Color;
import org.game.runner.game.player.Player;
import org.andengine.util.debug.Debug;

/**
 *
 * @author Karl
 */
public class Trap extends LevelElement{
    public Trap(int level){
        super(level);
    }
    
    @Override
    public Color getColor() {
        return Color.WHITE;
    }

    @Override
    protected Shape buildShape(float pX, float pY, VertexBufferObjectManager pVertexBufferObjectManager, final Player player) {
        return new Rectangle(pX, pY, TRAP_WIDTH, TRAP_HEIGHT, pVertexBufferObjectManager);
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
