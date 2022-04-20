package br.senai.sp.cfp8.guidecar.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import br.senai.sp.cfp8.guidecar.model.Hotel;
import br.senai.sp.cfp8.guidecar.model.TipoHotel;

public interface HotelRepository extends PagingAndSortingRepository<Hotel, Long> {

	public List<TipoHotel> findAllByOrderByNomeAsc();
	
	public List<Hotel> findByTipoHotelId(Long idTipo);
	
	public List<Hotel> findByEstado(String estado);
	
}
