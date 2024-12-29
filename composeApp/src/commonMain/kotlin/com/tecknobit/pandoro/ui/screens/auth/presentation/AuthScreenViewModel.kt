package com.tecknobit.pandoro.ui.screens.auth.presentation

import androidx.compose.material3.SnackbarHostState
import com.tecknobit.apimanager.formatters.JsonHelper
import com.tecknobit.equinoxbackend.Requester.Companion.sendRequest
import com.tecknobit.equinoxbackend.environment.models.EquinoxUser.LANGUAGE_KEY
import com.tecknobit.equinoxbackend.environment.models.EquinoxUser.NAME_KEY
import com.tecknobit.equinoxbackend.environment.models.EquinoxUser.SURNAME_KEY
import com.tecknobit.equinoxcompose.helpers.viewmodels.EquinoxAuthViewModel
import com.tecknobit.equinoxcompose.helpers.viewmodels.EquinoxViewModel
import com.tecknobit.equinoxcore.annotations.RequiresSuperCall
import com.tecknobit.equinoxcore.helpers.InputsValidator.Companion.isEmailValid
import com.tecknobit.equinoxcore.helpers.InputsValidator.Companion.isNameValid
import com.tecknobit.equinoxcore.helpers.InputsValidator.Companion.isPasswordValid
import com.tecknobit.equinoxcore.helpers.InputsValidator.Companion.isServerSecretValid
import com.tecknobit.equinoxcore.helpers.InputsValidator.Companion.isSurnameValid
import com.tecknobit.pandoro.HOME_SCREEN
import com.tecknobit.pandoro.localUser
import com.tecknobit.pandoro.navigator
import com.tecknobit.pandoro.requester
import com.tecknobit.pandorocore.helpers.PandoroInputsValidator.isHostAddressValid

/**
 * The **AuthScreenViewModel** class is the support class used to execute the authentication requests
 * to the backend
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see androidx.lifecycle.ViewModel
 * @see com.tecknobit.equinoxbackend.FetcherManager
 * @see EquinoxViewModel
 * @see EquinoxAuthViewModel
 */
class AuthScreenViewModel: EquinoxAuthViewModel(
    snackbarHostState = SnackbarHostState(),
    requester = requester,
    localUser = localUser
) {

    @Deprecated(
        message = "USE THE BUILT-IN ONE"
    )
    fun workAroundAuth() {
        if (isSignUp.value) {
            if(signUpFormIsValid())
                signUp()
        } else {
            if(signInFormIsValid())
                signIn()
        }
    }

    @Deprecated(
        message = "USE THE BUILT-IN ONE"
    )
    private fun signUp() {
        if (signUpFormIsValid()) {
            val language = getUserLanguage()
            requester.changeHost(host.value)
            requester.sendRequest(
                request = {
                    signUp(
                        serverSecret = serverSecret.value,
                        name = name.value,
                        surname = surname.value,
                        email = email.value,
                        password = password.value,
                        language = language,
                        custom = getSignUpCustomParameters()
                    )
                },
                onSuccess = { response ->
                    launchApp(
                        name = name.value,
                        surname = surname.value,
                        language = language,
                        response = response,
                        custom = getSignUpCustomParameters()
                    )
                },
                onFailure = { showSnackbarMessage(it) }
            )
        }
    }

    @Deprecated(
        message = "USE THE BUILT-IN ONE"
    )
    override fun signUpFormIsValid(): Boolean {
        var isValid: Boolean = isHostAddressValid(host.value)
        if (!isValid) {
            hostError.value = true
            return false
        }
        isValid = isServerSecretValid(serverSecret.value)
        if (!isValid) {
            serverSecretError.value = true
            return false
        }
        isValid = isNameValid(name.value)
        if (!isValid) {
            nameError.value = true
            return false
        }
        isValid = isSurnameValid(surname.value)
        if (!isValid) {
            surnameError.value = true
            return false
        }
        isValid = isEmailValid(email.value)
        if (!isValid) {
            emailError.value = true
            return false
        }
        isValid = isPasswordValid(password.value)
        if (!isValid) {
            passwordError.value = true
            return false
        }
        return true
    }

    @Deprecated(
        message = "USE THE BUILT-IN ONE"
    )
    private fun signIn() {
        if (signInFormIsValid()) {
            requester.changeHost(host.value)
            requester.sendRequest(
                request = {
                    requester.signIn(
                        email = email.value,
                        password = password.value,
                        custom = getSignInCustomParameters()
                    )
                },
                onSuccess = { response ->
                    launchApp(
                        name = response.getString(NAME_KEY),
                        surname = response.getString(SURNAME_KEY),
                        language = response.getString(LANGUAGE_KEY),
                        response = response,
                        custom = getSignInCustomParameters()
                    )
                },
                onFailure = { showSnackbarMessage(it) }
            )
        }
    }

    @Deprecated(
        message = "USE THE BUILT-IN ONE"
    )
    override fun signInFormIsValid(): Boolean {
        var isValid: Boolean = isHostAddressValid(host.value)
        if (!isValid) {
            hostError.value = true
            return false
        }
        isValid = isEmailValid(email.value)
        if (!isValid) {
            emailError.value = true
            return false
        }
        isValid = isPasswordValid(password.value)
        if (!isValid) {
            passwordError.value = true
            return false
        }
        return true
    }

    /**
     * Method to launch the application after the authentication request, will be instantiated with the user details
     * both the [requester] and the [localUser]
     *
     * @param response The response of the authentication request
     * @param name The name of the user
     * @param surname The surname of the user
     * @param language The language of the user
     * @param custom The custom parameters added in a customization of the [PandoroUser]
     */
    @RequiresSuperCall
    override fun launchApp(
        response: JsonHelper,
        name: String,
        surname: String,
        language: String,
        vararg custom: Any?
    ) {
        super.launchApp(response, name, surname, language, *custom)
        navigator.navigate(HOME_SCREEN)
    }
    
}