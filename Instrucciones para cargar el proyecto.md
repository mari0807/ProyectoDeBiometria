# ProyectoBiometria

Autora: Mari Dapcheva

```
ProyectoBiometria/
├── android/     → app Android (abrir esta carpeta en Android Studio)
│   └── app/
├── arduino/     → código de la SparkFun nRF52840
├── doc/
└── src/
    ├── business_logic/
    ├── communication/
    ├── database/
    ├── frontend_business_logic/
    └── gui/
```

## Android
Abre en Android Studio la carpeta `android/` (no la raíz del repo).

## Arduino
El sketch está en `arduino/HolaMundoIBeacon/`.

## Servidor y web
1. XAMPP: Apache y MySQL.
2. Importar `src/database/datosMediciones.sql`.
3. Copiar el proyecto a `C:\xampp\htdocs\biometria`.
4. La puerta HTTP es `http://localhost/biometria/src/communication/ReglasREST.php`.
5. La página es `http://localhost/biometria/src/gui/webMari.html`.
