package ro.poke.userlist

import android.net.Uri
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

class UserPagingDataAdapter(
            private val parentActivity: ItemListActivity,
            private val picasso: Picasso,
            private val twoPane: Boolean
            ): PagingDataAdapter<User, UserPagingDataAdapter.ViewHolder>(GalleryDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_list_content, parent, false)
        return ViewHolder(view)
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
//                setOnClickListener(onClickListener)
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameView: TextView = view.findViewById(R.id.tvName)
        val emailView: TextView = view.findViewById(R.id.tvEmail)
        val profileView: ImageView = view.findViewById(R.id.ivProfile)
    }

}

private class GalleryDiffCallback : DiffUtil.ItemCallback<User>() {
    override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem.name.first == newItem.name.first && oldItem.name.last == newItem.name.last
    }

    override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem == newItem
    }
}
