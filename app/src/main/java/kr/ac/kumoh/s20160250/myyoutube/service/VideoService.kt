package kr.ac.kumoh.s20160250.myyoutube.service

import kr.ac.kumoh.s20160250.myyoutube.dto.VideoDto
import retrofit2.Call
import retrofit2.http.GET

interface VideoService {

    @GET("https://run.mocky.io/v3/8b14b078-eccd-4333-86cb-60d8c2f69f84")
    fun listVideos(): Call<VideoDto>

}