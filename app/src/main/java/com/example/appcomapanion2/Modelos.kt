package com.example.appcomapanion2

// ╔══════════════════════════════════════════════╗
// ║  Modelos.kt                                  ║
// ║  Data classes que representam os itens       ║
// ║  do app (ainda sem banco de dados)           ║
// ╚══════════════════════════════════════════════╝

data class Heroi(
    val id: String,
    val nome: String,
    val classe: String,
    val hp: Int,
    val stress: Int
)

data class Dungeon(
    val nome: String,
    val risco: String,
    val duracao: String
)

data class ItemSuprimento(
    val nome: String,
    val categoria: String,
    val selecionado: Boolean = false
)

data class Chefe(
    val id: String,
    val nome: String,
    val descricao: String,
    val hpBase: Int,
    val danoBase: Int,
    val level: Int = 1,
    val regiao: String = "Desconhecida",       // liga o chefe a um Dungeon.nome
    val partyIds: List<String?> = listOf(null, null, null, null) // 4 posições: id do Heroi ou null (vazio)
)

// TODO: nova entidade no app (ex: Item de inventário)? Crie a data class aqui.
