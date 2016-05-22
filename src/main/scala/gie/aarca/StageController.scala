package gie.aarca

import com.badlogic.gdx.utils.viewport.Viewport
import gie.gdx.ResourceContext
import gie.gdx.stage.{StageControllerApiTrait, StageTrait, StageWrapper, StageController => GdxStageController}

class StageController()(implicit implicitParentResourceContext: ResourceContext) extends GdxStageController{

    override implicit def implicitResourceContext: ResourceContext = implicitParentResourceContext

    override def onCreate(): Unit ={
        super.onCreate()

        enqueue_pushStage(new StageWrapper(new ArcanoidStage(this)))

    }

}



class ArcanoidStage(val stageController: StageControllerApiTrait) extends StageTrait {

    def update(delta: Float): Unit = {}

    def onSurfaceChanged(width: Int, height: Int): Unit ={}

    def onPause(): Unit ={}

    def onSaveState(): Unit ={}

    def viewport: Viewport =null

    def render(delta: Float): Unit ={}

    def onResume(): Unit ={}

    def onDestroy(): Unit ={}

    def onCreate(): Unit ={}
}