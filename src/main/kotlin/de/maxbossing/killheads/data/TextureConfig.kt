package de.maxbossing.killheads.data

import kotlinx.serialization.Serializable

@Serializable
data class TextureConfig (
    var entityTextures: MutableMap<String, EntityTextureConfig>,
    var playerTexture: EntityTextureConfig
)