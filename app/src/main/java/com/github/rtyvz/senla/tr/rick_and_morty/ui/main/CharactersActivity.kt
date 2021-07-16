package com.github.rtyvz.senla.tr.rick_and_morty.ui.main

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.FragmentManager
import com.github.rtyvz.senla.tr.rick_and_morty.App
import com.github.rtyvz.senla.tr.rick_and_morty.R
import com.github.rtyvz.senla.tr.rick_and_morty.State
import com.github.rtyvz.senla.tr.rick_and_morty.ui.characters.ActivityContract
import com.github.rtyvz.senla.tr.rick_and_morty.ui.characters.CharacterListFragment
import com.github.rtyvz.senla.tr.rick_and_morty.ui.dialog.HandleClickErrorLoadCharactersDialog
import com.github.rtyvz.senla.tr.rick_and_morty.ui.dialog.HandleClickErrorLoadSingleCharacter
import com.github.rtyvz.senla.tr.rick_and_morty.ui.particularCharacter.HandleNegativeButtonDialogClick
import com.github.rtyvz.senla.tr.rick_and_morty.ui.particularCharacter.HomeButtonBehaviorContract
import com.github.rtyvz.senla.tr.rick_and_morty.ui.particularCharacter.ParticularCharacterFragment

class CharactersActivity : AppCompatActivity(), ActivityContract, HandleNegativeButtonDialogClick,
    HandleClickErrorLoadSingleCharacter,
    HandleClickErrorLoadCharactersDialog, HomeButtonBehaviorContract {
    private var dataContainer: FragmentContainerView? = null

    companion object {
        private const val BACK_STACK_ITEM_NAME = "BACK_STACK_ITEM_NAME"
        private const val BACK_STACK_LIST_CHARACTERS_ITEM_NAME =
            "BACK_STACK_LIST_CHARACTERS_ITEM_NAME"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.character_activity)

        dataContainer = findViewById(R.id.characterDataContainer)
        setSupportActionBar(findViewById(R.id.toolBar))
        val state = App.INSTANCE.state
        createFragmentsFromOrientation(state)

        if (state == null) {
            App.INSTANCE.state = State()
        }
    }

    private fun createFragmentsFromOrientation(state: State?) {
        if (state == null) {
            addFragment()
        }

        if (isDataContainerAvailable()) {
            createFragment(
                R.id.characterDataContainer,
                ParticularCharacterFragment.newInstance(App.INSTANCE.state?.lastOpenedCharacterId),
                ParticularCharacterFragment.TAG
            )
        }

        if (App.INSTANCE.state?.lastOpenedCharacterId != 0L && App.INSTANCE.state != null
            && !isDataContainerAvailable()
        ) {
            supportFragmentManager.popBackStack(
                BACK_STACK_ITEM_NAME,
                FragmentManager.POP_BACK_STACK_INCLUSIVE
            )
            createFragment(
                R.id.characterListContainer,
                ParticularCharacterFragment.newInstance(
                    App.INSTANCE.state?.lastOpenedCharacterId
                ),
                ParticularCharacterFragment.TAG
            )
        }
    }

    private fun addFragment() {
        supportFragmentManager
            .beginTransaction()
            .add(R.id.characterListContainer, CharacterListFragment(), CharacterListFragment.TAG)
            .addToBackStack(BACK_STACK_LIST_CHARACTERS_ITEM_NAME)
            .commit()
    }

    private fun createFragment(containerViewId: Int, fragment: Fragment, tag: String) {
        supportFragmentManager.popBackStack(
            BACK_STACK_ITEM_NAME,
            FragmentManager.POP_BACK_STACK_INCLUSIVE
        )
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(containerViewId, fragment, tag)
        transaction.addToBackStack(BACK_STACK_ITEM_NAME)
        transaction.commit()
    }

    private fun isDataContainerAvailable() = dataContainer != null

    override fun onBackPressed() {
        when (supportFragmentManager.backStackEntryCount) {
            1 -> {
                App.INSTANCE.state = null
                finish()
            }
            else -> {
                App.INSTANCE.state?.lastOpenedCharacterId = 0L
                supportFragmentManager.popBackStack(
                    BACK_STACK_ITEM_NAME,
                    FragmentManager.POP_BACK_STACK_INCLUSIVE
                )
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                return when (supportFragmentManager.backStackEntryCount) {
                    1 -> {
                        App.INSTANCE.state = null
                        finish()
                        true
                    }
                    else -> {
                        App.INSTANCE.state?.lastOpenedCharacterId = 0L
                        supportFragmentManager.popBackStack(
                            BACK_STACK_ITEM_NAME,
                            FragmentManager.POP_BACK_STACK_INCLUSIVE
                        )
                        true
                    }
                }
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun clickOnCharacter(id: Long) {
        if (isDataContainerAvailable()) {
            val fragment = supportFragmentManager.findFragmentByTag(ParticularCharacterFragment.TAG)
            if (fragment is ParticularCharacterFragment) {
                fragment.loadCharacterById(id)
            } else {
                createFragment(
                    R.id.characterDataContainer,
                    ParticularCharacterFragment.newInstance(id),
                    ParticularCharacterFragment.TAG
                )
            }
        } else {
            createFragment(
                R.id.characterListContainer,
                ParticularCharacterFragment.newInstance(id),
                ParticularCharacterFragment.TAG
            )
        }
    }

    override fun closeActivity() {
        App.INSTANCE.state = null
        finish()
    }

    override fun popBackStack() {
        supportFragmentManager.popBackStack(
            BACK_STACK_ITEM_NAME,
            FragmentManager.POP_BACK_STACK_INCLUSIVE
        )
    }

    override fun handlePositiveActionErrorLoadListCharactersDialog() {
        val fragment = supportFragmentManager.findFragmentByTag(CharacterListFragment.TAG)
        if (fragment is CharacterListFragment) {
            fragment.positiveAction()
        }
    }

    override fun handleNegativeActionErrorLoadListCharacterDialog() {
        val fragment = supportFragmentManager.findFragmentByTag(CharacterListFragment.TAG)
        if (fragment is CharacterListFragment) {
            fragment.negativeAction()
        }
    }

    override fun handlePositiveActionErrorLoadSingleCharacterDialog() {
        val fragment = supportFragmentManager.findFragmentByTag(ParticularCharacterFragment.TAG)
        if (fragment is ParticularCharacterFragment) {
            fragment.positiveAction()
        }
    }

    override fun handleNegativeActionErrorLoadSingleCharacterDialog() {
        val fragment = supportFragmentManager.findFragmentByTag(ParticularCharacterFragment.TAG)
        if (fragment is ParticularCharacterFragment) {
            fragment.negativeAction()
        }
    }

    override fun enableHomeButton() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
    }

    override fun disableHomeButton() {
        if (!isDataContainerAvailable()) {
            supportActionBar?.setDisplayHomeAsUpEnabled(false)
            supportActionBar?.setHomeButtonEnabled(false)
        }
    }
}