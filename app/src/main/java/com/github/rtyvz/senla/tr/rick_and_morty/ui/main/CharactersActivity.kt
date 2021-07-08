package com.github.rtyvz.senla.tr.rick_and_morty.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.rtyvz.senla.tr.rick_and_morty.App
import com.github.rtyvz.senla.tr.rick_and_morty.R
import com.github.rtyvz.senla.tr.rick_and_morty.State
import com.github.rtyvz.senla.tr.rick_and_morty.ui.character.CharacterListFragment

class CharactersActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.character_activity)

        val state = App.state

        if (state == null) {
            App.state = State()
        }

        replaceFragment(R.id.characterListContainer)
    }

    private fun replaceFragment(container: Int) {
        val fragment = supportFragmentManager.findFragmentByTag(CharacterListFragment.TAG)

        supportFragmentManager.beginTransaction()
            .replace(
                container, when (fragment) {
                    null -> CharacterListFragment()
                    else -> {
                        (fragment as CharacterListFragment).checkRunningTask()
                        fragment
                    }
                }
            )
            .addToBackStack(null)
            .commit()
    }
}