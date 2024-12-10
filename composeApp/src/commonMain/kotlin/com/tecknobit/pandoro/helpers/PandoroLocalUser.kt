package com.tecknobit.pandoro.helpers

import com.tecknobit.equinoxbackend.environment.models.EquinoxLocalUser
import com.tecknobit.kmprefs.KMPrefs

class PandoroLocalUser : EquinoxLocalUser() {

    private val kmPrefs = KMPrefs(
        path = "Pandoro"
    )

    init {
        initLocalUser()
    }

    /**
     * Method to store and set a preference
     *
     * @param key:   the key of the preference
     * @param value: the value of the preference
     */
    override fun setPreference(
        key: String,
        value: String?
    ) {
        kmPrefs.storeString(
            key = key,
            value = value
        )
    }

    /**
     * Method to get a stored preference
     *
     * @param key: the key of the preference to get
     * @return the preference stored as [String]
     */
    override fun getPreference(
        key: String
    ): String? {
        return kmPrefs.retrieveString(
            key = key
        )
    }

    /**
     * Method to clear the current local user session <br></br>
     * No-any params required
     */
    override fun clear() {
        kmPrefs.clearAll()
        initLocalUser()
    }

}