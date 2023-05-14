package com.example.mobilesocialapp.response

import com.example.mobilesocialapp.utils.Observed
import com.example.mobilesocialapp.utils.User

data class GetObservedResponse(
    val id: Int,
    val user: User,
    val observed: Observed
)
