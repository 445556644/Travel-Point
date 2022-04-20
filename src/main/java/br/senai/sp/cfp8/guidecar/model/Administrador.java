package br.senai.sp.cfp8.guidecar.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import br.senai.sp.cfp8.guidecar.util.HashUtil;
import lombok.Data;

@Data
@Entity // faz com que seja uma tabela no BD
public class Administrador {

	// chave Primaria e auto-increment
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotEmpty
	private String nome;
	// faz com que reconheça o como email
	@Email
	// faz com que o email seja unico
	@Column(unique = true)
	private String email;
	@NotEmpty
	private String senha;

	// metodo para setar a senha aplicando o hash
	public void setSenha(String senha) {

		// aplica o hash e seta a senha no objeto
		this.senha = HashUtil.hash256(senha);
	}

	// metodo que seta a senha sem o hash
	public void setSenhaComHash(String hash) {

		// seta o hash na senha
		this.senha = hash;

	}

}
