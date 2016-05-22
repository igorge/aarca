package gie.gdx.stage

import com.badlogic.gdx.utils.viewport.Viewport
import gie.gdx.{ResourceContext, ResourceContextResolver}
import slogging.StrictLogging

trait StageControllerApiTrait extends ResourceContextResolver {


    implicit def implicitResourceContext: ResourceContext

    protected def enqueue_pushStage(stage: StageTrait): Unit
    protected def enqueue_popStage(): Unit

    protected def enqueue_replaceStage(stage: StageTrait): Unit

}

trait StageTrait extends ResourceContextResolver {

    def stageController: StageControllerApiTrait

    implicit def implicitResourceContext: ResourceContext = stageController.implicitResourceContext

    def viewport: Viewport // may return null

    def update(delta: Float): Unit
    def render(delta: Float): Unit


    def onCreate(): Unit //TODO: pass load context
    def onDestroy(): Unit
    def onSaveState(): Unit //TODO: pass save context

    def onSurfaceChanged(width: Int, height: Int): Unit

    def onPause(): Unit
    def onResume(): Unit

    def renderDebugInfo(): Unit

}

class StageWrapper(stage: StageTrait) extends StageTrait with StrictLogging {
    def stageController: StageControllerApiTrait = {
        logger.debug(s"StageWrapper(${stage}).stageController")
        stage.stageController
    }

    def update(delta: Float): Unit ={
        //logger.debug(s"StageWrapper(${stage}).update(${delta})")
        stage.update(delta)
    }

    def onSurfaceChanged(width: Int, height: Int): Unit ={
        logger.debug(s"StageWrapper(${stage}).onSurfaceChanged(${width}, ${height})")
        stage.onSurfaceChanged(width, height)
    }

    def onPause(): Unit ={
        logger.debug(s"StageWrapper(${stage}).onPause()")
        stage.onPause()
    }

    def onSaveState(): Unit ={
        logger.debug(s"StageWrapper(${stage}).onSaveState()")
        stage.onSaveState()
    }

    def viewport: Viewport ={
        //logger.debug(s"StageWrapper(${stage}).viewport")
        stage.viewport
    }

    def render(delta: Float): Unit ={
        //logger.debug(s"StageWrapper(${stage}).render(${delta})")
        stage.render(delta)
    }

    def onResume(): Unit ={
        logger.debug(s"StageWrapper(${stage}).onResume()")
        stage.onResume()
    }

    def onDestroy(): Unit ={
        logger.debug(s"StageWrapper(${stage}).onDestroy()")
        stage.onDestroy()
    }

    def onCreate(): Unit ={
        logger.debug(s"StageWrapper(${stage}).onCreate()")
        stage.onCreate()

    }

    def renderDebugInfo(): Unit={
        //logger.debug(s"StageWrapper(${stage}).renderDebugInfo()")
        stage.renderDebugInfo()
    }

}