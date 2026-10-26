package com.campus.app

import androidx.compose.ui.tooling.preview.Preview
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Ink = Color(0xFF25265E)
private val Purple = Color(0xFF6750A4)
private val Lilac = Color(0xFFEDE9FF)
private val Page = Color(0xFFF7F7FB)
private val Muted = Color(0xFF77788C)
private val Green = Color(0xFF16845B)
private val Orange = Color(0xFFE9A23B)
private val Red = Color(0xFFB65353)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme(
                colorScheme = androidx.compose.material3.lightColorScheme(
                    primary = Purple,
                    secondary = Green,
                    background = Page,
                    surface = Color.White,
                    onPrimary = Color.White,
                    onBackground = Ink,
                    onSurface = Ink
                )
            ) {
                CampusNoteApp()
            }
        }
    }
}

private data class Note(
    val title: String,
    val subtitle: String,
    val date: String,
    val category: String,
    val color: Color,
    val icon: String,
    val description: String
)

private val sampleNotes = listOf(
    Note("Pemrograman Mobile", "Jetpack Compose & UI", "14 Okt 2026", "Kuliah", Color(0xFFE7EEFF), "📘",
        "Ringkasan materi tentang komponen Compose, state sederhana, dan penyusunan layout responsif."),
    Note("Basis Data", "Normalisasi dan ERD", "12 Okt 2026", "Kuliah", Color(0xFFDDF4E9), "🗃️",
        "Catatan contoh tentang entitas, relasi, primary key, dan bentuk normalisasi data."),
    Note("Struktur Data", "Array dan List", "10 Okt 2026", "Praktikum", Color(0xFFFFEED8), "📊",
        "Materi latihan mengenai array, list, pencarian data, dan kompleksitas dasar."),
    Note("Pemrograman Web", "Dasar HTML & CSS", "8 Okt 2026", "Tugas", Color(0xFFFFE4E2), "🧑‍💻",
        "Daftar poin belajar HTML semantik, layout CSS, dan prinsip desain responsif.")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CampusNoteApp() {
    var selectedTab by remember { mutableStateOf(0) }
    var selectedCategory by remember { mutableStateOf("Semua") }
    var searchOpen by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf("") }
    var selectedNote by remember { mutableStateOf<Note?>(null) }
    var showQuickMenu by remember { mutableStateOf(false) }
    var showActionDialog by remember { mutableStateOf<String?>(null) }

    val categories = listOf("Semua", "Kuliah", "Praktikum", "Tugas")
    val filteredNotes = sampleNotes.filter { note ->
        (selectedCategory == "Semua" || note.category == selectedCategory) &&
                (note.title.contains(searchText, ignoreCase = true) ||
                        note.subtitle.contains(searchText, ignoreCase = true))
    }

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        // Window width is measured in dp so the layout adapts to phones and tablets.
        val isExpanded = maxWidth >= 600.dp

        Scaffold(
            containerColor = Page,
            topBar = {
                TopAppBar(
                    title = {
                        Column {
                            Text("CampusNote", fontWeight = FontWeight.ExtraBold, fontSize = 21.sp, color = Ink)
                            Text("Catatan Mahasiswa", fontSize = 12.sp, color = Muted)
                        }
                    },
                    actions = {
                        IconButton(onClick = { searchOpen = !searchOpen }) {
                            Icon(if (searchOpen) Icons.Default.Close else Icons.Default.Search, "Cari catatan", tint = Ink)
                        }
                        IconButton(onClick = { showActionDialog = "Notifikasi" }) {
                            Text("🔔", fontSize = 18.sp)
                        }
                        Spacer(Modifier.width(6.dp))
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Page)
                )
            },
            bottomBar = {
                if (!isExpanded) {
                    NavigationBar(containerColor = Color.White) {
                        NavigationBarItem(
                            selected = selectedTab == 0,
                            onClick = { selectedTab = 0 },
                            icon = { Icon(Icons.Default.Home, contentDescription = "Beranda") },
                            label = { Text("Beranda") }
                        )
                        NavigationBarItem(
                            selected = selectedTab == 1,
                            onClick = { selectedTab = 1 },
                            icon = { Icon(Icons.Default.CalendarMonth, contentDescription = "Jadwal") },
                            label = { Text("Jadwal") }
                        )
                        NavigationBarItem(
                            selected = selectedTab == 2,
                            onClick = { selectedTab = 2 },
                            icon = { Icon(Icons.Default.Person, contentDescription = "Profil") },
                            label = { Text("Profil") }
                        )
                    }
                }
            },
            floatingActionButton = {
                Box {
                    val rotation by animateFloatAsState(if (showQuickMenu) 45f else 0f, label = "fabRotation")
                    FloatingActionButton(
                        onClick = { showQuickMenu = !showQuickMenu },
                        containerColor = Purple,
                        contentColor = Color.White,
                        shape = CircleShape
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Menu cepat", modifier = Modifier.rotate(rotation))
                    }
                    androidx.compose.material3.DropdownMenu(
                        expanded = showQuickMenu,
                        onDismissRequest = { showQuickMenu = false }
                    ) {
                        listOf("Catatan", "Tugas", "Jadwal").forEach { action ->
                            androidx.compose.material3.DropdownMenuItem(
                                text = { Text("＋  $action") },
                                onClick = {
                                    showQuickMenu = false
                                    showActionDialog = action
                                }
                            )
                        }
                    }
                }
            }
        ) { padding ->
            if (isExpanded) {
                Row(
                    modifier = Modifier.fillMaxSize().padding(padding)
                ) {
                    NavigationRail(containerColor = Color.White) {
                        NavigationRailItem(
                            selected = selectedTab == 0,
                            onClick = { selectedTab = 0 },
                            icon = { Icon(Icons.Default.Home, contentDescription = "Beranda") },
                            label = { Text("Beranda") }
                        )
                        NavigationRailItem(
                            selected = selectedTab == 1,
                            onClick = { selectedTab = 1 },
                            icon = { Icon(Icons.Default.CalendarMonth, contentDescription = "Jadwal") },
                            label = { Text("Jadwal") }
                        )
                        NavigationRailItem(
                            selected = selectedTab == 2,
                            onClick = { selectedTab = 2 },
                            icon = { Icon(Icons.Default.Person, contentDescription = "Profil") },
                            label = { Text("Profil") }
                        )
                    }
                    Box(Modifier.weight(1f).fillMaxHeight()) {
                        when (selectedTab) {
                            0 -> HomeContent(
                                modifier = Modifier.fillMaxSize(),
                                searchOpen = searchOpen,
                                searchText = searchText,
                                onSearchChange = { searchText = it },
                                categories = categories,
                                selectedCategory = selectedCategory,
                                onCategorySelect = { selectedCategory = it },
                                notes = filteredNotes,
                                onNoteClick = { selectedNote = it }
                            )
                            1 -> SimpleSection(
                                modifier = Modifier.fillMaxSize(), emoji = "🗓️",
                                title = "Jadwal Minggu Ini",
                                description = "Senin · Pemrograman Mobile\nRabu · Basis Data\nJumat · Struktur Data",
                                chip = "3 agenda contoh"
                            )
                            else -> SimpleSection(
                                modifier = Modifier.fillMaxSize(), emoji = "🎓",
                                title = "Profil Mahasiswa",
                                description = "Andi Pratama\nProgram Studi Informatika\nSemester 4",
                                chip = "Tampilan profil"
                            )
                        }
                    }
                }
            } else {
                when (selectedTab) {
                    0 -> HomeContent(
                        modifier = Modifier.padding(padding),
                        searchOpen = searchOpen,
                        searchText = searchText,
                        onSearchChange = { searchText = it },
                        categories = categories,
                        selectedCategory = selectedCategory,
                        onCategorySelect = { selectedCategory = it },
                        notes = filteredNotes,
                        onNoteClick = { selectedNote = it }
                    )
                    1 -> SimpleSection(
                        modifier = Modifier.padding(padding), emoji = "🗓️",
                        title = "Jadwal Minggu Ini",
                        description = "Senin · Pemrograman Mobile\nRabu · Basis Data\nJumat · Struktur Data",
                        chip = "3 agenda contoh"
                    )
                    else -> SimpleSection(
                        modifier = Modifier.padding(padding), emoji = "🎓",
                        title = "Profil Mahasiswa",
                        description = "Andi Pratama\nProgram Studi Informatika\nSemester 4",
                        chip = "Tampilan profil"
                    )
                }
            }
        }
    }

    selectedNote?.let { note ->
        AlertDialog(
            onDismissRequest = { selectedNote = null },
            title = { Text(note.title, fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("${note.category}  •  ${note.date}", color = Muted, fontSize = 12.sp)
                    Text(note.description)
                    Surface(color = note.color, shape = RoundedCornerShape(12.dp)) {
                        Text("Contoh tampilan detail catatan", Modifier.padding(12.dp), color = Ink)
                    }
                }
            },
            confirmButton = { TextButton(onClick = { selectedNote = null }) { Text("Tutup") } }
        )
    }

    showActionDialog?.let { action ->
        AlertDialog(
            onDismissRequest = { showActionDialog = null },
            title = { Text("$action (Demo UI)") },
            text = { Text("Ini hanya simulasi interaksi untuk presentasi. Tidak ada data yang disimpan.") },
            confirmButton = { TextButton(onClick = { showActionDialog = null }) { Text("Mengerti") } }
        )
    }
}

@Composable
private fun HomeContent(
    modifier: Modifier = Modifier,
    searchOpen: Boolean,
    searchText: String,
    onSearchChange: (String) -> Unit,
    categories: List<String>,
    selectedCategory: String,
    onCategorySelect: (String) -> Unit,
    notes: List<Note>,
    onNoteClick: (Note) -> Unit
) {
    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val horizontalInset = if (maxWidth >= 900.dp) ((maxWidth - 900.dp) / 2) + 24.dp else 18.dp
        LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = horizontalInset, end = horizontalInset, top = 8.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        if (searchOpen) {
            item {
                OutlinedTextField(
                    value = searchText,
                    onValueChange = onSearchChange,
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    placeholder = { Text("Cari mata kuliah atau catatan...") },
                    leadingIcon = { Icon(Icons.Default.Search, null) },
                    shape = RoundedCornerShape(16.dp)
                )
            }
        }

        item { WelcomeCard() }

        item {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.weight(1f)) {
                    Text("Catatan Terbaru", fontSize = 19.sp, fontWeight = FontWeight.Bold, color = Ink)
                    Text("Materi belajarmu dalam satu tempat", fontSize = 12.sp, color = Muted)
                }
                Text("Lihat semua  →", color = Purple, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                categories.forEach { category ->
                    val selected = selectedCategory == category
                    Surface(
                        color = if (selected) Purple else Color.White,
                        contentColor = if (selected) Color.White else Muted,
                        shape = RoundedCornerShape(50),
                        modifier = Modifier.clickable { onCategorySelect(category) }
                    ) {
                        Text(
                            category,
                            modifier = Modifier.padding(horizontal = 17.dp, vertical = 10.dp),
                            fontSize = 13.sp,
                            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal
                        )
                    }
                }
            }
        }

        if (notes.isEmpty()) {
            item {
                Surface(color = Color.White, shape = RoundedCornerShape(18.dp)) {
                    Column(
                        Modifier.fillMaxWidth().padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("🔎", fontSize = 30.sp)
                        Text("Catatan tidak ditemukan", fontWeight = FontWeight.Bold)
                        Text("Coba kata kunci atau kategori lain.", color = Muted, fontSize = 13.sp)
                    }
                }
            }
        } else {
            items(notes) { note ->
                NoteCard(note = note, onClick = { onNoteClick(note) })
            }
        }

        item { StudyTipCard() }
        }
    }
}

@Composable
private fun WelcomeCard() {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF81C784)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .background(Brush.linearGradient(listOf(Color(0xFF81C784), Color(0xFF388E3C))))
                .padding(20.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text("Halo, Andi! 👋", color = Color(0xFFFFCC80), fontSize = 21.sp, fontWeight = FontWeight.ExtraBold)
                    Spacer(Modifier.height(7.dp))
                    Text(
                        "Siap melanjutkan\nprogres belajarmu hari ini?",
                        color = Color(0xFFE8E5FF),
                        fontSize = 13.sp,
                        lineHeight = 19.sp
                    )
                    Spacer(Modifier.height(14.dp))
                    Surface(color = Color(0x33FFFFFF), shape = RoundedCornerShape(50)) {
                        Text("✨  Tetap produktif", Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
                            color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
                Box(
                    Modifier.size(76.dp).background(Color(0x33FFFFFF), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text("🧑🏻‍🎓", fontSize = 43.sp)
                }
            }
        }
    }
}

@Composable
private fun NoteCard(note: Note, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().animateContentSize(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 13.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(48.dp).background(note.color, RoundedCornerShape(14.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(note.icon, fontSize = 23.sp)
            }
            Spacer(Modifier.width(13.dp))
            Column(Modifier.weight(1f)) {
                Text(note.title, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Ink, maxLines = 1,
                    overflow = TextOverflow.Ellipsis)
                Spacer(Modifier.height(4.dp))
                Text(note.subtitle, color = Muted, fontSize = 12.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Spacer(Modifier.height(7.dp))
                Text(note.date, color = Purple, fontSize = 10.sp, fontWeight = FontWeight.Medium)
            }
            Icon(Icons.Default.ArrowForward, contentDescription = "Buka detail", tint = Muted,
                modifier = Modifier.size(18.dp))
        }
    }
}

@Composable
private fun StudyTipCard() {
    Surface(
        color = Color(0xFFE8F5EE),
        shape = RoundedCornerShape(18.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.Top) {
            Text("💡", fontSize = 24.sp)
            Spacer(Modifier.width(12.dp))
            Column {
                Text("Tips belajar", color = Color(0xFF176B4B), fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Spacer(Modifier.height(4.dp))
                Text("Catat poin penting setelah kelas agar lebih mudah diingat saat mengulang materi.",
                    color = Color(0xFF3C6C58), fontSize = 12.sp, lineHeight = 17.sp)
            }
        }
    }
}

@Composable
private fun SimpleSection(
    modifier: Modifier = Modifier,
    emoji: String,
    title: String,
    description: String,
    chip: String
) {
    Column(
        modifier = modifier.fillMaxSize().padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(title, fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, color = Ink)
        Surface(color = Color.White, shape = RoundedCornerShape(24.dp), shadowElevation = 2.dp) {
            Column(
                Modifier.fillMaxWidth().padding(22.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(emoji, fontSize = 42.sp)
                Spacer(Modifier.height(12.dp))
                Text(description, fontSize = 16.sp, lineHeight = 25.sp, color = Ink)
                Spacer(Modifier.height(18.dp))
                Surface(color = Lilac, shape = RoundedCornerShape(50)) {
                    Text(chip, Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                        color = Purple, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }
        Text("Konten contoh untuk demonstrasi UI interaktif.", color = Muted, fontSize = 13.sp)
    }
}

@Preview(showBackground = true)
@Composable
private fun WelcomeCardPreview() {
    MaterialTheme {
        WelcomeCard()
    }
}