package ro.poke.userlist

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.squareup.picasso.Picasso
import ro.poke.userlist.data.entity.User

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a [ItemListActivity]
 * in two-pane mode (on tablets) or a [ItemDetailActivity]
 * on handsets.
 */
class ItemDetailFragment : Fragment() {

    private var user: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            if (it.containsKey(ARG_ITEM)) {
                user = it.getParcelable<User>(ARG_ITEM)
                activity?.findViewById<CollapsingToolbarLayout>(R.id.toolbar_layout)?.title = user?.name?.first
                val profileView = activity?.findViewById<ImageView>(R.id.detail_image)
                profileView?.let { Picasso.get()
                    .load(Uri.parse(user?.picture?.large))
                    .placeholder(R.drawable.ic_launcher_background)
                    .into(it) }

            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.item_detail, container, false)

        user?.let {
            rootView.findViewById<TextView>(R.id.item_email).text = it.email
            rootView.findViewById<TextView>(R.id.item_phone).text = it.phone
        }

        return rootView
    }

    companion object {
        const val ARG_ITEM = "item"
    }
}