package com.dicoding.nadahasnim.mygithubpeoplelist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.nadahasnim.mygithubpeoplelist.adapter.ListPeopleAdapter
import com.dicoding.nadahasnim.mygithubpeoplelist.databinding.ActivityHomeBinding
import com.dicoding.nadahasnim.mygithubpeoplelist.model.ResponseListUsersItem
import com.dicoding.nadahasnim.mygithubpeoplelist.service.ResponseCall
import com.dicoding.nadahasnim.mygithubpeoplelist.service.Status
import com.dicoding.nadahasnim.mygithubpeoplelist.viewmodel.HomeViewModel

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private val homeViewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Github Users"

        binding.rvPeople.layoutManager = LinearLayoutManager(this)
        binding.rvPeople.setHasFixedSize(true)

        homeViewModel.let {
            it.listUsers.observe(this) { listUsers ->
                showRecyclerList(listUsers)
            }
            it.responseCall.observe(this) {request ->
                showLoadingIndicator(request)
            }
        }
    }

    private fun showLoadingIndicator(request: ResponseCall) {
        if (request.status == Status.LOADING) {
            binding.rvPeople.visibility = View.INVISIBLE
            binding.progressBar.visibility = View.VISIBLE
            return
        }

        if (request.status == Status.ERROR) {
            return
        }

        if (request.status == Status.COMPLETED) {
            binding.progressBar.visibility = View.INVISIBLE
            binding.rvPeople.visibility = View.VISIBLE
            return
        }
    }

    private fun showRecyclerList(list: List<ResponseListUsersItem>) {
        val listPeopleAdapter = ListPeopleAdapter(list)
        binding.rvPeople.adapter = listPeopleAdapter

        listPeopleAdapter.setOnItemClickCallback(object : ListPeopleAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ResponseListUsersItem) = openDetail(data)
        })
    }

    fun openDetail(data: ResponseListUsersItem) {
        Log.d(TAG, data.toString())

//        val moveToDetailIntent = Intent(this@HomeActivity, DetailActivity::class.java)
//        moveToDetailIntent.putExtra(DetailActivity.EXTRA_PEOPLE, data)
//        startActivity(moveToDetailIntent)
    }

    companion object {
        const val TAG = "Home Activity"
    }
}