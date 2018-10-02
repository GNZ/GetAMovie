package com.gnz.getamovie.data.common


sealed class ResourceState

object Loading : ResourceState()
object EmptyState : ResourceState()
object EmptyQueryState : ResourceState()
object PopulateState : ResourceState()
data class ErrorState(val throwable: Throwable) : ResourceState()