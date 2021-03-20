package me.ibrahimsn.lib.internal.lifecycle

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class LifecycleFlow(
    private val flow: Flow<Lifecycle.State>
) : Lifecycle, Flow<Lifecycle.State> by flow {

    override fun combineWith(other: Lifecycle): Lifecycle {
        return LifecycleFlow(this.combine(other) { first, second ->
            first.combine(second)
        })
    }
}
