package com.github.rtyvz.senla.tr.rick_and_morty.task

import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import bolts.Continuation
import bolts.Task
import com.github.rtyvz.senla.tr.rick_and_morty.App
import com.github.rtyvz.senla.tr.rick_and_morty.network.CharacterResponse
import com.github.rtyvz.senla.tr.rick_and_morty.network.Response
import com.github.rtyvz.senla.tr.rick_and_morty.ui.character.CharacterListFragment

class LoadCharacterPageTask {
    fun fetchCharactersByPage(pageId: Int) {
        val localBroadcastManager = LocalBroadcastManager.getInstance(App.INSTANCE)

        Task.callInBackground {
            App.state?.isCharacterTaskRunning = true
            App.api.getCharacters(pageId).execute().body()
        }.onSuccess(Continuation<Response?, List<CharacterResponse>> {
            return@Continuation it.result.listData
        }, Task.BACKGROUND_EXECUTOR)
            .continueWith(Continuation<List<CharacterResponse>, Nothing> {
                if (it.isFaulted) {
                    App.state?.isCharacterTaskRunning = false
                    return@Continuation null
                } else {
                    localBroadcastManager
                        .sendBroadcastSync(Intent(CharacterListFragment.BROADCAST_CHARACTER_LIST)
                            .apply {
                                putExtra(
                                    CharacterListFragment.EXTRA_CHARACTER_LIST,
                                    ArrayList(it.result.map { character ->
                                        character.toCharacterEntity()
                                    })
                                )
                            })
                    App.state?.isCharacterTaskRunning = false
                    return@Continuation null
                }
            }, Task.UI_THREAD_EXECUTOR)
    }
}