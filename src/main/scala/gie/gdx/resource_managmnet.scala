package gie.gdx

import com.badlogic.gdx.utils.Disposable
import slogging.StrictLogging



final class DisposableOnce[T >: Null <:Disposable ](var resource:T) extends Disposable {
    @inline def apply() = resource
    @inline def get = resource

    def dispose(): Unit ={
        if (resource ne null){
            resource.dispose()
            resource = null
        }
    }
}

class ResourceHolder[T >: Null <: Disposable ](resource:T) extends Disposable {
    private val m_resource = new DisposableOnce[T](resource)
    @inline private[gdx] def disposableOnce = m_resource
    @inline def apply():T = m_resource()

    def dispose(): Unit ={
        m_resource.dispose()
    }
}

 class ResourceReference[T >: Null <: Disposable ](holder: ResourceHolder[T],
                                          val value: DisposableOnce[T],
                                          queue: java.lang.ref.ReferenceQueue[AnyRef]) extends java.lang.ref.PhantomReference[AnyRef](holder, queue) {
    def dispose(): Unit ={
        value.dispose()
    }
}

trait ResourceContextResolver {
    implicit def implicitResourceContext: ResourceContext
}


trait ResourceContext extends Disposable { this: StrictLogging =>

    private lazy val m_homeTread = Thread.currentThread.getId

    protected val gdxPhantomsQueue = new java.lang.ref.ReferenceQueue[AnyRef]()
    protected val gdxResources = new collection.mutable.HashSet[ResourceReference[_]]

    def queue = gdxPhantomsQueue

    @inline private def impl_checkHome(){
        if(m_homeTread != Thread.currentThread.getId) throw new Exception(s"Not at home, ${m_homeTread} != ${Thread.currentThread.getId}")
    }

    def registerResourceReference[T >: Null <: Disposable ](holder: ResourceHolder[T]):ResourceReference[T] = {
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
            logger.debug("gcTick(): released gdx resource")
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
    def managedResource[T >: Null <: Disposable ](resource: T)(implicit resourceContext:ResourceContext) = {
        val resourceHolder = new ResourceHolder[T](resource)

        resourceContext.registerResourceReference(resourceHolder)

        resourceHolder
    }

}