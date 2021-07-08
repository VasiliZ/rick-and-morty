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
    RecyclerView.Adapter<CharacterAdapter.CharacterHolder>() {
    private val characterList = mutableListOf<CharacterEntity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        CharacterHolder(
            glide,
            LayoutInflater.from(parent.context)
                .inflate(R.layout.character_item, parent, false)
        )


    override fun onBindViewHolder(holder: CharacterHolder, position: Int) {
        holder.bind(characterList[position])
    }

    override fun getItemCount() = characterList.size

    override fun onViewRecycled(holder: CharacterHolder) {
        holder.cleanUp()
    }

    class CharacterHolder(private val glide: RequestManager, private val view: View) :
        RecyclerView.ViewHolder(view) {
        private lateinit var image: ShapeableImageView
        private lateinit var nameTextView: MaterialTextView
        private lateinit var genderTextView: MaterialTextView
        private lateinit var statusTextView: MaterialTextView
        private lateinit var statusImageView: ShapeableImageView

        companion object {
            private const val ALIVE_STATUS = "Alive"
            private const val DEAD_STATUS = "Dead"
        }

        fun bind(data: CharacterEntity) {
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

        fun cleanUp() {
            glide.clear(image)
        }
    }

    fun setData(data: List<CharacterEntity>) {
        characterList.addAll(data)
        notifyDataSetChanged()
    }
}