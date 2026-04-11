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

# Benvenuti in Laboratorio! 💻

Oggi cambiamo marcia. Niente più ore di sola teoria.
Le prossime 4 ore saranno dedicate a scrivere codice, sbagliare, cercare su Google (o chiedere all'IA) e far funzionare le cose.

**Come funzionano i Laboratori:**

1. 🧠 **Briefing & Recap:** 10 minuti di ripasso.
2. 👨‍🏫 **Scaffolding (Insieme):** Scriviamo l'architettura base guidati da me.
3. 🚀 **Sfida Autonoma:** Aggiungete funzionalità da soli (o in team).

---

# Recap Rapido: I 3 Mattoncini di Oggi

Per l'app di oggi vi serviranno solo questi 3 concetti chiave visti a lezione:

1. **Il Layout Base:** `Column` (per impilare le cose dall'alto verso il basso).
2. **L'Input:** `OutlinedTextField` (per far scrivere l'utente) e `keyboardOptions` (per far apparire il tastierino numerico anziché le lettere).
3. **La Memoria:** `var stato by remember { mutableStateOf("") }` (per ricordare cosa scrive l'utente e aggiornare lo schermo in tempo reale).

---

# Il Progetto di Oggi

## 🍕 Split The Bill (Il Conto alla Romana)

---

# Il Problema Reale

Siete in pizzeria con 5 amici.
Arriva il conto: **87,50€**.
Inizia il panico: _"Facciamo alla romana? Quanto fa a testa? Aspetta, io ho preso solo la margherita!"_

Oggi risolveremo questo dramma creando un'app che fa il lavoro sporco per noi.

---

# Architettura dell'App

La nostra app avrà una struttura molto semplice:

- **Input 1:** Casella di testo per il "Totale del Conto" (es. 87.50).
- **Input 2:** Casella di testo per il "Numero di Persone" (es. 5).
- **Output:** Un testo in grande che mostra la "Quota a Persona".

**La Formula Magica:** `Quota = Totale / Persone`

---

# Fase 1: Scaffolding

## (Lo facciamo insieme)

---

# Cosa faremo ora nella Fase 1

Aprite Android Studio. Io condividerò lo schermo e costruiremo insieme **le fondamenta** dell'app.

1. Creeremo una `Column` centrata.
2. Dichiariamo le variabili di Stato per catturare il testo.
3. Inseriamo i due `OutlinedTextField`.
4. Creiamo la logica base: convertiamo il testo in numeri (`toDoubleOrNull()`) e calcoliamo la divisione.

_Preparate le tastiere, vi aspetto pronti con un progetto vuoto!_

---

# Fase 2: Sfida Autonoma

## (Tocca a voi)

---

# Il vostro turno

La base funziona, ma è brutta da vedere e fa il minimo indispensabile.
È il momento di trasformarla in un'app "Premium".

Avete **due Task** da completare per finire il laboratorio di oggi. Il primo è puramente visivo (UI), il secondo richiede logica.

---

# 🎨 Task 1: Sfida Grafica (UI)

L'interfaccia base è troppo noiosa. Dovete renderla accattivante!

- **Spaziature:** Usate i `Modifier.padding` e gli `Spacer` per distanziare i componenti in modo elegante.
- **Tipografia:** Il risultato (la quota a persona) deve essere enorme e in grassetto (`fontWeight = Bold`).
- **Logica Visiva:** Se la quota a persona supera i **50€**, il testo del risultato deve diventare **ROSSO** (perché è una cena costosa!). Altrimenti, resta del colore di default.

---

# ⚙️ Task 2: Sfida Logica (La Mancia)

Negli Stati Uniti (e sempre più spesso anche qui), non si divide solo il conto, ma si aggiunge la **Mancia (Tip)**.

**Obiettivo:** Aggiungere un componente per calcolare la mancia prima di dividere il conto.

- Inserite un componente visivo per la percentuale di mancia (Potete usare un `Slider` o una fila di `Button` tipo: 5%, 10%, 20%).
- Modificate la formula matematica. _Esempio: Conto 100€ + Mancia 10% = 110€ totali._
- Dividete il NUOVO totale per le persone.

---

# 🤖 Un avviso sull'Intelligenza Artificiale

## Siete Ingegneri, non Copiatori.

---

# Come usare l'IA nel modo giusto

Siete autorizzati (e incoraggiati!) a usare ChatGPT, Gemini o Claude per superare le Sfide Autonome. Ma c'è una regola fondamentale.

❌ **Prompt Sbagliato:** _"Scrivimi tutto il codice di un'app in Jetpack Compose che fa il conto alla romana e aggiunge la mancia con uno slider."_
_(Se fate così, non imparerete nulla)._

✅ **Prompt Corretto (Da Ingegnere):** _"Come si usa il componente Slider in Jetpack Compose per selezionare un valore da 0 a 20? Fammi un piccolo esempio."_

---

# Il ruolo del Copilota

L'IA è il vostro **Assistente Junior**.
L'architettura (dove va la Column, come si passano i dati) la decidete **Voi**.
L'IA vi aiuta solo a ricordare la sintassi che non avete a memoria.

Se vi dà un blocco di codice che contiene comandi che non abbiamo mai spiegato a lezione... **NON COPIATELO**. Chiedetegli di spiegarvelo prima.

---

# Let's Code! 🚀

Condivido lo schermo, partiamo con la Fase 1!

---

![bg cover](../img/ultima_pagina.png)
