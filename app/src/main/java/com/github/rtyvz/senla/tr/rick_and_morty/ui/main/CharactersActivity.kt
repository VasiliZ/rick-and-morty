package com.github.rtyvz.senla.tr.rick_and_morty.ui.main

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import com.github.rtyvz.senla.tr.rick_and_morty.App
import com.github.rtyvz.senla.tr.rick_and_morty.R
import com.github.rtyvz.senla.tr.rick_and_morty.State
import com.github.rtyvz.senla.tr.rick_and_morty.ui.characters.CharacterListFragment
import com.github.rtyvz.senla.tr.rick_and_morty.ui.characters.ClickOnCharacterContract
import com.github.rtyvz.senla.tr.rick_and_morty.ui.particularCharacter.ParticularCharacterFragment

class CharactersActivity : AppCompatActivity(), ClickOnCharacterContract {
    private var dataContainer: FragmentContainerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.character_activity)

        dataContainer = findViewById(R.id.characterDataContainer)
        val state = App.INSTANCE.state

        if (state == null) {
            App.INSTANCE.state = State()
        }

        createFragmentFromOrientation()

        actionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun createFragmentFromOrientation() {
        createFragment(R.id.characterListContainer, CharacterListFragment())

        if (isDataContainerAvailable()) {
            createFragment(R.id.characterDataContainer, ParticularCharacterFragment())
        }
    }

    private fun createFragment(fragmentId: Int, fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(fragmentId, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun isDataContainerAvailable() = dataContainer != null

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

    override fun clickOnCharacter(id: Long) {
        if (isDataContainerAvailable()) {
            val fragment = supportFragmentManager.findFragmentById(R.id.characterDataContainer)
            if (fragment == null) {
                createFragment(
                    R.id.characterDataContainer,
                    ParticularCharacterFragment.newInstance(0L)
                )
            } else {
                (fragment as ParticularCharacterFragment).loadCharacterById(id)
            }
        } else {
            createFragment(R.id.characterListContainer, ParticularCharacterFragment.newInstance(id))
        }
    }
}