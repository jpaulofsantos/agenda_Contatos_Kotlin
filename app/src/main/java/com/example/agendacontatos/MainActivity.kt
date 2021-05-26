package com.example.agendacontatos

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.provider.ContactsContract
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    val REQUEST_CONTACT = 1
    val LINEAR_LAYOUT_VERTICAL = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.READ_CONTACTS),
                REQUEST_CONTACT
            )
        } else {
            setContacts()
        }
    }

    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CONTACT) setContacts()
    }

    private fun setContacts() {
        val listaContatos: ArrayList<Contact> = ArrayList()

        val cursor = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null, null, null, null
        )

        if (cursor != null) {
            while (cursor.moveToNext()) {
                listaContatos.add(
                    Contact(
                        cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)),
                        cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                    )
                )
            }

            cursor.close()
        }

        val adapter = ContactAdapter(listaContatos)

        val contactRecycleView = findViewById<RecyclerView>(R.id.contacts_recyclerView)
        contactRecycleView.layoutManager = LinearLayoutManager(this,  LINEAR_LAYOUT_VERTICAL,
            false)
        contactRecycleView.adapter = adapter
    }
}

