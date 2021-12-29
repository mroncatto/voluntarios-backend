  <!-- PROJECT SHIELDS -->
[![Tags][tag-shield]][tag-url]
[![Contributors][contributors-shield]][contributors-url]
[![Forks][forks-shield]][forks-url]
[![Issues][issues-shield]][issues-url]
[![MIT License][license-shield]][license-url]
[![Last Commit][lastcommit-shield]][lastcommit-url]
[![Environment][environment-shield]][environment-url]
[![Code Size][codesize-shield]][codesize-url]

## 

<p align="center">

<h3 align="center">Red de Voluntarios</h3>

  <p align="center">
    Creación de actividades de organizaciones en busca de voluntarios
    <br />
    <a href="https://voluntarios-backend.herokuapp.com/"><strong>Explore the docs »</strong></a>
    <br />
    <br />
    <a href="https://voluntarios-backend.herokuapp.com/">View Demo</a>
    ·
    <a href="https://github.com/mroncatto/voluntarios-backend/issues">Report Bug</a>
    ·
    <a href="https://github.com/mroncatto/voluntarios-backend/issues">Request Feature</a>
  </p>
</p>

<!-- TABLE OF CONTENTS -->
<details open="open">
  <summary>Tabla de contenido</summary>
  <ol>
    <li>
      <a href="#sobre-el-proyecto">Sobre el proyecto</a>
    </li>
    <li>
      <a href="#empezando">Empezando</a>
      <ul>
        <li><a href="#requisitos">Requisitos</a></li>
        <li><a href="#instalación">Instalación</a></li>
      </ul>
    </li>
    <li><a href="#uso">Uso</a></li>
    <li><a href="#licencia">Licencia</a></li>
  </ol>
</details>

<!-- ABOUT THE PROJECT -->
## Sobre el proyecto
Plataforma para organizaciones registrar actividades donde voluntarios realizaran subscripciones en dichas actividades.

Cuenta con las siguientes funcionalidades:
```
- Registro de voluntarios y organizaciones
- Registro actividades
- Subscripción de actividades que se desea voluntariar
```

<!-- GETTING STARTED -->
## Empezando

Para ejecutar la API siga las siguientes etapas...


### Requisitos

* IntelliJ IDEA o Eclipse IDE
* Java 14
* PostgreSQL en ejeccución
* Declarar las siguientes variables de ambiente:
  ```
  APP_DB_URL="DATABASE_URL:PORT" // Ejemplo: localhost:5432
  APP_DB_NAME="DATABASE_NAME"
  APP_DB_USER="DATABASE_USER"
  APP_DB_PWD="DATABASE_PASSWORD"
  APP_EMAIL_USER="CUENTA_DE_CORREO"
  APP_EMAIL_PWD="PASSWORD_DEL_CORREO"
  APP_EMAIL_HOST="SERVIDOR_SMTP" // Para pruebas se puede utilizar los servicios de Mailtrap
  APP_EMAIL_PORT="SMTP_PORT"
  APP_JWT_SECRET="LA_CLAVE_PARA_JWT"
  APP_JWT_EXPIRES=TIEMPO_PARA_EXPIRAR_EL_TOKEN // Ejemplo: 43200000 (12 horas)
  APP_JWT_REFRESH=86400000
  APP_TIMEZONE="TIME_ZONE" // Ejemplo: America/Asuncion
  APP_MODO="dev" // Modo de profile (dev o prod)
  ```

### Instalación

1. Clona el repo
   ```sh
   git clone https://github.com/mroncatto/voluntarios-backend
    ```
2. Abre con Eclipse o IntelliJ
3. Realiza ajustes a (`application-dev.yml`) si necesario
4. Certifique que las variables de ambiente fueron declaradas
5. Inicia el servicio

<!-- USAGE EXAMPLES -->
## Uso

- Utiliza [Postman](https://www.postman.com/downloads/) u otro servicio para manipular [metodos HTTP](https://www.w3schools.com/tags/ref_httpmethods.asp)
- Abre la documentación (`http://localhost:8180`)

<!-- LICENSE -->
## Licencia

Distribuido bajo la licencia MIT. Hecha un vistazo en [Licencia MIT](https://github.com/mroncatto/voluntarios-backend/blob/main/LICENSE) para obtener más información.


<!-- MARKDOWN LINKS & IMAGES -->
<!-- https://www.markdownguide.org/basic-syntax/#reference-style-links -->
[contributors-shield]: https://img.shields.io/github/contributors/mroncatto/voluntarios-backend?style=flat
[contributors-url]: https://github.com/mroncatto/voluntarios-backend/graphs/contributors
[forks-shield]: https://img.shields.io/github/forks/mroncatto/voluntarios-backend.svg?style=flat
[forks-url]: https://github.com/mroncatto/voluntarios-backend/network/members
[tag-shield]: https://img.shields.io/github/v/tag/mroncatto/voluntarios-backend
[tag-url]: https://github.com/mroncatto/voluntarios-backend/tags
[issues-shield]: https://img.shields.io/github/issues/mroncatto/voluntarios-backend.svg?style=flat
[issues-url]: https://github.com/mroncatto/voluntarios-backend/issues
[license-shield]: https://img.shields.io/github/license/mroncatto/voluntarios-backend
[license-url]: https://github.com/mroncatto/voluntarios-backend/blob/main/LICENSE
[lastcommit-shield]: https://img.shields.io/github/last-commit/mroncatto/voluntarios-backend
[lastcommit-url]: https://github.com/mroncatto/voluntarios-backend/commits/main
[environment-shield]: https://img.shields.io/github/deployments/mroncatto/voluntarios-backend/voluntarios-backend
[environment-url]: https://github.com/mroncatto/voluntarios-backend
[codesize-shield]: https://img.shields.io/github/languages/code-size/mroncatto/voluntarios-backend
[codesize-url]: https://github.com/mroncatto/voluntarios-backend
