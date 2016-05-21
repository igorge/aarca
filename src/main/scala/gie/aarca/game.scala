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

import scala.concurrent.{ExecutionContext, Future}

class Game()(implicit executor: ExecutionContext) extends ApplicationListener
    with ResourceContext
    with ResourceContextResolver
    with DebugRenderer
    with StrictLogging
{

    implicit def implicitResourceContext = this

    //fixed 'virtual' field
    val virtW = 1080*8 //Gdx.graphics.getWidth()
    val virtH = 1794*8 //Gdx.graphics.getHeight()

    lazy val fpsLogger = new FPSLogger()

    lazy val camera = new OrthographicCamera()
    lazy val viewport = new FitViewport(virtW,virtH, camera)

    lazy val batch = manageDisposableResource (new SpriteBatch())
//    lazy val font = managedResource (new BitmapFont())
    lazy val texture = manageDisposableResource (new Texture(Gdx.files.internal("data/ball_orange.png")))
    lazy val sprite = {
        val s = new Sprite(texture())
        s.setOPosition(0,0)
        s
    }

    object sim {

        val debugRenderer = new Box2DDebugRenderer()

        val phyScale = 1f / 216
        val world = manageDisposableResource (new World(new Vector2(0, -50), true))

        val body = {
            val bodyDef = new BodyDef()
            bodyDef.`type` = BodyDef.BodyType.DynamicBody
            bodyDef.position.set(sprite.oX, sprite.oY)

            val b = world().createBody(bodyDef)

            fixture(b)

            b
        }

        private def fixture(body:Body) = {
            val shape = manageResource(new PolygonShape())
            shape().setAsBox(sprite.width/2, sprite.height/2)

            val fixtureDef = new FixtureDef()
            fixtureDef.shape = shape()
            fixtureDef.density = 1f

            body.createFixture(fixtureDef)
        }


        val bottomEdge = {

            val bodyDef = new BodyDef {
                `type` = BodyType.StaticBody
                position.set(0, -1000f)
            }

            val b = world().createBody(bodyDef)


            val s = manageResource(new PolygonShape(){
                setAsBox(400*8,50)
            })

            b.createFixture( new FixtureDef{
                shape = s()
                density = 1f
                restitution = 1f
            })


            logger.debug("box")

            b


        }



    }


    def create(): Unit ={
        logger.trace(s"Game.create()")
        //Gdx.graphics.setContinuousRendering(false)
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
            //val pos = camera.unproject( new Vector3(Gdx.input.getX, Gdx.input.getY, 0), viewport.getScreenX, viewport.getScreenY, viewport.getScreenWidth, viewport.getScreenHeight)
            //sprite.setOriginPosition(pos.x, pos.y)
        }

        sim.world().step(Gdx.graphics.getDeltaTime(), 8, 3)

        val pos = sim.body.getPosition()


        //logger.debug(s"${sim.bottomEdge.getPosition}")

        sprite.setOPosition(pos)

        Gdx.gl.glClearColor(1, 1, 1, 1)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        batch().setProjectionMatrix(camera.combined)
        batch().begin()
        //font.draw(batch, "Hello World", 200, 200)
        sprite.draw(batch())
        batch().end()

        Gdx.gl.glLineWidth(5)
        sim.debugRenderer.render( sim.world(), camera.combined)

        drawDebugAxis(viewport)

        this.gcTick()


        fpsLogger.log()


    }

    def resume(): Unit ={
        logger.trace(s"Game.resume()")

    }

}
