/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.kirauks.pixelrunner.scene;

import net.kirauks.pixelrunner.R;
import net.kirauks.pixelrunner.manager.ResourcesManager;
import net.kirauks.pixelrunner.manager.SceneManager;
import net.kirauks.pixelrunner.manager.SceneManager.SceneType;
import net.kirauks.pixelrunner.manager.db.SuccessDatabase;
import net.kirauks.pixelrunner.manager.db.SuccessDatabase.Success;
import net.kirauks.pixelrunner.scene.base.BaseListMenuScene;
import net.kirauks.pixelrunner.scene.base.element.ListElement;
import net.kirauks.pixelrunner.scene.base.element.SuccessListElement;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;

/**
 *
 * @author Karl
 */
public class BonusSuccessScene extends BaseListMenuScene{
    private Sprite top;
    private Sprite bottom;
    
    public BonusSuccessScene(){
        SuccessDatabase successDb = new SuccessDatabase(this.activity);
        for(Success success : Success.values()){
            this.addSuccess(success.getTitleResId(), success.getSubTitleResId(), successDb.getSuccessStatus(success));
        }
        
        this.getListWrapper().setX(100);
        
        this.top = new Sprite(765, 450, ResourcesManager.getInstance().lvlLeft, this.vbom){
            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X, float Y){
                if (pSceneTouchEvent.isActionUp()){
                    BonusSuccessScene.this.activity.vibrate(30);
                    BonusSuccessScene.this.impulseUp();
                }
                return false;
            };
        };
        this.top.setScale(6f);
        this.top.setRotation(90f);
        this.registerTouchArea(this.top);
        this.attachChild(this.top);
        this.bottom = new Sprite(765, 30, ResourcesManager.getInstance().lvlRight, this.vbom){
            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X, float Y){
                if (pSceneTouchEvent.isActionUp()){
                    BonusSuccessScene.this.activity.vibrate(30);
                    BonusSuccessScene.this.impulseDown();
                }
                return false;
            };
        };
        this.bottom.setScale(6f);
        this.bottom.setRotation(90f);
        this.registerTouchArea(this.bottom);
        this.attachChild(this.bottom);
        this.onListMove(0, 0, 1);
    }
    
    private void addSuccess(int redIdName, int resIdSub, boolean has){
        this.addListElement(new SuccessListElement(this.activity.getString(redIdName), this.activity.getString(resIdSub), !has, this.vbom), 30);
    }
    
    @Override
    protected void onListMove(float newPos, float minPos, float maxPos) {
        if(newPos >= maxPos){
            this.bottom.setVisible(false);
        }
        else{
            this.bottom.setVisible(true);
        }
        if(newPos <= minPos){
            this.top.setVisible(false);
        }
        else{
            this.top.setVisible(true);
        }
    }

    @Override
    public void onElementAction(ListElement element) {
        
    }

    @Override
    public void onBackKeyPressed() {
        SceneManager.getInstance().createBonusChoiceScene();
        SceneManager.getInstance().disposeBonusSuccessScene();
    }

    @Override
    public SceneType getSceneType() {
        return SceneType.SCENE_BONUS_SUCCESS;
    }
    
    
    @Override
    public void disposeScene() {
        super.disposeScene();
        this.top.detachSelf();
        this.top.dispose();
        this.bottom.detachSelf();
        this.bottom.dispose();
        this.detachSelf();
        this.dispose();
    }
}
