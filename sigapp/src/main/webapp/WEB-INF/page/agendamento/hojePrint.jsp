<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>


<siga:pagina titulo="Agendadas Hoje Print"/>
<title>Imprime agendadas hoje</title>

<script language="javascript">
	alert('Quando terminar tecle BACKSPACE para retornar.');
</script>
<center><img src="/siga/imagens/brasao.gif"><br>
PODER JUDICI&Aacute;ÅRIO<br>
Justi&ccedil;a Federal do Rio de Janeiro
</center>
<div style="font-family:arial,calibri;">
	<center><h4>AGENDAMENTOS DE HOJE - SigaPP</h4></center>
<div style="position:absolute;left:10%;">
	DATA:
	<c:if test="${listAgendamentos != null}">
		${listAgendamentos.data_ag.toString().substring(9,11)}/
		${listAgendamentos.data_ag.toString().substring(6,8)}/
		${listAgendamentos.data_ag.toString().substring(1,5)}
	</c:if>
</div>
<br><br>
<div style="position:absolute;left:10%;">
 <table border="1" cellspacing="0" cellpadding="4">
 <tr>
 <th>HORA</th><th>SALA</th><th>PERICIADO</th><th>&Otilde;RG&Atilde;O</th><th>PERITO JU&Iacute;ZO</th><th>PERITO PARTE</th>
 </tr>
 <c:forEach items="${listAgendamentos}" var="ag">
	 <tr>
		<td>${ag.hora_ag.substring(0,2)}:${ag.hora_ag.substring(2,4)} </td>
		<td>${ag.localFk.local} &nbsp;</td>
		<td>${ag.periciado}</td>
		<td>${ag.orgao} &nbsp;</td>
		<td>
			<c:if test="${null == ag.perito_juizo}">
				Sem perito do ju&iacute;zo
			</c:if>
			<c:if test="${null != ag.perito_juizo}">
				<c:if test="${'' == ag.perito_juizo.trim()}">
					Sem perito do ju&iacute;zo.
				</c:if>
				<c:forEach items="${listPeritos}" var="prt"> 
					<c:if test="${ag.perito_juizo.trim() == prt.cpf_perito.trim()}">
						${prt.nome_perito}
					</c:if>	
		 		</c:forEach>
			</c:if>
			&nbsp;
		</td>
		<td>${ag.perito_parte} &nbsp;</td>
	 </tr>
 </c:forEach>
 </table>
</div>
</div>