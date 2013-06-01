/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.kirauks.pixelrunner.scene.base.element;

import net.kirauks.pixelrunner.manager.ResourcesManager;
import org.andengine.entity.text.Text;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 *
 * @author Karl
 */
public class SuccessListElement extends ListElement{
    private Text sub;
    
    public SuccessListElement(String name, String subName, VertexBufferObjectManager pVertexBufferObjectManager){
        super(name, pVertexBufferObjectManager);
        this.sub = new Text(0, 0, ResourcesManager.getInstance().fontPixel_34, subName, pVertexBufferObjectManager);
        this.sub.setPosition(this.sub.getWidth()/2 + 20, - this.sub.getHeight()/2);
        this.sub.setCullingEnabled(true);
        this.attachChild(this.sub);
    }
    
    @Override
    public float getWrappingHeight(){
        return super.getHeight() + this.sub.getHeight();
        
    }
    
    @Override
    public void disposeElement(){
        super.disposeElement();
        this.sub.detachSelf();
        this.sub.dispose();
    }
}
