package br.gov.jfrj.siga.pp.vraptor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.servlet.http.HttpServletRequest;

import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.model.ContextoPersistencia;
import br.gov.jfrj.siga.pp.dao.PpDao;
import br.gov.jfrj.siga.pp.models.Agendamentos;
import br.gov.jfrj.siga.pp.models.Locais;
import br.gov.jfrj.siga.pp.models.Peritos;
import br.gov.jfrj.siga.pp.models.UsuarioForum;
import br.gov.jfrj.siga.vraptor.SigaObjects;

@Resource
@Path("/app/agendamento")
public class AgendamentoController extends PpController {

    public AgendamentoController (HttpServletRequest request, Result result, CpDao dao, SigaObjects so, EntityManager em) {
        super(request, result, PpDao.getInstance(), so, em);
    }

    @Path("/excluir")
    public void excluir(String data) {
        // pega matricula do usuario do sistema
        String matriculaSessao = getCadastrante().getMatricula().toString();
        // pega a permiss√£o do usuario
        UsuarioForum objUsuario = UsuarioForum.AR.find(
                "matricula_usu =" + matriculaSessao).first();
        // verifica se tem permissao
        if (objUsuario != null) {
            List<Agendamentos> listAgendamentos = new ArrayList<Agendamentos>();
            // verifica se o formul√°rio submeteu alguma data
            if (data != null) {
                // Busca os agendamentos da data do formul√°rio
                listAgendamentos = Agendamentos.AR.find("data_ag = to_date('" + data + "','dd-mm-yy') order by hora_ag , cod_local")
                        .fetch();
                // busca os locais do forum do usuario
                List<Locais> listLocais = Locais.AR.find(
                        "cod_forum='" + objUsuario.getForumFk().getCod_forum() + "'")
                        .fetch();
                // Verifica se existe local naquele forum do usu√°rio
                if (listAgendamentos.size() != 0) {
                    // para cada agendamento, inlcui na lista a sala que √© do
                    // forum daquele usu·rio
                    List<Agendamentos> auxAgendamentos = new ArrayList<Agendamentos>();
                    for (Integer i = 0; i < listAgendamentos.size(); i++) {
                        // pega o agendamento
                        for (Integer ii = 0; ii < listLocais.size(); ii++) {
                            // varre os locais do forum
                            if (listAgendamentos.get(i).getLocalFk().getCod_local() == listLocais
                                    .get(ii).getCod_local()) {
                                // pertence √† lista de agendamentos do forum do
                                // usuario
                                auxAgendamentos
                                        .add((Agendamentos) listAgendamentos
                                                .get(i));
                            }
                        }
                    }
                    listLocais.clear();
                    listAgendamentos.clear();
                    listAgendamentos.addAll(auxAgendamentos);
                    auxAgendamentos.clear();
                }
            }
            if (listAgendamentos.size() != 0) {
                List <Peritos> listPeritos = new ArrayList<Peritos>();
                listPeritos = Peritos.AR.findAll();
                // excluir do arraylist, os peritos que n„o possuem agendamentos nesta data.
                result.include("listAgendamentos", listAgendamentos);
                result.include("listPeritos", listPeritos);
                
//              render(listAgendamentos, listPeritos);
            } else {
//              render();
            }
        } else {
            exception();
        }

    }


    @Path("/hoje")
    public void hoje() {
		// pega usu·rio do sistema
		String matriculaSessao = "";//cadastrante().getMatricula().toString();
		UsuarioForum objUsuario = UsuarioForum.AR.find(
				"matricula_usu =" + matriculaSessao).first();
		if (objUsuario != null) {
			// busca locais em funÁ„o da configuraÁ„o do usu·rio
			String criterioSalas="";
			List<Locais> listaDeSalas = Locais.AR.find("forumFk="+objUsuario.getForumFk().getCod_forum()).fetch();
			// monta string de criterio
			for(int j=0;j<listaDeSalas.size();j++){
				criterioSalas = criterioSalas + "'" +listaDeSalas.get(j).getCod_local().toString() + "'";
				if(j+1<listaDeSalas.size()){
					criterioSalas = criterioSalas + ",";
				}
			}
			SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
			Date hoje = new Date();
			String dtt = df.format(hoje);
			// busca agendamentos de hoje
			if(criterioSalas.equals("")){criterioSalas="''";}
			 List<Agendamentos> listAgendamentos = Agendamentos.AR.find("data_ag = to_date('" + dtt + "','dd-mm-yy') and localFk in("+criterioSalas+") order by hora_ag, cod_local").fetch();
			 if (listAgendamentos.size() != 0) {
				// busca as salas daquele forum
				List<Locais> listLocais = Locais.AR.find(
						"cod_forum='" + objUsuario.getForumFk().getCod_forum() + "'")
						.fetch();
				// lista auxiliar
				List<Agendamentos> auxAgendamentos = new ArrayList<Agendamentos>();
				for (int i = 0; i < listAgendamentos.size(); i++) {
					// varre listAgendamentos
					for (int ii = 0; ii < listLocais.size(); ii++) {
						// compara com cada local do forum do usu√°rio
						if (listAgendamentos.get(i).getLocalFk().getCod_local() == listLocais
								.get(ii).getCod_local()) {
							auxAgendamentos.add((Agendamentos) listAgendamentos
									.get(i));
						}
					}
				}
				List<Peritos> listPeritos = new ArrayList<Peritos>();
				listPeritos = (ArrayList<Peritos>) Peritos.AR.findAll();
				result.include("listAgendamentos", listAgendamentos);
				result.include("listPeritos", listPeritos);
			}
		} else {
		    //TODO: Excecoes("Usuario sem permissao" , null);
		}
    }

    @Path("/hojePrint/{frm_data_ag}")
    public void hojePrint(String frm_data_ag) {
		// pega usu·rio do sistema
		String matriculaSessao = "";//cadastrante().getMatricula().toString();
		UsuarioForum objUsuario = UsuarioForum.AR.find(
				"matricula_usu =" + matriculaSessao).first();
		if (objUsuario != null) {
			// busca locais em funÁ„o da configuraÁ„o do usu·rio
			String criterioSalas="";
			List<Locais> listaDeSalas = Locais.AR.find("forumFk="+objUsuario.getForumFk().getCod_forum()).fetch();
			// monta string de criterio
			for(int j=0;j<listaDeSalas.size();j++){
				criterioSalas = criterioSalas + "'" +listaDeSalas.get(j).getCod_local().toString() + "'";
				if(j+1<listaDeSalas.size()){
					criterioSalas = criterioSalas + ",";
				}
			}
			if (!frm_data_ag.isEmpty()){
				List listAgendamentos = (List) Agendamentos.AR.find("data_ag=to_date('"+frm_data_ag.substring(0,10)+"','dd-mm-yy') and localFk in("+criterioSalas+") order by hora_ag , localFk" ).fetch();
				List<Peritos> listPeritos = new ArrayList<Peritos>();
				listPeritos = Peritos.AR.findAll();
				result.include("listAgendamentos", listAgendamentos);
				result.include("listPeritos", listPeritos);
			}
		}else {
		    //TODO: Excecoes("Usuario sem permissao" , null);
		}
    }

    @Path("/print/{frm_data_ag}/{frm_sala_ag}/{frm_processo_ag}/{frm_periciado}")
    public void print(String frm_data_ag, String frm_sala_ag, String frm_processo_ag, String frm_periciado ){
		List listAgendamentos = (List) Agendamentos.AR.find("data_ag=to_date('"+frm_data_ag.substring(0,10)+"','yy-mm-dd') and localFk.cod_local='"+frm_sala_ag+"' and processo='"+frm_processo_ag+"' and periciado='"+frm_periciado+"'" ).fetch();
		if(frm_periciado.isEmpty()){
		    //TODO: Excecoes("Relatorio depende de nome de periciado preenchido para ser impresso." , null);
		}else if(frm_processo_ag.isEmpty()){
		    //TODO: Excecoes("Relatorio depende de numero de processo preenchido para ser impresso." , null);
		}else{
			List<Peritos> listPeritos = new ArrayList<Peritos>();
			listPeritos = Peritos.AR.findAll();
			result.include("frm_processo_ag", frm_processo_ag);
            result.include("listAgendamentos", listAgendamentos);
            result.include("listPeritos", listPeritos);
		}
    }

    @Path("/salaLista/{frm_cod_local}/{frm_data_ag}")
    public void salaLista(String frm_cod_local, String frm_data_ag){
		String local = "";
		String lotacaoSessao = "";//cadastrante().getLotacao().getSiglaLotacao();
		List<Locais> listSalas = new ArrayList();
		// pega usuario do sistema
		String matriculaSessao = "";//cadastrante().getMatricula().toString();
		UsuarioForum objUsuario = UsuarioForum.AR.find(
				"matricula_usu =" + matriculaSessao).first();
		if (objUsuario != null) {
			// Pega o usu·rio do sistema, e, busca os locais(salas) daquele
			// forum onde ele est·.
			listSalas = ((List) Locais.AR.find("forumFk='" + objUsuario.getForumFk().getCod_forum() + "' order by ordem_apresentacao ").fetch()); // isso n„o d· erro no caso de retorno vazio.
			List<Agendamentos> listAgendamentosMeusSala = new ArrayList();
			if(!(frm_cod_local==null||frm_data_ag.isEmpty())){
				//lista os agendamentos do dia, e, da lotaÁ„o do cadastrante
				listAgendamentosMeusSala = ((List) Agendamentos.AR.find("localFk.cod_local='" + frm_cod_local + "' and data_ag = to_date('" + frm_data_ag + "','yy-mm-dd') order by hora_ag").fetch());
				for(int i=0;i<listSalas.size();i++){
					if(listSalas.get(i).getCod_local().equals(frm_cod_local)){
						local = listSalas.get(i).getLocal();
					}
				}

			}
			List<Peritos> listPeritos = new ArrayList<Peritos>();
			listPeritos = Peritos.AR.findAll();
            result.include("local", local);
            result.include("listSalas", listSalas);
            result.include("listAgendamentosMeusSala", listAgendamentosMeusSala);
            result.include("lotacaoSessao", lotacaoSessao);
            result.include("listPeritos", listPeritos);
		} else {
		    //TODO: Excecoes("Usuario sem permissao" , null);
		}
    }

    @Path("/insert")
    public void insert(String frm_data_ag,
    			String frm_hora_ag, String frm_cod_local, String matricula,
    			String periciado, String perito_juizo, String perito_parte,
    			String orgao, String processo, Integer lote) {
		matricula = "";//cadastrante().getMatricula().toString();
		String resposta = "";
		Locais auxLocal = Locais.AR.findById(frm_cod_local);
		String hr;
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date parametro = df.parse(frm_data_ag);
			Agendamentos objAgendamento = new Agendamentos(parametro,
					frm_hora_ag, auxLocal, matricula, periciado, perito_juizo,
					perito_parte, processo, orgao);
			hr = frm_hora_ag;
			Agendamentos agendamentoEmConflito = null;
			// begin transaction, que, segundo o Da Rocha √© automatico no inicio da action
			String hrAux = hr.substring(0, 2);
			String minAux = hr.substring(3, 5);
			if (hr != null && (!hr.isEmpty())) {
				//verifica se tem conflito
				String horaPretendida = null;
				for(int i = 0; i < lote; i++){
					horaPretendida=hrAux+minAux;
					agendamentoEmConflito = Agendamentos.AR.find("perito_juizo like '"+perito_juizo.trim()+"%' and perito_juizo <> '-' and hora_ag='" +horaPretendida+ "' and data_ag=to_date('"+ frm_data_ag +"' , 'yy-mm-dd')"  ).first();
					if (agendamentoEmConflito!=null){
						//TODO: Excecoes("Perito nao disponivel no horario de " + agendamentoEmConflito.getHora_ag().substring(0,2)+ "h" + agendamentoEmConflito.getHora_ag().substring(2,4) + "min" , null );
					}
					minAux = String.valueOf(Integer.parseInt(minAux)
							+ auxLocal.getIntervalo_atendimento());
					if (Integer.parseInt(minAux) >= 60) {
						hrAux = String.valueOf(Integer.parseInt(hrAux) + 1);
						minAux = "00";
					}
				}
				// sloop
				hrAux = hr.substring(0, 2);
				minAux = hr.substring(3, 5);
				for (int i = 0; i < lote; i++) {
					objAgendamento.setHora_ag(hrAux + minAux);
					objAgendamento.save();
					ContextoPersistencia.em().flush();
					ContextoPersistencia.em().clear();
					minAux = String.valueOf(Integer.parseInt(minAux)
							+ auxLocal.getIntervalo_atendimento());
					if (Integer.parseInt(minAux) >= 60) {
						hrAux = String.valueOf(Integer.parseInt(hrAux) + 1);
						minAux = "00";
					}
					resposta = "Ok.";
				}
				// floop
				// end transaction, que, segundo o Da Rocha √© autom√°tico; no fim
				// da action
			} else {
				resposta = "N„o Ok.";
			}
		} catch (Exception e) {
			// rollback transaction, que segundo o Da Rocha √© autom√°tico; ocorre
			// em qualquer erro
			e.printStackTrace();
			String erro = e.getMessage();
			if (erro.substring(24, 52).equals("ConstraintViolationException")) {
				resposta = "N„o Ok. O lote n„o foi agendado.";
			} else {
				resposta = "N„o Ok. Verifique se preencheu todos os campos do agendamento.";
			}
		} finally {
		    result.include("resposta", resposta);
		    result.include("perito_juizo", perito_juizo);
		}
    }

    @Path("/delete")
    public void delete(Agendamentos formAgendamento, String cod_local) {
		String resultado = "";
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		String dtt = df.format(formAgendamento.getData_ag());
		try {
			Agendamentos ag = Agendamentos.AR.find(
					"hora_ag = '" + formAgendamento.getHora_ag()
							+ "' and localFk.cod_local='" + cod_local
							+ "' and data_ag = to_date('" + dtt
							+ "','dd/mm/yy')").first();
			//--------------------------
			String lotacaoSessao = getCadastrante().getLotacao().getIdLotacao().toString();
			String matricula_ag = ag.getMatricula();
			DpPessoa p = (DpPessoa) DpPessoa.AR.find(
					"orgaoUsuario.idOrgaoUsu = "
							+ getCadastrante().getOrgaoUsuario().getIdOrgaoUsu()
							+ " and dataFimPessoa is null and matricula='"
							+ matricula_ag + "'").first();
			String lotacao_ag = p.getLotacao().getIdLotacao().toString();
			//System.out.println(p.getNomePessoa().toString()+ "Lotado em:" + lotacao_ag);
			if(lotacaoSessao.trim().equals(lotacao_ag.trim())){
			//--------------------------
			ag.delete();
			resultado = "Ok.";
			}else{
			    //TODO: Excecoes("Esse agendamento nao pode ser deletado; pertence a outra vara." , null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			resultado = "N„o Ok.";
		} finally {
		    result.include("resultado", resultado);
		    result.include("dtt", dtt);
		}
    }

    @Path("/atualiza")
    public void atualiza(String cod_sala, String data_ag, String hora_ag) {
		// pega usuario do sistema
		String matriculaSessao = getCadastrante().getMatricula().toString();
		UsuarioForum objUsuario = UsuarioForum.AR.find(
				"matricula_usu =" + matriculaSessao).first();
		if (objUsuario != null) {
			// Pega o usu·rio do sistema, e, busca os locais(salas) daquele
			// forum onde ele est·.
			Locais objSala = Locais.AR.find("cod_forum='" + objUsuario.getForumFk().getCod_forum() + "' and cod_local='" + cod_sala + "'").first(); // isso n„o d· erro no caso de retorno vazio?
			String sala_ag = objSala.getLocal();
			String lotacaoSessao = getCadastrante().getLotacao().getIdLotacao().toString();
			//System.out.println(lotacaoSessao);
			Agendamentos objAgendamento = Agendamentos.AR.find("cod_local='" + cod_sala + "' and data_ag = to_date('" + data_ag + "','yy-mm-dd') and hora_ag='" + hora_ag + "'").first();
			String matricula_ag = objAgendamento.getMatricula();
			DpPessoa p = (DpPessoa) DpPessoa.AR.find("orgaoUsuario.idOrgaoUsu = " + getCadastrante().getOrgaoUsuario().getIdOrgaoUsu() + " and dataFimPessoa is null and matricula='"	+ matricula_ag + "'").first();
			String lotacao_ag = p.getLotacao().getIdLotacao().toString();
			//System.out.println(p.getNomePessoa().toString()+ "Lotado em:" + lotacao_ag);
			if(lotacaoSessao.trim().equals(lotacao_ag.trim())){
				String nome_perito_juizo="";
				String processo = objAgendamento.getProcesso();
				String periciado = objAgendamento.getPericiado();
				String perito_juizo = objAgendamento.getPerito_juizo();
				String perito_parte = objAgendamento.getPerito_parte();
				String orgao_julgador = objAgendamento.getOrgao();
				ContextoPersistencia.em().flush();
				List<Peritos> listPeritos = new ArrayList<Peritos>();
				listPeritos = Peritos.AR.find("1=1 order by nome_perito").fetch();
				if(perito_juizo==null){perito_juizo="-";}
				if(!perito_juizo.trim().equals("-")){
					for(int i=0;i<listPeritos.size();i++){
						if(listPeritos.get(i).getCpf_perito().trim().equals(perito_juizo.trim())){
							nome_perito_juizo = listPeritos.get(i).getNome_perito();
						}
					}
				}
				result.include("sala_ag", sala_ag);
				result.include("cod_sala", cod_sala);
				result.include("data_ag", data_ag);
                result.include("hora_ag", hora_ag);
                result.include("processo", processo);
                result.include("periciado", periciado);
                result.include("perito_juizo", perito_juizo);
                result.include("nome_perito_juizo", nome_perito_juizo);
                result.include("perito_parte", perito_parte);
                result.include("orgao_julgador", orgao_julgador);
                result.include("listPeritos", listPeritos);
			}else{
			    //TODO: Excecoes("Esse agendamento nao pode ser modificado; pertence a outra vara." , null);
			}
		} else {
		    //TODO: Excecoes("Usuario sem permissao" , null);
		}
    }

    @Path("/update")
    public void update(String cod_sala, String data_ag, String hora_ag, String processo, String periciado, String perito_juizo, String perito_parte, String orgao_ag){
		String resultado = "";
		Agendamentos agendamentoEmConflito = null;
		try{
			// Devo verificar agendamento conflitante, antes de fazer o UPDATE.
			System.out.println(perito_juizo.trim()+""+data_ag+" "+hora_ag.substring(0,2)+hora_ag.substring(3,5));
			agendamentoEmConflito = Agendamentos.AR.find("perito_juizo like '"+perito_juizo.trim()+"%' and perito_juizo <> '-' and hora_ag='" +hora_ag.substring(0,2)+hora_ag.substring(3,5)+ "' and data_ag=to_date('"+ data_ag +"', 'dd-mm-yy' ) and localFk<>'"+cod_sala+"'").first();

			if (agendamentoEmConflito!=null){
				//TODO: Excecoes("Perito nao disponivel no horario de " + agendamentoEmConflito.getHora_ag().substring(0,2) +"h"+agendamentoEmConflito.getHora_ag().substring(2,4)+"min" , " agendamento_excluir?frm_data_ag="+data_ag);
			}
			ContextoPersistencia.em().createQuery("update Agendamentos set processo = '"+ processo +"', "+ "periciado='"+ periciado +"', perito_juizo='"+ perito_juizo.trim() +"', perito_parte='"+perito_parte+"', orgao='"+orgao_ag+"' where cod_local='"+cod_sala+"' and  hora_ag='"+hora_ag.substring(0,2)+hora_ag.substring(3,5)+"' and data_ag=to_date('"+data_ag+"','dd-mm-yy')").executeUpdate();
			ContextoPersistencia.em().flush();
			Agendamentos objAgendamento = (Agendamentos) Agendamentos.AR.find("cod_local='"+cod_sala+"' and  hora_ag='"+hora_ag.substring(0,2)+hora_ag.substring(3,5)+"' and data_ag=to_date('"+data_ag+"','dd-mm-yy')" ).first();
			if(objAgendamento==null){
				resultado = "Nao Ok.";
			}else{
				resultado = "Ok.";
			}
		}catch(PersistenceException e){
			e.printStackTrace();
			resultado = "N„o Ok.";
		}catch(Exception ee){
			ee.printStackTrace();
			resultado = "N„o Ok.";
		}finally{
		    result.include("resultado", resultado);
		    result.include("data_ag", data_ag);
		}
    }

    @Path("/imprime/{frm_data_ag}")
    public void imprime(String frm_data_ag){
		String matriculaSessao = "";//cadastrante().getMatricula().toString();
		String lotacaoSessao = "";//cadastrante().getLotacao().getSiglaLotacao();
		List<Agendamentos> listAgendamentos = new ArrayList<Agendamentos>();
		UsuarioForum objUsuario = UsuarioForum.AR.find(
				"matricula_usu =" + matriculaSessao).first();
    	if (objUsuario != null) {
    		if(frm_data_ag==null){
    			frm_data_ag = "";
    		}else{
    			listAgendamentos = Agendamentos.AR.find( "data_ag=to_date('" + frm_data_ag + "','dd-mm-yy') order by hora_ag, cod_local" ).fetch();
    			DpPessoa p = null;
    			// deleta os agendamentos de outros org„os
    			for(int i=0;i<listAgendamentos.size();i++){
    				 p = null;//(DpPessoa) DpPessoa.find("orgaoUsuario.idOrgaoUsu = " + cadastrante().getOrgaoUsuario().getIdOrgaoUsu()
    							//		+ " and dataFimPessoa is null and matricula='"+ listAgendamentos.get(i).matricula + "'").first();
    				if(lotacaoSessao.trim().equals(p.getLotacao().getSiglaLotacao().toString().trim())){
    					System.out.println("");
    				}else{
    					listAgendamentos.remove(i);
    					i--;
    				}
    			}
    		}
    		List<Peritos> listPeritos = new ArrayList<Peritos>();
    		listPeritos = Peritos.AR.findAll();
            result.include("listAgendamentos", listAgendamentos);
            result.include("listPeritos", listPeritos);
    	}else{
    	    //TODO: Excecoes("Usuario sem permissao" , null);
    	}
   	}
}
