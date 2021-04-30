package ro.poke.userlist.data

import androidx.paging.PagingSource
import ro.poke.userlist.api.UserService
import ro.poke.userlist.data.entity.User

private const val UUSER_STARTING_PAGE_INDEX = 1

class UserPagingDataSource(
    private val service: UserService
) : PagingSource<Int, User>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, User> {
        val page = params.key ?: UUSER_STARTING_PAGE_INDEX
        return try {
            val response = service.getUsers(page, params.loadSize, "abc")
            val users = response.results

            LoadResult.Page(
                data = users,
                prevKey = if (page == UUSER_STARTING_PAGE_INDEX) null else page - 1,
                nextKey = if (page == response.info.page) page + 1 else null
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }
}
