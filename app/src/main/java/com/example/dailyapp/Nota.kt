package com.example.dailyapp
import com.google.firebase.Timestamp

class Nota{
    var title: String? = null
        set(value) {
            field = value
        }

    var content: String? = null
        set(value) {
            field = value
        }

    var timestamp: Timestamp? = null
        set(value) {
            field = value
        }
}
