/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.game.runner.scene.base.element;
import org.andengine.entity.shape.Shape;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.color.Color;
import org.game.runner.manager.ResourcesManager;

/**
 *
 * @author Karl
 */
public class PageElement extends Sprite{
    private int id;
    private boolean locked;
    private IPageElementTouchListener listener;
    
    private Shape picto;
    private Sprite lock;
    
    public PageElement(float pX, float pY, int id, boolean locked, ITextureRegion texture, VertexBufferObjectManager pVertexBufferObjectManager){
        super(pX, pY, ResourcesManager.getInstance().lvlBack, pVertexBufferObjectManager);
        this.setCullingEnabled(true);
        this.id = id;
        this.locked = locked;
        this.initLock();
        this.picto = new Sprite(this.getHeight()/2, this.getWidth()/2, texture, this.getVertexBufferObjectManager());
        this.attachChild(this.picto);
        this.refreshEntity();
    }
    public PageElement(float pX, float pY, int id, boolean locked, VertexBufferObjectManager pVertexBufferObjectManager){
        super(pX, pY, ResourcesManager.getInstance().lvlBack, pVertexBufferObjectManager);
        this.setCullingEnabled(true);
        this.id = id;
        this.locked = locked;
        this.initLock();
        this.picto = new Text(this.getHeight()/2, this.getWidth()/2, ResourcesManager.getInstance().fontPixel_60, String.valueOf(id), this.getVertexBufferObjectManager());
        this.attachChild(this.picto);
        this.refreshEntity();
    }
    private void initLock(){
        this.lock = new Sprite(this.getHeight()/2, this.getWidth()/2, ResourcesManager.getInstance().lvlLock, this.getVertexBufferObjectManager());
        this.lock.setColor(new Color(0.4f, 0.4f, 0.4f));
        this.attachChild(this.lock);
    }
    
    public int getId(){
        return id;
    }
    
    @Override
    public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X, float Y){
        if (pSceneTouchEvent.isActionUp()){
            if(PageElement.this.listener != null){
                PageElement.this.listener.onElementActionUp(this);
            }
        }
        return false;
    };
    
    public void registerPageElementTouchListener(IPageElementTouchListener listener){
        this.listener = listener;
    }
    public void unregisterPageElementTouchListener(){
        this.listener = null;
    }
    
    public void setLocked(boolean locked){
        this.locked = locked;
        this.refreshEntity();
    }
    
    private void refreshEntity(){
        if(this.locked){
            this.lock.setVisible(true);
            this.picto.setVisible(false);
        }
        else{
            this.lock.setVisible(false);
            this.picto.setVisible(true);
        }
    }
    
    public void disposeElement(){
        this.picto.detachSelf();
        this.picto.dispose();
        this.lock.detachSelf();
        this.lock.dispose();
        this.detachSelf();
        this.dispose();
    }
}
