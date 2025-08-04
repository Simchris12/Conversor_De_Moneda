import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Set;
import java.util.stream.Collectors;

public class ConsultaMoneda {
    public Set<String> obtenerMonedasDisponibles() {
        URI direccion = URI.create("https://v6.exchangerate-api.com/v6/c54d586bdf6cdd603c31280e/latest/USD");

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(direccion)
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            JsonObject json = new Gson().fromJson(response.body(), JsonObject.class);
            JsonObject rates = json.getAsJsonObject("conversion_rates");

            return rates.keySet().stream().collect(Collectors.toSet());

        } catch (Exception e) {
            throw new RuntimeException("No se pudieron obtener las monedas disponibles. " + e.getMessage());
        }
    }


    public Moneda conversorDeMoneda(String base, String destino, double cantidad) {
        URI direccion = URI.create("https://v6.exchangerate-api.com/v6/c54d586bdf6cdd603c31280e/latest/" + base);

        // Crear un cliente y solicitud HTTP
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(direccion)
                .build();

        // Enviar la solicitud y procesar la respuesta
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Parsear solo el campo de tasa deseado
            var jsonTree = new Gson().fromJson(response.body(), com.google.gson.JsonObject.class);
            String resultado = jsonTree.get("result").getAsString();
            double tasa = jsonTree.getAsJsonObject("conversion_rates").get(destino).getAsDouble();
            int conversion = (int) (cantidad * tasa);

            return new Moneda(
                    resultado,
                    String.valueOf(tasa),
                    conversion,
                    base,
                    destino
            );
        } catch (Exception e) {
            throw new RuntimeException("No se pudo obtener la tasa de cambio. " + e.getMessage());
        }
    }
}
