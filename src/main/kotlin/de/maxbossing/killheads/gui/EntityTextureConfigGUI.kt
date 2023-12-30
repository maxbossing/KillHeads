package de.maxbossing.killheads.gui

import de.maxbossing.killheads.KILLHEADS
import de.maxbossing.killheads.data.ConfigController
import de.maxbossing.killheads.utils.skullTexture
import de.maxbossing.mxpaper.MXColors
import de.maxbossing.mxpaper.MXHeads
import de.maxbossing.mxpaper.extensions.bukkit.cmp
import de.maxbossing.mxpaper.extensions.bukkit.plus
import de.maxbossing.mxpaper.items.itemStack
import de.maxbossing.mxpaper.items.meta
import de.maxbossing.mxpaper.items.setLore
import io.github.rysefoxx.inventory.plugin.content.IntelligentItem
import io.github.rysefoxx.inventory.plugin.content.InventoryContents
import io.github.rysefoxx.inventory.plugin.content.InventoryProvider
import io.github.rysefoxx.inventory.plugin.pagination.RyseInventory
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta

class EntityTextureConfigGUI(val player: Player, val texture_name: String) {


    fun backButton() = IntelligentItem.of(
        itemStack(Material.PLAYER_HEAD) {
            meta<SkullMeta> {
                displayName(cmp("back", MXColors.GRAY))
                skullTexture(MXHeads.ARROW_RESET_WHITE)
            }
        }
    ) {
        TextureChooseGUI(player)
    }

    fun enabledButton() = IntelligentItem.of(
       itemStack(
           if (ConfigController.getEntityTextureConfig(texture_name)!!.enabled)
            Material.GREEN_STAINED_GLASS_PANE
           else
               Material.RED_STAINED_GLASS_PANE
       ){
           meta {
               displayName(if (ConfigController.getEntityTextureConfig(texture_name)!!.enabled) cmp("enabled", MXColors.GREEN) else cmp("disabled", MXColors.RED))
           }
       }
   ) {
       ConfigController.getEntityTextureConfig(texture_name)!!.enabled = !ConfigController.getEntityTextureConfig(texture_name)!!.enabled
       it.currentItem!!.type = if (ConfigController.getEntityTextureConfig(texture_name)!!.enabled)
           Material.GREEN_STAINED_GLASS_PANE
       else
           Material.RED_STAINED_GLASS_PANE

       it.currentItem!!.meta {
           displayName(if (ConfigController.getEntityTextureConfig(texture_name)!!.enabled) cmp("enabled", MXColors.GREEN) else cmp("disabled", MXColors.RED))
       }
   }

    fun dropChanceButton() = IntelligentItem.of(
        itemStack(Material.SPYGLASS) {
            meta {
                displayName(cmp("Drop Chance: ", MXColors.GRAY) + cmp("${ConfigController.getEntityTextureConfig(texture_name)!!.getDropRate()}", MXColors.CORNFLOWERBLUE))
                setLore {
                    lorelist += cmp("Right-Click: ", MXColors.GRAY) + cmp("+1", MXColors.CORNFLOWERBLUE)
                    lorelist += cmp("Shift-Right-Click: ", MXColors.GRAY)+ cmp("+10", MXColors.CORNFLOWERBLUE)
                    lorelist += cmp("----------------------", MXColors.GRAY, bold = true)
                    lorelist += cmp("Left-Click: ", MXColors.GRAY) + cmp("-1", MXColors.CORNFLOWERBLUE)
                    lorelist += cmp("Shift-Left-Click:", MXColors.GRAY) + cmp("-10", MXColors.CORNFLOWERBLUE)
                }
            }
        }
    ) {
        ConfigController.getEntityTextureConfig(texture_name)!!.setDropRate(ConfigController.getEntityTextureConfig(texture_name)!!.getDropRate() + if (it.isLeftClick) if (it.isShiftClick) -10 else -1 else if (it.isShiftClick) +10 else +1)
        it.currentItem!!.meta { displayName(cmp("Drop Chance: ", MXColors.GRAY) + cmp("${ConfigController.getEntityTextureConfig(texture_name)!!.getDropRate()}", MXColors.CORNFLOWERBLUE)) }
    }

    fun lootingButton() = IntelligentItem.of(
        itemStack(Material.ENCHANTED_BOOK) {
            meta {
                displayName(cmp("Looting Bonus: ", MXColors.GRAY) + cmp("${ConfigController.getEntityTextureConfig(texture_name)!!.getLootingFactor()}", MXColors.CORNFLOWERBLUE))
                setLore {
                    lorelist += cmp("Right-Click: ", MXColors.GRAY) + cmp("+1", MXColors.CORNFLOWERBLUE)
                    lorelist += cmp("Shift-Right-Click: ", MXColors.GRAY)+ cmp("+10", MXColors.CORNFLOWERBLUE)
                    lorelist += cmp("----------------------", MXColors.GRAY, bold = true)
                    lorelist += cmp("Left-Click: ", MXColors.GRAY) + cmp("-1", MXColors.CORNFLOWERBLUE)
                    lorelist += cmp("Shift-Left-Click:", MXColors.GRAY) + cmp("-10", MXColors.CORNFLOWERBLUE)
                }
            }
        }
    ) {
        ConfigController.getEntityTextureConfig(texture_name)!!.setLootingFactor(ConfigController.getEntityTextureConfig(texture_name)!!.getLootingFactor() + if (it.isLeftClick) if (it.isShiftClick) -10 else -1 else if (it.isShiftClick) +10 else +1)
        it.currentItem!!.meta { displayName(cmp("Looting Bonus: ", MXColors.GRAY) + cmp("${ConfigController.getEntityTextureConfig(texture_name)!!.getLootingFactor()}", MXColors.CORNFLOWERBLUE)) }
    }

    val gui = RyseInventory
        .builder()
        .rows(3)
        .title(texture_name.replace("_", " ").split(' ').joinToString(" ") { it.replaceFirstChar(Char::uppercaseChar) })
        .provider(
            object : InventoryProvider {
                override fun init(player: Player, contents: InventoryContents) {

                    contents.fill(itemStack(Material.GRAY_STAINED_GLASS_PANE) { meta { displayName(cmp(""))}})
                    contents.fillBorders(itemStack(Material.BLACK_STAINED_GLASS_PANE) { meta { displayName(cmp(""))}})

                    //  0 1  2  3  4  5  6  7  8
                    //  9 10 11 12 13 14 15 16 17
                    // 18 19 20 21 22 23 24 25 26


                    contents.set(0, backButton())
                    contents.set(11, enabledButton())
                    contents.set(13, dropChanceButton())
                    contents.set(15, lootingButton())
                }
            }
        ).build(KILLHEADS)

    init {
        gui.open(player)
    }
}