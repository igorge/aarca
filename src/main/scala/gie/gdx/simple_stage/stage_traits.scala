package gie.gdx.stage

import gie.gdx.{ResourceContext, ResourceContextResolver}
import slogging.LoggerHolder


trait StageControllerApiTrait extends ResourceContextResolver {


    implicit def implicitResourceContext: ResourceContext

    def enqueue_pushStage_currentPaused(stage: StageTrait): Unit
    def enqueue_pushStage_currentDestroy(stage: StageTrait): Unit
    def enqueue_popStage(): Unit

    def enqueue_ReplaceStage(stage: StageTrait)

}

trait StageTrait extends ResourceContextResolver {

    val stageController: StageControllerApiTrait

    implicit def implicitResourceContext: ResourceContext = stageController.implicitResourceContext

    def update(delta: Float): Unit
    def render(delta: Float): Unit


    def onCreate(): Unit //TODO: pass load context
    def onDestroy(): Unit
    def onSaveState(): Unit //TODO: pass save context

    def onSurfaceChanged(width: Int, height: Int): Unit

    def onPause(): Unit
    def onResume(): Unit

}