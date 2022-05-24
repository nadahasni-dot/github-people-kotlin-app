package com.dicoding.nadahasnim.mygithubpeoplelist.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.nadahasnim.mygithubpeoplelist.databinding.ItemRowPeopleBinding
import com.dicoding.nadahasnim.mygithubpeoplelist.model.People

class ListPeopleAdapter(private val listPeople: ArrayList<People>) :
    RecyclerView.Adapter<ListPeopleAdapter.ListViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding =
            ItemRowPeopleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (username, name, avatar, company) = listPeople[position]

        holder.binding.tvName.text = name
        holder.binding.tvUsername.text = username
        holder.binding.tvCompany.text = company
        holder.binding.ivAvatar.setImageResource(avatar)

        holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(listPeople[position]) }
    }

    override fun getItemCount(): Int = listPeople.size

    class ListViewHolder(val binding: ItemRowPeopleBinding) :
        RecyclerView.ViewHolder(binding.root)

    interface OnItemClickCallback {
        fun onItemClicked(data: People)
    }
}