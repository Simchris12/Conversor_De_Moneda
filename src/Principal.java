import java.io.IOException;
import java.util.Scanner;
import java.util.Set;

public class Principal {

    // Metodo para mostrar una opción de ayuda al usuario
    //en caso de que el usuario ingrese una moneda inválida.
    //Se le pregunta al usuario si desea ver las monedas disponibles.
    private static void mostrarOpcionAyuda(Set<String> monedasDisponibles, Scanner lectura) {
        System.out.print("¿Deseas ver las monedas disponibles? (S/N): ");
        String respuesta = lectura.nextLine().trim().toUpperCase();
        if (respuesta.equals("S")) {
            System.out.println("\n--- Monedas válidas ---");
            monedasDisponibles.stream().sorted().forEach(codigo -> System.out.println("• " + codigo));
            System.out.println("------------------------");
        }
    }

    // Metodo principal que inicia la aplicación
    public static void main(String[] args) {
        Scanner lectura = new Scanner(System.in);
        ConsultaMoneda consulta = new ConsultaMoneda();
        Set<String> monedasDisponibles = consulta.obtenerMonedasDisponibles();
        System.out.println("******************************************");
        System.out.println("Bienvenido a la aplicación de consulta de monedas");

        //Entra el Loop
        boolean continuar = true;

        while (continuar) {
            try {
                // Solicita al usuario los códigos de las monedas y la cantidad a convertir
                String monedaBase = "";
                while (true) {
                    System.out.print("Ingrese el código de la moneda base (por ejemplo, USD): ");
                    monedaBase = lectura.nextLine().trim().toUpperCase();
                    if (monedasDisponibles.contains(monedaBase)) break;

                    System.out.println("Moneda base inválida.");
                    mostrarOpcionAyuda(monedasDisponibles, lectura);
                }

                String monedaDestino = "";
                while (true) {
                    System.out.print("Ingrese el código de la moneda destino (por ejemplo, EUR): ");
                    monedaDestino = lectura.nextLine().trim().toUpperCase();
                    if (monedasDisponibles.contains(monedaDestino)) break;

                    System.out.println("Moneda destino inválida.");
                    mostrarOpcionAyuda(monedasDisponibles, lectura);
                }


                // Solicita al usuario la cantidad a convertir
                // Se asegura de que la cantidad sea un número válido y mayor que cero
                double cantidad = 0;
                while (true) {
                    System.out.print("Ingrese la cantidad que desea convertir: ");
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

                Moneda moneda = consulta.conversorDeMoneda(monedaBase, monedaDestino, cantidad);
                System.out.println("Resultado de la conversión: " + moneda.result());
                System.out.println("Tasa de conversión: " + moneda.conversion_rate());
                System.out.println("Cantidad convertida: " + moneda.conversion_result());
                System.out.println("De: " + moneda.base_code() + " → A: " + moneda.target_code());
                System.out.println("Fecha de la tasa: " + moneda.time_last_update_utc());


                GeneradorDeArchivo generador = new GeneradorDeArchivo();
                generador.guardarJson(moneda);


                String respuesta;
                while (true) {
                    System.out.print("\n¿Deseas realizar otra conversión? (S/N): ");
                    respuesta = lectura.nextLine().trim().toUpperCase();
                    if (respuesta.equals("S")) {
                        break; // vuelve al inicio del while principal
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