package com.example.appcomapanion2

// ╔══════════════════════════════════════════════╗
// ║  TelaChefeDetalhe.kt                         ║
// ║  TELA 7: nível mutável RECALCULA HP/Dano,    ║
// ║  e 4 posições de party ESCOLHÍVEIS, cada uma ║
// ║  puxando um Heroi da lista de Heróis —       ║
// ║  combina as duas listas de forma interativa  ║
// ╚══════════════════════════════════════════════╝

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
fun TelaChefeDetalhe(navController: NavHostController, viewModel: MeuViewModel, chefeId: String) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalhes do Chefe") },
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

        val chefe = viewModel.buscarChefe(chefeId)

        if (chefe == null) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("Chefe não encontrado.")
            }
        } else {
            // ↓ valores CALCULADOS a partir do nível (campo do próprio item)
            val hpCalculado = chefe.hpBase * chefe.level
            val danoCalculado = chefe.danoBase * chefe.level

            Column(
                modifier = Modifier.fillMaxSize().padding(padding).padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(chefe.nome, style = MaterialTheme.typography.headlineMedium)
                Text(chefe.descricao, style = MaterialTheme.typography.bodyLarge)
                Text("Região: ${chefe.regiao}", style = MaterialTheme.typography.bodySmall)

                Spacer(modifier = Modifier.height(16.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { viewModel.mudarNivelChefe(chefe, -1) }) {
                        Icon(Icons.Default.Remove, contentDescription = "Diminuir nível")
                    }
                    Text("Nível ${chefe.level}", style = MaterialTheme.typography.titleMedium)
                    IconButton(onClick = { viewModel.mudarNivelChefe(chefe, +1) }) {
                        Icon(Icons.Default.Add, contentDescription = "Aumentar nível")
                    }
                }

                Text("HP: $hpCalculado", style = MaterialTheme.typography.bodyLarge)
                Text("Dano: $danoCalculado", style = MaterialTheme.typography.bodyLarge)

                Spacer(modifier = Modifier.height(24.dp))
                Text("Party escolhida — toque numa posição pra trocar", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))

                // ↓ 4 posições, cada uma busca o Heroi correspondente na lista
                // de HERÓIS pelo id guardado em partyIds — combina as duas listas
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    chefe.partyIds.forEachIndexed { posicao, heroiId ->
                        val heroiDessaPosicao = heroiId?.let { viewModel.buscarHeroi(it) }
                        SlotDaParty(
                            posicao = posicao,
                            heroiAtual = heroiDessaPosicao,
                            todosOsHerois = viewModel.herois,
                            idsJaEscalados = chefe.partyIds,
                            onEscolher = { novoId -> viewModel.definirHeroiNaPosicao(chefe.id, posicao, novoId) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
                Button(onClick = { navController.popBackStack() }) { Text("← Voltar") }
            }
        }
    }
}

// Uma posição (1 a 4) da party de um chefe. Toque abre um menu com todos
// os heróis (e a opção "Nenhum"); heróis já escalados em OUTRA posição do
// mesmo chefe aparecem desabilitados, pra não duplicar.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SlotDaParty(
    posicao: Int,
    heroiAtual: Heroi?,
    todosOsHerois: List<Heroi>,
    idsJaEscalados: List<String?>,
    onEscolher: (String?) -> Unit
) {
    var menuAberto by remember { mutableStateOf(false) }

    Box {
        Card(modifier = Modifier.fillMaxWidth().clickable { menuAberto = true }) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Posição ${posicao + 1}",
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.width(90.dp)
                )
                Column(modifier = Modifier.weight(1f)) {
                    if (heroiAtual != null) {
                        Text(heroiAtual.nome, style = MaterialTheme.typography.bodyLarge)
                        Text(heroiAtual.classe, style = MaterialTheme.typography.bodySmall)
                    } else {
                        Text("Vazio — toque pra escolher", style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }

        DropdownMenu(expanded = menuAberto, onDismissRequest = { menuAberto = false }) {
            DropdownMenuItem(
                text = { Text("— Nenhum —") },
                onClick = { onEscolher(null); menuAberto = false }
            )
            todosOsHerois.forEach { heroi ->
                val emOutraPosicao = idsJaEscalados.contains(heroi.id) && heroiAtual?.id != heroi.id
                DropdownMenuItem(
                    text = { Text(heroi.nome + if (emOutraPosicao) " (já escalado)" else "") },
                    enabled = !emOutraPosicao,
                    onClick = { onEscolher(heroi.id); menuAberto = false }
                )
            }
        }
    }
}

// ── Preview ──────────────────────────────────────
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TelaChefeDetalhePreview() {
    AppComapanion2Theme {
        val vm: MeuViewModel = viewModel()
        TelaChefeDetalhe(navController = rememberNavController(), viewModel = vm, chefeId = vm.chefes.first().id)
    }
}
