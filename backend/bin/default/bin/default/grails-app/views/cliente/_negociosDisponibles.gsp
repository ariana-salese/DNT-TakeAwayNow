<section class="row">
                <div class="nav" role="navigation">
                    <ul>
                        <li><a class="home" href="${createLink(uri: '/cliente/index')}"><g:message code="default.home.label"/></a></li>
						<li><a class="home" href="${createLink(uri: '/cliente/listarNegocios')}"><g:message code="Negocios disponibles"/></a></li>
                        <li><a class="home" href="${createLink(uri: '/cliente/verSaldo')}"><g:message code="Consultar saldo"/></a></li>
                    </ul>
                </div>
            </section>
<g:if test="${negocios.size() > 0}">
	<h2>Estos son los negocios disponibles actualmente</h2>
	<table border="1|0">
		<tr>
			<td>Negocio</td>
		</tr>
		<g:each var="negocio" in="${negocios}">
			<tr>
				<td>
					${negocio.nombre}
				</td>
			</tr>
		</g:each>
	</table>   
</g:if>
<g:else>
	No hay negocios disponibles actualmente.
</g:else>
