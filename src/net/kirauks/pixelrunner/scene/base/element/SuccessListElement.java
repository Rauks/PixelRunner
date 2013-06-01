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
public class SuccessListElement extends ListElement{
    
    
    public SuccessListElement(String name, String subName, VertexBufferObjectManager pVertexBufferObjectManager){
        super(name, pVertexBufferObjectManager);
        
    }
    
    @Override
    public void disposeElement(){
        super.disposeElement();
        
    }
}
