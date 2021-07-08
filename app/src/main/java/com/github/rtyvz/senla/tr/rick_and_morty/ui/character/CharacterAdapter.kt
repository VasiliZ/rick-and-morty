package com.github.rtyvz.senla.tr.rick_and_morty.ui.character

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.github.rtyvz.senla.tr.rick_and_morty.R
import com.github.rtyvz.senla.tr.rick_and_morty.ui.entity.CharacterEntity
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textview.MaterialTextView

class CharacterAdapter(private val glide: RequestManager) :
    RecyclerView.Adapter<CharacterAdapter.BaseViewHolder>() {
    private val characterList = mutableListOf<CharacterEntity>()
    private var isLoaderVisible = false

    companion object {
        private const val LOADING_VIEW_TYPE = 0
        private const val DATA_VIEW_TYPE = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return when (viewType) {
            DATA_VIEW_TYPE -> CharacterHolder(
                glide,
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.character_item, parent, false)
            )
            else -> {
                LoadingViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.progress_item, parent, false)
                )
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            characterList.size - 1 -> LOADING_VIEW_TYPE
            else -> DATA_VIEW_TYPE
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

    override fun onViewRecycled(holder: BaseViewHolder) {
        holder.cleanUp()
    }

    class CharacterHolder(private val glide: RequestManager, private val view: View) :
        BaseViewHolder(view) {
        private lateinit var image: ShapeableImageView
        private lateinit var nameTextView: MaterialTextView
        private lateinit var genderTextView: MaterialTextView
        private lateinit var statusTextView: MaterialTextView
        private lateinit var statusImageView: ShapeableImageView

        companion object {
            private const val ALIVE_STATUS = "Alive"
            private const val DEAD_STATUS = "Dead"
        }

        override fun bind(data: CharacterEntity) {
            image = view.findViewById(R.id.characterImage)
            nameTextView = view.findViewById(R.id.characterName)
            genderTextView = view.findViewById(R.id.genderTextView)
            statusImageView = view.findViewById(R.id.statusImageView)
            statusTextView = view.findViewById(R.id.statusTextView)

            glide.load(data.image)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .placeholder(R.drawable.ic_placeholder)
                .into(image)
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
        }

        override fun cleanUp() {
            glide.clear(image)
        }
    }

    fun setData(data: List<CharacterEntity>) {
        characterList.addAll(data)
        notifyDataSetChanged()
    }

    class LoadingViewHolder(view: View) : BaseViewHolder(view) {
        override fun bind(data: CharacterEntity) {
        }

        override fun cleanUp() {
        }

    }

    abstract class BaseViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        abstract fun bind(data: CharacterEntity)
        abstract fun cleanUp()
    }
}