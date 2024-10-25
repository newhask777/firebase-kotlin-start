package com.example.bookstoreapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.bookstoreapp.data.Book
import com.example.bookstoreapp.ui.theme.BookStoreAppTheme
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen(name = "test", modifier = Modifier)
        }


//        enableEdgeToEdge()
//        setContent {
//            BookStoreAppTheme {
//                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                    Greeting(
//                        name = "Android",
//                        modifier = Modifier.padding(innerPadding)
//                    )
//                }
//            }
//        }
    }

//    private fun MainScreen() {
//        TODO("Not yet implemented")
//    }
}

@Composable
fun MainScreen(name: String, modifier: Modifier = Modifier) {

    val fs = Firebase.firestore

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ){
        LazyColumn(modifier = Modifier.fillMaxWidth().fillMaxHeight(0.8f))
        {

        }
        Spacer(modifier = Modifier.height(10.dp))
        Button(modifier = Modifier.fillMaxWidth().padding(10.dp),
            onClick = {
            fs.collection("books")
                .document().set(Book(
                    "Mu book",
                    "Description",
                    "23",
                    "fiction",
                    "url"
                ))
        })
        {
            Text(
                text = "Add Book"
            )
        }
    }
}
