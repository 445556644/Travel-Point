package br.senai.sp.cfp8.guidecar.util;

import java.io.IOException;
import java.util.UUID;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

@Service
public class FirebaseUtil {

	// variavel para guardar as credenciais do firebase
	private Credentials credenciais;
	// variavel para acessar o historico
	private Storage storage;
	// constante para o nome do bucket
	// usando o final pq é constante
	private final String BUCKET_NAME = "pointcar-eabc7.appspot.com";
	// constante para o prefixo da URL
	private final String PREFIX = "https://firebasestorage.googleapis.com/v0/b/" + BUCKET_NAME + "/o/";
	// constante para o sufixo da URL
	private final String SUFFIX = "?alt=media";
	// constante para a url
	// usando o %s pq usamos o string format
	private final String DOWNLOAD_URL = PREFIX + "%s" + SUFFIX;

	public FirebaseUtil() {

		// buscar as credenciais (arquivo json)
		Resource resource = new ClassPathResource("chavefirebase.json");
		// ler o arquivo para obter as credenciais

		try {
			// resource.getInputStream faz com que abra e leia o arquivo
			credenciais = GoogleCredentials.fromStream(resource.getInputStream());
			// acessa o serviço de storage
			storage = StorageOptions.newBuilder().setCredentials(credenciais).build().getService();
		} catch (IOException e) {

			throw new RuntimeException(e.getMessage());

		}
	}

	public String uploadFile(MultipartFile arquivo) throws IOException {

		// gera um nome aletorio para o arquivo
		String nomeArquivo = UUID.randomUUID().toString() + getExtensao(arquivo.getOriginalFilename());
		// criar um blobId atráves do nome gerado pelo arquivo
		BlobId blobId = BlobId.of(BUCKET_NAME, nomeArquivo);
		// criar um blobinfo através do blobid
		BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("media").build();
		// gravar o blobinfo no Storage passando os bytes do arquivo
		storage.create(blobInfo, arquivo.getBytes());
		// retorna a url do arquivo gerado no storage
		return String.format(DOWNLOAD_URL, nomeArquivo);

	}

	private String getExtensao(String nomeArquivo) {

		// retorna o trecho da string do ultimo . ate o fim (pega a extensao do arquivo)
		return nomeArquivo.substring(nomeArquivo.lastIndexOf('.'));

	}

	public void deletar(String nomeArquivo) {

		// retira o prefix e sufixo do nome do arquivo
		nomeArquivo = nomeArquivo.replace(PREFIX, "").replace(SUFFIX, "");

		// pega um blob atraves do arquivo
		Blob blob = storage.get(BlobId.of(BUCKET_NAME, nomeArquivo));

		// deleta o arquivo

		storage.delete(blob.getBlobId());

	}

}
