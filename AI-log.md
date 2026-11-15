# AI assistance log

This is an initial log entry, not a claim that the assignment is complete.
Team members must review the changes and keep adding their own AI-use records.

## 2026-11-12 — Basic repository infrastructure
- Tool: ChatGPT.
- Request: Prepare basic GitHub infrastructure while preserving the supplied Little Farm source.
- Assistance: Wrote README build/run and Git workflow instructions, a root .gitignore, and a GitHub Actions Java compilation workflow.
- Scope: Documentation and development setup only. No Java game source or functionality changed.
- Verification: Original source compiled locally with Temurin JDK 21 using javac --release 11. Remote workflow results should be reviewed in the pull request. Compilation alone does not test gameplay.
- Human review: Pending. Team should verify each setup file and understand the commands before merging.
- Not performed: Seasons implementation, golden-output tests, refactoring, design.pdf, or final submission.

## Further entries
For each later use, record the date, tool/model if known, prompt or request, affected files, what was accepted or rejected, verification performed, and the member who reviewed it. Do not invent test results or human review.

## 2026-11-12 to 2026-11-14 - Member 2: planning and seasons design
- Tool: Claude (Anthropic), chat assistant. Exact model not recorded.
- Request: Plan my part (save/load, exceptions, refactoring); design a class diagram for the seasons system; write user stories.
- Assistance: Proposed a work plan, a class diagram and 23 user stories written as GitHub issues text.
- Scope: Documents and discussion only. No Java source changed by this entry.
- Accepted / rejected: The AI first proposed 4 seasons, then corrected itself because the game lasts 30 days; we use 3 (Hot, Rainy, Cool). 
- Verification: None needed for documents. Class names were later compared with the teammate's implementation.
- Human review:Pending

## 2026-11-14 - Member 2: golden-output test and SaveGame refactoring (1.4)
- Tool: Claude (Anthropic), chat assistant.
- Request: Write a golden-output test for SaveGame and refactor SaveGame.loadInto.
- Assistance: Wrote test/GoldenSaveTest.java and the Extract Method refactoring of loadInto (smell: Long Method / Switch Statements).
- Affected files: test/GoldenSaveTest.java, golden/before.txt, golden/after.txt, src/SaveGame.java. Commits 6a1b2db (test) and b98c063 (refactor), pull request #26.
- Accepted / rejected: The first test file had a Thai comment. It failed to compile on my Windows machine (unmappable character for encoding windows-874), so it was replaced with an ASCII-only file. PowerShell ">" writes UTF-16, so the output is written with cmd /c instead.
- Verification: I ran the test before and after the change on my machine. git diff --no-index on the two outputs was empty. javac -d out src\*.java compiled.
- Human review:Pending

## 2026-11-14 - Member 2: save exception family (1.2)
- Tool: Claude (Anthropic), chat assistant.
- Request: Add exceptions that extend FarmException and make damaged saves report the line number.
- Assistance: Wrote SaveFormatException, UnknownCropException, WrongSeasonException, changes in SaveGame, and test/SaveErrorTest.java (17 cases).
- Affected files: src/SaveFormatException.java, src/UnknownCropException.java, src/WrongSeasonException.java, src/SaveGame.java, src/FarmScreen.java (one catch line), test/SaveErrorTest.java, test/GoldenSaveTest.java (throws clause only), golden/after_exceptions.txt. Commit adc6649, pull request #27.
- Accepted / rejected: The AI suggested making CropFactory.create throw UnknownCropException. Not used, because it is called from 5 places in other members' files; SaveGame checks the crop name instead. The AI noticed by reading the code that a save line such as "basket Cabbage 3" would be loaded and fail later in CropFactory.create; I did not reproduce that crash in the running game.
- Verification: SaveErrorTest printed ALL PASSED. I played the game, changed the gold line of a real save to "gold abc", and the game showed "line 4 is damaged: gold abc" and started a new farm without crashing.
- Human review:Pending

## 2026-11-15 - Member 2: review of a teammate's branch
- Tool: Claude (Anthropic), chat assistant.
- Request: Read the diff of feature/seasons-core and say what it means for the save/load work.
- Assistance: Read the diff, simulated the merge, and pointed out (1) Thai characters in 23 lines of src/*.java that break javac with Thai or ASCII encodings and (2) a duplicate WrongSeasonException.
- Scope: Analysis only. I did not edit my teammate's files; I sent them a message asking them to fix their own files.
- Verification: On dev, Select-String found 23 lines with non-ASCII characters and javac -encoding US-ASCII reported 100 errors (the javac limit).
- Human review:Pending
