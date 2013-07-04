/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.kirauks.pixelrunner.game.element.level;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import org.andengine.entity.shape.Shape;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.color.Color;
import net.kirauks.pixelrunner.game.descriptor.LevelDescriptor;
import net.kirauks.pixelrunner.game.Player;
import org.andengine.entity.modifier.ColorModifier;

/**
 *
 * @author Karl
 */
public abstract class LevelElement{
    public static final int BONUS_HEIGHT = 30;
    public static final int BONUS_WIDTH = 30;
    public static final int PLATFORM_THICKNESS = 10;
    public static final int PLATFORM_WIDTH = 200;
    
    public static final float TRAIL_MIN_SPEED_X = 150;
    public static final float TRAIL_MAX_SPEED_X = 120;
    public static final float TRAIL_MIN_SPEED_Y = -2;
    public static final float TRAIL_MAX_SPEED_Y = 2;
    public static final float TRAIL_MIN_RATE = 5;
    public static final float TRAIL_MAX_RATE = 10;
    public static final int TRAIL_MAX_PARTICULES = 10;
    
    public static final Color COLOR_DEFAULT = Color.WHITE;
    public static final Color COLOR_TRAIL_DEFAULT = Color.RED;
    public static final Color COLOR_DISABLED_DEFAULT = new Color(0.2f, 0.2f, 0.2f);
    
    private Body body;
    private Shape shape;
    private int level;
    
    private float heigth;
    private float width;
    
    private boolean actionDone;
    
    public LevelElement(int level, float width, float heigth){
        if(level > LevelDescriptor.LAYERS_MAX){
            this.level = LevelDescriptor.LAYERS_MAX;
        }
        else if(level < 0){
            this.level = 0;
        }
        else{
            this.level = level;
        }
        this.heigth = heigth;
        this.width = width;
        this.actionDone = false;
    }

    protected float getHeight() {
        return this.heigth;
    }

    protected float getWidth() {
        return this.width;
    }
    
    protected float getBodyHeight(){
        return this.heigth;
    }
    protected float getBodyWidth(){
        return this.width;
    }
    
    public void build(float pX, float pY, VertexBufferObjectManager pVertexBufferObjectManager, final Player player, PhysicsWorld physicWorld){
        this.shape = this.buildShape(pX, pY + this.heigth/2 + this.level*LevelDescriptor.LAYER_HIGH, pVertexBufferObjectManager, player);
        this.shape.setColor(this.getColor());
        this.body = PhysicsFactory.createBoxBody(physicWorld, this.shape.getX(), this.shape.getY(), this.getBodyWidth(), this.getBodyHeight(), BodyDef.BodyType.KinematicBody, PhysicsFactory.createFixtureDef(0, 0, 0));
        this.body.setFixedRotation(true);
        physicWorld.registerPhysicsConnector(new PhysicsConnector(this.shape, this.body, true, false));
    }
    public Body getBuildedBody(){
        return this.body;
    }
    public Shape getBuildedShape(){
        return this.shape;
    }
    
    public Color getColor(){
        return COLOR_DEFAULT;
    }
    public void changeColor(Color color){
        this.getBuildedShape().clearEntityModifiers();
        this.getBuildedShape().registerEntityModifier(new ColorModifier(0.3f, this.getBuildedShape().getColor(), color));
    }
    
    protected abstract Shape buildShape(float pX, float pY, VertexBufferObjectManager pVertexBufferObjectManager, final Player player);
    protected abstract void playerAction(Player player);
    public void doPlayerAction(Player player){
        if(!this.actionDone){
            this.playerAction(player);
            this.actionDone = true;
        }
    }
    public boolean isPlatform(){
        return false;
    }
}
