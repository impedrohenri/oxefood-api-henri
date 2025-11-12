package br.com.ifpe.oxefood.modelo.produto;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.com.ifpe.oxefood.util.Util;
import br.com.ifpe.oxefood.util.exception.ProdutoException;
import jakarta.transaction.Transactional;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository repository;

    @Transactional
    public Produto save(Produto produto) {

        if (produto.getValorUnitario() < 20) {
            throw new ProdutoException(ProdutoException.MSG_VALOR_MINIMO_PRODUTO);
        } else if (produto.getValorUnitario() > 100) {
            throw new ProdutoException(ProdutoException.MSG_VALOR_MAXIMO_PRODUTO);
        }

        produto.setHabilitado(Boolean.TRUE);
        return repository.save(produto);
    }

    public List<Produto> listarTodos() {

        return repository.findAll();
    }

    public Produto obterPorID(Long id) {

        return repository.findById(id).get();
    }

    public void update(Long id, Produto produtoAnterior) {

        Produto produto = repository.findById(id).get();
        produto.setCodigo(produtoAnterior.getCodigo());
        produto.setCategoria(produtoAnterior.getCategoria());
        produto.setTitulo(produtoAnterior.getTitulo());
        produto.setDescricao(produtoAnterior.getDescricao());
        produto.setValorUnitario(produtoAnterior.getValorUnitario());
        produto.setTempoEntregaMinimo(produtoAnterior.getTempoEntregaMinimo());
        produto.setTempoEntregaMaximo(produtoAnterior.getTempoEntregaMaximo());

        repository.save(produto);
    }

    public Produto delete(Long id) {

        Produto produto = repository.findById(id).get();
        produto.setHabilitado(Boolean.FALSE);

        return repository.save(produto);
    }

    public List<Produto> filtrar(String codigo, String titulo, Long idCategoria) {

        List<Produto> listaProdutos = repository.findAll();

        if ((codigo != null && !"".equals(codigo)) &&
                (titulo == null || "".equals(titulo)) &&
                (idCategoria == null)) {
            listaProdutos = repository.consultarPorCodigo(codigo);
        } else if ((codigo == null || "".equals(codigo)) &&
                (titulo != null && !"".equals(titulo)) &&
                (idCategoria == null)) {
            listaProdutos = repository.findByTituloContainingIgnoreCaseOrderByTituloAsc(titulo);
        } else if ((codigo == null || "".equals(codigo)) &&
                (titulo == null || "".equals(titulo)) &&
                (idCategoria != null)) {
            listaProdutos = repository.consultarPorCategoria(idCategoria);
        } else if ((codigo == null || "".equals(codigo)) &&
                (titulo != null && !"".equals(titulo)) &&
                (idCategoria != null)) {
            listaProdutos = repository.consultarPorTituloECategoria(titulo, idCategoria);
        }

        return listaProdutos;
    }

    @Transactional
    public Produto saveImage(Long id, MultipartFile imagem) {

        Produto produto = obterPorID(id);

        String imagemUpada = Util.fazerUploadImagem(imagem);

        if (imagemUpada != null) {
            produto.setImagem(imagemUpada);
        }

        return save(produto);
    }

}
