package br.senai.sp.cfp8.guidecar.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import br.senai.sp.cfp8.guidecar.anotation.Privado;
import br.senai.sp.cfp8.guidecar.anotation.Publico;
import br.senai.sp.cfp8.guidecar.model.Administrador;
import br.senai.sp.cfp8.guidecar.repository.AdminRepository;
import br.senai.sp.cfp8.guidecar.util.HashUtil;

@Controller
public class AdminController {

	@Autowired
	private AdminRepository repAdm;

	@Privado
	@RequestMapping("formAdmin")
	public String formAdmin() {

		return "Admin/FormAdmin";
	}

	// valid faz a validação no back end
	// BindingResult faz com que pegue os erros
	// RedirectAttributes faz com que mostre os erro no form
	@RequestMapping("salvarAdmin")
	@Privado
	public String salvarAdmin(@Valid Administrador adm, BindingResult result, RedirectAttributes redAtt) {
		// verifica se houve erro na validação do projeto

		// verfica se há erros na validaçao
		if (result.hasErrors()) {

			// envia msg de erro via requisição
			redAtt.addFlashAttribute("msgErro", "Verifique Os Campos ...");
			redAtt.addFlashAttribute("admin", adm);
			return "redirect:listarAdmin";
		}

		boolean alteracao = adm.getId() != null ? true : false;

		// verifica se a senha é igual a hash null
		if (adm.getSenha().equals(HashUtil.hash256(""))) {

			if (!alteracao) {

				// pega a "senha" que sera da primeira ate a ultima letra do email .
				// substring pega o inicio.
				// indexof pega ate onde que da string, na senha vai ate 0 @.
				String parte = adm.getEmail().substring(0, adm.getEmail().indexOf("@"));

				// seta a primeira parte do email que sera a nova senha.
				adm.setSenha(parte);
			} else {

				// busca a senha atual pelo id do adm
				String senha = repAdm.findById(adm.getId()).get().getSenha();

				// seta a senha com hash
				adm.setSenhaComHash(senha);
			}
		}

		try {
			repAdm.save(adm);
			redAtt.addFlashAttribute("msgSucesso",
					"Cadastro Salvo Com Sucesso, Caso a senha nao seja informada, sera a parte do e-mail antes do @"
							+ " Adm De Id: " + adm.getId());
			return "redirect:listarAdmin/1";

		} catch (Exception e) {

			// caso ocorra um erro informa ao usuario de forma melhor
			redAtt.addFlashAttribute("EmailRepetido", "Houve Um Erro Ao Cadastrar O administrador" + e.getMessage());
		}
		return "redirect:formAdmin";

		// verifica se nao é uma alteração pelo id, pois toda a alteracao possui um id

	}

	// request mapping para listar informando a página informada
	@RequestMapping("listarAdmin/{pagina}")
	@Privado
	// @PathVariable associando int page a ${page}
	public String listarAdmin(Model model, @PathVariable("pagina") int page) {

		// cria uma pagina que começa na 0, que possuem 6 elementos por paginas e ordena
		// pelo nome
		PageRequest pageble = PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.ASC, "nome"));

		// cria a pagina atual atraves do repository

		Page<Administrador> pagina = repAdm.findAll(pageble);

		// descobrir o total de pagina
		int totalPg = pagina.getTotalPages();

		// cria uma lista de inteiros para representar as paginas
		List<Integer> pageNumbers = new ArrayList<Integer>();

		for (int i = 0; i < totalPg; i++) {

			pageNumbers.add(i + 1);
		}
		// adiciona as variaveis na model

		// pendurando os admins cadastrados
		model.addAttribute("admin", pagina.getContent());
		// pagina atual
		model.addAttribute("pgAtual", page);
		//
		model.addAttribute("numTotalPg", totalPg);
		// quantidade de paginas com base nos cadastros
		model.addAttribute("numPg", pageNumbers);
		// retorna para o html da lista
		return "admin/ListaAdmin";
	}

	@Privado
	@RequestMapping("alterarAdmin")
	public String alterarAdmin(Model model, Long id) {

		model.addAttribute("admin", repAdm.findById(id).get());

		return "forward:formAdmin";

	}

	@RequestMapping("excluirAdmin")
	public String excluirAdmin(Long id) {

		repAdm.deleteById(id);

		return "redirect:listarAdmin/1";

	}

	// HttpSession, usado para guardar os adm na sessão, que é a ligação do
	// navegador com o site
	@Publico
	@RequestMapping("salvarLogin")
	public String login(Administrador administradorLogin, RedirectAttributes attr, HttpSession session) {

		// buscar o adm no BD, pelo email e senha

		Administrador adm = repAdm.findByEmailAndSenha(administradorLogin.getEmail(), administradorLogin.getSenha());

		if (adm == null) {

			attr.addFlashAttribute("mensagemErro", "Email Ou Senha Invalidas");

			return "redirect:/";
		} else {
			// se nao for nulo salva na sessão e acessa o sistema

			session.setAttribute("usuarioLogado", adm);
			return "redirect:listarHoteis/1";
		}

	}
	
	@Privado
	@RequestMapping("LogOut")
	public String logOut(HttpSession httpSession) {
		
		// elimina o usuario da sessao
		httpSession.invalidate();
		
		return"redirect:/";
	}

}
