package ro.poke.userlist.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.Flow
import ro.poke.userlist.data.entity.User
import ro.poke.userlist.data.UserRepository
import java.util.*

class UserListViewModel () : ViewModel() {
    var theUsers: MutableLiveData<List<User>> = MutableLiveData(Collections.synchronizedList(
        mutableListOf()
    ))

    private var currentSearchResult: Flow<PagingData<User>>? = null

    var loading = MutableLiveData<Boolean>(false)
    var repository = UserRepository()

//    w

    fun getUserFlow(): Flow<PagingData<User>> {
        val newResult: Flow<PagingData<User>> =
            repository.getUserStream().cachedIn(viewModelScope)
        currentSearchResult = newResult
        return newResult
    }
}