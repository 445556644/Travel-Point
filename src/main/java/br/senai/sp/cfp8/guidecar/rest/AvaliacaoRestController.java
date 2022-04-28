package br.senai.sp.cfp8.guidecar.rest;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.print.attribute.standard.Media;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.senai.sp.cfp8.guidecar.anotation.Privado;
import br.senai.sp.cfp8.guidecar.anotation.Publico;
import br.senai.sp.cfp8.guidecar.model.Avaliacao;
import br.senai.sp.cfp8.guidecar.model.Hotel;
import br.senai.sp.cfp8.guidecar.model.Usuario;
import br.senai.sp.cfp8.guidecar.repository.AvaliacaoRepository;

@RestController
@RequestMapping("/api/avaliacao")
public class AvaliacaoRestController {

	@Autowired

	private AvaliacaoRepository avaliacaoRepository;

	@Privado
	@RequestMapping(value = "", method = RequestMethod.POST, consumes = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Avaliacao> criarAvaliacao(@RequestBody Avaliacao avaliacao) {

		avaliacaoRepository.save(avaliacao);

		return ResponseEntity.created(URI.create("/avaliacao" + avaliacao.getId())).body(avaliacao);
	}
	
	// metodo que pega a avaliacao pelo id
		@RequestMapping(value = "/{id}", method = RequestMethod.GET)
		@Publico
		// response entity devolve uma respota
		public ResponseEntity<Avaliacao> getAvaliacao(@PathVariable("id") Long idAvalicao) {

			Optional<Avaliacao> avalicao = avaliacaoRepository.findById(idAvalicao);

			if (avalicao.isPresent()) {

				// caso encontre a avalicao devolve ele pelo id "informado"
				return ResponseEntity.ok(avalicao.get());
			} else {

				// caso não encontre uma avaliacao devolve um código de não encontrado 404
				return ResponseEntity.notFound().build();
			}

		}
		
		// metodo que busca as avaliações pelo hotel
		@Publico
		@RequestMapping(value = "/hotel/{id}", method = RequestMethod.GET)
		public List<Avaliacao> getHotel(@PathVariable("id") Long idHotel) {

			return avaliacaoRepository.findByHotelId(idHotel);

		}

}
