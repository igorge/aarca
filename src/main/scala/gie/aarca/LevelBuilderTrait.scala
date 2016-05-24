package gie.aarca

import com.badlogic.gdx.Gdx

import scala.collection.mutable.ArrayBuffer
import scala.io.Source


trait LevelBuilderTrait { this: ArcanoidStage=>


    protected def loadLevel(fileName: String): Seq[GameObjectBrick] ={
        val levelFile = Gdx.files.internal(s"levels/${fileName}")

        val lines = Source.fromInputStream(levelFile.read()).getLines().toArray

        var bricks = new ArrayBuffer[GameObjectBrick]()

        val yStep = -1f
        val xStart = -9
        val xStep = 2

        var currentY = h/2 - 1
        var currentX = xStart

        lines.foreach{ line =>

            assert(line.length<=10)

            line.foreach{ brickType=>
                buildBrick(brickType, currentX, currentY).map(bricks += _)
                currentX += xStep
            }
            currentX = xStart

            currentY += yStep
        }


        bricks
    }

    private def buildBrick(brickType: Char, x: Float, y: Float): Option[GameObjectBrick]={

        brickType match {
            case ' ' => None
            case '1' => Some(new GameObjectBrick(brickTex(), x, y))
            case '2' => Some(new GameObjectBrick2(brickGreenTex(), brickGreenTexBroken(), x, y))
            case _ =>
                logger.debug(s"unknown brick type '${brickType}'")
                None
        }

    }


}