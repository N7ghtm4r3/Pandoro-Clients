package com.tecknobit.pandoro.ui.screens.creategroup.presentation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.viewModelScope
import com.tecknobit.equinoxcore.pagination.PaginatedResponse
import com.tecknobit.pandoro.navigator
import com.tecknobit.pandoro.ui.screens.groups.data.Group
import com.tecknobit.pandoro.ui.screens.notes.data.Note
import com.tecknobit.pandoro.ui.screens.projects.data.Project
import com.tecknobit.pandoro.ui.screens.projects.data.ProjectUpdate
import com.tecknobit.pandoro.ui.screens.shared.data.GroupMember
import com.tecknobit.pandoro.ui.screens.shared.data.PandoroUser
import com.tecknobit.pandoro.ui.screens.shared.viewmodels.BaseGroupViewModel
import com.tecknobit.pandorocore.enums.Role
import com.tecknobit.pandorocore.enums.UpdateStatus
import com.tecknobit.pandorocore.helpers.PandoroInputsValidator.isGroupDescriptionValid
import com.tecknobit.pandorocore.helpers.PandoroInputsValidator.isGroupNameValid
import io.github.ahmad_hamwi.compose.pagination.PaginationState
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import pandoro.composeapp.generated.resources.Res
import pandoro.composeapp.generated.resources.wrong_logo
import kotlin.random.Random

class CreateGroupScreenViewModel(
    val groupId: String?
) : BaseGroupViewModel() {

    val candidateProjects: SnapshotStateList<String> = mutableStateListOf()

    val groupProjects: SnapshotStateList<Project> = mutableStateListOf()

    val userProjects: MutableList<Project> = mutableListOf()

    lateinit var groupLogo: MutableState<String?>

    lateinit var groupName: MutableState<String>

    lateinit var groupNameError: MutableState<Boolean>

    lateinit var groupDescription: MutableState<String>

    lateinit var groupDescriptionError: MutableState<Boolean>

    val candidateMembersState = PaginationState<Int, GroupMember>(
        initialPageKey = PaginatedResponse.DEFAULT_PAGE,
        onRequestPage = { page ->
            loadCandidateMembersState(
                page = page
            )
        }
    )

    lateinit var candidateMembersAvailable: MutableState<Boolean>

    val groupMembers: SnapshotStateList<GroupMember> = mutableStateListOf()

    override fun retrieveGroup() {
        if(groupId == null)
            return
        // TODO: MAKE THE REQUEST
        // groupProjects.addAll() TODO: ADD THE CURRENT PROJECTS OF THE GROUP
        // groupMembers.addAll() TODO: ADD THE CURRENT MEMBERS OF THE GROUP
        // TODO: MAKE THE REQUEST THEN
        _group.value = Group(
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
                            author = PandoroUser(
                                id = Random.nextLong().toString(),
                                profilePic = "https://res.cloudinary.com/momentum-media-group-pty-ltd/image/upload/c_fill,q_auto:best,f_auto,e_unsharp_mask:80,w_830,h_478/Space%20Connect%2Fspace-exploration-sc_fm1ysf",
                                "name",
                                "surname",
                                email = "name.surname@gmail.com"
                            ),
                            targetVersion = "1.0.0",
                            createDate = System.currentTimeMillis(),
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
                    id = "1",
                    profilePic = "https://res.cloudinary.com/momentum-media-group-pty-ltd/image/upload/c_fill,q_auto:best,f_auto,e_unsharp_mask:80,w_830,h_478/Space%20Connect%2Fspace-exploration-sc_fm1ysf",
                    "name",
                    "surname",
                    email = "name.surname@gmail.com",
                    role = Role.ADMIN
                )
            )
        )
        groupMembers.add(GroupMember(
            id = Random.nextLong().toString(),
            profilePic = "https://res.cloudinary.com/momentum-media-group-pty-ltd/image/upload/c_fill,q_auto:best,f_auto,e_unsharp_mask:80,w_830,h_478/Space%20Connect%2Fspace-exploration-sc_fm1ysf",
            "name",
            "surname",
            email = "name.surname@gmail.com",
            role = Role.ADMIN
        ),)
    }

    fun retrieveUserProjects() {
        // TODO: MAKE THE REQUEST THEN
        userProjects.addAll(listOf(
            Project(
                id = Random.nextLong().toString(),
                name = "Prova #1",
                creationDate = System.currentTimeMillis(),
                author = PandoroUser(
                    id = Random.nextLong().toString(),
                    profilePic = "https://res.cloudinary.com/momentum-media-group-pty-ltd/image/upload/c_fill,q_auto:best,f_auto,e_unsharp_mask:80,w_830,h_478/Space%20Connect%2Fspace-exploration-sc_fm1ysf",
                    "name",
                    "surname",
                    email = "name.surname@gmail.com"
                ),
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
                    ),
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
                    ),
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
                    ), Group(
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
                    ), Group(
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
                    ), Group(
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
                    ), Group(
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
                    ), Group(
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
                ),
                description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aenean venenatis pulvinar ullamcorper. Aliquam finibus nisi eget enim elementum, quis volutpat risus semper. Praesent mauris velit, lacinia non aliquet ac, faucibus at magna. Ut nec leo elit. In eleifend hendrerit diam, nec rutrum nisi porttitor sed. Sed auctor, massa sed viverra porta, odio erat feugiat tortor, non posuere nulla velit sed lacus. Nam bibendum orci id felis hendrerit, nec suscipit nibh ultricies.\n" +
                        "Donec rutrum aliquet pellentesque. Aenean condimentum venenatis eros a tincidunt. Vivamus rhoncus mauris eget nibh ornare, ut fermentum magna pellentesque. Nulla ac turpis fermentum, ullamcorper lorem in, feugiat lorem. Nam suscipit risus nec neque malesuada, sit amet tempor nisi lobortis. Nulla maximus nisl turpis, ut commodo dui varius a. Mauris bibendum leo eu lorem viverra, in pellentesque neque sagittis. Integer lacinia vestibulum diam quis faucibus. Curabitur dui libero, facilisis vel tristique non, accumsan id augue. Quisque at faucibus tortor. Fusce maximus ante at sapien ultrices bibendum quis quis tortor. In tristique lorem ut nibh lobortis dictum.\n" +
                        "Sed a tempus ligula. Ut pretium lobortis odio, sit amet fermentum felis. Phasellus porttitor lorem eget orci pharetra, in tempor urna aliquam. Nullam feugiat ante felis, sit amet tincidunt massa venenatis et. Proin tincidunt eget nisi ut tempus. Vestibulum lectus tellus, cursus vitae dui nec, egestas gravida mauris. Mauris pharetra accumsan consequat.",
                version = "1.0.0",
                updates = listOf(
                    ProjectUpdate(
                        id = Random.nextLong().toString(),
                        author = PandoroUser(
                            id = Random.nextLong().toString(),
                            profilePic = "https://res.cloudinary.com/momentum-media-group-pty-ltd/image/upload/c_fill,q_auto:best,f_auto,e_unsharp_mask:80,w_830,h_478/Space%20Connect%2Fspace-exploration-sc_fm1ysf",
                            "name",
                            "surname",
                            email = "name.surname@gmail.com"
                        ),
                        targetVersion = "1.0.0",
                        createDate = System.currentTimeMillis(),
                        startDate = System.currentTimeMillis(),
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
                                markedAsDone = Random.nextBoolean()
                            ),
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
                                markedAsDone = Random.nextBoolean()
                            ),
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
                                markedAsDone = Random.nextBoolean()
                            ),
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
                                markedAsDone = Random.nextBoolean()
                            ),
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
                                markedAsDone = Random.nextBoolean()
                            ),
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
                                markedAsDone = Random.nextBoolean()
                            ),
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
                                markedAsDone = Random.nextBoolean()
                            ),
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
                                markedAsDone = Random.nextBoolean()
                            ),
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
                                markedAsDone = Random.nextBoolean()
                            ),
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
                                markedAsDone = Random.nextBoolean()
                            ),
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
                                markedAsDone = Random.nextBoolean()
                            ),
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
                                markedAsDone = Random.nextBoolean()
                            ),
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
                                markedAsDone = Random.nextBoolean()
                            ),
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
                                markedAsDone = Random.nextBoolean()
                            ),
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
                                markedAsDone = Random.nextBoolean()
                            )
                        )
                    )
                )
            )
        ))
    }

    fun loadCandidateMembersState(
        page: Int
    ) {
        val list = retrieveCandidateMembers(
            page = page
        )
        candidateMembersState.appendPage(
            items = if(Random.nextBoolean())
                list
            else
                emptyList(), // TODO: USE THE REAL VALUE
            nextPageKey = page + 1, // TODO: USE THE REAL VALUE
            isLastPage = Random.nextBoolean()  // TODO: USE THE REAL VALUE
        )
    }

    fun retrieveCandidateMembers(
        page: Int = PaginatedResponse.DEFAULT_PAGE
    ) : List<GroupMember> {
        // TODO: MAKE THE REQUEST THEN
        val list = listOf(
            GroupMember(
                id = Random.nextLong().toString(),
                profilePic = "https://res.cloudinary.com/momentum-media-group-pty-ltd/image/upload/c_fill,q_auto:best,f_auto,e_unsharp_mask:80,w_830,h_478/Space%20Connect%2Fspace-exploration-sc_fm1ysf",
                "name",
                "surname",
                email = "name.surname@gmail.com",
                role = Role.ADMIN
            ),

            GroupMember(
                id = Random.nextLong().toString(),
                profilePic = "https://res.cloudinary.com/momentum-media-group-pty-ltd/image/upload/c_fill,q_auto:best,f_auto,e_unsharp_mask:80,w_830,h_478/Space%20Connect%2Fspace-exploration-sc_fm1ysf",
                "name",
                "surname",
                email = "name.surname@gmail.com",
                role = Role.ADMIN
            ),

            GroupMember(
                id = Random.nextLong().toString(),
                profilePic = "https://res.cloudinary.com/momentum-media-group-pty-ltd/image/upload/c_fill,q_auto:best,f_auto,e_unsharp_mask:80,w_830,h_478/Space%20Connect%2Fspace-exploration-sc_fm1ysf",
                "name",
                "surname",
                email = "name.surname@gmail.com",
                role = Role.ADMIN
            ),

            GroupMember(
                id = Random.nextLong().toString(),
                profilePic = "https://res.cloudinary.com/momentum-media-group-pty-ltd/image/upload/c_fill,q_auto:best,f_auto,e_unsharp_mask:80,w_830,h_478/Space%20Connect%2Fspace-exploration-sc_fm1ysf",
                "name",
                "surname",
                email = "name.surname@gmail.com",
                role = Role.ADMIN
            ),
            GroupMember(
                id = Random.nextLong().toString(),
                profilePic = "https://res.cloudinary.com/momentum-media-group-pty-ltd/image/upload/c_fill,q_auto:best,f_auto,e_unsharp_mask:80,w_830,h_478/Space%20Connect%2Fspace-exploration-sc_fm1ysf",
                "name",
                "surname",
                email = "name.surname@gmail.com",
                role = Role.DEVELOPER
            )
        )
        return list
    }

    fun manageProjectCandidate(
        project: Project,
        added: Boolean
    ) {
        if(added) {
            groupProjects.add(project)
            candidateProjects.add(project.id)
        } else {
            groupProjects.remove(project)
            candidateProjects.remove(project.id)
        }
    }

    fun workOnGroup() {
        if(!validForm())
            return
        // TODO: MAKE THE REQUEST THEN
        navigator.goBack()
    }

    private fun validForm() : Boolean {
        if(groupLogo.value.isNullOrEmpty()) {
            viewModelScope.launch {
                showSnackbarMessage(
                    message = getString(
                        resource = Res.string.wrong_logo
                    )
                )
            }
            return false
        }
        if(!isGroupNameValid(groupName.value)) {
            groupNameError.value = true
            return false
        }
        if(!isGroupDescriptionValid(groupDescription.value)) {
            groupDescriptionError.value = true
            return false
        }
        return true
    }

}