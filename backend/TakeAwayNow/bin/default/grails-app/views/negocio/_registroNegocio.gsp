<div class="container">
    <g:form class="loginForm d-flex flex-column flex-fill m-2 rounded-4" name="negocioForm" url="[controller:'negocio',action:'login']">
        <strong>Ingrese el nombre del negocio que desea registrar</strong>
        <g:textField name="negocioNombreField" value="${myValue}" />
        
        <label for="horarioApertura">Elija un horario de apertura</label>
        <input type="time" id="horarioApertura" name="horarioApertura">

        <label for="horarioCierre">Elija un horario de cierre</label>
        <input type="time" id="horarioCierre" name="horarioCierre">

        <g:actionSubmit class="mt-2" value="Registrarse como nuevo negocio" action="update" />
    </g:form>
</div>