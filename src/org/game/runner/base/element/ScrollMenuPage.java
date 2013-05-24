/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.game.runner.base.element;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.text.Text;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.game.runner.manager.ResourcesManager;

/**
 *
 * @author Karl
 */
public class ScrollMenuPage extends Rectangle{
    private Text title;
    
    public ScrollMenuPage(final float pWidth, final float pHeight, final VertexBufferObjectManager pVertexBufferObjectManager){
        super(0, 0, pWidth, pHeight, pVertexBufferObjectManager);
        this.setAlpha(0f);
    }
    
    public void setTitle(String title){
        if(this.title == null){
            this.title = new Text(this.getWidth()/2, this.getHeight() - 30, ResourcesManager.getInstance().fontPixel_100, title, this.getVertexBufferObjectManager());
        }
        else{
            this.title.setText(title);
        }
    }
    
    public void disposePage(){
        if(this.title != null){
            this.title.detachSelf();
            this.title.dispose();
        }
    }
}
