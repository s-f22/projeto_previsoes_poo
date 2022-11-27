import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import model.HistoricoPrevisao;
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

        // *Lista de opcoes */
        final String opcao = JOptionPane.showInputDialog(
                "O que quer fazer?\n[1] - Pesquisar previsão\n[2] - Historico de previsões\n[3] - Sair");

        var cityName = "";
        List<Previsao> resultadoOpenWeatherMAp = new ArrayList<>();
        List<HistoricoPrevisao> resultadoOracle = new ArrayList<>();

        switch (opcao) {
            case "1":
                cityName = JOptionPane.showInputDialog(null, "Qual cidade quer pesquisar?", "PREVISOES", 3);
                resultadoOpenWeatherMAp = service.obterPrevisoesWeatherMap(WEATHER_MAP_BASEURL, WEATHER_MAP_APPID, cityName,
                        WEATHER_MAP_UNITS);

                // *Inicio da Tabela JOptionPane Pesquisa*/
                JDialog dialogPesquisa = null;
                JOptionPane optionPanePesquisa = new JOptionPane();
                optionPanePesquisa.setMessage("PREVISÕES");
                optionPanePesquisa.setMessageType(JOptionPane.INFORMATION_MESSAGE);

                JPanel panelPesquisa = new JPanel();
                panelPesquisa.setLayout(new GridLayout(40, 4));
                Previsao[] previsoes = new Previsao[resultadoOpenWeatherMAp.size()];

                JLabel[] labelPesquisa = new JLabel[previsoes.length];
                for (int i = 0; i < previsoes.length; i++) {
                    labelPesquisa[i] = new JLabel(
                            "Tem. Mín:  " + String.valueOf(resultadoOpenWeatherMAp.get(i).getTemperaturaMinima()) + "ºC");
                    labelPesquisa[i].setBounds(20, 15, 100, 20);
                    panelPesquisa.add(labelPesquisa[i]);
                    labelPesquisa[i] = new JLabel(
                            "Tem. Máx:  " + String.valueOf(resultadoOpenWeatherMAp.get(i).getTemperaturaMaxima()) + "ºC");
                    labelPesquisa[i].setBounds(50, 15, 100, 20);
                    panelPesquisa.add(labelPesquisa[i]);
                    labelPesquisa[i] = new JLabel("Cidade:  " + resultadoOpenWeatherMAp.get(i).getCidade());
                    labelPesquisa[i].setBounds(80, 15, 100, 20);
                    panelPesquisa.add(labelPesquisa[i]);
                    labelPesquisa[i] = new JLabel("Data:  " + resultadoOpenWeatherMAp.get(i).getData());
                    labelPesquisa[i].setBounds(110, 15, 100, 20);
                    panelPesquisa.add(labelPesquisa[i]);
                }

                Object[] optionsPesquisa = {};
                optionPanePesquisa.setOptions(optionsPesquisa);
                optionPanePesquisa.setOptionType(JOptionPane.DEFAULT_OPTION);
                optionPanePesquisa.add(panelPesquisa);
                dialogPesquisa = optionPanePesquisa.createDialog(null, "Resultado da Pesquisa");
                dialogPesquisa.setVisible(true);
                // *Fim da Tabela JOptionPane Pesquisa*/

                Previsao p = new Previsao(cityName);
                service.armazenarPrevisaoNoHistoricoOracleCloud(p, ORACLE_CLOUD_DATABASE);
                break;

            case "2":
                resultadoOracle = service.resgatarDadosDoBancoOracle(ORACLE_CLOUD_DATABASE);

                // *Inicio da Tabela JOptionPane Histórico*/
                JDialog dialogHistorico = null;
                JOptionPane optionPaneHistorico = new JOptionPane();
                optionPaneHistorico.setMessage("HISTÓRICO");
                optionPaneHistorico.setMessageType(JOptionPane.INFORMATION_MESSAGE);

                HistoricoPrevisao[] historico = new HistoricoPrevisao[resultadoOracle.size()];

                JPanel panelHistorico = new JPanel();
                panelHistorico.setLayout(new GridLayout(historico.length, 2));

                JLabel[] labelHistorico = new JLabel[historico.length];
                for (int i = 0; i < historico.length; i++) {
                    labelHistorico[i] = new JLabel("     "+"Data:  " + resultadoOracle.get(i).getData() + "    ");
                    labelHistorico[i].setPreferredSize(new Dimension(170,15));
                    panelHistorico.add(labelHistorico[i]);
                    labelHistorico[i] = new JLabel("     "+"     "+"Cidade:  " + resultadoOracle.get(i).getCidade());
                    labelHistorico[i].setPreferredSize(new Dimension(50,15));
                    panelHistorico.add(labelHistorico[i]);
                }
                Object[] optionsHistorico = {};
                optionPaneHistorico.setOptions(optionsHistorico);
                optionPaneHistorico.setOptionType(JOptionPane.DEFAULT_OPTION);
                optionPaneHistorico.add(panelHistorico);
                dialogHistorico = optionPaneHistorico.createDialog(null, "Histórico de Pesquisa");
                dialogHistorico.setVisible(true);
                // *Fim da Tabela JOptionPane historico*/
                break;

            case "3":
                System.exit(0);
                break;
            default:
                System.out.println("Opção inválida");
                break;
        }

    }
}
