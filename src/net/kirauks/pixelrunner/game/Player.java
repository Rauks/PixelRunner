/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.kirauks.pixelrunner.game;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.ColorModifier;
import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.color.Color;
import org.andengine.util.modifier.IModifier;
import net.kirauks.pixelrunner.game.descriptor.LevelDescriptor;

/**
 *
 * @author Karl
 */
public abstract class Player extends AnimatedSprite{

    public enum JumpMode{
        DOUBLE, INFINITE
    }
    
    public static final long[] PLAYER_ANIMATE_RUN = new long[]        { 80,     80,     80,     80                          };
    public static final int[] PLAYER_ANIMATE_RUN_FRAMES = new int[]   { 8,      9,      10,     9                           };
    public static final long[] PLAYER_ANIMATE_JUMP = new long[]       { 1000                                                };
    public static final int[] PLAYER_ANIMATE_JUMP_FRAMES = new int[]  { 12                                                  };
    public static final long[] PLAYER_ANIMATE_ROLL = new long[]       { 60,     60,     60,     60,     60,     60,     60  };
    public static final int[] PLAYER_ANIMATE_ROLL_FRAMES = new int[]  { 0,      1,      2,      3,      4,      5,      6   };
    public static final long[] PLAYER_ANIMATE_DANCE = new long[]      { 150,    100,    150,    100                         };
    public static final int[] PLAYER_ANIMATE_DANCE_FRAMES = new int[] { 13,     14,     15,     14                          };
    
    private ITimerCallback bonusPickAction = new ITimerCallback(){                      
        @Override
        public void onTimePassed(final TimerHandler pTimerHandler) {
            Player.this.unregisterUpdateHandler(pTimerHandler);
            Player.this.registerEntityModifier(new SequenceEntityModifier(new IEntityModifier.IEntityModifierListener() {
                    @Override
                    public void onModifierStarted(final IModifier<IEntity> pModifier, final IEntity pItem) {}
                    @Override
                    public void onModifierFinished(final IModifier<IEntity> pEntityModifier, final IEntity pEntity) {
                        Player.this.endBonus();
                    }
                },
                new ColorModifier(0.4f, Player.this.getColor(), Color.WHITE),
                new ColorModifier(0.4f, Color.WHITE, Player.this.getColor()),
                new ColorModifier(0.3f, Player.this.getColor(), Color.WHITE),
                new ColorModifier(0.3f, Color.WHITE, Player.this.getColor()),
                new ColorModifier(0.2f, Player.this.getColor(), Color.WHITE),
                new ColorModifier(0.2f, Color.WHITE, Player.this.getColor()),
                new ColorModifier(0.1f, Player.this.getColor(), Color.WHITE),
                new ColorModifier(0.1f, Color.WHITE, Player.this.getColor()),
                new ColorModifier(0.1f, Player.this.getColor(), Color.WHITE),
                new ColorModifier(0.1f, Color.WHITE, Player.this.getColor()),
                new ColorModifier(0.1f, Player.this.getColor(), Color.WHITE),
                new ColorModifier(0.1f, Color.WHITE, Player.this.getColor()),
                new ColorModifier(0.1f, Player.this.getColor(), Color.WHITE),
                new ColorModifier(0.1f, Color.WHITE, Player.this.getColor()))
             );
        }
    };
    
    private IPlayerListener listener;
    
    private Body body;
    
    private JumpMode jumpMode;
    private final float JUMP_INFINITE_MAX_Y = LevelDescriptor.LAYERS_MAX * LevelDescriptor.LAYER_HIGH + 150;
    private int jumpCount;
    private boolean jumping;
    private boolean rolling;
    private boolean swapped;
    private float speed;
    private boolean hasLife;
    
    public Player(float pX, float pY, ITiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager, PhysicsWorld physicWorld){
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
        this.createPhysics(physicWorld);
        this.resetMovements();
        this.resetBonus();
    }
    
    private void createPhysics(PhysicsWorld physicWorld){        
        this.body = PhysicsFactory.createBoxBody(physicWorld, this.getX(), this.getY(), this.getWidth() - 16, this.getHeight(), BodyType.DynamicBody, PhysicsFactory.createFixtureDef(0, 0, 0));
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
        this.clearUpdateHandlers();
        this.clearEntityModifiers();
        this.endBonus();
    }
    private void endBonus() {
        this.jumpMode = JumpMode.DOUBLE;
        this.speed = 1f;
        this.setAlpha(1f);
        this.swapped = false;
        this.hasLife = false;
        this.setColor(Color.WHITE);
        if(this.listener != null){
            this.listener.onBonusEnd();
        }
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
        this.body.setLinearVelocity(0, 15);
        this.increaseJumpLevel();
        if(this.listener != null){
            this.listener.onJump();
        }
    }

    public float getSpeed() {
        return this.speed;
    }
    public void setSpeed(float speed){
        this.speed = speed;
        this.fireBonusTimer();
        if(this.listener != null){
            this.listener.onBonus();
        }
    }
    public void setJumpMode(JumpMode jumpMode){
        this.jumpMode = jumpMode;
        this.fireBonusTimer();
        if(this.listener != null){
            this.listener.onBonus();
        }
    }
    public JumpMode getJumpMode(){
        return this.jumpMode;
    }
    public void getLife(){
        this.hasLife = true;
        this.fireBonusTimer();
        if(this.listener != null){
            this.listener.onBonus();
        }
    }
    public boolean hasLife(){
        return this.hasLife;
    }
    public void setSwapped() {
        this.swapped = true;
        this.fireBonusTimer();
        if(this.listener != null){
            this.listener.onBonus();
        }
    }
    public boolean isSwapped(){
        return this.swapped;
    }
    public void rollBackJump(){
        this.jumpCount = 2;
        this.jumping = true;
        this.rolling = true;
        this.animate(PLAYER_ANIMATE_ROLL, PLAYER_ANIMATE_ROLL_FRAMES, true);
        this.body.setLinearVelocity(0, 25);
        if(this.listener != null){
            this.listener.onRollBackJump();
        }
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
                    Player.this.resetMovements();
                    Player.this.run();
                }
            });
        if(this.listener != null){
            this.listener.onRoll();
        }
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
    
    private void fireBonusTimer(){
        this.registerUpdateHandler(new TimerHandler(8, this.bonusPickAction));
    }
    
    public void registerPlayerListener(IPlayerListener listener){
        this.listener = listener;
    }
    public void unregisterPlayerListener(){
        this.listener = null;
    }
    
    public void changeColor(Color color){
        this.clearEntityModifiers();
        this.registerEntityModifier(new ColorModifier(0.5f, this.getColor(), color));
    }
}
