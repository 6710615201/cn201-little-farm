# Little Farm — CN201

Private three-person Java/Swing course project. Planned extension: seasons.
This setup does not implement the seasons feature or change the supplied Java code.

## Requirements
- JDK 11 or newer (not just a JRE), with `java` and `javac` on PATH.
- A desktop environment for the Swing game.
- No Maven, Gradle, Docker, server, database, or external Java libraries are required.

## Build and run
Run these commands from the repository root in macOS Terminal, Linux, or Windows Git Bash:

```sh
cd LittleFarm
mkdir -p out
javac --release 11 -encoding UTF-8 -d out src/*.java
java -cp out Main
```

In Windows PowerShell, use `New-Item -ItemType Directory -Force out` instead of `mkdir -p out`; the Java commands are the same.

The committed source is in **LittleFarm/src/**, not src/ at repository root.
Always launch from LittleFarm so the game's relative save path is consistent.
The game writes `littlefarm_save.txt` in its working directory. Personal saves and compiled classes are ignored by Git.

## Team Git workflow
1. Clone this private repository after the owner grants your GitHub account access.
2. Before starting work: `git switch main`, then `git pull --ff-only origin main`.
3. Create a task branch: `git switch -c feature/your-task`.
4. Keep commits small. Review `git diff` and stage only intended files.
5. Push with `git push -u origin HEAD` and open a pull request targeting main.
6. Another teammate reviews the diff and checks compilation before merging. Do not force-push main.

Suggested responsibilities, to confirm as a team:
- Core seasons/game rules: `feature/seasons-core`.
- Save/load and exceptions: `feature/season-save`.
- UI and startup strategy selection: `feature/season-ui-strategy`.

Coordinate shared classes before editing them. Capture golden output from the original behavior **before** refactoring. Keep refactoring commits separate from feature commits. A branch name alone does not implement a feature or give a teammate access.

## Automated check
`.github/workflows/java-compile.yml` compiles the unchanged project with JDK 11 and 21 on pushes and pull requests. It does not launch Swing or verify game behavior. Add meaningful tests separately; a green compile check does not mean the assignment is complete.

## Before submission
Consult the teacher's assignment as the source of truth. Still required as the team develops:
- New system with meaningful abstraction and implementations.
- Strategy/Factory and startup selection, new-state save/load, required exceptions.
- Golden-output evidence and separately committed refactoring.
- `design.pdf` (maximum 4 pages), `AI-log.md`, and `data/sample_save.txt` in the final submission structure.
- Each member's own code, commits, and understanding; manual game checks and demo.

Do not commit personal credentials, personal save files, or generated class files.
Keep the repository private and do not share course source with other groups.
