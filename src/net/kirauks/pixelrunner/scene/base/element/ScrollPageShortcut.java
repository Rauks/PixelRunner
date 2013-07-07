/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.kirauks.pixelrunner.scene.base.element;

import net.kirauks.pixelrunner.manager.ResourcesManager;
import org.andengine.entity.IEntity;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.ITouchArea;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.color.Color;

/**
 *
 * @author Karl
 */
public class ScrollPageShortcut extends Text{
    private static float TOUCH_AREA = 36;
    
    private Rectangle touchArea;
    private IScrollPageShortcutTouchListener touchListener;
    private int index;
    
    public ScrollPageShortcut(final int index, VertexBufferObjectManager pVertexBufferObjectManager){
        super(0, 0, ResourcesManager.getInstance().fontPixel_34, String.valueOf(index), pVertexBufferObjectManager);
        this.index = index;
        this.touchArea = new Rectangle(this.getWidth()/2, this.getHeight()/2, TOUCH_AREA, TOUCH_AREA, pVertexBufferObjectManager){
            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X, float Y){
                if(ScrollPageShortcut.this.touchListener != null && pSceneTouchEvent.isActionUp()){
                    ScrollPageShortcut.this.touchListener.onShortcut(index);
                }
                return false;
            };
        };
        this.touchArea.setAlpha(0f);
        this.attachChild(this.touchArea);
    }
    
    public void registerScrollPageShortcutTouchListener(IScrollPageShortcutTouchListener touchListener){
        this.touchListener = touchListener;
    }
    public void unregisterScrollPageShortcutTouchListener(){
        this.touchListener = null;
    }
    
    public ITouchArea getShortcutTouchArea(){
        return this.touchArea;
    }
    
    public void disposeShortcut(){
        this.touchArea.detachSelf();
        this.touchArea.dispose();
        this.detachSelf();
        this.dispose();
    }
}
