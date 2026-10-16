# ProyectoBiometria

Autora: Mari Dapcheva

```
ProyectoBiometria/
├── android/     → app Android (abrir esta carpeta en Android Studio)
│   └── app/
├── arduino/     → código de la plaquita que ya esta cargado en ella
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

En caso de no estar en casa con mi wifi 192.168.1.3 se tiene que actualizar ClienteREST.java con la nueva ip - la de cualquier otro wifi. Para ver cual es la ip de otro wifi en una terminal ejecuto el comando ipconfig y me quedo con la direccion IPv4 del nuevo Wi-Fi.
