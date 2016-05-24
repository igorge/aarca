package gie.aarca

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.{Sprite, SpriteBatch}
import com.badlogic.gdx.graphics.{GL20, OrthographicCamera, Texture}
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType
import com.badlogic.gdx.physics.box2d._
import com.badlogic.gdx.utils.viewport.{FitViewport, Viewport}
import gie.gdx.{ResourceContext, manageDisposableResource, manageResource}
import gie.gdx.stage.{StageControllerApiTrait, StageTrait}
import gie.gdx.implicits._
import slogging.{Logger, LoggerHolder, StrictLogging}

import scala.collection.mutable


class ArcanoidStage(val stageController: StageControllerApiTrait)
    extends StageTrait
        with StrictLogging
        with ContactResolverTrait
        with GameWorldWallsTrait
{ asThis =>

    protected val cmdQueue = new mutable.Queue[()=>Unit]()
    protected def applyCmdQueue(): Unit ={
        cmdQueue.foreach(_.apply())
        cmdQueue.clear()
    }

    protected val w = 20  //blocks
    protected val h = (w*1.66f).toInt

    protected implicit def implicitBoxWorld = world()

    private val camera = new OrthographicCamera()
    val viewport = new FitViewport(w,h, camera)
    private val batch = manageDisposableResource(new SpriteBatch())

    val brickTex = manageDisposableResource (new Texture(Gdx.files.internal("data/bricks/brick_pink_small.png")))
    val ballTex = manageDisposableResource (new Texture(Gdx.files.internal("data/ball_orange.png")))


    private val boxDebugRenderer = new Box2DDebugRenderer()

    protected val world = manageDisposableResource (new World(new Vector2(0, 0), true))


    world().setContactListener(contacter)

    protected val bricks = (for(i<-(-9 to 9 by 2)) yield new GameObjectBrick(brickTex(), i, h/2 -1)).to[mutable.Set]
    protected val ball = new GameObjectBall(ballTex(), 0,0)

    buildWorldWalls()

    def update(delta: Float): Unit = {


        world().step(delta, 8, 3)

        applyCmdQueue()

        bricks.foreach{go=>
            go.update()
        }

        ball.update()
    }

    def onSurfaceChanged(width: Int, height: Int): Unit ={
        viewport.update(w, h)
    }

    def onPause(): Unit ={}

    def onSaveState(): Unit ={}

    def render(delta: Float): Unit ={

        val lb = batch()

        Gdx.gl.glClearColor(1, 1, 1, 1)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)


        lb.setProjectionMatrix(camera.combined)
        lb.begin()
        bricks.foreach(_.sprite.draw(lb))
        ball.sprite.draw(lb)
        lb.end()
    }


    def renderDebugInfo(): Unit ={
        Gdx.gl.glLineWidth(5)
        boxDebugRenderer.render( world(), camera.combined)
    }

    def onResume(): Unit ={}

    def onDestroy(): Unit ={}

    def onCreate(): Unit ={}


}
