package com.tecknobit.pandoro.ui.screens.auth.presentation

import androidx.compose.material3.SnackbarHostState
import com.tecknobit.equinoxcompose.session.viewmodels.EquinoxAuthViewModel
import com.tecknobit.equinoxcore.annotations.RequiresSuperCall
import com.tecknobit.pandoro.HOME_SCREEN
import com.tecknobit.pandoro.localUser
import com.tecknobit.pandoro.navigator
import com.tecknobit.pandoro.requester
import kotlinx.serialization.json.JsonObject

/**
 * The **AuthScreenViewModel** class is the support class used to execute the authentication requests
 * to the backend
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see androidx.lifecycle.ViewModel
 * @see com.tecknobit.equinoxcompose.session.Retriever.RetrieverWrapper
 * @see EquinoxViewModel
 * @see EquinoxAuthViewModel
 */
class AuthScreenViewModel: EquinoxAuthViewModel(
    snackbarHostState = SnackbarHostState(),
    requester = requester,
    localUser = localUser
) {

    /**
     * Method to launch the application after the authentication request, will be instantiated with the user details
     * both the [requester] and the [localUser]
     *
     * @param response The response of the authentication request
     * @param name The name of the user
     * @param surname The surname of the user
     * @param language The language of the user
     * @param custom The custom parameters added in a customization of the [EquinoxUser]
     */
    @RequiresSuperCall
    override fun launchApp(
        response: JsonObject,
        name: String,
        surname: String,
        language: String,
        vararg custom: Any?
    ) {
        super.launchApp(response, name, surname, language, *custom)
        navigator.navigate(HOME_SCREEN)
    }
    
}