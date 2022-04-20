//json.parse transfere string para json que vira um objeto
// https://youtu.be/YVf7iy2sIOw exemplo...


// apontando para o campo que contem o id cep.
const cep = document.querySelector("#cep")

// tratando o resultado que veio da promessa
const showData = (result)=>{
    
    // for que percorre cada um dos campos que veio do objeto p cada um deles
    for(const campo in result){

        // verifica se aquele elemento com aquele id existem alguem com aquele id
        if(document.querySelector("#"+campo)){

            // preenchendo dinamicamente aqueles campos que existem 
            document.querySelector("#"+campo).value = result[campo]
        }
    }
}




cep.addEventListener("blur",(e)=>{
   
    // substituindo o - por um epaço vazio
    let busca = cep.value.replace("-","")
 
    // definindo o metodo, modulo e cache 
    const options = {
        method: 'GET',
        // esse mode faz com que elimine alguns erros silenciosos
        mode: 'cors',
        // guarda todos os "dados da API "
        cache: 'default'
    }

    // aceesando a api publica,e passando a variavel search
    fetch(`https://viacep.com.br/ws/${busca}/json/`, options)

    //se deu certo traz a resposta no formato json
    .then(response =>{ response.json()

        // apos verificar se deu certo e retorna os dados 
        .then( data => showData(data))
    })

    // verifica se deu errado capturando a msg de erro
    .catch(e => console.log('Deu Erro: '+ e,message))
})  