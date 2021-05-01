package ro.poke.userlist

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ro.poke.userlist.data.entity.User
import ro.poke.userlist.databinding.ItemListContentBinding

class UserPagingDataAdapter(
            private val parentActivity: ItemListActivity,
            private val picasso: Picasso,
            private val twoPane: Boolean
            ): PagingDataAdapter<User, UserPagingDataAdapter.ViewHolder>(UserDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemListContentBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = getItem(position)
        holder.nameView.text = "$position ${user?.name?.first} ${user?.name?.last}"
        holder.emailView.text = user?.email
        picasso
            .load(Uri.parse(user?.picture?.thumbnail))
            .placeholder(R.drawable.ic_launcher_background)
            .into(holder.profileView)

        with(holder.itemView) {
            tag = user
                setOnClickListener(View.OnClickListener { v ->
                    if (twoPane) {
                        val fragment = ItemDetailFragment().apply {
                            arguments = Bundle().apply {
                                putParcelable(ItemDetailFragment.ARG_ITEM, user)
                            }
                        }
                        parentActivity.supportFragmentManager
                            .beginTransaction()
                            .replace(R.id.item_detail_container, fragment)
                            .commit()
                    } else {
                        val intent = Intent(v.context, ItemDetailActivity::class.java).apply {
                            putExtra(ItemDetailFragment.ARG_ITEM, user)
                        }
                        v.context.startActivity(intent)
                    }
                })
        }
    }

    class ViewHolder(binding: ItemListContentBinding) : RecyclerView.ViewHolder(binding.root) {
        val nameView: TextView = binding.tvName
        val emailView: TextView = binding.tvEmail
        val profileView: ImageView = binding.ivProfile
    }
}

private class UserDiffCallback : DiffUtil.ItemCallback<User>() {
    override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem.name.first == newItem.name.first && oldItem.name.last == newItem.name.last
    }

    override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem == newItem
    }
}
