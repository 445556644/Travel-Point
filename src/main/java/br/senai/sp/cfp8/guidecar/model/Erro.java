package br.senai.sp.cfp8.guidecar.model;

import lombok.Data;

@Data
public class Erro {

	private int statusCode;
	private String mensagem;
	private String exception;
}
