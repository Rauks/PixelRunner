/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.kirauks.pixelrunner.scene.base;

import net.kirauks.pixelrunner.GameActivity;
import net.kirauks.pixelrunner.manager.SceneManager.SceneType;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.shape.Shape;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.adt.list.SmartList;

/**
 *
 * @author Karl
 */
public abstract class BaseListMenuScene extends BaseMenuScene implements IOnSceneTouchListener{
    private static final float MINIMUM_TOUCH_LENGTH_TO_SLIDE_DEFAULT = 50f;
    
    private enum SlideState {
        IDLE, SLIDING;
    }
    
    private SlideState mState;
    private float mStartSlide;
    private float mMinimumTouchLengthToSlide;
    private float lastY;
    
    private SmartList<Shape> elements;

    public BaseListMenuScene() {
        this.elements = new SmartList<Shape>();
    }
        
    @Override
    public void createScene(){
        super.createScene();

        this.setOnSceneTouchListener(this);
    }
    
    public void addElementAtEnd(Shape element, float margin){
        if(this.elements.isEmpty()){
            element.setPosition(GameActivity.CAMERA_WIDTH/2, GameActivity.CAMERA_HEIGHT/2);
        }
        else{
            Shape last = this.elements.getLast();
            element.setPosition(last.getX(), last.getY() - last.getHeight()/2 - element.getHeight()/2 - margin);
        }
        this.attachChild(element);
        this.elements.add(element);
    }
    public void addElementAtEnd(Shape element){
        this.addElementAtEnd(element, 0);
    }
    
    //IOnSceneTouchListener
    @Override
    public boolean onSceneTouchEvent(final Scene pScene, final TouchEvent pSceneTouchEvent) {
        final float touchY = pSceneTouchEvent.getY();
        switch(pSceneTouchEvent.getAction()) {
            case TouchEvent.ACTION_DOWN:
                this.mStartSlide = touchY;
                this.lastY = this.getY();
                this.mState = SlideState.IDLE;
                return true;
            case TouchEvent.ACTION_MOVE:
                if (this.mState != SlideState.SLIDING && Math.abs(touchY - mStartSlide) >= this.mMinimumTouchLengthToSlide) {
                    this.mState = SlideState.SLIDING;
                    // Avoid jerk after state change.
                    this.mStartSlide = touchY;
                    return true;
                } else if (this.mState == SlideState.SLIDING) {
                    float offsetY = touchY - mStartSlide;
                    this.setY(lastY + offsetY);
                    return true;
                } else {
                    return false;
                }
            case TouchEvent.ACTION_UP:
            case TouchEvent.ACTION_CANCEL:
                return true;
            default:
                return false;
        }
    }
    
    @Override
    public void disposeScene() {
        super.disposeScene();
        for(Shape element : this.elements){
            element.detachSelf();
            element.dispose();
        }
    }
}
