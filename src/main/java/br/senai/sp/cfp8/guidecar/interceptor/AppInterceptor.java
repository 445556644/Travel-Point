package br.senai.sp.cfp8.guidecar.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import br.senai.sp.cfp8.guidecar.anotation.Publico;

// faz com que o spring entenda ele como um componente
@Component
public class AppInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		// variavel para descobrir para onde estao tentando acessar
		String uri = request.getRequestURI();
		// Mostrar a uri
		System.out.println(uri);
		System.out.println(handler.toString());

		// instanceof faz com que verifique se a variavel é "dele"
		// verifica se o handler é um HandlerMetod
		// que indica que foi encontrado o metodo em algum controller para a requisição
		if (handler instanceof HandlerMethod) {

			// liberar a pagina inicial
			if (uri.endsWith("/error")) {

				return true;
			}
			
			if (uri.startsWith("/api")) {
				
				// quando for uma api
				return true;
				
			}else {
				
			
			
			// pegando a anotação do metodo fazendo o casting

			HandlerMethod method = (HandlerMethod) handler;
			
			// se o metodo for publico libera
			if (method.getMethodAnnotation(Publico.class)!= null) {
				
				return true;
			}
			
			// verifica se tem um usuario logado atraves da session que pegamos pela requisição
			
			if (request.getSession().getAttribute("usuarioLogado") != null){
				
				return true;
				
			}else {
				// redireciona para a pg inicial
				response.sendRedirect("/");
				return false;
			}

		}
		}

		return true;
	}
}
