package com.tecknobit.pandoro.ui.screens.group.presentation

import com.tecknobit.pandoro.ui.screens.groups.data.Group
import com.tecknobit.pandoro.ui.screens.shared.data.GroupMember
import com.tecknobit.pandoro.ui.screens.shared.data.PandoroUser
import com.tecknobit.pandoro.ui.screens.shared.viewmodels.BaseGroupViewModel
import com.tecknobit.pandoro.ui.screens.shared.viewmodels.BaseGroupViewModel.GroupDeleter
import com.tecknobit.pandorocore.enums.Role
import kotlin.random.Random

class GroupScreenViewModel(
    groupId: String
) : BaseGroupViewModel(), GroupDeleter {

    override fun retrieveGroup() {
        // TODO: MAKE THE REQUEST THEN
        _group.value =
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
    }

}