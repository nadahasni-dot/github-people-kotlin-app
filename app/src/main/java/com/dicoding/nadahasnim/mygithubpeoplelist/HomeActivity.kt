package com.dicoding.nadahasnim.mygithubpeoplelist

import android.app.SearchManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.core.content.getSystemService
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.nadahasnim.mygithubpeoplelist.adapter.ListPeopleAdapter
import com.dicoding.nadahasnim.mygithubpeoplelist.databinding.ActivityHomeBinding
import com.dicoding.nadahasnim.mygithubpeoplelist.model.People
import com.dicoding.nadahasnim.mygithubpeoplelist.model.ResponseListUsersItem
import com.dicoding.nadahasnim.mygithubpeoplelist.service.ResponseCall
import com.dicoding.nadahasnim.mygithubpeoplelist.service.Status
import com.dicoding.nadahasnim.mygithubpeoplelist.viewmodel.HomeViewModel

class HomeActivity : AppCompatActivity() {

    private var binding: ActivityHomeBinding? = null
    private val homeViewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        supportActionBar?.title = "Github Users"

        binding?.rvPeople?.layoutManager = LinearLayoutManager(this)
        binding?.rvPeople?.setHasFixedSize(true)

        homeViewModel.let {
            it.listUsers.observe(this) { listUsers ->
                showRecyclerList(listUsers)
            }
            it.responseCall.observe(this) { request ->
                showLoadingIndicator(request)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu)

        val searchManager = getSystemService<SearchManager>()
        val searchView = menu.findItem(R.id.search).actionView as SearchView

        searchView.setSearchableInfo(searchManager?.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    homeViewModel.searchUsers(query)
                } else {
                    homeViewModel.fetchAllUsers()
                }

                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        return true
    }

    private fun showLoadingIndicator(request: ResponseCall) {
        if (request.status == Status.LOADING) {
            binding?.errorLayout?.root?.visibility = View.INVISIBLE
            binding?.rvPeople?.visibility = View.INVISIBLE
            binding?.progressBar?.visibility = View.VISIBLE
            return
        }

        if (request.status == Status.ERROR) {
            binding?.errorLayout?.tvError?.text = request.message
            binding?.errorLayout?.btnReload?.setOnClickListener {
                homeViewModel.fetchAllUsers()
            }

            binding?.rvPeople?.visibility = View.INVISIBLE
            binding?.progressBar?.visibility = View.INVISIBLE
            binding?.errorLayout?.root?.visibility = View.VISIBLE
            return
        }

        if (request.status == Status.COMPLETED) {
            binding?.errorLayout?.root?.visibility = View.INVISIBLE
            binding?.progressBar?.visibility = View.INVISIBLE
            binding?.rvPeople?.visibility = View.VISIBLE
            return
        }
    }

    private fun showRecyclerList(list: List<ResponseListUsersItem>) {
        val listPeopleAdapter = ListPeopleAdapter(list)
        binding?.rvPeople?.adapter = listPeopleAdapter

        listPeopleAdapter.setOnItemClickCallback(object : ListPeopleAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ResponseListUsersItem) = openDetail(data)
        })
    }

    fun openDetail(data: ResponseListUsersItem) {
        Log.d(TAG, data.toString())

        val moveToDetailIntent = Intent(this@HomeActivity, DetailActivity::class.java)
        moveToDetailIntent.putExtra(
            DetailActivity.EXTRA_PEOPLE, People(
                data.login,
                data.htmlUrl,
            )
        )
        startActivity(moveToDetailIntent)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (binding != null) binding = null
    }

    companion object {
        const val TAG = "Home Activity"
    }
}