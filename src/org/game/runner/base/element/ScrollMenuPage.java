/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.game.runner.base.element;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 *
 * @author Karl
 */
public class ScrollMenuPage extends Rectangle{
    public ScrollMenuPage(final float pWidth, final float pHeight, final VertexBufferObjectManager pVertexBufferObjectManager){
        super(0, 0, pWidth, pHeight, pVertexBufferObjectManager);
    }
    
    public void disposePage(){
        
    }
    
}
