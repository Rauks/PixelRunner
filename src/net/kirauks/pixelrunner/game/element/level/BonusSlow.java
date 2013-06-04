/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.kirauks.pixelrunner.game.element.level;

import org.andengine.entity.modifier.AlphaModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.shape.Shape;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.color.Color;
import net.kirauks.pixelrunner.game.Player;

/**
 *
 * @author Karl
 */
public class BonusSlow extends LevelElement{
    public static Color DEFAULT_COLOR = Color.YELLOW;
    
    public BonusSlow(int level){
        super(level, BONUS_WIDTH, BONUS_HEIGHT);
    }
    
    @Override
    public Color getColor() {
        return DEFAULT_COLOR;
    }

    @Override
    protected Shape buildShape(float pX, float pY, VertexBufferObjectManager pVertexBufferObjectManager, final Player player) {
        return new Rectangle(pX, pY, this.getWidth(), this.getHeight(), pVertexBufferObjectManager);
    }

    @Override
    protected void playerAction(Player player) {
        player.resetBonus();
        player.changeColor(this.getColor());
        player.setSpeed(0.7f);
        this.getBuildedShape().registerEntityModifier(new AlphaModifier(0.5f, 1f, 0f));
    }
}
