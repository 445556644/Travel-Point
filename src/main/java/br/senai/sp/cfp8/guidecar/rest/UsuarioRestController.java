package br.senai.sp.cfp8.guidecar.rest;

import java.net.URI;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import br.senai.sp.cfp8.guidecar.anotation.Privado;
import br.senai.sp.cfp8.guidecar.anotation.Publico;
import br.senai.sp.cfp8.guidecar.model.Erro;
import br.senai.sp.cfp8.guidecar.model.Usuario;
import br.senai.sp.cfp8.guidecar.repository.UsuarioRepository;

@RestController
@RequestMapping("/api/usuario")
public class UsuarioRestController {

	@Autowired
	private UsuarioRepository usuarioRepository;

	// consumes = MediaType.APPLICATION_JSON_VALUE, faz com que consuma o json
	@RequestMapping(value = "", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@Publico
	// @RequestBody faz com que no corpo da requisição venha um corpo do usuario
	public ResponseEntity<Object> criarUsuario(@RequestBody Usuario usuario) {

		try {
			// salvar usuario no bd
			usuarioRepository.save(usuario);

			// retorna codigo 201 com a url para acesso no location e o usuario inserido no
			// corpo da resposta
			return ResponseEntity.created(URI.create("/" + usuario.getId())).body(usuario);

		} catch (Exception e) {
			e.printStackTrace();
			Erro erro = new Erro();
			erro.setStatusCode(500);
			erro.setMensagem("Erro de Constraint: Registro Duplicado");
			erro.setException(e.getClass().getName());

			return new ResponseEntity<Object>(erro, org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// metodo que pega o usuario pelo id
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@Publico
	// response entity devolve uma respota
	public ResponseEntity<Usuario> findUsuario(@PathVariable("id") Long idUsuario) {

		Optional<Usuario> usuario = usuarioRepository.findById(idUsuario);

		if (usuario.isPresent()) {

			// caso encontre o usuario devolve ele pelo id "informado"
			return ResponseEntity.ok(usuario.get());
		} else {

			// caso não encontre um usuario devolve um código de não encontrado 404
			return ResponseEntity.notFound().build();
		}

	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	@Privado
	public ResponseEntity<Void> atualizarUsuario(@RequestBody Usuario usuario, @PathVariable("id") Long id) {

		// valida o Id
		if (id != usuario.getId()) {

			throw new RuntimeException("ID INVÁLIDO");
		}
		// salva o usuário no bd
		usuarioRepository.save(usuario);
		// criando um cabeçalho http

		HttpHeaders header = new HttpHeaders();

		header.setLocation(URI.create("/api/usuario/"));
		return new ResponseEntity<Void>(header, HttpStatus.OK);
	}

	@Privado
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> excluirUsuario(@PathVariable("id") Long idUsuario) {

		// deletando o usuário
		usuarioRepository.deleteById(idUsuario);

		// código 204 sem conteúdo
		return ResponseEntity.noContent().build();

	}

}
