import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dtos.LocationDTO;
import dtos.PersonDTO;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class Main {
    public static void main(String[] args) {
        String jsonString = fetchWeatherDataInRoskilde();
        LocationDTO roskildeDTO = fromJsonToLocationDTO(jsonString);
        System.out.println("Sync:");
        System.out.println(roskildeDTO);
        List<LocationDTO> cities = fetchWeatherDataInDenmark();
        System.out.println("Async: ");
        cities.forEach(System.out::println);

        String jsonTextFile = JsonFileReader.readJsonFromFile("person.json");
        PersonDTO personDTO = fromJsonToPersonDTO(jsonTextFile);
        System.out.println(personDTO);
        jsonTextFile = JsonFileReader.readJsonFromFile("persons.json");
        List<PersonDTO> persons = fromJsonToPersonsDTO(jsonTextFile);
        persons.forEach(System.out::println);
    }

    private static String fetchWeatherDataInRoskilde() {
        HttpResponse<String> response;
        try {
            
            String uri = "https://vejr.eu/api.php?location=Roskilde&degree=C";
            
            // Create an HttpClient instance
            HttpClient client = HttpClient.newHttpClient();

            // Create a request
            HttpRequest request = HttpRequest.newBuilder().uri(new URI(uri)).GET().build();

            // Send the request and get the response
            response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Check the status code and print the response
            if (response.statusCode() == 200) {
                return response.body();
            } else {
                System.out.println("GET request failed. Status code: " + response.statusCode());
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private static LocationDTO fromJsonToLocationDTO(String jsonString) {
        // Create ObjectMapper instance
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // Convert JSON string to LocationDTO
            return objectMapper.readValue(jsonString, LocationDTO.class);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static List<LocationDTO> fetchWeatherDataInDenmark() {

        List<LocationDTO> cities = new ArrayList<>();
        HttpClient client = HttpClient.newHttpClient();
        List<CompletableFuture<HttpResponse<String>>> futures = getCompletableFutures(client);

        // Wait for all the requests to complete
        CompletableFuture<Void> allOfFutures = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));

        // Process the responses once all futures have completed
        allOfFutures.thenRun(() -> {
            for (CompletableFuture<HttpResponse<String>> future : futures) {
                try {
                    // Get the response for each future
                    HttpResponse<String> response = future.get();
                    if (response.statusCode() == 200) {
                        cities.add(fromJsonToLocationDTO(response.body()));
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).join(); // Ensures the main thread waits for all responses to complete
        return cities;
    }

    private static List<CompletableFuture<HttpResponse<String>>> getCompletableFutures(HttpClient client) {
        List<String> uris = List.of("https://vejr.eu/api.php?location=Roskilde&degree=C", "https://vejr.eu/api.php?location=Lyngby&degree=C", "https://vejr.eu/api.php?location=Skagen&degree=C", "https://vejr.eu/api.php?location=Odense&degree=C", "https://vejr.eu/api.php?location=Aarhus&degree=C");

        // List to hold all the CompletableFutures
        List<CompletableFuture<HttpResponse<String>>> futures = new ArrayList<>();

        // Loop through the list of URIs and create a CompletableFuture for each request
        uris.forEach(uri -> {
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(uri)).build();
            // Send the request asynchronously and add the CompletableFuture to the list
            CompletableFuture<HttpResponse<String>> future = client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
            futures.add(future);
        });
        return futures;
    }

    private static PersonDTO fromJsonToPersonDTO(String jsonString) {
        // Create ObjectMapper instance
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // Convert JSON string to LocationDTO
            return objectMapper.readValue(jsonString, PersonDTO.class);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static List<PersonDTO> fromJsonToPersonsDTO(String jsonString) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // Convert JSON string to List<PersonDTO>
            return objectMapper.readValue(jsonString, new TypeReference<List<PersonDTO>>() {});
        } catch (IOException e) {
            // Log the error
            e.printStackTrace();
            // Return an empty list instead of null
            return Collections.emptyList();
        }
    }

}