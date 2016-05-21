package gie.gdx

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.{Camera, Color}
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.{Matrix4, Vector2, Vector3}
import com.badlogic.gdx.utils.viewport.Viewport


trait DebugRenderer { this: ResourceContextResolver =>

    private lazy val m_debugRenderer = manageDisposableResource (new ShapeRenderer())

    def debugRenderer = m_debugRenderer()

    def drawDebugLine(start:Vector2, end:Vector2, lineWidth:Int, color:Color, projectionMatrix:Matrix4)
    {
        Gdx.gl.glLineWidth(lineWidth)
        debugRenderer.setProjectionMatrix(projectionMatrix)
        debugRenderer.begin(ShapeRenderer.ShapeType.Line)
        debugRenderer.setColor(color)
        debugRenderer.line(start, end)
        debugRenderer.end()
        Gdx.gl.glLineWidth(1)
    }

    def drawDebugAxis(viewport: Viewport, lineWidth:Int = 5): Unit ={
        val camera = viewport.getCamera

        val minXY = camera.unproject( new Vector3(viewport.getScreenX, viewport.getScreenY, 0),  viewport.getScreenX, viewport.getScreenY, viewport.getScreenWidth, viewport.getScreenHeight )
        val maxXY = camera.unproject( new Vector3(viewport.getScreenX+viewport.getScreenWidth, viewport.getScreenY+viewport.getScreenHeight, 0),  viewport.getScreenX, viewport.getScreenY, viewport.getScreenWidth, viewport.getScreenHeight )

        drawDebugLine(new Vector2(minXY.x,0), new Vector2(maxXY.x,0), lineWidth, Color.GREEN, camera.combined)
        drawDebugLine(new Vector2(0,minXY.y), new Vector2(0,maxXY.y), lineWidth, Color.RED, camera.combined)
    }

}