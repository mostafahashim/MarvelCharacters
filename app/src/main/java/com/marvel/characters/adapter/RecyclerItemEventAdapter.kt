package com.marvel.characters.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.marvel.characters.R
import com.marvel.characters.databinding.ItemProgressLoadingBinding
import com.marvel.characters.databinding.ItemRecyclerCharacterBinding
import com.marvel.characters.databinding.ItemRecyclerEventBinding
import com.marvel.characters.model.CharacterModel
import com.marvel.characters.model.ItemModel
import com.marvel.characters.observer.OnRecyclerItemClickListener
import hk.ids.gws.android.sclick.SClick

class RecyclerItemEventAdapter(
    var columnWidth: Double,
    var itemModels: ArrayList<ItemModel>,
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
                R.layout.item_recycler_event,
                parent,
                false
            ) as ItemRecyclerEventBinding
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
        return if (itemModels[position].holderType!!.isEmpty()) ItemViewData else ItemViewProgress
    }

    override fun getItemCount() = itemModels.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        itemModels[holder.adapterPosition].columnWidth = columnWidth
        itemModels[holder.adapterPosition].columnHeight = columnWidth * 2

        if (holder is ProgressViewHolder)
            return

        var holder = holder as ViewHolder

        holder.binding.model = itemModels[holder.adapterPosition]

        holder.binding.layoutItemRecyclerService.setOnClickListener {
            if (SClick.check(SClick.BUTTON_CLICK)) {
                onRecyclerItemClickListener.onRecyclerItemClickListener(position)
            }
        }

    }

    fun setList(list: ArrayList<ItemModel>) {
        this.itemModels = list
        notifyDataSetChanged()
    }

    fun setColumnWidthAndRatio(columnWidth: Double) {
        this.columnWidth = columnWidth
    }


    class ViewHolder(var binding: ItemRecyclerEventBinding) :
        RecyclerView.ViewHolder(binding.root)

    class ProgressViewHolder(var binding: ItemProgressLoadingBinding) :
        RecyclerView.ViewHolder(binding.root)

}