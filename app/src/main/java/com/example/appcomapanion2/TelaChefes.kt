package com.example.appcomapanion2

// ╔══════════════════════════════════════════════╗
// ║  TelaChefes.kt                               ║
// ║  TELA 6: segunda lista com CRUD real         ║
// ║  (igual Heróis, mesmo padrão do ViewModel)   ║
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
fun TelaChefes(navController: NavHostController, viewModel: MeuViewModel) {
    var nomeNovo by remember { mutableStateOf("") }
    var descricaoNova by remember { mutableStateOf("") }
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Chefes") },
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

            // -------- formulário de ADICIONAR novo chefe --------
            OutlinedTextField(
                value = nomeNovo, onValueChange = { nomeNovo = it },
                label = { Text("Nome do chefe") }, modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = descricaoNova, onValueChange = { descricaoNova = it },
                label = { Text("Breve descrição") }, modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                    if (nomeNovo.isNotBlank()) {
                        viewModel.adicionarChefe(nomeNovo, descricaoNova)
                        Toast.makeText(context, "$nomeNovo adicionado aos Chefes!", Toast.LENGTH_SHORT).show()
                        nomeNovo = ""; descricaoNova = ""
                    } else {
                        Toast.makeText(context, "Preencha ao menos o nome", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) { Text("Adicionar Chefe") }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(viewModel.chefes, key = { it.id }) { chefe ->
                    CardChefe(
                        chefe = chefe,
                        onClickCard = { navController.navigate(Rotas.chefeDetalhe(chefe.id)) },
                        onRemover = {
                            viewModel.removerChefe(chefe)
                            Toast.makeText(context, "${chefe.nome} removido", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }
        }
    }
}

// Clique abre Detalhe; ícone remove de verdade
@Composable
fun CardChefe(chefe: Chefe, onClickCard: () -> Unit, onRemover: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().clickable { onClickCard() }) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(chefe.nome, style = MaterialTheme.typography.titleMedium)
                Text(chefe.descricao, style = MaterialTheme.typography.bodySmall, maxLines = 1)
            }
            IconButton(onClick = onRemover) {
                Icon(Icons.Default.Delete, contentDescription = "Remover chefe")
            }
        }
    }
}

// ── Preview ──────────────────────────────────────
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TelaChefesPreview() {
    AppComapanion2Theme {
        TelaChefes(navController = rememberNavController(), viewModel = viewModel())
    }
}
