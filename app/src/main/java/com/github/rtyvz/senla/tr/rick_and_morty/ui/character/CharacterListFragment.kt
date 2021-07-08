package com.github.rtyvz.senla.tr.rick_and_morty.ui.character

import android.app.ProgressDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.github.rtyvz.senla.tr.rick_and_morty.App
import com.github.rtyvz.senla.tr.rick_and_morty.R
import com.github.rtyvz.senla.tr.rick_and_morty.common.PaginationScrollListener
import com.github.rtyvz.senla.tr.rick_and_morty.provider.TasksProvider
import com.github.rtyvz.senla.tr.rick_and_morty.ui.entity.CharacterEntity
import java.util.Collections.emptyList

class CharacterListFragment : Fragment() {
    private lateinit var characterRecyclerView: RecyclerView
    private lateinit var localBroadcastManager: LocalBroadcastManager
    private lateinit var characterListReceiver: BroadcastReceiver
    private val characterAdapter by lazy {
        CharacterAdapter(Glide.with(this))
    }
    private var progress: ProgressDialog? = null
    private var pageForLoad = 1
    private var isLoading = false
    private var isLastPage = false

    companion object {
        val TAG: String = CharacterListFragment::class.simpleName.toString()
        const val BROADCAST_CHARACTER_LIST = "local:BROADCAST_CHARACTER_LIST"
        const val EXTRA_CHARACTER_LIST = "CHARACTER_LIST"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.character_list_fragment, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        localBroadcastManager = LocalBroadcastManager.getInstance(requireContext())

        initRecycler(view)
        initProgress()
        initCharacterListReceiver()
    }

    override fun onResume() {
        super.onResume()

        val state = App.state
        if (state != null) {
            if (!state.isCharacterTaskRunning && state.data.isEmpty()) {
                progress?.show()
                TasksProvider.provideTaskForLoadCharacters(pageForLoad)
            } else {
                progress?.dismiss()
                characterAdapter.setData(state.data)
            }
        }

        characterRecyclerView.layoutManager?.onRestoreInstanceState(App.state?.characterRecyclerState)

        registerCharacterReceiver()
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

    private fun initCharacterListReceiver() {
        characterListReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                pageForLoad++
                isLoading = false
                progress?.dismiss()
                val data =
                    intent?.getParcelableArrayListExtra<CharacterEntity>(EXTRA_CHARACTER_LIST)
                        ?: emptyList()
                App.state?.data?.addAll(data)
                characterAdapter.setData(data)
            }
        }
    }

    private fun initRecycler(view: View) {
        characterRecyclerView = view.findViewById(R.id.characterList)
        characterRecyclerView.adapter = characterAdapter
        characterRecyclerView.addOnScrollListener(object : PaginationScrollListener(
            characterRecyclerView.layoutManager as LinearLayoutManager
        ) {
            override fun isLoading() = isLoading

            override fun loadMoreItems() {
                isLoading = true
                TasksProvider.provideTaskForLoadCharacters(pageForLoad)
            }

            override fun isLastPage() = isLastPage
        })
    }

    fun checkRunningTask() {
        if (App.state?.isCharacterTaskRunning == false) {
            progress?.show()
        } else {
            progress?.dismiss()
        }
    }

    override fun onPause() {
        localBroadcastManager.unregisterReceiver(characterListReceiver)
        App.state?.characterRecyclerState =
            characterRecyclerView.layoutManager?.onSaveInstanceState()

        super.onPause()
    }

    override fun onDestroy() {
        progress?.dismiss()

        super.onDestroy()
    }
}