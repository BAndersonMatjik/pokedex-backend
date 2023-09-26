package com.bmatjik.pokedexcatcher.pokedex.bean

data class BaseResponse<T>(
    val errorMessage: String?=null,
    val status: Boolean,
    val result: T
)
