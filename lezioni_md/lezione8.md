---
marp: true
theme: default
paginate: true
header: "![w:100px](../img/virvelle.png)"
style: |
  header {
    top: 20px;
    left: 30px;
    position: absolute;
  }
  section {
    background-color: #f4f6f9;
    font-size: 26px;
  }
  h1 {
    font-size: 1.8em;
  }
  .center-content {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    text-align: center;
    height: 100%;
  }
---

![bg cover](../img/prima_pagina.png)

---

# Benvenuti al Laboratorio 2! 💻

Il primo laboratorio è andato. Ora che sapete come far interagire l'utente con l'app, è il momento di gestire **i dati veri**.
Oggi niente schermi statici: costruiremo un'app in grado di mostrare centinaia di elementi senza far esplodere il telefono.

**Come funzionano i Laboratori:**

1. 🧠 **Briefing & Recap:** 10 minuti di ripasso.
2. 👨‍🏫 **Scaffolding (Insieme):** Scriviamo l'architettura base guidati da me.
3. 🚀 **Sfida Autonoma:** Aggiungete funzionalità da soli (o in team).

---

# Recap Rapido: I 3 Mattoncini di Oggi

Per l'app di oggi vi serviranno questi 3 concetti chiave visti a lezione:

1. **La Data Class:** Lo "stampo" per i nostri dati (es. `val nome: String`, `val tipo: String`).
2. **Il Componente Visivo Custom:** Una funzione `@Composable` che disegna una singola riga (una Card con immagine e testo).
3. **La Lista Pigra:** `LazyColumn` e il magico blocco `items(lista)`, che prende i nostri dati e li trasforma in grafica scorrevole.

---

# Il Progetto di Oggi

## 📖 Il Pokedex (Liste Dinamiche)

---

# Il Problema Reale

Immaginate di dover mostrare i contatti di WhatsApp o un catalogo di Amazon.
Se provate a mettere 1.000 testi dentro una normale `Column`, il telefono proverà a disegnarli tutti e 1.000 contemporaneamente.
Risultato? **Crash o lag estremo.**

Oggi risolveremo questo problema creando una lista dinamica che disegna _solo_ quello che l'utente vede in quel momento sullo schermo.

---

# Architettura dell'App

La nostra app avrà una struttura modulare, in pieno stile Jetpack Compose:

- **I Dati (Mock Data):** Creeremo una finta lista di Pokemon direttamente nel codice, senza usare ancora internet.
- **La Riga (Row):** Disegneremo il singolo "mattoncino" (Immagine a sinistra, Nome e Tipo a destra).
- **Il Contenitore:** Inseriremo tutto in una `LazyColumn` a tutto schermo.

---

# Fase 1: Scaffolding

## (Lo facciamo insieme)

---

# Cosa faremo ora nella Fase 1

Aprite Android Studio. Io condividerò lo schermo e costruiremo insieme **le fondamenta** dell'app.

1. Creeremo una `data class Pokemon`.
2. Genereremo una `List` fissa di 10-15 Pokemon.
3. Creeremo il componente `@Composable fun PokemonCard(pokemon: Pokemon)`.
4. Invocheremo la `LazyColumn` per renderizzare la lista intera.

_Preparate le tastiere, vi aspetto pronti con un progetto vuoto!_

---

# Fase 2: Sfida Autonoma

## (Tocca a voi)

---

# Il vostro turno

La base funziona: la lista scorre. Ma sembra un foglio Excel, non un Pokedex!
È il momento di trasformarla in un'app "Premium".

Avete **due Task** da completare per finire il laboratorio di oggi. Il primo è puramente visivo (UI), il secondo vi farà sbattere la testa sulla logica.

---

# 🎨 Task 1: Sfida Grafica (Custom UI)

La riga singola deve diventare una vera "Card" moderna!

- **Forme e Ombre:** Inserite i testi e l'immagine dentro una `Card` con bordi arrotondati (`RoundedCornerShape`) e una leggera ombra (`elevation`).
- **Layout ordinato:** Usate `Spacer` e `Modifier.padding` per evitare che il testo sia appiccicato ai bordi.
- **Colori Dinamici (Bonus):** Cambiate il colore di sfondo della Card in base al "tipo" di Pokemon (Es. Rosso se è Fuoco, Blu se è Acqua). _Pensate a quale costrutto logico serve per farlo!_

---

# ⚙️ Task 2: Sfida Logica (La Search Bar)

Un Pokedex senza ricerca è inutile. Dobbiamo poter cercare il nostro Pokemon!

**Obiettivo:** Inserire una barra di ricerca in alto e filtrare la lista in tempo reale.

- Inserite un `OutlinedTextField` sopra la `LazyColumn`.
- Salvate il testo digitato in una **variabile di Stato**.
- Modificate la lista passata alla `LazyColumn`: non mostrategli tutta la lista, ma solo una **lista filtrata** che contiene il testo digitato dall'utente.

---

# 🤖 Un avviso sull'Intelligenza Artificiale

## Siete Ingegneri, non Copiatori.

---

# Come usare l'IA nel modo giusto

Siete autorizzati (e incoraggiati!) a usare ChatGPT, Gemini o Claude per superare le Sfide Autonome. Ma ricordate la regola d'oro.

❌ **Prompt Sbagliato:** _"Scrivimi il codice per fare un Pokedex in Jetpack Compose con la barra di ricerca in alto."_
_(L'IA farà un mix di codici vecchi e nuovi e non ci capirete nulla)._

✅ **Prompt Corretto (Da Ingegnere):** _"In Kotlin, come si fa a filtrare una List di oggetti in base a una stringa inserita dall'utente, ignorando maiuscole e minuscole? Fammi un esempio."_

---

# Il ruolo del Copilota

L'IA è il vostro **Assistente Junior**.
L'architettura (dove mettere lo Stato, come organizzare le Row) la decidete **Voi**.
L'IA vi aiuta a trovare il metodo Kotlin giusto per filtrare i dati (`.filter { ... }`).

Se vi dà un blocco di codice enorme con funzioni che non abbiamo mai visto... **NON COPIATELO**. Chiedetegli di semplificare usando le basi che conoscete.

---

# Let's Code! 🚀

Condivido lo schermo, partiamo con la Fase 1!

---

![bg cover](../img/ultima_pagina.png)
