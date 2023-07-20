package com.example.newapppp.ui.home

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

abstract class SwipeToDelete: ItemTouchHelper.Callback() {
    //опеределяет флаги перемещения элементов в RecyclerView
    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        val swipeFlag = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        //создаёт флаги смахивания без перемещения по вертикали,
        //но флаги для перемещения у него 0
        return makeMovementFlags(0, swipeFlag)
    }
    //опеределяет перетаскивание внутри RecyclerView
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        //перемещение элементов не разрешено
        return false
    }
}