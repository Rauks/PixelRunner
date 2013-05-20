/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.game.runner.game.player;

import android.opengl.GLES20;
import java.util.Random;
import org.andengine.entity.IEntity;
import org.andengine.entity.particle.Particle;
import org.andengine.entity.particle.SpriteParticleSystem;
import org.andengine.entity.particle.emitter.PointParticleEmitter;
import org.andengine.entity.particle.initializer.BlendFunctionParticleInitializer;
import org.andengine.entity.particle.initializer.ExpireParticleInitializer;
import org.andengine.entity.particle.initializer.IParticleInitializer;
import org.andengine.entity.particle.initializer.VelocityParticleInitializer;
import org.andengine.entity.particle.modifier.AlphaParticleModifier;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.color.Color;

/**
 *
 * @author Karl
 */
public class Trail extends SpriteParticleSystem{
    public enum ColorMode{
        NORMAL, MULTICOLOR
    }
    
    private Random ranGen = new Random();
    
    private int dX;
    private int dY;
    private ColorMode colorMode;
    
    public Trail(int pX, int pY, int dX, int dY, ColorMode colorMode, IEntity attachTo, ITextureRegion pTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager){
        super(new PointParticleEmitter(pX, pY), 15, 20, 40, pTextureRegion, pVertexBufferObjectManager);
        this.dX = dX;
        this.dY = dY;
        this.colorMode = colorMode;
        attachTo.attachChild(this);
        this.setZIndex(attachTo.getZIndex() - 1);
        this.sortChildren();
        this.initInitializers();
    }
    
    private Color getRandomColor(){
        switch(this.colorMode){
            case MULTICOLOR:
                switch(ranGen.nextInt(6)){
                    case 0:
                        return Color.BLUE;
                    case 1:
                        return Color.CYAN;
                    case 2:
                        return Color.GREEN;
                    case 3:
                        return Color.RED;
                    case 4:
                        return Color.YELLOW;
                    case 5:
                        return Color.PINK;
                    default:
                        return Color.WHITE;
                }
            default:
            case NORMAL:
                return this.getColor();
        }
    }
    
    private void initInitializers(){
        this.addParticleInitializer(new BlendFunctionParticleInitializer<Sprite>(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE));
        this.addParticleInitializer(new VelocityParticleInitializer<Sprite>(-320, -280, -2, 2));
        this.addParticleInitializer(new ExpireParticleInitializer<Sprite>(1f));
        this.addParticleModifier( new AlphaParticleModifier<Sprite>(0.5f, 1f, 1f, 0f) );
        this.addParticleInitializer(new IParticleInitializer<Sprite>() {
            @Override
            public void onInitializeParticle(Particle<Sprite> pParticle) {
                Sprite pSprite = pParticle.getEntity();
                int tdX = 0;
                int tdY = 0;
                if(Trail.this.dX != 0){
                    tdX = Trail.this.ranGen.nextInt(Trail.this.dY);
                }
                if(Trail.this.dY != 0){
                    tdY = Trail.this.ranGen.nextInt(Trail.this.dY);
                }
                pSprite.setPosition(pSprite.getX() + tdX, pSprite.getY() + tdY);
                pSprite.setColor(Trail.this.getRandomColor());
            }
        });
    }
    
    public void setColorMode(ColorMode colorMode){
        this.colorMode = colorMode;
    }
    
    public void hide(){
        this.setParticlesSpawnEnabled(false);
        this.reset();
    }
    
    public void show(){
        this.setParticlesSpawnEnabled(true);
    }
}
