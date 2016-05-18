package gie.aarca

import android.app.{Activity, Application}
import android.os.Bundle
import android.util.Log
import com.badlogic.gdx.ApplicationListener
import com.badlogic.gdx.backends.android.AndroidApplication
import org.slf4j
import slogging._


class App extends Application {
    override def onCreate(): Unit ={
        super.onCreate()

        LoggerConfig.factory = SLF4JLoggerFactory//PrintLoggerFactory
        LoggerConfig.level = LogLevel.TRACE
    }
}


class Game extends ApplicationListener {
    def resize(i: Int, i1: Int): Unit ={

    }

    def dispose(): Unit ={

    }

    def pause(): Unit ={

    }

    def render(): Unit ={

    }

    def resume(): Unit ={

    }

    def create(): Unit ={

    }
}


class MainActivity extends AndroidApplication with TypedFindView with LazyLogging {
     lazy val textview = findView(TR.text)

    /** Called when the activity is first created. */
     override def onCreate(savedInstanceState: Bundle): Unit = {
        logger.debug("1111111111111111111debug message!!!")
        super.onCreate(savedInstanceState)
        initialize(new Game());
    }
}