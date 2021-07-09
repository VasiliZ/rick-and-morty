package com.github.rtyvz.senla.tr.rick_and_morty.task

import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import bolts.Continuation
import bolts.Task
import com.github.rtyvz.senla.tr.rick_and_morty.App
import com.github.rtyvz.senla.tr.rick_and_morty.R
import com.github.rtyvz.senla.tr.rick_and_morty.ui.characters.ParticularCharacterFragment

class GetParticularCharacterTask {
    fun getCharacter(id: Long) {
        val localBroadcastManager = LocalBroadcastManager.getInstance(App.INSTANCE)
        Task.callInBackground {
            App.api.getCharacter(id).execute().body()
        }.continueWith(Continuation {
            if (it.isFaulted) {
                localBroadcastManager
                    .sendBroadcastSync(Intent(ParticularCharacterFragment.BROADCAST_GET_CHARACTER_ERROR)
                        .apply {
                            putExtra(
                                ParticularCharacterFragment.EXTRA_GET_CHARACTER_ERROR,
                                App.INSTANCE.getString(R.string.task_fetching_error)
                            )
                        })
                return@Continuation null
            } else {
                localBroadcastManager
                    .sendBroadcastSync(Intent(ParticularCharacterFragment.BROADCAST_SINGLE_CHARACTER)
                        .apply {
                            putExtra(
                                ParticularCharacterFragment.EXTRA_SINGLE_CHARACTER,
                                it.result?.toCharacterEntity()
                            )
                        })
                return@Continuation null
            }
        }, Task.UI_THREAD_EXECUTOR)
    }
}