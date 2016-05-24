package gie.aarca

import com.badlogic.gdx.graphics._
import com.badlogic.gdx.graphics.g2d.{BitmapFont, Sprite, SpriteBatch}
import com.badlogic.gdx.math.{Vector2, Vector3}
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType
import com.badlogic.gdx.utils.viewport.FitViewport
import com.badlogic.gdx.{ApplicationListener, Gdx}
import com.badlogic.gdx.physics.box2d._
import slogging.StrictLogging
import gie.gdx.{DebugRenderer, ResourceContext, ResourceContextResolver, manageDisposableResource, manageResource}
import gie.gdx.implicits._
import gie.gdx.stage.StageWrapper
import gie.utils.RunOnce

import scala.concurrent.{ExecutionContext, Future}

class Game()(implicit executor: ExecutionContext) extends ApplicationListener
    with ResourceContext
    with ResourceContextResolver
    with DebugRenderer
    with StrictLogging
{


    private var wasSuspended:Boolean = true

    lazy val stageController = new StageWrapper(new StageController())

    stageController.implicitResourceContext

    implicit def implicitResourceContext = this

    lazy val fpsLogger = new FPSLogger()

    def create(): Unit ={
        logger.trace(s"Game.create()")

        stageController.onCreate()
        stageController.onResume()

        //Gdx.graphics.setContinuousRendering(false)
    }


    def resize(w: Int, h: Int): Unit ={
        logger.trace(s"Game.resize(${w}, ${h})")

        stageController.onSurfaceChanged(w,h)

        //viewport.update(w, h)
    }

    override def dispose(): Unit ={
        logger.trace(s"Game.dispose()")

        stageController.onDestroy()

        super.dispose()
    }

    def pause(): Unit ={
        logger.trace(s"Game.pause()")

        stageController.onPause()
        stageController.onSaveState()

        wasSuspended = true
    }


    def render(): Unit ={
        //logger.trace(s"Game.render()")

        val deltaInSec = if (wasSuspended){
            logger.debug("was suspended")
            wasSuspended=false
            0.01f
        } else {
            Gdx.graphics.getDeltaTime()
        }

        stageController.update(deltaInSec)
        stageController.render(deltaInSec)


        val vp = stageController.viewport
        if(vp ne null) {

            stageController.renderDebugInfo()

            Gdx.gl.glLineWidth(5)
            drawDebugAxis(vp)
        }

//        fpsLogger.log()

        this.gcTick()
    }

    def resume(): Unit ={
        logger.trace(s"Game.resume()")
        stageController.onResume()

    }

}
