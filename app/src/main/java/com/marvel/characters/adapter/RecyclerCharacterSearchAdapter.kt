package com.marvel.characters.adapter

import android.content.Context
import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.marvel.characters.R
import com.marvel.characters.databinding.ItemProgressLoadingBinding
import com.marvel.characters.databinding.ItemRecyclerCharacterSearchBinding
import com.marvel.characters.model.CharacterModel
import com.marvel.characters.observer.OnRecyclerItemClickListener
import hk.ids.gws.android.sclick.SClick


class RecyclerCharacterSearchAdapter(
    var characterModels: ArrayList<CharacterModel>,
    var onRecyclerItemClickListener: OnRecyclerItemClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit var context: Context
    val ItemViewData = 1
    val ItemViewProgress = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        context = parent.context
        if (viewType == ItemViewData) {
            val binding = DataBindingUtil.inflate(
                inflater,
                R.layout.item_recycler_character_search,
                parent,
                false
            ) as ItemRecyclerCharacterSearchBinding
            return ViewHolder(binding)
        } else {
            val binding = DataBindingUtil.inflate(
                inflater,
                R.layout.item_progress_loading,
                parent,
                false
            ) as ItemProgressLoadingBinding
            return ProgressViewHolder(binding)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (characterModels[position].holderType!!.isEmpty()) ItemViewData else ItemViewProgress
    }

    override fun getItemCount() = characterModels.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ProgressViewHolder)
            return

        var holder = holder as ViewHolder

//        characterModels[holder.adapterPosition].name =
//            characterModels[holder.adapterPosition].name?.replace(
//                keyWord,
//            "<span style=\"background-color: #730417\">$keyWord</span>",true
//            )

        holder.binding.model = characterModels[holder.adapterPosition]


        holder.binding.layoutItemRecyclerService.setOnClickListener {
            if (SClick.check(SClick.BUTTON_CLICK)) {
                onRecyclerItemClickListener.onRecyclerItemClickListener(position)
            }
        }

    }

    fun setList(list: ArrayList<CharacterModel>) {
        this.characterModels = list
        notifyDataSetChanged()
    }

    class ViewHolder(var binding: ItemRecyclerCharacterSearchBinding) :
        RecyclerView.ViewHolder(binding.root)

    class ProgressViewHolder(var binding: ItemProgressLoadingBinding) :
        RecyclerView.ViewHolder(binding.root)

}