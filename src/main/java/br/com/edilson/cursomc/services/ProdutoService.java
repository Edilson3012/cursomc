package br.com.edilson.cursomc.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import br.com.edilson.cursomc.domain.Categoria;
import br.com.edilson.cursomc.domain.Produto;
import br.com.edilson.cursomc.repositories.CategoriaRepository;
import br.com.edilson.cursomc.repositories.ProdutoRepository;
import br.com.edilson.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class ProdutoService {

	@Autowired
	private CategoriaRepository categoriaRepository;

	@Autowired
	private ProdutoRepository repo;

	public Produto find(Integer id) {
		Optional<Produto> obj = repo.findById(id);

		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ",Tipo: " + Produto.class.getName()));
	}

	/*
	 * Parâmetros: 
	 * - nome: um trecho de nome de produto 
	 * - idsCategorias: uma lista de códigos de categorias 
	 *  
	 * Retorno: 
	 * A listagem de produtos que contém o trecho de nome dado e
	 * que pertencem a pelo menos uma das categorias dadas,
	 * isso realizado diretamente com o repository, manipulando o BD
	 */
	public Page<Produto> search(String nome, List<Integer> ids, Integer page, Integer linesPerPage, String orderBy,
			String direction) {
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);

		List<Categoria> categorias = categoriaRepository.findAllById(ids);
		return repo.search(nome, categorias, pageRequest);
	}
}
