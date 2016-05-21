package gie.gdx

import scala.language.implicitConversions
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.math.Vector2

final class SpriteWrapper(val sprite: Sprite) extends AnyVal {

    @inline def setOPosition(x: Float, y: Float): Unit ={
        sprite.setPosition(x-sprite.getOriginX, y-sprite.getOriginY)
    }

    @inline def setOPosition(p: Vector2): Unit ={
        setOPosition(p.x, p.y)
    }

    @inline def x = sprite.getX
    @inline def y = sprite.getY

    @inline def width = sprite.getWidth
    @inline def height = sprite.getHeight

    @inline def oX = x + sprite.getOriginX
    @inline def oY = y + sprite.getOriginY

}



trait SpriteImplicitTrait {
    @inline implicit def implicitSpriteWrapper(sprite: Sprite) = new SpriteWrapper(sprite)
}

