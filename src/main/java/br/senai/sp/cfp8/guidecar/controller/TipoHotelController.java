package br.senai.sp.cfp8.guidecar.controller;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import br.senai.sp.cfp8.guidecar.anotation.Privado;
import br.senai.sp.cfp8.guidecar.model.TipoHotel;
import br.senai.sp.cfp8.guidecar.repository.TipoHotelRepository;

@Controller
public class TipoHotelController {

	// importando o repository para termos acesso aos metodos...
	
	@Autowired
	private TipoHotelRepository tipoLojaRepository;

	// metodo que faz o redirecionamento ao formulario
	@Privado
	@RequestMapping("formTipos")
	public String formCar() {

		return "tipos/Form";
	}

	// metodo que salva
	@RequestMapping("salvarTipo")
	@Privado
	public String salvarTipo(TipoHotel loja) {

		tipoLojaRepository.save(loja);

		return "redirect:listarHoteis/1";
	}

	// metodo que faz a listagem
	@RequestMapping("listarTiposLojas/{pagina}")
	@Privado
	public String listaTipos(Model model, @PathVariable("pagina") int page) {

		model.addAttribute("tipoLoja", tipoLojaRepository.findAll());

		// cria uma pagina que começa na 0, que possuem 6 elementos por paginas e ordena
		// pelo nome
		PageRequest pageble = PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.ASC, "nome"));

		// cria a pagina atual atraves do repository

		Page<TipoHotel> pagina = tipoLojaRepository.findAll(pageble);

		// descobrir o total de pagina
		int totalPg = pagina.getTotalPages();

		// cria uma lista de inteiros para representar as paginas
		List<Integer> pageNumbers = new ArrayList<Integer>();

		for (int i = 0; i < totalPg; i++) {

			pageNumbers.add(i + 1);
		}
		// adiciona as variaveis na model

		// pendurando os admins cadastrados
		model.addAttribute("loja", pagina.getContent());

		// pagina atual
		model.addAttribute("pgAtual", page);

		//
		model.addAttribute("numTotalPg", totalPg);

		// quantidade de paginas com base nos cadastros
		model.addAttribute("numPg", pageNumbers);

		// retorna para o html da lista

		return "Tipos/Lista";

	}

	// metodo que atualiza
	@RequestMapping("alterarTipo")
	@Privado
	public String atualizarTipo(Long id, Model model) {

		model.addAttribute("loja", tipoLojaRepository.findById(id).get());

		return "forward:formTipos";
	}

	// metodo que deleta
	@RequestMapping("excluirTipo")
	@Privado
	public String excluirTipo(Long id) {

		tipoLojaRepository.deleteById(id);

		return "redirect:listarLojas/1";
	}

	// metodo que faz a busca pela palvra chave cadastrada
	@RequestMapping("buscar")
	@Privado
	public String buscarPorpalavraChave(Model model, String busca) {

		model.addAttribute("tipoLoja", tipoLojaRepository.buscarPalavraChave(busca));

		return "Tipos/Lista";
	}
}
