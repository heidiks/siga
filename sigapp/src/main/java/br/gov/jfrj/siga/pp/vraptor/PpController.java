package br.gov.jfrj.siga.pp.vraptor;

import javax.persistence.EntityManager;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.caelum.vraptor.Result;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.pp.dao.PpDao;
import br.gov.jfrj.siga.vraptor.SigaController;
import br.gov.jfrj.siga.vraptor.SigaObjects;

public class PpController extends SigaController {

    HttpServletResponse response;
    ServletContext context;
    
    public PpController(HttpServletRequest request, Result result, CpDao dao, SigaObjects so, EntityManager em) {
        super(request, result, dao, so, em);
    }

    protected  CpDao dao() {
        return PpDao.getInstance();
    }
    
    protected void assertAcesso(String pathServico) throws AplicacaoException {
        super.assertAcesso("SR:Módulo de Serviços;" + pathServico);
    }
    
    protected  HttpServletResponse getResponse() {
        return response;
    }

    protected ServletContext getContext() {
        return context;
    }
    
    protected String getUsuarioMatricula() {
        return getCadastrante().getMatricula().toString();
    }
}
