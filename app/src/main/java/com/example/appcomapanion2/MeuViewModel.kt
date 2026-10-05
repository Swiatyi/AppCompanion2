package com.example.appcomapanion2

// ╔══════════════════════════════════════════════╗
// ║  MeuViewModel.kt                             ║
// ║  Guarda o ESTADO que sobrevive à rotação     ║
// ║  Compartilhado entre todas as telas          ║
// ╚══════════════════════════════════════════════╝

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

class MeuViewModel : ViewModel() {

    // ============================================================
    // HERÓIS
    // ============================================================
    val herois = mutableStateListOf(
        Heroi("reynauld", "Reynauld", "Crusader", hp = 20, stress = 35),
        Heroi("dismas", "Dismas", "Highwayman", hp = 17, stress = 42),
        Heroi("junia", "Junia", "Plague Doctor", hp = 15, stress = 20),
        Heroi("paracelsus", "Paracelsus", "Occultist", hp = 14, stress = 55),
        Heroi("baldwin", "Baldwin", "Man-at-Arms", hp = 22, stress = 15)
    )

    fun adicionarHeroi(nome: String, classe: String) {
        herois.add(Heroi(id = "h" + System.currentTimeMillis(), nome = nome, classe = classe, hp = 15, stress = 0))
    }

    fun removerHeroi(heroi: Heroi) {
        herois.remove(heroi)
        // some do(s) chefe(s) qualquer party em que esse herói estivesse escalado
        for (i in chefes.indices) {
            val slots = chefes[i].partyIds
            if (slots.contains(heroi.id)) {
                chefes[i] = chefes[i].copy(partyIds = slots.map { if (it == heroi.id) null else it })
            }
        }
    }

    fun buscarHeroi(id: String): Heroi? = herois.find { it.id == id }

    fun aliviarStress(heroi: Heroi) {
        val idx = herois.indexOf(heroi)
        if (idx >= 0) herois[idx] = heroi.copy(stress = (heroi.stress - 10).coerceAtLeast(0))
    }

    fun estressar(heroi: Heroi) {
        val idx = herois.indexOf(heroi)
        if (idx >= 0) herois[idx] = heroi.copy(stress = (heroi.stress + 10).coerceAtMost(100))
    }

    // ============================================================
    // DUNGEONS — lista fixa, sem CRUD (não é uma das 2 listas do MAF)
    // ============================================================
    val dungeons = listOf(
        Dungeon("Ruins", "Médio", "Curta"),
        Dungeon("Warrens", "Alto", "Média"),
        Dungeon("Weald", "Baixo", "Curta"),
        Dungeon("Cove", "Alto", "Longa"),
        Dungeon("Darkest Dungeon", "Muito Alto", "Longa")
    )

    // ============================================================
    // SUPRIMENTOS
    // ============================================================
    val itensSuprimento = mutableStateListOf(
        ItemSuprimento("Tochas", "Iluminação"),
        ItemSuprimento("Comida", "Sobrevivência"),
        ItemSuprimento("Antídoto", "Cura"),
        ItemSuprimento("Bandagem", "Cura"),
        ItemSuprimento("Chave de Baú", "Utilidade"),
        ItemSuprimento("Pá", "Utilidade")
    )

    fun toggleSuprimento(index: Int) {
        itensSuprimento[index] = itensSuprimento[index].copy(selecionado = !itensSuprimento[index].selecionado)
    }

    fun marcarTodosSuprimentos() {
        for (i in itensSuprimento.indices) itensSuprimento[i] = itensSuprimento[i].copy(selecionado = true)
    }

    fun limparTodosSuprimentos() {
        for (i in itensSuprimento.indices) itensSuprimento[i] = itensSuprimento[i].copy(selecionado = false)
    }

    // ============================================================
    // CHEFES
    // ============================================================
    val chefes = mutableStateListOf(
        Chefe("necromancer", "Necromancer", "Comanda os mortos-vivos nas Ruins.", hpBase = 80, danoBase = 12, regiao = "Ruins"),
        Chefe("brigand_pope", "Brigand Pope", "Líder fanático dos Bandidos.", hpBase = 110, danoBase = 18, regiao = "Warrens"),
        Chefe("hag", "The Hag", "Captura e cozinha heróis no Weald.", hpBase = 95, danoBase = 15, regiao = "Weald"),
        Chefe("siren", "Siren", "Hipnotiza a party inteira na Cove.", hpBase = 90, danoBase = 14, regiao = "Cove"),
        Chefe("fanatic", "The Fanatic", "Guarda fanático dos segredos do Darkest Dungeon.", hpBase = 130, danoBase = 20, regiao = "Darkest Dungeon")
    )

    fun adicionarChefe(nome: String, descricao: String) {
        chefes.add(
            Chefe(
                id = "c" + System.currentTimeMillis(),
                nome = nome,
                descricao = descricao.ifBlank { "Sem descrição ainda." },
                hpBase = 70,
                danoBase = 10
            )
        )
    }

    fun removerChefe(chefe: Chefe) {
        chefes.remove(chefe)
    }

    fun buscarChefe(id: String): Chefe? = chefes.find { it.id == id }

    fun mudarNivelChefe(chefe: Chefe, delta: Int) {
        val idx = chefes.indexOf(chefe)
        if (idx >= 0) {
            val novoNivel = (chefe.level + delta).coerceIn(1, 10)
            chefes[idx] = chefe.copy(level = novoNivel)
        }
    }

    // ---- Party por chefe: 4 posições, cada uma com um Heroi (ou vazia) ----

    // Escolhe explicitamente QUAL herói ocupa a posição (0 a 3) de um chefe.
    // heroiId = null limpa a posição.
    fun definirHeroiNaPosicao(chefeId: String, posicao: Int, heroiId: String?) {
        val idx = chefes.indexOfFirst { it.id == chefeId }
        if (idx < 0) return
        val chefe = chefes[idx]
        val novaParty = chefe.partyIds.toMutableList()
        // evita o mesmo herói em 2 posições do mesmo chefe
        if (heroiId != null) {
            for (i in novaParty.indices) if (novaParty[i] == heroiId) novaParty[i] = null
        }
        novaParty[posicao] = heroiId
        chefes[idx] = chefe.copy(partyIds = novaParty)
    }

    // Usado pelo botão "+" da tela de Heróis: joga o herói na primeira vaga
    // livre do chefe escolhido. Retorna false se não havia vaga (party cheia).
    fun adicionarHeroiAoChefe(chefeId: String, heroiId: String): Boolean {
        val idx = chefes.indexOfFirst { it.id == chefeId }
        if (idx < 0) return false
        val chefe = chefes[idx]
        if (chefe.partyIds.contains(heroiId)) return true // já está na party, nada a fazer
        val posicaoLivre = chefe.partyIds.indexOfFirst { it == null }
        if (posicaoLivre < 0) return false // party cheia (4/4)
        definirHeroiNaPosicao(chefeId, posicaoLivre, heroiId)
        return true
    }

    // TODO: quando chegar a hora de persistir dados de verdade (próximo
    // trabalho), é AQUI dentro do ViewModel que entra Room/DataStore/API.
    // As telas não precisam saber de onde os dados vêm — elas só chamam
    // os métodos acima, então não vão precisar mudar quase nada.
}
