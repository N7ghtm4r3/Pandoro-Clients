package com.tecknobit.pandoro.ui.screens.profile

import androidx.compose.material3.SnackbarHostState
import com.tecknobit.equinoxbackend.environment.helpers.EquinoxRequester
import com.tecknobit.equinoxbackend.environment.models.EquinoxLocalUser
import com.tecknobit.equinoxcompose.helpers.viewmodels.EquinoxProfileViewModel

class ProfileScreenViewModel: EquinoxProfileViewModel(
    snackbarHostState = SnackbarHostState(),
    // TODO: USE THE REAL ONE
    requester = object : EquinoxRequester(
        host = "g",
        connectionErrorMessage = "gaga"
    ) {

    },
    // TODO: USE THE REAL ONE
    localUser = object : EquinoxLocalUser() {
        /**
         * Method to store and set a preference
         *
         * @param key:   the key of the preference
         * @param value: the value of the preference
         */
        override fun setPreference(key: String?, value: String?) {
        }

        /**
         * Method to get a stored preference
         *
         * @param key: the key of the preference to get
         * @return the preference stored as [String]
         */
        override fun getPreference(key: String?): String {
            return ""
        }

        /**
         * Method to clear the current local user session <br></br>
         * No-any params required
         */
        override fun clear() {
        }

    }
) {
}