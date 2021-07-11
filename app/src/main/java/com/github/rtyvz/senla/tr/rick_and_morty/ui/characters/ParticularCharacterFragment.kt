package com.github.rtyvz.senla.tr.rick_and_morty.ui.characters

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.constraintlayout.widget.Group
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.bumptech.glide.Glide
import com.github.rtyvz.senla.tr.rick_and_morty.R
import com.github.rtyvz.senla.tr.rick_and_morty.provider.TasksProvider
import com.github.rtyvz.senla.tr.rick_and_morty.ui.entity.CharacterEntity
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textview.MaterialTextView

class ParticularCharacterFragment : Fragment() {
    private lateinit var image: ShapeableImageView
    private lateinit var nameTextView: MaterialTextView
    private lateinit var genderTextView: MaterialTextView
    private lateinit var statusTextView: MaterialTextView
    private lateinit var locationTextView: MaterialTextView
    private lateinit var typeTextView: MaterialTextView
    private lateinit var statusImageView: ShapeableImageView
    private lateinit var characterReceiver: BroadcastReceiver
    private lateinit var characterErrorReceiver: BroadcastReceiver
    private lateinit var progress: ProgressBar
    private lateinit var groupView: Group
    private lateinit var error: MaterialTextView
    private lateinit var localBroadcastManager: LocalBroadcastManager
    private var characterId: Long = 0L

    companion object {
        private const val EXTRA_CHARACTER_ID = "CHARACTER_ID"
        private const val ALIVE_STATUS = "Alive"
        private const val DEAD_STATUS = "Dead"
        const val BROADCAST_SINGLE_CHARACTER = "local:BROADCAST_SINGLE_CHARACTER"
        const val BROADCAST_GET_CHARACTER_ERROR = "local:BROADCAST_GET_CHARACTER_ERROR"
        const val EXTRA_SINGLE_CHARACTER = "SINGLE_CHARACTER"
        const val EXTRA_GET_CHARACTER_ERROR = "GET_CHARACTER_ERROR"

        fun newInstance(characterId: Long?) =
            ParticularCharacterFragment().apply {
                if (characterId != null) {
                    this.characterId = characterId
                    arguments?.putLong(EXTRA_CHARACTER_ID, characterId)
                }
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.particular_character_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareToLoadCharacter(characterId, view)
    }

    override fun onResume() {
        super.onResume()

        registerCharacterReceiver()
        registerCharacterErrorReceiver()
    }

    private fun prepareToLoadCharacter(characterId: Long?, view: View?) {
        localBroadcastManager = LocalBroadcastManager.getInstance(requireContext())

        findViews(view)
        initCharacterReceiver()
        initCharacterErrorReceiver()

        if (characterId != null) {
            progress.isVisible = true
            groupView.isVisible = false
            TasksProvider.getCharacter(characterId)
        }
    }

    private fun initCharacterErrorReceiver() {
        characterErrorReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                progress.isVisible = false
                groupView.isVisible = false
                error.isVisible = true
                error.text = intent?.getStringExtra(EXTRA_GET_CHARACTER_ERROR)
            }
        }
    }

    private fun registerCharacterErrorReceiver() {
        localBroadcastManager.registerReceiver(
            characterErrorReceiver, IntentFilter(BROADCAST_GET_CHARACTER_ERROR)
        )
    }

    private fun registerCharacterReceiver() {
        localBroadcastManager.registerReceiver(
            characterReceiver, IntentFilter(BROADCAST_SINGLE_CHARACTER)
        )
    }

    private fun initCharacterReceiver() {
        characterReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (intent != null) {
                    progress.isVisible = false
                    groupView.isVisible = true
                    setData(intent.getParcelableExtra(EXTRA_SINGLE_CHARACTER))
                } else {
                    progress.isVisible = false
                    groupView.isVisible = false
                    error.isVisible = true
                    error.text = getString(R.string.particulat_character_fragment_data_is_empty)
                }
            }
        }
    }

    private fun setData(character: CharacterEntity?) {
        Glide
            .with(this)
            .load(character?.image)
            .into(image)
        nameTextView.text = character?.name
        genderTextView.text = character?.gender
        statusTextView.text = character?.status
        locationTextView.text = character?.location
        typeTextView.text = character?.type

        statusImageView.setBackgroundColor(
            ResourcesCompat.getColor(
                requireActivity().resources,
                when (character?.status) {
                    ALIVE_STATUS ->
                        R.color.alive_color
                    DEAD_STATUS -> R.color.dead_color
                    else -> R.color.unknown_color
                },
                null
            )
        )

        if (character?.type.isNullOrBlank()) {
            typeTextView.visibility = View.GONE
        } else {
            typeTextView.visibility = View.VISIBLE
            typeTextView.text =
                formatString(character?.type, R.string.character_adapter_type)
        }

        locationTextView.text =
            formatString(character?.location, R.string.character_adapter_location)
    }

    private fun formatString(value: String?, resourceId: Int) =
        String.format(getString(resourceId), value)

    private fun findViews(view: View?) {
        if (view != null) {
            image = view.findViewById(R.id.characterImage)
            nameTextView = view.findViewById(R.id.characterName)
            genderTextView = view.findViewById(R.id.genderTextView)
            statusImageView = view.findViewById(R.id.statusImageView)
            statusTextView = view.findViewById(R.id.statusTextView)
            locationTextView = view.findViewById(R.id.locationTextView)
            typeTextView = view.findViewById(R.id.characterTypeTextView)
            groupView = view.findViewById(R.id.viewsGroup)
            progress = view.findViewById(R.id.progress)
            error = view.findViewById(R.id.errorTextView)
        }
    }

    override fun onPause() {
        localBroadcastManager.unregisterReceiver(characterReceiver)
        localBroadcastManager.unregisterReceiver(characterErrorReceiver)

        super.onPause()
    }

    fun loadCharacterById(id: Long?) {
        prepareToLoadCharacter(id, view)
    }
}