/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.kirauks.pixelrunner.scene.base;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.MoveXModifier;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.ITouchArea;
import org.andengine.entity.scene.Scene;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.adt.list.SmartList;
import org.andengine.util.modifier.IModifier;
import org.andengine.util.modifier.IModifier.IModifierListener;
import org.andengine.util.modifier.ease.EaseLinear;
import org.andengine.util.modifier.ease.IEaseFunction;
import net.kirauks.pixelrunner.scene.base.element.IScrollPageElementTouchListener;
import net.kirauks.pixelrunner.scene.base.element.IScrollPageNavigationTouchListener;
import net.kirauks.pixelrunner.scene.base.element.ScrollPage;
import net.kirauks.pixelrunner.scene.base.element.ScrollPageElement;

/**
 *
 * @author Karl
 */
public abstract class BaseScrollMenuScene extends BaseMenuScene implements IOnSceneTouchListener, IScrollPageNavigationTouchListener, IScrollPageElementTouchListener{
    public interface IOnScrollListener {
        public void onMoveToPageStarted(final int oldPageNumber, final int newPageNumber);
        public void onMoveToPageFinished(final int oldPageNumber, final int newPageNumber);
    }
    
    private enum ScrollState {
        IDLE, SLIDING;
    }
    
    private static final float SLIDE_DURATION_DEFAULT = 0.3f;
    private static final float MINIMUM_TOUCH_LENGTH_TO_SLIDE_DEFAULT = 50f;
    private static final float MINIMUM_TOUCH_LENGTH_TO_CHANGE_PAGE_DEFAULT = 100f;
    
    private SmartList<ScrollPage> mPages = new SmartList<ScrollPage>();
    private ScrollState mState;
    private IOnScrollListener mOnScrollScenePageListener;

    private int mPrevPage;
    private int mCurrentPage;
    private float mStartSwipe;

    private float mMinimumTouchLengthToSlide;
    private float mMinimumTouchLengthToChagePage;

    private float lastX;

    private float mPageWidth;
    private float mPageHeight;
    private float mOffset;

    private IEaseFunction mEaseFunction;
    private MoveXModifier mMoveXModifier;
    private IModifierListener<IEntity> mMoveXModifierListener;
    private boolean mEaseFunctionDirty = false;

    public BaseScrollMenuScene() {
        this(0, 0, MINIMUM_TOUCH_LENGTH_TO_SLIDE_DEFAULT, MINIMUM_TOUCH_LENGTH_TO_CHANGE_PAGE_DEFAULT);
    }

    public BaseScrollMenuScene(final float pPageWidth, final float pPageHeight) {
        this(pPageWidth, pPageHeight, MINIMUM_TOUCH_LENGTH_TO_SLIDE_DEFAULT, MINIMUM_TOUCH_LENGTH_TO_CHANGE_PAGE_DEFAULT);
    }

    public BaseScrollMenuScene(final float pPageWidth, final float pPageHeight, float pMinimumTouchLengthToSlide, float pMinimumTouchLengthToChangePage) {
        this.mPageWidth = pPageWidth;
        this.mPageHeight = pPageHeight;

        this.mMinimumTouchLengthToSlide = pMinimumTouchLengthToSlide;
        this.mMinimumTouchLengthToChagePage = pMinimumTouchLengthToChangePage;
        this.mEaseFunction = EaseLinear.getInstance();

        this.mPrevPage = 0;
        this.mCurrentPage = 0;
    }
        
    @Override
    public void createScene(){
        super.createScene();

        this.setOnSceneTouchListener(this);
    }

    public int getPagesCount() {
        return this.mPages.size();
    }
    public float getPageWidth() {
        return this.mPageWidth;
    }
    public float getPageHeight() {
        return this.mPageHeight;
    }
    public void registerScrollScenePageListener(IOnScrollListener pOnScrollScenePageListener) {
        this.mOnScrollScenePageListener = pOnScrollScenePageListener;
    }
    public void unregisterScrollScenePageListener(){
        this.mOnScrollScenePageListener = null;
    }
    public void setEaseFunction(IEaseFunction pEaseFunction) {
        this.mEaseFunction = pEaseFunction;
        this.mEaseFunctionDirty = true;
    }
    public void setPageWidth(float pageWidth) {
        this.mPageWidth = pageWidth;
    }
    public void setPageHeight(float pageHeight) {
        this.mPageHeight = pageHeight;
    }
    public void setOffset(float offset) {
        this.mOffset = offset;
    }
    public void setMinimumLengthToSlide(float pMinimumTouchLengthToSlide) {
        this.mMinimumTouchLengthToSlide = pMinimumTouchLengthToSlide;
    }
    public void setMinimumLengthToChangePage(float pMinimumTouchLengthToChangePage) {
        this.mMinimumTouchLengthToChagePage = pMinimumTouchLengthToChangePage;
    }
    public boolean isFirstPage(ScrollPage pPage) {
        if (this.mPages.getFirst().equals(pPage)) {
            return true;
        }
        return false;
    }
    public boolean isLastPage(ScrollPage pPage) {
        if (this.mPages.getLast().equals(pPage)) {
            return true;
        }
        return false;
    }
    public ScrollPage getCurrentPage() {
        return this.mPages.get(this.mCurrentPage);
    }
    public int getCurrentPageNumber() {
        return this.mCurrentPage;
    }
    public ScrollPage getPreviousPage() {
        return this.mPages.get(this.mPrevPage);
    }
    public int getPreviousPageNumber() {
        return this.mPrevPage;
    }

    public void updatePages() {
        int i = 0;
        for (ScrollPage page : this.mPages) {
            if(this.isFirstPage(page)){
                page.setLeftVisible(false);
            }
            else{
                page.setLeftVisible(true);
            }
            if(this.isLastPage(page)){
                page.setRightVisible(false);
            }
            else{
                page.setRightVisible(true);
            }
            page.setPosition(i * (this.mPageWidth - this.mOffset) + this.mPageWidth/2, this.mPageHeight/2);
            i++;
        }
    }
    
    public void addPage(final ScrollPage pPage) {
        this.registerPageTouchAreas(pPage);
        this.mPages.add(pPage);
        this.attachChild(pPage);

        this.updatePages();
    }
    
    public void addPages(final ScrollPage... pPages){
        for(ScrollPage pPage : pPages){
            this.addPage(pPage);
        }
    }
    
    public void addPage(final ScrollPage pPage, final int pPageNumber) {
        this.registerPageTouchAreas(pPage);
        this.mPages.add(pPageNumber, pPage);
        this.attachChild(pPage);

        this.updatePages();
    }
    
    public void removePage(final ScrollPage pPage) {
        this.unregisterPageTouchAreas(pPage);
        this.detachChild(pPage);
        this.mPages.remove(pPage);

        this.updatePages();

        this.mPrevPage = this.mCurrentPage;
        this.mCurrentPage = Math.min(this.mCurrentPage, mPages.size() - 1);
        this.moveToPage(this.mCurrentPage);

    }
    
    void removePageWithNumber(final ScrollPage pPage, final int pPageNumber) {
        if (pPageNumber < this.mPages.size()) {
            this.removePage(this.mPages.get(pPageNumber));
        }
    }
    
    private void registerPageTouchAreas(final ScrollPage pPage){
        pPage.registerPageNavigationTouchListener(this);
        this.registerTouchArea(pPage.getNavigationLeftTouchArea());
        this.registerTouchArea(pPage.getNavigationRightTouchArea());
        pPage.registerPageElementTouchListener(this);
        for(ITouchArea element : pPage.getElementsTouchAreas()){
            this.registerTouchArea(element);
        }
    }
    
    private void unregisterPageTouchAreas(final ScrollPage pPage){
        pPage.registerPageNavigationTouchListener(null);
        this.unregisterTouchArea(pPage.getNavigationLeftTouchArea());
        this.unregisterTouchArea(pPage.getNavigationRightTouchArea());
        for(ITouchArea element : pPage.getElementsTouchAreas()){
            this.unregisterTouchArea(element);
        }
    }
    
    public void moveToPage(final int pageNumber) {
        if (pageNumber >= 0 && pageNumber < this.mPages.size()) {
            this.mPrevPage = this.mCurrentPage;
            this.mCurrentPage = pageNumber;

            final float toX = positionForPageWithNumber(pageNumber);

            if (this.mEaseFunctionDirty) {
                if (this.mMoveXModifier != null) {
                    if (this.mMoveXModifierListener != null) {
                        this.mMoveXModifier.removeModifierListener(this.mMoveXModifierListener);
                    }
                    this.unregisterEntityModifier(mMoveXModifier);
                    this.mMoveXModifierListener = null;
                    this.mMoveXModifier = null;
                }
                this.mEaseFunctionDirty = false;
            }

            if (this.mMoveXModifier == null) {
                this.mMoveXModifier = new MoveXModifier(SLIDE_DURATION_DEFAULT, this.getX(), toX, this.mEaseFunction);
                this.mMoveXModifier.setAutoUnregisterWhenFinished(false);
                this.registerEntityModifier(this.mMoveXModifier);
            } else {
                this.mMoveXModifier.reset(SLIDE_DURATION_DEFAULT, this.getX(), toX);
            }

            if (mOnScrollScenePageListener != null) {
                if (this.mMoveXModifierListener == null) {
                    this.mMoveXModifierListener = new IModifierListener<IEntity>() {
                        @Override
                        public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {
                            BaseScrollMenuScene.this.mOnScrollScenePageListener.onMoveToPageStarted(BaseScrollMenuScene.this.mPrevPage, BaseScrollMenuScene.this.mCurrentPage);
                        }

                        @Override
                        public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
                            BaseScrollMenuScene.this.mOnScrollScenePageListener.onMoveToPageFinished(BaseScrollMenuScene.this.mPrevPage, BaseScrollMenuScene.this.mCurrentPage);
                        }
                    };
                    this.mMoveXModifier.addModifierListener(this.mMoveXModifierListener);
                }
            }
        }
    }
    
    public void selectPage(int pageNumber) {
        if (pageNumber >= this.mPages.size()) {
            throw new IndexOutOfBoundsException("selectPage: " + pageNumber + " - wrong page number, out of bounds.");
        }

        this.setX(positionForPageWithNumber(pageNumber));
        this.mPrevPage = this.mCurrentPage;
        this.mCurrentPage = pageNumber;
    }
    
    public float positionForPageWithNumber(int pageNumber) {
        return pageNumber * (this.mPageWidth - this.mOffset) * -1f;
    }
    
    public int pageNumberForPosition(float pPosition) {
        float pageFloat = -pPosition / (this.mPageWidth - this.mOffset);
        int pageNumber = (int) Math.ceil(pageFloat);

        if ((float) pageNumber - pageFloat >= 0.5f) {
            pageNumber--;
        }

        pageNumber = Math.max(0, pageNumber);
        pageNumber = Math.min(this.mPages.size() - 1, pageNumber);

        return pageNumber;
    }
    
    public void moveToNextPage() {
        final int pageNo = this.mPages.size();

        if (this.mCurrentPage + 1 < pageNo) {
            this.moveToPage(this.mCurrentPage + 1);
        }
    }
    
    public void moveToPreviousPage() {
        if (this.mCurrentPage > 0) {
            this.moveToPage(this.mCurrentPage - 1);
        }
    }
    
    public void clearPages() {
        for (int i = this.mPages.size() - 1; i >= 0; i--) {
            final ScrollPage page = this.mPages.remove(i);
            this.detachChild(page);
            //this.unregisterTouchArea(page);
            this.unregisterPageTouchAreas(page);
        }
    }
    
    @Override
    public void disposeScene() {
        super.disposeScene();
        for(ScrollPage page : this.mPages){
            page.disposePage();
        }
    }
    
    //IOnSceneTouchListener
    @Override
    public boolean onSceneTouchEvent(final Scene pScene, final TouchEvent pSceneTouchEvent) {
        final float touchX = pSceneTouchEvent.getX();
        switch(pSceneTouchEvent.getAction()) {
            case TouchEvent.ACTION_DOWN:
                this.mStartSwipe = touchX;
                this.lastX = this.getX();
                this.mState = ScrollState.IDLE;
                return true;
            case TouchEvent.ACTION_MOVE:
                if (this.mState != ScrollState.SLIDING && Math.abs(touchX - mStartSwipe) >= this.mMinimumTouchLengthToSlide) {
                    this.mState = ScrollState.SLIDING;
                    // Avoid jerk after state change.
                    this.mStartSwipe = touchX;
                    return true;
                } else if (this.mState == ScrollState.SLIDING) {
                    float offsetX = touchX - mStartSwipe;
                    this.setX(lastX + offsetX);
                    return true;
                } else {
                    return false;
                }
            case TouchEvent.ACTION_UP:
            case TouchEvent.ACTION_CANCEL:
                if (this.mState == ScrollState.SLIDING) {
                    int selectedPage = this.mCurrentPage;
                    float delta = touchX - mStartSwipe;
                    if (Math.abs(delta) >= this.mMinimumTouchLengthToChagePage) {
                        if (delta < 0.f && selectedPage < this.mPages.size() - 1) {
                            selectedPage++;
                        }
                        else if (delta > 0.f && selectedPage > 0) {
                            selectedPage--;
                        }
                    }
                    this.moveToPage(selectedPage);
                }
                return true;
            default:
                return false;
        }
    }
    
    //IPageNavigationTouchListener
    @Override
    public void onLeft(){
        this.moveToPage(BaseScrollMenuScene.this.mCurrentPage - 1);
    }
    @Override
    public void onRight(){
        this.moveToPage(BaseScrollMenuScene.this.mCurrentPage + 1);
    }
    
    //IPageElementTouchListener
    @Override
    public void onElementActionUp(ScrollPageElement element) {
        if(this.mState != ScrollState.SLIDING){
            this.onElementAction(element);
        }
    }
    public abstract void onElementAction(ScrollPageElement element);
}
