package com.dicoding.nadahasnim.mygithubpeoplelist.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.nadahasnim.mygithubpeoplelist.DetailActivity
import com.dicoding.nadahasnim.mygithubpeoplelist.adapter.ListPeopleAdapter
import com.dicoding.nadahasnim.mygithubpeoplelist.databinding.FragmentFollowersBinding
import com.dicoding.nadahasnim.mygithubpeoplelist.model.People
import com.dicoding.nadahasnim.mygithubpeoplelist.model.ResponseListUsersItem
import com.dicoding.nadahasnim.mygithubpeoplelist.viewmodel.DetailViewModel

class FollowersFragment : Fragment() {

    private val detailViewModel: DetailViewModel by activityViewModels()

    private lateinit var binding: FragmentFollowersBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFollowersBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvFollowers.layoutManager = LinearLayoutManager(activity)
        binding.rvFollowers.setHasFixedSize(true)

        detailViewModel.followers.observe(viewLifecycleOwner) { followers ->
            val listPeopleAdapter = ListPeopleAdapter(followers)
            binding.rvFollowers.adapter = listPeopleAdapter
            listPeopleAdapter.setOnItemClickCallback(object :
                ListPeopleAdapter.OnItemClickCallback {
                override fun onItemClicked(data: ResponseListUsersItem) {
                    openDetail(data)
                }
            })
        }
    }

    fun openDetail(data: ResponseListUsersItem) {
        Log.d(TAG, data.toString())

        val moveToDetailIntent = Intent(activity, DetailActivity::class.java)
        moveToDetailIntent.putExtra(
            DetailActivity.EXTRA_PEOPLE, People(
                data.login,
                data.htmlUrl,
            )
        )
        startActivity(moveToDetailIntent)
    }

    companion object {
        const val TAG = "FollowersFragment"
    }
}