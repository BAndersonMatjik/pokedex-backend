package com.bmatjik.pokedexcatcher.pokedex

import co.touchlab.stately.collections.ConcurrentMutableMap
import co.touchlab.stately.collections.ConcurrentMutableSet
import com.bmatjik.pokedexcatcher.pokedex.bean.BaseResponse
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.util.concurrent.ConcurrentMap
import kotlin.math.sqrt
import kotlin.random.Random
import kotlin.random.nextInt

@RestController
class PokeCatcherController {
    private val cache = ConcurrentMutableSet<String>()

    @GetMapping("/cache")
    fun getCacheData(): BaseResponse<List<String>> {
        return cache.map {
            "key ${it}"
        }.let {
            BaseResponse(status = true, result = it)
        }
    }


    @GetMapping("/catch")
    fun catchPokemon(): BaseResponse<Boolean> {
        val probabilty = Random.nextInt(0..100)
        val result = probabilty % 2 == 1
        return BaseResponse(status = true, result = result)
    }

    @GetMapping("/release")
    fun releasePokemon(): BaseResponse<Int> {
        val probabilty = Random.nextInt(0..100)
        val result = probabilty % 2 == 1
        return if (!result) {
            BaseResponse(status = true, result = probabilty)
        } else {
            BaseResponse(status = false, result = probabilty, errorMessage = "Failed to Release not Prime Number")
        }
    }

    @PostMapping("/rename")
    fun renamePokemon(@RequestBody request: Map<String, String>): BaseResponse<String> {
        if (!request.containsKey("name")) {
            return BaseResponse(errorMessage = "name has not defined in Request body", status = false, result = "")
        }
        val pokeName = request.get("name")?.split("-")?.first()
        var index = request.get("name")?.split("-")?.last()?.toInt()
            ?: return BaseResponse(
                errorMessage = "index has not defined name property in Request body",
                status = false,
                result = ""
            )
        //last fibbonacci cache index 1
        //cache save in memory be
        // 0 1 1 2 3
        //bug at one request first time will be return 2 not 1 solution need cache with has conccurency handling.
        //NO RACE CONDITION.
        //1. check if key has been here before M -1
        //2. if never call here M-1 will be cache
        var isCache = false
        if (!cache.contains(request.get("name")) && index == 1) {
            request.get("name")?.apply {
                cache.add(this)
            }
            isCache = true
        }
        val result = pokeName + "-" + nextFibbonacci(index ?: 0, isCache)
        return BaseResponse(status = true, result = result)
    }


    private fun nextFibbonacci(n: Int, isCache: Boolean): Long {
        if (isCache) {
            return n.toLong();
        }
        val first = n * ((1 + sqrt(5.0)) / 2.0)
        return Math.round(first)
    }
}