package br.senai.sp.cfp8.guidecar.rest;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.senai.sp.cfp8.guidecar.anotation.Publico;
import br.senai.sp.cfp8.guidecar.model.Hotel;
import br.senai.sp.cfp8.guidecar.model.TipoHotel;
import br.senai.sp.cfp8.guidecar.repository.HotelRepository;
import br.senai.sp.cfp8.guidecar.repository.TipoHotelRepository;

@RestController
@RequestMapping("/api/hotel")
public class HotelRestController {
	@Autowired
	private HotelRepository hotelRepository;

	// metodo que pega (get) a listagem de hoteis
	@RequestMapping(value = "", method = RequestMethod.GET)
	@Publico
	public Iterable<Hotel> getHotel() {

		return hotelRepository.findAll();
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@Publico
	public ResponseEntity<Hotel> findHotel(@PathVariable("id") Long idHotel) {

		Optional<Hotel> hotel = hotelRepository.findById(idHotel);

		if (hotel.isPresent()) {

			// caso encontre o hotel devolve ele pelo id "informado"
			return ResponseEntity.ok(hotel.get());
		} else {

			// caso nao encontre um hotel devolve um codigo de nao encontrado 404
			return ResponseEntity.notFound().build();
		}

	}

	// metodo que busca pelo tipo
	@Publico
	@RequestMapping(value = "/tipo/{id}", method = RequestMethod.GET)
	public List<Hotel> getHotelByTipo(@PathVariable("id") Long idTipo) {

		return hotelRepository.findByTipoHotelId(idTipo);

	}
	
	@RequestMapping(value = "/estado/{estado}", method = RequestMethod.GET)
	public List<Hotel> getEstado(@PathVariable("estado") String estado){
		
		return hotelRepository.findByEstado(estado);
	}
	
	

}
