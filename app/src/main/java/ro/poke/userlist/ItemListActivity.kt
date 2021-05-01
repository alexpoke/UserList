package ro.poke.userlist


import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ro.poke.userlist.databinding.ActivityItemListBinding
import ro.poke.userlist.viewmodels.ItemListViewModel

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

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private var twoPane: Boolean = false

    private lateinit var adapter: UserPagingDataAdapter
    private lateinit var viewModel: ItemListViewModel
    private var loadJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityItemListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        binding.toolbar.title = title

        if (findViewById<NestedScrollView>(R.id.item_detail_container) != null) {
            twoPane = true
        }

        viewModel = ViewModelProvider(this).get(ItemListViewModel::class.java)

        adapter = UserPagingDataAdapter(this, Picasso.get(), twoPane)

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
            viewModel.getUserFlow().collectLatest {
                adapter.submitData(it)
            }
        }
    }
}