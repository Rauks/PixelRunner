/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.kirauks.pixelrunner.scene.base.element;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.ITouchArea;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.color.Color;
import org.andengine.util.adt.list.SmartList;
import org.andengine.util.debug.Debug;
import net.kirauks.pixelrunner.game.descriptor.utils.World;
import net.kirauks.pixelrunner.manager.ResourcesManager;

/**
 *
 * @author Karl
 */
public class ScrollPage extends Rectangle implements IScrollPageElementTouchListener{
    protected static final int NB_COLS = 4;
    protected static final int NB_ROWS = 3;
    protected static final int MAX_ELEMENTS = 12;
    protected static final float PADDING_X = 130;
    protected static final float PADDING_Y = 80;
    protected static final float MARGIN_TOP = 40;
    protected static final float TITLE_MARGIN_TOP = 20;
    
    private Text title;
    private Sprite left;
    private Sprite right;
    
    private IScrollPageNavigationTouchListener navigationListener;
    private IScrollPageElementTouchListener elementListener;
    
    private World world;
    private int progress;
    private SmartList<ScrollPageElement> elements;
    
    public ScrollPage(final float pWidth, final float pHeight, World world, final int nbElements, final VertexBufferObjectManager pVertexBufferObjectManager){
        super(0, 0, pWidth, pHeight, pVertexBufferObjectManager);
        this.setColor(Color.TRANSPARENT);
        this.setCullingEnabled(true);
        this.elements = new SmartList<ScrollPageElement>(MAX_ELEMENTS);
        this.world = world;
        this.progress = 1;
        this.left = new Sprite(0, this.getHeight()/2 - MARGIN_TOP, ResourcesManager.getInstance().lvlLeft, this.getVertexBufferObjectManager()){
            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X, float Y){
                if (pSceneTouchEvent.isActionUp() && this.isVisible()){
                    if(ScrollPage.this.navigationListener != null){
                        ScrollPage.this.navigationListener.onLeft();
                    }
                }
                return false;
            };
        };
        this.left.setScale(6f);
        this.attachChild(this.left);
        this.right = new Sprite(this.getWidth(), this.getHeight()/2 - MARGIN_TOP, ResourcesManager.getInstance().lvlRight, this.getVertexBufferObjectManager()){
            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X, float Y){
                if (pSceneTouchEvent.isActionUp() && this.isVisible()){
                    if(ScrollPage.this.navigationListener != null){
                        ScrollPage.this.navigationListener.onRight();
                    }
                }
                return false;
            };
        };
        this.right.setScale(6f);
        this.attachChild(this.right);
        this.addElements(nbElements);
        this.refreshLocks();
    }
    
    public World getWorld(){
        return this.world;
    }
    
    public void registerPageNavigationTouchListener(IScrollPageNavigationTouchListener navigationListener){
        this.navigationListener = navigationListener;
    }
    public void unregisterPageNavigationTouchListener(){
        this.navigationListener = null;
    }
    public void registerPageElementTouchListener(IScrollPageElementTouchListener elementListener){
        this.elementListener = elementListener;
    }
    public void unregisterPageElementTouchListener(){
        this.elementListener = null;
    }
    @Override
    public void onElementActionUp(ScrollPageElement element) {
        if(this.elementListener != null){
            this.elementListener.onElementActionUp(element);
        }
    }
    public ITouchArea getNavigationLeftTouchArea(){
        return this.left;
    }
    public ITouchArea getNavigationRightTouchArea(){
        return this.right;
    }
    
    public void setTitle(String title){
        if(this.title == null){
            this.title = new Text(this.getWidth()/2, this.getHeight() - TITLE_MARGIN_TOP, ResourcesManager.getInstance().fontPixel_60, title, this.getVertexBufferObjectManager());
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
    protected void addElement(ScrollPageElement element){
        int index = this.elements.size();
        if(index >= MAX_ELEMENTS){
            Debug.e("Elements limit reached in level choice page.");
        }
        else{
            element.setPosition(this.getElementX(index), this.getElementY(index));
            element.registerPageElementTouchListener(this);
            this.elements.add(index, element);
            this.attachChild(this.elements.get(index));
        }
    }
    protected float getElementX(int index){
        return (index%NB_COLS)*(this.getWidth() - 2*PADDING_X)/(NB_COLS - 1) + PADDING_X;
    }
    protected float getElementY(int index){
        return (NB_ROWS - 1 - index/NB_COLS)*(this.getHeight() - 2*PADDING_Y)/(NB_ROWS - 1) + PADDING_Y - MARGIN_TOP;
    }
    private void addElements(int nbElements){
        for (int i = 0; i < nbElements; i++) {
            this.addElement(new ScrollPageElement(0, 0, i + 1, true, this.getVertexBufferObjectManager()));
        }
        this.refreshLocks();
    }
    
    public SmartList<ITouchArea> getElementsTouchAreas(){
        SmartList<ITouchArea> areas = new SmartList<ITouchArea>();
        for(ScrollPageElement element : this.elements){
            areas.add(element);
        }
        return areas;
    }
    
    public void setProgress(int progress){
        this.progress = progress;
        this.refreshLocks();
    }
    public int getProgress(){
        return this.progress;
    }
    
    private void refreshLocks(){
        for(int i = 0; i < this.elements.size(); i++){
            if(i + 1 <= this.progress){
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
        for(ScrollPageElement element : this.elements){
            element.disposeElement();
        }
        this.elements.clear();
        this.detachSelf();
        this.dispose();
    }
}
