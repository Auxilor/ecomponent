package com.willfp.ecomponent.components

import com.willfp.eco.core.gui.menu.Menu
import com.willfp.eco.core.gui.slot
import com.willfp.eco.core.gui.slot.Slot
import com.willfp.ecomponent.AutofillComponent
import com.willfp.ecomponent.GUIPosition
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import kotlin.math.ceil

/** The order of the level progression. */
private val progressionOrder = "123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray()

/** The state of each level. */
enum class LevelState(
    val key: String
) {
    /** Level is unlocked / complete. */
    UNLOCKED("unlocked"),

    /** Level is currently being worked on. */
    IN_PROGRESS("in-progress"),

    /** Level has yet to be worked on. */
    LOCKED("locked")
}

/** Component to display level progression, for Skills/Jobs/etc. */
@Suppress("MemberVisibilityCanBePrivate")
abstract class LevelComponent(
    pattern: List<String>,
    maxLevel: Int
) : AutofillComponent() {
    private val slots = mutableMapOf<Int, MutableMap<GUIPosition, Slot>>()

    override fun getSlotAt(row: Int, column: Int, player: Player, menu: Menu): Slot? {
        return slots[menu.getPage(player)]?.get(GUIPosition(row, column))
    }

    val levelsPerPage: Int
    val pages: Int

    init {
        val progressionSlots = mutableMapOf<Int, GUIPosition>()

        var x = 0
        for (row in pattern) {
            x++
            var y = 0
            for (char in row) {
                y++
                if (char == '0') {
                    continue
                }

                val pos = progressionOrder.indexOf(char)

                if (pos == -1) {
                    continue
                }

                progressionSlots[pos + 1] = GUIPosition(x, y)
            }
        }

        levelsPerPage = progressionSlots.size
        pages = ceil(maxLevel.toDouble() / levelsPerPage).toInt()

        for (page in 1..pages) {
            for ((levelOffset, position) in progressionSlots) {
                val level = ((page - 1) * levelsPerPage) + levelOffset

                if (level > maxLevel) {
                    continue
                }

                val pageSlots = slots[page] ?: mutableMapOf()

                pageSlots[position] = slot { player, menu ->
                    getLevelItem(
                        player,
                        menu,
                        level,
                        getLevelState(
                            player,
                            level
                        )
                    )
                }

                slots[page] = pageSlots
            }
        }
    }

    /** Get the item to be shown given a specific [level] and [levelState]. */
    abstract fun getLevelItem(player: Player, menu: Menu, level: Int, levelState: LevelState): ItemStack

    /** Get the state given a [player]'s [level]. */
    abstract fun getLevelState(player: Player, level: Int): LevelState
}
