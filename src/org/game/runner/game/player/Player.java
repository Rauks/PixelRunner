/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.game.runner.game.player;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 *
 * @author Karl
 */
public class Player extends AnimatedSprite{
    final long[] PLAYER_ANIMATE_RUN = new long[]        { 80,     80,     80,     80                          };
    final int[] PLAYER_ANIMATE_RUN_FRAMES = new int[]   { 8,      9,      10,     9                           };
    final long[] PLAYER_ANIMATE_JUMP = new long[]       { 1000                                                 };
    final int[] PLAYER_ANIMATE_JUMP_FRAMES = new int[]  { 12                                                  };
    final long[] PLAYER_ANIMATE_ROLL = new long[]       { 100,    100,    100,    100,    100,    100,    100 };
    final int[] PLAYER_ANIMATE_ROLL_FRAMES = new int[]  { 0,      1,      2,      3,      4,      5,      6   };
    
    private Body body;
    private int jump;
    
    public Player(float pX, float pY, ITiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager, PhysicsWorld physicWorld){
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
        this.createPhysics(physicWorld);
        this.onGround();
    }
    
    private void createPhysics(PhysicsWorld physicWorld){        
        this.body = PhysicsFactory.createBoxBody(physicWorld, this, BodyType.DynamicBody, PhysicsFactory.createFixtureDef(0, 0, 0));
        this.body.setFixedRotation(true);
        physicWorld.registerPhysicsConnector(new PhysicsConnector(this, this.body, true, false));
    }
    
    private void increaseJumpLevel(){
        this.jump++;
    }
    public void onGround(){
        this.jump = 0;
        this.run();
    }
    public void jump(){
        if(this.jump < 2){
            this.body.setLinearVelocity(new Vector2(0, 15));
            this.animate(PLAYER_ANIMATE_JUMP, PLAYER_ANIMATE_JUMP_FRAMES, true);
            this.increaseJumpLevel();
        }
    }
    
    public void roll(){
        this.animate(PLAYER_ANIMATE_ROLL, PLAYER_ANIMATE_ROLL_FRAMES, false, new IAnimationListener() {
            @Override
            public void onAnimationStarted(AnimatedSprite pAnimatedSprite, int pInitialLoopCount) {
            }
            
            @Override
            public void onAnimationFrameChanged(AnimatedSprite pAnimatedSprite, int pOldFrameIndex, int pNewFrameIndex) {
            }
            
            @Override
            public void onAnimationLoopFinished(AnimatedSprite pAnimatedSprite, int pRemainingLoopCount, int pInitialLoopCount) {
            }
            
            @Override
            public void onAnimationFinished(AnimatedSprite pAnimatedSprite) {
                Player.this.run();
            }
        });
    }
    
    public void run(){
        this.animate(PLAYER_ANIMATE_RUN, PLAYER_ANIMATE_RUN_FRAMES, true);
    }
    
    public Body getBody(){
        return this.body;
    }
}
