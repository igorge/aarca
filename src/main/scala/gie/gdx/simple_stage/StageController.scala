package gie.gdx.stage
import com.badlogic.gdx.utils.viewport.Viewport
import gie.gdx.ResourceContext
import slogging.StrictLogging

import scala.collection.mutable


abstract class StageController
    extends StageTrait
        with StageControllerApiTrait
        with StrictLogging
{

    val stageController: StageControllerApiTrait = this

    override implicit def implicitResourceContext: ResourceContext

    private val m_stageStack = new mutable.Stack[StageTrait]()
    private val m_cmdQueue = new mutable.Queue[()=>Unit]()

    private var m_surfaceWidth = 0
    private var m_surfaceHeight = 0

    private var m_currentStage: StageTrait = NullStage

    protected def enqueue_replaceStage(stage: StageTrait): Unit ={
        logger.debug(s"StageController.enqueue_ReplaceStage(${stage})")

        m_cmdQueue += (()=>{
            impl_destroyStage(m_currentStage)
            m_currentStage = stage
            impl_createAndResumeStage(stage)
        })
    }

    protected def enqueue_popStage(): Unit ={
        logger.debug(s"StageController.enqueue_popStage()")

        m_cmdQueue += (()=>{
            impl_destroyStage(m_currentStage)
            m_currentStage = m_stageStack.pop()
            imp_resumeStage(m_currentStage)
        })
    }

    protected def enqueue_pushStage(stage: StageTrait): Unit ={
        logger.debug(s"StageController.enqueue_pushStage(${stage})")

        m_cmdQueue += (()=>{
            m_currentStage.onPause()
            m_stageStack.push(m_currentStage)
            m_currentStage = stage
            impl_createAndResumeStage(stage)
        })
    }


    def update(delta: Float): Unit ={
        m_currentStage.update(delta)
    }

    def render(delta: Float): Unit ={
        m_currentStage.render(delta)
        imp_ProcessCmd()
    }

    def onSurfaceChanged(width: Int, height: Int): Unit = {
        m_surfaceWidth=width
        m_surfaceHeight=height

        m_currentStage.onSurfaceChanged(width, height)

        imp_ProcessCmd()
    }

    def onPause(): Unit ={
        imp_ProcessCmd()
        m_currentStage.onPause()
    }
    def onResume(): Unit ={
        imp_resumeStage(m_currentStage)
    }

    def onDestroy(): Unit ={
        imp_ProcessCmd()
        m_currentStage.onDestroy()
    }

    def onCreate(): Unit ={
        m_currentStage.onCreate()
    }

    def onSaveState(): Unit ={
        m_currentStage.onSaveState()
        imp_ProcessCmd()
    }


    def renderDebugInfo(): Unit ={
        m_currentStage.renderDebugInfo()
    }

    def viewport: Viewport = m_currentStage.viewport

    private def imp_ProcessCmd(): Unit ={
        m_cmdQueue.foreach(f=>f())
        m_cmdQueue.clear()
    }


    private def imp_resumeStage(stage: StageTrait): Unit ={
        stage.onSurfaceChanged(m_surfaceWidth, m_surfaceHeight)
        stage.onResume()
    }

    private def impl_createAndResumeStage(stage: StageTrait): Unit ={
        stage.onCreate()
        imp_resumeStage(stage)
    }

    private def impl_destroyStage(stage: StageTrait): Unit ={
        stage.onPause()
        stage.onDestroy()
    }

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

    def viewport: Viewport = null

    def renderDebugInfo(): Unit ={}
}