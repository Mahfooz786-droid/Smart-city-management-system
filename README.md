# Smart City Management System

A Maven multi-module Java project that delivers a Smart City Management System with **two independent front-ends** (a Java Swing desktop GUI and a Jakarta Servlet/JSP web app) backed by **one shared business layer** (smart-city-core).

By **Exorcisms** · B.Tech CSE · Galgotias University

The project demonstrates the full set of college-level CS fundamentals in one coherent codebase: OOP (inheritance + polymorphism + abstraction + interfaces + encapsulation), collections & generics, thread-safe concurrency with `AtomicInteger` + `ExecutorService`, JDBC with `PreparedStatement` + try-with-resources, BCrypt password hashing, CSRF-protected forms, session-based auth, JUnit 5 tests, and Maven multi-module packaging.

---

## 1. Project Overview & Objectives

The Smart City Management System is a centralised platform that lets **citizens** register complaints, browse city services and submit feedback, while **administrators** manage citizens, monitor complaints, update complaint status, and review citizen feedback.

The submission satisfies **both** college rubrics — the **Java GUI-Based rubric** (Swing + JDBC + OOP + collections + multithreading) **and** the **Java Web-Based rubric** (servlet design + JDBC + core Java + problem understanding) — from a single shared codebase. There is no duplication of business logic: the GUI and the Web modules both depend on `smart-city-core`, which owns the models, DAOs, services, security, and concurrency package.

---

## 2. Features

### Citizen-facing
- Register a complaint (subject + description) — status starts as **Pending**
- View only your own complaints
- Browse the city services catalog
- Submit a rating (1–5) + comment piece of feedback

### Admin-facing (admin login only)
- List all citizens
- Add / update / delete a citizen (deletion cascades to their complaints + feedback)
- View all complaints across all citizens (with citizen name denormalised)
- Update any complaint's status (Pending → In Progress → Resolved)
- View all feedback submitted by citizens

### Cross-cutting
- BCrypt-hashed passwords (cost 12)
- Session-based auth with session-fixation protection (`changeSessionId` on login)
- CSRF protection on every state-changing form
- Server-side input validation (email format, 10-digit phone, rating range 1–5, etc.)
- All JDBC ops use `PreparedStatement` + try-with-resources — no resource leaks, no SQL injection
- All long-running DB ops run off the Swing EDT via `SwingWorker`
- All long-running web ops would normally block a Tomcat thread; the design uses the service-layer abstractions so request handlers stay short

---

## 3. Architecture Diagram

```
                          ┌──────────────────────────────────────────┐
                          │             smart-city-core               │
                          │      (shared business layer, JAR)         │
                          ├──────────────────────────────────────────┤
                          │  model/   Person (abstract)               │
                          │           ├── Citizen                     │
                          │           └── Administrator              │
                          │  interfaces/  CitizenRepository           │
                          │                ComplaintRepository       │
                          │                ServiceRepository         │
                          │                FeedbackRepository        │
                          │  dao/      JdbcCitizenRepository         │
                          │            JdbcComplaintRepository        │
                          │            JdbcServiceRepository        │
                          │            JdbcFeedbackRepository       │
                          │            DatabaseInitializer           │
                          │  service/  CitizenService               │
                          │            ComplaintService             │
                          │            CityServiceService            │
                          │            FeedbackService               │
                          │            CitizenAuthService           │
                          │            AdminAuthService              │
                          │            ServiceFactory                │
                          │  exception/  SmartCityException          │
                          │              AuthenticationException     │
                          │              ValidationException         │
                          │              NotFoundException           │
                          │              AuthorizationException       │
                          │              DatabaseException            │
                          │  concurrency/ ComplaintProcessing       │
                          │                ComplaintProcessingExecutor│
                          │  security/  PasswordHasher (BCrypt)     │
                          │  config/    DatabaseConfig (env vars)   │
                          │  util/      SessionUser                  │
                          └────────────┬──────────────────────────────┘
                                       │ depends on
                ┌──────────────────────┴──────────────────────┐
                │                                              │
   ┌────────────▼────────────────┐              ┌──────────────▼───────────────┐
   │       smart-city-gui        │              │       smart-city-web         │
   │  (Java Swing desktop, JAR)  │              │  (Jakarta Servlet 6, WAR)    │
   ├─────────────────────────────┤              ├──────────────────────────────┤
   │ ui/  Main (launcher)        │              │ controller/  6 Servlets     │
   │       LoginFrame (admin)    │              │ filter/      AuthFilter     │
   │       CitizenLoginFrame     │              │              CSRFFilter     │
   │       DashboardFrame        │              │ util/        SessionUtil      │
   │       CitizenDashboardFrame │              │              CSRFUtil        │
   │       UITheme (design sys)  │              │              WebContext      │
   │       GuiContext (wiring)   │              │ webapp/WEB-INF/             │
   │                             │              │   jsp/ (7 JSPs with c:out)   │
   │                             │              │   web.xml (servlets+filters) │
   │                             │              │   css/style.css              │
   └─────────────────────────────┘              └──────────────────────────────┘
                              │
                              ▼
                          ┌────────┐
                          │ MySQL  │  (production)
                          └────────┘
                          ┌────────┐
                          │   H2   │  (test only, MySQL compat mode)
                          └────────┘
```

---

## 4. Technology Stack

| Layer              | Technology | Version |
|--------------------|------------|---------|
| JDK                | Java       | 21 (LTS) |
| Build              | Apache Maven | 3.9.x |
| Build output (GUI) | maven-shade-plugin | executable JAR |
| Build output (Web) | maven-war-plugin | WAR |
| Desktop UI         | Java Swing + FlatLaf | 3.5.2 |
| Web framework      | Jakarta Servlet | 6.1 |
| JSP                | Jakarta Server Pages | 3.1 |
| JSTL               | jakarta.servlet.jsp.jstl | 3.0.1 |
| Target runtime     | Apache Tomcat | 10.1+ |
| Database           | MySQL | 8.0+ |
| JDBC driver        | mysql-connector-j | 8.4.0 |
| Test DB            | H2 (in MySQL compatibility mode) | 2.3.232 |
| Password hashing   | BCrypt (at.favre.lib:bcrypt) | 0.10.2 |
| Tests              | JUnit 5 + AssertJ + Mockito | 5.11 / 3.26 / 5.14 |

---

## 5. Requirements & Prerequisites

- **JDK 21 or newer** — required to compile and run.
- **Apache Maven 3.8+** — required to build.
- **MySQL 8.0+** — required for production runtime. NOT required to run the tests (they use H2).
- **Apache Tomcat 10.1+** — required to deploy the WAR. Earlier Tomcat versions are incompatible because they target the older `javax.servlet` namespace, not `jakarta.servlet`.
- **FlatLaf** is bundled inside the GUI JAR — no separate install.

---

## 6. Database Creation & Initialization

```bash
# 1. Create the database (one-time)
mysql -u root -p -e "CREATE DATABASE smart_city CHARACTER SET utf8mb4;"

# 2. Apply the schema
mysql -u root -p smart_city < database/schema.sql

# 3. Apply the seed data
mysql -u root -p smart_city < database/seed.sql
```

The `seed.sql` file creates 5 demo citizens (with BCrypt-hashed passwords), 5 complaints, 4 feedback entries and 6 city services.

### Demo credentials

| Role   | Username / Email        | Password    |
|--------|-------------------------|-------------|
| Admin  | `admin`                 | `admin123`  |
| Citizen | `aarav@example.com`   | `password1` |
| Citizen | `priya@example.com`   | `password2` |
| Citizen | `rahul@example.com`   | `password3` |
| Citizen | `anjali@example.com`   | `password4` |
| Citizen | `vikram@example.com`   | `password5` |

---

## 7. Environment Variable Configuration

The `DatabaseConfig` class reads from environment variables. **No credentials are hardcoded** (the original `Mahfooz@786` MySQL password in the legacy `DatabaseConnection.java` must be rotated — see "Known Limitations").

| Variable      | Required? | Example |
|---------------|-----------|---------|
| `DB_URL`      | recommended (falls back to `jdbc:mysql://localhost:3306/smart_city?...`) | `jdbc:mysql://db.example.com:3306/smart_city` |
| `DB_USER`     | recommended (falls back to `root`) | `smartcity_app` |
| `DB_PASSWORD`  | recommended (falls back to empty) | a strong secret |

For Tomcat, set these in `$CATALINA_BASE/bin/setenv.sh`:

```bash
export DB_URL="jdbc:mysql://localhost:3306/smart_city?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true"
export DB_USER="smartcity_app"
export DB_PASSWORD="your-strong-password-here"
```

For the GUI, set them in the shell before launching the JAR:

```bash
export DB_URL="..."
export DB_USER="..."
export DB_PASSWORD="..."
java -jar smart-city-gui.jar
```

---

## 8. Maven Build & Test Commands

From the project root:

```bash
# Build all 4 modules + run all tests
mvn clean test

# Build the production artifacts without re-running tests
mvn package -DskipTests

# Build artifacts AND run tests (default)
mvn clean package

# Build a single module
mvn -pl smart-city-gui -am package

# Run only one test class
mvn -pl smart-city-core test -Dtest=ComplaintProcessingConcurrencyTest
```

**Verified build status (this submission):**
- `mvn clean test` → BUILD SUCCESS, 53 tests, 0 failures, 0 errors, 0 skipped
- `mvn package` → BUILD SUCCESS, 4 artifacts produced:
  - `smart-city-core-1.0.0.jar` (50 KB)
  - `smart-city-gui.jar` (5.4 MB, executable shaded JAR)
  - `smart-city-web.war` (7.5 MB, deployable to Tomcat 10.1+)
  - `original-smart-city-gui.jar` (intermediate, ignore)

---

## 9. Launching the Java GUI

After `mvn package`:

```bash
java -jar smart-city-gui/target/smart-city-gui.jar
```

Or, from your IDE, run `com.smartcity.ui.Main` (the `smart-city-gui` module).

The launcher window shows two tiles — click **Administrator** or **Citizen** to reach the appropriate login screen. Long-running DB ops run on `SwingWorker` background threads so the UI never freezes.

---

## 10. Deploying the Java Web Application to Tomcat

```bash
# 1. Install Tomcat 10.1+
#    e.g. on Linux: tar -xzf apache-tomcat-10.1.x.tar.gz

# 2. Set the DB env vars on the Tomcat JVM (see section 7)

# 3. Copy the WAR
cp smart-city-web/target/smart-city-web.war $CATALINA_BASE/webapps/

# 4. Start Tomcat
$CATALINA_BASE/bin/startup.sh

# 5. Open the app
xdg-open http://localhost:8080/smart-city-web/login
```

Tomcat auto-deploys the WAR on startup. The application's `welcome-file` is `login`, so visiting `/smart-city-web/` will redirect to the login page.

---

## 11. Example Routes

### Public
| Method | Path                  | Behaviour |
|--------|-----------------------|-----------|
| GET    | `/login`              | Renders the login form (issues a CSRF token) |
| POST   | `/login`              | Authenticates admin OR citizen, redirects to /citizens or /complaints |
| POST   | `/logout`             | Invalidates the session, redirects to /login |

### Authenticated (any role)
| Method | Path                  | Behaviour |
|--------|-----------------------|-----------|
| GET    | `/complaints`         | Admin: all complaints. Citizen: only their own |
| POST   | `/complaints`         | Citizen-only: register a new complaint (citizenId from session, never the form) |
| GET    | `/services`           | Lists all city services |
| GET    | `/feedback`           | Admin: list all feedback. Citizen: render the submission form |
| POST   | `/feedback`           | Citizen-only: submit a new piece of feedback |

### Admin-only
| Method | Path                          | Behaviour |
|--------|-------------------------------|-----------|
| GET    | `/citizens`                   | Lists all citizens |
| POST   | `/citizens`                   | Adds a new citizen |
| POST   | `/complaints/status`          | Updates a complaint's status (Pending/In Progress/Resolved) |

---

## 12. Screenshots

Live screenshots of the Java Swing GUI are in the `download/shots/` folder (rendered via Xvfb in a headless build environment). The web module can be visually inspected by deploying the WAR to a local Tomcat as described in section 10.

---

## 13. Troubleshooting

| Symptom | Fix |
|---------|-----|
| `mvn clean test` fails to download dependencies | Check your `~/.m2/settings.xml` mirrors; ensure outbound HTTPS to `repo.maven.apache.org` is allowed. |
| Tests fail with `No suitable driver found for jdbc:h2:...` | The H2 jar is bundled under `smart-city-core` test scope. Make sure you're building from the root POM (`mvn -pl smart-city-core -am test`). |
| GUI launches but `Database Connection Failed!` | Set `DB_URL` / `DB_USER` / `DB_PASSWORD` env vars (section 7) and make sure MySQL is running. |
| WAR deploys but every page shows 403 | You hit the CSRF filter without a token. Always navigate via the rendered forms (the JSPs embed the CSRF token in a hidden field). |
| Tomcat throws `java.lang.NoClassDefFoundError: jakarta/servlet/...` | You're on Tomcat 9 or older. Upgrade to Tomcat 10.1+. |
| `mvn package` produces `original-smart-city-gui.jar` | That's the unshaded intermediate JAR. Use `smart-city-gui.jar` (the shaded, executable one). |

---

## 14. Known Limitations

1. **Admin credentials are hardcoded in `AdminAuthService.java`.** This preserves backward compatibility with the original project's `LoginFrame` (admin / admin123). The hash is BCrypt, not plaintext, but the credentials are still in source code. The `admins` table in `schema.sql` is provided as a placeholder; migrating admin accounts to the database requires implementing a `JdbcAdminRepository` and swapping `AdminAuthService`'s internals. The migration point is documented in `AdminAuthService.java` itself.
2. **The original `Smart-city-management-system` project (preserved at the repo root for reference) contains a hardcoded MySQL password (`Mahfooz@786`) in `src/com/smartcity/dao/DatabaseConnection.java`.** This credential is exposed in Git history. **You MUST rotate this MySQL password immediately.** The new Maven project does NOT contain this password — it reads from env vars.
3. **No HTTPS enforcement in the web module.** The `web.xml` sets `<secure>false</secure>` on the session cookie. Set this to `true` once Tomcat is fronted by HTTPS (or use Tomcat's `RemoteIpValve` + a reverse proxy).
4. **No pagination on admin list views.** The current `/citizens` and `/feedback` JSPs render every row in one page. For datasets beyond a few thousand rows this would need pagination + server-side sorting.
5. **No password reset flow.** Citizens cannot currently reset their own password. The `CitizenService.changePassword` method exists, but there is no UI for it yet.
6. **No email verification on registration.** Citizens can register with any syntactically-valid email; there's no verification step.
7. **CSRF tokens are per-session, not per-form.** A single CSRF token is issued per session and reused across all forms in that session. This is the OWASP-recommended approach but means a stolen session can still submit forms.
8. **The web module's tests use Mockito mocks** for the Servlet API. End-to-end testing with a real embedded Tomcat (e.g. `tomcat-embed-core`) would catch more integration bugs but adds dependency weight.
9. **The GUI module has no automated tests** — Swing UI testing requires either FEST-Swing or UiTestFramework, which would significantly increase the build complexity. All GUI business logic is exercised via the shared core's tests.

---

## Rubric Compliance

See [RUBRIC.md](RUBRIC.md) for the full mapping of every rubric requirement to source files, methods, and test evidence.

---

## Final Folder Structure

```
smart-city-management-system/         (preserved original Java project, for reference)
smart-city/                            (NEW Maven multi-module project)
├── pom.xml                            (root parent POM, dependencyManagement)
├── README.md                          (this file)
├── RUBRIC.md                          (rubric compliance table)
├── .gitignore
├── database/
│   ├── schema.sql                     (FKs, constraints, indexes)
│   └── seed.sql                       (BCrypt-hashed demo passwords)
├── smart-city-core/                   (shared business layer, JAR)
│   ├── pom.xml
│   └── src/
│       ├── main/java/com/smartcity/
│       │   ├── model/      Person, Citizen, Administrator, Complaint, CityService, Feedback, UserOperations
│       │   ├── interfaces/ 4 repository contracts
│       │   ├── dao/         4 JDBC repositories + DatabaseInitializer
│       │   ├── service/     6 service classes + Validator
│       │   ├── exception/   6 custom exceptions
│       │   ├── concurrency/ ComplaintProcessing, ComplaintProcessingExecutor
│       │   ├── security/    PasswordHasher
│       │   ├── config/      DatabaseConfig (env vars)
│       │   └── util/         SessionUser
│       ├── main/resources/com/smartcity/schema.sql
│       └── test/
│           ├── java/com/smartcity/   (9 JUnit test classes, 46 tests)
│           └── resources/com/smartcity/seed.sql
├── smart-city-gui/                    (Java Swing desktop, executable JAR)
│   ├── pom.xml
│   └── src/main/java/com/smartcity/ui/ (Main + 5 frames + UITheme + GuiContext)
└── smart-city-web/                    (Jakarta Servlet 6 + JSP, WAR)
    ├── pom.xml
    └── src/
        ├── main/java/com/smartcity/web/
        │   ├── controller/ 6 Servlets
        │   ├── filter/      AuthFilter, CSRFFilter
        │   └── util/         SessionUtil, CSRFUtil, WebContext
        ├── main/webapp/
        │   ├── WEB-INF/web.xml
        │   ├── WEB-INF/jsp/ 7 JSPs (login, citizens, complaints, services, feedback-form, feedback-list, error)
        │   └── css/style.css
        └── test/java/com/smartcity/web/  (2 test classes, 7 tests)
```
