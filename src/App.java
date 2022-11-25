import java.io.FileInputStream;
import java.util.List;
import java.util.Properties;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import model.Previsao;
import service.PrevisaoService;

public class App {
    public static void main(String[] args) throws Exception {
        Properties properties = new Properties();
        properties.load(new FileInputStream("src/App.properties"));

        final String WEATHER_MAP_BASEURL = properties.getProperty("WEATHER_MAP_BASEURL");
        final String WEATHER_MAP_APPID = properties.getProperty("WEATHER_MAP_APPID");
        final String WEATHER_MAP_UNITS = properties.getProperty("WEATHER_MAP_UNITS");
        final String ORACLE_CLOUD_DATABASE = properties.getProperty("ORACLE_CLOUD_DATABASE");

        PrevisaoService service = new PrevisaoService();

        // Previsao p = new Previsao("Ilheus");
        // service.armazenarPrevisaoNoHistoricoOracleCloud(p, ORACLE_CLOUD_DATABASE);

        List <Previsao> listaPrevisoes = service.obterPrevisoesWeatherMap(
        WEATHER_MAP_BASEURL,
        WEATHER_MAP_APPID,
        "Juazeiro",
        WEATHER_MAP_UNITS);

        int opcao = Integer.parseInt(JOptionPane.showInputDialog("Digite a opcao desejada: \n 1-Consultar Previsao\n2-Consultar Historico\n"));

        

        

    }
}
