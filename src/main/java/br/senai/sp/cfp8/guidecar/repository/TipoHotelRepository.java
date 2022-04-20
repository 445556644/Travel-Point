package br.senai.sp.cfp8.guidecar.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import br.senai.sp.cfp8.guidecar.model.TipoHotel;

public interface TipoHotelRepository extends PagingAndSortingRepository<TipoHotel, Long> {

	@Query("SELECT b FROM TipoHotel b WHERE b.palavrasChave LIKE %:busca%")
	public List<TipoHotel> buscarPalavraChave(@Param("busca") String Busca);
}
