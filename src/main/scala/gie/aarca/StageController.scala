package gie.aarca

import com.badlogic.gdx.utils.viewport.Viewport
import gie.gdx.ResourceContext
import gie.gdx.stage.{StageControllerApiTrait, StageTrait, StageWrapper, StageController => GdxStageController}

abstract class StageController()(implicit implicitParentResourceContext: ResourceContext) extends GdxStageController{

    override implicit def implicitResourceContext: ResourceContext = implicitParentResourceContext

    override def onCreate(): Unit ={
        super.onCreate()

        enqueue_pushStage{ controller=>
            new StageWrapper(new ArcanoidStage(1, controller))
        }

    }

}

