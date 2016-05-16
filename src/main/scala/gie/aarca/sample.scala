package gie.aarca

import android.app.Activity
import android.os.Bundle
import android.util.Log
import org.slf4j
import slogging._


object MainActivity {
    LoggerConfig.factory = SLF4JLoggerFactory//PrintLoggerFactory
    LoggerConfig.level = LogLevel.TRACE

    def  onCreate(): Unit ={}
}

class MainActivity extends Activity with TypedFindView with LazyLogging {
    lazy val textview = findView(TR.text)

     val ll = slf4j.LoggerFactory.getLogger(classOf[MainActivity])

    /** Called when the activity is first created. */
     override def onCreate(savedInstanceState: Bundle): Unit = {
        super.onCreate(savedInstanceState)
         MainActivity.onCreate()
         setContentView(R.layout.main)
         textview.setText("Hello world, from aarca!")

        Log.d("tag","debug Log.d()")

        ll.debug("ddddd!!!fff")

        logger.debug("1111111111111111111debug message!!!")

        ll.warn("lll ddddd!!!12")


        logger.info("INFO!! wow22!")
        logger.error("TEST!!! wow2")
        logger.warn("WARN!!!")
    }
}