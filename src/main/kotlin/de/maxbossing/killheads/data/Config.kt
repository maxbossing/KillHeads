package de.maxbossing.killheads.data

import kotlinx.serialization.Serializable

@Serializable
data class Config(
    var prefix: String,
    var debug: Boolean,
    val persistentUUID: String
)