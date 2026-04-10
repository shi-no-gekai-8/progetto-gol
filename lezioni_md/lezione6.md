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

# Recap: La Memoria di Ferro

Nella scorsa lezione abbiamo dato alla nostra app una **memoria a lungo termine**:

- **DataStore:** Per le piccole impostazioni (Key-Value).
- **Room Database:** Per mappare tabelle complesse e fare ricerche SQL scrivendo in puro Kotlin.
- Abbiamo scoperto le animazioni e il **Motion UX**.

Oggi facciamo l'ultimo passo: la nostra app prende vita, percepisce il mondo e comunica da sola.

---

# Obiettivi di Oggi

## L'App nel Mondo Reale

1. **I Sensi dell'App:** Sensori Hardware e richiami IoT.
2. **Il Muro della Privacy:** Gestione dei Permessi.
3. **L'App che non dorme:** I Servizi (Foreground e Background).
4. **Ascoltare il Sistema:** Broadcast Receivers.
5. **Richiamare l'Attenzione:** Le Notifiche.
6. **Demo Finale:** Costruiamo un "Antifurto" per smartphone.

---

# Parte 1: L'App ha dei Sensi

## Hardware, Sensori e il mondo IoT

---

# Oltre lo Schermo

Fino ad oggi abbiamo considerato lo smartphone solo come un piccolo schermo con una tastiera.
Ma sotto il vetro c'è un concentrato di **Sensori Elettronici** che lo rendono uno strumento di misurazione scientifica avanzato.

In Android, per parlare con questo hardware, usiamo una classe chiamata **`SensorManager`**.

---

# Le Tre Famiglie di Sensori

1. 🏃 **Sensori di Movimento:** Accelerometro, Giroscopio, Contapassi. Misurano le forze fisiche applicate al dispositivo.
2. 🌍 **Sensori di Posizione:** GPS, Magnetometro (Bussola). Ci dicono dove siamo nel mondo.
3. 🌡️ **Sensori Ambientali:** Barometro, Sensore di Luce, Termometro (raro oggi). Misurano l'ambiente circostante.

---

# Lo Smartphone come "Edge Device" (IoT)

Voi studiate Elettronica e Telecomunicazioni. Sapete cos'è un Arduino o un ESP32.
Nell'Internet of Things (IoT), i dispositivi che raccolgono dati si chiamano **Edge Devices**.

Oggi, moltissimi progetti industriali usano vecchi smartphone Android come Edge Devices invece di creare hardware personalizzato. Perché?
_Ha sensori integrati, Wi-Fi, batteria tampone e uno schermo... per 100€!_

---

# Leggere un Sensore (La Logica)

Come fa l'app a sapere se stiamo correndo? Non chiede il dato una volta sola.
Si **iscrive (Listener)** al sensore.

```kotlin
// Esempio pseudo-codice della logica del SensorManager
sensorManager.registerListener(
    accelerometro,
    velocitaAggiornamento = "MOLTO_VELOCE"
) { dati ->
    val asseX = dati[0]
    val asseY = dati[1]
    println("Stai muovendo il telefono con forza: $asseX")
}
```

---

# Il lato oscuro dei sensori

L'accelerometro non serve solo a girare lo schermo in orizzontale o a giocare ai giochi di corsa. Un'intelligenza artificiale che analizza i dati dell'accelerometro può capire esattamente:

- Se state camminando, correndo o siete in auto.
- Il vostro **Parkinson** (dai micro-tremori della mano).
- Il vostro livello di ubriachezza.

---

# Dibattito: Assicurazioni e Privacy

Immaginate che un'Assicurazione Auto vi offra uno sconto del 50% sul premio annuale, a patto che installiate la loro app.

L'app usa l'accelerometro e il GPS per capire se frenate bruscamente, se accelerate troppo o se guardate il telefono mentre guidate.

_Accettereste questo compromesso? E se diventasse obbligatorio per tutti?_

---

# Parte 2: Il Muro della Sicurezza

# I Permessi (Permissions)

---

# Perché esistono i Permessi?

Nei primi anni di Android (fino al 2015), quando scaricavi un'app, il Play Store ti mostrava un muro di testo: _"Quest'app userà fotocamera, contatti, microfono e GPS"_. L'utente premeva "Accetto" senza leggere e l'app faceva ciò che voleva in background per sempre.

Oggi, Android utilizza un sistema a **Sandboxing** e permessi a Runtime.

---

# La Sandbox (Il recinto di sabbia)

Ogni app su Android è isolata. Vive in un recinto di sabbia chiuso. La mia app "Calcolatrice" non può leggere i file di "WhatsApp".

Per scavalcare il recinto e accedere al mondo (Internet, GPS, Fotocamera), l'app deve chiedere il **Permesso** al Re (Il Sistema Operativo).

---

# Permessi Normali vs Pericolosi

- 🟢 **Permessi Normali:** (Es. Internet, Bluetooth, Allarme). Non ledono la privacy. Li dichiari nel file `AndroidManifest.xml` e il sistema te li accorda automaticamente.
- 🔴 **Permessi Pericolosi:** (Es. Fotocamera, Microfono, Posizione, Rubrica). Sono bloccati di default. L'utente deve premere fisicamente su un **Popup di Sistema** per consentirli.

---

# Dibattito: L'App Torcia

È il 2014. Scaricate dal Play Store una normalissima app "Torcia" per fare luce al buio.

All'avvio, vi compare questo popup: _"Torcia HD vuole accedere ai tuoi Contatti, ai tuoi SMS e alla tua Posizione GPS."_

_Perché un'app del genere chiedeva quei permessi? Cosa ci faceva con i vostri dati? (Indizio: i dati sono il nuovo petrolio)._

---

# Runtime Permissions (Oggi)

Oggi i permessi si chiedono nel momento esatto in cui servono (Runtime). Se apro WhatsApp, non mi chiede il microfono all'avvio. Me lo chiede **solo quando provo a mandare un vocale per la prima volta**.

Questo dà fiducia all'utente: _"Ah, mi chiede il microfono perché ho appena premuto il tasto del microfono!"_

---

# Il Flusso Logico dei Permessi

Quando scriviamo codice per un permesso pericoloso, non possiamo solo dire "Apri la fotocamera". Dobbiamo gestire 3 casi:

1.  **Mai chiesto prima:** Mostriamo il popup.
2.  **Accettato:** Apriamo la fotocamera.
3.  **Rifiutato:** L'utente ha detto NO. Dobbiamo disabilitare il bottone e spiegare _perché_ l'app non può funzionare.

---

# I Permessi in Jetpack Compose

In Jetpack Compose, gestire i permessi è diventato molto più pulito grazie alla libreria di Google.

Kotlin

```
// Chiediamo il permesso per la fotocamera
val permessoCamera = rememberPermissionState(
    Permission.CAMERA
)

if (permessoCamera.status.isGranted) {
    Text("Fotocamera Pronta!")
} else {
    Button(onClick = { permessoCamera.launchPermissionRequest() }) {
        Text("Richiedi Accesso")
    }
}

```

---

# UX: Non farsi odiare (La Rationale)

Se l'utente preme "Nega" a un permesso, il sistema operativo (Android 11+) gli permette di bloccare per sempre la richiesta.

Per evitarlo, prima di chiedere il permesso, i bravi sviluppatori mostrano una schermata di **Rationale** (Spiegazione): _"Caro utente, ci serve il GPS per dirti dove hai parcheggiato l'auto. Promettiamo di non inviare questi dati a terzi."_

---

# Parte 3: L'App che non dorme

## Services e WorkManager

---

# Il limite dell'Activity

Abbiamo imparato che l'Activity (la schermata UI) vive solo finché è visibile (`onResume`). Se premete il tasto "Home", l'app va in `onStop`. Le coroutines si fermano, i download si bloccano, il GPS si spegne.

E se volessimo che un'operazione continuasse in background? Abbiamo bisogno di un **Service** (Servizio).

---

# Cos'è un Service?

Un Service è un componente Android **senza interfaccia grafica**. Gira in background e continua a lavorare anche se l'utente sta usando un'altra app.

Attenzione: Il Service gira di base sul Main Thread! Bisogna comunque usare le Coroutines al suo interno se facciamo operazioni pesanti.

---

# Tipi di Servizi

Ci sono due modi per far sopravvivere il nostro codice "dietro le quinte":

1.  **Foreground Service (In Primo Piano)**
2.  **Background Service (Dietro le quinte)**

---

# Foreground Service (Farsi Notare)

Android non ama le app che consumano batteria di nascosto. Se volete usare il GPS o suonare musica mentre l'utente usa WhatsApp, dovete usare un **Foreground Service**.

**La Regola:** Siete obbligati a mostrare una **Notifica ineliminabile**. L'utente deve _sapere_ che state consumando le sue risorse (Pensate al navigatore di Google Maps o a Spotify).

---

# Background Service e WorkManager

E se volessimo semplicemente dire all'app: _"Fai il backup del database sul cloud una volta al giorno"_?

Per operazioni differibili nel tempo, non usiamo più i Service tradizionali. Usiamo **WorkManager**.

---

# La Magia del WorkManager

Il WorkManager è il maggiordomo intelligente di Android. Gli date un compito e le **Condizioni** in cui deve svolgerlo.

```
// Esempio logico di WorkManager:
val backupLavoro = PeriodicWorkRequest.Builder(
    BackupDatabaseWorker::class.java,
    24, TimeUnit.HOURS
).setConstraints(
    Constraints.Builder()
        .setRequiresCharging(true) // Solo se in carica!
        .setRequiredNetworkType(NetworkType.UNMETERED) // Solo Wi-Fi!
        .build()
).build()

```

_Il sistema sceglierà il momento perfetto per non prosciugare la batteria._

---

# IoT Tie-In: MQTT vs HTTP

Come comunicano i veri Edge Devices?

---

# Dal Web all'IoT

Nel Modulo 4 abbiamo usato **HTTP (Retrofit)**. HTTP è perfetto per il web (Apro la pagina -> Ricevo i dati -> Chiudo la connessione).

Ma per l'IoT, l'HTTP è troppo "pesante" e lento. Se ho una serra intelligente, voglio che l'app mi avvisi _istantaneamente_ se la temperatura sale troppo, senza dover "ricaricare la pagina".

---

# MQTT: Il protocollo delle Cose

In ambito IoT si usa **MQTT**. È un protocollo leggerissimo basato su **Pub/Sub** (Pubblicazione e Iscrizione).

1.  L'ESP32 (Sensore) _pubblica_ la temperatura su un "canale" chiamato `serra/temperatura`.
2.  La nostra App Android (Service in background) _si iscrive_ a quel canale.
3.  Appena l'ESP32 invia un dato, l'app lo riceve in un millisecondo.

_(Questa è la base della Domotica Moderna: Alexa, Home Assistant)._

---

# Parte 4: Comunicare con l'Utente

## Broadcast e Notifiche

---

# Broadcast Receivers (Le orecchie dell'app)

Android lancia continuamente degli "urli" nel sistema: _"Batteria scesa al 15%!"_ _"Modalità Aereo attivata!"_ _"Schermo acceso!"_

Possiamo registrare un **Broadcast Receiver** nella nostra app per ascoltare questi urli e reagire (es. Salvare i dati al volo se la batteria sta morendo).

---

# Le Notifiche Push

Quando l'app è chiusa o in background, l'unico modo per parlare con l'utente è la **Notifica**.

L'anatomia di una notifica moderna:

- **Small Icon:** L'iconcina nella barra di stato (deve essere bianca su sfondo trasparente).
- **Titolo e Testo.**
- **Azioni Rapide:** Pulsanti direttamente nella tendina (es. "Rispondi", "Segna come letto").

---

# Notification Channels (L'Argine)

Da Android 8.0, Google ha introdotto i **Canali di Notifica**. Siete obbligati a classificare le vostre notifiche.

Esempio in un'app di E-commerce:

- Canale 1: **"Spedizioni"** (Priorità Alta, fa suonare il telefono).
- Canale 2: **"Sconti e Promo"** (Priorità Bassa, silenziosa).

Così l'utente può disattivare le notifiche di marketing _senza_ perdersi l'avviso che il suo pacco è arrivato!

---

# Dibattito: L'Economia dell'Attenzione

Molte app (Social Network, Giochi Free-to-play) inviano notifiche finte come: _"A Marco manca la tua fattoria, torna a giocare!"_

_Dal punto di vista di uno sviluppatore e di un utente: le notifiche push sono uno strumento di comunicazione vitale o una tecnica di manipolazione psicologica per tenerci incollati allo schermo? Quando è giusto inviare una notifica?_

---

# Mettiamoci alla prova!

## Kahoot Time 🕹️

L'ultimo quiz in presenza! Vediamo chi si ricorda come funzionano il GPS, i Permessi e il WorkManager!

---

# Kahoot Time!

<div align="center">

**PIN:** 696660

</div>

---

# Parte 5: Verso la Pratica

## L'Antifurto e i Laboratori

---

# Il Progetto di Oggi: L'Antifurto

Uniremo tutto ciò che abbiamo visto oggi in una mini-app **Antifurto**.

1.  **Sensore:** Leggeremo l'accelerometro. Se l'app rileva un movimento improvviso...
2.  **Servizio:** Faremo partire un suono di allarme a volume massimo che non si spegne chiudendo l'app (Foreground Service).
3.  **Notifica:** Mostreremo una notifica con il tasto "Disattiva Allarme".

---

# Roadmap Laboratori a Distanza

Dalla prossima lezione non ci vedremo in aula, ma a distanza. Saranno **7 Laboratori Pratici**. La formula sarà:

1.  Sviluppo Guidato (Faccio io, voi copiate e capite l'architettura).
2.  Sfida Autonoma (Aggiungete una feature aiutati da me e dall'IA).

---

# Breve Demo 💻

_(Condivido lo schermo: Mettiamo l'emulatore sul tavolo virtuale, lanciamo l'Antifurto e muoviamo il telefono... attenti alle orecchie!)_

---

![bg cover](../img/ultima_pagina.png)
