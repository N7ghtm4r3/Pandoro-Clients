package com.tecknobit.pandoro.ui.screens.createproject.presentation

import androidx.compose.runtime.MutableState
import com.tecknobit.pandoro.navigator
import com.tecknobit.pandoro.ui.sharedviewmodels.BaseProjectViewModel
import com.tecknobit.pandorocore.helpers.PandoroInputsValidator.isValidProjectDescription
import com.tecknobit.pandorocore.helpers.PandoroInputsValidator.isValidProjectName
import com.tecknobit.pandorocore.helpers.PandoroInputsValidator.isValidRepository
import com.tecknobit.pandorocore.helpers.PandoroInputsValidator.isValidVersion

class CreateProjectScreenViewModel(
    val projectId: String?
) : BaseProjectViewModel() {

    lateinit var projectIcon: MutableState<String?>

    lateinit var projectName: MutableState<String>

    lateinit var projectNameError: MutableState<Boolean>

    lateinit var projectVersion: MutableState<String>

    lateinit var projectVersionError: MutableState<Boolean>

    lateinit var projectRepository: MutableState<String>

    lateinit var projectRepositoryError: MutableState<Boolean>

    lateinit var projectDescription: MutableState<String>

    lateinit var projectDescriptionError: MutableState<Boolean>

    override fun retrieveProject() {
        if(projectId == null)
            return
        // TODO: MAKE THE REQUEST
    }

    fun workOnProject() {
        // TODO: MAKE THE REQUEST TO EDIT OR ADD THEN
        if(!isFormValid())
            return
        navigator.goBack()
    }

    private fun isFormValid() : Boolean {
        if(!isValidProjectName(projectName.value)) {
            projectNameError.value = true
            return false
        }
        if(!isValidVersion(projectVersion.value)) {
            projectVersionError.value = true
            return false
        }
        if(!isValidRepository(projectRepository.value)) {
            projectRepositoryError.value = true
            return false
        }
        if(!isValidProjectDescription(projectDescription.value)) {
            projectDescriptionError.value = true
            return false
        }
        return true
    }

}