package gie.aarca

import com.badlogic.gdx.{Gdx, Input}
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.{Vector2, Vector3}
import gie.gdx.{ResourceContext, manageDisposableResource, manageResource}

trait BatTrait { this: ArcanoidStage=>

    protected val bat = new GameObjectBat(manageDisposableResource (new Texture(Gdx.files.internal("data/bat_yellow.png"))), 0, -h/2+1)

    addRenderable(bat)

    private val leftVelocity = new Vector2(-10,0)
    private val rightVelocity = new Vector2(10,0)


    protected def processInput(): Unit ={
        if(Gdx.input.isKeyPressed(Input.Keys.A)){
            logger.debug("left")
            bat.body.setLinearVelocity(leftVelocity)
        }

        if(Gdx.input.isKeyPressed(Input.Keys.D)){
            logger.debug("right")
            bat.body.setLinearVelocity(rightVelocity)
        }


        if (Gdx.input.isTouched()) {
            logger.trace("Input occurred at x=" + Gdx.input.getX() + ", y=" + Gdx.input.getY())
            val pos = camera.unproject( new Vector3(Gdx.input.getX, Gdx.input.getY, 0), viewport.getScreenX, viewport.getScreenY, viewport.getScreenWidth, viewport.getScreenHeight)

            bat.body.setTransform(pos.x, bat.y, 0)
            //sprite.setOriginPosition(pos.x, pos.y)
        }

    }


}