<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<siga:pagina titulo="Cancelar Agendamento"/>

<title>Cancelar Agendamento</title>

<script src="public/javascripts/jquery-1.9.1.js"></script>
<script src="public/javascripts/jquery-ui.js"></script>
<script type="text/javascript" language="Javascript1.1">

 $(function () {
	 
	 $.datepicker.setDefaults({monthNames: [ "Janeiro", "Fevereiro", "Mar&ccedil;o", "Abril", "Maio", "Junho", "Julho", "Agosto",  "Setembro", "Outubro", "Novembro", "Dezembro" ] ,
		  dayNamesMin: [ "Dom", "Seg", "Ter", "Qua", "Qui", "Sex", "Sab" ],
		  dateFormat: "dd-mm-yy",
		  duration: "slow",
		  prevText: "M&ecirc;s anterior",
		  nextText: "Pr&oacute;ximo m&ecirc;s",
		  showOn: "both",
		  buttonText: "...",
		  minDate: new Date()        					  
		  });  	  
  	
  	$( "#frm_data_ag01" ).datepicker();
	
 } );
 
</script>
<br>
<br>
 <table class="ui-tabs"  align="center" style="font-size:100%;">
 <tr bgcolor="Silver">
  <th>&nbsp; Data &nbsp; </th>
  <th>&nbsp; Hora &nbsp; </th>
  <th>&nbsp; Sala/Local &nbsp;</th>
  <th>&nbsp; Periciado &nbsp;</th>
  <th>&nbsp; &Oacute;rg&atilde;o &nbsp;</th>
  <th>&nbsp; Processo &nbsp;</th>
  <th>&nbsp; Perito Ju&iacute;zo &nbsp;</th>
  <th>&nbsp; Perito Parte &nbsp;</th>
  <th></th>
  <th></th>
 </tr>

<c:forEach items="${listAgendamentos}" var="ag">
	<tr class="ui-button-icon-only"
		<c:if test ="${!b}">
			bgcolor="#dddddd"
		</c:if>
	/>
	<c:set var="b" value="${!b}"/>
	<td>&nbsp; ${ag.data_ag.toString().substring(8,10)}-${ag.data_ag.toString().substring(5,7)}-${ag.data_ag.toString().substring(0,4)}</td>
	<td>&nbsp; ${ag.hora_ag.substring(0,2)}:${ag.hora_ag.substring(2,4)}</td>
	<td>&nbsp; ${ag.localFk.local}</td>
	<td>&nbsp; ${ag.periciado}</td>
	<td>&nbsp; ${ag.orgao}</td>
	<td>&nbsp; ${ag.processo}</td>
	<td>&nbsp;
	<c:if test="${ag.perito_juizo==null}">
		Sem perito do ju&iacute;zo
	</c:if>
	<c:if test="${ag.perito_juizo!=null}">
		<c:if test="${ag.perito_juizo.trim()==""}">
			Sem perito do ju&iacute;zo.
		</c:if>
		<c:forEach items="${listPeritos}" var="as">
			<c:if test="${ag.perito_juizo.trim()==prt.cpf_perito.trim()}">
				${prt.nome_perito}
			</c:if>
	 	</c:forEach>
	</c:if> 
		</td>	
	<td>&nbsp; ${ag.perito_parte}</td>

	<td> &nbsp;
		
		<form name="agendamento_deleta01" action="${linkTo[AgendamentoController].delete}" method="post" enctype="multipart/form-data">
			<img  src="/siga/css/famfamfam/icons/delete.png">
			<input type="hidden" name="cod_local" value="${ag.localFk.cod_local}" />
			<input type="hidden" name="formAgendamento.data_ag" value="${ag.data_ag}"/>
			<input type="hidden" name=formAgendamento.hora_ag value="${ag.hora_ag}" />
			&nbsp;
			<input type="submit" value="Exclui"/>
		</form>
	</td>
	<td>&nbsp;
		<form name="agendamento_atualiza01" action="${linkTo[AgendamentoController].atualiza}" method="get" enctype="multipart/form-data">
			&nbsp; &nbsp;
			<img src="/siga/css/famfamfam/icons/user_edit.png">
			<input type="hidden" name="cod_sala" value=${ag.localFk.cod_local} />
			<input type="hidden" name="data_ag" value=${ag.data_ag}/>
			<input type="hidden" name="hora_ag" value=${ag.hora_ag} />
			&nbsp;
			<input type="submit" value="Edita" />
		</form>
	</td>
	</tr>
</c:forEach>
 </table>
 <br><br>
 <div style="position:absolute;left:5%";>
  <form style="border-style: groove; border-color: silver;" method="get" action="${linkTo[AgendamentoController].excluir}">
  	<br>
 	&nbsp; Data:<input type="text" name="frm_data_ag" id="frm_data_ag01" maxlength="10" readonly="readonly" />
 	
 	<input type="submit" value="Buscar" /> &nbsp; <br><br>
 	
  </form>
  <a style="position:absolute;left:5%;" class="ui-state-hover" href="/sigapp/">Voltar</a>
 </div>
 