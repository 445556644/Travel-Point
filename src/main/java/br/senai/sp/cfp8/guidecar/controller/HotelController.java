package br.senai.sp.cfp8.guidecar.controller;

import java.io.IOException;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import br.senai.sp.cfp8.guidecar.anotation.Privado;
import br.senai.sp.cfp8.guidecar.model.Hotel;
import br.senai.sp.cfp8.guidecar.repository.HotelRepository;
import br.senai.sp.cfp8.guidecar.repository.TipoHotelRepository;
import br.senai.sp.cfp8.guidecar.util.FirebaseUtil;

@Controller
public class HotelController {

	@Autowired
	TipoHotelRepository tipoHotelRepository;

	@Autowired
	HotelRepository hotelRepository;

	@Autowired
	FirebaseUtil firebaseUtil;

	@Privado
	@RequestMapping("formHotel")
	public String formLoja(Model model) {

		model.addAttribute("tipos", tipoHotelRepository.findAll());

		return "Hotel/FormHotel";

	}

	@RequestMapping("index2")
	public String index() {

		return "Principal/index2";
	}

	@Privado
	@RequestMapping("salvarHotel")
	// referenciando o name do input(fotos) a variavel fileFotos
	public String salvarLoja(Hotel loja, @RequestParam("fotos") MultipartFile[] filefotos) {
		// variavel para url das fotos
		String fotos = loja.getFoto();

		// percorrer cada arquivo que foi submetido no form

		for (MultipartFile file : filefotos) {

			// verificar se o arquivo esta vazio

			if (file.getOriginalFilename().isEmpty()) {

				// caso tenha um vazio, verifica se esta vazia, vai pra proxima
				continue;
			}

			// faz o upload para a a nuvem e obtem a url gerada

			try {
				fotos += firebaseUtil.uploadFile(file) + ";";
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new RuntimeException(e);
			}

		}

		// atribui a string fotos ao objeto loja
		loja.setFoto(fotos);

		hotelRepository.save(loja);

		return "redirect:formHotel";

	}

	// metodo que faz a listagem
	@Privado
	@RequestMapping("listarHoteis/{pagina}")
	public String listaTipos(Model model, @PathVariable("pagina") int page) {

		model.addAttribute("hotel", hotelRepository.findAll());

		// cria uma pagina que começa na 0, que possuem 6 elementos por paginas e ordena
		// pelo nome
		PageRequest pageble = PageRequest.of(page - 1, 1, Sort.by(Sort.Direction.ASC, "nome"));

		// cria a pagina atual atraves do repository

		Page<Hotel> pagina = hotelRepository.findAll(pageble);

		// descobrir o total de pagina
		int totalPg = pagina.getTotalPages();

		// cria uma lista de inteiros para representar as paginas
		List<Integer> pageNumbers = new ArrayList<Integer>();

		for (int i = 0; i < totalPg; i++) {

			pageNumbers.add(i + 1);
		}
		// adiciona as variaveis na model

		// pendurando os admins cadastrados
		model.addAttribute("hotel", pagina.getContent());

		// pagina atual
		model.addAttribute("pgAtual", page);

		//
		model.addAttribute("numTotalPg", totalPg);

		// quantidade de paginas com base nos cadastros
		model.addAttribute("numPg", pageNumbers);

		// retorna para o html da lista

		return "Principal/index2";

	}

	@Privado
	@RequestMapping("alterarHotel")
	public String atualizarLoja(Model model, Long id) {

		model.addAttribute("hotel", hotelRepository.findById(id).get());

		return "forward:formHotel";

	}

	@Privado
	@RequestMapping("excluirHotel")
	public String excluirLoja(Long id) {

		Hotel loja = hotelRepository.findById(id).get();

		// se o comprimento da foro for maior que zero, deleta todas as fotos da
		// firebase
		if (loja.getFoto().length() > 0) {
			for (String foto : loja.verFotos()) {

				firebaseUtil.deletar(foto);
			}
		}

		hotelRepository.deleteById(id);

		return "redirect:listarHoteis/1";
	}

	@Privado
	@RequestMapping("excluirFotoHotel")
	public String excluirFoto(Long id, int numFoto, Model model) {

		// busca o restauranten no bd

		Hotel loja = hotelRepository.findById(id).get();

		// pegando a string da foto que vai ser excluida

		String fotoUrl = loja.verFotos()[numFoto];

		// excluir do firebase
		firebaseUtil.deletar(fotoUrl);

		// tirando a foto da string foto de loja
		loja.setFoto(loja.getFoto().replace(fotoUrl + "", ""));

		// salva no bd

		hotelRepository.save(loja);

		// adiciona a loja na model

		model.addAttribute("hotel", loja);

		return "forward:formHotel";
	}

	@Privado
	@RequestMapping("verMais")
	public String verCaixa() {

		return "Hotel/Detalhes";
	}

	@Privado
	@RequestMapping("detalhes")
	public String mostrarCaixa(Model model, Long id) {

		model.addAttribute("mostrar", hotelRepository.findById(id).get());

		return "forward:verMais";
	}

}
