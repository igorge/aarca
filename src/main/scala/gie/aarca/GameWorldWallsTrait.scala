package gie.aarca

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.World
import gie.gdx.{ResourceContext, manageDisposableResource, manageResource}


trait GameWorldWallsTrait { asThis:ArcanoidStage=>

    protected def buildWall(x: Float, y: Float, w: Float, h: Float):GameObjectWall ={
        new GameObjectWall(x, y, w, h)
    }

    protected def buildWorldWalls(): Unit ={
        //bottom
        buildWall(0, -h/2-1, w, 1)

        //top
        buildWall(0, h/2, w, 1)

        //left
        buildWall(-w/2-0.5f, 0, 1, h)

        //right
        buildWall(w/2+0.5f, 0, 1, h)
    }



}