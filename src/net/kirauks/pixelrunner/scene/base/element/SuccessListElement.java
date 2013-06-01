/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.kirauks.pixelrunner.scene.base.element;

import net.kirauks.pixelrunner.manager.ResourcesManager;
import org.andengine.entity.shape.Shape;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.color.Color;

/**
 *
 * @author Karl
 */
public class SuccessListElement extends ListElement{
    private Text sub;
    private Sprite picto;
    
    public SuccessListElement(String name, String subName, boolean locked, VertexBufferObjectManager pVertexBufferObjectManager){
        super(name, pVertexBufferObjectManager);
        this.sub = new Text(0, 0, ResourcesManager.getInstance().fontPixel_34, subName, pVertexBufferObjectManager);
        this.sub.setPosition(this.sub.getWidth()/2 + 20, - this.sub.getHeight()/2);
        this.sub.setCullingEnabled(true);
        this.sub.setColor(new Color(0.4f, 0.4f, 0.4f));
        this.attachChild(this.sub);
        
        if(locked){
            this.picto = new Sprite(-50, this.getHeight() - 36, ResourcesManager.getInstance().lvlLock, pVertexBufferObjectManager);
            this.picto.setColor(new Color(0.4f, 0.4f, 0.4f));
            this.setColor(new Color(0.4f, 0.4f, 0.4f));
        }
        else{
            this.picto = new Sprite(-50, this.getHeight() - 37, ResourcesManager.getInstance().lvlStar, pVertexBufferObjectManager);
        }
        this.attachChild(this.picto);
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
