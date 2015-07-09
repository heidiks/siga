package br.gov.jfrj.siga.pp.vraptor;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.vraptor.SigaObjects;

@Resource
public class PrincipalController extends PpController {
    public PrincipalController(HttpServletRequest request, Result result, CpDao dao, SigaObjects so, EntityManager em) {
        super(request, result, dao, so, em);
    }

    @Path("/app/principal")
    public void principal() throws Exception {
        // Principal
    }
}