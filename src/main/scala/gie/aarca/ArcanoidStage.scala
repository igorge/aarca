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
        with RenderableQueueTrait
        with GameWorldWorldTrait
        with ContactResolverTrait
        with GameWorldWallsTrait
        with BatTrait
        with LevelBuilderTrait
        /*trait order does matter*/
{ asThis =>

    protected val camera = new OrthographicCamera()
    val viewport = new FitViewport(w,h, camera)

    protected val batch = manageDisposableResource(new SpriteBatch())

    val brickTex = manageDisposableResource (new Texture(Gdx.files.internal("data/bricks/brick_pink_small.png")))
    val brickTexBroken = manageDisposableResource (new Texture(Gdx.files.internal("data/bricks/brick_pink_small.png")))

    val brickGreenTex = manageDisposableResource (new Texture(Gdx.files.internal("data/bricks/brick_green_small.png")))
    val brickGreenTexBroken = manageDisposableResource (new Texture(Gdx.files.internal("data/bricks/brick_green_small_cracked.png")))

    private val boxDebugRenderer = new Box2DDebugRenderer()

    //(for(i<-(-9 to 9 by 2)) yield new GameObjectBrick(brickTex(), i, h/2 -1)).foreach(addRenderable _)

    loadLevel("01.txt").foreach(addRenderable _)

    protected val ball = new GameObjectBall(manageDisposableResource (new Texture(Gdx.files.internal("data/ball_orange.png"))), 0,0)

        addRenderable(ball)


    def update(delta: Float): Unit = {

        implicitly[World].step(delta, 8, 3)
        bat.updateWorld() //TODO: generalize
        applyAfterWorldCmdQueue()
    }

    def onSurfaceChanged(width: Int, height: Int): Unit ={
        viewport.update(width, height)
    }

    def onPause(): Unit ={
        Gdx.input.setInputProcessor(null)
    }

    def onSaveState(): Unit ={}

    def render(delta: Float): Unit ={

        val lb = batch()

        Gdx.gl.glClearColor(1, 1, 1, 1)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        lb.setProjectionMatrix(camera.combined)
        lb.begin()
        renderRenderable(lb)
        lb.end()
    }


    def renderDebugInfo(): Unit ={
        Gdx.gl.glLineWidth(5)
        boxDebugRenderer.render( world(), camera.combined)
    }

    def onResume(): Unit ={
        Gdx.input.setInputProcessor(inputProcessor)
    }

    def onDestroy(): Unit ={}

    def onCreate(): Unit ={}


}
