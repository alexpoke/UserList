# UserList

### Notest on the solution
* Started up with the default master-detail project in Android Studio. However, a solution using the Navigation Component would probably be preferable in a larger scale app.
* Used the Paging Library from the Architecture Components to acheive pagination with infinite scrolling.
* Opted for only in-memory storage
* Pros:
    * Easier / faster to implement
    * Local data is not sure to be up to date, so network requests are still necessary.
* Cons:
  * No offline support.
  * Specifically for this case, where online data does not change, local sorage would mean that only requests for missing pages would be necessary.
