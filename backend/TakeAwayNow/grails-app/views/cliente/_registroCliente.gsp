<div class="container">
    <g:form class="loginForm d-flex flex-column flex-fill m-2 rounded-4" resource="cliente" action="registro">
        <strong>Ingrese el nombre del cliente que desea registrar</strong>
        <g:textField name="nombreCliente" />
        <g:actionSubmit class="mt-2" value="Registrarse como nuevo cliente" action="registro" />
    </g:form>
</div>