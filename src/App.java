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
import javax.swing.SwingConstants;

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
        final String action = JOptionPane.showInputDialog(
                "O que quer fazer?\n[1] - Pesquisar previsão\n[2] - Historico de previsões\n[3] - Sair");

        var cityName = "";
        List<Previsao> previsoesResultado = new ArrayList<>();
        List<HistoricoPrevisao> getOracle = new ArrayList<>();

        switch (action) {
            case "1":
                cityName = JOptionPane.showInputDialog(null, "Qual cidade quer pesquisar?", "PREVISOES", 3);
                previsoesResultado = service.obterPrevisoesWeatherMap(WEATHER_MAP_BASEURL, WEATHER_MAP_APPID, cityName,
                        WEATHER_MAP_UNITS);

                // *Inicio da Tabela JOptionPane Pesquisa*/
                JDialog dialog = null;
                JOptionPane optionPane = new JOptionPane();
                optionPane.setMessage("PREVISÕES");
                optionPane.setMessageType(JOptionPane.INFORMATION_MESSAGE);

                JPanel panel = new JPanel();
                panel.setLayout(new GridLayout(40, 4));
                Previsao[] previsoes = new Previsao[previsoesResultado.size()];

                JLabel[] label = new JLabel[previsoes.length];
                for (int i = 0; i < previsoes.length; i++) {
                    label[i] = new JLabel(
                            "Tem. Mín:  " + String.valueOf(previsoesResultado.get(i).getTemperaturaMinima()) + "ºC");
                    label[i].setBounds(20, 15, 100, 20);
                    panel.add(label[i]);
                    label[i] = new JLabel(
                            "Tem. Máx:  " + String.valueOf(previsoesResultado.get(i).getTemperaturaMaxima()) + "ºC");
                    label[i].setBounds(50, 15, 100, 20);
                    panel.add(label[i]);
                    label[i] = new JLabel("Cidade:  " + previsoesResultado.get(i).getCidade());
                    label[i].setBounds(80, 15, 100, 20);
                    panel.add(label[i]);
                    label[i] = new JLabel("Data:  " + previsoesResultado.get(i).getData());
                    label[i].setBounds(110, 15, 100, 20);
                    panel.add(label[i]);
                }

                Object[] options = {};
                optionPane.setOptions(options);
                optionPane.setOptionType(JOptionPane.DEFAULT_OPTION);
                optionPane.add(panel);
                dialog = optionPane.createDialog(null, "Resultado da Pesquisa");
                dialog.setVisible(true);
                // *Fim da Tabela JOptionPane Pesquisa*/

                Previsao p = new Previsao(cityName);
                service.armazenarPrevisaoNoHistoricoOracleCloud(p, ORACLE_CLOUD_DATABASE);
                break;

            case "2":
                getOracle = service.resgatarDadosDoBancoOracle(ORACLE_CLOUD_DATABASE);

                // *Inicio da Tabela JOptionPane Histórico*/
                JDialog dialoG = null;
                JOptionPane optionPanE = new JOptionPane();
                optionPanE.setMessage("HISTÓRICO");
                optionPanE.setMessageType(JOptionPane.INFORMATION_MESSAGE);

                HistoricoPrevisao[] historico = new HistoricoPrevisao[getOracle.size()];

                JPanel paneL = new JPanel();
                paneL.setLayout(new GridLayout(historico.length, 2));

                JLabel[] labeL = new JLabel[historico.length];
                for (int i = 0; i < historico.length; i++) {
                    labeL[i] = new JLabel("   "+"Data:  " + getOracle.get(i).getData() + "    ");
                    // labeL[i].setBounds(0, 15, 50, 20);
                    labeL[i].setPreferredSize(new Dimension(100,15));
                    paneL.add(labeL[i]);
                    labeL[i] = new JLabel("Cidade:  " + getOracle.get(i).getCidade());
                    labeL[i].setPreferredSize(new Dimension(100,15));
                    // labeL[i].setBounds(0, 15, 50, 20);
                    paneL.add(labeL[i]);
                }
                Object[] optionS = {};
                optionPanE.setOptions(optionS);
                optionPanE.setOptionType(JOptionPane.DEFAULT_OPTION);
                optionPanE.add(paneL);
                dialoG = optionPanE.createDialog(null, "Histórico de Pesquisa");
                dialoG.setVisible(true);
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
