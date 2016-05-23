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


    lazy val stageController = new StageWrapper(new StageController())

    stageController.implicitResourceContext


    implicit def implicitResourceContext = this

    //fixed 'virtual' field
    val virtW = 1080*8 //Gdx.graphics.getWidth()
    val virtH = 1794*8 //Gdx.graphics.getHeight()

    lazy val fpsLogger = new FPSLogger()

    lazy val camera = new OrthographicCamera()
    lazy val viewport = new FitViewport(virtW,virtH, camera)

    //lazy val batch = manageDisposableResource (new SpriteBatch())
//    lazy val font = managedResource (new BitmapFont())
//    lazy val texture = manageDisposableResource (new Texture(Gdx.files.internal("data/ball_orange.png")))
//    lazy val sprite = {
//        val s = new Sprite(texture())
//        s.setOPosition(0,0)
//        s
//    }
//
//    object sim {
//
//        val debugRenderer = new Box2DDebugRenderer()
//
//        val phyScale = 1f / 216
//        val world = manageDisposableResource (new World(new Vector2(0, -50), true))
//
//        val body = {
//            val bodyDef = new BodyDef()
//            bodyDef.`type` = BodyDef.BodyType.DynamicBody
//            bodyDef.position.set(sprite.oX, sprite.oY)
//
//            val b = world().createBody(bodyDef)
//
//            fixture(b)
//
//            b
//        }
//
//        private def fixture(body:Body) = {
//            val shape = manageResource(new PolygonShape())
//            shape().setAsBox(sprite.width/2, sprite.height/2)
//
//            val fixtureDef = new FixtureDef()
//            fixtureDef.shape = shape()
//            fixtureDef.density = 1f
//
//            body.createFixture(fixtureDef)
//        }
//
//
//        val bottomEdge = {
//
//            val bodyDef = new BodyDef {
//                `type` = BodyType.StaticBody
//                position.set(0, -1000f)
//            }
//
//            val b = world().createBody(bodyDef)
//
//
//            val s = manageResource(new PolygonShape(){
//                setAsBox(400*8,50)
//            })
//
//            b.createFixture( new FixtureDef{
//                shape = s()
//                density = 1f
//                restitution = 1f
//            })
//
//
//            logger.debug("box")
//
//            b
//
//
//        }
//
//
//
//    }


    def create(): Unit ={
        logger.trace(s"Game.create()")

        stageController.onCreate()
        stageController.onResume()

        //Gdx.graphics.setContinuousRendering(false)
    }


    def resize(w: Int, h: Int): Unit ={
        logger.trace(s"Game.resize(${w}, ${h})")

        stageController.onSurfaceChanged(w,h)

        viewport.update(w, h)
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
    }


    def render(): Unit ={
        //logger.trace(s"Game.render()")

        val deltaInSec = Gdx.graphics.getDeltaTime()

        //logger.debug(s"DELTA: ${deltaInSec}")

        stageController.update(deltaInSec)
        stageController.render(deltaInSec)

        if (Gdx.input.isTouched()) {
            //logger.trace("Input occurred at x=" + Gdx.input.getX() + ", y=" + Gdx.input.getY())
            //val pos = camera.unproject( new Vector3(Gdx.input.getX, Gdx.input.getY, 0), viewport.getScreenX, viewport.getScreenY, viewport.getScreenWidth, viewport.getScreenHeight)
            //sprite.setOriginPosition(pos.x, pos.y)
        }

        this.gcTick()



        val vp = stageController.viewport
        if(vp ne null) {

            stageController.renderDebugInfo()

            Gdx.gl.glLineWidth(5)
            drawDebugAxis(viewport)
        }

//        fpsLogger.log()


    }

    def resume(): Unit ={
        logger.trace(s"Game.resume()")
        stageController.onResume()

    }

}
