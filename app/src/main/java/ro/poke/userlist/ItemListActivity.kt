package ro.poke.userlist

import android.os.Bundle
import androidx.core.widget.NestedScrollView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.Job
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.squareup.picasso.Picasso
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ro.poke.userlist.databinding.ActivityItemListBinding


import ro.poke.userlist.viewmodels.UserListViewModel

/**
 * An activity representing a list of Pings. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a [ItemDetailActivity] representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
class ItemListActivity : AppCompatActivity() {

    lateinit var binding: ActivityItemListBinding
        private set
    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private var twoPane: Boolean = false

    private lateinit var adapter: UserPagingDataAdapter
    private lateinit var viewModel: UserListViewModel
    private var loadJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityItemListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        binding.toolbar.title = title

        viewModel = ViewModelProvider(this).get(UserListViewModel::class.java)

        if (findViewById<NestedScrollView>(R.id.item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            twoPane = true
        }
        adapter = UserPagingDataAdapter(this, Picasso.get(), twoPane)

//        setupRecyclerView(findViewById(R.id.item_list))

        val recyclerView = findViewById<RecyclerView>(R.id.item_list)
        recyclerView.adapter = adapter
        lifecycleScope.launchWhenCreated {
            adapter.loadStateFlow.collectLatest { loadStates ->
                val loading = loadStates.refresh is LoadState.Loading
                binding.frameLayout.visibility = if (loading) View.GONE else View.VISIBLE
                binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
            }
        }

        loadUsers()
    }

    private fun loadUsers() {
        // Make sure we cancel the previous job before creating a new one
        loadJob?.cancel()
        loadJob = lifecycleScope.launch {
//            viewModel.loadUsers()
            viewModel.getUserFlow().collectLatest {
                adapter.submitData(it)
            }
        }
    }
}