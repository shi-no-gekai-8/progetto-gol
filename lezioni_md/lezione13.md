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

# Benvenuti al Laboratorio Finale! 🏆

Oggi non ci sono sconti. Uniremo tutto ciò che avete imparato in questi mesi: Navigazione, Sensori, GPS e Database locali.
Costruiremo un progetto reale, perfetto da inserire nel vostro portfolio.

**Come funziona il Lab Finale:**

1. 🧠 **Il Progetto:** Analizziamo l'architettura.
2. 👨‍🏫 **Scaffolding (Insieme):** Prepariamo le fondamenta.
3. 🚀 **Sfida Autonoma:** Le 3 Task per completare l'esame.

---

# Il Progetto Finale

## 🚨 SafeRide (Sicurezza Stradale)

---

# Il Problema Reale

Guidare distratti è una delle cause principali di incidenti. E se avessimo un'app che fa da "scatola nera" e assistente di guida?
Un'app che ci dice a quanto stiamo andando, rileva se freniamo troppo bruscamente e salva uno storico dei nostri viaggi per farci capire se stiamo guidando in modo sicuro.

---

# Architettura dell'App

La nostra app è modulare e unisce 3 grandi mondi:

1. **La Mappa (Navigazione):** Due schermate. La "Dashboard" (tachimetro in tempo reale) e lo "Storico Viaggi" (la lista dei viaggi passati).
2. **Il Motore (Sensori & GPS):** Il GPS calcola la velocità attuale. L'accelerometro ascolta le variazioni di movimento per rilevare gli impatti o le frenate.
3. **La Memoria (Room/DataStore):** Alla fine del viaggio, salviamo i dati salienti (Durata, Velocità Massima, Numero di frenate brusche) nel database locale.

---

# Fase 1: Scaffolding

## (Lo facciamo insieme)

---

# Cosa faremo ora nella Fase 1

Aprite Android Studio. Io condividerò lo schermo e scriveremo insieme la parte strutturale del progetto.

1. **NavHost:** Imposteremo le due rotte principali (`"dashboard"` e `"storico"`).
2. **Permessi:** Chiederemo il permesso per il GPS (`ACCESS_FINE_LOCATION`).
3. **Sensori Base:** Inizializzeremo il `SensorManager` e il `FusedLocationProviderClient` dentro il `ViewModel` per non perdere i dati.
4. **Il Database:** Creeremo una semplice Entity `Viaggio` in Room.

_Aprite un progetto vuoto, impostate la concentrazione al massimo. Si parte!_

---

# Fase 2: Sfida Autonoma

## (Il vostro turno)

---

# Il vostro turno

Le fondamenta ci sono: l'app legge la velocità dal GPS e ascolta l'accelerometro. Ma al momento stampa solo numeri noiosi nel Logcat.

Avete **tre Task** per trasformarla in un'app professionale e chiudere in bellezza questo corso.

---

# 🎨 Task 1: Sfida Grafica (Il Tachimetro Smart)

I numeri piccoli distraggono chi guida. Servono segnali visivi forti!

- **Obiettivo:** Creare un tachimetro che cambia colore in base alla velocità.
- Inserite un grande cerchio al centro dello schermo (`Box` con `background` circolare o un `CircularProgressIndicator`).
- Mostrate la velocità (convertita da m/s a **km/h**) al centro.
- **Logica visiva:** Se la velocità supera i **50 km/h**, il colore del tachimetro deve passare fluidamente da Verde a **ROSSO**.

---

# ⚙️ Task 2: Sfida Logica (Rilevamento Frenata)

Come facciamo a sapere se stiamo frenando bruscamente e non stiamo solo rallentando?

- **Obiettivo:** Rilevare la "Frenata Brusca" con l'accelerometro.
- Nel vostro `SensorEventListener`, calcolate la differenza di accelerazione (il _Delta_) sull'asse Y (che indica il movimento avanti/indietro del telefono, supponendo sia sul cruscotto).
- Se il calo di accelerazione è repentino e supera una certa soglia (es. `-5.0 m/s²`), incrementate un contatore `frenateBrusche` nel ViewModel.
- Mostrate questo contatore sulla UI!

---

# 💥 Task 3: Crash Test (Il Viaggio Zombie)

Siete a metà di un viaggio perfetto. Ruotate il telefono in orizzontale per usare il navigatore e... il viaggio si azzera!

**Obiettivo:** Proteggere lo stato attivo del viaggio.

- Attualmente, se ruotate lo schermo, la vostra `Activity` viene distrutta e ricreata, resettando i chilometri percorsi a zero.
- **La Soluzione:** Assicuratevi che tutte le variabili di stato in tempo reale (Velocità, Frenate, Chilometri) siano gestite rigorosamente all'interno del **ViewModel** e non nella UI con dei semplici `remember`.
- Provate a registrare un viaggio sull'emulatore, ruotare lo schermo, e verificare che i dati restino intatti!

---

# 🤖 Un avviso sull'Intelligenza Artificiale

## L'ultimo test da Ingegneri

---

# Come usare l'IA nel modo giusto

Lavorare con l'accelerometro e la fisica non è banale. Usate l'IA come consulente matematico, non come programmatore.

❌ **Prompt Sbagliato:** _"Scrivimi un'app Jetpack Compose che fa da tachimetro e rileva le frenate."_
_(Vi darà un codice spaghetti impossibile da inserire nel nostro NavHost)._

✅ **Prompt Corretto (Da Ingegnere):** _"Come converto la velocità restituita dall'oggetto Location di Android (che è in metri al secondo) in chilometri orari (km/h) in Kotlin?"_

---

# Let's Code! 🚀

Condivido lo schermo. Cominciamo dall'architettura del ViewModel.
**In bocca al lupo!**

---

![bg cover](../img/ultima_pagina.png)
