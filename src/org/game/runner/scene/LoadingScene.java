/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.game.runner.scene;

import org.andengine.entity.scene.background.Background;
import org.andengine.entity.text.Text;
import org.andengine.util.adt.color.Color;
import org.game.runner.GameActivity;
import org.game.runner.base.BaseScene;
import org.game.runner.manager.SceneManager.SceneType;

/**
 *
 * @author Karl
 */
public class LoadingScene extends BaseScene{
    private Text title;
    
    @Override
    public void createScene() {
        this.setBackground(new Background(Color.BLACK));
        this.title = new Text(GameActivity.CAMERA_WIDTH/2, GameActivity.CAMERA_HEIGHT/2, resourcesManager.fontPixel_60, "LOADING...", vbom);
        attachChild(this.title);
    }

    @Override
    public void onBackKeyPressed() {
    }

    @Override
    public SceneType getSceneType() {
        return SceneType.SCENE_LOADING;
    }

    @Override
    public void onPause() {
    }

    @Override
    public void onResume() {
    }

    @Override
    public void disposeScene() {
        this.title.detachSelf();
        this.title.dispose();
        this.detachSelf();
        this.dispose();
    }
}
