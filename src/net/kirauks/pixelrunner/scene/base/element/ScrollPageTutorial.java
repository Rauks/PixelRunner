/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.kirauks.pixelrunner.scene.base.element;

import org.andengine.opengl.vbo.VertexBufferObjectManager;
import net.kirauks.pixelrunner.game.descriptor.utils.World;
import net.kirauks.pixelrunner.manager.ResourcesManager;

/**
 *
 * @author Karl
 */
public class ScrollPageTutorial extends ScrollPage{
    public ScrollPageTutorial(final float pWidth, final float pHeight, World world, final VertexBufferObjectManager pVertexBufferObjectManager){
        super(pWidth, pHeight, world, 0, pVertexBufferObjectManager);
        this.addElement(new ScrollPageElement(0, 0, 1, false, true, ResourcesManager.getInstance().lvlJump, pVertexBufferObjectManager));
        this.addElement(new ScrollPageElement(0, 0, 2, false, true, ResourcesManager.getInstance().lvlRoll, pVertexBufferObjectManager));
        this.addElement(new ScrollPageElement(0, 0, 3, false, true, ResourcesManager.getInstance().lvlDoubleJump, pVertexBufferObjectManager));
        this.addElement(new ScrollPageElement(0, 0, 4, false, true, ResourcesManager.getInstance().lvlPlatform, pVertexBufferObjectManager));
        this.setProgress(4);
    }
    
    @Override
    protected float getElementX(int index){
        return super.getElementX(index + 4);
    }
    @Override
    protected float getElementY(int index){
        return super.getElementY(index + 4);
    }
}
