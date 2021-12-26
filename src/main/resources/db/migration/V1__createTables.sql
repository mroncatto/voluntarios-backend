-- Marcelo Roncatto Ficagna

CREATE SEQUENCE usuario_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 99999999999 START 1 CACHE 1;

CREATE TABLE "public"."usuario" (
    "id" bigint DEFAULT nextval('usuario_id_seq') NOT NULL,
    "active" boolean NOT NULL,
    "ciudad" character varying(75) NOT NULL,
    "email" character varying(45) NOT NULL,
    "full_name" character varying(75) NOT NULL,
    "join_date" timestamp NOT NULL,
    "last_login_date" timestamp,
    "non_locked" boolean NOT NULL,
    "password" character varying(128) NOT NULL,
    "updated_at" timestamp,
    "user_tipo" character varying(1) NOT NULL,
    "username" character varying(25) NOT NULL,
    CONSTRAINT "uk_5171l57faosmj8myawaucatdw" UNIQUE ("email"),
    CONSTRAINT "uk_863n1y3x0jalatoir4325ehal" UNIQUE ("username"),
    CONSTRAINT "usuario_pkey" PRIMARY KEY ("id")
) WITH (oids = false);

CREATE SEQUENCE role_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 99999999999 START 1 CACHE 1;

CREATE TABLE "public"."role" (
    "id" bigint DEFAULT nextval('role_id_seq') NOT NULL,
    "role" character varying(30),
    CONSTRAINT "role_pkey" PRIMARY KEY ("id")
) WITH (oids = false);


CREATE TABLE "public"."usuario_roles" (
    "user_id" bigint NOT NULL,
    "roles_id" bigint NOT NULL,
    CONSTRAINT "fkr5p0u8r15jjo6u7xr1928hsld" FOREIGN KEY (roles_id) REFERENCES role(id) NOT DEFERRABLE,
    CONSTRAINT "fkvfid90smx2pshpvqc3r88xqt" FOREIGN KEY (user_id) REFERENCES usuario(id) NOT DEFERRABLE
) WITH (oids = false);

CREATE SEQUENCE actividad_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 99999999999 START 1 CACHE 1;

CREATE TABLE "public"."actividad" (
    "id" bigint DEFAULT nextval('actividad_id_seq') NOT NULL,
    "actividad" character varying(75) NOT NULL,
    "alterado" timestamp NOT NULL,
    "creado" timestamp NOT NULL,
    "detalle" character varying(300),
    "inicio" timestamp NOT NULL,
    "situacion" character varying(1) NOT NULL,
    "creado_por_id" bigint NOT NULL,
    CONSTRAINT "actividad_pkey" PRIMARY KEY ("id"),
    CONSTRAINT "fk88k3ardv36mhjpy8ke8ktxjkg" FOREIGN KEY (creado_por_id) REFERENCES usuario(id) NOT DEFERRABLE
) WITH (oids = false);

CREATE TABLE "public"."actividad_voluntarios" (
    "actividad_id" bigint NOT NULL,
    "voluntarios_id" bigint NOT NULL,
    CONSTRAINT "fkme59wgdy19dd2ov4no92a0elm" FOREIGN KEY (voluntarios_id) REFERENCES usuario(id) NOT DEFERRABLE,
    CONSTRAINT "fkqxs27bsxi31403qlbigyq4r16" FOREIGN KEY (actividad_id) REFERENCES actividad(id) NOT DEFERRABLE
) WITH (oids = false);

-- ADMIN ACCOUNT
INSERT INTO "role" ("role") VALUES ('ADMIN');
INSERT INTO "usuario" ("active", "email", "full_name", "ciudad", "join_date", "last_login_date", "non_locked", "password", "updated_at", "user_tipo", "username") VALUES ('1', 'admin@voluntarios.com', 'Administrador', 'San Cristobal', now(), NULL, '1', '$2a$10$8L.jmdP9sgoJWx7TRLS26ulrXVy1i.q3Y3ZW6TzzTOSG.fdh93k3i', NULL, 'V', 'admin');
INSERT INTO "usuario_roles" ("user_id", "roles_id") VALUES ('1', '1');



-- 2021-12-26 18:16:13.91639+00

