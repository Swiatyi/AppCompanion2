package com.example.appcomapanion2

// ╔══════════════════════════════════════════════╗
// ║  TelaHome.kt                                 ║
// ║  TELA 1: TopAppBar + botões de navegação     ║
// ║  Ponto de partida pras 4 áreas do app        ║
// ╚══════════════════════════════════════════════╝

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
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
fun TelaHome(navController: NavHostController, viewModel: MeuViewModel) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Darkest Dungeon Companion") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DungeonSurfaceAlt,
                    titleContentColor = DungeonGold
                )
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Companion não-oficial de Darkest Dungeon: heróis, " +
                    "expedições, suprimentos e chefes antes de descer na escuridão.",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(onClick = { navController.navigate(Rotas.HEROIS) }, modifier = Modifier.fillMaxWidth()) {
                Text("Roster de Heróis")
            }
            Spacer(modifier = Modifier.height(10.dp))

            Button(onClick = { navController.navigate(Rotas.DUNGEON) }, modifier = Modifier.fillMaxWidth()) {
                Text("Expedições")
            }
            Spacer(modifier = Modifier.height(10.dp))

            Button(onClick = { navController.navigate(Rotas.SUPRIMENTOS) }, modifier = Modifier.fillMaxWidth()) {
                Text("Suprimentos")
            }
            Spacer(modifier = Modifier.height(10.dp))

            Button(onClick = { navController.navigate(Rotas.CHEFES) }, modifier = Modifier.fillMaxWidth()) {
                Text("Chefes")
            }
        }
    }
}

// ── Preview ──────────────────────────────────────
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TelaHomePreview() {
    AppComapanion2Theme {
        TelaHome(navController = rememberNavController(), viewModel = viewModel())
    }
}
