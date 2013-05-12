/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.game.runner.scene;

import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.util.adt.align.HorizontalAlign;
import org.game.runner.base.BaseMenuScene;
import org.game.runner.base.BaseScene;
import org.game.runner.manager.SceneManager;
import org.game.runner.manager.SceneManager.SceneType;

/**
 *
 * @author Karl
 */
public class CreditScene extends BaseMenuScene{
    private Text title;
    private Text prod;
    private Text prodNames;
    private Text thanks;
    private Text thanksNames;
    
    @Override
    public void createScene() {
        super.createScene();
        this.title = new Text(this.camera.getWidth()/2, this.camera.getHeight()/2 + 150, resourcesManager.fontPixel_100, "CREDITS", vbom);
        this.prod = new Text(this.camera.getWidth()/2, this.camera.getHeight()/2 + 70, resourcesManager.fontPixel_34, "DESIGN + PRODUCTION", vbom);
        this.prodNames = new Text(this.camera.getWidth()/2, this.camera.getHeight()/2, resourcesManager.fontPixel_60, "KARL WODITSCH\nFLORENT LACROIX", new TextOptions(HorizontalAlign.CENTER), vbom);
        this.thanks = new Text(this.camera.getWidth()/2, this.camera.getHeight()/2 - 90, resourcesManager.fontPixel_34, "SPECIAL THANKS", vbom);
        this.thanksNames = new Text(this.camera.getWidth()/2, this.camera.getHeight()/2 - 130, resourcesManager.fontPixel_60, "MICHEL HASSENFORDER", vbom);
        attachChild(this.title);
        attachChild(this.prod);
        attachChild(this.prodNames );
        attachChild(this.thanks );
        attachChild(this.thanksNames );
    }

    @Override
    public SceneType getSceneType() {
        return SceneType.SCENE_CREDITS;
    }
    
    @Override
    public void onBackKeyPressed() {
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
        this.thanks.detachSelf();
        this.thanks.dispose();
        this.thanksNames.detachSelf();
        this.thanksNames.dispose();
        this.detachSelf();
        this.dispose();
    }
    
}
