package me.ibrahimsn.lib.internal.lifecycle

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class LifecycleFlow(
    private val flow: Flow<Lifecycle.State>
) : Lifecycle, Flow<Lifecycle.State> by flow {

    override fun combineWith(vararg others: Lifecycle): Lifecycle {
        var combined: Flow<Lifecycle.State> = this
        others.forEach {
            combined = combined.combine(it) { first, second ->
                first.combine(second)
            }
        }
        return LifecycleFlow(combined)
    }
}
