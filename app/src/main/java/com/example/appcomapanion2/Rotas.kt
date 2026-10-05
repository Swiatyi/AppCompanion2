package com.example.appcomapanion2

// ╔══════════════════════════════════════════════╗
// ║  Rotas.kt                                    ║
// ║  Constantes das ROTAS de navegação           ║
// ║  Centraliza os nomes para evitar typos       ║
// ╚══════════════════════════════════════════════╝

object Rotas {
    const val HOME = "home"
    const val HEROIS = "herois"
    const val DUNGEON = "dungeon"
    const val SUPRIMENTOS = "suprimentos"
    const val CHEFES = "chefes"

    // Rota com argumento: "heroiDetalhe/{heroiId}"
    const val HEROI_DETALHE = "heroiDetalhe/{heroiId}"
    fun heroiDetalhe(id: String) = "heroiDetalhe/$id"

    // Rota com argumento: "chefeDetalhe/{chefeId}"
    const val CHEFE_DETALHE = "chefeDetalhe/{chefeId}"
    fun chefeDetalhe(id: String) = "chefeDetalhe/$id"

    // TODO: nova tela? Adicione a const val (e a fun, se tiver argumento) aqui.
}
