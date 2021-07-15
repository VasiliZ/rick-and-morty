package com.github.rtyvz.senla.tr.rick_and_morty.ui.characters

import android.app.ProgressDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.github.rtyvz.senla.tr.rick_and_morty.App
import com.github.rtyvz.senla.tr.rick_and_morty.R
import com.github.rtyvz.senla.tr.rick_and_morty.State
import com.github.rtyvz.senla.tr.rick_and_morty.common.PaginationScrollListener
import com.github.rtyvz.senla.tr.rick_and_morty.entity.CharacterEntity
import com.github.rtyvz.senla.tr.rick_and_morty.provider.TasksProvider
import com.github.rtyvz.senla.tr.rick_and_morty.ui.dialog.ErrorLoadListCharactersDialogFragment
import com.google.android.material.textview.MaterialTextView
import java.util.Collections.emptyList

class CharacterListFragment : Fragment() {
    private lateinit var characterRecyclerView: RecyclerView
    private lateinit var errorTextView: MaterialTextView
    private lateinit var swipeToRefresh: SwipeRefreshLayout
    private lateinit var localBroadcastManager: LocalBroadcastManager
    private lateinit var characterListReceiver: BroadcastReceiver
    private lateinit var characterLoadingErrorReceiver: BroadcastReceiver
    private val characterAdapter by lazy {
        CharacterAdapter(Glide.with(this)) {
            (activity as ActivityContract).clickOnCharacter(it)
        }
    }
    private var progress: ProgressDialog? = null
    private var isLoading = false
    private var isErrorLoad = false
    private var state: State? = null

    companion object {
        const val BROADCAST_CHARACTER_LIST = "local:BROADCAST_CHARACTER_LIST"
        const val BROADCAST_CHARACTER_LOADING_ERROR = "local:BROADCAST_CHARACTER_LOADING_ERROR"
        const val EXTRA_CHARACTER_LIST = "CHARACTER_LIST"
        const val EXTRA_CHARACTER_LOADING_ERROR = "CHARACTER_LOADING_ERROR"
        val TAG = CharacterListFragment::class.java.simpleName.toString()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.character_list_fragment, container, false)
        initFragmentState(view)
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val state = App.INSTANCE.state
        this.state = state
        if (state != null) {
            when {
                !state.isCharacterTaskRunning && state.characterEntityList.isEmpty() -> {
                    progress?.show()
                    TasksProvider.provideTaskForLoadCharacters(state.nextPage)
                }

                state.isCharacterTaskRunning -> progress?.show()
                else -> {
                    progress?.dismiss()
                    characterAdapter.setData(state.characterEntityList)
                }
            }

            swipeToRefresh.setOnRefreshListener {
                characterAdapter.clearData()
                App.INSTANCE.state = State()
                TasksProvider.provideTaskForLoadCharacters(App.INSTANCE.state?.nextPage ?: 0)
            }
        }

        characterRecyclerView
            .layoutManager?.onRestoreInstanceState(App.INSTANCE.state?.characterRecyclerState)

        initCharacterListReceiver()
        initLoadingErrorReceiver()
    }

    private fun initFragmentState(view: View?) {
        localBroadcastManager = LocalBroadcastManager.getInstance(requireContext())
        initRecycler(view)
        findViews(view)
        initProgress()
    }

    override fun onResume() {
        super.onResume()

        val state = App.INSTANCE.state
        if (state != null && state.characterEntityList.isEmpty() && !state.isCharacterTaskRunning) {
            progress?.show()
            TasksProvider.provideTaskForLoadCharacters(state.nextPage)
        }

        registerCharacterReceiver()
        registerLoadingErrorReceiver()
    }

    private fun findViews(view: View?) {
        if (view != null) {
            errorTextView = view.findViewById(R.id.errorTextView)
            swipeToRefresh = view.findViewById(R.id.refreshLayout)
        }
    }

    private fun initProgress() {
        progress = ProgressDialog(requireContext()).apply {
            setMessage(getString(R.string.character_list_fragment_wait_label))
            setCancelable(false)
            create()
        }
    }

    private fun registerCharacterReceiver() {
        localBroadcastManager.registerReceiver(
            characterListReceiver, IntentFilter(
                BROADCAST_CHARACTER_LIST
            )
        )
    }

    private fun registerLoadingErrorReceiver() {
        localBroadcastManager.registerReceiver(
            characterLoadingErrorReceiver, IntentFilter(
                BROADCAST_CHARACTER_LOADING_ERROR
            )
        )
    }

    private fun initCharacterListReceiver() {
        characterListReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val state = App.INSTANCE.state
                swipeToRefresh.isRefreshing = false

                if (state != null) {
                    isLoading = false
                    progress?.dismiss()
                    val data =
                        intent?.getParcelableArrayListExtra<CharacterEntity>(EXTRA_CHARACTER_LIST)
                            ?: emptyList()

                    if (state.nextPage > 1) {
                        characterAdapter.removeLoading()
                    }

                    state.nextPage++
                    App.INSTANCE.state?.characterEntityList?.addAll(data)
                    characterAdapter.setData(data)
                    displayData()
                }
            }
        }
    }

    private fun initLoadingErrorReceiver() {
        characterLoadingErrorReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                swipeToRefresh.isRefreshing = false
                val state = App.INSTANCE.state

                if (state != null) {
                    isLoading = false
                    isErrorLoad = true
                    progress?.dismiss()

                    if (state.characterEntityList.isNotEmpty()) {
                        characterAdapter.removeLoading()
                    }

                    ErrorLoadListCharactersDialogFragment().show(
                        parentFragmentManager,
                        ErrorLoadListCharactersDialogFragment.TAG
                    )
                }
            }
        }
    }

    private fun displayData() {
        errorTextView.isVisible = false
        characterRecyclerView.isVisible = true
    }

    private fun startLoading(state: State?) {
        isLoading = true

        if (state?.characterEntityList?.isNotEmpty() == true) {
            characterAdapter.addLoading()
        }

        TasksProvider.provideTaskForLoadCharacters(state?.nextPage ?: 0)
    }

    private fun initRecycler(view: View?) {
        if (view != null) {
            characterRecyclerView = view.findViewById(R.id.characterList)
            characterRecyclerView.adapter = characterAdapter
            characterRecyclerView.addOnScrollListener(object : PaginationScrollListener(
                characterRecyclerView.layoutManager as LinearLayoutManager
            ) {
                override fun isLoading() = isLoading

                override fun loadMoreItems() {
                    val state = App.INSTANCE.state
                    if (state != null && state.pageCount >= state.nextPage && !isErrorLoad) {
                        startLoading(state)
                    }
                }

                override fun isLastPage(): Boolean {
                    val state = App.INSTANCE.state

                    if (state != null) {
                        return state.pageCount <= state.nextPage
                    }

                    return false
                }
            })
        }
    }

    override fun onPause() {
        localBroadcastManager.unregisterReceiver(characterListReceiver)
        localBroadcastManager.unregisterReceiver(characterLoadingErrorReceiver)
        App.INSTANCE.state?.characterRecyclerState =
            characterRecyclerView.layoutManager?.onSaveInstanceState()

        super.onPause()
    }

    override fun onDestroyView() {
        progress?.dismiss()

        super.onDestroyView()
    }

    fun positiveAction() {
        isErrorLoad = false

        if (state?.characterEntityList?.isEmpty() == true) {
            progress?.show()
        }
        startLoading(state)
    }

    fun negativeAction() {
        if (state?.characterEntityList?.isEmpty() == true) {
            (activity as ActivityContract).closeActivity()
        }
    }
}

interface ActivityContract {
    fun clickOnCharacter(id: Long)
    fun closeActivity()
}
