package gie

import com.badlogic.gdx.Gdx

import scala.concurrent._
import scala.util.Try


package object gdx {

    def uiFuture[R](f: =>R): Future[R]={

        val promise = Promise[R]()

        Gdx.app.postRunnable(new Runnable() {
            def run(): Unit = promise.complete(Try{f})
        })


        promise.future
    }


}


