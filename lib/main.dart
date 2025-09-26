import 'package:flutter/material.dart';
import 'theme/app_theme';                 // ← lib/theme/app_theme.dart
import 'page/Camera';        // ← lib/page/Camera/camera_page.dart

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      title: 'AI Skin Scanner',
      theme: appTheme,
      home: const CameraPage(),
    );
  }
}
