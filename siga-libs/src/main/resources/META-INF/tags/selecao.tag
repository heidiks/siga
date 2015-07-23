<%@ tag body-content="empty"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="ww" uri="/webwork"%>
<%@ attribute name="titulo" required="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ attribute name="propriedade"%>
<%@ attribute name="reler" required="false"%>
<%@ attribute name="relertab" required="false"%>
<%@ attribute name="desativar" required="false"%>
<%@ attribute name="buscar" required="false"%>
<%@ attribute name="ocultardescricao" required="false"%>
<%@ attribute name="tema"%>
<%@ attribute name="modulo" required="false"%>
<%@ attribute name="tipo" required="false"%>
<%@ attribute name="idAjax" required="false"%>
<%@ attribute name="paramList" required="false"%>
<%@ attribute name="idInicial" required="false"%>
<%@ attribute name="siglaInicial" required="false"%>
<%@ attribute name="descricaoInicial" required="false"%>
<%@ attribute name="inputName" required="false"%>
<%@ attribute name="urlAcao" required="false"%>
<%@ attribute name="urlSelecionar" required="false"%>
<!-- A lista de par -->

<c:forEach var="parametro" items="${fn:split(paramList,';')}">
	<c:set var="p2" value="${fn:split(parametro,'=')}" />
	<c:if test="${not empty p2 and not empty p2[0]}">
		<c:set var="selecaoParams" value="${urlParams}&${p2[0]}=${p2[1]}" />
	</c:if>
</c:forEach>

<c:set var="propriedadeSel" value="${propriedade}Sel" />

<c:choose>
	<c:when test="${empty tipo}">
		<c:set var="acaoBusca" value="${requestScope[propriedadeSel].acaoBusca}" />
		<c:set var="tipoSel" value="" scope="request" />
	</c:when>
	<c:otherwise>
		<c:set var="acaoBusca" value="/${tipo}" />
		<c:set var="tipoSel" value="_${tipo}" scope="request" />
	</c:otherwise>
</c:choose>

<c:set var="propriedadeTipoSel" value="${propriedade}${tipoSel}Sel" />
<c:choose>
	<c:when test="${empty inputName}">
		<c:set var="inputNameTipoSel" value="${propriedadeTipoSel}" />
	</c:when>
	<c:otherwise>
		<c:set var="inputNameTipoSel" value="${inputName}${tipoSel}Sel" />
	</c:otherwise>
</c:choose>

<c:choose>
	<c:when test="${empty urlAcao}">
		<c:set var="urlBuscar" value="${acaoBusca}/buscar.action" />
	</c:when>
	<c:otherwise>
		<c:set var="urlBuscar" value="/app${acaoBusca}/${urlAcao}" />
	</c:otherwise>
</c:choose>

<c:set var="tam" value="${requestScope[propriedadeSel].tamanho}" />

<c:set var="larguraPopup" value="600" />
<c:set var="alturaPopup" value="400" />


<script type="text/javascript">

self.retorna_${propriedade}${tipoSel} = function(id, sigla, descricao) {
    try {
		newwindow_${propriedade}.close();
    } catch (E) {
    } finally {
    }
    
	document.getElementsByName('${inputNameTipoSel}.id')[0].value = id;
	
	<c:if test="${ocultardescricao != 'sim'}">
		try {
			document.getElementsByName('${inputNameTipoSel}.descricao')[0].value = descricao;
			document.getElementById('${propriedade}${tipoSel}SelSpan').innerHTML = descricao;
		} catch (E) {
		}
	</c:if>
	
	document.getElementsByName('${inputNameTipoSel}.sigla')[0].value = sigla;
	
	<c:if test="${reler == 'sim'}">
		document.getElementsByName('req${inputNameTipoSel}')[0].value = "sim";
		document.getElementById('alterouSel').value='${propriedade}';
		sbmt();
	</c:if>
	
	<c:if test="${reler == 'ajax'}">
		sbmt('${empty idAjax ? propriedade : idAjax}');
	</c:if> 
}
 
 
<c:choose>
	<c:when test="${empty modulo}">
		<c:set var="urlPrefix" value="/" />
	</c:when>
	<c:otherwise> 
		<c:set var="urlPrefix" value="${modulo}" />
	</c:otherwise>
</c:choose>

self.newwindow_${propriedade} = '';
self.popitup_${propriedade}${tipoSel} = function(sigla) {

	var url = '/${urlPrefix}${urlBuscar}?propriedade=${propriedade}${tipoSel}&sigla='+encodeURI(sigla) +'${selecaoParams}';
		
	if (!newwindow_${propriedade}.closed && newwindow_${propriedade}.location) {
		newwindow_${propriedade}.location.href = url;
	} else {
	
		var popW;
		var popH;
		
		<c:choose>
			<c:when test="${tam eq 'grande'}">
				 popW = screen.width*0.75;
				 popH = screen.height*0.75;
			</c:when>
			<c:otherwise>
				 popW = ${larguraPopup};
				 popH = ${alturaPopup};	
			</c:otherwise>
		</c:choose>
			var winleft = (screen.width - popW) / 2;
			var winUp = (screen.height - popH) / 2;	
		winProp = 'width='+popW+',height='+popH+',left='+winleft+',top='+winUp+',scrollbars=yes,resizable'
		newwindow_${propriedade}=window.open(url,'${propriedade}${tipoSel}',winProp);
	}
	newwindow_${propriedade}.opener = self;
	
	if (window.focus) {
		newwindow_${propriedade}.focus()
	}
	return false;
}

self.resposta_ajax_${propriedade}${tipoSel} = function(response, d1, d2, d3) {
	var sigla = document.getElementsByName('${inputNameTipoSel}.sigla')[0].value;
    var data = response.split(';');
    if (data[0] == '1')
	    return retorna_${propriedade}${tipoSel}(data[1], data[2], data[3]);
    retorna_${propriedade}${tipoSel}('', '', '');
    
    <c:choose>
		<c:when test="${buscar != 'nao'}">
			return popitup_${propriedade}${tipoSel}(sigla);
		</c:when>
		<c:otherwise>
			return;
		</c:otherwise>
	</c:choose>
}

self.ajax_${propriedade}${tipoSel} = function() {
	var sigla = document.getElementsByName('${inputNameTipoSel}.sigla')[0].value;
	if (sigla == '') {
		return retorna_${propriedade}${tipoSel}('', '', '');
	}
	<c:choose>
		<c:when test="${empty urlSelecionar}">
			var url = '/${urlPrefix}${acaoBusca}/selecionar.action?propriedade=${propriedade}${tipoSel}'+'${selecaoParams}';
		</c:when>
		<c:otherwise>
			var url = '/${urlPrefix}/app${acaoBusca}/${urlSelecionar}?propriedade=${propriedade}${tipoSel}'+'${selecaoParams}';
		</c:otherwise>
	</c:choose>
	url = url + '&sigla=' + sigla;
	PassAjaxResponseToFunction(url, 'resposta_ajax_${propriedade}${tipoSel}', false);
}

</script>

<c:if test="${tema != 'simple'}">
	<tr>
	<td>${titulo}</td>
	<td>
</c:if>
<c:choose>
	<c:when test="${desativar == 'sim'}">
		<c:set var="disabledTxt" value="disabled" />
		<c:set var="disabledBtn" value="disabled" />
	</c:when>
</c:choose>

<input type="hidden" name="req${inputNameTipoSel}"  />
<input type="hidden" name="alterouSel" value="" id="alterouSel" />
<input type="hidden" name="${inputNameTipoSel}.id" value="<c:out value="${requestScope[propriedadeTipoSel].id}"/>" id="formulario_${inputNameTipoSel}_id"/>
<input type="hidden" name="${inputNameTipoSel}.descricao" value="<c:out value="${requestScope[propriedadeTipoSel].descricao}"/>" id="formulario_${inputNameTipoSel}_descricao"/>
<input type="hidden" name="${inputNameTipoSel}.buscar" value="<c:out value="${requestScope[propriedadeTipoSel].buscar}"/>" id="formulario_${inputNameTipoSel}_buscar"/>
<input type="text" name="${inputNameTipoSel}.sigla" value="<c:out value="${requestScope[propriedadeTipoSel].sigla}"/>" id="formulario_${inputNameTipoSel}_sigla" 
	onkeypress="return handleEnter(this, event)"
	onblur="javascript: ajax_${propriedade}${tipoSel}();" size="25"
	"${disabledTxt}" />	
	
<c:if test="${buscar != 'nao'}">
	<input type="button" id="${propriedade}${tipoSel}SelButton" value="..."
		onclick="javascript: popitup_${propriedade}${tipoSel}('');"
		${disabledBtn} theme="simple">
</c:if>

<c:if test="${ocultardescricao != 'sim'}">
	<span id="${propriedade}${tipoSel}SelSpan">
		<c:out value="${requestScope[propriedadeTipoSel].descricao}" />
	</span>
</c:if>

<c:if test="${not empty tipo}">
	<c:choose>
		<c:when test="${empty idInicial}">
			<c:set var="idSubst" value="${requestScope[propriedadeTipoSel].id}" />
		</c:when>
		<c:otherwise>
			<c:set var="idSubst" value="${idInicial}" />
		</c:otherwise>
	</c:choose>
	<c:choose>
		<c:when test="${empty siglaInicial}">
			<c:set var="siglaSubst" value="${requestScope[propriedadeTipoSel].sigla}" />
		</c:when>
		<c:otherwise>
			<c:set var="siglaSubst" value="${siglaInicial}" />
		</c:otherwise>
	</c:choose>
	<c:choose>
		<c:when test="${empty descricaoInicial}">
			<c:set var="descricaoSubst"  value="${requestScope[propriedadeTipoSel].descricao}" />
		</c:when>
		<c:otherwise>
			<c:set var="descricaoSubst" value="${descricaoInicial}" />
		</c:otherwise>
	</c:choose>
	<script type="text/javascript">
		document.getElementsByName('${inputNameTipoSel}.id')[0].value = '${idSubst}';
		document.getElementsByName('${inputNameTipoSel}.sigla')[0].value = '${siglaSubst}';
		document.getElementsByName('${inputNameTipoSel}.descricao')[0].value = "${descricaoSubst}";
		<c:if test="${ocultardescricao != 'sim'}">
			document.getElementById('${propriedade}${tipoSel}SelSpan').innerHTML = "${descricaoSubst}";
		</c:if>
	</script>
</c:if>

<c:if test="${tema != 'simple'}">
	</td>
	</tr>
</c:if>
