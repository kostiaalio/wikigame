package ua.boberproduction.wikigame.mvvm.pregame

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import kotlinx.android.synthetic.main.fragment_pregame.*
import ua.boberproduction.wikigame.BaseFragment
import ua.boberproduction.wikigame.R
import ua.boberproduction.wikigame.databinding.FragmentPregameBinding

class PregameFragment : BaseFragment() {
    lateinit var binding: FragmentPregameBinding
    lateinit var viewModel: PregameViewModel

    companion object {
        const val START_PHRASE = "start_phrase"
        const val TARGET_PHRASE = "target_phrase"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_pregame, container, false)
        binding.setLifecycleOwner(this)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = getViewModel()
        binding.viewModel = viewModel

        phrase_start.animationDuration = 1200
        phrase_start.animationDelay = 100

        phrase_target.animationDuration = 1200
        phrase_target.animationDelay = 100

        phrase_target.addAnimatorListener(object : Animator.AnimatorListener {

            override fun onAnimationRepeat(p0: Animator?) {

            }

            override fun onAnimationEnd(p0: Animator?) {
                showButtons()
            }

            override fun onAnimationStart(p0: Animator?) {

            }

            override fun onAnimationCancel(p0: Animator?) {

            }

        })

        viewModel.onCreate()

        viewModel.errorMessage.observe(this, Observer {
            if (!it.isNullOrEmpty()) showError(it)
        })

        viewModel.phrases.observe(this, Observer { pair ->
            if (pair != null) {
                phrase_start.text = pair.first
                phrase_target.text = pair.second
            }
        })

        viewModel.startGame.observe(this, Observer {
            val bundle = bundleOf(
                    START_PHRASE to viewModel.phrases.value?.first,
                    TARGET_PHRASE to viewModel.phrases.value?.second)
            NavHostFragment.findNavController(this).navigate(R.id.action_pregameFragment_to_gameFragment, bundle)
        })
    }

    private fun showButtons() {
        val scaleXstart = ObjectAnimator.ofFloat(btn_start, "scaleX", 0f, 1f).setDuration(400)
        val scaleYstart = ObjectAnimator.ofFloat(btn_start, "scaleY", 0f, 1f).setDuration(400)
        scaleXstart.interpolator = OvershootInterpolator()
        scaleYstart.interpolator = OvershootInterpolator()

        val scaleXquestion = ObjectAnimator.ofFloat(target_info_btn, "scaleX", 0f, 1f).setDuration(400)
        val scaleYquestion = ObjectAnimator.ofFloat(target_info_btn, "scaleY", 0f, 1f).setDuration(400)
        scaleXquestion.interpolator = OvershootInterpolator()
        scaleYquestion.interpolator = OvershootInterpolator()

        val set = AnimatorSet()
        set.playTogether(scaleXstart, scaleYstart, scaleXquestion, scaleYquestion)
        set.start()
    }
}