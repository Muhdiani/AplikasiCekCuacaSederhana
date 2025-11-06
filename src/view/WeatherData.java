// File: WeatherData.java
package view; 

/**
 * Model data untuk menyimpan informasi cuaca yang telah diparsing.
 */
public class WeatherData {
    private String cityName;
    private double temperature;
    private String description;
    private int humidity;
    private String weatherIconCode; // Untuk menentukan gambar cuaca

    public WeatherData(String cityName, double temperature, String description, int humidity, String weatherIconCode) {
        this.cityName = cityName;
        this.temperature = temperature;
        this.description = description;
        this.humidity = humidity;
        this.weatherIconCode = weatherIconCode;
    }

    // Getters
    public String getCityName() { return cityName; }
    public double getTemperature() { return temperature; }
    public String getDescription() { return description; }
    public int getHumidity() { return humidity; }
    public String getWeatherIconCode() { return weatherIconCode; }
}