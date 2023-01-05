<!doctype html>
<html lang="en" class="no-js">
    <head>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-GLhlTQ8iRABdZLl6O3oVMWSktQOp6b7In1Zl3/Jr59b6EGGoI1aFkw7cmDA6j6gD" crossorigin="anonymous">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
        <title>
            <g:layoutTitle default="Grails"/>
        </title>
        <meta name="viewport" content="width=device-width, initial-scale=1"/>
        <asset:link rel="icon" href="favicon.ico" type="image/x-ico"/>

        <asset:stylesheet src="application.css"/>

        <g:layoutHead/>
    </head>

    <body>

        <nav class="navbar navbar-expand-lg navbar-dark navbar-static-top" role="navigation">
            <div class="container-fluid">
                <a class="navbar-brand" href="/#"><asset:image src="grails.svg" alt="Grails Logo"/></a>
                <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarContent" aria-controls="navbarContent" aria-expanded="false" aria-label="Toggle navigation">
                    <span class="navbar-toggler-icon"></span>
                </button>

                <div class="collapse navbar-collapse" aria-expanded="false" style="height: 0.8px;" id="navbarContent">
                    <ul class="nav navbar-nav ml-auto">
                        <g:pageProperty name="page.nav"/>
                    </ul>
                </div>
            </div>
        </nav>

        <g:layoutBody/>

        <%-- <g:render template="logo" />

        <div class="container-fluid d-flex">
            <div class="formClienteContainer mb-2 container d-flex flex-column">
                <g:render template="/cliente/logoCliente" />
                <g:render template="/cliente/ingresoCliente" />
                <g:render template="/cliente/registroCliente" />
            </div>
            <div class="formNegocioContainer mb-2 container d-flex flex-column">
                <g:render template="/negocio/logoNegocio" />
                <g:render template="/negocio/ingresoNegocio" />
                <g:render template="/negocio/registroNegocio" />
            </div>
        </div> --%>

        <div class="footer" role="contentinfo">
            <div class="container-fluid">
                <div class="row">
                    <div class="col">
                        <a href="http://guides.grails.org" target="_blank">
                            <asset:image src="advancedgrails.svg" alt="Grails Guides" class="float-left"/>
                        </a>
                        <strong class="centered"><a href="http://guides.grails.org" target="_blank">Grails Guides</a></strong>
                        <p>Building your first Grails app? Looking to add security, or create a Single-Page-App? Check out the <a href="http://guides.grails.org" target="_blank">Grails Guides</a> for step-by-step tutorials.</p>

                    </div>
                    <div class="col">
                        <a href="http://docs.grails.org" target="_blank">
                            <asset:image src="documentation.svg" alt="Grails Documentation" class="float-left"/>
                        </a>
                        <strong class="centered"><a href="http://docs.grails.org" target="_blank">Documentation</a></strong>
                        <p>Ready to dig in? You can find in-depth documentation for all the features of Grails in the <a href="http://docs.grails.org" target="_blank">User Guide</a>.</p>

                    </div>
                    <div class="col">
                        <a href="https://slack.grails.org" target="_blank">
                            <asset:image src="slack.svg" alt="Grails Slack" class="float-left"/>
                        </a>
                        <strong class="centered"><a href="https://slack.grails.org" target="_blank">Join the Community</a></strong>
                        <p>Get feedback and share your experience with other Grails developers in the community <a href="https://slack.grails.org" target="_blank">Slack channel</a>.</p>
                    </div>
                </div>
            </div>
        </div>

        <div id="spinner" class="spinner" style="display:none;">
            <g:message code="spinner.alt" default="Loading&hellip;"/>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js" integrity="sha384-w76AqPfDkMBDXo30jS1Sgez6pr3x5MlQ1ZAGC+nuZB+EYdgRZgiwxhTBTkF7CXvN" crossorigin="anonymous"></script>
        <asset:javascript src="application.js"/>

    </body>
</html>
