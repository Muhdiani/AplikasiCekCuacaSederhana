package view; 

import view.WeatherData; // Pastikan ini sesuai dengan lokasi file WeatherData Anda

import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class WeatherAPIClient {

    // GANTI DENGAN KUNCI API ANDA YANG SEBENARNYA
    private static final String API_KEY = "433d3488b408448488898e69f543a67f"; 
    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/weather?q=";

    /**
     * Mengambil data cuaca mentah (JSON String) dari API.
     * @param city Nama kota yang akan dicari.
     * @return String JSON data cuaca, atau null/ERROR jika gagal.
     */
    public String getWeatherData(String city) {
        // Encoding kota menggunakan URLEncoder untuk penanganan spasi, karakter khusus, dsb.
        String encodedCity;
        try {
            encodedCity = URLEncoder.encode(city, StandardCharsets.UTF_8.toString());
        } catch (Exception e) {
            encodedCity = city.replaceAll(" ", "%20");
        }
        
        String urlString = BASE_URL + encodedCity + "&appid=" + API_KEY + "&units=metric";
        StringBuilder response = new StringBuilder();

        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            int responseCode = connection.getResponseCode();
            if (responseCode != 200) {
                // Tangani error, seperti kota tidak ditemukan (404)
                return "ERROR:" + responseCode; 
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            connection.disconnect();
            
        } catch (Exception e) {
            e.printStackTrace();
            return "EXCEPTION:" + e.getMessage();
        }
        
        return response.toString();
    }

    /**
     * Mengambil dan memparsing data cuaca.
     * @param city Nama kota.
     * @return Objek WeatherData, atau null jika terjadi error.
     */
    public WeatherData fetchAndParseWeather(String city) {
        String jsonResponse = getWeatherData(city);

        if (jsonResponse == null || jsonResponse.startsWith("ERROR") || jsonResponse.startsWith("EXCEPTION")) {
             // Cek jika API mengembalikan kode error 404/kota tidak ditemukan
            if (jsonResponse != null && jsonResponse.contains("404")) {
                 System.err.println("Kota tidak ditemukan.");
            }
            return null; // Gagal mendapatkan data
        }

        return parseSimpleJson(jsonResponse);
    }
    
    /**
     * Memparsing String JSON secara sederhana (manual) menjadi objek WeatherData.
     * Menggunakan delimiter koma (,) untuk memisahkan nilai-nilai di dalam objek JSON.
     */
    private WeatherData parseSimpleJson(String json) {
        try {
            // Ekstraksi Kota
            String cityName = extractValue(json, "\"name\":\"", "\"");
            
            // Ekstraksi Suhu (main -> temp). Gunakan koma sebagai delimiter akhir.
            String tempString = extractValue(json, "\"temp\":", ","); 
            double temperature = Double.parseDouble(tempString);

            // Ekstraksi Kelembaban (main -> humidity). Gunakan koma sebagai delimiter akhir.
            // Kita akan mencoba koma terlebih dahulu, jika gagal (berarti humidity elemen terakhir), 
            // kita akan mencoba delimiter kurawal penutup }.
            
            String humidityString = extractValue(json, "\"humidity\":", ",");
            
            if (humidityString.isEmpty()) {
                // Coba dengan delimiter kurawal penutup (jika humidity adalah item terakhir)
                humidityString = extractValue(json, "\"humidity\":", "}");
            }
            
            // Hapus karakter whitespace di sekitar nilai (jika ada)
            humidityString = humidityString.trim();

            int humidity = (int) Double.parseDouble(humidityString);

            // Ekstraksi Deskripsi (weather[0] -> description)
            String description = extractValue(json, "\"description\":\"", "\"");
            
            // Ekstraksi Kode Ikon (weather[0] -> icon)
            String iconCode = extractValue(json, "\"icon\":\"", "\"");

            // Pastikan tidak ada data yang null/kosong
            if (cityName.isEmpty() || description.isEmpty() || iconCode.isEmpty()) {
                 throw new Exception("Salah satu nilai cuaca tidak ditemukan atau format JSON tidak sesuai.");
            }
            
            // Kembalikan objek WeatherData
            return new WeatherData(cityName, temperature, description, humidity, iconCode);

        } catch (NumberFormatException nfe) {
            System.err.println("Gagal mengkonversi nilai angka. Kesalahan delimiter parsing.");
            nfe.printStackTrace();
            return null;
        } catch (Exception e) {
            System.err.println("Gagal memparsing JSON secara manual.");
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Utility method untuk mendapatkan nilai string di antara dua delimiter.
     */
    private String extractValue(String source, String startDelimiter, String endDelimiter) {
        int startIndex = source.indexOf(startDelimiter);
        if (startIndex == -1) return "";
        
        startIndex += startDelimiter.length();
        
        int endIndex = source.indexOf(endDelimiter, startIndex);
        if (endIndex == -1) return "";
        
        return source.substring(startIndex, endIndex);
    }
}