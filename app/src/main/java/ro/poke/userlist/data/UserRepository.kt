package ro.poke.userlist.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ro.poke.userlist.api.UserService
import ro.poke.userlist.data.entity.User

class UserRepository {

    fun getUserStream(): Flow<PagingData<User>> {
        return Pager(
            config = PagingConfig(enablePlaceholders = false, pageSize = NETWORK_PAGE_SIZE),
            pagingSourceFactory = { UserPagingDataSource(UserService.userService) }
        ).flow
    }

    companion object {
        private const val NETWORK_PAGE_SIZE = 100
    }
}
