rm ScriptUnico.sql
touch ScriptUnico.sql

cat General/Eliminacion.sql >> ScriptUnico.sql
cat General/Creacion.sql >> ScriptUnico.sql

cat Operaciones/*.sql >> ScriptUnico.sql

cat RRHH/*.sql >> ScriptUnico.sql

cat Tesoreria/*.sql >> ScriptUnico.sql
