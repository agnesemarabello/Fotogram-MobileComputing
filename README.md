# Fotogram 📸

**Fotogram** è un prototipo di client per un social network basato sulla condivisione di immagini. Il progetto è stato sviluppato come parte del corso di **Mobile Computing** (2025/2026), mettendo in pratica i principi della programmazione reattiva, della gestione delle reti e della persistenza dei dati.

## 🚀 Funzionalità Principali

* **Autenticazione & Setup:** Registrazione automatica dell'utente e configurazione del profilo (Username, Bio, Immagine del profilo).
* **Feed dinamico:** Visualizzazione di post in ordine cronologico con caricamento paginato.
* **Social Interactions:** Possibilità di seguire/smettere di seguire (Follow/Unfollow) altri utenti.
* **Creazione Post:** Selezione di foto dalla galleria con possibilità di aggiungere descrizioni e posizione geografica.
* **Geolocalizzazione:** Integrazione con mappe per visualizzare dove è stata scattata una foto.
* **Profilo Utente:** Gestione dei propri dati, visualizzazione del numero di followers/following e griglia personale dei post.

## 🛠 Swagger UI
**Swagger UI** utilizzato per le chiamate API del server fornito dal professore: `https://develop.ewlab.di.unimi.it/mc/2526/#/` 


## 📦 Architettura del Progetto

L'app segue i principi di "Clean Architecture" per garantire manutenibilità e testabilità:

1.  **UI Layer (Screens):** Composable function che reagiscono allo stato (es. `FeedScreen`, `ProfileScreen`).
2.  **ViewModel Layer:** Gestione della logica di business e dello stato tramite `StateFlow`.
3.  **Data Layer:** * `RequestManager`: Gestisce la comunicazione diretta con il server EWLab.
    * `PostRepository`: Implementa un sistema di cache per ottimizzare il caricamento dei post.
    * `DataStoreManager`: Gestisce la persistenza delle credenziali di sessione.

## 🔧 Installazione e Configurazione

1. Clonare il repository:
   ```bash
   git clone [https://github.com/agnesemarabello/Fotogram-MobileComputing.git](https://github.com/agnesemarabello/Fotogram-MobileComputing.git)
