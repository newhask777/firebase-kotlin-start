package com.example.bookstoreapp

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.bookstoreapp.data.Book
import com.example.bookstoreapp.ui.theme.BookStoreAppTheme
import com.google.api.Context
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage
import java.io.ByteArrayOutputStream

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen(name = "test", modifier = Modifier)
        }


        enableEdgeToEdge()
    }

}

@Composable
fun MainScreen(name: String, modifier: Modifier = Modifier) {

    val fs = Firebase.firestore
    val storage = Firebase.storage.reference.child("images")
    val list = remember {
        mutableStateOf(emptyList<Book>())
    }

//    Without autoload
//    fs.collection("books").get().addOnCompleteListener {
//        task -> if (task.isSuccessful){
//            list.value = task.result.toObjects(Book::class.java)
//        }
//    }


    fs.collection("books").addSnapshotListener {
        snapShot, exception -> list.value = snapShot?.toObjects(Book::class.java) ?: emptyList()
    }
//    listener.remove()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ){
        LazyColumn(modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
            .fillMaxHeight(0.8f))
        {
            items(list.value) { book ->
                Card(modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)) {
                    Text(text = book.name, modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth().padding(15.dp))
                }
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        Button(modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
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
                text = "Add Book", modifier = Modifier.fillMaxWidth().wrapContentWidth()
            )
        }
    }
}


private fun bitmapToByteArray(context: Resources): ByteArray
{
    val bitmap = BitmapFactory.decodeResource(context, R.drawable.gob)
    val baos = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
    return baos.toByteArray()
}
