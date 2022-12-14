package com.willfp.ecomponent.components

import com.willfp.eco.core.gui.GUIComponent
import com.willfp.eco.core.gui.menu.Menu
import com.willfp.eco.core.gui.slot.Slot
import com.willfp.ecomponent.AutofillComponent
import org.bukkit.entity.Player

/** A line of identical slots. */
private class LineComponent(
    private val direction: LineDirection,
    private val maxSize: Int,
    private val slot: Slot?
) : AutofillComponent() {
    override fun getColumns() =
        if (direction == LineDirection.HORIZONTAL) maxColumns.coerceAtMost(maxSize) else 1

    override fun getRows() =
        if (direction == LineDirection.VERTICAL) maxRows.coerceAtMost(maxSize) else 1

    override fun getSlotAt(row: Int, column: Int, player: Player, menu: Menu) =
        slot
}

/** A direction for a line to go in. */
private enum class LineDirection {
    VERTICAL,
    HORIZONTAL
}


/**
 * Create a vertical line of identical [slot]s. Takes the maximum height
 * possible.
 */
fun verticalLine(slot: Slot?): GUIComponent = verticalLine(Int.MAX_VALUE, slot)

/** Create a vertical line of identical [slot]s, with a [maxSize]. */
fun verticalLine(maxSize: Int, slot: Slot?): GUIComponent = LineComponent(LineDirection.VERTICAL, maxSize, slot)


/**
 * Create a horizontal line of identical [slot]s. Takes the maximum height
 * possible.
 */
fun horizontalLine(slot: Slot?): GUIComponent = horizontalLine(Int.MAX_VALUE, slot)

/** Create a horizontal line of identical [slot]s, with a [maxSize]. */
fun horizontalLine(maxSize: Int, slot: Slot?): GUIComponent =
    LineComponent(LineDirection.HORIZONTAL, maxSize, slot)


/**
 * Create a rectangle of identical [slot]s. Fills as much space as
 * possible.
 */
fun rectangle(slot: Slot): GUIComponent = rectangle(Int.MAX_VALUE, Int.MAX_VALUE, slot)

/**
 * Create a rectangle of identical [slot]s. Fills up to a [maxHeight] and
 * [maxWidth].
 */
fun rectangle(maxHeight: Int, maxWidth: Int, slot: Slot): GUIComponent = object : AutofillComponent() {
    override fun getColumns() = maxColumns.coerceAtMost(maxWidth)

    override fun getRows() = maxRows.coerceAtMost(maxHeight)

    override fun getSlotAt(row: Int, column: Int, player: Player, menu: Menu) = slot
}
