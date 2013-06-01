/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.kirauks.pixelrunner.scene.base.element;

import net.kirauks.pixelrunner.manager.ResourcesManager;
import org.andengine.entity.text.TextOptions;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.align.HorizontalAlign;

/**
 *
 * @author Karl
 */
public class XmAudioListElement extends ListElement{
    private String xmFile;
    
    public XmAudioListElement(String name, String xmFile, VertexBufferObjectManager pVertexBufferObjectManager){
        super(name, pVertexBufferObjectManager);
        this.xmFile = xmFile;
    }
    
    public String getXmFileName(){
        return this.xmFile;
    }
}
