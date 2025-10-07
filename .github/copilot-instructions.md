<!--
Short, actionable instructions for AI coding agents working on this repo.
Keep concise and reference concrete files/lines so suggestions stay correct.
-->

# copilot-instructions — sep_twentythird (Flutter)

Purpose: quickly orient an AI assistant to the codebase so it can make safe, focused edits.

High-level architecture
- Flutter single-app entry at `lib/main.dart` (MaterialApp, theme: `theme/app_theme.dart`).
- UI pages live under `lib/page/` (ImagePickerPage, PreviewPage, ResultPage, GalleryPage).
- Small reusable widgets in `lib/widget/` (e.g., `CircleButton` in `camera_page.dart`).
- A simple HTTP client/service in `lib/servivce/api_servives.dart` that uploads images to a backend at POST `$baseUrl/predict`.
- Android native integration: `MethodChannel("com.example.save_image")` implemented in `android/app/src/main/kotlin/.../MainActivity.kt` to save images to the gallery.

Key flows to preserve
- Image selection -> optional preview -> send image to server -> show `ResultPage`.
  - Picker and permission logic: `lib/page/ImagePickerPage.dart` (uses `image_picker`, `permission_handler`).
  - Preview returns the selected image path via `Navigator.pop(context, imagePath)` (`lib/page/preview.dart`).
  - Upload is handled by `ApiService.uploadImage()`; baseUrl is hard-coded in `lib/servivce/api_servives.dart` and must be updated to the developer's backend IP.

Important implementation details & conventions
- Folder name `servivce/` is misspelled (not `service/`) — refer to `lib/servivce/api_servives.dart` when adding services or imports.
- Network code uses `http.MultipartRequest` and returns decoded JSON map on status 200. Throw on non-200.
- Native Android save: Flutter calls `MethodChannel('com.example.save_image').invokeMethod('saveImage', {'path': path})` from `ImagePickerPage`. When changing this, update both Dart and `MainActivity.kt`.
- Permissions: `permission_handler` calls are done inline in `ImagePickerPage._pickImage()` — keep permission requests close to usage.

Docs for editing and testing
- Run app (device/emulator): `flutter run` from repository root.
- Build release APK: `flutter build apk --release`.
- If changing native Android code, rebuild the app (clean if necessary): `flutter clean` then `flutter run`.
- Update pub packages: `flutter pub get`.

Quick safety rules for code edits
- Don't change the `MethodChannel` string or Android channel constant unless both Dart and Kotlin are updated together.
- If modifying `ApiService.baseUrl`, prefer reading from a config or environment; otherwise document the required local IP in the file comment (current is `http://192.168.1.100:5000`).
- Preserve Navigator usage patterns: pages return values via `Navigator.pop(context, value)` (see `preview.dart`).

Examples to reference
- Add an upload call after user confirms photo: see `lib/servivce/api_servives.dart` and `lib/page/preview.dart` for how imagePath is passed back to caller.
- Native save integration: `lib/page/ImagePickerPage.dart` (MethodChannel invoke) + `android/.../MainActivity.kt` (handler).

If you need help deciding scope
- Small UI fixes, wiring pages, or service improvements are OK; large refactors (state management overhaul, adding DI) require an explicit design PR with rationale.

Ask the user if anything is ambiguous (e.g., target backend IP, desired error handling UX) before changing cross-cutting behavior.
