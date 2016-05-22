package gie.gdx.stage
import gie.gdx.ResourceContext
import slogging.StrictLogging


class StageController
    extends ResourceContext
        with StageTrait
        with StageControllerApiTrait
        with StrictLogging
{

    val stageController: StageControllerApiTrait = this

    private var m_currentStage: StageTrait = NullStage

    override implicit def implicitResourceContext: ResourceContext = this

    def enqueue_ReplaceStage(stage: StageTrait): Unit = ???
    def enqueue_popStage(): Unit = ???
    def enqueue_pushStage_currentDestroy(stage: StageTrait): Unit = ???
    def enqueue_pushStage_currentPaused(stage: StageTrait): Unit = ???


    def update(delta: Float): Unit = m_currentStage.update(delta)
    def render(delta: Float): Unit = m_currentStage.render(delta)

    def onSurfaceChanged(width: Int, height: Int): Unit = m_currentStage.onSurfaceChanged(width, height)
    def onPause(): Unit = m_currentStage.onPause()
    def onResume(): Unit = m_currentStage.onResume()
    def onDestroy(): Unit = m_currentStage.onDestroy()
    def onCreate(): Unit = m_currentStage.onCreate()
    def onSaveState(): Unit = m_currentStage.onSaveState()
}


object NullStage extends StageTrait {
    val stageController: StageControllerApiTrait = null

    def update(delta: Float): Unit = {}

    def onSurfaceChanged(width: Int, height: Int): Unit = {}

    def onPause(): Unit = {}


    def onResume(): Unit = {}

    def render(delta: Float): Unit = {}

    def onDestroy(): Unit = {}

    def onCreate(): Unit = {}

    def onSaveState(): Unit = {}
}