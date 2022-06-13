package com.galboss.protorype.utils


import android.content.Context
import android.graphics.Canvas
import android.util.TypedValue
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.galboss.protorype.R
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator

abstract class SwipeHelperCallback(context: Context): ItemTouchHelper.SimpleCallback(0,
ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

    var editColor = ContextCompat.getColor(context,R.color.editColor)
    var deleteColor = ContextCompat.getColor(context,R.color.deleteItem)
    var blanco = ContextCompat.getColor(context,R.color.white)

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onChildDraw(
        c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean
    ) {
        RecyclerViewSwipeDecorator.Builder(c,recyclerView,viewHolder,dX,dY,actionState,isCurrentlyActive)
            .addPadding(1,5F,5F, 5F)
            .addCornerRadius(1,5)
            .addSwipeLeftBackgroundColor(editColor)
            .addSwipeLeftActionIcon(R.drawable.ic_outline_edit_24)
            .setSwipeLeftActionIconTint(blanco)
            .addSwipeLeftLabel("Editar")
            .setSwipeLeftLabelColor(blanco)
            .addSwipeRightBackgroundColor(deleteColor)
            .addSwipeRightActionIcon(R.drawable.ic_baseline_delete_sweep_24)
            .setSwipeRightActionIconTint(blanco)
            .addSwipeRightLabel("Borrar")
            .setSwipeRightLabelColor(blanco)
            .create().decorate()
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

    }
}