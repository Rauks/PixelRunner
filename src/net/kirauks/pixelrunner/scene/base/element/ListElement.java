/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.kirauks.pixelrunner.scene.base.element;

import android.util.Log;
import net.kirauks.pixelrunner.manager.AudioManager;
import net.kirauks.pixelrunner.manager.ResourcesManager;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.align.HorizontalAlign;

/**
 *
 * @author Karl
 */
public class ListElement extends Text{
    private IListElementTouchListener listener;
    
    private String name;
    
    public ListElement(String name, VertexBufferObjectManager pVertexBufferObjectManager){
        super(0, 0, ResourcesManager.getInstance().fontPixel_60, name, new TextOptions(HorizontalAlign.LEFT), pVertexBufferObjectManager);
        this.name = name;
    }
    
    public String getName(){
        return this.name;
    }
    
    @Override
    public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X, float Y){
        if (pSceneTouchEvent.isActionUp() && this.listener != null){
            ListElement.this.listener.onElementActionUp(this);
        }
        return false;
    };  
    
    public void registerListElementTouchListener(IListElementTouchListener listener){
        this.listener = listener;
    }
    public void unregisterListElementTouchListener(){
        this.listener = null;
    }
    
    public void disposeElement(){
        this.detachSelf();
        this.dispose();
    }
}
