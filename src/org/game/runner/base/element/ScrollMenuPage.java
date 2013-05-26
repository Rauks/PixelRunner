/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.game.runner.base.element;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
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
    public static interface IScrollNavigationListener{
        public void onLeft();
        public void onRight();
    }
    
    private static final int NB_COLS = 4;
    private static final int NB_ROWS = 3;
    private static final int MAX_ELEMENTS = 12;
    private static final float PADDING_X = 100;
    private static final float PADDING_Y = 80;
    private static final float MARGIN_TOP = 50;
    
    private Text title;
    private Sprite left;
    private Sprite right;
    
    private IScrollNavigationListener listener;
    
    private int progress;
    private SmartList<ScrollMenuPageElement> elements;
    
    public ScrollMenuPage(final float pWidth, final float pHeight, final int nbElements,final VertexBufferObjectManager pVertexBufferObjectManager){
        super(0, 0, pWidth, pHeight, pVertexBufferObjectManager);
        this.setColor(Color.TRANSPARENT);
        this.elements = new SmartList<ScrollMenuPageElement>(nbElements);
        this.progress = 0;
        this.left = new Sprite(0, this.getHeight()/2 - MARGIN_TOP, ResourcesManager.getInstance().lvlLeft, this.getVertexBufferObjectManager()){
            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X, float Y){
                if (pSceneTouchEvent.isActionUp()){
                    if(ScrollMenuPage.this.listener != null){
                        ScrollMenuPage.this.listener.onLeft();
                    }
                }
                return false;
            };
        };
        this.left.setScale(4f);
        this.attachChild(this.left);
        this.right = new Sprite(this.getWidth(), this.getHeight()/2 - MARGIN_TOP, ResourcesManager.getInstance().lvlRight, this.getVertexBufferObjectManager()){
            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X, float Y){
                if (pSceneTouchEvent.isActionUp()){
                    if(ScrollMenuPage.this.listener != null){
                        ScrollMenuPage.this.listener.onRight();
                    }
                }
                return false;
            };
        };
        this.right.setScale(4f);
        this.attachChild(this.right);
        this.addElements(nbElements);
    }
    
    public void registerScrollNavigationListener(IScrollNavigationListener listener){
        this.listener = listener;
    }
    public Sprite getNavigationLeft(){
        return this.left;
    }
    public Sprite getNavigationRight(){
        return this.right;
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
    public void setLeftVisible(boolean visible){
        this.left.setVisible(visible);
    }
    public void setRightVisible(boolean visible){
        this.right.setVisible(visible);
    }
    private void addElement() throws IndexOutOfBoundsException{
        int index = this.elements.size();
        if(index >= MAX_ELEMENTS){
            Debug.e("Elements limit reached in level choice page.");
        }
        else{
            
            float x = (index%NB_COLS)*(this.getWidth() - 2*PADDING_X)/(NB_COLS - 1) + PADDING_X;
            float y = (NB_ROWS - 1 - index/NB_COLS)*(this.getHeight() - 2*PADDING_Y)/(NB_ROWS - 1) + PADDING_Y - MARGIN_TOP;
            this.elements.add(index, new ScrollMenuPageElement(x, y, index + 1, true, this.getVertexBufferObjectManager()));
            this.attachChild(this.elements.get(index));
        }
    }
    private void addElements(int nbElements){
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
        this.left.detachSelf();
        this.left.dispose();
        this.right.detachSelf();
        this.right.dispose();
        for(ScrollMenuPageElement element : this.elements){
            element.detachSelf();
            element.dispose();
        }
        this.elements.clear();
        this.detachSelf();
        this.dispose();
    }
}
