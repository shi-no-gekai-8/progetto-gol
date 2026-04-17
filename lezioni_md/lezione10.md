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

# Benvenuti al Laboratorio 4! 💻

Fino ad oggi le nostre app vivevano in una "scatola chiusa". Non sapevano nulla del mondo esterno.
Oggi rompiamo le pareti: la nostra app si collegherà a **Internet** per scaricare dati in tempo reale.

**Come funzionano i Laboratori:**

1. 🧠 **Briefing & Recap:** 10 minuti di ripasso.
2. 👨‍🏫 **Scaffolding (Insieme):** Scriviamo l'architettura base guidati da me.
3. 🚀 **Sfida Autonoma:** Aggiungete funzionalità da soli (o in team).

---

# Recap Rapido: I 3 Mattoncini di Oggi

Per parlare con Internet, useremo questi 3 concetti:

1. **Retrofit:** _💡 È il nostro "cameriere". Noi gli diamo l'ordinazione (l'URL), lui va in cucina (il Server), aspetta, e ci riporta il piatto pronto (il file JSON) già trasformato in oggetti Kotlin._
2. **Asincronia (`LaunchedEffect`):** _💡 Internet è lento. Se aspettiamo il server sul "Main Thread", l'app si congela. Le chiamate di rete vanno fatte in background!_
3. **Gli Stati della UI (`UI State`):** _💡 Quando interroghiamo un'API, l'app può trovarsi in soli 3 stati: Caricamento (Rotellina), Successo (Dati) o Errore (Disastro). Dobbiamo gestirli tutti._

---

# Il Progetto di Oggi

## 🎬 Movie Finder (Internet & API)

---

# Il Problema Reale

Un'app come Netflix non ha tutti i film salvati dentro il vostro telefono, altrimenti peserebbe 50 Terabyte! I dati risiedono su un server distante.
Noi dobbiamo imparare a **chiedere** questi dati solo quando l'utente li cerca, mostrando un feedback visivo mentre aspettiamo la risposta.

---

# Architettura dell'App

Costruiremo un'app basata sulle API pubbliche di **OMDB** (Open Movie Database):

- **Il Motore:** Una `data class` per mappare il JSON in arrivo (Titolo, Trama, URL della Locandina).
- **L'Attesa:** Una schermata che mostra un `CircularProgressIndicator` mentre Retrofit lavora.
- **Il Risultato:** Una Card accattivante che mostra i dettagli del film scaricato.

---

# Fase 1: Scaffolding

## (Lo facciamo insieme)

---

# Cosa faremo ora nella Fase 1

Aprite Android Studio. Io condividerò lo schermo e scriveremo insieme la parte più "noiosa" ma fondamentale del networking.

1. Aggiungeremo il permesso `INTERNET` nel Manifest (senza questo, non si va da nessuna parte).
2. Creeremo le Data Class leggendo la struttura del JSON di OMDB.
3. Configureremo l'istanza di Retrofit.
4. Faremo una chiamata "fissa" (es. cercheremo sempre "Batman") per vedere comparire la rotellina di caricamento e i primi testi.

_Preparate le tastiere e verificate di essere connessi al Wi-Fi!_

---

# Fase 2: Sfida Autonoma

## (Tocca a voi)

---

# Il vostro turno

Abbiamo il collegamento a Internet, ma l'app cerca sempre lo stesso film e i dati sono solo testo grezzo. È il momento di farla diventare una vera app!

Avete **tre Task** da completare per finire il laboratorio: Grafica (con un focus sulle immagini), Logica e un Crash Test molto subdolo.

---

# 🎨 Task 1: Sfida Grafica (Le Locandine)

Il JSON di OMDB ci restituisce l'URL del poster del film (es. `https://.../poster.jpg`). Ma Compose di base non sa scaricare immagini da un link!

- **Obiettivo:** Mostrare la locandina del film.
- **La Soluzione:** Dovete usare una libreria esterna chiamata **Coil**.
- Cercate su Google (o chiedete all'IA) come aggiungere Coil al file `build.gradle.kts` e come usare il componente `AsyncImage` per disegnare l'immagine a schermo. Poi, abbellite la Card!

---

# ⚙️ Task 2: Sfida Logica (Motore di Ricerca)

Cercare sempre "Batman" è noioso. L'utente deve poter scegliere!

**Obiettivo:** Rendere la chiamata API dinamica.

- Inserite un `OutlinedTextField` e un `Button` ("Cerca") sopra la Card del risultato.
- Salvate il testo digitato dall'utente in una variabile di stato.
- Modificate il `LaunchedEffect` (o usate un `CoroutineScope` sul click del bottone) affinché Retrofit passi alla query dell'API il titolo digitato dall'utente invece della parola fissa.

---

# 💥 Task 3: Crash Test (Il Film Inesistente)

Cosa succede se l'utente cerca "asdfghjkl"?

**Obiettivo:** Gestire le risposte "ingannevoli" dell'API.

- OMDB è subdolo: se non trova un film, non va in errore di rete (Codice 404). Risponde con un **Successo (Codice 200)**, ma il JSON dice: `{"Response":"False", "Error":"Movie not found!"}`.
- Se provate a leggere il Titolo da quel JSON, l'app vi mostrerà dati vuoti o andrà in crash per un valore nullo.
- **La Soluzione:** Aggiornate la vostra `data class` per leggere il campo "Response". Prima di disegnare la UI, verificate con un `if/else` se il film è stato trovato. Altrimenti mostrate un messaggio di errore all'utente!

---

# 🤖 Un avviso sull'Intelligenza Artificiale

## Siete Ingegneri, non Copiatori.

---

# Come usare l'IA nel modo giusto

Lavorare con JSON e Retrofit richiede molta precisione. L'IA è perfetta per aiutarvi a fare il "parsing" (traduzione) dei dati.

❌ **Prompt Sbagliato:** _"Scrivimi l'app Movie Finder con OMDB in Jetpack Compose."_
_(Vi darà un codice basato su librerie vecchie di 4 anni che non compilerà mai)._

✅ **Prompt Corretto (Da Ingegnere):** _"Come si usa la libreria Coil in Jetpack Compose per caricare un'immagine partendo da una stringa URL? Dammi un esempio di AsyncImage."_

---

# Il ruolo del Copilota

Se l'IA vi suggerisce di usare librerie come `Glide` o `Picasso`, sappiate che sono vecchie e pensate per XML, non per Compose. Usate **Coil**.

Se l'app non compila, copiate l'errore rosso (Logcat) e chiedete all'IA di spiegarvi _cosa_ significa quell'errore, non solo di risolverlo ciecamente.

---

# Let's Code! 🚀

Condivido lo schermo. Apriamo il Manifest e chiediamo il permesso a Internet!

---

![bg cover](../img/ultima_pagina.png)
