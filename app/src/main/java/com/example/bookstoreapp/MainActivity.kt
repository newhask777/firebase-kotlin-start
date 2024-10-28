package com.example.bookstoreapp

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.Uri
import coil3.compose.AsyncImage
import com.example.bookstoreapp.data.Book
import com.example.bookstoreapp.ui.theme.BookStoreAppTheme
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage
import java.io.ByteArrayOutputStream

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val fs = Firebase.firestore
            val storage = Firebase.storage.reference.child("images")
        //  Get media file
            val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.PickVisualMedia()) {
                uri ->
                if (uri == null) return@rememberLauncherForActivityResult
                val task = storage.child("test.jpg").putBytes(
                    bitmapToByteArray(this, uri)
                )
                task.addOnSuccessListener {
                        uploadTask -> uploadTask.metadata?.reference?.downloadUrl?.addOnCompleteListener{
                        uriTask -> saveBook(fs, uriTask.result.toString())
                }

                }
            }
//            MainScreen(name = "test", modifier = Modifier)
            MainScreen {
                launcher.launch(PickVisualMediaRequest(mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly))
            }
        }


        enableEdgeToEdge()
    }

}

@Composable
//fun MainScreen(name: String, modifier: Modifier = Modifier, onClick: () -> Unit)
fun MainScreen(onClick: () -> Unit)
{

    val context = LocalContext.current
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
                    Row(modifier = Modifier.fillMaxWidth().padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically) {
                        AsyncImage(model = book.imageUrl, contentDescription = null,
                            modifier = Modifier.height(100.dp).width(100.dp))
                        Text(text = book.name, modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth().padding(15.dp))
                    }

                }
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        Button(modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
            onClick = {
                    onClick()
//                val task = storage.child("cat.jpg").putBytes(
//                    bitmapToByteArray(context)
//                )
//                task.addOnSuccessListener {
//                    uploadTask -> uploadTask.metadata?.reference?.downloadUrl?.addOnCompleteListener{
//                        uriTask -> saveBook(fs, uriTask.result.toString())
//                    }
//
//                }
            }
        )
        {
            Text(
                text = "Add Book", modifier = Modifier.fillMaxWidth().wrapContentWidth()
            )
        }
    }
}


private fun bitmapToByteArray(context: Context, uri: android.net.Uri): ByteArray
{
    val inputStream = context.contentResolver.openInputStream(uri)
    val bitmap = BitmapFactory.decodeStream(inputStream)

//    val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.cat)
    val baos = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos)
    return baos.toByteArray()
}


private fun saveBook(fs: FirebaseFirestore, url: String)
{
    fs.collection("books")
        .document().set(
            Book(
                "My Book",
                "Bla Bla",
                "100",
                "fiction",
                url
            )
        )
}


