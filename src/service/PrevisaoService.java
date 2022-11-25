package service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import model.Previsao;

public class PrevisaoService {
    private HttpClient cliente = HttpClient.newBuilder().build();

    public void armazenarPrevisaoNoHistoricoOracleCloud(Previsao p, String oracle) throws Exception {
        JSONObject pJOSN = new JSONObject();
        pJOSN.put("cidade_previsao", p.getCidade());
        pJOSN.put("data_previsao", java.time.LocalDateTime.now() + "Z");
        // pJOSN.put("data_previsao", java.time.LocalDate.now());

        HttpRequest req = HttpRequest.newBuilder()
                .POST(BodyPublishers.ofString(pJOSN.toString()))
                .uri(URI.create(oracle))
                .header("content-type", "application/json")
                .build();

        cliente.send(req, BodyHandlers.ofString());

        System.out.println(pJOSN);
    }

    public List<Previsao> obterPrevisoesWeatherMap(
            String url,
            String appid,
            String cidade,
            String units) throws Exception {
        url = String.format("%s?q=%s&appid=%s&units=%s",
                url,
                cidade,
                appid,
                units);
        // 1.Construir o objeto que representa a requisiçao
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();
        // 2.Enviar a requisicao ao servidor weatherMap
        var res = cliente.send(req, BodyHandlers.ofString());
        // 3. Mapeamento JSON => COLECAO DE OBJETOS JAVA
        JSONObject raiz = new JSONObject(res.body());
        JSONArray list = raiz.getJSONArray("list");

        List<Previsao> listaPrevisoes = new ArrayList<>();
        
        for (int i = 0; i < list.length(); i++) {
            JSONObject previsaoJSON = list.getJSONObject(i);
            JSONObject main = previsaoJSON.getJSONObject("main");
            double temp_min = main.getDouble("temp_min");
            double temp_max = main.getDouble("temp_max");
            String dt_txt = previsaoJSON.getString("dt_txt");

            Previsao p = new Previsao(temp_min, temp_max, cidade, dt_txt);
            System.out.println(p);
            listaPrevisoes.add(p);
        }
        return listaPrevisoes;
    }
}
