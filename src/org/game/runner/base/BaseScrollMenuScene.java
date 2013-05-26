/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.game.runner.base;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.MoveXModifier;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.adt.list.SmartList;
import org.andengine.util.modifier.IModifier;
import org.andengine.util.modifier.ease.EaseLinear;
import org.andengine.util.modifier.ease.IEaseFunction;
import org.game.runner.base.element.ScrollMenuPage;

/**
 *
 * @author Karl
 */
public abstract class BaseScrollMenuScene extends BaseMenuScene implements IOnSceneTouchListener{
    public static interface IOnScrollScenePageListener {
        public void onMoveToPageStarted(final int oldPageNumber, final int newPageNumber);
        public void onMoveToPageFinished(final int oldPageNumber, final int newPageNumber);
    }
    
    private enum ScrollState {
        IDLE, SLIDING;
    }
    
    private static final float SLIDE_DURATION_DEFAULT = 0.3f;
    private static final float MINIMUM_TOUCH_LENGTH_TO_SLIDE_DEFAULT = 30f;
    private static final float MINIMUM_TOUCH_LENGTH_TO_CHANGE_PAGE_DEFAULT = 100f;
    
    private SmartList<ScrollMenuPage> mPages = new SmartList<ScrollMenuPage>();
    private ScrollState mState;
    private IOnScrollScenePageListener mOnScrollScenePageListener;

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
    private IModifier.IModifierListener<IEntity> mMoveXModifierListener;
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
    public void registerScrollScenePageListener(IOnScrollScenePageListener pOnScrollScenePageListener) {
        this.mOnScrollScenePageListener = pOnScrollScenePageListener;
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
    public boolean isFirstPage(ScrollMenuPage pPage) {
        if (this.mPages.getFirst().equals(pPage)) {
            return true;
        }
        return false;
    }
    public boolean isLastPage(ScrollMenuPage pPage) {
        if (this.mPages.getLast().equals(pPage)) {
            return true;
        }
        return false;
    }
    public ScrollMenuPage getCurrentPage() {
        return this.mPages.get(this.mCurrentPage);
    }
    public int getCurrentPageNumber() {
        return this.mCurrentPage;
    }
    public ScrollMenuPage getPreviousPage() {
        return this.mPages.get(this.mPrevPage);
    }
    public int getPreviousPageNumber() {
        return this.mPrevPage;
    }

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
    
    public void updatePages() {
        int i = 0;
        for (ScrollMenuPage page : this.mPages) {
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
    
    public void addPage(final ScrollMenuPage pPage) {
        pPage.registerScrollNavigationListener(new ScrollMenuPage.IScrollNavigationListener() {
            @Override
            public void onLeft() {
                BaseScrollMenuScene.this.moveToPage(BaseScrollMenuScene.this.mCurrentPage - 1);
            }
            @Override
            public void onRight() {
                BaseScrollMenuScene.this.moveToPage(BaseScrollMenuScene.this.mCurrentPage + 1);
            }
        });
        this.registerTouchArea(pPage.getNavigationLeft());
        this.registerTouchArea(pPage.getNavigationRight());
        this.mPages.add(pPage);
        this.attachChild(pPage);

        this.updatePages();
    }
    
    public void addPages(final ScrollMenuPage... pPages){
        for(ScrollMenuPage pPage : pPages){
            this.addPage(pPage);
        }
    }
    
    public void addPage(final ScrollMenuPage pPage, final int pPageNumber) {
        pPage.registerScrollNavigationListener(new ScrollMenuPage.IScrollNavigationListener() {
            @Override
            public void onLeft() {
                BaseScrollMenuScene.this.moveToPage(BaseScrollMenuScene.this.mCurrentPage - 1);
            }
            @Override
            public void onRight() {
                BaseScrollMenuScene.this.moveToPage(BaseScrollMenuScene.this.mCurrentPage + 1);
            }
        });
        this.registerTouchArea(pPage.getNavigationLeft());
        this.registerTouchArea(pPage.getNavigationRight());
        this.mPages.add(pPageNumber, pPage);
        this.attachChild(pPage);

        this.updatePages();
    }
    
    public void removePage(final ScrollMenuPage pPage) {
        this.unregisterTouchArea(pPage);
        this.detachChild(pPage);
        this.mPages.remove(pPage);

        this.updatePages();

        this.mPrevPage = this.mCurrentPage;
        this.mCurrentPage = Math.min(this.mCurrentPage, mPages.size() - 1);
        this.moveToPage(this.mCurrentPage);

    }
    
    void removePageWithNumber(final ScrollMenuPage pPage, final int pPageNumber) {
        if (pPageNumber < this.mPages.size()) {
            this.removePage(this.mPages.get(pPageNumber));
        }
    }
    
    private void moveToPage(final int pageNumber) {
        if (pageNumber >= this.mPages.size()) {
            throw new IndexOutOfBoundsException("moveToPage: " + pageNumber + " - wrong page number, out of bounds.");
        }
        
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
                this.mMoveXModifierListener = new IModifier.IModifierListener<IEntity>() {
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
            final ScrollMenuPage page = this.mPages.remove(i);
            this.detachChild(page);
            this.unregisterTouchArea(page);
        }
    }
    
    @Override
    public void disposeScene() {
        super.disposeScene();
        for(ScrollMenuPage page : this.mPages){
            page.detachSelf();
            page.dispose();
        }
    }
}
