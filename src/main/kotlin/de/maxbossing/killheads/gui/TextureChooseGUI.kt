package de.maxbossing.killheads.gui

import de.maxbossing.killheads.KILLHEADS
import de.maxbossing.killheads.data.ConfigController
import de.maxbossing.killheads.utils.HeadFactory
import de.maxbossing.killheads.utils.skullTexture
import de.maxbossing.mxpaper.MXColors
import de.maxbossing.mxpaper.MXHeads
import de.maxbossing.mxpaper.extensions.bukkit.cmp
import de.maxbossing.mxpaper.extensions.bukkit.plus
import de.maxbossing.mxpaper.extensions.bukkit.toComponent
import de.maxbossing.mxpaper.items.itemStack
import de.maxbossing.mxpaper.items.meta
import de.maxbossing.mxpaper.items.setLore
import io.github.rysefoxx.inventory.plugin.content.IntelligentItem
import io.github.rysefoxx.inventory.plugin.content.InventoryContents
import io.github.rysefoxx.inventory.plugin.content.InventoryProvider
import io.github.rysefoxx.inventory.plugin.pagination.Pagination
import io.github.rysefoxx.inventory.plugin.pagination.RyseInventory
import io.github.rysefoxx.inventory.plugin.pagination.SlotIterator
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.meta.SkullMeta

class TextureChooseGUI(val player: Player) {

    fun textureButton(texture_name: String): IntelligentItem {
        val stack = HeadFactory.createHead(texture_name)!!
        stack.meta {
            displayName(cmp(texture_name.replace("_", " ").split(' ').joinToString(" ") { it.replaceFirstChar(Char::uppercaseChar) }, MXColors.CORNFLOWERBLUE))
            setLore {
                lorelist += cmp("Enabled: ", MXColors.GRAY) + if (ConfigController.getEntityTextureConfig(texture_name)!!.enabled) cmp("yes", MXColors.GREEN) else cmp("no", MXColors.RED)
                lorelist += cmp("")
                lorelist += cmp("Left-Click: Toggle enabled")
                lorelist += cmp("Right-Click: Edit", )
            }
        }

        return IntelligentItem.of(stack) {
            if (it.isLeftClick) {
                ConfigController.getEntityTextureConfig(texture_name)!!.enabled = !ConfigController.getEntityTextureConfig(texture_name)!!.enabled
                it.currentItem!!.meta {
                    setLore {
                        lorelist += cmp("Enabled: ", MXColors.GRAY) + if (ConfigController.getEntityTextureConfig(texture_name)!!.enabled) cmp("yes", MXColors.GREEN) else cmp("no", MXColors.RED)
                        lorelist += cmp("")
                        lorelist += cmp("Left-Click: Toggle enabled")
                        lorelist += cmp("Right-Click: Edit", )
                    }
                }
            } else {
                EntityTextureConfigGUI(player, texture_name)
            }
        }
    }

    fun nextPageButton(pagination: Pagination): IntelligentItem = IntelligentItem.of(
        itemStack(Material.PLAYER_HEAD) {
            meta<SkullMeta> {
                skullTexture(MXHeads.ARROW_RIGHT_WHITE)
                displayName(if (pagination.isLast) cmp("»", strikethrough = true, bold = true, color = MXColors.GRAY) else cmp("»", strikethrough = false, bold = true, color = MXColors.GRAY))
                amount = if (pagination.isLast) 1 else pagination.page()

            }
        }
    ) {
        if (pagination.isLast) return@of
        pagination.inventory().open(player, pagination.next().page())
    }

    fun previousPageButton(pagination: Pagination): IntelligentItem = IntelligentItem.of(
        itemStack(Material.PLAYER_HEAD) {
            meta<SkullMeta> {
                skullTexture(MXHeads.ARROW_LEFT_WHITE)
                displayName(if (pagination.isFirst) cmp("«", strikethrough = true, bold = true, color = MXColors.GRAY) else cmp("«", strikethrough = false, bold = true, color = MXColors.GRAY))
                amount = pagination.page()
                amount = if (pagination.isFirst) 1 else pagination.page() - 1
            }
        }
    ) {
        if (pagination.isFirst) return@of
        pagination.inventory().open(player, pagination.previous().page())
    }
    val gui = RyseInventory
        .builder()
        .rows(6)
        .title("KillHeads")
        .provider( object : InventoryProvider {
            override fun init(player: Player?, contents: InventoryContents) {

                //  0  1  2  3  4  5  6  7  8
                // 10 11 12 13 14 15 16 17 18
                // 19 20 21 22 23 24 25 26 27
                // 28 29 30 31 32 33 34 35 36
                // 37 38 39 40 41 42 43 44 45
                // 46 47 48 49 50 51 52 53 54

                contents.fillBorders(itemStack(Material.BLACK_STAINED_GLASS_PANE) { meta { displayName(cmp(""))}})

                val pagination = contents.pagination()

                pagination.iterator(SlotIterator.builder()
                    .startPosition(1,1)
                    .endPosition(4,7)
                    .type(SlotIterator.SlotIteratorType.HORIZONTAL)
                    .build())

                for (key in ConfigController.TEXTURES.entityTextures.map { it.key }.plus("player").sorted())
                    pagination.addItem(textureButton(key))


                contents.set(5, 3, previousPageButton(pagination))
                contents.set(5, 5, nextPageButton(pagination))
            }
        }).build(KILLHEADS)

    init {
        gui.open(player)
    }

}