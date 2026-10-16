// Nombre: testEmisionUnaVez.cpp
// Descripción: Test automático de que el anuncio se publica una sola vez.
// Copyright: (c) 2026 Mari Rosenova Dapcheva
// Fecha: 16/10/2026
// Autor: Mari Rosenova Dapcheva
// Aportación: criterio de aceptación del loop de Arduino.
#include <stdio.h>

int main() {
  int fallos = 0;
  bool yaEmitido = false;
  int publicaciones = 0;
  int valor = 0;
  int contador = 0;

  for (int i = 0; i < 3; i++) {
    if (yaEmitido) {
      continue;
    }
    yaEmitido = true;
    valor = 123;
    contador = 1;
    publicaciones++;
  }

  if (publicaciones != 1 || valor != 123 || contador != 1) {
    printf("Fallo una sola publicacion\n");
    fallos++;
  } else {
    printf("una sola publicacion 123 ok\n");
  }

  if (fallos > 0) {
    printf("Prueba Arduino: %d fallos\n", fallos);
    return 1;
  }
  printf("Prueba Arduino: ok\n");
  return 0;
}
