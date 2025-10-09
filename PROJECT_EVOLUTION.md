# 📈 Project Evolution Log

> This document tracks the architectural evolution of the JDBC CLI project across milestones. It’s intended for contributors, maintainers, and curious developers who want to understand the design decisions behind the framework.

---

## 🏗️ Initial Snapshot – Milestone v1

- Basic CLI structure for JDBC CRUD operations  
- Manual insert/update/delete logic using `Statement` and `PreparedStatement`  
- Table creation using a map of column definitions  
- Column name fetch via metadata (`getColumnNames`)  
- Table existence check using `SHOW TABLES`  
- Raw `int` return codes for success/failure  
- No retry logic or schema-aware validation  
- No expressive UX or emoji feedback

### ✅ JDK Compatibility
- Written in JDK 21 but compatible with **JDK 8+**

---

## ⚠️ JDK Compatibility Update – Milestone v2

- Now requires **JDK 14+** due to use of `record` and `switch (return)` syntax

---

## 🔧 ConnectionFactory – Milestone v2

- Replaced raw `int` codes with `OperationStatus` enum  
- Added `closeResource()` for safe JDBC cleanup  
- Refactored `createDataBase()` and `createTable()` to return status enums  
- Added `isDatabaseExist()` using metadata  
- Introduced `isColumnValueUnique()` for pre-insert validation  
- Built `getInsertableColumns()` to fetch metadata:
  - Skips auto-increment fields  
  - Detects mandatory and unique columns  
  - Returns structured `ColumnMeta` records  
- Improved insert logic with better error handling and semantic status  
- Replaced hardcoded messages with enum-driven responses  
- Enhanced CLI feedback using `MessageStyler` and emoji support

---

## 🎛️ MenuManager – Milestone v2

- Switched from `CrudImpl` to `CrudManager` for cleaner separation of concerns  
- Added styled CLI prompts using `MessageStyler` and `Color`  
- Introduced emoji feedback for input, warnings, and exit  
- Refactored `menuHandler()` with expressive retry logic  
- Added `getColumnType(int)` to map SQL types to semantic labels  
- Introduced new input methods:
  - `decimalInput()` for fractional values  
  - `validName()` with regex and reserved keyword filtering  
- Improved existing input methods with styled prompts and error handling  
- Removed `tableData()` method and its dependency on `Model`

---

## 🧩 CrudManager – Introduced in Milestone v2

- Bridges `MenuManager` and `CrudImpl`  
- Centralizes logic for table creation, data insertion, and future operations  
- Validates mandatory and unique fields interactively  
- Uses `getColumnValue()` for type-based input  
- Handles nullable fields with fallback via `nullManager()`  
- Delegates execution to `CrudImpl`  
- Placeholder methods added for read, update, and delete

---

## 📐 Crud Interface – Milestone v2 Refactor

- Removed overloaded `createTable()` method with hardcoded schema  
- Replaced `String` return types with `OperationStatus` enums  
- Replaced `Model` parameter with `Map<String, Object>` for dynamic inserts  
- Interface now reflects execution-only responsibilities

---

## ⚙️ CrudImpl – Milestone v2 Refactor

- Refactored to remove business logic and focus purely on execution  
- `CrudManager` now handles validation and orchestration  
- Uses `OperationStatus` enums for semantic clarity  
- Accepts dynamic column-value map for inserts  
- Removed legacy methods and internal validation logic

---

## 🧱 Constants – Milestone v2

- Refactored flat constants into structured nested classes and enums  
- Grouped menu options under `Constants.Menu`  
- Separated input-related constants into `Constants.Input`  
- Added `Constants.DefaultValue` for fallback values  
- Introduced `Constants.ColumnType` for standardized type references  
- Added `OperationMessage` enum for consistent CLI feedback

---

## 🚀 Launch Class – Milestone v2 Cleanup

- Removed hardcoded test logic for manual data insertion  
- Replaced with a clean call to `manager.menuHandler()`  
- Now serves purely as a bootstrapping entry point for the interactive CLI

---

## 🎨 Utility Enhancements – Milestone v2

Also introduced `ColumnMeta` as a `record` for structured metadata, along with `Emoji`, `Color`, and `MessageStyler` classes to deliver expressive, color-coded, and emoji-enhanced CLI feedback.

---

## 🧾 Model Class – Removed in Milestone v2

- Previously used to encapsulate user data for inserts  
- Removed to support dynamic, schema-aware input handling  
- `tableData()` method deleted from `MenuManager`  
- `Model.java` class removed from codebase

---

## 🧭 Summary

Milestone v2 transformed this project from a basic JDBC CLI into a modular, metadata-aware, UX-enhanced framework. Key themes include:

- Separation of concerns via `CrudManager`  
- Schema-driven validation and dynamic input handling  
- Expressive CLI feedback using `MessageStyler`, `Emoji`, and `Color`  
- Enum-based status reporting for clarity and maintainability  
- Removal of rigid DTOs (`Model`) in favor of flexible maps

This log is here to help future contributors understand the why behind the what.

---
