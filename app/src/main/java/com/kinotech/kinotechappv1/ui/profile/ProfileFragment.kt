package com.kinotech.kinotechappv1.ui.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.kinotech.kinotechappv1.AuthActivity
import com.kinotech.kinotechappv1.R

class ProfileFragment : Fragment() {

    private lateinit var photoAcc: ImageView
    private lateinit var nickName: TextView
    private lateinit var signOut: Button
    private lateinit var mSignInClient: GoogleSignInClient
    private lateinit var profileViewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        profileViewModel =
            ViewModelProvider(this).get(ProfileViewModel::class.java)
        /*val textView = root.findViewById<TextView>(R.id.text_profile)
        profileViewModel.text.observe(
            viewLifecycleOwner,
            Observer {
                textView.text = it
            }
        )*/
        val root = inflater.inflate(R.layout.fragment_profile, container, false)
        signOut = root.findViewById(R.id.image_exit)
        nickName = root.findViewById(R.id.text_profile)
        photoAcc = root.findViewById(R.id.photo)

        var button = root.findViewById<Button>(R.id.change_profile_button)
        button.setOnClickListener{
            loadfragment()
        }
        return root
    }
    private fun loadfragment(){
        val transaction = activity?.getSupportFragmentManager()?.beginTransaction()
        if (transaction != null) {
            transaction.replace(R.id.container, ChangeProfileFragment())
            transaction.disallowAddToBackStack()
            transaction.commit()
        }
    }

    override fun onResume() {
        super.onResume()
        val buffAcc = GoogleSignIn.getLastSignedInAccount(context)
        bind(buffAcc)
        val gso: GoogleSignInOptions =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()
        mSignInClient = context?.let { GoogleSignIn.getClient(it, gso) }!!
        signOut.setOnClickListener {
            mSignInClient.signOut()
            val intent = Intent(context, AuthActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }
    }

    private fun bind(acc: GoogleSignInAccount?) {
        if (acc == null) {
            Log.d("check", "null")
        } else {
            nickName.text = acc.displayName
            Glide
                .with(this)
                .load(acc.photoUrl)
                .error(R.drawable.ic_like_40dp)
                .into(photoAcc)
        }
    }
}

