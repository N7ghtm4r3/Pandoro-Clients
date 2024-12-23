package com.tecknobit.pandoro.helpers

import com.tecknobit.apimanager.apis.APIRequest.Params
import com.tecknobit.apimanager.apis.sockets.SocketManager.StandardResponseCode
import com.tecknobit.apimanager.apis.sockets.SocketManager.StandardResponseCode.FAILED
import com.tecknobit.apimanager.apis.sockets.SocketManager.StandardResponseCode.GENERIC_RESPONSE
import com.tecknobit.apimanager.apis.sockets.SocketManager.StandardResponseCode.SUCCESSFUL
import com.tecknobit.equinoxbackend.Requester
import com.tecknobit.equinoxbackend.environment.helpers.EquinoxRequester
import com.tecknobit.equinoxbackend.environment.models.EquinoxUser.IDENTIFIER_KEY
import com.tecknobit.equinoxbackend.environment.models.EquinoxUser.NAME_KEY
import com.tecknobit.equinoxcore.annotations.RequestPath
import com.tecknobit.equinoxcore.annotations.Structure
import com.tecknobit.equinoxcore.annotations.Wrapper
import com.tecknobit.equinoxcore.network.RequestMethod
import com.tecknobit.equinoxcore.network.RequestMethod.*
import com.tecknobit.equinoxcore.pagination.PaginatedResponse
import com.tecknobit.equinoxcore.pagination.PaginatedResponse.Companion.DATA_KEY
import com.tecknobit.equinoxcore.pagination.PaginatedResponse.Companion.DEFAULT_PAGE
import com.tecknobit.equinoxcore.pagination.PaginatedResponse.Companion.DEFAULT_PAGE_SIZE
import com.tecknobit.equinoxcore.pagination.PaginatedResponse.Companion.IS_LAST_PAGE_KEY
import com.tecknobit.equinoxcore.pagination.PaginatedResponse.Companion.PAGE_KEY
import com.tecknobit.equinoxcore.pagination.PaginatedResponse.Companion.PAGE_SIZE_KEY
import com.tecknobit.pandorocore.CHANGELOGS_KEY
import com.tecknobit.pandorocore.CHANGELOG_IDENTIFIER_KEY
import com.tecknobit.pandorocore.CONTENT_NOTE_KEY
import com.tecknobit.pandorocore.FILTERS_KEY
import com.tecknobit.pandorocore.GROUPS_KEY
import com.tecknobit.pandorocore.GROUP_DESCRIPTION_KEY
import com.tecknobit.pandorocore.GROUP_IDENTIFIER_KEY
import com.tecknobit.pandorocore.GROUP_MEMBERS_KEY
import com.tecknobit.pandorocore.MEMBER_ROLE_KEY
import com.tecknobit.pandorocore.NOTES_KEY
import com.tecknobit.pandorocore.ONLY_AUTHORED_GROUPS
import com.tecknobit.pandorocore.PROJECTS_KEY
import com.tecknobit.pandorocore.PROJECT_DESCRIPTION_KEY
import com.tecknobit.pandorocore.PROJECT_REPOSITORY_KEY
import com.tecknobit.pandorocore.PROJECT_VERSION_KEY
import com.tecknobit.pandorocore.UPDATE_CHANGE_NOTES_KEY
import com.tecknobit.pandorocore.UPDATE_TARGET_VERSION_KEY
import com.tecknobit.pandorocore.enums.Role
import com.tecknobit.pandorocore.helpers.PandoroEndpoints.ACCEPT_GROUP_INVITATION_ENDPOINT
import com.tecknobit.pandorocore.helpers.PandoroEndpoints.ADD_CHANGE_NOTE_ENDPOINT
import com.tecknobit.pandorocore.helpers.PandoroEndpoints.ADD_MEMBERS_ENDPOINT
import com.tecknobit.pandorocore.helpers.PandoroEndpoints.CHANGE_MEMBER_ROLE_ENDPOINT
import com.tecknobit.pandorocore.helpers.PandoroEndpoints.DECLINE_GROUP_INVITATION_ENDPOINT
import com.tecknobit.pandorocore.helpers.PandoroEndpoints.EDIT_PROJECTS_ENDPOINT
import com.tecknobit.pandorocore.helpers.PandoroEndpoints.IN_DEVELOPMENT_PROJECTS_ENDPOINT
import com.tecknobit.pandorocore.helpers.PandoroEndpoints.LEAVE_GROUP_ENDPOINT
import com.tecknobit.pandorocore.helpers.PandoroEndpoints.MARK_AS_DONE_ENDPOINT
import com.tecknobit.pandorocore.helpers.PandoroEndpoints.MARK_AS_TO_DO_ENDPOINT
import com.tecknobit.pandorocore.helpers.PandoroEndpoints.MARK_CHANGE_NOTE_AS_DONE_ENDPOINT
import com.tecknobit.pandorocore.helpers.PandoroEndpoints.MARK_CHANGE_NOTE_AS_TODO_ENDPOINT
import com.tecknobit.pandorocore.helpers.PandoroEndpoints.PUBLISH_UPDATE_ENDPOINT
import com.tecknobit.pandorocore.helpers.PandoroEndpoints.REMOVE_MEMBER_ENDPOINT
import com.tecknobit.pandorocore.helpers.PandoroEndpoints.SCHEDULE_UPDATE_ENDPOINT
import com.tecknobit.pandorocore.helpers.PandoroEndpoints.START_UPDATE_ENDPOINT
import com.tecknobit.pandorocore.helpers.PandoroEndpoints.UNREAD_CHANGELOGS_ENDPOINT
import com.tecknobit.pandorocore.helpers.PandoroEndpoints.UPDATES_PATH
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.boolean
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
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
@Structure
open class PandoroRequester(
    host: String,
    userId: String? = null,
    userToken: String? = null,
    debugMode: Boolean = false,
) : EquinoxRequester(
    host = host,
    userId = userId,
    userToken = userToken,
    enableCertificatesValidation = true,
    connectionErrorMessage = "Server is temporarily unavailable",
    debugMode = debugMode
) {

    companion object {

        // TODO: TO REMOVE OR INTEGRATE IN THE EQUINOX LIBRARY
        fun JsonObject.toResponseData() : JsonObject {
            return this[RESPONSE_DATA_KEY]!!.jsonObject
        }

        // TODO: TO REMOVE OR INTEGRATE IN THE EQUINOX LIBRARY
        fun JsonObject.toResponseContent() : String {
            return this[RESPONSE_DATA_KEY]!!.jsonPrimitive.content
        }

        /**
         * Method to execute and manage the response of a request
         *
         * @param request The request to execute
         * @param onResponse The action to execute when a response is returned from the backend
         * @param onConnectionError The action to execute if the request has been failed for a connection error
         */
        @Deprecated(
            message = "TO REMOVE"
        )
        fun <R : Requester> R.sendWRequest(
            request: R.() -> JsonObject,
            onResponse: (JsonObject) -> Unit,
            onConnectionError: ((JsonObject) -> Unit)? = null,
        ) {
            return sendWRequest(
                request = request,
                onSuccess = onResponse,
                onFailure = onResponse,
                onConnectionError = onConnectionError
            )
        }

        /**
         * Method to execute and manage the response of a request
         *
         * @param request The request to execute
         * @param onSuccess The action to execute if the request has been successful
         * @param onFailure The action to execute if the request has been failed
         * @param onConnectionError The action to execute if the request has been failed for a connection error
         */
        @Deprecated(
            message = "TO REMOVE"
        )
        fun <R : Requester> R.sendWRequest(
            request: R.() -> JsonObject,
            onSuccess: (JsonObject) -> Unit,
            onFailure: (JsonObject) -> Unit,
            onConnectionError: ((JsonObject) -> Unit)? = null,
        ) {
            val response = request.invoke(this)
            when (isSuccessfulResponse(response)) {
                SUCCESSFUL -> onSuccess.invoke(response)
                GENERIC_RESPONSE -> {
                    if (onConnectionError != null)
                        onConnectionError.invoke(response)
                    else
                        onFailure.invoke(response)
                }

                else -> onFailure.invoke(response)
            }
        }

        /**
         * Method to execute and manage the paginated response of a request
         *
         * @param request The request to execute
         * @param onSuccess The action to execute if the request has been successful
         * @param onFailure The action to execute if the request has been failed
         * @param onConnectionError The action to execute if the request has been failed for a connection error
         */
        @Deprecated(
            message = "TO REMOVE"
        )
        fun <R : Requester, T> R.sendPaginatedWRequest(
            request: R.() -> JsonObject,
            serializer: KSerializer<T>,
            onSuccess: (PaginatedResponse<T>) -> Unit,
            onFailure: (JsonObject) -> Unit,
            onConnectionError: ((JsonObject) -> Unit)? = null,
        ) {
            sendWRequest(
                request = { request.invoke(this)  },
                onSuccess = { jPage ->
                    // TODO: USING DIRECTLY THE SERIALIZATION
                    val data = jPage[RESPONSE_DATA_KEY]!!.jsonObject
                    val page = PaginatedResponse(
                        data = data[DATA_KEY]!!.jsonArray.map {
                            Json.decodeFromJsonElement(serializer, it)
                        },
                        page = data[PAGE_KEY]!!.jsonPrimitive.int,
                        pageSize = data[PAGE_SIZE_KEY]!!.jsonPrimitive.int,
                        isLastPage = data[IS_LAST_PAGE_KEY]!!.jsonPrimitive.boolean
                    )
                    onSuccess.invoke(page)
                },
                onFailure = onFailure,
                onConnectionError = onConnectionError
            )
        }

        /**
         * Method to get whether the request has been successful or not
         *
         * @param response The response of the request
         *
         * @return whether the request has been successful or not as [StandardResponseCode]
         */
        @Deprecated(
            message = "TO REMOVE"
        )
        private fun isSuccessfulResponse(
            response: JsonObject?,
        ): StandardResponseCode {
            if (response == null || !response.containsKey(RESPONSE_STATUS_KEY))
                return FAILED
            return when (response.jsonObject[RESPONSE_STATUS_KEY]!!.jsonPrimitive.content) {
                SUCCESSFUL.name -> SUCCESSFUL
                GENERIC_RESPONSE.name -> GENERIC_RESPONSE
                else -> FAILED
            }
        }

    }

    /**
     * Function to execute the request to get the projects list of the user
     *
     * @return the result of the request as [JsonObject]
     *
     */
    @RequestPath(
        path = "/api/v1/users/{id}/projects/in_development", method = GET)
    fun getInDevelopmentProjects(
        page: Int = DEFAULT_PAGE,
        pageSize: Int = DEFAULT_PAGE_SIZE,
        filters: String
    ): JsonObject {
        val query = createProjectsQuery(
            page = page,
            pageSize = pageSize,
            filters = filters
        )
        return execWGet(
            endpoint = createProjectEndpoint(
                subEndpoint = IN_DEVELOPMENT_PROJECTS_ENDPOINT
            ),
            query = query
        )
    }

    /**
     * Function to execute the request to get the projects list of the user
     *
     * @return the result of the request as [JsonObject]
     *
     */
    @RequestPath(path = "/api/v1/users/{id}/projects", method = GET)
    fun getProjects(
        page: Int = DEFAULT_PAGE,
        pageSize: Int = DEFAULT_PAGE_SIZE,
        filters: String
    ): JsonObject {
        val query = createProjectsQuery(
            page = page,
            pageSize = pageSize,
            filters = filters
        )
        return execWGet(
            endpoint = createProjectEndpoint(),
            query = query
        )
    }

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
     * Function to execute the request to add a new project of the user
     *
     * @param name The name of the project
     * @param projectDescription The description of the project
     * @param projectVersion The current version of the project
     * @param groups The list of groups where the project can be visible
     * @param projectRepository The url of the repository of the project
     *
     * @return the result of the request as [JsonObject]
     *
     */
    @RequestPath(path = "/api/v1/users/{id}/projects", method = POST)
    fun addProject(
        name: String,
        projectDescription: String,
        projectVersion: String,
        groups: List<String>,
        projectRepository: String = ""
    ): JsonObject {
        return execWPost(
            endpoint = createProjectEndpoint(),
            payload = createProjectPayload(
                name = name,
                projectDescription = projectDescription,
                projectVersion = projectVersion,
                groups = groups,
                projectRepository = projectRepository
            )
        )
    }

    /**
     * Function to execute the request to edit an existing project of the user
     *
     * @param projectId The project identifier
     * @param name The name of the project
     * @param projectDescription The description of the project
     * @param projectVersion The current version of the project
     * @param groups The list of groups where the project can be visible
     * @param projectRepository The url of the repository of the project
     *
     * @return the result of the request as [JsonObject]
     *
     */
    @RequestPath(path = "/api/v1/users/{id}/projects/{project_id}", method = PATCH)
    fun editProject(
        projectId: String,
        name: String,
        projectDescription: String,
        projectVersion: String,
        groups: List<String>,
        projectRepository: String = ""
    ): JsonObject {
        return execWPatch(
            endpoint = createProjectEndpoint(
                id = projectId
            ),
            payload = createProjectPayload(
                name = name,
                projectDescription = projectDescription,
                projectVersion = projectVersion,
                groups = groups,
                projectRepository = projectRepository
            )
        )
    }

    /**
     * Function to create the payload to execute the [addProject] or the [editProject] requests
     *
     * @param name The name of the project
     * @param projectDescription The description of the project
     * @param projectVersion The current version of the project
     * @param groups The list of groups where the project can be visible
     * @param projectRepository The url of the repository of the project
     *
     * @return the payload as [Params]
     *
     */
    private fun createProjectPayload(
        name: String,
        projectDescription: String,
        projectVersion: String,
        groups: List<String>,
        projectRepository: String = ""
    ): JsonObject {
        return buildJsonObject {
            put(NAME_KEY, name)
            put(PROJECT_DESCRIPTION_KEY, projectDescription)
            put(PROJECT_VERSION_KEY, projectVersion)
            put(GROUPS_KEY, Json.encodeToJsonElement(groups))
            put(PROJECT_REPOSITORY_KEY, projectRepository)   
        }
    }

    /**
     * Function to execute the request to get a project of the user
     *
     * @param projectId The project identifier of the project to fetch
     *
     * @return the result of the request as [JsonObject]
     *
     */
    @RequestPath(path = "/api/v1/users/{id}/projects/{project_id}", method = GET)
    fun getProject(
        projectId: String
    ): JsonObject {
        return execWGet(
            endpoint = createProjectEndpoint(
                id = projectId
            )
        )
    }

    /**
     * Function to execute the request to delete a project of the user
     *
     * @param projectId The project identifier of the project to delete
     *
     * @return the result of the request as [JsonObject]
     *
     */
    @RequestPath(path = "/api/v1/users/{id}/projects/{project_id}", method = DELETE)
    fun deleteProject(
        projectId: String
    ): JsonObject {
        return execWDelete(
            endpoint = createProjectEndpoint(
                id = projectId
            )
        )
    }

    /**
     * Function to execute the request to schedule a new update for a project
     *
     * @param projectId The project identifier where schedule the new update
     * @param targetVersion The target version of the update
     * @param updateChangeNotes The change notes of the update
     *
     * @return the result of the request as [JsonObject]
     *
     */
    @RequestPath(path = "/api/v1/users/{id}/projects/{project_id}/updates/schedule", method = POST)
    fun scheduleUpdate(
        projectId: String,
        targetVersion: String,
        updateChangeNotes: List<String>
    ): JsonObject {
        val payload = buildJsonObject {
            put(UPDATE_TARGET_VERSION_KEY, targetVersion)
            put(UPDATE_CHANGE_NOTES_KEY, Json.encodeToJsonElement(updateChangeNotes))
        }
        return execWPost(
            endpoint = createUpdatesEndpoint(
                subEndpoint = SCHEDULE_UPDATE_ENDPOINT,
                projectId = projectId
            ),
            payload = payload
        )
    }

    /**
     * Function to execute the request to start an existing update of a project
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
    fun startUpdate(
        projectId: String,
        updateId: String
    ): JsonObject {
        return execWPatch(
            endpoint = createUpdatesEndpoint(
                subEndpoint = START_UPDATE_ENDPOINT,
                projectId = projectId,
                updateId = updateId
            )
        )
    }

    /**
     * Function to execute the request to publish an existing update of a project
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
    fun publishUpdate(
        projectId: String,
        updateId: String
    ): JsonObject {
        return execWPatch(
            endpoint = createUpdatesEndpoint(
                subEndpoint = PUBLISH_UPDATE_ENDPOINT,
                projectId = projectId,
                updateId = updateId
            )
        )
    }

    /**
     * Function to execute the request to add a new change note to an update
     *
     * @param projectId The project identifier
     * @param updateId The update identifier where add the change note
     * @param changeNote The content of the change note to add
     *
     * @return the result of the request as [JsonObject]
     *
     */
    @RequestPath(
        path = "/api/v1/users/{id}/projects/{project_id}/updates/{update_id}/addChangeNote",
        method = PUT
    )
    fun addChangeNote(
        projectId: String,
        updateId: String,
        changeNote: String
    ): JsonObject {
        val payload = buildJsonObject {
            put(CONTENT_NOTE_KEY, changeNote)
        }
        return execWPut(
            endpoint = createUpdatesEndpoint(
                subEndpoint = ADD_CHANGE_NOTE_ENDPOINT,
                projectId = projectId,
                updateId = updateId
            ),
            payload = payload
        )
    }

    /**
     * Function to execute the request to mark a change note as done
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
    fun markChangeNoteAsDone(
        projectId: String,
        updateId: String,
        changeNoteId: String
    ): JsonObject {
        return execWPatch(
            endpoint = createUpdatesEndpoint(
                subEndpoint = "/${NOTES_KEY}/$changeNoteId${MARK_CHANGE_NOTE_AS_DONE_ENDPOINT}",
                projectId = projectId,
                updateId = updateId
            )
        )
    }

    /**
     * Function to execute the request to mark a change note as todo
     *
     * @param projectId The project identifier
     * @param updateId The update identifier
     * @param changeNoteId The note identifier to mark as todo
     *
     * @return the result of the request as [JsonObject]
     *
     */
    @RequestPath(
        path = "/api/v1/users/{id}/projects/{project_id}/updates/{update_id}/notes/{note_id}/markChangeNoteAsToDo",
        method = PATCH
    )
    fun markChangeNoteAsToDo(
        projectId: String,
        updateId: String,
        changeNoteId: String
    ): JsonObject {
        return execWPatch(
            endpoint = createUpdatesEndpoint(
                subEndpoint = "/${NOTES_KEY}/$changeNoteId${MARK_CHANGE_NOTE_AS_TODO_ENDPOINT}",
                projectId = projectId,
                updateId = updateId
            )
        )
    }

    /**
     * Function to execute the request to delete change note of an update
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
    fun deleteChangeNote(
        projectId: String,
        updateId: String,
        changeNoteId: String
    ): JsonObject {
        return execWDelete(
            endpoint = createUpdatesEndpoint(
                subEndpoint = "/${NOTES_KEY}/$changeNoteId",
                projectId = projectId,
                updateId = updateId
            )
        )
    }

    /**
     * Function to execute the request to delete an update
     *
     * @param projectId The project identifier
     * @param updateId The update identifier to delete
     *
     * @return the result of the request as [JsonObject]
     *
     */
    @RequestPath(path = "/api/v1/users/{id}/projects/{project_id}/updates/{update_id}", method = DELETE)
    fun deleteUpdate(
        projectId: String,
        updateId: String,
    ): JsonObject {
        return execWDelete(
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
     * Function to execute the request to get the groups list of the user where him/her is the author
     *
     * @return the result of the request as [JsonObject]
     *
     */
    @Wrapper
    @RequestPath(path = "/api/v1/users/{id}/groups", method = GET)
    fun getAuthoredGroups(): JsonObject {
        return getGroups(
            onlyAuthoredGroups = true,
            pageSize = Int.MAX_VALUE
        )
    }

    /**
     * Function to execute the request to get the groups list of the user
     *
     * @return the result of the request as [JsonObject]
     *
     */
    @RequestPath(path = "/api/v1/users/{id}/groups", method = GET)
    fun getGroups(
        page: Int = DEFAULT_PAGE,
        pageSize: Int = DEFAULT_PAGE_SIZE,
        onlyAuthoredGroups: Boolean = false,
        nameFilter: String = ""
    ): JsonObject {
        val query = buildJsonObject {
            put(PAGE_KEY, page)
            put(PAGE_SIZE_KEY, pageSize)
            put(ONLY_AUTHORED_GROUPS, onlyAuthoredGroups)
            put(NAME_KEY, nameFilter)
        }
        return execWGet(
            endpoint = createGroupsEndpoint(),
            query = query
        )
    }

    /**
     * Function to execute the request to create a new group for the user
     *
     * @param name The name of the group
     * @param groupDescription The description of the group
     * @param members The list of members of the group
     *
     * @return the result of the request as [JsonObject]
     *
     */
    @RequestPath(path = "/api/v1/users/{id}/groups", method = POST)
    fun createGroup(
        name: String,
        groupDescription: String,
        members: List<String>
    ): JsonObject {
        val payload = buildJsonObject {
            put(NAME_KEY, name)
            put(GROUP_DESCRIPTION_KEY, groupDescription)
            put(GROUP_MEMBERS_KEY, Json.encodeToJsonElement(members))
        }
        return execWPost(
            endpoint = createGroupsEndpoint(),
            payload = payload
        )
    }

    /**
     * Function to execute the request to get a group of the user
     *
     * @param groupId The group identifier of the group to fetch
     *
     * @return the result of the request as [JsonObject]
     *
     */
    @RequestPath(path = "/api/v1/users/{id}/groups/{group_id}", method = GET)
    fun getGroup(groupId: String): JsonObject {
        return execWGet(
            endpoint = createGroupsEndpoint(
                id = groupId
            )
        )
    }

    /**
     * Function to execute the request to add members to a group
     *
     * @param groupId The group identifier where add the members
     * @param members The list of the members to add
     *
     * @return the result of the request as [JsonObject]
     *
     */
    @RequestPath(path = "/api/v1/users/{id}/groups/{group_id}/addMembers", method = PUT)
    fun addMembers(
        groupId: String,
        members: List<String>
    ): JsonObject {
        val payload = buildJsonObject {
            put(GROUP_MEMBERS_KEY, Json.encodeToJsonElement(members))
        }
        return execWPut(
            endpoint = createGroupsEndpoint(
                subEndpoint = ADD_MEMBERS_ENDPOINT,
                id = groupId
            ),
            payload = payload
        )
    }

    /**
     * Function to execute the request to accept a group invitation
     *
     * @param groupId The group identifier of the group to accept the invitation
     * @param changelogId The changelog identifier to delete
     *
     * @return the result of the request as [JsonObject]
     *
     */
    @RequestPath(path = "/api/v1/users/{id}/groups/{group_id}/acceptGroupInvitation", method = PATCH)
    fun acceptInvitation(
        groupId: String,
        changelogId: String
    ): JsonObject {
        val payload = buildJsonObject {
            put(CHANGELOG_IDENTIFIER_KEY, changelogId)
        }
        return execWPatch(
            endpoint = createGroupsEndpoint(
                subEndpoint = ACCEPT_GROUP_INVITATION_ENDPOINT,
                id = groupId
            ),
            payload = payload
        )
    }

    /**
     * Function to execute the request to decline a group invitation
     *
     * @param groupId The group identifier of the group to decline the invitation
     * @param changelogId The changelog identifier to delete
     *
     * @return the result of the request as [JsonObject]
     *
     */
    @RequestPath(path = "/api/v1/users/{id}/groups/{group_id}/declineGroupInvitation", method = DELETE)
    fun declineInvitation(
        groupId: String,
        changelogId: String
    ): JsonObject {
        val payload = buildJsonObject {
            put(CHANGELOG_IDENTIFIER_KEY, changelogId)
        }
        return execWDelete(
            endpoint = createGroupsEndpoint(
                subEndpoint = DECLINE_GROUP_INVITATION_ENDPOINT,
                id = groupId
            ),
            payload = payload
        )
    }

    /**
     * Function to execute the request to change a role of a member of a group
     *
     * @param groupId The group identifier of the group where change the role
     * @param memberId The identifier of the member to change the role
     * @param role The new role of the user
     *
     * @return the result of the request as [JsonObject]
     *
     */
    @RequestPath(path = "/api/v1/users/{id}/groups/{group_id}/changeMemberRole", method = PATCH)
    fun changeMemberRole(
        groupId: String,
        memberId: String,
        role: Role
    ): JsonObject {
        val payload = buildJsonObject {
            put(IDENTIFIER_KEY, memberId)
            put(MEMBER_ROLE_KEY, role.name)
        }
        return execWPatch(
            endpoint = createGroupsEndpoint(
                subEndpoint = CHANGE_MEMBER_ROLE_ENDPOINT,
                id = groupId
            ),
            payload = payload
        )
    }

    /**
     * Function to execute the request to remove a member from a group
     *
     * @param groupId The group identifier of the group where change the role
     * @param memberId The identifier of the member to remove
     *
     * @return the result of the request as [JsonObject]
     *
     */
    @RequestPath(path = "/api/v1/users/{id}/groups/{group_id}/removeMember", method = DELETE)
    fun removeMember(
        groupId: String,
        memberId: String,
    ): JsonObject {
        val payload = buildJsonObject {
            put(IDENTIFIER_KEY, memberId)
        }
        return execWDelete(
            endpoint = createGroupsEndpoint(
                subEndpoint = REMOVE_MEMBER_ENDPOINT,
                id = groupId
            ),
            payload = payload
        )
    }

    /**
     * Function to execute the request to edit a projects list of a group
     *
     * @param groupId The group identifier of the group where edit the projects
     * @param projects The list of the projects for the group
     *
     * @return the result of the request as [JsonObject]
     *
     */
    @RequestPath(path = "/api/v1/users/{id}/groups/{group_id}/editProjects", method = PATCH)
    fun editProjects(
        groupId: String,
        projects: List<String>
    ): JsonObject {
        val payload = buildJsonObject {
            put(PROJECTS_KEY, Json.encodeToJsonElement(projects))
        }
        return execWPatch(
            endpoint = createGroupsEndpoint(
                subEndpoint = EDIT_PROJECTS_ENDPOINT,
                id = groupId
            ),
            payload = payload
        )
    }

    /**
     * Function to execute the request to leave from a group
     *
     * @param groupId The group identifier of the group from leave
     *
     * @return the result of the request as [JsonObject]
     *
     */
    @RequestPath(path = "/api/v1/users/{id}/groups/{group_id}/leaveGroup", method = DELETE)
    fun leaveGroup(
        groupId: String
    ): JsonObject {
        return execWDelete(
            endpoint = createGroupsEndpoint(
                subEndpoint = LEAVE_GROUP_ENDPOINT,
                id = groupId
            )
        )
    }

    /**
     * Function to execute the request to delete a group
     *
     * @param groupId The group identifier of the group to delete
     *
     * @return the result of the request as [JsonObject]
     *
     */
    @RequestPath(path = "/api/v1/users/{id}/groups/{group_id}", method = DELETE)
    fun deleteGroup(groupId: String): JsonObject {
        return execWDelete(
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
     * Function to execute the request to get the notes list of the user
     *
     * @return the result of the request as [JsonObject]
     *
     */
    @RequestPath(path = "/api/v1/users/{id}/notes", method = GET)
    fun getNotesList(): JsonObject {
        return execWGet(
            endpoint = createNotesEndpoint()
        )
    }

    /**
     * Function to execute the request to add a new note of the user
     * @param contentNote The content of the new note to add
     *
     * @return the result of the request as [JsonObject]
     *
     */
    @RequestPath(path = "/api/v1/users/{id}/notes", method = POST)
    fun addNote(
        contentNote: String
    ): JsonObject {
        val payload = buildJsonObject {
            put(CONTENT_NOTE_KEY, contentNote)
        }
        return execWPost(
            endpoint = createNotesEndpoint(),
            payload = payload
        )
    }

    /**
     * Function to execute the request to mark a user's note as done
     * @param noteId The note identifier to mark as done
     *
     * @return the result of the request as [JsonObject]
     *
     */
    @RequestPath(path = "/api/v1/users/{id}/notes/{note_id}/markAsDone", method = PATCH)
    fun markNoteAsDone(
        noteId: String
    ): JsonObject {
        return execWPatch(
            endpoint = createNotesEndpoint(
                subEndpoint = MARK_AS_DONE_ENDPOINT,
                id = noteId
            )
        )
    }

    /**
     * Function to execute the request to mark a user's note as todo
     * @param noteId The note identifier to mark as todo
     *
     * @return the result of the request as [JsonObject]
     *
     */
    @RequestPath(path = "/api/v1/users/{id}/notes/{note_id}/markAsToDo", method = PATCH)
    fun markNoteAsToDo(
        noteId: String
    ): JsonObject {
        return execWPatch(
            endpoint = createNotesEndpoint(
                subEndpoint = MARK_AS_TO_DO_ENDPOINT,
                id = noteId
            )
        )
    }

    /**
     * Function to execute the request to delete a user's note
     * @param noteId The note identifier to delete
     *
     * @return the result of the request as [JsonObject]
     *
     */
    @RequestPath(path = "/api/v1/users/{id}/notes/{note_id}", method = DELETE)
    fun deleteNote(
        noteId: String
    ): JsonObject {
        return execWDelete(
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
     * Function to execute the request to get the changelogs list of the user
     *
     * @return the result of the request as [JsonObject]
     *
     */
    @RequestPath(path = "/api/v1/users/{id}/changelogs/unread", method = GET)
    fun getUnreadChangelogsCount(): JsonObject {
        return execWGet(
            endpoint = assembleCustomEndpointPath(
                customEndpoint = CHANGELOGS_KEY,
                subEndpoint = UNREAD_CHANGELOGS_ENDPOINT
            )
        )
    }

    /**
     * Function to execute the request to get the changelogs list of the user
     *
     * @return the result of the request as [JsonObject]
     *
     */
    @RequestPath(path = "/api/v1/users/{id}/changelogs", method = GET)
    fun getChangelogs(
        page: Int = DEFAULT_PAGE,
        pageSize: Int = DEFAULT_PAGE_SIZE
    ): JsonObject {
        val query = createPaginatedQuery(
            page = page,
            pageSize = pageSize
        )
        return execWGet(
            endpoint = createChangelogsEndpoint(),
            query = query
        )
    }

    /**
     * Function to execute the request to read a changelog
     *
     * @param changelogId The changelog identifier to read
     *
     * @return the result of the request as [JsonObject]
     *
     */
    @RequestPath(path = "/api/v1/users/{id}/changelogs/{changelog_id}", method = PATCH)
    fun readChangelog(
        changelogId: String
    ): JsonObject {
        return execWPatch(
            endpoint = createChangelogsEndpoint(
                id = changelogId
            )
        )
    }

    /**
     * Function to execute the request to delete a changelog
     *
     * @param changelogId The changelog identifier to delete
     * @param groupId The group identifier where leave if is a [ChangelogEvent.INVITED_GROUP]
     *
     * @return the result of the request as [JsonObject]
     *
     */
    @RequestPath(path = "/api/v1/users/{id}/changelogs/{changelog_id}", method = DELETE)
    fun deleteChangelog(
        changelogId: String,
        groupId: String? = null
    ): JsonObject {
        val payload = buildJsonObject {
            groupId?.let {
                put(GROUP_IDENTIFIER_KEY, groupId)
            }
        }
        return execWDelete(
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

    /**
     * Method to execute a [RequestMethod.GET] request to the backend
     *
     * @param endpoint The endpoint path of the request url
     * @param query The query parameters of the request
     *
     * @return the result of the request as [JsonObject]
     */
    @Wrapper
    @Deprecated(
        message = "WILL BE REMOVED AFTER THE PROJECT INTEGRATES iOs and WEB targets"
    )
    protected fun execWGet(
        endpoint: String,
        query: JsonObject? = null
    ) : JsonObject {
        return Json.parseToJsonElement(
            execGet(
                endpoint = endpoint,
                query = query.toParams()
            ).toString()
        ).jsonObject
    }

    /**
     * Method to execute a [RequestMethod.POST] request to the backend
     *
     * @param endpoint The endpoint path of the request url
     * @param query The query parameters of the request
     * @param payload The payload of the request
     *
     * @return the result of the request as [JsonObject]
     */
    @Wrapper
    @Deprecated(
        message = "WILL BE REMOVED AFTER THE PROJECT INTEGRATES iOs and WEB targets"
    )
    protected fun execWPost(
        endpoint: String,
        query: JsonObject? = null,
        payload: JsonObject = JsonObject(emptyMap())
    ) : JsonObject {
        return Json.parseToJsonElement(
            execPost(
                endpoint = endpoint,
                query = query.toParams(),
                payload = payload.toParams()!!
            ).toString()
        ).jsonObject
    }

    /**
     * Method to execute a [RequestMethod.PUT] request to the backend
     *
     * @param endpoint The endpoint path of the request url
     * @param query The query parameters of the request
     * @param payload The payload of the request
     *
     * @return the result of the request as [JsonObject]
     */
    @Wrapper
    @Deprecated(
        message = "WILL BE REMOVED AFTER THE PROJECT INTEGRATES iOs and WEB targets"
    )
    protected fun execWPut(
        endpoint: String,
        query: JsonObject? = null,
        payload: JsonObject = JsonObject(emptyMap())
    ) : JsonObject {
        return Json.parseToJsonElement(
            execPut(
                endpoint = endpoint,
                query = query.toParams(),
                payload = payload.toParams()!!
            ).toString()
        ).jsonObject
    }

    /**
     * Method to execute a [RequestMethod.PATCH] request to the backend
     *
     * @param endpoint The endpoint path of the request url
     * @param query The query parameters of the request
     * @param payload The payload of the request
     *
     * @return the result of the request as [JsonObject]
     */
    @Wrapper
    @Deprecated(
        message = "WILL BE REMOVED AFTER THE PROJECT INTEGRATES iOs and WEB targets"
    )
    protected fun execWPatch(
        endpoint: String,
        query: JsonObject? = null,
        payload: JsonObject = JsonObject(emptyMap())
    ) : JsonObject {
        return Json.parseToJsonElement(
            execPatch(
                endpoint = endpoint,
                query = query.toParams(),
                payload = payload.toParams()!!
            ).toString()
        ).jsonObject
    }

    /**
     * Method to execute a [RequestMethod.DELETE] request to the backend
     *
     * @param endpoint The endpoint path of the request url
     * @param query The query parameters of the request
     * @param payload The payload of the request
     *
     * @return the result of the request as [JsonObject]
     */
    @Wrapper
    @Deprecated(
        message = "WILL BE REMOVED AFTER THE PROJECT INTEGRATES iOs and WEB targets"
    )
    protected fun execWDelete(
        endpoint: String,
        query: JsonObject? = null,
        payload: JsonObject = JsonObject(emptyMap())
    ) : JsonObject {
        return Json.parseToJsonElement(
            execDelete(
                endpoint = endpoint,
                query = query.toParams(),
                payload = payload.toParams()!!
            ).toString()
        ).jsonObject
    }
    
    @Deprecated(
        message = "TO REMOVE"
    )
    private fun JsonObject?.toParams() : Params? {
        if(this == null)
            return null
        val params = Params()
        this.entries.forEach { entry ->
            params.addParam(entry.key, entry.value.toString().replace("\"", ""))
        }
        return params
    }

    @Deprecated(
        message = "TO USE THE BUILT-IN ONE"
    )
    private fun createPaginatedQuery(
        page: Int,
        pageSize: Int
    ) : JsonObject {
        return buildJsonObject {
            put(PAGE_KEY, page)
            put(PAGE_SIZE_KEY, pageSize)
        }
    }

}