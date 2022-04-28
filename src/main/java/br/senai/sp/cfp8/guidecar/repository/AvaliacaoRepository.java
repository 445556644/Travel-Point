package br.senai.sp.cfp8.guidecar.repository;

import java.util.List;
import org.springframework.data.repository.PagingAndSortingRepository;
import br.senai.sp.cfp8.guidecar.model.Avaliacao;
public interface AvaliacaoRepository extends PagingAndSortingRepository<Avaliacao, Long> {

	public List<Avaliacao> findByHotelId(Long idHotel);
}
