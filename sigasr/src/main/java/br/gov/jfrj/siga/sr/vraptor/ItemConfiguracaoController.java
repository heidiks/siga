package br.gov.jfrj.siga.sr.vraptor;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import play.data.validation.Validation;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;
import br.gov.jfrj.siga.cp.CpComplexo;
import br.gov.jfrj.siga.cp.CpUnidadeMedida;
import br.gov.jfrj.siga.cp.model.CpPerfilSelecao;
import br.gov.jfrj.siga.cp.model.DpCargoSelecao;
import br.gov.jfrj.siga.cp.model.DpFuncaoConfiancaSelecao;
import br.gov.jfrj.siga.cp.model.DpLotacaoSelecao;
import br.gov.jfrj.siga.cp.model.DpPessoaSelecao;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.sr.model.SrConfiguracao;
import br.gov.jfrj.siga.sr.model.SrItemConfiguracao;
import br.gov.jfrj.siga.sr.model.SrLista;
import br.gov.jfrj.siga.sr.model.SrPesquisa;
import br.gov.jfrj.siga.sr.model.SrSolicitacao;
import br.gov.jfrj.siga.sr.model.vo.SelecionavelVO;
import br.gov.jfrj.siga.sr.validator.SrValidator;
import br.gov.jfrj.siga.vraptor.SigaObjects;

@Resource
@Path("app/itemConfiguracao")
public class ItemConfiguracaoController extends SrController {
	
	private static final String MOSTRAR_DESATIVADOS = "mostrarDesativados";
	private static final String ITENS = "itens";
	private static final String ORGAOS = "orgaos";
	private static final String LOCAIS = "locais";
	private static final String UNIDADES_MEDIDA = "unidadesMedida";
	private static final String PESQUISA_SATISFACAO = "pesquisaSatisfacao";

	public ItemConfiguracaoController(HttpServletRequest request, Result result, CpDao dao, SigaObjects so, EntityManager em, SrValidator srValidator) {
		super(request, result, dao, so, em, srValidator);
	}
	
	//@AssertAcesso(ADM_ADMINISTRAR)
	@SuppressWarnings("unchecked")
	@Path("/listar/{mostrarDesativados}")
	public void listar(boolean mostrarDesativados) throws Exception {
		List<SrItemConfiguracao> itens = SrItemConfiguracao.listar(mostrarDesativados);
		List<CpOrgaoUsuario> orgaos = CpOrgaoUsuario.AR.em().createQuery("from CpOrgaoUsuario").getResultList();
		List<CpComplexo> locais = CpComplexo.AR.all().fetch();
		List<CpUnidadeMedida> unidadesMedida = CpDao.getInstance().listarUnidadesMedida();
		List<SrPesquisa> pesquisaSatisfacao = SrPesquisa.AR.find("hisDtFim is null").fetch();
		List<SrConfiguracao> designacoes = new ArrayList<SrConfiguracao>();

		result.include(ITENS, itens);
		result.include(ORGAOS, orgaos);
		result.include(LOCAIS, locais);
		result.include(UNIDADES_MEDIDA, unidadesMedida);
		result.include(PESQUISA_SATISFACAO, pesquisaSatisfacao);
		result.include(MOSTRAR_DESATIVADOS, mostrarDesativados);
		
		// Includes para os componentes de pessoaLotaSelecao de Gestor e Fator de Multiplicação
		result.include("gestorPessoaSel", new DpPessoaSelecao());
		result.include("gestorLotacaoSel", new DpLotacaoSelecao());
		result.include("fatorPessoaSel", new DpPessoaSelecao());
		result.include("fatorLotacaoSel", new DpLotacaoSelecao());
		
		// includes para o componente de Designação
		result.include("modoExibicao", "item");
		result.include("designacoes", designacoes);
		result.include("dpPessoaSel", new DpPessoaSelecao());
		result.include("atendenteSel", new DpLotacaoSelecao());
		result.include("lotacaoSel", new DpLotacaoSelecao());
		result.include("funcaoConfiancaSel", new DpFuncaoConfiancaSelecao());
		result.include("cargoSel", new DpCargoSelecao());
		result.include("cpGrupoSel", new CpPerfilSelecao());
		result.include("itemConfiguracao", new SelecionavelVO(null,null));
		result.include("acao", new SelecionavelVO(null,null));
	}
	
	public void desativar(Long id, boolean mostrarDesativados) throws Exception {
		SrLista lista = SrLista.AR.findById(id);
		lista.finalizar();

		result.include("lista", lista.toJson());
	}

	public void reativar(Long id, boolean mostrarDesativados) throws Exception {
		SrLista lista = SrLista.AR.findById(id);
		lista.salvar();
		
		result.include("lista", lista.toJson());
	}
	
	public void gravar(SrLista lista) throws Exception {
		lista.lotaCadastrante = getLotaTitular();
		validarFormEditarLista(lista);
		lista.salvar();
		
		result.include("lista", lista.toJson());
	}
	
	
	@Get
	@Path("/{id}/designacoes")
	public void buscarDesignacoes(Long id) throws Exception {
		List<SrConfiguracao> designacoes;

		if (id != null) {
			SrItemConfiguracao itemConfiguracao = SrItemConfiguracao.AR.findById(id);
			designacoes = new ArrayList<SrConfiguracao>(itemConfiguracao.getDesignacoesAtivas());
			designacoes.addAll(itemConfiguracao.getDesignacoesPai());
		}
		else
			designacoes = new ArrayList<SrConfiguracao>();

		result.use(Results.json())
		.from(SrConfiguracao.convertToJSon(designacoes))
	    .serialize();
	}
	
	private void validarFormEditarLista(SrLista lista) {
		if (lista.nomeLista == null || lista.nomeLista.trim().equals("")) {
			Validation.addError("lista.nomeLista","Nome da Lista n�o informados");
		}

		if (Validation.hasErrors()) {
			enviarErroValidacao();
		}
	}
	
	//@AssertAcesso(ADM_ADMINISTRAR)
	public void gravarDesignacaoItem(SrConfiguracao designacao, Long idItemConfiguracao) throws Exception {
		designacao.salvarComoDesignacao();
		designacao.refresh();

		SrItemConfiguracao itemConfiguracao = SrItemConfiguracao.AR.findById(idItemConfiguracao);
		itemConfiguracao.adicionarDesignacao(designacao);
		
		result.use(Results.json())
			.from(designacao.getSrConfiguracaoJson(itemConfiguracao))
		    .serialize();
	}
	
	@Path("/buscar")
	public void buscar(String sigla, String nome, String siglaItemConfiguracao, 
			String tituloItemConfiguracao, SrSolicitacao sol, String propriedade) {
		List<SrItemConfiguracao> items = null;
		SrItemConfiguracao filtro = new SrItemConfiguracao();
		try {
			filtro.setSiglaItemConfiguracao(siglaItemConfiguracao);
			filtro.setTituloItemConfiguracao(tituloItemConfiguracao);
			
			if (sigla != null && !sigla.trim().equals(""))
				filtro.setSigla(sigla);
			
			if (buscaPorItens(sol)) {
				items = filtro.buscar(sol.getItensDisponiveis());
			}else{
				items = filtro.buscar();
			}
		} catch (Exception e) {
			items = new ArrayList<SrItemConfiguracao>();
		}
		if (sol.getLocal() == null ){
			sol.setLocal(new CpComplexo());
		}
		if (sol.getSolicitante() == null ){
			sol.setSolicitante(new DpPessoa());
		}
		result.include("items", items);
		result.include("sol", sol);
		result.include("filtro", filtro);
		result.include("nome", nome);
		result.include("propriedade", propriedade);
	}

	private boolean buscaPorItens(SrSolicitacao sol) {
		return sol != null && 
				((sol.getSolicitante() != null && sol.getSolicitante().getId() != null) || 
				 (sol.getLocal() != null && sol.getLocal().getIdComplexo() != null));
	}
	
	@Path("/selecionar")
	public void selecionar(String sigla, SrSolicitacao sol) throws Exception {
		SrItemConfiguracao sel = new SrItemConfiguracao().selecionar(sigla, sol.getItensDisponiveis());
		result.include("selecionar", sel);			
		result.use(Results.status()).ok();
	}

}