package de.maxbossing.killheads.utils

import de.maxbossing.killheads.data.ConfigController
import de.maxbossing.mxpaper.MXColors
import de.maxbossing.mxpaper.extensions.bukkit.cmp
import de.maxbossing.mxpaper.extensions.bukkit.toComponent
import de.maxbossing.mxpaper.items.itemStack
import de.maxbossing.mxpaper.items.meta
import org.bukkit.Material
import org.bukkit.block.Skull
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import org.bukkit.entity.Item
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta
import java.util.*

/**
 * Creates Heads
 */
object HeadFactory {

    /**
     * Create a Head of an [Entity]
     *
     * This will resolve the Texture Key using [NameResolver] and then create a Head based from the texture in textures.json under that key
     *
     * This will only create Heads from actual "living" entities
     *
     * @param entity The Entity to create a Head from
     * @return The Head from the given [Entity], null if the [Entity] is invalid
     */
    fun createHead(entity: Entity): ItemStack? {

        if (entity.type == EntityType.PLAYER)
            return createPlayerHead(entity as Player)

        if (!entity.type.isSpawnable || !entity.type.isAlive || entity !is LivingEntity)
            return null

        // if the entity has a Vanilla Head, use that
        if (listOf(EntityType.ZOMBIE, EntityType.CREEPER, EntityType.SKELETON, EntityType.WITHER_SKELETON).contains(entity.type))return createVanillaHead(entity)!!

        val textureConfig = ConfigController.getEntityTextureConfig(entity)?:return null

        return createHeadFromTexture(entity, textureConfig.texture)
    }

    /**
     * Create a Head from a Texture Key
     * @param texture_name The Texture key as the key from [NameResolver]
     *
     * @return The Head, null if the key is invalid
     */
    fun createHead(texture_name: String): ItemStack?  {
        val textureConfig = ConfigController.getEntityTextureConfig(texture_name)?:return null
        return when (texture_name) {
            "creeper" -> if (textureConfig.texture == "[vanilla]") ItemStack(Material.CREEPER_HEAD) else createHeadFromTexture("Creeper", textureConfig.texture)
            "zombie" -> if (textureConfig.texture == "[vanilla]") ItemStack(Material.ZOMBIE_HEAD) else createHeadFromTexture("Zombie", textureConfig.texture)
            "skeleton" -> if (textureConfig.texture == "[vanilla]") ItemStack(Material.SKELETON_SKULL) else createHeadFromTexture("Skeleton", textureConfig.texture)
            "wither_skeleton" -> if (textureConfig.texture == "[vanilla]") ItemStack(Material.WITHER_SKELETON_SKULL) else createHeadFromTexture("Wither Skeleton", textureConfig.texture)
            "ender_dragon" -> if (textureConfig.texture == "[vanilla]") ItemStack(Material.DRAGON_HEAD) else createHeadFromTexture("Ender Dragon", textureConfig.texture)
            else -> createHeadFromTexture(texture_name, textureConfig.texture)
        }
    }

    /**
     * Create a Player Head
     * @param entity The Player whose skin to use
     * @return The Players Head
     */
    private fun createPlayerHead(entity: Player): ItemStack = itemStack(Material.PLAYER_HEAD) {
        meta<SkullMeta> {
            displayName(entity.name())
            owningPlayer = entity
        }
    }

    /**
     * Create a Head from a Base64 Texture
     * @param entity The Entity whose name to use for the Item Name
     * @param texture The Texture to use
     */
    private fun createHeadFromTexture(entity: Entity, texture: String): ItemStack = itemStack(Material.PLAYER_HEAD) {
        meta<SkullMeta> {
            if (entity.type == EntityType.VILLAGER)
                displayName(cmp("Villager", color = MXColors.YELLOW, italic = true))
            else if (entity.type == EntityType.ZOMBIE_VILLAGER)
                displayName(cmp("Zombie Villager", color = MXColors.YELLOW, italic = true))
            else
                displayName(entity.name())

            skullTexture(texture, UUID.fromString(ConfigController.CONFIG.persistentUUID))
        }
    }

    private fun createHeadFromTexture(name: String, texture: String): ItemStack = itemStack(Material.PLAYER_HEAD) {
        meta<SkullMeta> {
            displayName(name.toComponent())
            skullTexture(texture, UUID.fromString(ConfigController.CONFIG.persistentUUID))
        }
    }

    /**
     * Create a Vanilla Head from an Entity that has a respective head in game
     *
     * If a custom texture is defined, it will override the vanilla texture
     *
     * @param entity The Entity to create the head from
     * @return The Head, null if the entity does not have a Vanilla Head
     */
    private fun createVanillaHead(entity: Entity): ItemStack? {

        if (!listOf(EntityType.ZOMBIE, EntityType.CREEPER, EntityType.SKELETON, EntityType.WITHER_SKELETON, EntityType.ENDER_DRAGON).contains(entity.type))return null

        if (ConfigController.getEntityTextureConfig(entity) != null)
            if (ConfigController.getEntityTextureConfig(entity)!!.texture != "[vanilla]")
                return createHeadFromTexture(entity, ConfigController.getEntityTextureConfig(entity)!!.texture)

        return when (entity.type) {
            EntityType.ZOMBIE -> ItemStack(Material.ZOMBIE_HEAD)
            EntityType.SKELETON -> ItemStack(Material.SKELETON_SKULL)
            EntityType.WITHER_SKELETON -> ItemStack(Material.WITHER_SKELETON_SKULL)
            EntityType.ENDER_DRAGON -> ItemStack(Material.DRAGON_HEAD)
            EntityType.CREEPER -> ItemStack(Material.CREEPER_HEAD)
            else -> null
        }
    }

}