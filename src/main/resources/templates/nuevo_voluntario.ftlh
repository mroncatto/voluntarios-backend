<html style="margin: 0;padding: 0;" xmlns="http://www.w3.org/1999/xhtml">

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <!--<![endif]-->
    <meta name="viewport" content="width=device-width" />
    <title>Red de Voluntarios</title>
    <style>
        body, html, p, h1, h2, h3, h4, h4 {
            padding: 0;
            margin: 0;
        }
        .container {
            width: 80%;
            max-width: 400px;
            margin: 0 auto;
            padding: 5px;
            border: 1px solid rgb(167, 167, 226);
        }

        .card {
            width: 90%;
            margin: 0 auto;
            font-family: 'Calibri', 'Lucida Grande', 'Lucida Sans', 'Lucida Sans Regular', 'Lucida Sans Unicode', Verdana, sans-serif;
        }

        .card-header {
            text-align: center;
            font-size: 1rem;
            font-weight: 600;
            background-color: rgb(167, 167, 226);
            color: rgb(49, 49, 49);
            min-height: 80px;
            line-height: 80px;
        }

        .card-header h3 {
            padding: 0;
            margin: 0;
        }

        .card-body {
            min-height: 300px;
        }
        .card-body .voluntario {
            background-color: blue;
            color: #FFF;
            padding: 2px;
        }

        .card-footer {
            background-color: rgb(167, 167, 226);
            font-weight: 300;
            min-height: 40px;
            text-align: center;
            padding: 5px;
            display: flex;
            justify-content: center;
            flex-direction: column;
            align-items: center;
            gap: 1;
        }



    </style>
</head>

<body>
    <div class="container">
        <div class="card">
            <div class="card-header">
                <h3>Nuevo voluntario</h3>
            </div>
            <div class="card-body">
                <p>Ola ${name}, un nuevo voluntario se unio a la actividad '${actividad}'.</p>
                <br>
                <p class="voluntario">${voluntario}</p>
                
            </div>
            <div class="card-footer">
                <h5>Red de Voluntarios</h5>
                <p>${fecha}</p>
            </div>
        </div>
    </div>
</body>

</html>