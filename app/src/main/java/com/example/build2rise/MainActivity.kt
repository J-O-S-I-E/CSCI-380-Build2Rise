package com.example.build2rise

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.build2rise.ui.InvestorProfilePosts

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            InvestorProfilePosts()   // ✅ launch profile directly
        }
    }
}





