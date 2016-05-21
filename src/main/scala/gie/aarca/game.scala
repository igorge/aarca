package gie.aarca


import com.badlogic.gdx.graphics.{Color, GL20, OrthographicCamera, Texture}
import com.badlogic.gdx.graphics.g2d.{BitmapFont, Sprite, SpriteBatch}
import com.badlogic.gdx.math.{Vector2, Vector3}
import com.badlogic.gdx.utils.viewport.FitViewport
import com.badlogic.gdx.{ApplicationListener, Gdx}
import com.badlogic.gdx.physics.box2d._
import slogging.StrictLogging
import gie.gdx.{DebugRenderer, ResourceContext, ResourceContextResolver, managedResource}

import scala.concurrent.{ExecutionContext, Future}

class Game()(implicit executor: ExecutionContext) extends ApplicationListener
    with ResourceContext
    with ResourceContextResolver
    with DebugRenderer
    with StrictLogging
{

    implicit def implicitResourceContext = this

    //fixed 'virtual' field
    val virtW = 1080//*8 //Gdx.graphics.getWidth()
    val virtH = 1794//*8 //Gdx.graphics.getHeight()


    lazy val camera = new OrthographicCamera()
    lazy val viewport = new FitViewport(virtW,virtH, camera)

    lazy val batch = managedResource (new SpriteBatch())
//    lazy val font = managedResource (new BitmapFont())
    lazy val texture = managedResource (new Texture(Gdx.files.internal("data/ball_orange.png")))
    lazy val sprite = {
        val s = new Sprite(texture())
        logger.debug(s"${s.getOriginX} :: ${s.getOriginY}")
        s.setOriginCenter()
        logger.debug(s"${s.getOriginX} :: ${s.getOriginY}")
        s.setPosition(0 - s.getWidth /2, 0 -s.getHeight /2)
        //s.setPosition(0 - s.getWidth /2, 0 -s.getHeight /2)
        s
    }

    object sim {
        val phyScale = 1f / 216
        val world = managedResource (new World(new Vector2(0, -98f), true))

        val bodyDef = {
            val bd = new BodyDef()
            bd.`type` = BodyDef.BodyType.DynamicBody
            bd.position.set(sprite.getX, sprite.getY)

            bd
        }



    }


    def create(): Unit ={
        logger.trace(s"Game.create()")
        Gdx.graphics.setContinuousRendering(false)
//        font().setColor(Color.RED)
    }

    def resize(w: Int, h: Int): Unit ={
        logger.trace(s"Game.resize(${w}, ${h})")
        viewport.update(w, h)
    }

    override def dispose(): Unit ={
        logger.trace(s"Game.dispose()")
        super.dispose()
    }

    def pause(): Unit ={
        logger.trace(s"Game.pause()")
    }

    def render(): Unit ={
        //logger.trace(s"Game.render()")

        if (Gdx.input.isTouched()) {
            //logger.trace("Input occurred at x=" + Gdx.input.getX() + ", y=" + Gdx.input.getY())
            val pos = camera.unproject( new Vector3(Gdx.input.getX, Gdx.input.getY, 0), viewport.getScreenX, viewport.getScreenY, viewport.getScreenWidth, viewport.getScreenHeight)
            sprite.setPosition(pos.x - sprite.getWidth /2, pos.y -sprite.getHeight /2)
        }

        Gdx.gl.glClearColor(1, 1, 1, 1)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        batch().setProjectionMatrix(camera.combined)
        batch().begin()
        //font.draw(batch, "Hello World", 200, 200)
        sprite.draw(batch())
        batch().end()

        drawDebugAxis(viewport)

        this.gcTick()

    }

    def resume(): Unit ={
        logger.trace(s"Game.resume()")

    }

}
