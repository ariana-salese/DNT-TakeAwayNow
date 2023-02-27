<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main" />
        <%-- <g:set var="entityName" value="${message(code: 'cliente.label', default: 'Cliente')}" /> --%>
        <title>Perfil</title>
    </head>
    <body>
        <%-- TODO disenio y organizar --%>
        <h1>TU PERFIL</h1>
        <%-- TODO FOTO DE PERFIL<img src=""> --%>
        <h2>Nombre: ${cliente.nombre}</h2>
        <h2>Saldo: $${cliente.saldo.monto}</h2>
        <g:form name="entrarACargarSaldo" url="[controller: 'cliente', action: 'entrarACargarSaldo']">
            <g:submitButton name="entrarACargarSaldo" value="Cargar saldo"/>
        </g:form>
        <%-- <g:link controller="cliente" action="entrarACargarSaldo" params="[clienteId:cliente.id]">
            CARGAR
        </g:link> --%>
        <h2>Puntos de confianza: ${cliente.puntosDeConfianza().cantidad}</h2>
        <div>
            <h2>Historial de compras retiradas:</h2>
            <g:if test="${cliente.comprasRetiradas.size() == 0}">
                <h3>No hay compras retiradas todavia!</h3>
            </g:if>
            <g:else>
                <g:each in="${cliente.comprasRetiradas}">
                    <p>ID: ${it.id}</p>
                    <p>Fecha: ${it.fecha}</p>
                    <%-- TODO Ver informacion de compra --%>
                </g:each>
            </g:else>
        </div>
        <div>
            <h2>Compras a retirar:</h2>
            <g:if test="${cliente.comprasRealizadas.size() == 0}">
                <h3>No hay compras para retirar!</h3>
            </g:if>
            <g:else>
                <g:each in="${cliente.comprasRealizadas}">
                    <p>ID: ${it.id}</p>
                    <p>Fecha: ${it.fecha}</p>
                    <%--TODO Ver compra --%>
                </g:each>
            </g:else>
        </div>


    </body>
</html>