package ro.poke.userlist

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.core.widget.NestedScrollView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.Job
import androidx.lifecycle.lifecycleScope
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch
import ro.poke.userlist.data.User
import ro.poke.userlist.databinding.ActivityItemListBinding


import ro.poke.userlist.dummy.DummyContent
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

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private var twoPane: Boolean = false

    private lateinit var viewModel: UserListViewModel
    private var loadJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityItemListBinding>(this, R.layout.activity_item_list)
//        setContentView(R.layout.activity_item_list)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.title = title

        viewModel = ViewModelProvider(this).get(UserListViewModel::class.java)

        // TODO: use data binding
        viewModel.loading.observe(this, { loading ->
            run {
                binding.frameLayout.visibility = if (loading) View.GONE else View.VISIBLE
                binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
            }
        })

        if (findViewById<NestedScrollView>(R.id.item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            twoPane = true
        }

//        setupRecyclerView(findViewById(R.id.item_list))

        val recyclerView = findViewById<RecyclerView>(R.id.item_list)
        viewModel.theUsers.observe(this, { users ->
            run {
                recyclerView.adapter = UsersRecyclerViewAdapter(this, users, twoPane, Picasso.get())
            }
        })
        loadUsers()
    }

    private fun loadUsers() {
        // Make sure we cancel the previous job before creating a new one
        loadJob?.cancel()
        loadJob = lifecycleScope.launch {
            viewModel.loadUsers()
        }
    }

//    private fun setupRecyclerView(recyclerView: RecyclerView) {
//        recyclerView.adapter = UsersRecyclerViewAdapter(this, DummyContent.ITEMS, twoPane)
//    }
//
//    fun usersObserver(): Observer<List<User>> {
//        return Observer { users ->
//            Log.e("ItemListActivity", "Got ${users.size} users")
//            recyclerView.adapter = UsersRecyclerViewAdapter(this, DummyContent.ITEMS, twoPane)
//        }
//    }

    class UsersRecyclerViewAdapter(private val parentActivity: ItemListActivity,
                                   private val values: List<User>,
                                   private val twoPane: Boolean,
                                   private val picasso: Picasso
                                   ) : RecyclerView.Adapter<UsersRecyclerViewAdapter.ViewHolder>() {

//        private val onClickListener: View.OnClickListener

        init {
//            onClickListener = View.OnClickListener { v ->
//                val item = v.tag as DummyContent.DummyItem
//                if (twoPane) {
//                    val fragment = ItemDetailFragment().apply {
//                        arguments = Bundle().apply {
//                            putString(ItemDetailFragment.ARG_ITEM_ID, item.id)
//                        }
//                    }
//                    parentActivity.supportFragmentManager
//                            .beginTransaction()
//                            .replace(R.id.item_detail_container, fragment)
//                            .commit()
//                } else {
//                    val intent = Intent(v.context, ItemDetailActivity::class.java).apply {
//                        putExtra(ItemDetailFragment.ARG_ITEM_ID, item.id)
//                    }
//                    v.context.startActivity(intent)
//                }
//            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_list_content, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = values[position]
            holder.nameView.text = item.name.first + " " + item.name.last
            holder.emailView.text = item.email
            picasso
                .load(Uri.parse(item.picture.thumbnail))
                .placeholder(R.drawable.ic_launcher_background)
                .into(holder.profileView)

            with(holder.itemView) {
                tag = item
//                setOnClickListener(onClickListener)
            }
        }

        override fun getItemCount() = values.size

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val nameView: TextView = view.findViewById(R.id.tvName)
            val emailView: TextView = view.findViewById(R.id.tvEmail)
            val profileView: ImageView = view.findViewById(R.id.ivProfile)
        }
    }
}