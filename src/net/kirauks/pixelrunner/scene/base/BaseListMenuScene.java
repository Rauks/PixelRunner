/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.kirauks.pixelrunner.scene.base;

import net.kirauks.pixelrunner.GameActivity;
import net.kirauks.pixelrunner.manager.SceneManager.SceneType;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.shape.Shape;
import org.andengine.input.touch.TouchEvent;
import org.andengine.input.touch.detector.ScrollDetector;
import org.andengine.input.touch.detector.ScrollDetector.IScrollDetectorListener;
import org.andengine.input.touch.detector.SurfaceScrollDetector;
import org.andengine.util.adt.list.SmartList;

/**
 *
 * @author Karl
 */
public abstract class BaseListMenuScene extends BaseMenuScene implements IScrollDetectorListener, IOnSceneTouchListener{
    private static final float FREQ_D = 50.0f;
    private static final float MAX_ACCEL = 5000;
    private static final double FRICTION = 0.96f;
    
    private static final int WRAPPER_HEIGHT = 1260;
    private static final float START_Y = GameActivity.CAMERA_HEIGHT;
    private float wrapperHeight;
    private float maxY;
    
    private enum SlideState{
        WAIT, SCROLLING, MOMENTUM, DISABLE;
    }
    
    private TimerHandler thandle;
    private SlideState currentState;
    private double accel, accel1, accel0;
    private float currentY;
    private IEntity wrapper;
    private SurfaceScrollDetector scrollDetector;
    private long t0;
    
    private SmartList<Shape> elements;
        
    @Override
    public void createScene(){
        super.createScene();
        
        this.elements = new SmartList<Shape>();
        
        this.wrapper = new Entity(GameActivity.CAMERA_WIDTH/2, START_Y);
        this.wrapperHeight = 0;
        this.attachChild(this.wrapper);

        this.thandle = new TimerHandler(1.0f / FREQ_D, true, new ITimerCallback() {
            @Override
            public void onTimePassed(final TimerHandler pTimerHandler) {
                BaseListMenuScene.this.doSetPos();
            }
        });
        
        this.scrollDetector = new SurfaceScrollDetector(this);
        this.setOnSceneTouchListener(this);
        
        this.registerUpdateHandler(this.thandle);
        this.currentState = SlideState.WAIT;
    }
    
    public void addElementAtEnd(Shape element, float margin){
        if(this.elements.isEmpty()){
            element.setPosition(0, -element.getHeight()/2 - margin);
            this.wrapperHeight += element.getHeight() + 2*margin;
        }
        else{
            Shape last = this.elements.getLast();
            element.setPosition(last.getX(), last.getY() - last.getHeight()/2 - element.getHeight()/2 - margin);
            this.wrapperHeight += element.getHeight() + margin;
        }
        this.maxY = (this.wrapperHeight > GameActivity.CAMERA_HEIGHT) ? (this.wrapperHeight - GameActivity.CAMERA_HEIGHT) : 0;
        this.wrapper.attachChild(element);
        this.elements.add(element);
    }
    public void addElementAtEnd(Shape element){
        this.addElementAtEnd(element, 0);
    }
    
    //IOnSceneTouchListener
    @Override
    public boolean onSceneTouchEvent(final Scene pScene, final TouchEvent pSceneTouchEvent) {
        if (this.currentState == SlideState.DISABLE) {
            return true;
        }
        if (this.currentState == SlideState.MOMENTUM) {
            this.accel0 = this.accel1 = this.accel = 0;
            this.currentState = SlideState.WAIT;
        }
        this.scrollDetector.onTouchEvent(pSceneTouchEvent);
        return true;
    }

    //IScrollDetectorListener
    @Override
    public void onScrollStarted(ScrollDetector pScollDetector, int pPointerID, float pDistanceX, float pDistanceY) {
        this.t0 = System.currentTimeMillis();
        this.currentState = SlideState.SCROLLING;
    }
    @Override
    public void onScroll(ScrollDetector pScollDetector, int pPointerID, float pDistanceX, float pDistanceY) {
        long dt = System.currentTimeMillis() - this.t0;
        if (dt == 0) {
            return;
        }
        double s =  pDistanceY / (double)dt * 1000.0;  // pixel/second
        this.accel = (this.accel0 + this.accel1 + s) / 3;
        this.accel0 = this.accel1;
        this.accel1 = this.accel;

        this.t0 = System.currentTimeMillis();
        this.currentState = SlideState.SCROLLING;
    }
    @Override
    public void onScrollFinished(ScrollDetector pScollDetector, int pPointerID, float pDistanceX, float pDistanceY) {
        this.currentState = SlideState.MOMENTUM;
    }
    
    protected synchronized void doSetPos() {
        if (this.accel == 0) {
            return;
        }

        if (this.currentY < 0) {
            this.currentY = 0;
            this.currentState = SlideState.WAIT;
            this.accel0 = this.accel1 = this.accel = 0;
        }
        if (this.currentY > this.maxY) {
            this.currentY = this.maxY;
            this.currentState = SlideState.WAIT;
            this.accel0 = this.accel1 = this.accel = 0;
        }
        this.wrapper.setY(START_Y + this.currentY);

        if (this.accel < 0 && this.accel < -MAX_ACCEL) {
            this.accel0 = this.accel1 = this.accel = -MAX_ACCEL;
        }
        if (this.accel > 0 && this.accel > MAX_ACCEL) {
            this.accel0 = this.accel1 = this.accel = MAX_ACCEL;
        }

        double deltaY = accel / FREQ_D;
        if (deltaY >= -1 && deltaY <= 1) {
            this.currentState = SlideState.WAIT;
            this.accel0 = this.accel1 = this.accel = 0;
            return;
        }
        if (!Double.isNaN(deltaY) && !Double.isInfinite(deltaY)) {
            this.currentY -= deltaY;
        }
        this.accel = (this.accel * FRICTION);
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
