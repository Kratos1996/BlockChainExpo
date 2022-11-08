package com.ishant.blockchainexpo.domain.usecase

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ishant.blockchainexpo.application.constance.ErrorMessage.INTERNET_CONNECTION_ERROR
import com.ishant.blockchainexpo.application.constance.ErrorMessage.UNEXPECTED
import com.ishant.blockchainexpo.data.model.error.ErrorDto
import com.ishant.blockchainexpo.data.model.spot.CoinDto
import com.ishant.blockchainexpo.data.repository.CoinRepository
import com.ishant.blockchainexpo.network.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException

class CoinUseCaseImpl(private val  repository: CoinRepository,private val gson:Gson):CoinUseCase {
    override fun invoke(): Flow<Resource<CoinDto>> = flow {
        try {
            emit(Resource.Loading<CoinDto>())
            val response=repository.getAllCoin()
            emit(Resource.Success<CoinDto>(response))

        } catch (e: HttpException) {
            try {
                val errorObj = JSONObject(e.response()!!.errorBody()?.charStream()?.readText()!!)
                if(e.code()==404){
                    val type = object : TypeToken<ErrorDto>() {}.type
                    var errorResponse: ErrorDto? = gson.fromJson(errorObj.toString(), type)
                    emit(Resource.Error<CoinDto>(errorResponse!!.message))
                }
                else{
                    emit(Resource.Error<CoinDto>(errorObj.toString()))
                }
            }catch (e:Exception){
                emit(Resource.Error<CoinDto>(UNEXPECTED))
            }

        } catch (e: IOException) {
            emit(Resource.Error<CoinDto>(INTERNET_CONNECTION_ERROR))
        }
    }
}