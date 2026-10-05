package com.example.appcomapanion2

// ╔══════════════════════════════════════════════╗
// ║  TelaHeroiDetalhe.kt                         ║
// ║  TELA 3: recebe o id pela rota, busca o      ║
// ║  herói CERTO no ViewModel, calcula um dado   ║
// ║  (nível de stress) e permite EDITAR o item   ║
// ╚══════════════════════════════════════════════╝

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
fun TelaHeroiDetalhe(navController: NavHostController, viewModel: MeuViewModel, heroiId: String) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalhes do Herói") },
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
        }
    ) { padding ->

        val heroi = viewModel.buscarHeroi(heroiId)

        if (heroi == null) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("Herói não encontrado.")
            }
        } else {
            // ↓ dado CALCULADO a partir do stress do herói, não só reexibido
            val nivelStress = when {
                heroi.stress < 30 -> "Estável"
                heroi.stress < 60 -> "Tenso"
                else -> "Crítico — risco de aflição"
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(heroi.nome, style = MaterialTheme.typography.headlineMedium)
                Text(heroi.classe, style = MaterialTheme.typography.bodyLarge)

                Spacer(modifier = Modifier.height(16.dp))
                Text("HP: ${heroi.hp}", style = MaterialTheme.typography.bodyLarge)
                Text("Stress: ${heroi.stress}  ·  $nivelStress", style = MaterialTheme.typography.bodyLarge)

                Spacer(modifier = Modifier.height(24.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    // editam o herói DE VERDADE dentro do ViewModel
                    Button(onClick = { viewModel.aliviarStress(heroi) }) { Text("Aliviar (-10)") }
                    Button(onClick = { viewModel.estressar(heroi) }) { Text("Estressar (+10)") }
                }

                Spacer(modifier = Modifier.height(24.dp))
                Button(onClick = { navController.popBackStack() }) { Text("← Voltar") }
            }
        }
    }
}

// ── Preview ──────────────────────────────────────
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TelaHeroiDetalhePreview() {
    AppComapanion2Theme {
        val vm: MeuViewModel = viewModel()
        TelaHeroiDetalhe(navController = rememberNavController(), viewModel = vm, heroiId = vm.herois.first().id)
    }
}
