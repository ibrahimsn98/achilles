package me.ibrahimsn.lib.internal.lifecycle

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged

class LifecycleRegistry internal constructor(
    private val flow: MutableSharedFlow<Lifecycle.State>,
    private val debounceMillis: Long
) : Lifecycle by LifecycleFlow(
    flow.apply {
        distinctUntilChanged(Lifecycle.State::isEquivalentTo)
        if (debounceMillis != 0L) debounce(debounceMillis)
        distinctUntilChanged(Lifecycle.State::isEquivalentTo)
    }
) {

    constructor(debounceMillis: Long = 0L) : this(
        MutableSharedFlow(),
        debounceMillis
    )
}
