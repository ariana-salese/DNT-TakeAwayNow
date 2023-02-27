<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main" />
        <%-- <g:set var="entityName" value="${message(code: 'cliente.label', default: 'Cliente')}" /> --%>
        <title>Cargar saldo</title>
    </head>
    <body>
        <h1>CARGAR SALDO</h1>
        <h2>Saldo: $${cliente.saldo.monto}</h2>
        <g:form name="login" url="[controller: 'cliente', action: 'cargarSaldo']" update="idRespuesta">
            <%-- TODO no es malisimo usar tablas? --%>
            <table>
                <tr>
                    <td>Ingrese el sado a cargar</td>
                    <td><g:textField name="saldoACargar"/></td>
                </tr>
                <tr>
                    <td colspan="2"><g:submitButton name="cargarSaldo" value="Cargar" /></td>
                </tr>
            </table>
        </g:form>
    </body>
</html>