<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main" />
        <%-- <g:set var="entityName" value="${message(code: 'cliente.label', default: 'Cliente')}" /> --%>
        <title>Cargar saldo</title>
    </head>
    <body>
        <h1>CARGAR SALDO</h1>
        <h2>Saldo: $${cliente?.saldo?.monto}</h2>
        <g:form url="[controller: 'cliente', action: 'cargarSaldo']" update="idRespuesta">
            <g:textField type="text" name="saldoACargar"/>
            <g:submitButton name="cargarSaldo" value="Cargar"/>
        </g:form>
    </body>
</html>