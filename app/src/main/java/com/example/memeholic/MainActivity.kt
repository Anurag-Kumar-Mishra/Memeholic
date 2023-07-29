package com.example.memeholic

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import java.time.temporal.TemporalAdjusters.next

class MainActivity : AppCompatActivity() {

    var currentmemeurl : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val next: Button = findViewById(R.id.next)
        val share: Button = findViewById(R.id.share)
        val meme: ImageView = findViewById(R.id.meme)
        val progressBar: ProgressBar = findViewById(R.id.progressBar)
        loadmeme(meme,progressBar)

        next.setOnClickListener {
            Toast.makeText(this, "Loading", Toast.LENGTH_LONG).show()
            loadmeme(meme,progressBar)
        }

        share.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type="text/plain"
            intent.putExtra(Intent.EXTRA_TEXT,"View this funny meme on Reddit : $currentmemeurl")
            val chooser = Intent.createChooser(intent,"Share this meme via : ")
            startActivity(chooser)
        }
    }

    private fun loadmeme(imageView: ImageView,progressBar: ProgressBar) {
        val url = "https://meme-api.com/gimme"
        progressBar.visibility = View.VISIBLE


        // Request a string response from the provided URL.
        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
            { response ->
                currentmemeurl = response.getString("url")
                Glide.with(this).load(currentmemeurl).listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar.visibility = View.GONE
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar.visibility = View.GONE
                        return false
                        
                    }
                }).into(imageView)
            },
            { error ->
                Toast.makeText(this, "An Error Occurred", Toast.LENGTH_LONG).show()
            }
        )

        // Add the request to the RequestQueue.
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }

}