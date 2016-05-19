package gie.aarca

import android.app.{Activity, Application}
import android.os.Bundle
import android.util.Log
import com.badlogic.gdx.ApplicationListener
import com.badlogic.gdx.backends.android.{AndroidApplication, AndroidApplicationConfiguration}
import org.slf4j
import slogging._

import scala.concurrent.ExecutionContext.Implicits.global

class App extends Application with LazyLogging {

    override def onCreate(): Unit ={
        super.onCreate()

        LoggerConfig.factory = SLF4JLoggerFactory//PrintLoggerFactory
        LoggerConfig.level = LogLevel.TRACE

        logger.trace("Application.onCreate()")
    }
}


class MainActivity extends AndroidApplication with TypedFindView with LazyLogging {

    override def onCreate(savedInstanceState: Bundle): Unit = {
        logger.trace("MainActivity.onCreate()")
        super.onCreate(savedInstanceState)

        val cfg = new AndroidApplicationConfiguration()
        initialize(new Game(), cfg)
    }

}
