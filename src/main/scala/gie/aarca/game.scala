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


    private val hack_SimDelta = 1f /60
    private var wasSuspended:Boolean = true

    lazy val stageController = new StageWrapper(new StageController(){
        def renderDebug: Boolean = false
    })

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


    private var drift:Float = 0

    def render(): Unit ={
        //logger.trace(s"Game.render()")

        val deltaInSec = if (wasSuspended){
            logger.debug("was suspended")
            wasSuspended=false
            0.01f
        } else {
            Gdx.graphics.getDeltaTime()
        }

        //hack, thats because box2d requires constant world update deltas
        drift += deltaInSec - hack_SimDelta
        if(drift<0){
            // need to slow down sim
            val deltaAbs = Math.abs(drift)
            if(deltaAbs / hack_SimDelta <= 1 ) {
                stageController.update(hack_SimDelta)
            } else {
                logger.debug(s"skipping sim, sim delta: ${hack_SimDelta}, delta abs: ${deltaAbs}")
                drift += hack_SimDelta
            }
        } else {
            // need to speed up sim
            val deltaAbs = Math.abs(drift)

            if(deltaAbs / hack_SimDelta <= 1 ) {
                stageController.update(hack_SimDelta)
            } else {
                val simCount = (deltaAbs / hack_SimDelta).toInt
                logger.debug(s"multi sim, extra count: ${simCount}, sim delta: ${hack_SimDelta}, delta abs: ${deltaAbs}")

                stageController.update(hack_SimDelta)

                var i = 0
                while(i<simCount) {
                    stageController.update(hack_SimDelta)
                    i+=1
                    drift -= hack_SimDelta
                }
            }
        }


        //logger.debug(s"DRIFT: ${drift}")

        stageController.render(deltaInSec)


        val vp = stageController.viewport
        if((vp ne null) && stageController.stageController.renderDebug) {

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
