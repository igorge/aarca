package gie.aarca

import com.badlogic.gdx.graphics.g2d.{Sprite, SpriteBatch}
import com.badlogic.gdx.physics.box2d._
import gie.gdx.implicits._


trait GameObjectTrait extends RenderableTrait {
    def sprite: Sprite
    protected def body: Body
    def world: World = body.getWorld

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

    // update before render
    def render(batch: SpriteBatch): Unit ={
        sprite.draw(batch)
    }

    def destroy(): Unit ={
        world.destroyBody(body)
    }
}
