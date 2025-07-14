import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import org.json.JSONObject;

public class WeatherApp extends JFrame {

    private JTextField cityTextField;
    private JButton getWeatherButton;
    private JTextArea resultArea;

    private final String API_KEY = "0b25ec0f691b65dc6bb04b517d0cab01";  
    public WeatherApp() {
        setTitle("Weather App");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Input panel
        JPanel panel = new JPanel();
        cityTextField = new JTextField(15);
        getWeatherButton = new JButton("Get Weather");
        panel.add(new JLabel("City:"));
        panel.add(cityTextField);
        panel.add(getWeatherButton);

        // Result area
        resultArea = new JTextArea();
        resultArea.setEditable(false);

        add(panel, BorderLayout.NORTH);
        add(new JScrollPane(resultArea), BorderLayout.CENTER);

        // Action listener
        getWeatherButton.addActionListener(e -> fetchWeather());
    }

    private void fetchWeather() {
        String city = cityTextField.getText().trim();
        if (city.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter a city name!");
            return;
        }

        try {
            String urlStr = "http://api.openweathermap.org/data/2.5/weather?q=" + city +
                            "&appid=" + API_KEY + "&units=metric";
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder jsonText = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                jsonText.append(line);
            }
            in.close();

            // Parse JSON
            JSONObject json = new JSONObject(jsonText.toString());
            JSONObject main = json.getJSONObject("main");
            String weather = json.getJSONArray("weather").getJSONObject(0).getString("description");
            double temp = main.getDouble("temp");
            int humidity = main.getInt("humidity");

            String output = "Weather: " + weather + "\n" +
                            "Temperature: " + temp + "°C\n" +
                            "Humidity: " + humidity + "%";

            resultArea.setText(output);

        } catch (Exception e) {
            resultArea.setText("Error fetching weather data.\n" + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new WeatherApp().setVisible(true));
    }
}