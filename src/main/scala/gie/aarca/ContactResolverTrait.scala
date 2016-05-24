package gie.aarca

import com.badlogic.gdx.physics.box2d.{Contact, ContactImpulse, ContactListener, Manifold}
import slogging.{Logger, LoggerHolder}

import scala.annotation.tailrec


trait ContactResolverTrait { asThis: ArcanoidStage =>


    def handleCollision(ball: GameObjectBall, brick: GameObjectBrick): Unit ={
        logger.debug("HIT")

        asThis.cmdQueue += (()=>{
            val isRemoved=bricks.remove(brick)
            assert (isRemoved)

            world().destroyBody(brick.body)

        })
    }



    protected object contacter extends ContactListener {
        val stage = asThis

        protected def logger: Logger = asThis.logger

        def preSolve(contact: Contact, oldManifold: Manifold): Unit = {
            val a = contact.getFixtureA.getBody.getUserData()
            val b = contact.getFixtureB.getBody.getUserData()

            //logger.debug(s"contact preSolve: ${a}  <=>  ${b}")
        }


        def postSolve(contact: Contact, impulse: ContactImpulse): Unit = {

            val a = contact.getFixtureA.getBody.getUserData()
            val b = contact.getFixtureB.getBody.getUserData()

            //logger.debug(s"contact postSolve: ${a}  <=>  ${b}")
        }

        def endContact(contact: Contact): Unit = {
            val a = contact.getFixtureA.getBody.getUserData()
            val b = contact.getFixtureB.getBody.getUserData()

            (a, b) match {

                case (brick: GameObjectBrick, ball: GameObjectBall) =>
                    stage.handleCollision(ball, brick)
                case (ball: GameObjectBall, brick: GameObjectBrick) =>
                    stage.handleCollision(ball, brick)

                case (_: GameObjectWall, _: GameObjectBall) =>
                case (_: GameObjectBall, _: GameObjectWall) =>

                case _ =>
                    logger.debug(s"unknown type contact end: ${a}  <=>  ${b}")
            }

            // logger.debug(s"contact end: ${a}  <=>  ${b}")
        }

        def beginContact(contact: Contact): Unit = {
            val a = contact.getFixtureA.getBody.getUserData()
            val b = contact.getFixtureB.getBody.getUserData()

            //logger.debug(s"contact begin: ${a}  <=>  ${b}")
        }
    }


}
