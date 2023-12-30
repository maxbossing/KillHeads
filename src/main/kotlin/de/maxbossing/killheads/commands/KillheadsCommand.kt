package de.maxbossing.killheads.commands

import de.maxbossing.killheads.data.ConfigController
import de.maxbossing.killheads.gui.TextureChooseGUI
import de.maxbossing.killheads.utils.HeadFactory
import de.maxbossing.mxpaper.MXColors
import de.maxbossing.mxpaper.extensions.bukkit.cmp
import de.maxbossing.mxpaper.extensions.bukkit.give
import de.maxbossing.mxpaper.extensions.bukkit.plus
import de.maxbossing.mxpaper.main.prefix
import dev.jorel.commandapi.arguments.ArgumentSuggestions
import dev.jorel.commandapi.kotlindsl.*
import org.bukkit.entity.Wither.Head

val killHeadsCommand = commandTree("killheads") {
    withPermission("killheads.admin")

    literalArgument("reload") {
        withPermission("killheads.admin.reload")
        playerExecutor { player, _ ->
            ConfigController.reload(false)
            player.sendMessage(prefix + cmp("Textures Reloaded", MXColors.GRAY))
        }
    }

    literalArgument("head") {
        withPermission("killheads.admin.head")
        stringArgument("texture_name") {
            replaceSuggestions(ArgumentSuggestions.strings(ConfigController.TEXTURES.entityTextures.map { it.key }))
            playerExecutor { player, args ->

                val head = HeadFactory.createHead(args["texture_name"] as String)

                if (head == null) {
                    player.sendMessage(prefix + cmp("Head not found!", MXColors.INDIANRED))
                    return@playerExecutor
                }

                player.give(head)
            }
        }
    }

    playerExecutor { player, _ ->
        TextureChooseGUI(player)
    }
}