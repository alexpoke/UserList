package ro.poke.userlist.viewmodels

import android.util.Log
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ro.poke.userlist.api.UserService
import ro.poke.userlist.data.User
import java.util.*

class UserListViewModel () : ViewModel() {
    var theUsers: MutableLiveData<List<User>> = MutableLiveData(Collections.synchronizedList(
        mutableListOf()
    ))

//    var loading = ObservableBoolean(false)
    var loading = MutableLiveData<Boolean>(false)

    init {
        Log.i("UserListViewModel", "UserListViewModel created!")
    }

    fun loadUsers() {
        Log.i("UserListViewModel", "Load users")
        getUsersFromWeb()
    }

    fun getUsersFromWeb() {
        loading.value = true
        viewModelScope.launch(Dispatchers.IO ){
            var users = UserService.userService.getUsers(0, 100, "abc")
            withContext(Dispatchers.Main) {

                var users = theUsers.value as MutableList<User> + users.results as MutableList<User>
                theUsers.postValue( users )
                loading.value = false;
            }
        }
    }
}