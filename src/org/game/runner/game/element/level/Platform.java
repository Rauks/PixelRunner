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
    
    public Platform(int level){
        super(level < 1 ? 1 : level);
    }
    
    @Override
    public Color getColor() {
        return Color.WHITE;
    }

    @Override
    protected Shape buildShape(float pX, float pY, VertexBufferObjectManager pVertexBufferObjectManager, Player player) {
        return new Rectangle(pX, pY, PLATFORM_WIDTH, PLATFORM_THICKNESS, pVertexBufferObjectManager);
    }

    @Override
    protected void playerAction(Player player) {
        /* Nothing */
    }
    
    @Override
    public boolean isPlatform(){
        return true;
    }
}
