package gie.utils

class RunOnce(f: ()=>Unit) extends Function0[Unit]{

    private var m_current = impl_first _

    private def impl_first(): Unit =try {
        f()
    } finally {
        m_current = impl_second _
    }

    private def impl_second(): Unit ={

    }


    def apply(): Unit = m_current()
}