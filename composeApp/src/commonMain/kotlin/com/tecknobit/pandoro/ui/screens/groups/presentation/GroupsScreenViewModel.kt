package com.tecknobit.pandoro.ui.screens.groups.presentation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.viewModelScope
import com.tecknobit.equinoxcore.pagination.PaginatedResponse.Companion.DEFAULT_PAGE
import com.tecknobit.pandoro.ui.screens.groups.data.Group
import com.tecknobit.pandoro.ui.screens.notes.data.Note
import com.tecknobit.pandoro.ui.screens.projects.data.Project
import com.tecknobit.pandoro.ui.screens.projects.data.ProjectUpdate
import com.tecknobit.pandoro.ui.screens.shared.data.GroupMember
import com.tecknobit.pandoro.ui.screens.shared.data.PandoroUser
import com.tecknobit.pandoro.ui.screens.shared.viewmodels.BaseGroupViewModel.GroupDeleter
import com.tecknobit.pandoro.ui.screens.shared.viewmodels.MultipleListViewModel
import com.tecknobit.pandorocore.enums.Role
import com.tecknobit.pandorocore.enums.UpdateStatus
import io.github.ahmad_hamwi.compose.pagination.PaginationState
import kotlinx.coroutines.launch
import kotlin.random.Random

class GroupsScreenViewModel : MultipleListViewModel(), GroupDeleter {

    lateinit var myGroupsStateFilters: MutableState<String>

    val myGroupsState = PaginationState<Int, Group>(
        initialPageKey = DEFAULT_PAGE,
        onRequestPage = { page ->
            retrieveMyGroups(
                page = page
            )
        }
    )

    private fun retrieveMyGroups(
        page: Int
    ) {
        viewModelScope.launch {
            // TODO: MAKE THE REQUEST THEN
            val listFromServer = listOf<Group>(
                Group(
                    id = Random.nextLong().toString(),
                    name = "Group",
                    logo = "https://img.freepik.com/foto-gratuito/sfondo-astratto-nebulosa-ultra-dettagliata-4_1562-749.jpg",
                    creationDate = System.currentTimeMillis(),
                    author = PandoroUser(
                        id = Random.nextLong().toString(),
                        profilePic = "https://res.cloudinary.com/momentum-media-group-pty-ltd/image/upload/c_fill,q_auto:best,f_auto,e_unsharp_mask:80,w_830,h_478/Space%20Connect%2Fspace-exploration-sc_fm1ysf",
                        "name",
                        "surname",
                        email = "name.surname@gmail.com"
                    ),
                    projects = listOf(
                        Project(
                            id = Random.nextLong().toString(),
                            name = "Prova",
                            creationDate = System.currentTimeMillis(),
                            author = PandoroUser(
                                "1",
                                profilePic = "https://res.cloudinary.com/momentum-media-group-pty-ltd/image/upload/c_fill,q_auto:best,f_auto,e_unsharp_mask:80,w_830,h_478/Space%20Connect%2Fspace-exploration-sc_fm1ysf",
                                "name",
                                "surname",
                                email = "name.surname@gmail.com"
                            ),
                            description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aenean venenatis pulvinar ullamcorper. Aliquam finibus nisi eget enim elementum, quis volutpat risus semper. Praesent mauris velit, lacinia non aliquet ac, faucibus at magna. Ut nec leo elit. In eleifend hendrerit diam, nec rutrum nisi porttitor sed. Sed auctor, massa sed viverra porta, odio erat feugiat tortor, non posuere nulla velit sed lacus. Nam bibendum orci id felis hendrerit, nec suscipit nibh ultricies.\n" +
                                    "Donec rutrum aliquet pellentesque. Aenean condimentum venenatis eros a tincidunt. Vivamus rhoncus mauris eget nibh ornare, ut fermentum magna pellentesque. Nulla ac turpis fermentum, ullamcorper lorem in, feugiat lorem. Nam suscipit risus nec neque malesuada, sit amet tempor nisi lobortis. Nulla maximus nisl turpis, ut commodo dui varius a. Mauris bibendum leo eu lorem viverra, in pellentesque neque sagittis. Integer lacinia vestibulum diam quis faucibus. Curabitur dui libero, facilisis vel tristique non, accumsan id augue. Quisque at faucibus tortor. Fusce maximus ante at sapien ultrices bibendum quis quis tortor. In tristique lorem ut nibh lobortis dictum.\n" +
                                    "Cras elementum venenatis laoreet. Sed velit libero, dignissim eu enim vel, condimentum faucibus orci. Phasellus ut finibus velit. Suspendisse fermentum ac mauris a maximus. Suspendisse dignissim convallis nunc, eu tristique eros suscipit vel. Phasellus non vehicula justo. Nam ultrices euismod purus, sit amet ultrices libero accumsan sit amet. Mauris semper, dolor ac lacinia bibendum, metus nulla blandit leo, in iaculis dolor lectus id magna. Mauris vestibulum sit amet ante non luctus. Integer a purus dui. Donec nisi augue, facilisis eget ipsum nec, mattis tristique ipsum. Ut lacus nisi, elementum ut ipsum et, rhoncus sodales sem.\n" +
                                    "Aenean faucibus porttitor ipsum. Nulla facilisi. Quisque elementum nulla eget ex convallis dignissim. Morbi eget faucibus nisi, id rhoncus nisi. Sed ut cursus felis. Pellentesque placerat nulla vitae eros blandit, at feugiat diam eleifend. Proin ultrices mauris at sem congue hendrerit. Aenean leo metus, porta in volutpat et, volutpat sed risus. Donec ante mi, ullamcorper quis pretium vitae, maximus eget mi.\n" +
                                    "Sed a tempus ligula. Ut pretium lobortis odio, sit amet fermentum felis. Phasellus porttitor lorem eget orci pharetra, in tempor urna aliquam. Nullam feugiat ante felis, sit amet tincidunt massa venenatis et. Proin tincidunt eget nisi ut tempus. Vestibulum lectus tellus, cursus vitae dui nec, egestas gravida mauris. Mauris pharetra accumsan consequat.",
                            version = "1.0.0",
                            updates = listOf(
                                ProjectUpdate(
                                    id = Random.nextLong().toString(),
                                    author = GroupMember(
                                        id = Random.nextLong().toString(),
                                        profilePic = "https://res.cloudinary.com/momentum-media-group-pty-ltd/image/upload/c_fill,q_auto:best,f_auto,e_unsharp_mask:80,w_830,h_478/Space%20Connect%2Fspace-exploration-sc_fm1ysf",
                                        "name",
                                        "surname",
                                        email = "name.surname@gmail.com"
                                    ),
                                    targetVersion = "1.0.0",
                                    creationDate = System.currentTimeMillis(),
                                    startDate = 1731588486000,
                                    status = UpdateStatus.IN_DEVELOPMENT,
                                    notes = listOf(
                                        Note(
                                            id = Random.nextLong().toString(),
                                            author = PandoroUser(
                                                id = Random.nextLong().toString(),
                                                profilePic = "https://res.cloudinary.com/momentum-media-group-pty-ltd/image/upload/c_fill,q_auto:best,f_auto,e_unsharp_mask:80,w_830,h_478/Space%20Connect%2Fspace-exploration-sc_fm1ysf",
                                                "name",
                                                "surname",
                                                email = "name.surname@gmail.com"
                                            ),
                                            creationDate = System.currentTimeMillis(),
                                            content = "111",
                                            markedAsDone = true
                                        )
                                    )
                                )
                            )
                        )
                    ),
                    description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aenean venenatis pulvinar ullamcorper. Aliquam finibus nisi eget enim elementum, quis volutpat risus semper. Praesent mauris velit, lacinia non aliquet ac, faucibus at magna. Ut nec leo elit. In eleifend hendrerit diam, nec rutrum nisi porttitor sed. Sed auctor, massa sed viverra porta, odio erat feugiat tortor, non posuere nulla velit sed lacus. Nam bibendum orci id felis hendrerit, nec suscipit nibh ultricies.\n" +
                            "Donec rutrum aliquet pellentesque. Aenean condimentum venenatis eros a tincidunt. Vivamus rhoncus mauris eget nibh ornare, ut fermentum magna pellentesque. Nulla ac turpis fermentum, ullamcorper lorem in, feugiat lorem. Nam suscipit risus nec neque malesuada, sit amet tempor nisi lobortis. Nulla maximus nisl turpis, ut commodo dui varius a. Mauris bibendum leo eu lorem viverra, in pellentesque neque sagittis. Integer lacinia vestibulum diam quis faucibus. Curabitur dui libero, facilisis vel tristique non, accumsan id augue. Quisque at faucibus tortor. Fusce maximus ante at sapien ultrices bibendum quis quis tortor. In tristique lorem ut nibh lobortis dictum.\n" +
                            "Sed a tempus ligula. Ut pretium lobortis odio, sit amet fermentum felis. Phasellus porttitor lorem eget orci pharetra, in tempor urna aliquam. Nullam feugiat ante felis, sit amet tincidunt massa venenatis et. Proin tincidunt eget nisi ut tempus. Vestibulum lectus tellus, cursus vitae dui nec, egestas gravida mauris. Mauris pharetra accumsan consequat.",
                    members = listOf(
                        GroupMember(
                            id = Random.nextLong().toString(),
                            profilePic = "https://res.cloudinary.com/momentum-media-group-pty-ltd/image/upload/c_fill,q_auto:best,f_auto,e_unsharp_mask:80,w_830,h_478/Space%20Connect%2Fspace-exploration-sc_fm1ysf",
                            "name",
                            "surname",
                            email = "name.surname@gmail.com",
                            role = Role.ADMIN
                        )
                    )
                )
            )
            myGroupsState.appendPage(
                items = listFromServer, // TODO: USE THE REAL VALUE
                nextPageKey = page + 1, // TODO: USE THE REAL VALUE
                isLastPage = Random.nextBoolean() // TODO: USE THE REAL VALUE
            )
        }
    }

    lateinit var allGroupsStateFilters: MutableState<String>

    val roleFilters: SnapshotStateList<Role> = Role.entries.toMutableStateList()

    val allGroupsState = PaginationState<Int, Group>(
        initialPageKey = DEFAULT_PAGE,
        onRequestPage = { page ->
            retrieveAllGroups(
                page = page
            )
        }
    )

    private fun retrieveAllGroups(
        page: Int
    ) {
        viewModelScope.launch {
            // TODO: MAKE THE REQUEST THEN
            val listFromServer = listOf<Group>(
                Group(
                    id = Random.nextLong().toString(),
                    name = "Group",
                    logo = "https://img.freepik.com/foto-gratuito/sfondo-astratto-nebulosa-ultra-dettagliata-4_1562-749.jpg",
                    creationDate = System.currentTimeMillis(),
                    author = PandoroUser(
                        id = Random.nextLong().toString(),
                        profilePic = "https://res.cloudinary.com/momentum-media-group-pty-ltd/image/upload/c_fill,q_auto:best,f_auto,e_unsharp_mask:80,w_830,h_478/Space%20Connect%2Fspace-exploration-sc_fm1ysf",
                        "name",
                        "surname",
                        email = "name.surname@gmail.com"
                    ),
                    projects = listOf(
                        Project(
                            id = Random.nextLong().toString(),
                            name = "Prova",
                            creationDate = System.currentTimeMillis(),
                            author = PandoroUser(
                                "1",
                                profilePic = "https://res.cloudinary.com/momentum-media-group-pty-ltd/image/upload/c_fill,q_auto:best,f_auto,e_unsharp_mask:80,w_830,h_478/Space%20Connect%2Fspace-exploration-sc_fm1ysf",
                                "name",
                                "surname",
                                email = "name.surname@gmail.com"
                            ),
                            description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aenean venenatis pulvinar ullamcorper. Aliquam finibus nisi eget enim elementum, quis volutpat risus semper. Praesent mauris velit, lacinia non aliquet ac, faucibus at magna. Ut nec leo elit. In eleifend hendrerit diam, nec rutrum nisi porttitor sed. Sed auctor, massa sed viverra porta, odio erat feugiat tortor, non posuere nulla velit sed lacus. Nam bibendum orci id felis hendrerit, nec suscipit nibh ultricies.\n" +
                                    "Donec rutrum aliquet pellentesque. Aenean condimentum venenatis eros a tincidunt. Vivamus rhoncus mauris eget nibh ornare, ut fermentum magna pellentesque. Nulla ac turpis fermentum, ullamcorper lorem in, feugiat lorem. Nam suscipit risus nec neque malesuada, sit amet tempor nisi lobortis. Nulla maximus nisl turpis, ut commodo dui varius a. Mauris bibendum leo eu lorem viverra, in pellentesque neque sagittis. Integer lacinia vestibulum diam quis faucibus. Curabitur dui libero, facilisis vel tristique non, accumsan id augue. Quisque at faucibus tortor. Fusce maximus ante at sapien ultrices bibendum quis quis tortor. In tristique lorem ut nibh lobortis dictum.\n" +
                                    "Cras elementum venenatis laoreet. Sed velit libero, dignissim eu enim vel, condimentum faucibus orci. Phasellus ut finibus velit. Suspendisse fermentum ac mauris a maximus. Suspendisse dignissim convallis nunc, eu tristique eros suscipit vel. Phasellus non vehicula justo. Nam ultrices euismod purus, sit amet ultrices libero accumsan sit amet. Mauris semper, dolor ac lacinia bibendum, metus nulla blandit leo, in iaculis dolor lectus id magna. Mauris vestibulum sit amet ante non luctus. Integer a purus dui. Donec nisi augue, facilisis eget ipsum nec, mattis tristique ipsum. Ut lacus nisi, elementum ut ipsum et, rhoncus sodales sem.\n" +
                                    "Aenean faucibus porttitor ipsum. Nulla facilisi. Quisque elementum nulla eget ex convallis dignissim. Morbi eget faucibus nisi, id rhoncus nisi. Sed ut cursus felis. Pellentesque placerat nulla vitae eros blandit, at feugiat diam eleifend. Proin ultrices mauris at sem congue hendrerit. Aenean leo metus, porta in volutpat et, volutpat sed risus. Donec ante mi, ullamcorper quis pretium vitae, maximus eget mi.\n" +
                                    "Sed a tempus ligula. Ut pretium lobortis odio, sit amet fermentum felis. Phasellus porttitor lorem eget orci pharetra, in tempor urna aliquam. Nullam feugiat ante felis, sit amet tincidunt massa venenatis et. Proin tincidunt eget nisi ut tempus. Vestibulum lectus tellus, cursus vitae dui nec, egestas gravida mauris. Mauris pharetra accumsan consequat.",
                            version = "1.0.0",
                            updates = listOf(
                                ProjectUpdate(
                                    id = Random.nextLong().toString(),
                                    author = GroupMember(
                                        id = Random.nextLong().toString(),
                                        profilePic = "https://res.cloudinary.com/momentum-media-group-pty-ltd/image/upload/c_fill,q_auto:best,f_auto,e_unsharp_mask:80,w_830,h_478/Space%20Connect%2Fspace-exploration-sc_fm1ysf",
                                        "name",
                                        "surname",
                                        email = "name.surname@gmail.com"
                                    ),
                                    targetVersion = "1.0.0",
                                    creationDate = System.currentTimeMillis(),
                                    startDate = 1731588486000,
                                    status = UpdateStatus.IN_DEVELOPMENT,
                                    notes = listOf(
                                        Note(
                                            id = Random.nextLong().toString(),
                                            author = PandoroUser(
                                                id = Random.nextLong().toString(),
                                                profilePic = "https://res.cloudinary.com/momentum-media-group-pty-ltd/image/upload/c_fill,q_auto:best,f_auto,e_unsharp_mask:80,w_830,h_478/Space%20Connect%2Fspace-exploration-sc_fm1ysf",
                                                "name",
                                                "surname",
                                                email = "name.surname@gmail.com"
                                            ),
                                            creationDate = System.currentTimeMillis(),
                                            content = "111",
                                            markedAsDone = true
                                        )
                                    )
                                )
                            )
                        )
                    ),
                    description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aenean venenatis pulvinar ullamcorper. Aliquam finibus nisi eget enim elementum, quis volutpat risus semper. Praesent mauris velit, lacinia non aliquet ac, faucibus at magna. Ut nec leo elit. In eleifend hendrerit diam, nec rutrum nisi porttitor sed. Sed auctor, massa sed viverra porta, odio erat feugiat tortor, non posuere nulla velit sed lacus. Nam bibendum orci id felis hendrerit, nec suscipit nibh ultricies.\n" +
                            "Donec rutrum aliquet pellentesque. Aenean condimentum venenatis eros a tincidunt. Vivamus rhoncus mauris eget nibh ornare, ut fermentum magna pellentesque. Nulla ac turpis fermentum, ullamcorper lorem in, feugiat lorem. Nam suscipit risus nec neque malesuada, sit amet tempor nisi lobortis. Nulla maximus nisl turpis, ut commodo dui varius a. Mauris bibendum leo eu lorem viverra, in pellentesque neque sagittis. Integer lacinia vestibulum diam quis faucibus. Curabitur dui libero, facilisis vel tristique non, accumsan id augue. Quisque at faucibus tortor. Fusce maximus ante at sapien ultrices bibendum quis quis tortor. In tristique lorem ut nibh lobortis dictum.\n" +
                            "Sed a tempus ligula. Ut pretium lobortis odio, sit amet fermentum felis. Phasellus porttitor lorem eget orci pharetra, in tempor urna aliquam. Nullam feugiat ante felis, sit amet tincidunt massa venenatis et. Proin tincidunt eget nisi ut tempus. Vestibulum lectus tellus, cursus vitae dui nec, egestas gravida mauris. Mauris pharetra accumsan consequat.",
                    members = listOf(
                        GroupMember(
                            id = Random.nextLong().toString(),
                            profilePic = "https://res.cloudinary.com/momentum-media-group-pty-ltd/image/upload/c_fill,q_auto:best,f_auto,e_unsharp_mask:80,w_830,h_478/Space%20Connect%2Fspace-exploration-sc_fm1ysf",
                            "name",
                            "surname",
                            email = "name.surname@gmail.com",
                            role = Role.ADMIN
                        )
                    )
                )
            )
            allGroupsState.appendPage(
                items = listFromServer, // TODO: USE THE REAL VALUE
                nextPageKey = page + 1, // TODO: USE THE REAL VALUE
                isLastPage = Random.nextBoolean() // TODO: USE THE REAL VALUE
            )
        }
    }

    override fun retryRetrieveLists() {
        myGroupsState.retryLastFailedRequest()
        allGroupsState.retryLastFailedRequest()
    }

    override fun areFiltersSet(
        allItems: Boolean
    ): Boolean {
        return if(allItems) {
            allGroupsStateFilters.value.isNotEmpty() || roleFilters.size != Role.entries.size
        } else
            myGroupsStateFilters.value.isNotEmpty()
    }

    fun manageRoleFilter(
        selected: Boolean,
        role: Role
    ) {
        if(selected)
            roleFilters.remove(role)
        else
            roleFilters.add(role)
    }

    override fun clearFilters(
        allItems: Boolean
    ) {
        if(allItems) {
            allGroupsStateFilters.value = ""
            resetRoles()
            allGroupsState.refresh()
        } else {
            myGroupsStateFilters.value = ""
            myGroupsState.refresh()
        }
    }

    fun resetRoles() {
        roleFilters.clear()
        roleFilters.addAll(Role.entries)
    }

    override fun filterItems(
        allItems: Boolean,
        filters: MutableState<String>,
        onFiltersSet: () -> Unit
    ) {
        if(allItems) {
            allGroupsStateFilters.value = filters.value
            allGroupsState.refresh()
        } else {
            myGroupsStateFilters.value = filters.value
            myGroupsState.refresh()
        }
        onFiltersSet.invoke()
    }

}