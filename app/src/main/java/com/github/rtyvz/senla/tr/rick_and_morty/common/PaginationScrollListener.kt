package com.github.rtyvz.senla.tr.rick_and_morty.common

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class PaginationScrollListener(private val manager: LinearLayoutManager) :
    RecyclerView.OnScrollListener() {

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        val visibleItemCount = manager.childCount
        val totalItemCount = manager.itemCount
        val firstVisibleItemPosition = manager.findFirstVisibleItemPosition()

        if (!isLoading() && !isLastPage()
            && ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount)
        ) {
            loadMoreItems()
        }
    }

    abstract fun isLoading(): Boolean
    abstract fun loadMoreItems()
    abstract fun isLastPage(): Boolean
}