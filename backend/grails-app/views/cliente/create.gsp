<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'cliente.label', default: 'Cliente')}" />
        <title><g:message code="default.create.label" args="[entityName]" /></title>
    </head>
    <body>
    <div id="content" role="main">
        <div class="container">
            <%-- <section class="row">
                <a href="#create-cliente" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
                <div class="nav" role="navigation">
                    <ul>
                        <li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
                        <li><g:link class="list" action="index"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
                    </ul>
                </div>
            </section> --%>
            <section class="row">
                <div id="create-cliente" class="col-12 content scaffold-create" role="main">
                    <h1><g:message code="default.create.label" args="[entityName]" /></h1>
                    <g:if test="${flash.message}">
                    <div class="message" role="status">${flash.message}</div>
                    </g:if>
                    <g:hasErrors bean="${this.cliente}">
                    <ul class="errors" role="alert">
                        <g:eachError bean="${this.cliente}" var="error">
                        <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
                        </g:eachError>
                    </ul>
                    </g:hasErrors>
                    <g:form name="registrarUsuario" url="[controller: 'cliente', action: 'registrarUsuario']" update="idRespuesta">
                        <table>
                            <tr>
                                <td>Usuario</td>
                                <td><g:textField name="usuario" /></td>
                            </tr>
                            <tr>
                                <td>Contrase&ntilde;a</td>
                                <td><g:passwordField name="password" /></td>
                            </tr>
                            <tr>
                                <td>Fecha de cumplea&ntilde;os</td>
                                <td><g:datePicker name="dia" value="${new Date()}" precision="day" noSelection="['':'-Choose-']"/></td>
                            </tr>
                            <tr>
                                <td colspan="2"><g:submitButton name="registrarUsuario" value="Registrar Usuario" /></td>
                            </tr>
                        </table>
                    </g:form>
                    <%-- <g:form resource="${this.cliente}" method="POST">
                        <fieldset class="form">
                            <f:all bean="cliente"/>
                        </fieldset>
                        <fieldset class="buttons">
                            <g:submitButton name="create" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}" />
                        </fieldset>
                    </g:form> --%>
                </div>
            </section>
        </div>
    </div>
    </body>
</html>
