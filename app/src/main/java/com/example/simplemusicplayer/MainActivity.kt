package com.example.simplemusicplayer

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.database.Cursor
import android.nfc.Tag
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.simplemusicplayer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    companion object{
        const val PERMISSION_REQUEST_CODE = 1001
    }

    class Track(val title:String,val album:String,val artist:String )

    class AudioAdapter(private val dataSet:List<Track>): RecyclerView.Adapter<AudioAdapter.ViewHolder>(){
        class ViewHolder(view: View):RecyclerView.ViewHolder( view ){
            val textTitle: TextView
            val textAlbum : TextView
            val textArtist : TextView
            init{
                textTitle = view.findViewById( R.id.textTitle )
                textAlbum = view.findViewById( R.id.textAlbum )
                textArtist = view.findViewById( R.id.textArtist )
            }
        }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.music_item,parent,false )
            return ViewHolder( view )
            //TODO("Not yet implemented")
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            //TODO("Not yet implemented")
            Log.d("onBind",dataSet[position].title )
            holder.textTitle.text = dataSet[position].title
            holder.textArtist.text = dataSet[position].artist
            holder.textAlbum.text = dataSet[position].album
        }

        override fun getItemCount(): Int = dataSet.size

    }

    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate( layoutInflater )

        setContentView( binding.root )

        if( Build.VERSION.SDK_INT >= 23 ) {
            checkPermission( arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), PERMISSION_REQUEST_CODE)
        }

        val proj = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.ARTIST
        )
        val cursor = contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            proj,
            null,
            null,
            null
        )

        var tracks = mutableListOf<Track>()
        //cursor?.moveToFirst()
        Log.d("Text","Text")
        while(cursor?.moveToNext() == true){
            //Log.d("cursor",cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)))
            tracks.add( Track(
                cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)),
                cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM)),
                cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST))
                ))
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = AudioAdapter( tracks )

    }

    private fun checkPermission( permission:Array<String>, request_code : Int){
        ActivityCompat.requestPermissions(this,permission,request_code)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

    }

}












