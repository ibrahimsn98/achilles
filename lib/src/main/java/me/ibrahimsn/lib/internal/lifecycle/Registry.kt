package me.ibrahimsn.lib.internal.lifecycle

interface Registry {

    fun process(state: Lifecycle.State)
}
