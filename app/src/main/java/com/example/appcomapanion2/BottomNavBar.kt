package com.example.appcomapanion2

// ╔══════════════════════════════════════════════╗
// ║  BottomNavBar.kt                             ║
// ║  Barra de navegação inferior com 4 áreas     ║
// ║  Destaca automaticamente a área ativa        ║
// ╚══════════════════════════════════════════════╝

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.appcomapanion2.ui.theme.AppComapanion2Theme

@Composable
fun BottomNavBar(navController: NavHostController) {

    // Observa qual rota está ativa (atualiza automaticamente)
    val backStackEntry by navController.currentBackStackEntryAsState()
    val rotaAtual = backStackEntry?.destination?.route

    NavigationBar {

        NavigationBarItem(
            selected = rotaAtual == Rotas.HEROIS,
            onClick = { navegarSeNecessario(navController, rotaAtual, Rotas.HEROIS) },
            icon = { Icon(Icons.Default.Groups, contentDescription = "Heróis") },
            label = { Text("Heróis") }
        )

        NavigationBarItem(
            selected = rotaAtual == Rotas.DUNGEON,
            onClick = { navegarSeNecessario(navController, rotaAtual, Rotas.DUNGEON) },
            icon = { Icon(Icons.Default.Explore, contentDescription = "Expedições") },
            label = { Text("Expedições") }
        )

        NavigationBarItem(
            selected = rotaAtual == Rotas.SUPRIMENTOS,
            onClick = { navegarSeNecessario(navController, rotaAtual, Rotas.SUPRIMENTOS) },
            icon = { Icon(Icons.Default.Inventory2, contentDescription = "Suprimentos") },
            label = { Text("Suprimentos") }
        )

        NavigationBarItem(
            selected = rotaAtual == Rotas.CHEFES,
            onClick = { navegarSeNecessario(navController, rotaAtual, Rotas.CHEFES) },
            icon = { Icon(Icons.Default.Whatshot, contentDescription = "Chefes") },
            label = { Text("Chefes") }
        )
    }
}

// Evita empilhar a mesma tela de novo se o usuário já está nela
private fun navegarSeNecessario(navController: NavHostController, rotaAtual: String?, destino: String) {
    if (rotaAtual != destino) {
        navController.navigate(destino) {
            popUpTo(Rotas.HOME)
            launchSingleTop = true
        }
    }
}

// ── Preview ──────────────────────────────────────
@Preview(showBackground = true)
@Composable
fun BottomNavBarPreview() {
    AppComapanion2Theme {
        BottomNavBar(navController = rememberNavController())
    }
}
