# 🖥️ Java Selenium — Remove Modals Automation

This repository provides a **Java Selenium WebDriver** automation framework that demonstrates:

- 🧭 Automating browser interactions
- ❌ Dynamically removing modal pop-ups
- 🔍 Executing test scenarios on the Harvard Business Review (HBR) website
- 💾 Managing browser storage and session data programmatically

The project is modular, using **JUnit 5** for test execution and **Maven** for dependency management.

---

## 📂 Project Structure

```
.
├── src
│   └── test
│       └── java
│           └── com
│               └── example
│                   └── app
│                       ├── GenUtils.java
│                       ├── HbrSearchFunctions.java
│                       ├── HbrPageActions.java
│                       ├── BrowserStorage.java
│                       └── MainTestRunner.java
└── pom.xml
```

---

## 🧩 Class-by-Class Overview

### 1️⃣ `GenUtils.java`

General-purpose utility methods:
- `extractNumberFromText` — Extracts whole numbers from the text for calculations.
- `normalizeDashes` — Deals with (-) char and spaces during Assertion Checks.
- `hideModalUsingJS` — Detects and removes modal pop-ups from the DOM using JavaScript.
- `clickEnabledSubmitButton` — Waits for an element to be enabled, then it is clicked and submitted.
- `typeSlowly` — Console logger for test output.

---

### 2️⃣ `HbrSearchFunctions.java`

Handles search-related automation on HBR:
- Navigates to the HBR homepage
- Performs a search with a given query
- Extracts and prints search result titles
- Utilizes `GenUtils` to bypass modals

---

### 3️⃣ `HbrPageActions.java`

Handles general simple test methods on the website:
- Opens specific article URLs
- Removes modals while viewing articles
- Extracts main article headings
- Navigates between pages

Keeps test scripts clean by abstracting page behavior.

---

### 4️⃣ `BrowserStorage.java`

Manages browser data:
- Contains all browser options e.g. firefoxOptions
- Uses a switch statement to check which browser container is active
- Add arguments here for how you want the browser to behave

Supports persistent sessions and clean-slate testing.

---

### 5️⃣ `MainTestRunner.java`

JUnit 5-powered test runner:
- Sets up Selenium WebDriver
- Configures browser options (e.g., Chrome)
- Executes the full test flow:
  1. Load HBR site
  2. Remove modals
  3. Perform search
  4. Open and validate article content
- Cleans up browser sessions after tests

---

## 🚀 Getting Started

### 🔧 Prerequisites

Ensure the following are installed:

- Java **17+**
- Maven **3.8+**
- Google Chrome browser

---

### 📦 Install Dependencies

```bash
mvn clean install
```

---

### ▶️ Run Tests

```bash
mvn test
```

---

## ⚙️ Key Features

- 🛑 **Modal Removal** — Avoids UI blocking modal dialogs.
- 🔁 **Reusable Utilities** — Shared functions for element interaction and logging.
- 🍪 **Storage Management** — Full control over local/session storage and cookies.
- 🧱 **Modular Architecture** — Clear separation of page actions, utilities, and test logic.