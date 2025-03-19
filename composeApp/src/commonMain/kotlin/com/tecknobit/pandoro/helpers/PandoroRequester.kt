package com.tecknobit.pandoro.helpers

import com.tecknobit.ametistaengine.AmetistaEngine.Companion.ametistaEngine
import com.tecknobit.equinoxcompose.network.EquinoxRequester
import com.tecknobit.equinoxcore.annotations.RequestPath
import com.tecknobit.equinoxcore.annotations.Wrapper
import com.tecknobit.equinoxcore.helpers.IDENTIFIER_KEY
import com.tecknobit.equinoxcore.helpers.NAME_KEY
import com.tecknobit.equinoxcore.network.RequestMethod.DELETE
import com.tecknobit.equinoxcore.network.RequestMethod.GET
import com.tecknobit.equinoxcore.network.RequestMethod.PATCH
import com.tecknobit.equinoxcore.network.RequestMethod.POST
import com.tecknobit.equinoxcore.network.RequestMethod.PUT
import com.tecknobit.equinoxcore.pagination.PaginatedResponse.Companion.DEFAULT_PAGE
import com.tecknobit.equinoxcore.pagination.PaginatedResponse.Companion.DEFAULT_PAGE_SIZE
import com.tecknobit.equinoxcore.pagination.PaginatedResponse.Companion.PAGE_KEY
import com.tecknobit.equinoxcore.pagination.PaginatedResponse.Companion.PAGE_SIZE_KEY
import com.tecknobit.pandoro.ui.screens.shared.data.GroupMember
import com.tecknobit.pandorocore.CHANGELOGS_KEY
import com.tecknobit.pandorocore.CHANGELOG_IDENTIFIER_KEY
import com.tecknobit.pandorocore.CONTENT_NOTE_KEY
import com.tecknobit.pandorocore.FILTERS_KEY
import com.tecknobit.pandorocore.GROUPS_KEY
import com.tecknobit.pandorocore.GROUP_DESCRIPTION_KEY
import com.tecknobit.pandorocore.GROUP_IDENTIFIER_KEY
import com.tecknobit.pandorocore.GROUP_LOGO_KEY
import com.tecknobit.pandorocore.GROUP_MEMBERS_KEY
import com.tecknobit.pandorocore.MARKED_AS_DONE_KEY
import com.tecknobit.pandorocore.MEMBER_ROLE_KEY
import com.tecknobit.pandorocore.NOTES_KEY
import com.tecknobit.pandorocore.ONLY_AUTHORED_GROUPS
import com.tecknobit.pandorocore.PROJECTS_KEY
import com.tecknobit.pandorocore.PROJECT_DESCRIPTION_KEY
import com.tecknobit.pandorocore.PROJECT_ICON_KEY
import com.tecknobit.pandorocore.PROJECT_REPOSITORY_KEY
import com.tecknobit.pandorocore.PROJECT_VERSION_KEY
import com.tecknobit.pandorocore.ROLES_FILTER_KEY
import com.tecknobit.pandorocore.UPDATE_CHANGE_NOTES_KEY
import com.tecknobit.pandorocore.UPDATE_TARGET_VERSION_KEY
import com.tecknobit.pandorocore.enums.Role
import com.tecknobit.pandorocore.helpers.PandoroEndpoints.ACCEPT_GROUP_INVITATION_ENDPOINT
import com.tecknobit.pandorocore.helpers.PandoroEndpoints.ADD_CHANGE_NOTE_ENDPOINT
import com.tecknobit.pandorocore.helpers.PandoroEndpoints.ADD_MEMBERS_ENDPOINT
import com.tecknobit.pandorocore.helpers.PandoroEndpoints.AUTHORED_PROJECTS_ENDPOINT
import com.tecknobit.pandorocore.helpers.PandoroEndpoints.CANDIDATE_GROUP_MEMBERS_ENDPOINT
import com.tecknobit.pandorocore.helpers.PandoroEndpoints.CHANGE_MEMBER_ROLE_ENDPOINT
import com.tecknobit.pandorocore.helpers.PandoroEndpoints.CHANGE_NOTE_STATUS_ENDPOINT
import com.tecknobit.pandorocore.helpers.PandoroEndpoints.COUNT_CANDIDATE_GROUP_MEMBERS_ENDPOINT
import com.tecknobit.pandorocore.helpers.PandoroEndpoints.DECLINE_GROUP_INVITATION_ENDPOINT
import com.tecknobit.pandorocore.helpers.PandoroEndpoints.EDIT_PROJECTS_ENDPOINT
import com.tecknobit.pandorocore.helpers.PandoroEndpoints.IN_DEVELOPMENT_PROJECTS_ENDPOINT
import com.tecknobit.pandorocore.helpers.PandoroEndpoints.LEAVE_GROUP_ENDPOINT
import com.tecknobit.pandorocore.helpers.PandoroEndpoints.MARK_CHANGE_NOTE_AS_DONE_ENDPOINT
import com.tecknobit.pandorocore.helpers.PandoroEndpoints.MARK_CHANGE_NOTE_AS_TODO_ENDPOINT
import com.tecknobit.pandorocore.helpers.PandoroEndpoints.OVERVIEW_ENDPOINT
import com.tecknobit.pandorocore.helpers.PandoroEndpoints.PUBLISH_UPDATE_ENDPOINT
import com.tecknobit.pandorocore.helpers.PandoroEndpoints.REMOVE_MEMBER_ENDPOINT
import com.tecknobit.pandorocore.helpers.PandoroEndpoints.SCHEDULE_UPDATE_ENDPOINT
import com.tecknobit.pandorocore.helpers.PandoroEndpoints.START_UPDATE_ENDPOINT
import com.tecknobit.pandorocore.helpers.PandoroEndpoints.UNREAD_CHANGELOGS_ENDPOINT
import com.tecknobit.pandorocore.helpers.PandoroEndpoints.UPDATES_PATH
import io.ktor.client.request.forms.formData
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.content.PartData
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

/**
 * The **PandoroRequester** class is useful to communicate with the Pandoro's backend
 *
 * @param host The host where is running the Pandoro's backend
 * @param userId The user identifier
 * @param userToken The user token
 *
 * @author N7ghtm4r3 - Tecknobit
 */
open class PandoroRequester(
    host: String,
    userId: String? = null,
    userToken: String? = null,
    debugMode: Boolean = false,
) : EquinoxRequester(
    host = host,
    userId = userId,
    userToken = userToken,
    byPassSSLValidation = true,
    connectionErrorMessage = "Server is temporarily unavailable",
    debugMode = debugMode
) {

    init {
        attachInterceptorOnRequest {
            ametistaEngine.notifyNetworkRequest()
        }
    }

    /**
     * Method to execute the request to get the projects list of the user where him/her is the author
     *
     * @return the result of the request as [JsonObject]
     *
     */
    @RequestPath(path = "/api/v1/users/{id}/projects/authored", method = GET)
    suspend fun getAuthoredProjects(): JsonObject {
        return execGet(
            endpoint = createProjectEndpoint(
                subEndpoint = AUTHORED_PROJECTS_ENDPOINT
            )
        )
    }

    /**
     * Method to execute the request to get the projects list of the user
     *
     * @return the result of the request as [JsonObject]
     *
     */
    @RequestPath(path = "/api/v1/users/{id}/projects/in_development", method = GET)
    suspend fun getInDevelopmentProjects(
        page: Int = DEFAULT_PAGE,
        pageSize: Int = DEFAULT_PAGE_SIZE,
        filters: String
    ): JsonObject {
        val query = createProjectsQuery(
            page = page,
            pageSize = pageSize,
            filters = filters
        )
        return execGet(
            endpoint = createProjectEndpoint(
                subEndpoint = IN_DEVELOPMENT_PROJECTS_ENDPOINT
            ),
            query = query
        )
    }

    /**
     * Method to execute the request to get the projects list of the user
     *
     * @return the result of the request as [JsonObject]
     *
     */
    @RequestPath(path = "/api/v1/users/{id}/projects", method = GET)
    suspend fun getProjects(
        page: Int = DEFAULT_PAGE,
        pageSize: Int = DEFAULT_PAGE_SIZE,
        filters: String
    ): JsonObject {
        val query = createProjectsQuery(
            page = page,
            pageSize = pageSize,
            filters = filters
        )
        return execGet(
            endpoint = createProjectEndpoint(),
            query = query
        )
    }

    /**
     * Method to create the query with the pagination parameters for the [getAuthoredProjects] and
     * [getProjects] requests
     *
     * @param page The number of the page to request to the backend
     * @param pageSize The size of the result for the page
     * @param filters The filters to apply to retrieve the projects
     */
    private fun createProjectsQuery(
        page: Int = DEFAULT_PAGE,
        pageSize: Int = DEFAULT_PAGE_SIZE,
        filters: String
    ) : JsonObject {
        return buildJsonObject {
            put(PAGE_KEY, page)
            put(PAGE_SIZE_KEY, pageSize)
            put(FILTERS_KEY, filters)
        }
    }

    /**
     * Method to execute the request to add a new project or edit an exiting project
     *
     * @param icon The icon of the project
     * @param iconName The name of the icon of the project
     * @param projectId The identifier of the project to edit
     * @param name The name of the project
     * @param projectDescription The description of the project
     * @param projectVersion The current version of the project
     * @param groups The list of groups where the project can be visible
     * @param projectRepository The url of the repository of the project
     *
     * @return the result of the request as [JsonObject]
     *
     */
    suspend fun workOnProject(
        icon: ByteArray?,
        iconName: String?,
        projectId: String? = null,
        name: String,
        projectDescription: String,
        projectVersion: String,
        groups: List<String>,
        projectRepository: String = ""
    ) : JsonObject {
        val payload = formData {
            icon?.let {
                append(PROJECT_ICON_KEY, icon, Headers.build {
                    append(HttpHeaders.ContentType, "image/*")
                    append(HttpHeaders.ContentDisposition, "filename=\"$iconName\"")
                })
            }
            append(NAME_KEY, name)
            append(PROJECT_DESCRIPTION_KEY, projectDescription)
            append(PROJECT_VERSION_KEY, projectVersion)
            append(GROUPS_KEY, groups.joinToString())
            append(PROJECT_REPOSITORY_KEY, projectRepository)
        }
        return if(projectId == null) {
            addProject(
                payload = payload
            )
        } else {
            editProject(
                projectId = projectId,
                payload = payload
            )
        }
    }


    /**
     * Method to execute the request to add a new project of the user
     *
     * @param payload The payload with the project details
     *
     * @return the result of the request as [JsonObject]
     *
     */
    @RequestPath(path = "/api/v1/users/{id}/projects", method = POST)
    private suspend fun addProject(
        payload: List<PartData>
    ): JsonObject {
        return execMultipartRequest(
            endpoint = createProjectEndpoint(),
            payload = payload
        )
    }

    /**
     * Method to execute the request to edit an existing project of the user
     *
     * @param projectId The project identifier
     * @param payload The payload with the project details
     *
     * @return the result of the request as [JsonObject]
     *
     */
    @RequestPath(path = "/api/v1/users/{id}/projects/{project_id}", method = PATCH)
    private suspend fun editProject(
        projectId: String,
        payload: List<PartData>
    ): JsonObject {
        return execMultipartRequest(
            endpoint = createProjectEndpoint(
                id = projectId
            ),
            payload = payload
        )
    }

    /**
     * Method to execute the request to get a project of the user
     *
     * @param projectId The project identifier of the project to fetch
     *
     * @return the result of the request as [JsonObject]
     *
     */
    @RequestPath(path = "/api/v1/users/{id}/projects/{project_id}", method = GET)
    suspend fun getProject(
        projectId: String
    ): JsonObject {
        return execGet(
            endpoint = createProjectEndpoint(
                id = projectId
            )
        )
    }

    /**
     * Method to execute the request to delete a project of the user
     *
     * @param projectId The project identifier of the project to delete
     *
     * @return the result of the request as [JsonObject]
     *
     */
    @RequestPath(path = "/api/v1/users/{id}/projects/{project_id}", method = DELETE)
    suspend fun deleteProject(
        projectId: String
    ): JsonObject {
        return execDelete(
            endpoint = createProjectEndpoint(
                id = projectId
            )
        )
    }

    /**
     * Method to execute the request to schedule a new update for a project
     *
     * @param projectId The project identifier where schedule the new update
     * @param targetVersion The target version of the update
     * @param updateChangeNotes The change notes of the update
     *
     * @return the result of the request as [JsonObject]
     *
     */
    @RequestPath(path = "/api/v1/users/{id}/projects/{project_id}/updates/schedule", method = POST)
    suspend fun scheduleUpdate(
        projectId: String,
        targetVersion: String,
        updateChangeNotes: List<String>
    ): JsonObject {
        val payload = buildJsonObject {
            put(UPDATE_TARGET_VERSION_KEY, targetVersion)
            put(UPDATE_CHANGE_NOTES_KEY, updateChangeNotes.joinToString())
        }
        return execPost(
            endpoint = createUpdatesEndpoint(
                subEndpoint = SCHEDULE_UPDATE_ENDPOINT,
                projectId = projectId
            ),
            payload = payload
        )
    }

    /**
     * Method to execute the request to start an existing update of a project
     *
     * @param projectId The project identifier where start an update
     * @param updateId The update identifier of the update to start
     *
     * @return the result of the request as [JsonObject]
     *
     */
    @RequestPath(
        path = "/api/v1/users/{id}/projects/{project_id}/updates/{update_id}/start",
        method = PATCH
    )
    suspend fun startUpdate(
        projectId: String,
        updateId: String
    ): JsonObject {
        return execPatch(
            endpoint = createUpdatesEndpoint(
                subEndpoint = START_UPDATE_ENDPOINT,
                projectId = projectId,
                updateId = updateId
            )
        )
    }

    /**
     * Method to execute the request to publish an existing update of a project
     *
     * @param projectId The project identifier where publish an update
     * @param updateId The update identifier of the update to publish
     *
     * @return the result of the request as [JsonObject]
     *
     */
    @RequestPath(
        path = "/api/v1/users/{id}/projects/{project_id}/updates/{update_id}/publish",
        method = PATCH
    )
    suspend fun publishUpdate(
        projectId: String,
        updateId: String
    ): JsonObject {
        return execPatch(
            endpoint = createUpdatesEndpoint(
                subEndpoint = PUBLISH_UPDATE_ENDPOINT,
                projectId = projectId,
                updateId = updateId
            )
        )
    }

    /**
     * Wrapper method to execute the request to mark a change note as done
     *
     * @param projectId The project identifier
     * @param updateId The update identifier
     * @param changeNoteId The note identifier to mark as done
     * @param completed The status to set to the current change note
     *
     * @return the result of the request as [JsonObject]
     *
     */
    suspend fun workOnChangeNoteStatus(
        projectId: String,
        updateId: String,
        changeNoteId: String,
        completed: Boolean
    ) : JsonObject {
        return if(completed) {
            markChangeNoteAsDone(
                projectId = projectId,
                updateId = updateId,
                changeNoteId = changeNoteId
            )
        } else {
            markChangeNoteAsToDo(
                projectId = projectId,
                updateId = updateId,
                changeNoteId = changeNoteId
            )
        }
    }

    /**
     * Method to execute the request to mark a change note as done
     *
     * @param projectId The project identifier
     * @param updateId The update identifier
     * @param changeNoteId The note identifier to mark as done
     *
     * @return the result of the request as [JsonObject]
     *
     */
    @RequestPath(
        path = "/api/v1/users/{id}/projects/{project_id}/updates/{update_id}/notes/{note_id}/markChangeNoteAsDone",
        method = PATCH
    )
    private suspend fun markChangeNoteAsDone(
        projectId: String,
        updateId: String,
        changeNoteId: String
    ): JsonObject {
        return execPatch(
            endpoint = createUpdatesEndpoint(
                subEndpoint = "/${NOTES_KEY}/$changeNoteId${MARK_CHANGE_NOTE_AS_DONE_ENDPOINT}",
                projectId = projectId,
                updateId = updateId
            )
        )
    }

    /**
     * Method to execute the request to mark a change note as to-do
     *
     * @param projectId The project identifier
     * @param updateId The update identifier
     * @param changeNoteId The note identifier to mark as to-do
     *
     * @return the result of the request as [JsonObject]
     *
     */
    @RequestPath(
        path = "/api/v1/users/{id}/projects/{project_id}/updates/{update_id}/notes/{note_id}/markChangeNoteAsToDo",
        method = PATCH
    )
    private suspend fun markChangeNoteAsToDo(
        projectId: String,
        updateId: String,
        changeNoteId: String
    ): JsonObject {
        return execPatch(
            endpoint = createUpdatesEndpoint(
                subEndpoint = "/${NOTES_KEY}/$changeNoteId${MARK_CHANGE_NOTE_AS_TODO_ENDPOINT}",
                projectId = projectId,
                updateId = updateId
            )
        )
    }

    /**
     * Method to execute the request to delete change note of an update
     *
     * @param projectId The project identifier
     * @param updateId The update identifier
     * @param changeNoteId The note identifier to delete
     *
     * @return the result of the request as [JsonObject]
     *
     */
    @RequestPath(
        path = "/api/v1/users/{id}/projects/{project_id}/updates/{update_id}/notes/{note_id}",
        method = DELETE
    )
    suspend fun deleteChangeNote(
        projectId: String,
        updateId: String,
        changeNoteId: String
    ): JsonObject {
        return execDelete(
            endpoint = createUpdatesEndpoint(
                subEndpoint = "/${NOTES_KEY}/$changeNoteId",
                projectId = projectId,
                updateId = updateId
            )
        )
    }

    /**
     * Method to execute the request to delete an update
     *
     * @param projectId The project identifier
     * @param updateId The update identifier to delete
     *
     * @return the result of the request as [JsonObject]
     *
     */
    @RequestPath(path = "/api/v1/users/{id}/projects/{project_id}/updates/{update_id}", method = DELETE)
    suspend fun deleteUpdate(
        projectId: String,
        updateId: String,
    ): JsonObject {
        return execDelete(
            endpoint = createUpdatesEndpoint(
                projectId = projectId,
                updateId = updateId
            )
        )
    }

    /**
     * Method to an endpoint to make the request to the projects/updates controller
     *
     * @param subEndpoint The path sub-endpoint of the url
     * @param projectId The project identifier
     * @param updateId The update identifier
     *
     * @return an endpoint to make the request as [JsonObject]
     */
    @Wrapper
    private fun createUpdatesEndpoint(
        subEndpoint: String = "",
        projectId: String,
        updateId: String? = null,
    ): String {
        var updatesEndpointPart = UPDATES_PATH
        updatesEndpointPart += if (updateId != null)
            updateId + subEndpoint
        else
            subEndpoint
        return createProjectEndpoint(
            subEndpoint = updatesEndpointPart,
            id = projectId
        )
    }

    /**
     * Method to an subEndpoint to make the request to the projects controller
     *
     * @param subEndpoint The path of the sub-endpoint of the url
     * @param id The eventual identifier to create the path variable
     *
     * @return an subEndpoint to make the request as [JsonObject]
     */
    @Wrapper
    private fun createProjectEndpoint(
        subEndpoint: String? = null,
        id: String? = null
    ): String {
        return createEndpoint(
            baseEndpoint = PROJECTS_KEY,
            subEndpoint = subEndpoint,
            id = id
        )
    }

    /**
     * Method to execute the request to get the groups list of the user where him/her is the author
     *
     * @param page The number of the page to request to the backend
     * @param pageSize The size of the result for the page
     * @param nameFilter The name to use as filter to retrieve authored groups
     *
     * @return the result of the request as [JsonObject]
     *
     */
    @Wrapper
    @RequestPath(path = "/api/v1/users/{id}/groups", method = GET)
    suspend fun getAuthoredGroups(
        page: Int = DEFAULT_PAGE,
        pageSize: Int = DEFAULT_PAGE_SIZE,
        nameFilter: String = ""
    ): JsonObject {
        return getGroups(
            page = page,
            pageSize = pageSize,
            onlyAuthoredGroups = true,
            nameFilter = nameFilter
        )
    }

    /**
     * Method to execute the request to get the groups list of the user
     *
     * @param page The number of the page to request to the backend
     * @param pageSize The size of the result for the page
     * @param onlyAuthoredGroups Whether retrieve only the groups list of the user where him/her is the author
     * @param nameFilter The name to use as filter to retrieve authored groups
     * @param roles The roles list to use as filter to retrieve the groups list
     *
     * @return the result of the request as [JsonObject]
     *
     */
    @RequestPath(path = "/api/v1/users/{id}/groups", method = GET)
    suspend fun getGroups(
        page: Int = DEFAULT_PAGE,
        pageSize: Int = DEFAULT_PAGE_SIZE,
        onlyAuthoredGroups: Boolean = false,
        nameFilter: String = "",
        roles: List<Role> = emptyList()
    ): JsonObject {
        val query = buildJsonObject {
            put(PAGE_KEY, page)
            put(PAGE_SIZE_KEY, pageSize)
            put(ONLY_AUTHORED_GROUPS, onlyAuthoredGroups)
            put(NAME_KEY, nameFilter)
            put(ROLES_FILTER_KEY, roles.joinToString())
        }
        return execGet(
            endpoint = createGroupsEndpoint(),
            query = query
        )
    }

    /**
     * Method to execute the request to count the candidates member available
     *
     * @param membersToExclude The numbers of the members exclude
     *
     * @return the result of the request as [JsonObject]
     */
    suspend fun countCandidatesMember(
        membersToExclude: Int
    ) : JsonObject {
        val query = buildJsonObject {
            put(GROUP_MEMBERS_KEY, membersToExclude)
        }
        return execGet(
            endpoint = assembleUsersEndpointPath(
                endpoint = COUNT_CANDIDATE_GROUP_MEMBERS_ENDPOINT
            ),
            query = query
        )
    }

    /**
     * Method to execute the request to get the candidates member for a group
     *
     * @param page The number of the page to request to the backend
     * @param pageSize The size of the result for the page
     * @param membersToExclude The members exclude
     *
     * @return the result of the request as [JsonObject]
     */
    suspend fun getCandidateMembers(
        page: Int = DEFAULT_PAGE,
        pageSize: Int = DEFAULT_PAGE_SIZE,
        membersToExclude: List<String>
    ) : JsonObject {
        val query = buildJsonObject {
            put(PAGE_KEY, page)
            put(PAGE_SIZE_KEY, pageSize)
            put(GROUP_MEMBERS_KEY, membersToExclude.joinToString())
        }
        return execGet(
            endpoint = assembleUsersEndpointPath(
                endpoint = CANDIDATE_GROUP_MEMBERS_ENDPOINT
            ),
            query = query
        )
    }

    /**
     * Method to execute the request to create a new group or edit an exiting group
     *
     * @param groupId The identifier of the group
     * @param logo The logo of the group
     * @param logoName The name of the logo of the group
     * @param name The name of the group
     * @param description The description of the group
     * @param members The members to add in the group
     * @param projects The projects to share with the group
     *
     * @return the result of the request as [JsonObject]
     *
     */
    suspend fun workOnGroup(
        groupId: String?,
        logo: ByteArray?,
        logoName: String?,
        name: String,
        description: String,
        members: List<GroupMember>,
        projects: List<String>
    ) : JsonObject {
        val payload = formData {
            logo?.let {
                append(GROUP_LOGO_KEY, logo, Headers.build {
                    append(HttpHeaders.ContentType, "image/*")
                    append(HttpHeaders.ContentDisposition, "filename=\"$logoName\"")
                })
            }
            append(NAME_KEY, name)
            append(GROUP_DESCRIPTION_KEY, description)
            append(GROUP_MEMBERS_KEY, members.joinToString { member -> member.id })
            append(PROJECTS_KEY, projects.joinToString())
        }
        return if(groupId == null) {
            createGroup(
                payload = payload
            )
        } else {
            editGroup(
                groupId = groupId,
                payload = payload
            )
        }
    }

    /**
     * Method to execute the request to create a new group
     *
     * @param payload The payload with the group details
     *
     * @return the result of the request as [JsonObject]
     *
     */
    @RequestPath(path = "/api/v1/users/{id}/groups", method = POST)
    private suspend fun createGroup(
        payload: List<PartData>
    ): JsonObject {
        return execMultipartRequest(
            endpoint = createGroupsEndpoint(),
            payload = payload
        )
    }

    /**
     * Method to execute the request to create a new group
     *
     * @param payload The payload with the group details
     *
     * @return the result of the request as [JsonObject]
     *
     */
    @RequestPath(path = "/api/v1/users/{id}/groups/{group_id}", method = POST)
    private suspend fun editGroup(
        groupId: String,
        payload: List<PartData>
    ): JsonObject {
        return execMultipartRequest(
            endpoint = createGroupsEndpoint(
                id = groupId
            ),
            payload = payload
        )
    }

    /**
     * Method to execute the request to get a group of the user
     *
     * @param groupId The group identifier of the group to fetch
     *
     * @return the result of the request as [JsonObject]
     *
     */
    @RequestPath(path = "/api/v1/users/{id}/groups/{group_id}", method = GET)
    suspend fun getGroup(
        groupId: String
    ): JsonObject {
        return execGet(
            endpoint = createGroupsEndpoint(
                id = groupId
            )
        )
    }

    /**
     * Method to execute the request to add members to a group
     *
     * @param groupId The group identifier where add the members
     * @param members The list of the members to add
     *
     * @return the result of the request as [JsonObject]
     *
     */
    @RequestPath(path = "/api/v1/users/{id}/groups/{group_id}/addMembers", method = PUT)
    suspend fun addMembers(
        groupId: String,
        members: List<String>
    ): JsonObject {
        val payload = buildJsonObject {
            put(GROUP_MEMBERS_KEY, members.joinToString())
        }
        return execPut(
            endpoint = createGroupsEndpoint(
                subEndpoint = ADD_MEMBERS_ENDPOINT,
                id = groupId
            ),
            payload = payload
        )
    }

    /**
     * Method to execute the request to accept a group invitation
     *
     * @param groupId The group identifier of the group to accept the invitation
     * @param changelogId The changelog identifier to delete
     *
     * @return the result of the request as [JsonObject]
     *
     */
    @RequestPath(path = "/api/v1/users/{id}/groups/{group_id}/acceptGroupInvitation", method = PATCH)
    suspend fun acceptInvitation(
        groupId: String,
        changelogId: String
    ): JsonObject {
        val payload = buildJsonObject {
            put(CHANGELOG_IDENTIFIER_KEY, changelogId)
        }
        return execPatch(
            endpoint = createGroupsEndpoint(
                subEndpoint = ACCEPT_GROUP_INVITATION_ENDPOINT,
                id = groupId
            ),
            payload = payload
        )
    }

    /**
     * Method to execute the request to decline a group invitation
     *
     * @param groupId The group identifier of the group to decline the invitation
     * @param changelogId The changelog identifier to delete
     *
     * @return the result of the request as [JsonObject]
     *
     */
    @RequestPath(path = "/api/v1/users/{id}/groups/{group_id}/declineGroupInvitation", method = DELETE)
    suspend fun declineInvitation(
        groupId: String,
        changelogId: String
    ): JsonObject {
        val payload = buildJsonObject {
            put(CHANGELOG_IDENTIFIER_KEY, changelogId)
        }
        return execDelete(
            endpoint = createGroupsEndpoint(
                subEndpoint = DECLINE_GROUP_INVITATION_ENDPOINT,
                id = groupId
            ),
            payload = payload
        )
    }

    /**
     * Method to execute the request to change a role of a member of a group
     *
     * @param groupId The group identifier of the group where change the role
     * @param memberId The identifier of the member to change the role
     * @param role The new role of the user
     *
     * @return the result of the request as [JsonObject]
     *
     */
    @RequestPath(path = "/api/v1/users/{id}/groups/{group_id}/changeMemberRole", method = PATCH)
    suspend fun changeMemberRole(
        groupId: String,
        memberId: String,
        role: Role
    ): JsonObject {
        val payload = buildJsonObject {
            put(IDENTIFIER_KEY, memberId)
            put(MEMBER_ROLE_KEY, role.name)
        }
        return execPatch(
            endpoint = createGroupsEndpoint(
                subEndpoint = CHANGE_MEMBER_ROLE_ENDPOINT,
                id = groupId
            ),
            payload = payload
        )
    }

    /**
     * Method to execute the request to remove a member from a group
     *
     * @param groupId The group identifier of the group where change the role
     * @param memberId The identifier of the member to remove
     *
     * @return the result of the request as [JsonObject]
     *
     */
    @RequestPath(path = "/api/v1/users/{id}/groups/{group_id}/removeMember", method = DELETE)
    suspend fun removeMember(
        groupId: String,
        memberId: String,
    ): JsonObject {
        val payload = buildJsonObject {
            put(IDENTIFIER_KEY, memberId)
        }
        return execDelete(
            endpoint = createGroupsEndpoint(
                subEndpoint = REMOVE_MEMBER_ENDPOINT,
                id = groupId
            ),
            payload = payload
        )
    }

    /**
     * Method to execute the request to edit a projects list of a group
     *
     * @param groupId The group identifier of the group where edit the projects
     * @param projects The list of the projects for the group
     *
     * @return the result of the request as [JsonObject]
     *
     */
    @RequestPath(path = "/api/v1/users/{id}/groups/{group_id}/editProjects", method = PATCH)
    suspend fun editProjects(
        groupId: String,
        projects: List<String>
    ): JsonObject {
        val payload = buildJsonObject {
            put(PROJECTS_KEY, projects.joinToString())
        }
        return execPatch(
            endpoint = createGroupsEndpoint(
                subEndpoint = EDIT_PROJECTS_ENDPOINT,
                id = groupId
            ),
            payload = payload
        )
    }

    /**
     * Method to execute the request to leave from a group
     *
     * @param groupId The group identifier of the group from leave
     *
     * @return the result of the request as [JsonObject]
     *
     */
    @RequestPath(path = "/api/v1/users/{id}/groups/{group_id}/leaveGroup", method = DELETE)
    suspend fun leaveGroup(
        groupId: String
    ): JsonObject {
        return execDelete(
            endpoint = createGroupsEndpoint(
                subEndpoint = LEAVE_GROUP_ENDPOINT,
                id = groupId
            )
        )
    }

    /**
     * Method to execute the request to delete a group
     *
     * @param groupId The group identifier of the group to delete
     *
     * @return the result of the request as [JsonObject]
     *
     */
    @RequestPath(path = "/api/v1/users/{id}/groups/{group_id}", method = DELETE)
    suspend fun deleteGroup(
        groupId: String
    ): JsonObject {
        return execDelete(
            endpoint = createGroupsEndpoint(
                id = groupId
            )
        )
    }

    /**
     * Method to an endpoint to make the request to the groups controller
     *
     * @param subEndpoint The path of the sub-endpoint of the url
     * @param id The eventual identifier to create the path variable
     *
     * @return an endpoint to make the request as [JsonObject]
     */
    @Wrapper
    private fun createGroupsEndpoint(
        subEndpoint: String? = null,
        id: String? = null
    ): String {
        return createEndpoint(
            baseEndpoint = GROUPS_KEY,
            subEndpoint = subEndpoint,
            id = id
        )
    }

    /**
     * Method to execute the request to get the notes list of the user
     *
     * @param page The number of the page to request to the backend
     * @param pageSize The size of the result for the page
     * @param selectToDoNotes Whether select the yet to-do notes
     * @param selectCompletedNotes Whether select the completed notes
     *
     * @return the result of the request as [JsonObject]
     */
    @RequestPath(path = "/api/v1/users/{id}/notes", method = GET)
    suspend fun getNotes(
        page: Int = DEFAULT_PAGE,
        pageSize: Int = DEFAULT_PAGE_SIZE,
        selectToDoNotes: Boolean,
        selectCompletedNotes: Boolean
    ): JsonObject {
        val selectNotes = if(selectToDoNotes && selectCompletedNotes)
            ""
        else
            selectCompletedNotes.toString()
        val query = buildJsonObject {
            put(PAGE_KEY, page)
            put(PAGE_SIZE_KEY, pageSize)
            put(MARKED_AS_DONE_KEY, selectNotes)
        }
        return execGet(
            endpoint = createNotesEndpoint(),
            query = query
        )
    }

    /**
     * Method to execute the request to get the notes list of the user
     *
     * @param noteId The identifier of the note to get
     *
     * @return the result of the request as [JsonObject]
     *
     */
    @RequestPath(path = "/api/v1/users/{id}/notes/{note_id}", method = GET)
    suspend fun getNote(
        noteId: String
    ): JsonObject {
        return execGet(
            endpoint = createNotesEndpoint(
                id = noteId
            )
        )
    }

    /**
     * Wrapper method to execute the request to add or edit a note
     *
     * @param noteId The identifier of the note to edit
     * @param projectId The project identifier
     * @param updateId The update identifier
     * @param contentNote The content of the note
     *
     * @return the result of the request as [JsonObject]
     *
     */
    suspend fun workOnNote(
        noteId: String?,
        projectId: String?,
        updateId: String?,
        contentNote: String
    ) : JsonObject {
        val payload = buildJsonObject {
            put(CONTENT_NOTE_KEY, contentNote)
        }
        return if(noteId == null) {
            if(updateId == null) {
                addNote(
                    payload = payload
                )
            } else {
                addChangeNote(
                    projectId = projectId!!,
                    updateId = updateId,
                    payload = payload
                )
            }
        } else {
            if(updateId == null) {
                editNote(
                    noteId = noteId,
                    payload = payload
                )
            } else {
                editChangeNote(
                    projectId = projectId!!,
                    updateId = updateId,
                    noteId = noteId,
                    payload = payload
                )
            }
        }
    }

    /**
     * Method to execute the request to add a new note of the user
     * @param payload The content of the new note to add
     *
     * @return the result of the request as [JsonObject]
     *
     */
    @RequestPath(path = "/api/v1/users/{id}/notes", method = POST)
    private suspend fun addNote(
        payload: JsonObject
    ): JsonObject {
        return execPost(
            endpoint = createNotesEndpoint(),
            payload = payload
        )
    }

    /**
     * Method to execute the request to add a new note of the user
     * @param noteId The identifier of the note to edit
     * @param payload The content of the existing note to edit
     *
     * @return the result of the request as [JsonObject]
     *
     */
    @RequestPath(path = "/api/v1/users/{id}/notes/{note_id}", method = PATCH)
    private suspend fun editNote(
        noteId: String,
        payload: JsonObject
    ): JsonObject {
        return execPatch(
            endpoint = createNotesEndpoint(
                id = noteId
            ),
            payload = payload
        )
    }

    /**
     * Method to execute the request to add a new change note to an update
     *
     * @param projectId The project identifier
     * @param updateId The update identifier where add the change note
     * @param payload The content of the change note to add
     *
     * @return the result of the request as [JsonObject]
     *
     */
    @RequestPath(
        path = "/api/v1/users/{id}/projects/{project_id}/updates/{update_id}/addChangeNote",
        method = PUT
    )
    suspend fun addChangeNote(
        projectId: String,
        updateId: String,
        payload: JsonObject
    ): JsonObject {
        return execPut(
            endpoint = createUpdatesEndpoint(
                subEndpoint = ADD_CHANGE_NOTE_ENDPOINT,
                projectId = projectId,
                updateId = updateId
            ),
            payload = payload
        )
    }

    /**
     * Method to execute the request to edit an existing change note of an update
     *
     * @param projectId The project identifier
     * @param updateId The update identifier where add the change note
     * @param noteId The note identifier of the note to edit
     * @param payload The content of the change note to add
     *
     * @return the result of the request as [JsonObject]
     *
     */
    @RequestPath(
        path = "/api/v1/users/{id}/projects/{project_id}/updates/{update_id}/notes/{note_id}",
        method = PATCH
    )
    suspend fun editChangeNote(
        projectId: String,
        updateId: String,
        noteId: String,
        payload: JsonObject
    ): JsonObject {
        return execPatch(
            endpoint = createUpdatesEndpoint(
                subEndpoint = "/$NOTES_KEY/$noteId",
                projectId = projectId,
                updateId = updateId
            ),
            payload = payload
        )
    }

    /**
     * Method to execute the request to mark a user's note as done
     *
     * @param noteId The note identifier to mark as done
     * @param completed Whether the note is completed
     *
     * @return the result of the request as [JsonObject]
     *
     */
    @RequestPath(path = "/api/v1/users/{id}/notes/{note_id}/changeNoteStatus", method = PATCH)
    suspend fun changeNoteStatus(
        noteId: String,
        completed: Boolean
    ): JsonObject {
        val payload = buildJsonObject {
            put(MARKED_AS_DONE_KEY, completed)
        }
        return execPatch(
            endpoint = createNotesEndpoint(
                subEndpoint = CHANGE_NOTE_STATUS_ENDPOINT,
                id = noteId
            ),
            payload = payload
        )
    }

    /**
     * Method to execute the request to delete a user's note
     * @param noteId The note identifier to delete
     *
     * @return the result of the request as [JsonObject]
     *
     */
    @RequestPath(path = "/api/v1/users/{id}/notes/{note_id}", method = DELETE)
    suspend fun deleteNote(
        noteId: String
    ): JsonObject {
        return execDelete(
            endpoint = createNotesEndpoint(
                id = noteId
            )
        )
    }

    /**
     * Method to an endpoint to make the request to the notes controller
     *
     * @param subEndpoint The path of the sub-endpoint of the url
     * @param id The eventual identifier to create the path variable
     *
     * @return an endpoint to make the request as [JsonObject]
     */
    @Wrapper
    private fun createNotesEndpoint(
        subEndpoint: String? = null,
        id: String? = null
    ): String {
        return createEndpoint(
            baseEndpoint = NOTES_KEY,
            subEndpoint = subEndpoint,
            id = id
        )
    }

    /**
     * Method to execute the request to get the changelogs list of the user
     *
     * @return the result of the request as [JsonObject]
     *
     */
    @RequestPath(path = "/api/v1/users/{id}/changelogs/unread", method = GET)
    suspend fun getUnreadChangelogsCount(): JsonObject {
        return execGet(
            endpoint = assembleCustomEndpointPath(
                customEndpoint = CHANGELOGS_KEY,
                subEndpoint = UNREAD_CHANGELOGS_ENDPOINT
            )
        )
    }

    /**
     * Method to execute the request to get the changelogs list of the user
     *
     * @param page The number of the page to request to the backend
     * @param pageSize The size of the result for the page
     *
     * @return the result of the request as [JsonObject]
     *
     */
    @RequestPath(path = "/api/v1/users/{id}/changelogs", method = GET)
    suspend fun getChangelogs(
        page: Int = DEFAULT_PAGE,
        pageSize: Int = DEFAULT_PAGE_SIZE
    ): JsonObject {
        val query = createPaginationQuery(
            page = page,
            pageSize = pageSize
        )
        return execGet(
            endpoint = createChangelogsEndpoint(),
            query = query
        )
    }

    /**
     * Method to execute the request to read a changelog
     *
     * @param changelogId The changelog identifier to read
     *
     * @return the result of the request as [JsonObject]
     *
     */
    @RequestPath(path = "/api/v1/users/{id}/changelogs/{changelog_id}", method = PATCH)
    suspend fun readChangelog(
        changelogId: String
    ): JsonObject {
        return execPatch(
            endpoint = createChangelogsEndpoint(
                id = changelogId
            )
        )
    }

    /**
     * Method to execute the request to delete a changelog
     *
     * @param changelogId The changelog identifier to delete
     * @param groupId The group identifier of the group to decline if is a [com.tecknobit.pandorocore.enums.ChangelogEvent.INVITED_GROUP] event
     *
     * @return the result of the request as [JsonObject]
     *
     */
    @RequestPath(path = "/api/v1/users/{id}/changelogs/{changelog_id}", method = DELETE)
    suspend fun deleteChangelog(
        changelogId: String,
        groupId: String? = null
    ): JsonObject {
        val payload = buildJsonObject {
            groupId?.let {
                put(GROUP_IDENTIFIER_KEY, groupId)
            }
        }
        return execDelete(
            endpoint = createChangelogsEndpoint(
                id = changelogId
            ),
            payload = payload
        )
    }

    /**
     * Method to an endpoint to make the request to the changelogs controller
     *
     * @param subEndpoint The path of the sub-endpoint of the url
     * @param id The eventual identifier to create the path variable
     *
     * @return an endpoint to make the request as [JsonObject]
     */
    @Wrapper
    private fun createChangelogsEndpoint(
        subEndpoint: String? = null,
        id: String? = null
    ): String {
        return createEndpoint(
            baseEndpoint = CHANGELOGS_KEY,
            subEndpoint = subEndpoint,
            id = id
        )
    }

    /**
     * Method to execute the request to get the current overview of the user
     *
     * @return the result of the request as [JsonObject]
     */
    suspend fun getOverview() : JsonObject {
        return execGet(
            endpoint = createEndpoint(
                baseEndpoint = OVERVIEW_ENDPOINT
            )
        )
    }

    /**
     * Method to an endpoint to make the request
     *
     * @param baseEndpoint The base endpoint of the url
     * @param subEndpoint The path of the sub-endpoint of the url
     * @param id The eventual identifier to create the path variable
     *
     * @return an endpoint to make the request as [JsonObject]
     */
    private fun createEndpoint(
        baseEndpoint: String,
        subEndpoint: String? = null,
        id: String? = null
    ): String {
        var endpoint = assembleUsersEndpointPath() + "/$baseEndpoint"
        if (id != null)
            endpoint += "/$id"
        if (subEndpoint != null)
            endpoint += subEndpoint
        return endpoint
    }

}