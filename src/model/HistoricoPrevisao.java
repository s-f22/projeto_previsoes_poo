package model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@RequiredArgsConstructor
@ToString
public class HistoricoPrevisao {
    private final String cidade;
    private final String data;
}

