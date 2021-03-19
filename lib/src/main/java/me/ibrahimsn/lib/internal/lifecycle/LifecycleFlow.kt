package me.ibrahimsn.lib.internal.lifecycle

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.combine

class LifecycleFlow(
    private val flow: MutableSharedFlow<Lifecycle.State>
) : Lifecycle, MutableSharedFlow<Lifecycle.State> by flow {

    override fun combineWith(vararg others: Lifecycle): Lifecycle {
        others.forEach {
            this.combine(it) { first, second ->
                first.combine(second)
            }
        }
        return this
    }
}
