ALTER TABLE "actividad"
ALTER "detalle" TYPE text,
ALTER "detalle" DROP DEFAULT,
ALTER "detalle" SET NOT NULL;
COMMENT ON COLUMN "actividad"."detalle" IS '';
COMMENT ON TABLE "actividad" IS '';