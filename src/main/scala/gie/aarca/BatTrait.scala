package gie.aarca

import com.badlogic.gdx.{Gdx, Input}
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.Vector2
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

    }


}