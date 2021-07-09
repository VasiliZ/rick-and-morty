package com.github.rtyvz.senla.tr.rick_and_morty.ui.main

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.github.rtyvz.senla.tr.rick_and_morty.App
import com.github.rtyvz.senla.tr.rick_and_morty.R
import com.github.rtyvz.senla.tr.rick_and_morty.State
import com.github.rtyvz.senla.tr.rick_and_morty.ui.characters.CharacterListFragment

class CharactersActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.character_activity)

        val state = App.INSTANCE.state

        if (state == null) {
            App.INSTANCE.state = State()
        }

        replaceFragment(R.id.characterListContainer)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setHomeButtonEnabled(true)
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                return when (supportFragmentManager.backStackEntryCount) {
                    1 -> {
                        finish()
                        true
                    }
                    else -> {
                        supportFragmentManager.popBackStack()
                        true
                    }
                }
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}