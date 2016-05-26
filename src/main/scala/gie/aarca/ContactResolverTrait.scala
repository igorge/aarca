package gie.aarca

import com.badlogic.gdx.physics.box2d._
import slogging.{Logger, LoggerHolder}

import scala.annotation.tailrec


trait ContactResolverTrait { asThis: ArcanoidStage =>

    implicitly[World].setContactListener(contacter)

    protected def handleCollisionBegin(ball: GameObjectBall, brick: GameObjectBrick): Unit ={

        sndKick01().play()

        brick.hit()
        if(brick.hitLimitReached){
            enqueueAfterWorldCmd{()=>
                aliveBricks-=1
                removeRenderable(brick)
                brick.destroy()
            }
        }

    }

    protected def handleCollisionBegin(ball: GameObjectBall, brick: GameObjectWall): Unit ={
        sndKick02().play()
    }

    protected def handleCollisionBegin(ball: GameObjectBall, brick: GameObjectBat): Unit ={
        sndKick02().play()
    }

    protected def handleCollisionEnd(ball: GameObjectBall, brick: GameObjectBrick): Unit ={

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
                    stage.handleCollisionEnd(ball, brick)
                case (ball: GameObjectBall, brick: GameObjectBrick) =>
                    stage.handleCollisionEnd(ball, brick)

                case (_: GameObjectWall, _: GameObjectBall) =>
                case (_: GameObjectBall, _: GameObjectWall) =>

                case _ =>
//                    logger.debug(s"unknown type contact end: ${a}  <=>  ${b}")
            }

            // logger.debug(s"contact end: ${a}  <=>  ${b}")
        }

        def beginContact(contact: Contact): Unit = {
            val a = contact.getFixtureA.getBody.getUserData()
            val b = contact.getFixtureB.getBody.getUserData()

            (a, b) match {

                case (brick: GameObjectBrick, ball: GameObjectBall) =>
                    stage.handleCollisionBegin(ball, brick)
                case (ball: GameObjectBall, brick: GameObjectBrick) =>
                    stage.handleCollisionBegin(ball, brick)

                case (wall: GameObjectWall, ball: GameObjectBall) =>
                    stage.handleCollisionBegin(ball, wall)
                case (ball: GameObjectBall, wall: GameObjectWall) =>
                    stage.handleCollisionBegin(ball, wall)

                case (_: GameObjectLooserDetector, _: GameObjectBall) =>
                    stage.impl_gameOver()
                case (_: GameObjectBall, _: GameObjectLooserDetector) =>
                    stage.impl_gameOver()

                case (bat: GameObjectBat, ball: GameObjectBall) =>
                    stage.handleCollisionBegin(ball, bat)
                case (ball: GameObjectBall, bat: GameObjectBat) =>
                    stage.handleCollisionBegin(ball, bat)


                case _ =>
                    logger.debug(s"unknown type contact end: ${a}  <=>  ${b}")
            }
        }
    }


}
