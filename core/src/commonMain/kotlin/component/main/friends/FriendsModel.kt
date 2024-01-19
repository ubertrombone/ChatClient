package component.main.friends

import com.arkivanov.decompose.value.MutableValue
import component.main.friends.model.FriendsSet
import kotlinx.coroutines.CoroutineScope
import util.Status

interface FriendsModel {
    val scope: CoroutineScope
    val friendsListState: MutableValue<FriendsSet>
    val friendsListStatus: MutableValue<Status>
    val friendsListLoading: MutableValue<Boolean>

    suspend fun friendLooper()
    suspend fun getFriends()
}