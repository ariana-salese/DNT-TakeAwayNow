<div class="container">
    <g:form class="loginForm d-flex flex-column flex-fill m-2 rounded-4" name="negocioForm" url="[controller:'negocio',action:'login']">
        <strong>Ingrese el nombre de su negocio</strong>
        <g:textField name="negocioNombreField" value="${myValue}" />
        <g:actionSubmit class="mt-2" value="Ingresar como negocio" action="update" />
    </g:form>
</div>