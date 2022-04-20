package br.senai.sp.cfp8.guidecar.model;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import br.senai.sp.cfp8.guidecar.util.HashUtil;
import lombok.Data;

@Data
@Entity
public class Usuario {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String nome;
	@Column(unique = true)
	private String email;
	private String senha;
	
	public void setSenhaComHash(String senha) {
		
		this.senha = HashUtil.hash256(senha);
	}
}
