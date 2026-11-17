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

## 2026-11-15 — คนที่ 1 แก้การเข้ารหัสซอร์ส Java

- **เครื่องมือ** OpenAI Codex
- **งานที่ขอให้ช่วย** ทำให้ Java คอมไพล์ด้วย US-ASCII โดยคงไอคอนฤดูและกฎเกมเดิม
- **งานที่ใช้จาก AI** Codex ลบคอมเมนต์ภาษาไทยบรรทัดแรกใน Java 20 ไฟล์ใน `LittleFarm/src/` และเปลี่ยนไอคอนฤดูทั้งสามเป็น Unicode escape ดู PR #29 และ commit `7d54e0b`
- **ผลตรวจ** Codex ตรวจซอร์สครบ 45 ไฟล์ คอมไพล์ด้วย Java 11 แบบ US-ASCII และตรวจค่าไอคอนขณะรันครบสามฤดู เทสต์ `SaveErrorTest` ผ่าน 17 กรณี และผล `GoldenSaveTest` ตรงกับไฟล์อ้างอิง 70 บรรทัด

## 2026-11-15 — คนที่ 1 เพิ่มตัวนับความเสียหายสะสมจากพายุ

- **เครื่องมือ** OpenAI Codex
- **งานที่ขอให้ช่วย** เพิ่มยอดสะสมของพืชที่ถูกพายุทำลาย และให้ระบบโหลดเซฟคืนยอดสะสมเดิมได้
- **งานที่ใช้จาก AI** Codex เพิ่มตัวนับสะสมและ getter `getCropsLostToStorms()` ใน `LittleFarm/src/Farm.java` สำหรับ setter ผมส่งตัวอย่างโค้ด `setCropsLostToStorms(int total)` พร้อมเงื่อนไขให้โยน `IllegalArgumentException` เมื่อค่าติดลบ Codex นำโค้ดไปใส่และทดสอบ ดู PR #32 และ commit `bc6b592` กับ `1584b5e`
- **ขอบเขตที่ทีมแบ่งกัน** ผมให้แก้เฉพาะ `Farm.java` ใน PR นี้ คนที่ 2 รับส่วนอ่านและเขียน `stormlosses` ใน `SaveGame.java` ต่อใน PR #33
- **ผลตรวจ** Codex ทดสอบพายุหลายครั้ง การรีเซ็ตยอดรายวัน การรอดของพืชที่โตเต็มที่ และการนับต่อจากยอดที่คืนค่าผ่าน setter ค่าเป็นลบถูกปฏิเสธ การคอมไพล์ Java 11 แบบ US-ASCII และเทสต์เซฟเดิมผ่าน

## 2026-11-15 ถึง 2026-11-17 — คนที่ 1 ตรวจงานรวมและ Class Diagram

- **เครื่องมือ** OpenAI Codex
- **งานที่ขอให้ช่วย** เทียบ Class Diagram ใน Lucidchart กับโค้ดบน `dev` ตรวจสถานะ PR และระบุงานที่ทีมยังต้องทำ
- **งานที่ใช้จาก AI** Codex อ่านแผนภาพและโค้ด รันเทสต์จาก `dev` และตรวจโค้ดเลือกโหมดหลัง PR #38 พบว่าเกมมีเมนูเลือกโหมดแล้ว แต่ `Main` ยังไม่อ่านชื่อโหมดจากคำสั่งตอนเปิดเกม
- **ผลตรวจ** บน `dev` ที่ `59975be` คอมไพล์ Java 11 แบบ UTF-8 ผ่าน และเทสต์เซฟกับ Strategy ผ่านรวม 37 กรณี การคอมไพล์แบบ US-ASCII พบคอมเมนต์ไทยใน Java 6 ไฟล์ ส่วน golden output มีบรรทัด `weatherMode normal` เพิ่มจากไฟล์อ้างอิงเดิม
