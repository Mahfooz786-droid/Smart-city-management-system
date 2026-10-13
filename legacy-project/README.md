# Legacy Project (Original Smart City Management System)

This folder contains the **original** Smart City Management System that the new Maven multi-module project (at the repo root) refactored and upgraded. It is preserved here for educational reference — so faculty can compare the original implementation against the refactored version.

## What's here

```
legacy-project/
├── README-legacy.md              Original project README
├── smart city management systenm.iml  IntelliJ IDEA module file
├── lib/
│   ├── flatlaf.jar                FlatLaf 3.5.2 (modern Swing LAF)
│   └── mysql-connector-j.jar     MySQL Connector/J 8.4.0 (JDBC driver)
└── src/com/smartcity/
    ├── Main.java                  Entry point
    ├── ui/                        Swing UI screens (LoginFrame, DashboardFrame, etc.)
    ├── model/                     Domain classes (Person, Citizen, OOPDemo, etc.)
    ├── dao/                       DatabaseConnection (env-var config in the new project)
    └── database/                  Legacy DAOs (CitizenDAO, ComplaintDAO, etc.)
```

## Security notice

The original `DatabaseConnection.java` had a hardcoded MySQL password (`Mahfooz@786`). **It has been scrubbed** from this preserved copy — the password constant now reads `REPLACE_WITH_YOUR_OWN_DB_PASSWORD`. If you want to run the legacy project, set your own MySQL password in this file before launching.

⚠️ The original password (`Mahfooz@786`) was committed to the original repo's Git history before this scrub. **Rotate it immediately** if you ever used it on a real MySQL server.

## How to run the legacy project (no Maven required)

1. Open this `legacy-project/` folder in IntelliJ IDEA
2. **File → Project Structure → Libraries → New** → add `lib/flatlaf.jar` and `lib/mysql-connector-j.jar`
3. Set up your MySQL database (run `../database/schema.sql` and `../database/seed.sql`)
4. Edit `src/com/smartcity/dao/DatabaseConnection.java` to use your MySQL credentials
5. Right-click `src/com/smartcity/Main.java` → Run

## Why keep the legacy project?

The legacy project demonstrates the original implementation against which the refactored Maven project (in the parent folder) was designed. Comparing the two side-by-side shows:

- The old hardcoded `DatabaseConnection` → the new `DatabaseConfig` (env-var based)
- The old static DAOs returning `List<String>` → the new repository interfaces returning typed `List<Citizen>`, `List<Complaint>`, etc.
- The old `synchronized` instance method guarding a `static int` counter (a classic concurrency bug) → the new `AtomicInteger`-based `ComplaintProcessing`
- The old plaintext-password auth (`SELECT ... WHERE email=? AND password=?`) → the new BCrypt-based `CitizenAuthService.login`

For full details of the refactor, see the parent folder's `README.md` and `RUBRIC.md`.
