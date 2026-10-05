package com.example.appcomapanion2

// ╔══════════════════════════════════════════════╗
// ║  TelaHerois.kt                               ║
// ║  TELA 2: lista com CRUD real (ViewModel)     ║
// ║  Clique no card abre o Detalhe daquele herói ║
// ╚══════════════════════════════════════════════╝

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
fun TelaHerois(navController: NavHostController, viewModel: MeuViewModel) {
    var nomeNovo by remember { mutableStateOf("") }
    var classeNova by remember { mutableStateOf("") }
    // herói que o jogador está tentando escalar em algum chefe agora (abre o diálogo)
    var heroiEscolhendoChefe by remember { mutableStateOf<Heroi?>(null) }
    val context = LocalContext.current

    // -------- diálogo: "em qual chefe esse herói entra?" --------
    val heroiAtual = heroiEscolhendoChefe
    if (heroiAtual != null) {
        AlertDialog(
            onDismissRequest = { heroiEscolhendoChefe = null },
            title = { Text("Escalar ${heroiAtual.nome} em qual chefe?") },
            text = {
                Column {
                    viewModel.chefes.forEach { chefe ->
                        val ocupadas = chefe.partyIds.count { it != null }
                        val cheia = ocupadas >= 4
                        TextButton(
                            enabled = !cheia,
                            onClick = {
                                viewModel.adicionarHeroiAoChefe(chefe.id, heroiAtual.id)
                                Toast.makeText(
                                    context,
                                    "${heroiAtual.nome} escalado em ${chefe.nome}!",
                                    Toast.LENGTH_SHORT
                                ).show()
                                heroiEscolhendoChefe = null
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("${chefe.nome}  ($ocupadas/4)${if (cheia) " — cheia" else ""}")
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { heroiEscolhendoChefe = null }) { Text("Cancelar") }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Roster de Heróis") },
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

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Text(
                "Toque no + de um herói pra escalar ele na party de um chefe",
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.height(12.dp))

            // -------- formulário de ADICIONAR novo herói --------
            OutlinedTextField(
                value = nomeNovo, onValueChange = { nomeNovo = it },
                label = { Text("Nome do herói") }, modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = classeNova, onValueChange = { classeNova = it },
                label = { Text("Classe") }, modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                    if (nomeNovo.isNotBlank() && classeNova.isNotBlank()) {
                        viewModel.adicionarHeroi(nomeNovo, classeNova)
                        Toast.makeText(context, "$nomeNovo adicionado ao roster!", Toast.LENGTH_SHORT).show()
                        nomeNovo = ""; classeNova = ""
                    } else {
                        Toast.makeText(context, "Preencha nome e classe", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) { Text("Adicionar Herói") }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(viewModel.herois, key = { it.id }) { heroi ->
                    CardHeroi(
                        heroi = heroi,
                        onClickCard = { navController.navigate(Rotas.heroiDetalhe(heroi.id)) },
                        onEscalarEmChefe = { heroiEscolhendoChefe = heroi },
                        onRemover = {
                            viewModel.removerHeroi(heroi)
                            Toast.makeText(context, "${heroi.nome} removido", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }
        }
    }
}

// Clique no card abre Detalhe; ícone remove de verdade; "+" abre o diálogo
// de escalar esse herói na party de algum chefe
@Composable
fun CardHeroi(heroi: Heroi, onClickCard: () -> Unit, onEscalarEmChefe: () -> Unit, onRemover: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().clickable { onClickCard() }) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(heroi.nome, style = MaterialTheme.typography.titleMedium)
                Text(
                    "${heroi.classe} · HP ${heroi.hp} · Stress ${heroi.stress}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            IconButton(onClick = onRemover) {
                Icon(Icons.Default.Delete, contentDescription = "Remover herói")
            }
            Button(onClick = onEscalarEmChefe) { Text("+") }
        }
    }
}

// ── Preview ──────────────────────────────────────
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TelaHeroisPreview() {
    AppComapanion2Theme {
        TelaHerois(navController = rememberNavController(), viewModel = viewModel())
    }
}
