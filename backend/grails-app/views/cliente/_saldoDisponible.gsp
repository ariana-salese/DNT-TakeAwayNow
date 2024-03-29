<div id="content" role="main">
    <div class="container">
        <section class="row">
            <div class="nav" role="navigation">
                <ul>
                    <li><a class="home" href="${createLink(uri: '/cliente/index')}"><g:message code="default.home.label"/></a></li>
                    <li><a class="home" href="${createLink(uri: '/cliente/listarNegocios')}"><g:message code="Negocios disponibles"/></a></li>
                    <li><a class="home" href="${createLink(uri: '/cliente/verSaldo')}"><g:message code="Consultar saldo"/></a></li>
                </ul>
            </div>
        </section>
        <g:if test="${clientes.size() > 0}">
            <h2>Estos son los saldos disponibles de cada cliente</h2>
            <table border="1|0">
                <tr>
                    <td>Cliente</td>
                    <td>Puntos de confianza</td>
                    <td>Saldo</td>
                    <td>Compras realizadas</td>
                    <td>Compras retiradas</td>
                    <td>Plan actual</td>
                    <td>Buffet ingresado</td>
                </tr>
                <g:each var="cliente" in="${clientes}">
                    <tr>
                        <td>
                            ${cliente.nombre}
                        </td>
                        <td>
                            ${cliente.saldo?.monto}
                        </td>
                        <td>
                            ${cliente.comprasRealizadas?.size()}
                        </td>
                        <td>
                            ${cliente.comprasRetiradas?.size()}
                        </td>
                        <td>
                            ${cliente.puntosDeConfianza?.cantidad}
                        </td>
                        <td>
                            ${cliente.plan?.getClass() == takeawaynow.PlanRegular ? "Plan Regular" : "Plan Prime " + {(String) cliente.getPlan().diasRestantesDePlanPrime()}}
                        </td>
                        <td>
                            ${cliente.negocioIngresado?.nombre}
                        </td>
                    </tr>
                </g:each>
            </table>    
        </g:if>
        <g:else>
            No hay clientes registrados actualmente.
        </g:else>
    </div>