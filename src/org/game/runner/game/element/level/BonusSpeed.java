/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.game.runner.game.element.level;

import org.andengine.entity.modifier.AlphaModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.shape.Shape;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.color.Color;
import org.game.runner.game.player.Player;

/**
 *
 * @author Karl
 */
public class BonusSpeed extends LevelElement{
    public BonusSpeed(int level){
        super(level, BONUS_WIDTH, BONUS_HEIGHT);
    }
    
    @Override
    public Color getColor() {
        return Color.PINK;
    }

    @Override
    protected Shape buildShape(float pX, float pY, VertexBufferObjectManager pVertexBufferObjectManager, final Player player) {
        return new Rectangle(pX, pY, this.getWidth(), this.getHeigth(), pVertexBufferObjectManager);
    }

    @Override
    protected void playerAction(Player player) {
        player.resetBonus();
        player.setColor(this.getColor());
        player.setSpeed(1.4f);
        this.getBuildedShape().registerEntityModifier(new AlphaModifier(0.5f, 1f, 0f));
    }
}
