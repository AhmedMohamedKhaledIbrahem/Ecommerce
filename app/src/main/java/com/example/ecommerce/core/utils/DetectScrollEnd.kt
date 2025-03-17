package com.example.ecommerce.core.utils

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerce.core.decoration.BottomSpacingDecoration

fun detectScrollEnd(
    recyclerView: RecyclerView,
    itemHeightDivisor: Int = 1,

    ) {
    val safeDivisor = if (itemHeightDivisor <= 0) 1 else itemHeightDivisor
    recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager ?: return
            val (visibleItemCount, totalItemCount, pastVisibleItems) = when (layoutManager) {
                is GridLayoutManager -> Triple(
                    layoutManager.childCount,
                    layoutManager.itemCount,
                    layoutManager.findFirstVisibleItemPosition()
                )

                is LinearLayoutManager -> Triple(
                    layoutManager.childCount,
                    layoutManager.itemCount,
                    layoutManager.findFirstVisibleItemPosition()
                )

                else -> return
            }
            if ((visibleItemCount + pastVisibleItems) >= totalItemCount && dy > 0) {
                recyclerView.post {
                    val lastView =
                        recyclerView.findViewHolderForAdapterPosition(totalItemCount - 1)?.itemView
                    lastView?.let {

                        val lastItemHeight = it.height / safeDivisor

                        removeItemDecoration(recyclerView)

                        recyclerView.addItemDecoration(BottomSpacingDecoration(lastItemHeight))
                        recyclerView.clipToPadding = false
                    }
                }
            }
        }
    })
}

private fun removeItemDecoration(recyclerView: RecyclerView) {
    for (i in 0 until recyclerView.itemDecorationCount) {
        val decoration = recyclerView.getItemDecorationAt(i)
        if (decoration is BottomSpacingDecoration) {
            recyclerView.removeItemDecoration(decoration)
            break
        }
    }
}