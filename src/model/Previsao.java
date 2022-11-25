package model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@RequiredArgsConstructor
@ToString
public class Previsao{
  //private final int codigo;
  private final double temperaturaMinima;
  private final double temperaturaMaxima;
  private final String cidade;
  private final String data;

  public Previsao(String cidade){
    this(0, 0, cidade, null);
  }
}
