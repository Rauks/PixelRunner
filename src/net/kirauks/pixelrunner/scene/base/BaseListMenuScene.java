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
    private static final float FREQ_D = 120.0f;
    private static final int WRAPPER_HEIGHT = 1260;
    private static final float MAX_ACCEL = 5000;
    private static final double FRICTION = 0.96f;
    
    private enum SlideState{
        WAIT, SCROLLING, MOMENTUM, DISABLE;
    }
    
    private TimerHandler thandle;
    private SlideState mState;
    private double accel, accel1, accel0;
    private float mCurrentY;
    private IEntity mWrapper;
    private SurfaceScrollDetector mScrollDetector;
    private long t0;
    
    private SmartList<Shape> elements;

    public BaseListMenuScene() {
        this.elements = new SmartList<Shape>();
        
    }
        
    @Override
    public void createScene(){
        super.createScene();

        this.thandle = new TimerHandler(1.0f / FREQ_D, true, new ITimerCallback() {
            @Override
            public void onTimePassed(final TimerHandler pTimerHandler) {
                BaseListMenuScene.this.doSetPos();
            }
        });
        
        this.mScrollDetector = new SurfaceScrollDetector(this);
        this.setOnSceneTouchListener(this);
        
        //Test purpose only
        this.mWrapper = new Entity(0, 0);
        for (int i = 0; i < 20; i++) {
            Rectangle r = new Rectangle(17.5f, i * 100 + 20, 445, 80, this.vbom);
            this.mWrapper.attachChild(r);
        }
        this.attachChild(this.mWrapper);
        //End test purpose
        
        this.registerUpdateHandler(this.thandle);
        this.mState = SlideState.WAIT;
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
        if (this.mState == SlideState.DISABLE) {
            return true;
        }
        if (this.mState == SlideState.MOMENTUM) {
            this.accel0 = this.accel1 = this.accel = 0;
            this.mState = SlideState.WAIT;
        }
        this.mScrollDetector.onTouchEvent(pSceneTouchEvent);
        return true;
    }

    //IScrollDetectorListener
    @Override
    public void onScrollStarted(ScrollDetector pScollDetector, int pPointerID, float pDistanceX, float pDistanceY) {
        this.t0 = System.currentTimeMillis();
        this.mState = SlideState.SCROLLING;
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
        this.mState = SlideState.SCROLLING;
    }
    @Override
    public void onScrollFinished(ScrollDetector pScollDetector, int pPointerID, float pDistanceX, float pDistanceY) {
        this.mState = SlideState.MOMENTUM;
    }
    
    protected synchronized void doSetPos() {
        if (this.accel == 0) {
            return;
        }

        if (this.mCurrentY > 0) {
            this.mCurrentY = 0;
            this.mState = SlideState.WAIT;
            this.accel0 = this.accel1 = this.accel = 0;
        }
        if (this.mCurrentY < -WRAPPER_HEIGHT) {
            this.mCurrentY = -WRAPPER_HEIGHT;
            this.mState = SlideState.WAIT;
            this.accel0 = this.accel1 = this.accel = 0;
        }
        this.mWrapper.setPosition(0, this.mCurrentY);

        if (this.accel < 0 && this.accel < -MAX_ACCEL) {
            this.accel0 = this.accel1 = this.accel = - MAX_ACCEL;
        }
        if (this.accel > 0 && this.accel > MAX_ACCEL) {
            this.accel0 = this.accel1 = this.accel = MAX_ACCEL;
        }

        double ny = accel / FREQ_D;
        if (ny >= -1 && ny <= 1) {
            this.mState = SlideState.WAIT;
            this.accel0 = this.accel1 = this.accel = 0;
            return;
        }
        if (! (Double.isNaN(ny) || Double.isInfinite(ny))) {
            this.mCurrentY += ny;
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
