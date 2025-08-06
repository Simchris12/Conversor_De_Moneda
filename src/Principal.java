import java.io.IOException;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

public class Principal {

    private static final Set<String> MONEDAS_PERMITIDAS = Set.of("ARS", "BOB", "BRL", "CLP", "COP", "USD");

    public static void main(String[] args) {
        Scanner lectura = new Scanner(System.in);
        ConsultaMoneda consulta = new ConsultaMoneda();

        // Filtrar solo las monedas permitidas
        Set<String> monedasDisponibles = consulta.obtenerMonedasDisponibles()
                .stream()
                .filter(MONEDAS_PERMITIDAS::contains)
                .collect(Collectors.toSet());

        boolean continuar = true;

        while (continuar) {
            try {
                // Mostrar menú
                System.out.println("""
                        ******************************************************
                        Bienvenido/a al Conversor de Moneda:
                        ------------------------------------------------------
                        1) Dólar           => Peso argentino
                        2) Peso argentino  => Dólar
                        3) Dólar           => Real brasileño
                        4) Real brasileño  => Dólar
                        5) Dólar           => Peso colombiano
                        6) Peso colombiano => Dólar
                        7) Salir
                        ------------------------------------------------------
                        Elija una opción válida (1-7):
                        ******************************************************
                        """);

                String opcion = lectura.nextLine().trim();
                String monedaBase = "";
                String monedaDestino = "";

                switch (opcion) {
                    case "1" -> {
                        monedaBase = "USD";
                        monedaDestino = "ARS";
                    }
                    case "2" -> {
                        monedaBase = "ARS";
                        monedaDestino = "USD";
                    }
                    case "3" -> {
                        monedaBase = "USD";
                        monedaDestino = "BRL";
                    }
                    case "4" -> {
                        monedaBase = "BRL";
                        monedaDestino = "USD";
                    }
                    case "5" -> {
                        monedaBase = "USD";
                        monedaDestino = "COP";
                    }
                    case "6" -> {
                        monedaBase = "COP";
                        monedaDestino = "USD";
                    }
                    case "7" -> {
                        System.out.println("Gracias por usar la aplicación. ¡Hasta pronto!");
                        System.out.println("******************************************");
                        break;
                    }
                    default -> {
                        System.out.println("Opción inválida. Intenta de nuevo.");
                        continue;
                    }
                }

                if (opcion.equals("7")) {
                    continuar = false;
                    lectura.close();
                    break;
                }

                // Solicitar cantidad a convertir
                double cantidad = 0;
                while (true) {
                    System.out.printf("Ingrese la cantidad que desea convertir de %s a %s: ", monedaBase, monedaDestino);
                    String entradaCantidad = lectura.nextLine().trim();

                    try {
                        cantidad = Double.parseDouble(entradaCantidad);
                        if (cantidad <= 0) {
                            System.out.println("La cantidad debe ser mayor que cero.");
                        } else {
                            break;
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Entrada inválida. Por favor, ingrese un número válido (por ejemplo, 100 o 50.5).");
                    }
                }

                // Realizar conversión
                Moneda moneda = consulta.conversorDeMoneda(monedaBase, monedaDestino, cantidad);
                System.out.println("Resultado de la conversión: " + moneda.result());
                System.out.println("Tasa de conversión: " + moneda.conversion_rate());
                System.out.println("Cantidad convertida: " + moneda.conversion_result());
                System.out.println("De: " + moneda.base_code() + " → A: " + moneda.target_code());
                System.out.println("Fecha de la tasa: " + moneda.time_last_update_utc());

                // Guardar archivo
                GeneradorDeArchivo generador = new GeneradorDeArchivo();
                generador.guardarJson(moneda);

                // Preguntar si desea realizar otra conversión
                String respuesta;
                while (true) {
                    System.out.print("\n¿Deseas realizar otra conversión? (S/N): ");
                    respuesta = lectura.nextLine().trim().toUpperCase();
                    if (respuesta.equals("S")) {
                        break;
                    } else if (respuesta.equals("N")) {
                        continuar = false;
                        System.out.println("Gracias por usar la aplicación. ¡Hasta pronto!");
                        System.out.println("******************************************");
                        lectura.close();
                        break;
                    } else {
                        System.out.println("Respuesta inválida. Por favor, escribe 'S' para sí o 'N' para no.");
                    }
                }

            } catch (RuntimeException | IOException e) {
                System.out.println("Error: " + e.getMessage());
                String respuesta;
                while (true) {
                    System.out.print("¿Deseas intentarlo de nuevo? (S/N): ");
                    respuesta = lectura.nextLine().trim().toUpperCase();
                    if (respuesta.equals("S")) {
                        break;
                    } else if (respuesta.equals("N")) {
                        continuar = false;
                        System.out.println("Finalizando la aplicación.");
                        break;
                    } else {
                        System.out.println("Respuesta inválida. Por favor, escribe 'S' para sí o 'N' para no.");
                    }
                }
                lectura.close();
            }
        }
    }
}
