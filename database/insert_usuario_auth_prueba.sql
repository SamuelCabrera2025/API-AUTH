-- Usuario aislado para probar API-AUTH sin modificar las cuentas que todavía
-- utiliza la API de negocio con contraseñas en texto plano.
--
-- Correo: auth.prueba@ricaldone.edu.sv
-- Clave:  Prueba123*
--
-- USU_PASSWORD contiene un hash BCrypt. Ejecutar con F5 en SQL Developer.

INSERT INTO USUARIOS (USU_EMAIL, USU_PASSWORD, USU_ROL)
SELECT
    'auth.prueba@ricaldone.edu.sv',
    '$2a$10$T.OWGkKAnJVWaeLhqrhDf.iz/YPiT8ZxL2n6fb/sRfv38fs8MEx5W',
    'PADRE'
FROM DUAL
WHERE NOT EXISTS (
    SELECT 1
    FROM USUARIOS
    WHERE UPPER(USU_EMAIL) = 'AUTH.PRUEBA@RICALDONE.EDU.SV'
);

COMMIT;

SELECT ID_USUARIO, USU_EMAIL, USU_ROL
FROM USUARIOS
WHERE UPPER(USU_EMAIL) = 'AUTH.PRUEBA@RICALDONE.EDU.SV';
