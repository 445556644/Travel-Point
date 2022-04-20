package br.senai.sp.cfp8.guidecar.anotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//faz com que em tempo de execução ele pegue o metodo e olhe a classe sla...
@Retention(RetentionPolicy.RUNTIME)
//expecificando que essa anotação serve para metodo
@Target(ElementType.METHOD)
public @interface Publico {

}
