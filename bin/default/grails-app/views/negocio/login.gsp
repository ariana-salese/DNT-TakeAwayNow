<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'cliente.label', default: 'Cliente')}" />
        <title>Bienvenid@</title>

        <style>
            body {
                background-color: #f0f0f0;
                font-family: Arial, Helvetica, sans-serif;
                font-size: 12px;
                color: #333;
            }
            table {
                width: 300px;
                margin: 50px auto;
                background-color: #f0f0f0;
                border: 1px solid #ccc;
            }
            table td {
                padding: 5px;
            }
            table td input {
                width: 200px;
            }
            table td input[type="submit"] {
                width: 100%;
            }
            a {
                display: block;
                width: 300px;
                margin: 0 auto;
                text-align: center;
            }
        </style>
        
    </head>
    <body>
        <div id="idRespuesta"></div>
        <g:form name="login" url="[controller: 'negocio', action: 'acceder']" update="idRespuesta">
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
                    <td colspan="2"><g:submitButton name="acceder" value="Iniciar Sesión" /></td>
                </tr>
            </table>
        </g:form>
        <a href="create">No tienes cuenta? Crear</a>
    </body>
</html>