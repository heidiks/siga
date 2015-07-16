package br.gov.jfrj.siga.pp.vraptor;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import br.com.caelum.vraptor.Result;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.model.ContextoPersistencia;
import br.gov.jfrj.siga.pp.models.UsuarioForum;
import br.gov.jfrj.siga.vraptor.SigaObjects;

public class PermissaoController extends PpController {

    public PermissaoController(HttpServletRequest request, Result result, CpDao dao, SigaObjects so, EntityManager em) {
        super(request, result, dao, so, em);
        // TODO Auto-generated constructor stub
    }

    public static void permissao_exclui(String matricula_proibida){
		String mensagem = "";
		// pega usuário do sistema
		String matriculaSessao = "";//cadastrante().getMatricula().toString();
		String lotacaoSessao = "";//cadastrante().getLotacao().getSiglaLotacao();
		UsuarioForum objUsuario = UsuarioForum.AR.find("matricula_usu = '"+matriculaSessao+"'").first();
		if ((objUsuario !=null) && ( (lotacaoSessao.trim().equals("CSIS")||lotacaoSessao.trim().equals("SESIA")) )){ //pode excluir a permissão
			List<UsuarioForum> listPermitidos = new ArrayList<UsuarioForum>();
			if((matricula_proibida!=null) && (!matricula_proibida.isEmpty())){ // deleta permissao
				try{
					UsuarioForum objUsuarioProibido = UsuarioForum.AR.find("matricula_usu='"+matricula_proibida+"'").first();
					objUsuarioProibido.delete();
					ContextoPersistencia.em().flush();
					ContextoPersistencia.em().clear();
					mensagem = "Ok";
				}catch(Exception e){
					e.printStackTrace();
					mensagem = "Nao Ok";
				}finally{
//					render(mensagem);
				}
			 } else{ // lista permitidos
				try{
					 listPermitidos = (List) UsuarioForum.AR.find(" order by nome_usu ").fetch(); // isso não dá erro no caso de retorno vazio.
				}catch(Exception e){
					e.printStackTrace();
				}
				finally{
//					render(listPermitidos);
				}
			}
		}
    }

    public static void perito_incluir(){
		// pega usuário do sistema
		String matriculaSessao = "";//cadastrante().getMatricula().toString();
		String lotacaoSessao = "";//cadastrante().getLotacao().getSiglaLotacao();
		UsuarioForum objUsuario = UsuarioForum.AR.find("matricula_usu = '"+matriculaSessao+"'").first();
		if ((objUsuario !=null)){ //pode incluir perito
//			render();
		}else{
			Excecoes("Usuario sem permissao." , null);
		}
    }



}
