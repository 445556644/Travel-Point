package br.senai.sp.cfp8.guidecar.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import br.senai.sp.cfp8.guidecar.model.Administrador;

public interface RepositoryAdmin extends PagingAndSortingRepository<Administrador, Long> {

}
