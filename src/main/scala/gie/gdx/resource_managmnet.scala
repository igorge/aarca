package gie.gdx

import com.badlogic.gdx.physics.box2d.Shape
import com.badlogic.gdx.utils.Disposable
import slogging.{LoggerHolder, StrictLogging}



abstract class DisposableOnceAbstract[T >: Null <: AnyRef](var resource:T) extends Disposable {
    @inline final def apply() = resource
    @inline final def get = resource

    private val debug_str = s"DisposableOnceAbstract(${resource}) aka ${super.toString}"

    protected def release():Unit

    override def toString = debug_str

    def dispose(): Unit ={
        if (resource ne null){
            release()
            resource = null
        }
    }

}

class ResourceHolder[T >: Null <: AnyRef](private val m_resource:DisposableOnceAbstract[T]) extends Disposable {
    @inline private[gdx] def disposableOnce = m_resource
    @inline def apply():T = m_resource()

    override def toString = s"ResourceHolder(${m_resource.toString}) aka ${super.toString}"

    def dispose(): Unit ={
        m_resource.dispose()
    }
}

 class ResourceReference[T >: Null <: AnyRef ](holder: ResourceHolder[T],
                                                   val value: DisposableOnceAbstract[T],
                                                   queue: java.lang.ref.ReferenceQueue[AnyRef]) extends java.lang.ref.PhantomReference[AnyRef](holder, queue) {

     private val debug_str = s"ResourceReference(${value.toString}) aka java.lang.ref.PhantomReference aka ${super.toString}"

     override def toString = debug_str


    def dispose(): Unit ={
        value.dispose()
    }
}

trait ResourceContextResolver {
    implicit def implicitResourceContext: ResourceContext
}


trait ResourceContext extends Disposable { this: LoggerHolder =>

    private lazy val m_homeTread = Thread.currentThread.getId

    protected val gdxPhantomsQueue = new java.lang.ref.ReferenceQueue[AnyRef]()
    protected val gdxResources = new collection.mutable.HashSet[ResourceReference[_]]

    def queue = gdxPhantomsQueue

    @inline private def impl_checkHome(){
        if(m_homeTread != Thread.currentThread.getId) throw new Exception(s"Not at home, ${m_homeTread} != ${Thread.currentThread.getId}")
    }

    def registerResourceReference[T >: Null <:AnyRef](holder: ResourceHolder[T]):ResourceReference[T] = {
        impl_checkHome()

        val phantomRef = new ResourceReference[T](holder, holder.disposableOnce, queue)
        gdxResources.add(phantomRef)

        phantomRef
    }


    def gcTick(){
        impl_checkHome()

        val tmp = gdxPhantomsQueue.poll().asInstanceOf[ResourceReference[_]]
        if (tmp ne null ) {
            val r = gdxResources.remove(tmp)
            assert(r)
            try{ tmp.dispose() } catch {
                case ex:Throwable => logger.error(s"Exception while freeing gdx resources: ${ex}")
            }
            logger.debug(s"gcTick(): released gdx resource: ${tmp}")
        }
    }

    def gcAllOnQueue(){
        impl_checkHome()

        var tmp = gdxPhantomsQueue.poll().asInstanceOf[ResourceReference[_]]

        while (tmp ne null ) {
            val r = gdxResources.remove(tmp)
            assert(r)
            try{ tmp.dispose() } catch {
                case ex:Throwable => logger.error(s"Exception while freeing GL resources: ${ex}")
            }
            logger.debug("gcTick(): released gdx resource")
            tmp = gdxPhantomsQueue.poll().asInstanceOf[ResourceReference[_]]
        }
    }

    def dispose(): Unit ={
        impl_checkHome()

        gdxResources.foreach{ resourceRef=>
            try{ resourceRef.dispose() } catch {
                case ex:Throwable => logger.error(s"Exception while freeing gdx resources: ${ex}")
            }
        }
        logger.debug("gcForceClearAll()")
    }
}

trait ResourcePackageTrait {

    def manageDisposableResource[T >: Null <: Disposable ](resource: T)(implicit resourceContext:ResourceContext) = {
        val resourceHolder = new ResourceHolder[T](new DisposableOnceAbstract(resource){
            protected def release(): Unit = resource.dispose()
        })

        resourceContext.registerResourceReference(resourceHolder)

        resourceHolder
    }

    def manageResource[T >: Null <: Shape](resource: T)(implicit resourceContext:ResourceContext) = {
        val resourceHolder = new ResourceHolder[T](new DisposableOnceAbstract(resource){
            protected def release(): Unit = resource.dispose()
        })

        resourceContext.registerResourceReference(resourceHolder)

        resourceHolder
    }


}