package br.senai.sp.cfp8.guidecar.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import lombok.Data;

@Data
@Entity
public class Hotel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String nome;
	@Column(columnDefinition = "TEXT")
	private String descricao;
	private String cep;
	private String endereco;
	private String numero;
	private String complemento;
	private String estado;
	private String cidade;
	private String foto;
	private String redesSociais;
	private String formasPagamento;
	private String bairro;
	private String telefone;
	private String site;
	@ManyToOne
	private TipoHotel tipoHotel;
	@OneToMany(mappedBy = "hotel")
	private List<Avaliacao> avaliacoes;

	public String[] verFotos() {

		return this.foto.split(";");
	}

}
