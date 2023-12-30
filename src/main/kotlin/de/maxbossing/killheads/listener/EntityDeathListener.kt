package de.maxbossing.killheads.listener

import de.maxbossing.killheads.data.ConfigController
import de.maxbossing.killheads.utils.HeadFactory
import de.maxbossing.killheads.utils.debug
import de.maxbossing.mxpaper.event.listen
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.EntityType
import org.bukkit.event.entity.EntityDeathEvent
import kotlin.random.Random

object EntityDeathListener {
    val eventDeathEvent = listen<EntityDeathEvent> {

        val textureConfig = ConfigController.getEntityTextureConfig(it.entity)?:return@listen
        if (!textureConfig.enabled)return@listen

        var actualChance = textureConfig.getDropRate()


        // Looting Logic
        if (it.entity.killer != null) {
            if (it.entity.killer!!.itemInHand.enchantments.containsKey(Enchantment.LOOT_BONUS_MOBS))
                actualChance += actualChance * it.entity.killer!!.itemInHand.enchantments[Enchantment.LOOT_BONUS_MOBS]!!
        }

        if (Random.nextInt(0, 100) > actualChance)return@listen

        val world = it.entity.location.world
        val loc = it.entity.location

        val head = HeadFactory.createHead(it.entity)?:return@listen

        world.dropItemNaturally(loc, head)
    }
}