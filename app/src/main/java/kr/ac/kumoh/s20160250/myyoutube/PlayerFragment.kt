package kr.ac.kumoh.s20160250.myyoutube

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.recyclerview.widget.LinearLayoutManager
import kr.ac.kumoh.s20160250.myyoutube.adapter.VideoAdapter
import kr.ac.kumoh.s20160250.myyoutube.databinding.FragmentPlayerBinding
import kr.ac.kumoh.s20160250.myyoutube.dto.VideoDto
import kr.ac.kumoh.s20160250.myyoutube.service.VideoService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.math.abs


class PlayerFragment : Fragment(R.layout.fragment_player) {

    private var binding: FragmentPlayerBinding? = null

    private lateinit var videoAdapter: VideoAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragmentPlayerBinding = FragmentPlayerBinding.bind(view)
        binding = fragmentPlayerBinding

        initMotionLayoutEvent(fragmentPlayerBinding)
        initRecyclerView(fragmentPlayerBinding)
        getVideoList()
    }

    private fun initMotionLayoutEvent(fragmentPlayerBinding: FragmentPlayerBinding) {
        // binding 을 사용하지않은 이유 ? nullable 이라
        fragmentPlayerBinding.playerMotionLayout.setTransitionListener(object :
            MotionLayout.TransitionListener {
            override fun equals(other: Any?): Boolean {
                return super.equals(other)
            }

            override fun onTransitionStarted(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int
            ) {
            }

            override fun onTransitionChange(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int,
                progress: Float
            ) {
                binding?.let {
                    (activity as MainActivity).also { mainActivity ->
                        mainActivity.findViewById<MotionLayout>(R.id.mainMotionLayout).progress =
                            abs(progress)
                    }
                }
            }

            override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {

            }

            override fun onTransitionTrigger(
                motionLayout: MotionLayout?,
                triggerId: Int,
                positive: Boolean,
                progress: Float
            ) {

            }
        }
        )
    }

    private fun initRecyclerView(fragmentPlayerBinding: FragmentPlayerBinding) {
        videoAdapter = VideoAdapter(callback = { url, title ->
            play(url, title)
        })
        fragmentPlayerBinding.fragmentRecyclerView.apply {
            adapter = videoAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    fun play(url: String, title: String) {
        binding?.let {
            it.playerMotionLayout.transitionToEnd()
            it.bottomTitleTextView.text = title
        }
    }

    private fun getVideoList() {
        val retrofit = Retrofit.Builder().baseUrl("https://run.mocky.io/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(VideoService::class.java).also {
            it.listVideos()
                .enqueue(object : Callback<VideoDto> {
                    override fun onResponse(call: Call<VideoDto>, response: Response<VideoDto>) {
                        if (response.isSuccessful.not()) {
                            Log.d("MainActivity", "response fail")
                            return
                        }
                        response.body()?.let { videoDto ->
                            Log.d("MainActivity", it.toString())
                            videoAdapter.submitList(videoDto.videos)
                        }
                    }

                    override fun onFailure(call: Call<VideoDto>, t: Throwable) {
                        //예외처리
                    }
                })
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}