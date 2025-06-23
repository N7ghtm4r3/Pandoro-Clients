package com.tecknobit.pandoro.ui.shared.presenters

import androidx.compose.runtime.ExperimentalComposeApi
import com.tecknobit.equinoxcompose.session.sessionflow.SessionFlowState
import com.tecknobit.equinoxcore.annotations.Returner

/**
 * The [SessionFlowStateConsumer] interface provides a way to consume a [SessionFlowState] instance
 * by [PandoroScreen] leveraging the template method pattern
 *
 *
 * @author N7ghtm4r3 - Tecknobit
 *
 * @see SessionFlowState
 *
 * @since 1.1.3
 */
interface SessionFlowStateConsumer {

    /**
     * Method used to retrieve a [SessionFlowState] instance used by the inheritors screens
     *
     * @return the state instance as [SessionFlowState]
     */
    @OptIn(ExperimentalComposeApi::class)
    @Returner
    fun sessionFlowState() : SessionFlowState

}