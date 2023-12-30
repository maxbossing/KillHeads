package de.maxbossing.killheads

import de.maxbossing.killheads.commands.killHeadsCommand
import de.maxbossing.killheads.data.ConfigController
import de.maxbossing.killheads.listener.EntityDeathListener
import de.maxbossing.mxpaper.MXColors
import de.maxbossing.mxpaper.extensions.bukkit.cmp
import de.maxbossing.mxpaper.main.MXPaper
import de.maxbossing.mxpaper.main.prefix
import dev.jorel.commandapi.CommandAPI
import dev.jorel.commandapi.CommandAPIBukkitConfig
import io.github.rysefoxx.inventory.plugin.pagination.InventoryManager

class KillHeads: MXPaper() {

    companion object {
        lateinit var INSTANCE: KillHeads
    }


    val INVENTORY_MANAGER = InventoryManager(this)

    override fun load() {
        INSTANCE = this

        CommandAPI.onLoad(
            CommandAPIBukkitConfig(this)
        )

        ConfigController
        prefix = cmp("prefix", MXColors.RED)

        logger.info("KillHeads loaded!")
    }

    override fun startup() {
        CommandAPI.onEnable()

        killHeadsCommand

        INVENTORY_MANAGER.invoke()

        EntityDeathListener
        logger.info("KillHeads activated")
    }

    override fun shutdown() {

        ConfigController.save()

        logger.info("KillHeads deactivated")
    }
}

val KILLHEADS by lazy { KillHeads.INSTANCE }
val LOGGER by lazy { KILLHEADS.logger }
