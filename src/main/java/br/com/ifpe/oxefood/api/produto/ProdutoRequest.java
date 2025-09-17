package br.com.ifpe.oxefood.api.produto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class ProdutoRequest {
    
    private String codigo;

    private String titulo;

    private String descricao;

    private Double valorUnitario;

    private Integer tempoEntrega;
}
