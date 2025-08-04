import java.io.IOException;
import java.util.Scanner;

public class Principal {
    public static void main(String[] args) {
        Scanner lectura = new Scanner(System.in);
        ConsultaMoneda consulta = new ConsultaMoneda();
        System.out.println("******************************************");
        System.out.println("Bienvenido a la aplicación de consulta de monedas");

        //Entra el Loop
        boolean continuar = true;

        while (continuar) {
            try {
                // Solicita al usuario los códigos de las monedas y la cantidad a convertir
                System.out.println("Ingrese el código de la moneda base (por ejemplo, USD): ");
                String monedaBase = lectura.nextLine().toUpperCase();
                System.out.println("Ingrese el código de la moneda destino (por ejemplo, EUR): ");
                String monedaDestino = lectura.nextLine().toUpperCase();
                System.out.println("Ingrese la cantidad que desea convertir: ");
                var cantidad = Double.parseDouble(lectura.nextLine());
                Moneda moneda = consulta.conversorDeMoneda(monedaBase, monedaDestino, cantidad);
                System.out.println("Resultado de la conversión: " + moneda.result());
                System.out.println("Tasa de conversión: " + moneda.conversion_rate());
                System.out.println("Cantidad convertida: " + moneda.conversion_result());
                System.out.println("Código base: " + moneda.base_code());
                System.out.println("Código de destino: " + moneda.target_code());
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