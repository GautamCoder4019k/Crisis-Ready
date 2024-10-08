package com.example.crisisready.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
data object SignIn

@Serializable
data object Home

@Serializable
data object DoDonts

@Serializable
data class DisasterDetails(val disaster: String)

@Serializable
data object Map

@Serializable
data object Contact

@Serializable
data object Ward

@Serializable
data object SafetyTips

@Serializable
data object TransmitInformation
