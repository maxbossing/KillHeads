package de.maxbossing.killheads.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.minecraft.nbt.FloatTag

@Serializable
data class EntityTextureConfig (
    var enabled: Boolean,

    @SerialName("drop_rate")
    private var dropRate: Float,

    @SerialName("looting_factor")
    private var lootingFactor: Float,

    var texture: String
) {
    fun getDropRate(): Float = dropRate.coerceIn(0f, 100f)
    fun setDropRate(rate: Float) { dropRate = rate.coerceIn(0f, 100f) }

    fun getLootingFactor(): Float = lootingFactor.coerceIn(0f, 100f)
    fun setLootingFactor(factor: Float) { lootingFactor = factor.coerceIn(0f, 100f) }
}