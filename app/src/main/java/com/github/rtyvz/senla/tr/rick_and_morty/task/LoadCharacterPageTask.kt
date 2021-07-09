package com.github.rtyvz.senla.tr.rick_and_morty.task

import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import bolts.Continuation
import bolts.Task
import com.github.rtyvz.senla.tr.rick_and_morty.App
import com.github.rtyvz.senla.tr.rick_and_morty.R
import com.github.rtyvz.senla.tr.rick_and_morty.network.Response
import com.github.rtyvz.senla.tr.rick_and_morty.ui.characters.CharacterListFragment

class LoadCharacterPageTask {
    fun fetchCharactersByPage(pageId: Int) {
        val localBroadcastManager = LocalBroadcastManager.getInstance(App.INSTANCE)

        Task.callInBackground {
            App.INSTANCE.state?.isCharacterTaskRunning = true
            App.api.getCharacters(pageId).execute().body()
        }.onSuccess(Continuation<Response?, Response> {
            return@Continuation it.result
        }, Task.BACKGROUND_EXECUTOR)
            .continueWith(Continuation<Response, Nothing> {
                if (it.isFaulted) {
                    App.INSTANCE.state?.isCharacterTaskRunning = false
                    localBroadcastManager
                        .sendBroadcastSync(Intent(CharacterListFragment.BROADCAST_CHARACTER_LOADING_ERROR)
                            .apply {
                                putExtra(
                                    CharacterListFragment.EXTRA_CHARACTER_LOADING_ERROR,
                                    App.INSTANCE.getString(
                                        R.string.task_fetching_error
                                    )
                                )
                            })
                    return@Continuation null
                } else {
                    val response = it.result
                    App.INSTANCE.state?.pageCount = response.info.pages
                    localBroadcastManager
                        .sendBroadcastSync(Intent(CharacterListFragment.BROADCAST_CHARACTER_LIST)
                            .apply {
                                putExtra(
                                    CharacterListFragment.EXTRA_CHARACTER_LIST,
                                    ArrayList(response.listData.map { character ->
                                        character.toCharacterEntity()
                                    })
                                )
                            })
                    App.INSTANCE.state?.isCharacterTaskRunning = false
                    return@Continuation null
                }
            }, Task.UI_THREAD_EXECUTOR)
    }
}