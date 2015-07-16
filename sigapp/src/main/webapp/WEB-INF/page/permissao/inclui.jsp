<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<siga:pagina titulo="Permissao Inclui"/> 

<title>Permiss&atilde;o Inclui</title>
<form name="frm_inclui_permissao" method="GET" action="permissao_inclui" enctype="multipart/form-data">
<br>
<div class="ui-state-default" style="position: absolute;left:5%;right:77%;"> Matr&iacute;cula:</div><input type="text" name="matricula_permitida" style="position: absolute;left:25%;" value="" /><div class="ui-state-default" style="position:absolute;left:45%;"> &nbsp; (Exemplo: 11222)</div>
<br><br>
<div class="ui-state-default" style="position: absolute;left:5%;right:77%;" > Nome:</div><input type="text" name="nome_permitido" style="position:absolute;left:25%;" value="" />
<br><br>
<div class="ui-state-default" style="position: absolute;left:5%;right:77%;"> C&oacute;digo do Forum:</div><input type="text" name="forum_permitido" style="position:absolute;left:25%; value="" maxlength="3" />
<br><br>
<input class="ui-button" style="position: absolute;left:5%;" type="submit" value="Permite usu&aacute;rio" />
<br><br><br>
<h4 class="ui-widget" style="position: absolute;left:5%; color:red">${mensagem}</h4>
</form>
<a style="position: absolute;left:5%;" class="ui-state-hover" href="/sigapp/">Voltar</a>