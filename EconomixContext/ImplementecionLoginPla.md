# Plan de Implementación del Sistema de Autenticación y Flujo de Login

Este documento describe el sistema de autenticación por cookies de 8 horas dentro de la aplicación Blazor Server (EconomixWA), consumiendo el API externo en Java/GlassFish.

## Flujo General de Autenticación

Para evitar el bloqueo de WebSockets de Blazor Server al escribir cookies, se utiliza un flujo híbrido seguro:

```
[Pantalla Login.razor] 
       │ 
       ▼ (Formulario POST HTML tradicional)
[Endpoint local: /auth/login] (En Program.cs)
       │ 
       ▼ (Llamada síncrona mediante HttpClient)
[UsuarioWSImpl.ValidarCredenciales] ───> [API Java / GlassFish] (Retorna JSON)
       │
       ▼ (Si es válido, lee herencia Admin/Empleado)
[Generación de Cookie (8 Horas)] (Mediante HttpContext.SignInAsync)
       │
       ▼ (Redirección segura)
[Pantalla /dashboard] (Dentro del contexto de Blazor ya autenticado)
```

---

## 🛠️ Fase 1: Interfaces y Clientes de Servicio (UsuarioWS)

Definición del servicio en la capa proxy .NET.

### 1.1. Interfaz IUsuarioWS

Ubicación: `EconomixWS/rrhh/IWS/IUsuarioWS.cs`

```csharp
namespace EconomixWS.rrhh;
using EconomixModel.Model;

public interface IUsuarioWS
{
    Usuario? ValidarCredenciales(LoginRequest request);
    bool IsAuthenticated();
    Usuario? GetCurrentUser();
    void Logout();
}
```

### 1.2. Clase de Implementación UsuarioWS

Ubicación: `EconomixWS/rrhh/WSImpl/UsuarioWS.cs`

Esta clase utiliza un `HttpClient` configurado externamente y el `IHttpContextAccessor` oficial de .NET para acceder a las cookies.

---

## ⚙️ Fase 2: Configuración en Program.cs y Registro del Endpoint REST

Dentro de `Program.cs` de `EconomixWA`, se configura el middleware de cookies por 8 horas y se mapea la ruta `/auth/login` para recibir los posts del formulario tradicional.

### Flujo de Registro en Cookie:
- `UsuarioID` mapped to `ClaimTypes.NameIdentifier`
- `Correo` mapped to `ClaimTypes.Email`
- `Nombre`, `ApellidoPaterno`, `ApellidoMaterno` as custom claims
- `Role` mapped based on area/hierarchy check using `DeterminarRolEmpleado` in `Program.cs`

---

## 🖥️ Fase 3: Pantalla de Login (Login.razor)

Ubicación: `EconomixWA/Components/Pages/Login.razor`

El formulario utiliza un POST HTML tradicional directo a `/auth/login` para poder escribir las cookies de sesión fuera del circuito WebSocket.

---

## 🔐 Fase 4: Comprobación de Sesión y Logout en Pantallas

- Comprobación: Se realiza inyectando `IUsuarioWS` y comprobando `IsAuthenticated()`.
- Cierre de Sesión: Redirige a `/auth/logout` que elimina la cookie física y redirige al index.

---

## 📝 TO-DO / Auditoría de Inicio de Sesión

- [ ] **Auditoría de Intentos de Login**: Al intentar realizar un login (exitoso o fallido), se debe disparar de forma automática una llamada a `AuditoriaWS` para registrar el intento en `log_auditoria`, registrando el correo del usuario, la fecha/hora, la acción ("LOGIN_SUCCESS" / "LOGIN_FAILED") y el ID del usuario (si se encontró).
