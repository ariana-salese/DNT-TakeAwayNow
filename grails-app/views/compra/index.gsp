<html>
    <head>
    </head>
    <body>

        <g:form action="comprar">

            <div>
                cliente:
                <g:select optionKey="id" name="clienteId" from="${clientes}" />
            </div>

            <div>
                productos:
                <g:select optionKey="id" name="productoId" from="${productos}" />
            </div>

            <div>
                <button type="submit">comprar</button>
            </div>
        </g:form>

    </body>
</html>