@file:OptIn(ExperimentalPaginationApi::class)

package com.tecknobit.pandoro.ui.screens.project.presentation

import androidx.compose.runtime.snapshots.SnapshotStateList
import com.tecknobit.equinoxcore.pagination.PaginatedResponse.Companion.DEFAULT_PAGE
import com.tecknobit.equinoxcore.pagination.PaginatedResponse.Companion.DEFAULT_PAGE_SIZE
import com.tecknobit.pandoro.ui.screens.groups.data.Group
import com.tecknobit.pandoro.ui.screens.notes.data.Note
import com.tecknobit.pandoro.ui.screens.project.presenter.ProjectScreen
import com.tecknobit.pandoro.ui.screens.projects.data.Project
import com.tecknobit.pandoro.ui.screens.projects.data.ProjectUpdate
import com.tecknobit.pandoro.ui.screens.shared.data.GroupMember
import com.tecknobit.pandoro.ui.screens.shared.data.PandoroUser
import com.tecknobit.pandoro.ui.screens.shared.viewmodels.BaseProjectViewModel
import com.tecknobit.pandoro.ui.screens.shared.viewmodels.BaseProjectViewModel.ProjectDeleter
import com.tecknobit.pandorocore.enums.Role
import com.tecknobit.pandorocore.enums.UpdateStatus
import io.github.ahmad_hamwi.compose.pagination.ExperimentalPaginationApi
import io.github.ahmad_hamwi.compose.pagination.PaginationState
import kotlin.random.Random

class ProjectScreenViewModel(
    projectId: String
) : BaseProjectViewModel(), ProjectDeleter {

    private val totalUpdates = mutableSetOf<ProjectUpdate>()

    private val currentUpdatesLoaded = mutableListOf<ProjectUpdate>()

    lateinit var updateStatusesFilters: SnapshotStateList<UpdateStatus>

    val updatesState = PaginationState<Int, ProjectUpdate>(
        initialPageKey = DEFAULT_PAGE,
        onRequestPage = { page ->
            appendUpdates(
                page = page
            )
        }
    )

    override fun retrieveProject() {
        execRefreshingRoutine(
            currentContext = ProjectScreen::class.java,
            routine = {
                // TODO: MAKE THE REQUEST THEN
                _project.value = Project(
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
                    icon ="https://res.cloudinary.com/momentum-media-group-pty-ltd/image/upload/c_fill,q_auto:best,f_auto,e_unsharp_mask:80,w_830,h_478/Space%20Connect%2Fspace-exploration-sc_fm1ysf",
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
                            startedBy = GroupMember(
                                id = Random.nextLong().toString(),
                                profilePic = "https://res.cloudinary.com/momentum-media-group-pty-ltd/image/upload/c_fill,q_auto:best,f_auto,e_unsharp_mask:80,w_830,h_478/Space%20Connect%2Fspace-exploration-sc_fm1ysf",
                                "name",
                                "surname",
                                email = "name.surname@gmail.com"
                            ),
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
                        ),
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
                            publishDate = System.currentTimeMillis(),
                            startDate = 1731588486000,
                            startedBy = GroupMember(
                                id = Random.nextLong().toString(),
                                profilePic = "https://res.cloudinary.com/momentum-media-group-pty-ltd/image/upload/c_fill,q_auto:best,f_auto,e_unsharp_mask:80,w_830,h_478/Space%20Connect%2Fspace-exploration-sc_fm1ysf",
                                "name",
                                "surname",
                                email = "name.surname@gmail.com"
                            ),
                            publishedBy = GroupMember(
                                id = Random.nextLong().toString(),
                                profilePic = "https://starwalk.space/gallery/images/what-is-space/1920x1080.jpg",
                                "fafaf",
                                "egwg",
                                email = "name.surname@gmail.com"
                            ),
                            status = UpdateStatus.PUBLISHED,
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
                        ),
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
                            //startDate = 1731588486000,
                            status = UpdateStatus.SCHEDULED,
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
                    ),
                    projectRepo = "https://github.com/N7ghtm4r3/Pandoro-Clients",
                    groups = listOf(
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
                            ),
                            projects = emptyList()
                        )
                    )
                )
                totalUpdates.addAll(_project.value!!.updates)
            }
        )
    }

    fun manageStatusesFilter(
        updateStatus: UpdateStatus,
        selected: Boolean
    ) {
        if(selected)
            updateStatusesFilters.add(updateStatus)
        else
            updateStatusesFilters.remove(updateStatus)
        filterList()
    }

    fun areFiltersSet() : Boolean {
        return updateStatusesFilters.size != UpdateStatus.entries.size
    }

    fun clearFilters() {
        updateStatusesFilters.clear()
        updateStatusesFilters.addAll(UpdateStatus.entries)
        filterList()
    }

    private fun filterList() {
        updatesState.appendPageWithUpdates(
            allItems = totalUpdates.filter { update -> updateStatusesFilters.contains(update.status) },
            nextPageKey = 0,
            isLastPage = true
        )
    }

    private fun appendUpdates(
        page: Int
    ) {
        val toIndex = ((page + 1) * DEFAULT_PAGE_SIZE)
        val lastIndex = totalUpdates.size
        val pagedUpdates = totalUpdates.toList().subList(
            fromIndex = currentUpdatesLoaded.size,
            toIndex = if(toIndex > lastIndex)
                lastIndex
            else
                toIndex
        )
        currentUpdatesLoaded.addAll(pagedUpdates)
        updatesState.appendPage(
            items = pagedUpdates,
            nextPageKey = page + 1,
            isLastPage = currentUpdatesLoaded.size == totalUpdates.size
        )
    }

    fun deleteUpdate(
        update: ProjectUpdate,
        onDelete: () -> Unit
    ) {
        // TODO: MAKE THE REQUEST THEN
    }

}