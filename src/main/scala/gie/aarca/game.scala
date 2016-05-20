package gie.aarca


import com.badlogic.gdx.graphics.{Color, GL20, OrthographicCamera, Texture}
import com.badlogic.gdx.graphics.g2d.{BitmapFont, Sprite, SpriteBatch}
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.{ApplicationListener, Gdx}
import slogging.StrictLogging
import gie.gdx.{ResourceContext, managedResource}

import scala.concurrent.{ExecutionContext, Future}

class Game()(implicit executor: ExecutionContext) extends ApplicationListener with ResourceContext with StrictLogging {

    implicit def context = this

    //fixed 'virtual' field
    val w = 1080*8 //Gdx.graphics.getWidth()
    val h = 1794*8 //Gdx.graphics.getHeight()

    lazy val camera = new OrthographicCamera(w, h)

    lazy val batch = managedResource (new SpriteBatch())
    lazy val font = managedResource (new BitmapFont())
    lazy val texture = managedResource (new Texture(Gdx.files.internal("data/ball_orange.png")))
    lazy val sprite = new Sprite(texture())


    def create(): Unit ={
        logger.trace(s"Game.create()")
        font().setColor(Color.RED)

    }

    def resize(i: Int, i1: Int): Unit ={
        logger.trace(s"Game.resize(${i}, ${i1})")
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
            val pos = camera.unproject( new Vector3(Gdx.input.getX, Gdx.input.getY, 0))
            sprite.setPosition(pos.x - sprite.getRegionHeight/2, pos.y -sprite.getRegionWidth/2)
        }

        Gdx.gl.glClearColor(1, 1, 1, 1)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        batch().setProjectionMatrix(camera.combined)
        batch().begin()
        //font.draw(batch, "Hello World", 200, 200)
        sprite.draw(batch())
        batch().end()

        this.gcTick()

    }

    def resume(): Unit ={
        logger.trace(s"Game.resume()")

    }

}
