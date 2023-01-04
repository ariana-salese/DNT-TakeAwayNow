<div class="container">
    <g:form class="loginForm d-flex flex-column flex-fill m-2 rounded-4" resource="cliente" action="ingreso">
        <strong>Ingrese su nombre</strong>
        <g:textField name="nombreCliente" />
        <g:actionSubmit class="mt-2" value="Ingresar como cliente" action="ingreso" />
    </g:form>
</div>