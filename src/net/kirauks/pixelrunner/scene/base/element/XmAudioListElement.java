/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.kirauks.pixelrunner.scene.base.element;

import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 *
 * @author Karl
 */
public class XmAudioListElement extends ListElement{
    private String xmFile;
    
    public XmAudioListElement(String name, String xmFile, VertexBufferObjectManager pVertexBufferObjectManager){
        super(name, pVertexBufferObjectManager);
        this.setCullingEnabled(true);
        this.xmFile = xmFile;
    }
    
    public String getXmFileName(){
        return this.xmFile;
    }
}
