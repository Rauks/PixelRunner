/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.game.runner.base.element;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.text.Text;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.color.Color;
import org.andengine.util.adt.list.SmartList;
import org.andengine.util.debug.Debug;
import org.game.runner.manager.ResourcesManager;

/**
 *
 * @author Karl
 */
public class ScrollMenuPage extends Rectangle{
    private static final int NB_COLS = 4;
    private static final int NB_ROWS = 3;
    private static final int MAX_ELEMENTS = 12;
    private static final float PADDING_X = 100;
    private static final float PADDING_Y = 80;
    
    private Text title;
    
    private int progress;
    private SmartList<ScrollMenuPageElement> elements;
    
    public ScrollMenuPage(final float pWidth, final float pHeight, final VertexBufferObjectManager pVertexBufferObjectManager){
        super(0, 0, pWidth, pHeight, pVertexBufferObjectManager);
        this.setColor(Color.TRANSPARENT);
        this.elements = new SmartList<ScrollMenuPageElement>();
        this.progress = 0;
    }
    
    public void setTitle(String title){
        if(this.title == null){
            this.title = new Text(this.getWidth()/2, this.getHeight() - 30, ResourcesManager.getInstance().fontPixel_60, title, this.getVertexBufferObjectManager());
            attachChild(this.title);
        }
        else{
            this.title.setText(title);
        }
    }
    public void addElement() throws IndexOutOfBoundsException{
        int index = this.elements.size();
        if(index >= MAX_ELEMENTS){
            Debug.e("Elements limit reached in level choice page.");
        }
        else{
            float x = (index%NB_COLS)*(this.getWidth() - 2*PADDING_X)/3 + PADDING_X;
            float y = (NB_ROWS - 1 - index/NB_COLS)*(this.getHeight() - 2*PADDING_Y)/2 + PADDING_Y - 50;
            this.elements.add(index, new ScrollMenuPageElement(x, y, index + 1, true, this.getVertexBufferObjectManager()));
            this.attachChild(this.elements.get(index));
        }
    }
    public void addElements(int nbElements){
        for (int i = 0; i < nbElements; i++) {
            this.addElement();
        }
    }
    
    public void setProgress(int progress){
        this.progress = progress;
    }
    public int getProgress(){
        return this.progress;
    }
    
    public void refreshLocks(){
        for(int i = 0; i < this.elements.size(); i++){
            if(i <= this.progress){
                this.elements.get(i).setLocked(false);
            }
            else{
                this.elements.get(i).setLocked(true);
            }
        }
    }
    
    public void disposePage(){
        if(this.title != null){
            this.title.detachSelf();
            this.title.dispose();
        }
        for(ScrollMenuPageElement element : this.elements){
            element.detachSelf();
            element.dispose();
        }
        this.elements.clear();
        this.detachSelf();
        this.dispose();
    }
}
