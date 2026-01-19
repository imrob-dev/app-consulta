package dev.imrob.painelconsulta.service;

import dev.imrob.painelconsulta.dto.response.ConsultaResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class MockConsultaApiServiceImpl implements ConsultaApiService {

    private final Random random = new Random();

    @Override
    public ConsultaResponse executarConsulta(String tipo, String valor) {
        return switch (tipo.toUpperCase()) {
            case "CPF" -> consultaCpf(valor);
            case "CNPJ" -> consultaCnpj(valor);
            case "PLACA" -> consultaPlaca(valor);
            case "TELEFONE" -> consultaTelefone(valor);
            case "EMAIL" -> consultaEmail(valor);
            case "NOME" -> consultaNome(valor);
            default -> new ConsultaResponse(null, tipo, valor, Map.of("erro", "Tipo de consulta não suportado"), "ERRO", LocalDateTime.now());
        };
    }

    private ConsultaResponse consultaCpf(String cpf) {
        Map<String, Object> dados = new HashMap<>();
        dados.put("cpf", formatCpf(cpf));
        dados.put("nome", gerarNomeAleatorio());
        dados.put("dataNascimento", gerarDataNascimento());
        dados.put("idade", random.nextInt(40) + 20);
        dados.put("sexo", random.nextBoolean() ? "M" : "F");
        dados.put("nomeMae", gerarNomeAleatorio() + " " + gerarSobrenome());
        dados.put("situacaoCadastral", "REGULAR");
        dados.put("dataInscricao", "2010-03-15");
        dados.put("digitoVerificador", cpf.substring(cpf.length() - 2));

        Map<String, Object> endereco = new HashMap<>();
        endereco.put("logradouro", gerarLogradouro());
        endereco.put("numero", String.valueOf(random.nextInt(9999) + 1));
        endereco.put("bairro", gerarBairro());
        endereco.put("cidade", gerarCidade());
        endereco.put("uf", gerarUf());
        endereco.put("cep", gerarCep());
        dados.put("endereco", endereco);

        return new ConsultaResponse(null, "CPF", cpf, dados, "SUCESSO", LocalDateTime.now());
    }

    private ConsultaResponse consultaCnpj(String cnpj) {
        Map<String, Object> dados = new HashMap<>();
        dados.put("cnpj", formatCnpj(cnpj));
        dados.put("razaoSocial", gerarRazaoSocial());
        dados.put("nomeFantasia", gerarNomeFantasia());
        dados.put("situacaoCadastral", "ATIVA");
        dados.put("dataAbertura", "2015-06-20");
        dados.put("naturezaJuridica", "206-2 - Sociedade Empresária Limitada");
        dados.put("capitalSocial", random.nextInt(900000) + 100000);
        dados.put("porte", random.nextBoolean() ? "PEQUENO" : "MÉDIO");

        Map<String, Object> endereco = new HashMap<>();
        endereco.put("logradouro", gerarLogradouro());
        endereco.put("numero", String.valueOf(random.nextInt(9999) + 1));
        endereco.put("bairro", gerarBairro());
        endereco.put("cidade", gerarCidade());
        endereco.put("uf", gerarUf());
        endereco.put("cep", gerarCep());
        dados.put("endereco", endereco);

        dados.put("atividadePrincipal", Map.of(
            "codigo", "62.01-5-01",
            "descricao", "Desenvolvimento de programas de computador sob encomenda"
        ));

        dados.put("telefone", gerarTelefone());
        dados.put("email", "contato@" + gerarNomeFantasia().toLowerCase().replace(" ", "") + ".com.br");

        return new ConsultaResponse(null, "CNPJ", cnpj, dados, "SUCESSO", LocalDateTime.now());
    }

    private ConsultaResponse consultaPlaca(String placa) {
        Map<String, Object> dados = new HashMap<>();
        dados.put("placa", placa.toUpperCase());
        dados.put("marca", gerarMarcaVeiculo());
        dados.put("modelo", gerarModeloVeiculo());
        dados.put("anoFabricacao", 2015 + random.nextInt(10));
        dados.put("anoModelo", 2016 + random.nextInt(10));
        dados.put("cor", gerarCor());
        dados.put("combustivel", random.nextBoolean() ? "GASOLINA" : "FLEX");
        dados.put("categoria", "PARTICULAR");
        dados.put("situacao", "REGULAR");
        dados.put("chassi", gerarChassi());
        dados.put("renavam", gerarRenavam());
        dados.put("municipio", gerarCidade());
        dados.put("uf", gerarUf());

        Map<String, Object> proprietario = new HashMap<>();
        proprietario.put("nome", gerarNomeAleatorio() + " " + gerarSobrenome());
        proprietario.put("documento", gerarCpf());
        proprietario.put("tipo", "PESSOA_FISICA");
        dados.put("proprietario", proprietario);

        dados.put("restricoes", random.nextInt(10) > 8 ?
            java.util.List.of("ALIENAÇÃO FIDUCIÁRIA") : java.util.List.of());

        return new ConsultaResponse(null, "PLACA", placa, dados, "SUCESSO", LocalDateTime.now());
    }

    private ConsultaResponse consultaTelefone(String telefone) {
        Map<String, Object> dados = new HashMap<>();
        dados.put("telefone", telefone);
        dados.put("tipo", telefone.length() > 10 ? "CELULAR" : "FIXO");
        dados.put("operadora", gerarOperadora());
        dados.put("portabilidade", random.nextBoolean());
        dados.put("cidade", gerarCidade());
        dados.put("uf", gerarUf());

        Map<String, Object> titular = new HashMap<>();
        titular.put("nome", gerarNomeAleatorio() + " " + gerarSobrenome());
        titular.put("documento", gerarCpf());
        dados.put("titular", titular);

        return new ConsultaResponse(null, "TELEFONE", telefone, dados, "SUCESSO", LocalDateTime.now());
    }

    private ConsultaResponse consultaEmail(String email) {
        Map<String, Object> dados = new HashMap<>();
        dados.put("email", email);
        dados.put("valido", true);
        dados.put("provedor", email.split("@")[1]);
        dados.put("tipo", email.contains("gmail") || email.contains("hotmail") ? "PESSOAL" : "CORPORATIVO");

        Map<String, Object> titular = new HashMap<>();
        titular.put("nome", gerarNomeAleatorio() + " " + gerarSobrenome());
        titular.put("documento", gerarCpf());
        dados.put("titular", titular);

        dados.put("redesSociais", java.util.List.of("LinkedIn", "Facebook"));

        return new ConsultaResponse(null, "EMAIL", email, dados, "SUCESSO", LocalDateTime.now());
    }

    private ConsultaResponse consultaNome(String nome) {
        Map<String, Object> dados = new HashMap<>();
        dados.put("nomePesquisado", nome);

        var resultados = new java.util.ArrayList<Map<String, Object>>();
        int qtd = random.nextInt(5) + 1;
        for (int i = 0; i < qtd; i++) {
            Map<String, Object> pessoa = new HashMap<>();
            pessoa.put("nome", nome + " " + gerarSobrenome());
            pessoa.put("cpf", gerarCpf());
            pessoa.put("dataNascimento", gerarDataNascimento());
            pessoa.put("cidade", gerarCidade());
            pessoa.put("uf", gerarUf());
            resultados.add(pessoa);
        }
        dados.put("resultados", resultados);
        dados.put("totalEncontrado", qtd);

        return new ConsultaResponse(null, "NOME", nome, dados, "SUCESSO", LocalDateTime.now());
    }

    // Métodos auxiliares para gerar dados fictícios
    private String gerarNomeAleatorio() {
        String[] nomes = {"João", "Maria", "José", "Ana", "Carlos", "Fernanda", "Pedro", "Juliana", "Lucas", "Beatriz"};
        return nomes[random.nextInt(nomes.length)];
    }

    private String gerarSobrenome() {
        String[] sobrenomes = {"Silva", "Santos", "Oliveira", "Souza", "Rodrigues", "Ferreira", "Alves", "Pereira", "Lima", "Gomes"};
        return sobrenomes[random.nextInt(sobrenomes.length)];
    }

    private String gerarDataNascimento() {
        int ano = 1960 + random.nextInt(40);
        int mes = random.nextInt(12) + 1;
        int dia = random.nextInt(28) + 1;
        return String.format("%04d-%02d-%02d", ano, mes, dia);
    }

    private String gerarLogradouro() {
        String[] tipos = {"Rua", "Avenida", "Travessa", "Alameda"};
        String[] nomes = {"das Flores", "Brasil", "São Paulo", "Principal", "Central", "da Liberdade"};
        return tipos[random.nextInt(tipos.length)] + " " + nomes[random.nextInt(nomes.length)];
    }

    private String gerarBairro() {
        String[] bairros = {"Centro", "Jardim América", "Vila Nova", "Boa Vista", "Santa Cruz", "São José"};
        return bairros[random.nextInt(bairros.length)];
    }

    private String gerarCidade() {
        String[] cidades = {"São Paulo", "Rio de Janeiro", "Belo Horizonte", "Curitiba", "Porto Alegre", "Salvador"};
        return cidades[random.nextInt(cidades.length)];
    }

    private String gerarUf() {
        String[] ufs = {"SP", "RJ", "MG", "PR", "RS", "BA"};
        return ufs[random.nextInt(ufs.length)];
    }

    private String gerarCep() {
        return String.format("%05d-%03d", random.nextInt(99999), random.nextInt(999));
    }

    private String gerarTelefone() {
        return String.format("(%02d) 9%04d-%04d", 11 + random.nextInt(89), random.nextInt(9999), random.nextInt(9999));
    }

    private String gerarRazaoSocial() {
        String[] nomes = {"Tech", "Brasil", "Global", "Prime", "Master", "Alpha"};
        String[] tipos = {"Solutions", "Tecnologia", "Serviços", "Comércio", "Consultoria"};
        return nomes[random.nextInt(nomes.length)] + " " + tipos[random.nextInt(tipos.length)] + " LTDA";
    }

    private String gerarNomeFantasia() {
        String[] nomes = {"TechCorp", "BrasilNet", "GlobalSoft", "PrimeTech", "MasterData", "AlphaSys"};
        return nomes[random.nextInt(nomes.length)];
    }

    private String gerarMarcaVeiculo() {
        String[] marcas = {"VOLKSWAGEN", "FIAT", "CHEVROLET", "FORD", "TOYOTA", "HONDA", "HYUNDAI"};
        return marcas[random.nextInt(marcas.length)];
    }

    private String gerarModeloVeiculo() {
        String[] modelos = {"GOL", "ONIX", "HB20", "COROLLA", "CIVIC", "KICKS", "COMPASS"};
        return modelos[random.nextInt(modelos.length)];
    }

    private String gerarCor() {
        String[] cores = {"PRETO", "BRANCO", "PRATA", "VERMELHO", "AZUL", "CINZA"};
        return cores[random.nextInt(cores.length)];
    }

    private String gerarChassi() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 17; i++) {
            sb.append("0123456789ABCDEFGHJKLMNPRSTUVWXYZ".charAt(random.nextInt(33)));
        }
        return sb.toString();
    }

    private String gerarRenavam() {
        return String.format("%011d", random.nextLong(99999999999L));
    }

    private String gerarCpf() {
        return String.format("%03d.%03d.%03d-%02d",
            random.nextInt(999), random.nextInt(999), random.nextInt(999), random.nextInt(99));
    }

    private String gerarOperadora() {
        String[] operadoras = {"VIVO", "CLARO", "TIM", "OI"};
        return operadoras[random.nextInt(operadoras.length)];
    }

    private String formatCpf(String cpf) {
        cpf = cpf.replaceAll("\\D", "");
        if (cpf.length() == 11) {
            return cpf.substring(0, 3) + "." + cpf.substring(3, 6) + "." +
                   cpf.substring(6, 9) + "-" + cpf.substring(9);
        }
        return cpf;
    }

    private String formatCnpj(String cnpj) {
        cnpj = cnpj.replaceAll("\\D", "");
        if (cnpj.length() == 14) {
            return cnpj.substring(0, 2) + "." + cnpj.substring(2, 5) + "." +
                   cnpj.substring(5, 8) + "/" + cnpj.substring(8, 12) + "-" + cnpj.substring(12);
        }
        return cnpj;
    }
}
