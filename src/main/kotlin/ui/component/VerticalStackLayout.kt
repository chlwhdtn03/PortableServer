package ui.component

import java.awt.Component
import java.awt.Container
import java.awt.Dimension
import java.awt.Insets
import java.awt.LayoutManager2


class VerticalStackLayout : LayoutManager2 {
    private val components: MutableList<Component> = ArrayList<Component>()
    override fun addLayoutComponent(comp: Component, constraints: Any?) {
        components.add(comp)
    }

    override fun addLayoutComponent(name: String?, comp: Component) {
        components.add(comp)
    }

    override fun removeLayoutComponent(comp: Component) {
        components.remove(comp)
    }

    override fun preferredLayoutSize(parent: Container): Dimension {
        var prefWidth = 0
        var prefHeight = 0
        for (comp in components) {
            val compPref: Dimension = comp.preferredSize
            prefWidth = prefWidth.coerceAtLeast(compPref.width)
            prefHeight += compPref.height
        }
        val insets: Insets = parent.insets
        return Dimension(
            prefWidth + insets.left + insets.right,
            prefHeight + insets.top + insets.bottom
        )
    }

    override fun minimumLayoutSize(parent: Container): Dimension {
        var minWidth = 0
        var minHeight = 0
        for (comp in components) {
            val compPref: Dimension = comp.minimumSize
            minWidth = minWidth.coerceAtLeast(compPref.width)
            minHeight += compPref.height
        }
        val insets: Insets = parent.insets
        return Dimension(
            minWidth + insets.left + insets.right,
            minHeight + insets.top + insets.bottom
        )
    }

    override fun maximumLayoutSize(parent: Container): Dimension {
        var maxWidth = 0
        var maxHeight = 0
        for (comp in components) {
            val compPref: Dimension = comp.maximumSize
            maxWidth = Math.max(maxWidth, compPref.width)
            maxHeight += compPref.height
        }
        val insets: Insets = parent.insets
        return Dimension(
            maxWidth + insets.left + insets.right,
            maxHeight + insets.top + insets.bottom
        )
    }

    override fun getLayoutAlignmentX(target: Container?): Float {
        return 0F
    }

    override fun getLayoutAlignmentY(target: Container?): Float {
        return 0F
    }

    override fun invalidateLayout(target: Container?) {}
    override fun layoutContainer(parent: Container) {
        val insets: Insets = parent.insets
        var y = insets.top
        val width: Int = parent.getWidth() - insets.left - insets.right
        val height: Int = parent.getHeight() - insets.top - insets.bottom
        for (comp in components) {
            val compPref: Dimension = comp.preferredSize
            val heightLeft = height - y
            val compHeight = heightLeft.coerceAtMost(compPref.height)
            comp.setBounds(insets.left, y, width, compHeight)
            y += compPref.height
        }
    }
}