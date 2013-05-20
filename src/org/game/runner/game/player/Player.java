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
public abstract class Player extends AnimatedSprite{
    public enum JumpMode{
        DOUBLE, INFINITE
    }
    
    final long[] PLAYER_ANIMATE_RUN = new long[]        { 80,     80,     80,     80                          };
    final int[] PLAYER_ANIMATE_RUN_FRAMES = new int[]   { 8,      9,      10,     9                           };
    final long[] PLAYER_ANIMATE_JUMP = new long[]       { 1000                                                };
    final int[] PLAYER_ANIMATE_JUMP_FRAMES = new int[]  { 12                                                  };
    final long[] PLAYER_ANIMATE_ROLL = new long[]       { 60,     60,     60,     60,     60,     60,     60  };
    final int[] PLAYER_ANIMATE_ROLL_FRAMES = new int[]  { 0,      1,      2,      3,      4,      5,      6   };
    
    private Body body;
    
    private JumpMode jumpMode;
    private final float JUMP_INFINITE_MAX_Y = 350;
    private int jumpCount;
    private boolean jumping;
    
    private boolean rolling;
    
    private float speed;
    
    private boolean hasLife;
    
    public Player(float pX, float pY, ITiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager, PhysicsWorld physicWorld){
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
        this.createPhysics(physicWorld);
        this.resetMovements();
        this.resetBonus();
    }
    
    private void createPhysics(PhysicsWorld physicWorld){        
        this.body = PhysicsFactory.createBoxBody(physicWorld, this, BodyType.DynamicBody, PhysicsFactory.createFixtureDef(0, 0, 0));
        this.body.setFixedRotation(true);
        physicWorld.registerPhysicsConnector(new PhysicsConnector(this, this.body, true, false));
    }
    
    private void increaseJumpLevel(){
        this.jumpCount++;
    }
    public void resetMovements(){
        this.jumping = false;
        this.rolling = false;
        this.jumpCount = 0;
        this.run();
    }
    public void resetBonus(){
        this.jumpMode = JumpMode.DOUBLE;
        this.speed = 1f;
        this.hasLife = false;
        this.setColor(Color.WHITE);
    }
    public void jump(){
        switch(this.jumpMode){
            case DOUBLE:
                if(this.jumpCount < 2 && !this.rolling){
                    this.doJump();
                }
                break;
            case INFINITE:
                if(this.getY() < JUMP_INFINITE_MAX_Y && !this.rolling){
                    this.doJump();
                }
                break;
        }
    }
    private void doJump(){
        this.jumping = true;
        this.rolling = false;
        this.animate(PLAYER_ANIMATE_JUMP, PLAYER_ANIMATE_JUMP_FRAMES, true);
        this.body.setLinearVelocity(new Vector2(0, 15));
        this.increaseJumpLevel();
        this.onJump();
    }

    public float getSpeed() {
        return this.speed;
    }
    public void setSpeed(float speed){
        this.speed = speed;
        this.onSpeedChange(speed);
        this.onBonus();
    }
    public void setJumpMode(JumpMode jumpMode){
        this.jumpMode = jumpMode;
        this.onJumpModeChange();
        this.onBonus();
    }
    public void getLife(){
        this.hasLife = true;
        this.onGetLife();
        this.onBonus();
    }
    public boolean hasLife(){
        return this.hasLife;
    }
    public void rollBackJump(){
        this.jumpCount = 2;
        this.jumping = true;
        this.rolling = true;
        this.animate(PLAYER_ANIMATE_ROLL, PLAYER_ANIMATE_ROLL_FRAMES, true);
        this.body.setLinearVelocity(new Vector2(0, 25));
        this.onRollBackJump();
    }
    public void roll(){
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
            this.onRoll();
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
        for(int i = 0; i < this.getChildCount(); i++) {
            this.mChildren.get(i).setColor(color);
        }
        super.setColor(color);
    }
    
    protected abstract void onSpeedChange(float speed);
    protected abstract void onJump();
    protected abstract void onGetLife();
    protected abstract void onJumpModeChange();
    protected abstract void onRoll();
    protected abstract void onRollBackJump();
    protected abstract void onBonus();
}
