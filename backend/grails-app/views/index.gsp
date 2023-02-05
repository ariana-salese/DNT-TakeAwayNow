<!doctype html>
<html>
    <head>
        <meta name="layout" content="main"/>
        <title>TakeAwayNow</title>
        <%-- TODO crear archivo separado--%>
        <style>
            div {
                display: flex;
                flex-direction: column;
                align-items: center;
            }

            h1 {
                text-align: center;
            }

            button {
                width: 7rem;
                margin: 2rem;
            }
        </style>
    </head>
    <body>
        <div>
            <h1>Bienvenid@ a TakeAwayNow</h1>
            <button onclick="location.href = 'negocio/login'">Negocio</button>
            <button onclick="location.href = 'cliente/login'">Cliente</button>
        </div>
    </body>
</html>
