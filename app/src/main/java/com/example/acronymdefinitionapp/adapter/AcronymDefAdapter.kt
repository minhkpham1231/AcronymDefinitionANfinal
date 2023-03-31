package com.example.acronymdefinitionapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.acronymdefinitionapp.databinding.DefinitionItemBinding
import com.example.acronymdefinitionapp.model.Lf

class AcronymDefAdapter(
    private val acronymData: MutableList<Lf> = mutableListOf()
) : RecyclerView.Adapter<DefinitionViewHolder>() {

    fun setDataDefinition(newData: List<Lf>) {
        acronymData.clear()
        acronymData.addAll(newData)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DefinitionViewHolder =
        DefinitionViewHolder(
            DefinitionItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: DefinitionViewHolder, position: Int) =
        holder.bind(acronymData[position])

    override fun getItemCount(): Int = acronymData.size
}

class DefinitionViewHolder(
    private val defBinding: DefinitionItemBinding
) : RecyclerView.ViewHolder(defBinding.root) {

    fun bind(definition: Lf) {
        defBinding.apply {
            definitionsName.text = definition.lf
        }
    }

}