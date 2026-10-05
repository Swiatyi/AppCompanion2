package com.example.appcomapanion2

// ╔══════════════════════════════════════════════╗
// ║  TelaSuprimentos.kt                          ║
// ║  TELA 5: checklist de suprimentos com        ║
// ║  Checkbox + ações rápidas + confirmar        ║
// ╚══════════════════════════════════════════════╝

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.appcomapanion2.ui.theme.AppComapanion2Theme
import com.example.appcomapanion2.ui.theme.DungeonGold
import com.example.appcomapanion2.ui.theme.DungeonSurfaceAlt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaSuprimentos(navController: NavHostController, viewModel: MeuViewModel) {
    val context = LocalContext.current
    val totalSelecionados = viewModel.itensSuprimento.count { it.selecionado }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Suprimentos") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DungeonSurfaceAlt,
                    titleContentColor = DungeonGold,
                    navigationIconContentColor = DungeonGold
                )
            )
        },
        bottomBar = { BottomNavBar(navController = navController) }
    ) { padding ->

        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)) {

            Row {
                TextButton(onClick = { viewModel.marcarTodosSuprimentos() }) { Text("Marcar recomendados") }
                TextButton(onClick = { viewModel.limparTodosSuprimentos() }) { Text("Limpar tudo") }
            }

            Text(
                "Selecionados: $totalSelecionados de ${viewModel.itensSuprimento.size}",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.weight(1f)
            ) {
                itemsIndexed(viewModel.itensSuprimento) { index, item ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = item.selecionado,
                            onCheckedChange = { viewModel.toggleSuprimento(index) }
                        )
                        Column {
                            Text(item.nome, style = MaterialTheme.typography.bodyLarge)
                            Text(item.categoria, style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }

            Button(
                onClick = {
                    Toast.makeText(context, "Suprimentos prontos: $totalSelecionados itens para a expedição!", Toast.LENGTH_LONG).show()
                },
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
            ) { Text("Confirmar Suprimentos") }
        }
    }
}

// ── Preview ──────────────────────────────────────
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TelaSuprimentosPreview() {
    AppComapanion2Theme {
        TelaSuprimentos(navController = rememberNavController(), viewModel = viewModel())
    }
}
