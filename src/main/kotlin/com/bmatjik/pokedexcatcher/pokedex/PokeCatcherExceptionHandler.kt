package com.bmatjik.pokedexcatcher.pokedex

import com.bmatjik.pokedexcatcher.pokedex.bean.BaseResponse
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class PokeCatcherExceptionHandler {

    @ExceptionHandler(Exception::class)
    fun handleException(exception: Exception): ResponseEntity<BaseResponse<String>> {
        return ResponseEntity<BaseResponse<String>>(
            BaseResponse(status = false, errorMessage = exception.message.toString(), result = ""),
            HttpStatus.BAD_REQUEST,
        )
    }
}