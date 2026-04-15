package com.example.project_2417051009

import Model.Music
import Model.MusicList
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.example.project_2417051009.ui.theme.Project_2417051009Theme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            Project_2417051009Theme {
                // SnackbarHostState untuk menampilkan Snackbar
                val snackbarHostState = remember { SnackbarHostState() }
                
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        CenterAlignedTopAppBar(
                            title = { Text("Relaxing Sounds", fontWeight = FontWeight.Bold) }
                        )
                    },
                    // Menambahkan SnackbarHost ke dalam Scaffold
                    snackbarHost = { SnackbarHost(snackbarHostState) }
                ) { innerPadding ->
                    MusicListScreen(
                        modifier = Modifier.padding(innerPadding),
                        snackbarHostState = snackbarHostState
                    )
                }
            }
        }
    }
}

@Composable
fun MusicListScreen(modifier: Modifier = Modifier, snackbarHostState: SnackbarHostState) {
    var favoriteIds by remember { mutableStateOf(setOf<Int>()) }
    val scope = rememberCoroutineScope()

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        // Bagian Rekomendasi Populer
        item {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Rekomendasi Populer",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(horizontal = 4.dp)
                ) {
                    items(MusicList.items.take(3)) { item ->
                        PopularMusicItem(music = item)
                    }
                }
            }
        }

        item {
            Text(
                text = "Daftar Menu Lengkap",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 8.dp)
            )
        }

        items(MusicList.items) { item ->
            Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                MusicItem(
                    music = item,
                    isFavorite = favoriteIds.contains(item.id),
                    onOrderClick = {
                        // Implementasi Coroutine saat tombol diklik
                        scope.launch {
                            // Menampilkan snackbar setelah proses delay (disimulasikan di dalam MusicItem)
                            snackbarHostState.showSnackbar("Pesanan ${item.title} berhasil diproses!")
                        }
                    },
                    onFavoriteClick = {
                        favoriteIds = if (favoriteIds.contains(item.id)) {
                            favoriteIds - item.id
                        } else {
                            favoriteIds + item.id
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun PopularMusicItem(music: Music) {
    Card(
        modifier = Modifier.width(160.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            Image(
                painter = painterResource(id = music.imageRes),
                contentDescription = music.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = music.title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
                Text(
                    text = "Rp 10000",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
fun MusicItem(
    music: Music,
    isFavorite: Boolean,
    onOrderClick: () -> Unit,
    onFavoriteClick: () -> Unit
) {
    // State Loading untuk simulasi proses Asynchronous
    var isLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                Image(
                    painter = painterResource(id = music.imageRes),
                    contentDescription = music.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                
                IconButton(
                    onClick = onFavoriteClick,
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = if (isFavorite) Color.Red else Color.White
                    )
                }
            }
            
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = music.title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    text = "Deskripsi Produk",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
                Text(
                    text = "Harga: Rp 10000",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 4.dp)
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Tombol Pesan Sekarang dengan Kondisi Loading
                Button(
                    onClick = {
                        scope.launch {
                            isLoading = true
                            delay(2000) // Simulasi proses loading selama 2 detik
                            isLoading = false
                            onOrderClick() // Panggil feedback snackbar
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    enabled = !isLoading, // Button disable saat loading
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isLoading) Color.LightGray else MaterialTheme.colorScheme.primary
                    )
                ) {
                    if (isLoading) {
                        // Menampilkan CircularProgressIndicator saat proses berlangsung
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Memproses...", color = Color.White)
                    } else {
                        Text("Pesan Sekarang", color = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Tombol Kembali (Opsional sesuai gambar)
                Button(
                    onClick = { /* Aksi Kembali */ },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text("Kembali", color = Color.White)
                }
            }
        }
    }
}
