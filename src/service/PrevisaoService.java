package service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import model.Previsao;

public class PrevisaoService {
  private HttpClient client = HttpClient.newBuilder().build();

  public void armazenarPrevisaoNoHistoricoOracleCloud(Previsao p, String urlOracl) throws Exception{
    /*
     * { "cidade": "Itu"}
     * 
     */
    
    JSONObject pJSON = new JSONObject();
    pJSON.put("cidade", p.getCidade());
    pJSON.put("data_previsao", java.time.LocalDateTime.now() + "Z");
    //pJSON.put("data_previsao", "2016-11-04T10:58:10.228Z");
    
    HttpRequest req = HttpRequest.newBuilder().uri(URI.create(urlOracl)).POST(BodyPublishers.ofString(pJSON.toString())).header("Content-Type", "application/json").build();
    
    System.out.println(client.send(req, BodyHandlers.ofString()));
    //System.out.println(pJSON);
  }


  public List<Previsao> obterPrevisoesWeatherMap(
    String url,
    String appid,
    String cidade,
    String units
  ) throws Exception {
    url = String.format(
      "%s?q=%s&appid=%s&units=%s",
      url,
      cidade,
      appid,
      units
    );

    //1. Construir um objeto que representa a requisição
    HttpRequest req = 
      HttpRequest.newBuilder().uri(URI.create(url)).build();
    
    //2. Enviar a requisição ao servidor WeatherMap
    var res = client.send(req, BodyHandlers.ofString());

    //3. Mapeamento JSON -> coleção de objetos Java
    JSONObject raiz = new JSONObject(res.body());
    JSONArray list = raiz.getJSONArray("list");

    List <Previsao> listaPrevisoes = new ArrayList();

    for (int i = 0; i < list.length(); i++){
      JSONObject previsaoJSON = list.getJSONObject(i);
      JSONObject main = previsaoJSON.getJSONObject("main");
      double temp_min = main.getDouble("temp_min");
      double temp_max = main.getDouble("temp_max");
      String dt_txt = previsaoJSON.getString("dt_txt");
      
      Previsao p = 
        new Previsao(temp_min, temp_max, cidade, dt_txt);
        System.out.println(p);
        listaPrevisoes.add(p);
    }
    return listaPrevisoes;
  }
}
