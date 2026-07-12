# oegen-java
Kotlin genealogy assistant (Œgen).

A simple and straightforward genealogy tool:
- Persons with only one firstname/lastname, one parent of each gender. 
- Fuzzy dates for birth, baptism, marriage, death and funeral (text, partial, precise, boolean composition). 
- Simple places (no hierarchy) for each of these events (planned). 
- Loading and saving to XML, JSON, or YAML (Jackson; JSON Schema validation on import).
- Swing interface with tree and detail view. 
- Basic reports printing (tree, detail...)

## Build

Requires Java 25 (Gradle 9.6.1):

```bash
./gradlew build
./gradlew run              # Compose Desktop UI (default)
./gradlew run --args="--swing"   # legacy Swing UI
./gradlew test
```

## Fuzzy dates

Life events on [Person](app/src/main/kotlin/fr/cdbs/oegen/data/Person.kt) use a sealed [FuzzyDate](app/src/main/kotlin/fr/cdbs/oegen/data/date/FuzzyDate.kt) model:

| Kind | Role |
|------|------|
| `Text` | Free-form label when the date is not structured |
| `Partial` | Year/month/day strings with `?` for missing digits (e.g. `18??`) |
| `Precise` | Known calendar values with precision: exact, before, after, circa |
| `And` / `Or` / `Not` | Boolean composition over other fuzzy dates |

Calendars are selected with [CalendarSystem](app/src/main/kotlin/fr/cdbs/oegen/data/date/CalendarSystem.kt) (`GREGORIAN` by default; optional `[JULIAN]` suffix in the minimal parser). Serialization uses Jackson polymorphic `kind` properties in XML/JSON/YAML export.

Minimal parse/format helpers: [FuzzyDateParser](app/src/main/kotlin/fr/cdbs/oegen/data/date/FuzzyDateParser.kt), [FuzzyDateDisplay](app/src/main/kotlin/fr/cdbs/oegen/data/date/FuzzyDateDisplay.kt) (EN/FR via `messages*.properties`).

Person fields: `birthDate`, `baptismDate`, `marriageDate`, `deathDate`, `funeralDate` (backed by [PersonLifeEvents](app/src/main/kotlin/fr/cdbs/oegen/data/PersonLifeEvents.kt)).

## UI

The default launcher opens a **Compose Desktop** window (Material 3, three-pane layout: list | relations | detail). The legacy Swing UI remains available with `--swing`.

On startup, the Compose UI **reopens the last saved database** (path stored in Java Preferences and `~/.oegen/last-database.path`). Changes to a file that already has a save location are **auto-saved** after about 1.5 seconds of inactivity and again when closing the window. A new database without **Save As** is not auto-saved.

## Roadmap

| Done | Next |
|------|------|
| Fuzzy life-event dates in model/XML/JSON/YAML | Places for life events |
| Gradle 9.6.1 / Java 25 | GEDCOM import/export |
| Compose Desktop shell (list, relations, detail) | Genealogy tree `Canvas` (port of `PersonTree`) |
| Person search | Tree wheel view |
| XML/JSON/YAML round-trip + schema tests | Flatpak or `.deb` packaging |
| Jackson XML/JSON/YAML I/O | Dark theme toggle |


## Database formats

| Format | Extension | Notes |
|--------|-----------|-------|
| XML | `.xml` | Legacy Oegen XML (Jackson XML, same structure as before) |
| JSON | `.json` | Canonical interchange format; validated against `oegen-schema.json` on import |
| YAML | `.yaml`, `.yml` | Same document model as JSON; validated on import |

The JSON Schema lives at `app/src/main/resources/oegen-schema.json`. XML imports are validated by mapping to the shared document model and checking against the same schema.

## Database persistence

Primary storage is a relational database (default **SQLite** file). XML remains the interchange format (File → Open imports into the DB; Save persists to the DB; Save As exports XML).

### Default SQLite path

`~/.oegen/oegen.db`

Override with:

- Environment: `OEGEN_DB_PATH=/path/to/oegen.db`
- JVM property: `-Doegen.db.path=/path/to/oegen.db`

On startup the app runs JDBC migrations from `app/src/main/resources/db/migration/` and loads persons into the in-memory model used by the UI.

### PostgreSQL (optional)

Set a JDBC URL and credentials:

```bash
export OEGEN_DB_URL='jdbc:postgresql://localhost:5432/oegen'
export OEGEN_DB_USER=oegen
export OEGEN_DB_PASSWORD=secret
./gradlew run
```

Equivalent system properties: `oegen.db.url`, plus `OEGEN_DB_USER` / `OEGEN_DB_PASSWORD` (or `-Doegen.db.url=...`).

Create the database first (`createdb oegen`). Schema is applied automatically on first run.

### Migrating from files-only

1. Start the app (creates `~/.oegen/oegen.db` if missing).
2. Use **File → Open** on an existing `.xml` genealogy file — data is imported into memory and written to the database.
3. Use **File → Save As** when you need a portable XML copy; **Save** updates the database only.

### Schema (version 1)

| Column | Purpose |
|--------|---------|
| `persons.id` | Stable person id (same as in XML) |
| `first_name`, `last_name`, `gender` | Core fields |
| `father_id`, `mother_id` | Parent links |
| `extra_json` | Reserved for life events / fuzzy dates (future) |

### Stack

Plain **JDBC** (`sqlite-jdbc`, PostgreSQL driver) with a small repository layer — no ORM.
