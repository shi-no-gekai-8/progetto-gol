---
marp: true
theme: default
paginate: true
header: "![w:100px](./img/logo_azienda.png)"
style: |
  header {
    top: 20px;
    left: 30px;
    position: absolute;
  }
  section {
    background-color: #f4f6f9; /* Sfondo leggermente grigio/bluastro per maggiore contrasto */
    font-size: 26px; /* Font globale ridotto per evitare che il testo finisca fuori pagina */
  }
  h1 {
    font-size: 1.8em;
  }
---

<div align="center">

![w:180px](./img/campania.png)

<br>

# Progetto GOL

## Competenze Digitali

### Corso di App Mobile

<br>

**dott. Alfonso Zappia**
_9 Aprile 2026_

</div>

---

# Il Mercato Mobile Oggi

## Apple vs Android

![bg right:50% w:400px](./img/ios_vs_android_market.png)

Prima di scrivere una riga di codice, dobbiamo capire **dove** stiamo sviluppando.

- **Market Share:** Chi ha più utenti nel mondo?
- **Revenue:** Chi genera più profitti per gli sviluppatori?
- **Filosofia:** Sistema chiuso (Walled Garden) vs Ecosistema aperto.

---

# Dibattito: Scegli il tuo lato

Se un'azienda vi desse **100.000€** per sviluppare la prossima app virale, su quale piattaforma la lancereste per prima? Perché?

<br>

<div align="center">

![w:200px](./img/apple.svg) &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ![w:200px](./img/android_logo.png)

</div>

---

# L'ecosistema Android (Il Passato)

## Java + XML

![bg left:40% w:300px](./img/vecchia_app_android.png) Fino a pochi anni fa, sviluppare per Android significava:

- Scrivere la logica in **Java** (verboso e pesante).
- Disegnare l'interfaccia in file **XML** statici.
- Usare `findViewById` per collegare i due mondi.

**Risultato:** Crash continui per _NullPointerException_ e interfacce difficili da mantenere.

---

# L'ecosistema Android (Il 2026)

## Il passaggio a Kotlin

Oggi l'industria è passata a **Kotlin**.
Perché Google lo ha reso il linguaggio ufficiale?

1. **Sintassi Concisa:** Fai le stesse cose con il 50% del codice in meno.
2. **Null Safety:** Il compilatore ti impedisce di creare app che crashano per variabili vuote.
3. **Interoperabile:** Può leggere il vecchio codice Java.

---

# Kotlin: Un assaggio di sintassi

Guardate la differenza nella gestione di una variabile che potrebbe non esistere:

**Vecchio Java:**

```java
String nome = null;
if (nome != null) {
    System.out.println(nome.length());
}
```

---

# Kotlin Moderno:

```kotlin
var nome: String? = null
println(nome?.length) // Nessun crash, stampa solo "null"!
```

---

# Come si disegnano le App oggi?

## Addio XML, benvenuto Jetpack Compose

Oggi non si disegna più l'interfaccia, la si dichiara tramite codice.

L'interfaccia utente è semplicemente una funzione dello stato dell'app.

Se i dati cambiano → L'interfaccia si aggiorna da sola.

---

# I Componenti Visivi Base

In Jetpack Compose tutto è un `@Composable`.

I tre mattoncini fondamentali:

- 🧱 **Column:** elementi uno sotto l'altro
- 🧱 **Row:** elementi affiancati
- 📦 **Box:** elementi sovrapposti

---

# Esempio di Layout

```kotlin
@Composable
fun  ProfiloUtente() {
  Row { // Immagine a sinistra, testi a destra
  Image(fotoProfilo)
  Column { // Nome sopra, bio sotto
  Text("Alfonso Zappia")
  Text("Sviluppatore Mobile")
 }
 }
}
```

---

# Mettiamoci alla prova!

## Kahoot Time 🕹️

Inquadra il QR Code con il tuo smartphone.
Chi vince il quiz sceglie il tema dell'app del prossimo laboratorio!

<div align="center">

![w:300px](./img/qrcode_kahoot.png) **PIN:** 123 456 7

</div>

---

# Roadmap del Corso

## Dalla teoria alla pratica

Il pomeriggio non faremo teoria. **Costruiremo app.**
Lavoreremo a piccoli progetti di difficoltà crescente:

1. 🧮 L'App Calcolatrice (Logica base)
2. 📖 La Rubrica (Gestione liste)
3. ☁️ Cinema App (Chiamate API a internet)
4. 💾 Il Diario (Salvataggio dati fisici)

---

# Il vostro ambiente di lavoro

## Setup per il pomeriggio

Niente installazioni pesanti sui PC della scuola o a casa. Lavoreremo in **Cloud**.

**I passi di oggi:**

1. Iscrizione al **Google Developer Program** (Gratuito).
2. Accesso a **Firebase Studio / Project IDX**.
3. Creazione del nostro primo _Workspace_ Android.

---

# Demo Time 💻

<br><br><br>

<div align="center">

_(Passaggio a Firebase Studio per mostrare l'emulatore live nel browser)_

</div>
