/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.game.runner.game.element.level;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.shape.Shape;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.color.Color;
import org.game.runner.game.player.Player;

/**
 *
 * @author Karl
 */
public class Platform extends LevelElement {
    private int level = 1;
    
    /**
     * @param level Range from 1 to 3. 
     */
    public Platform(int level){
        if(level > 3){
            this.level = 3;
        }
        else if(level < 1){
            this.level = 1;
        }
        else{
            this.level = level;
        }
    }
    
    @Override
    public Color getColor() {
        return Color.WHITE;
    }

    @Override
    protected Shape buildShape(float pX, float pY, VertexBufferObjectManager pVertexBufferObjectManager, Player player) {
        return new Rectangle(pX, pY + this.level*PLATFORM_LEVEL_HIGH, PLATFORM_WIDTH, PLATFORM_THICKNESS, pVertexBufferObjectManager);
    }

    @Override
    protected void playerAction(Player player) {
        /* Nothing */
    }
}
