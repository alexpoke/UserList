package ro.poke.userlist.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.Flow
import ro.poke.userlist.data.UserRepository
import ro.poke.userlist.data.entity.User

class ItemListViewModel () : ViewModel() {

    var repository = UserRepository()

    fun getUserFlow(): Flow<PagingData<User>> {
        val newResult: Flow<PagingData<User>> = repository.getUserStream().cachedIn(viewModelScope)
        return newResult
    }
}