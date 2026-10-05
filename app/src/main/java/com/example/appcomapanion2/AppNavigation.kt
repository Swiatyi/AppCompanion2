package com.example.appcomapanion2

// ╔══════════════════════════════════════════════╗
// ║  AppNavigation.kt                            ║
// ║  Define TODAS as rotas do app (NavHost)      ║
// ║  Conecta cada rota ao Composable correto     ║
// ╚══════════════════════════════════════════════╝

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

@Composable
fun AppNavigation() {
    // Cria o controlador de navegação (gerencia pilha de telas)
    val navController = rememberNavController()

    // ViewModel criado UMA VEZ aqui e compartilhado entre todas as telas
    val viewModel: MeuViewModel = viewModel()

    // NavHost = "mapa" de todas as telas do app
    NavHost(
        navController = navController,
        startDestination = Rotas.HOME
    ) {
        composable(Rotas.HOME) {
            TelaHome(navController = navController, viewModel = viewModel)
        }
        composable(Rotas.HEROIS) {
            TelaHerois(navController = navController, viewModel = viewModel)
        }
        composable(Rotas.DUNGEON) {
            TelaDungeon(navController = navController, viewModel = viewModel)
        }
        composable(Rotas.SUPRIMENTOS) {
            TelaSuprimentos(navController = navController, viewModel = viewModel)
        }
        composable(Rotas.CHEFES) {
            TelaChefes(navController = navController, viewModel = viewModel)
        }

        // Rota com argumento: "heroiDetalhe/{heroiId}"
        composable(
            route = Rotas.HEROI_DETALHE,
            arguments = listOf(navArgument("heroiId") { type = NavType.StringType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("heroiId") ?: ""
            TelaHeroiDetalhe(navController = navController, viewModel = viewModel, heroiId = id)
        }

        // Rota com argumento: "chefeDetalhe/{chefeId}"
        composable(
            route = Rotas.CHEFE_DETALHE,
            arguments = listOf(navArgument("chefeId") { type = NavType.StringType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("chefeId") ?: ""
            TelaChefeDetalhe(navController = navController, viewModel = viewModel, chefeId = id)
        }

        // TODO: nova tela? Registre a rota aqui (e crie a const em Rotas.kt).
    }
}
