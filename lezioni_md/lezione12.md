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

# Benvenuti al Laboratorio 6! 💻

Fino ad ora le nostre app vivevano di dati inseriti a mano o scaricati da internet.
Oggi l'app si connette con il **mondo fisico**: capiremo esattamente dove si trova l'utente e salveremo i suoi progressi nel tempo in modo leggero e persistente.

**Come funzionano i Laboratori:**

1. 🧠 **Briefing & Recap:** 10 minuti di ripasso.
2. 👨‍🏫 **Scaffolding (Insieme):** Scriviamo l'architettura base guidati da me.
3. 🚀 **Sfida Autonoma:** Aggiungete funzionalità da soli (o in team).

---

# Recap Rapido: I 3 Mattoncini di Oggi

1. **Permessi a Runtime:** _💡 Dimenticate il solo Manifest. Per dati sensibili come la posizione, l'utente deve cliccare su "Consenti" mentre usa l'app. È una questione di privacy._
2. **GPS (FusedLocationProvider):** _💡 Il sistema intelligente di Google. Sceglie lui se usare il satellite (preciso ma consuma batteria) o il Wi-Fi/Celle telefoniche per trovarci._
3. **DataStore (Preferences):** _💡 L'evoluzione moderna delle vecchie SharedPreferences. Ottimo per salvare piccoli dati (es. "Passi totali: 4000") in modo asincrono senza il peso di Room._

---

# Il Progetto di Oggi

## 🏃‍♂️ GPS Pedometer (Il Contapassi Fake)

---

# Il Problema Reale

Volete tracciare la vostra passeggiata domenicale. Vi serve un'app che conti i chilometri (o i passi) e che, se la chiudete per rispondere a un messaggio su WhatsApp, non vi faccia perdere tutto il progresso della giornata.

Oggi costruiremo un **Tracker GPS** che calcola la distanza percorsa e la traduce in un conteggio passi stimato, salvandolo in memoria.

---

# Architettura dell'App

La nostra app farà tre cose in sequenza:

- **Il Guardiano:** Chiederà educatamente il permesso `ACCESS_FINE_LOCATION` all'avvio.
- **Il Sensore:** Leggerà le coordinate GPS a intervalli regolari, calcolando i metri percorsi.
- **Il Taccuino:** Salverà il numero di passi nel **DataStore**, così chiudendo l'app il conteggio non ripartirà da zero.

---

# Fase 1: Scaffolding

## (Lo facciamo insieme)

---

# Cosa faremo ora nella Fase 1

Aprite Android Studio. Io condividerò lo schermo e scriveremo la parte burocratica (Permessi e Setup) che è sempre la più insidiosa.

1. Aggiungeremo i permessi GPS nel `AndroidManifest.xml`.
2. Creeremo il "launcher" per il popup di richiesta permessi a schermo.
3. Inizializzeremo il `FusedLocationProviderClient` per ottenere la prima coordinata.
4. Creeremo l'istanza del `DataStore` per salvare il contatore.

_Preparate le tastiere. E ricordatevi che sull'emulatore simuleremo il movimento!_

---

# Fase 2: Sfida Autonoma

## (Tocca a voi)

---

# Il vostro turno

La base è scritta: il permesso viene chiesto e il GPS (se simulato) ci lancia delle coordinate. Ma l'app è vuota. È il momento di trasformarla in un fitness tracker.

Avete **tre Task** da completare per finire il laboratorio: Grafica (stile Apple Watch), Logica (Matematica GPS) e l'immancabile Crash Test.

---

# 🎨 Task 1: Sfida Grafica (Gli Anelli di Attività)

I contapassi moderni usano anelli visivi per motivare l'utente, non semplici numeri.

- **Obiettivo:** Creare un obiettivo giornaliero (Es. 10.000 passi).
- **La Soluzione:** Usate un `CircularProgressIndicator` gigante al centro dello schermo.
- Impostate il `progress` come la divisione tra i passi attuali e il goal (es. 5000 / 10000 = 0.5f).
- Inserite il testo con il numero dei passi **al centro** del cerchio usando un componente `Box`.

---

# ⚙️ Task 2: Sfida Logica (La Formula Magica)

Non stiamo usando il sensore "Pedometer", ma il GPS. Dobbiamo tradurre la distanza in passi e salvarla.

**Obiettivo:** Trasformare i metri in passi e usare il DataStore.

- **La Matematica:** La lunghezza media di un passo umano è di **0,7 metri**.
- Se il GPS vi dice che avete percorso 14 metri, quanti passi avete fatto? (Semplice divisione).
- **Persistenza:** Ogni volta che i passi aumentano, scrivete il nuovo valore nel `DataStore` usando una Coroutine. All'avvio dell'app, leggete quel valore per non ripartire da zero!

---

# 💥 Task 3: Crash Test (Il Muro del "NO")

Gli utenti sono paranoici (giustamente). Cosa succede se alla richiesta del GPS cliccano su **"Rifiuta"**?

**Obiettivo:** Sopravvivere al permesso negato.

- Riavviate l'app (o cancellate i dati dell'app dalle impostazioni dell'emulatore).
- Quando appare il popup dei permessi, cliccate "Non consentire".
- **Il Disastro:** Spesso lo schermo rimane bianco o l'app va in crash se cerca di leggere il GPS forzatamente.
- **La Soluzione:** Usate lo Stato! Se il permesso è negato, nascondete gli anelli di attività e mostrate un grande `Text` e un `Icon` di avviso: _"Devi abilitare il GPS per usare l'app!"_, accompagnato da un bottone per riprovare.

---

# 🤖 Un avviso sull'Intelligenza Artificiale

## Siete Ingegneri, non Copiatori.

---

# Come usare l'IA nel modo giusto

Gestire la distanza tra due punti GPS (`Location.distanceTo`) e salvare asincronamente su DataStore richiede sintassi complessa. Usate l'IA per aiutarvi a collegare i pezzi.

❌ **Prompt Sbagliato:** _"Scrivimi un contapassi GPS in Jetpack Compose con DataStore e CircularProgress."_
_(L'IA vi genererà una classe monolitica illeggibile)._

✅ **Prompt Corretto (Da Ingegnere):** _"Come si usa DataStore Preferences in Kotlin per salvare un numero intero (i passi totali) e leggerlo come Flow dentro un ViewModel?"_

---

# Il ruolo del Copilota

Se l'IA vi suggerisce di usare le `SharedPreferences`, **fermatela e correggetela** ("Voglio usare Jetpack DataStore"). Le SharedPreferences sono vecchie e sincrone, noi vogliamo fare le cose nel modo moderno.

Per simulare la passeggiata: nell'emulatore cliccate i tre puntini `...` -> **Location** -> **Routes**, cercate un punto di partenza e uno di arrivo e cliccate "Play Route". Il telefono inizierà a "camminare"!

---

# Let's Code! 🚀

Condivido lo schermo. Apriamo il Manifest e iniziamo l'avventura!

---

![bg cover](../img/ultima_pagina.png)
