package de.maxbossing.killheads.utils

import org.bukkit.DyeColor
import org.bukkit.entity.*
import org.bukkit.entity.Villager.Profession


/**
 * Resolves Entities to their Texture Keys
 */
object NameResolver {

    /**
     * Resolve the Texture name of a an [Entity]
     *
     * This will only resolve Entities that can drop a (custom) Head
     *
     * @param entity the [Entity] to resolve
     * @return the texture key that can be used to create a Head from the [HeadFactory]
     */
    fun resolve(entity: Entity): String? {
        debug("Requesting name for $entity")

        // Only actual "Mobs" can be resolved for a texture key
        if (!entity.type.isSpawnable || !entity.type.isAlive || entity !is LivingEntity)
            return null

        return when (entity.type) {
            EntityType.CREEPER -> resolveCreeper(entity as Creeper)
            EntityType.CAT -> resolveCat(entity as Cat)
            EntityType.RABBIT -> resolveRabbit(entity as Rabbit)
            EntityType.HORSE -> resolveHorse(entity as Horse)
            EntityType.LLAMA -> resolveLlama(entity as Llama)
            EntityType.SHEEP -> resolveSheep(entity as Sheep)
            EntityType.WOLF -> resolveWolf(entity as Wolf)
            EntityType.PARROT -> resolveParrot(entity as Parrot)
            else -> entity.type.name.lowercase()
        }
    }


    /**
     * Resolve the Different [Creeper]s
     */
    private fun resolveCreeper(entity: Creeper): String = if (entity.isPowered) "charged_creeper" else "creeper"

    /**
     * Resolve the different [Cat]s
     */
    private fun resolveCat(entity: Cat): String = "cat_${entity.catType.name.lowercase()}"

    /**
     * Resolve the differen [Rabbit]s
     */
    private fun resolveRabbit(entity: Rabbit): String = "rabbit_${entity.rabbitType.name.lowercase()}"

    /**
     * Resolve the different [Horse]s
     */
    private fun resolveHorse(entity: Horse): String = "horse_${entity.color.name.lowercase()}"

    /**
     * Resolve the different [Llama]s
     */
    private fun resolveLlama(entity: Llama): String = "llama_${entity.color.name.lowercase()}"

    /**
     * Resolve the different [Sheep]s
     */
    private fun resolveSheep(entity: Sheep): String = "sheep_${(entity.color?:DyeColor.WHITE).name.lowercase()}"

    /**
     * Resolve the different [Wolf]s
     */
    private fun resolveWolf(entity: Wolf): String = if (entity.isTamed) "wolf_tamed" else "wolf"

    /**
     * Resolve the different [Parrot]s
     */
    private fun resolveParrot(entity: Parrot): String = "parrot_${entity.variant.name.lowercase()}"

}