package br.gov.jfrj.siga.pp.vraptor;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.pp.dao.PpDao;
import br.gov.jfrj.siga.pp.models.Agendamentos;
import br.gov.jfrj.siga.pp.models.Locais;
import br.gov.jfrj.siga.pp.models.Peritos;
import br.gov.jfrj.siga.pp.models.UsuarioForum;
import br.gov.jfrj.siga.vraptor.SigaObjects;


@Resource
@Path("/app/agendamento")
public class AgendamentoController extends PpController {

    public AgendamentoController(HttpServletRequest request, Result result,
            CpDao dao, SigaObjects so, EntityManager em) {
        super(request, result, PpDao.getInstance(), so, em);
    }

    @Path("/excluir")
    public void excluir(String data) {
        // pega matricula do usuario do sistema
        String matriculaSessao = getCadastrante().getMatricula().toString();
        // pega a permissão do usuario
        UsuarioForum objUsuario = UsuarioForum.AR.find(
                "matricula_usu =" + matriculaSessao).first();
        // verifica se tem permissao
        if (objUsuario != null) {
            List<Agendamentos> listAgendamentos = new ArrayList<Agendamentos>();
            // verifica se o formulário submeteu alguma data
            if (data != null) {
                // Busca os agendamentos da data do formulário
                listAgendamentos = Agendamentos.AR.find("data_ag = to_date('" + data + "','dd-mm-yy') order by hora_ag , cod_local")
                        .fetch();
                // busca os locais do forum do usuario
                List<Locais> listLocais = Locais.AR.find(
                        "cod_forum='" + objUsuario.getForumFk().getCod_forum() + "'")
                        .fetch();
                // Verifica se existe local naquele forum do usuário
                if (listAgendamentos.size() != 0) {
                    // para cada agendamento, inlcui na lista a sala que é do
                    // forum daquele usu�rio
                    List<Agendamentos> auxAgendamentos = new ArrayList<Agendamentos>();
                    for (Integer i = 0; i < listAgendamentos.size(); i++) {
                        // pega o agendamento
                        for (Integer ii = 0; ii < listLocais.size(); ii++) {
                            // varre os locais do forum
                            if (listAgendamentos.get(i).getLocalFk().getCod_local() == listLocais
                                    .get(ii).getCod_local()) {
                                // pertence à lista de agendamentos do forum do
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
                // excluir do arraylist, os peritos que n�o possuem agendamentos nesta data.
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

}
