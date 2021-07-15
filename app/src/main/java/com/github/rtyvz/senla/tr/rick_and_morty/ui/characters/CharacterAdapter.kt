package com.github.rtyvz.senla.tr.rick_and_morty.ui.characters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.github.rtyvz.senla.tr.rick_and_morty.R
import com.github.rtyvz.senla.tr.rick_and_morty.entity.CharacterEntity
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textview.MaterialTextView

class CharacterAdapter(
    private val glide: RequestManager,
    private val click: (Long) -> (Unit)
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
                    glide,
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.character_item, parent, false)
                ).apply {
                    itemView.setOnClickListener {
                        click(getItem(adapterPosition).id ?: 0L)
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
        return if (isLoaderVisible && position == characterList.size - 1) {
            LOADING_VIEW_TYPE
        } else {
            DATA_VIEW_TYPE
        }
    }

    override fun onViewRecycled(holder: BaseViewHolder) {
        holder.clear()

        super.onViewRecycled(holder)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bind(characterList[position])
    }

    override fun getItemCount() = characterList.size

    fun addLoading() {
        isLoaderVisible = true
        characterList.add(CharacterEntity())
        notifyItemInserted(characterList.size - 1)
    }

    fun removeLoading() {
        isLoaderVisible = false
        characterList.removeAt(characterList.size - 1)
        notifyItemRemoved(characterList.size - 1)
    }

    class CharacterHolder(private val glide: RequestManager, private val view: View) :
        BaseViewHolder(view) {
        private lateinit var nameTextView: MaterialTextView
        private lateinit var characterImage: ShapeableImageView

        override fun bind(data: CharacterEntity) {
            nameTextView = view.findViewById(R.id.characterName)
            characterImage = view.findViewById(R.id.characterImage)

            nameTextView.text = data.name
            glide
                .load(data.imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(characterImage)
        }

        override fun clear() {
            glide.clear(characterImage)
        }
    }

    fun setData(data: List<CharacterEntity>) {
        characterList.addAll(data)
        notifyDataSetChanged()
    }

    fun clearData(){
        characterList.clear()
        notifyDataSetChanged()
    }

    class LoadingViewHolder(view: View) : BaseViewHolder(view) {
        override fun bind(data: CharacterEntity) {}
        override fun clear() {}
    }

    abstract class BaseViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        abstract fun bind(data: CharacterEntity)
        abstract fun clear()
    }
}