/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.game.runner.scene;

import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.ColorModifier;
import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.adt.align.HorizontalAlign;
import org.andengine.util.adt.color.Color;
import org.andengine.util.modifier.IModifier;
import org.game.runner.GameActivity;
import org.game.runner.base.BaseMenuScene;
import org.game.runner.game.player.Player;
import org.game.runner.manager.SceneManager;
import org.game.runner.manager.SceneManager.SceneType;

/**
 *
 * @author Karl
 */
public class CreditScene extends BaseMenuScene implements IOnSceneTouchListener{
    private float speed = 1f;
    private LoopEntityModifier loopScroll;
    
    private Text title;
    private Text prod;
    private Text prodNames;
    private Text design;
    private Text designNames;
    private Text thanks;
    private Text thanksNames;
    private Text tests;
    private Text testsNames;
    
    private Entity e;
    
    @Override
    public void createScene() {
        super.createScene();
        
        float centerX = GameActivity.CAMERA_WIDTH/2;
        float centerY = GameActivity.CAMERA_HEIGHT/2;
        
        this.title = new Text(centerX, centerY + 150, resourcesManager.fontPixel_100, "CREDITS", vbom);
        this.title.setCullingEnabled(true);
        this.prod = new Text(centerX, 0, resourcesManager.fontPixel_34, "PROGRAMMING", vbom);
        this.prod.setCullingEnabled(true);
        this.prodNames = new Text(centerX, 0, resourcesManager.fontPixel_60, "KARL WODITSCH", new TextOptions(HorizontalAlign.CENTER), vbom);
        this.prodNames.setCullingEnabled(true);
        this.design = new Text(centerX, 0, resourcesManager.fontPixel_34, "DESIGN", vbom);
        this.design.setCullingEnabled(true);
        this.designNames = new Text(centerX, 0, resourcesManager.fontPixel_60, "FLORENT LACROIX", new TextOptions(HorizontalAlign.CENTER), vbom);
        this.designNames.setCullingEnabled(true);
        this.tests = new Text(centerX, 0, resourcesManager.fontPixel_34, "BETA TESTERS", vbom);
        this.tests.setCullingEnabled(true);
        this.testsNames = new Text(centerX, 0, resourcesManager.fontPixel_60, "AURELIE KERAVAL\nCHARLES HAZARD\nGEORGES OLIVARES\nLUCAS D'AGUI\nNICOLAS DEVENET\nSIMON CHEVALIER", new TextOptions(HorizontalAlign.CENTER), vbom);
        this.testsNames.setCullingEnabled(true);
        this.thanks = new Text(centerX, 0, resourcesManager.fontPixel_34, "SPECIAL THANKS", vbom);
        this.thanks.setCullingEnabled(true);
        this.thanksNames = new Text(centerX, 0, resourcesManager.fontPixel_60, "ENSISA\nLEONARD DAVER\nMICHEL HASSENFORDER\nNICOLAS FRUTEAU\nSUZANNE NOLL\nTHIBAUT MEYER\nVALERIANE JEAN", new TextOptions(HorizontalAlign.CENTER), vbom);
        this.thanksNames.setCullingEnabled(true);
        
        this.setFlow(this.title, 30f, 5f, this.prod, this.prodNames, this.design, this.designNames, this.tests, this.testsNames, this.thanks, this.thanksNames);
        
        attachChild(this.title);
        attachChild(this.prod);
        attachChild(this.prodNames);
        attachChild(this.design);
        attachChild(this.designNames);
        attachChild(this.tests);
        attachChild(this.testsNames);
        attachChild(this.thanks);
        attachChild(this.thanksNames);
        
        final float startY = this.title.getY() + this.title.getHeight()/2 + centerY;
        final float endY = this.thanksNames.getY() - this.thanksNames.getHeight()/2 - centerY;
        this.e = new Entity();
        this.e.setPosition(centerX, startY);
        this.camera.setChaseEntity(this.e);
        this.e.registerEntityModifier(new LoopEntityModifier(new MoveModifier(30f, this.e.getX(), startY, this.e.getX(), endY), LoopEntityModifier.LOOP_CONTINUOUS, new IEntityModifier.IEntityModifierListener() {
            @Override
            public void onModifierStarted(final IModifier<IEntity> pModifier, final IEntity pItem) {}
            @Override
            public void onModifierFinished(final IModifier<IEntity> pEntityModifier, final IEntity pEntity) {
                CreditScene.this.e.setPosition(CreditScene.this.e.getX(), startY);
            }
        }){
            @Override
            public float onUpdate(final float pSecondsElapsed, final IEntity pItem){
                return super.onUpdate(CreditScene.this.speed * pSecondsElapsed, pItem);
            }
        });
        attachChild(this.e);
        
        this.setOnSceneTouchListener(this);
    }
    
    private void setFlow(Entity start, float oddMargin, float evenMargin, Entity... entities){
        Entity prev = start;
        int i = 0;
        for(Entity e : entities){
            if(i % 2 == 0){
                e.setY(prev.getY() - prev.getHeight()/2 - oddMargin - e.getHeight()/2);
            }
            else{
                e.setY(prev.getY() - prev.getHeight()/2 - evenMargin - e.getHeight()/2);
            }
            i++;
            prev = e;
        }
    }

    @Override
    public SceneType getSceneType() {
        return SceneType.SCENE_CREDITS;
    }
    
    @Override
    public void onBackKeyPressed() {
        this.camera.setChaseEntity(null);
        this.camera.setCenter(400, 240);
        SceneManager.getInstance().createMainMenuScene();
        SceneManager.getInstance().disposeCreditsScene();
    }

    @Override
    public void disposeScene() {
        super.disposeScene();
        this.title.detachSelf();
        this.title.dispose();
        this.prod.detachSelf();
        this.prod.dispose();
        this.prodNames.detachSelf();
        this.prodNames.dispose();
        this.design.detachSelf();
        this.design.dispose();
        this.designNames.detachSelf();
        this.designNames.dispose();
        this.tests.detachSelf();
        this.tests.dispose();
        this.testsNames.detachSelf();
        this.testsNames.dispose();
        this.thanks.detachSelf();
        this.thanks.dispose();
        this.thanksNames.detachSelf();
        this.thanksNames.dispose();
        this.e.detachSelf();
        this.e.dispose();
        this.detachSelf();
        this.dispose();
    }
    
    @Override
    public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
        if (pSceneTouchEvent.isActionUp()){
            this.speed = 1f;
        }
        else if(pSceneTouchEvent.isActionDown()){
            this.speed = 4f;
        }
    return false;
    }
}
