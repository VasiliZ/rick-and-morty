package com.github.rtyvz.senla.tr.rick_and_morty.ui.characters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.github.rtyvz.senla.tr.rick_and_morty.R
import com.github.rtyvz.senla.tr.rick_and_morty.ui.entity.CharacterEntity
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textview.MaterialTextView

class CharacterAdapter(
    private val click: (Long?) -> (Unit)
) :
    RecyclerView.Adapter<CharacterAdapter.BaseViewHolder>() {
    private val characterList = mutableListOf<CharacterEntity>()
    private var isLoaderVisible = false

    companion object {
        private const val LOADING_VIEW_TYPE = 0
        private const val DATA_VIEW_TYPE = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return when (viewType) {
            DATA_VIEW_TYPE -> {
                CharacterHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.character_item, parent, false)
                ).apply {
                    itemView.setOnClickListener {
                        click(getItem(adapterPosition).id)
                    }
                }
            }
            else -> {
                LoadingViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.progress_item, parent, false)
                )
            }
        }
    }

    private fun getItem(position: Int) =
        characterList[position]


    override fun getItemViewType(position: Int): Int {
        return if (isLoaderVisible) {
            if (position == characterList.size - 1) LOADING_VIEW_TYPE else DATA_VIEW_TYPE
        } else {
            DATA_VIEW_TYPE
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bind(characterList[position])
    }

    override fun getItemCount() = characterList.size

    fun appLoading() {
        isLoaderVisible = true
        characterList.add(CharacterEntity())
        notifyItemInserted(characterList.size - 1)
    }

    fun removeLoading() {
        isLoaderVisible = false
        characterList.removeAt(characterList.size - 1)
        notifyItemRemoved(characterList.size - 1)
    }

    class CharacterHolder(private val view: View) :
        BaseViewHolder(view) {
        private lateinit var nameTextView: MaterialTextView
        private lateinit var genderTextView: MaterialTextView
        private lateinit var statusTextView: MaterialTextView
        private lateinit var locationTextView: MaterialTextView
        private lateinit var typeTextView: MaterialTextView
        private lateinit var statusImageView: ShapeableImageView

        companion object {
            private const val ALIVE_STATUS = "Alive"
            private const val DEAD_STATUS = "Dead"
        }

        override fun bind(data: CharacterEntity) {
            nameTextView = view.findViewById(R.id.characterName)
            genderTextView = view.findViewById(R.id.genderTextView)
            statusImageView = view.findViewById(R.id.statusImageView)
            statusTextView = view.findViewById(R.id.statusTextView)
            locationTextView = view.findViewById(R.id.locationTextView)
            typeTextView = view.findViewById(R.id.characterTypeTextView)

            nameTextView.text = data.name
            genderTextView.text = data.gender
            statusTextView.text = data.status
            statusImageView.setBackgroundColor(
                ResourcesCompat.getColor(
                    view.resources,
                    when (data.status) {
                        ALIVE_STATUS ->
                            R.color.alive_color
                        DEAD_STATUS -> R.color.dead_color
                        else -> R.color.unknown_color
                    },
                    null
                )
            )

            if (data.type.isNullOrBlank()) {
                typeTextView.visibility = View.GONE
            } else {
                typeTextView.visibility = View.VISIBLE
                typeTextView.text =
                    formatString(data.type, view, R.string.character_adapter_type)
            }

            locationTextView.text =
                formatString(data.location, view, R.string.character_adapter_location)
        }

        private fun formatString(value: String?, view: View, resourceId: Int) =
            String.format(view.context.getString(resourceId), value)
    }

    fun setData(data: List<CharacterEntity>) {
        characterList.addAll(data)
        notifyDataSetChanged()
    }

    class LoadingViewHolder(view: View) : BaseViewHolder(view) {
        override fun bind(data: CharacterEntity) {
        }
    }

    abstract class BaseViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        abstract fun bind(data: CharacterEntity)
    }
}