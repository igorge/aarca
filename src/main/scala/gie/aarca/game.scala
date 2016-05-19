package gie.aarca


import com.badlogic.gdx.graphics.{Color, GL20, Texture}
import com.badlogic.gdx.graphics.g2d.{BitmapFont, Sprite, SpriteBatch}
import com.badlogic.gdx.{ApplicationListener, Gdx}
import slogging.StrictLogging

import scala.concurrent.{ExecutionContext, Future}

class Game()(implicit executor: ExecutionContext) extends ApplicationListener with StrictLogging {

    lazy val batch = new SpriteBatch()
    lazy val font = new BitmapFont()
    lazy val texture = new Texture(Gdx.files.internal("data/ball_orange.png"))
    lazy val sprite = new Sprite(texture)


    def create(): Unit ={
        logger.trace(s"Game.create()")
        font.setColor(Color.RED)

    }

    def resize(i: Int, i1: Int): Unit ={
        logger.trace(s"Game.resize(${i}, ${i1})")
    }

    def dispose(): Unit ={
        logger.trace(s"Game.dispose()")
    }

    def pause(): Unit ={
        logger.trace(s"Game.pause()")
    }

    def render(): Unit ={
        //logger.trace(s"Game.render()")

        if (Gdx.input.isTouched()) {
            logger.trace("Input occurred at x=" + Gdx.input.getX() + ", y=" + Gdx.input.getY());
        }

        Gdx.gl.glClearColor(1, 1, 1, 1)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        batch.begin()
        //font.draw(batch, "Hello World", 200, 200)
        sprite.draw(batch)
        batch.end()

    }

    def resume(): Unit ={
        logger.trace(s"Game.resume()")

    }

}
