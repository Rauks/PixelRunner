/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.game.runner.game.player;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import org.andengine.entity.IEntity;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.color.Color;

/**
 *
 * @author Karl
 */
public class Player extends AnimatedSprite{
    final long[] PLAYER_ANIMATE_RUN = new long[]        { 80,     80,     80,     80                          };
    final int[] PLAYER_ANIMATE_RUN_FRAMES = new int[]   { 8,      9,      10,     9                           };
    final long[] PLAYER_ANIMATE_JUMP = new long[]       { 1000                                                };
    final int[] PLAYER_ANIMATE_JUMP_FRAMES = new int[]  { 12                                                  };
    final long[] PLAYER_ANIMATE_ROLL = new long[]       { 60,     60,     60,     60,     60,     60,     60  };
    final int[] PLAYER_ANIMATE_ROLL_FRAMES = new int[]  { 0,      1,      2,      3,      4,      5,      6   };
    
    private Body body;
    private int jumpCount;
    private boolean jumping;
    private boolean rolling;
    
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
        this.jumpCount++;
    }
    public void onGround(){
        this.jumping = false;
        this.rolling = false;
        this.jumpCount = 0;
        this.run();
    }
    public void jump(final IPlayerActionCallback playerActionCallbask){
        if(this.jumpCount < 2 && !this.rolling){
            this.jumping = true;
            this.rolling = false;
            this.animate(PLAYER_ANIMATE_JUMP, PLAYER_ANIMATE_JUMP_FRAMES, true);
            this.body.setLinearVelocity(new Vector2(0, 15));
            this.increaseJumpLevel();
            playerActionCallbask.onActionDone();
        }
    }
    public void rollBackJump(final IPlayerActionCallback playerActionCallbask){
        this.jumpCount = 2;
        this.jumping = true;
        this.rolling = true;
        this.animate(PLAYER_ANIMATE_ROLL, PLAYER_ANIMATE_ROLL_FRAMES, true);
        this.body.setLinearVelocity(new Vector2(0, 25));
        playerActionCallbask.onActionDone();
    }
    
    public void roll(final IPlayerActionCallback playerActionCallbask){
        if(!this.rolling){
            this.animate(PLAYER_ANIMATE_ROLL, PLAYER_ANIMATE_ROLL_FRAMES, false, new IAnimationListener() {
                @Override
                public void onAnimationStarted(AnimatedSprite pAnimatedSprite, int pInitialLoopCount) {
                    Player.this.rolling = true;
                }

                @Override
                public void onAnimationFrameChanged(AnimatedSprite pAnimatedSprite, int pOldFrameIndex, int pNewFrameIndex) {
                }

                @Override
                public void onAnimationLoopFinished(AnimatedSprite pAnimatedSprite, int pRemainingLoopCount, int pInitialLoopCount) {
                }

                @Override
                public void onAnimationFinished(AnimatedSprite pAnimatedSprite) {
                    Player.this.rolling = false;
                    Player.this.run();
                }
            });
            playerActionCallbask.onActionDone();
        }
    }
    
    public void run(){
        this.animate(PLAYER_ANIMATE_RUN, PLAYER_ANIMATE_RUN_FRAMES, true);
    }
    
    public Body getBody(){
        return this.body;
    }

    public boolean isJumping() {
        return this.jumping;
    }

    public boolean isRolling() {
        return this.rolling;
    }
    
    @Override
    public void setColor(Color color){
        for(IEntity child : this.mChildren){
            child.setColor(color);
        }
        super.setColor(color);
    }
}
