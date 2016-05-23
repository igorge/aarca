package gie.aarca

import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.physics.box2d.Body
import gie.gdx.implicits._

trait GameObjectTrait {
    def sprite: Sprite
    def body: Body

    def x:Float = sprite.oX
    def y:Float = sprite.oY

    def width = sprite.width
    def height = sprite.height

    def setPosition(pX: Float, pY: Float): Unit ={
        body.setTransform(pX, pY, 0)
        sprite.setOPosition(pX, pY)
    }

    def update(): Unit= {
        if (body.isAwake) {
            sprite.setOPosition(body.getPosition)
        }
    }


    protected def init(): Unit ={
        body.setUserData(this)
    }
}
