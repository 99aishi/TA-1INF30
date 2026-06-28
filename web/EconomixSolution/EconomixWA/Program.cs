using System.Security.Claims;
using System.Text.Json;
using System.Text.Json.Serialization;
using EconomixModel.Converters;
using EconomixModel.Model;
using EconomixWA.Components;
using EconomixWS.OperacionesWS;
using EconomixWS.TesoreriaWS;
using EconomixWS.UsuarioWS;
using Microsoft.AspNetCore.Authentication;
using Microsoft.AspNetCore.Authentication.Cookies;
using IUsuarioWS = EconomixWS.UsuarioWS.IUsuarioWS;

var builder = WebApplication.CreateBuilder(args);

builder.Services.AddSingleton(new JsonSerializerOptions
{
    PropertyNamingPolicy = JsonNamingPolicy.CamelCase,
    PropertyNameCaseInsensitive = true,
    ReferenceHandler = ReferenceHandler.IgnoreCycles,
    Converters = { new UnixDateTimeConverter() }
});

var baseURL = builder.Configuration["URLServices:BaseWSPath"];

// Add services to the container.
builder.Services.AddRazorComponents()
    .AddInteractiveServerComponents();

// Add authentication services
builder.Services.AddAuthentication(CookieAuthenticationDefaults.AuthenticationScheme)
    .AddCookie(options =>
    {
        options.LoginPath = "/";
        options.LogoutPath = "/auth/logout";
        options.ExpireTimeSpan = TimeSpan.FromHours(8);
        options.SlidingExpiration = true;
    });

builder.Services.AddAuthorization(options =>
{
    options.AddPolicy("IsAdministrador", policy => policy.RequireRole("Administrador"));
    options.AddPolicy("IsJefe", policy => policy.RequireRole("Jefe", "Jefe de Área", "Jefe de Area"));
    options.AddPolicy("IsEmpleado", policy => policy.RequireRole("Empleado"));
    options.AddPolicy("IsTesoreria", policy => policy.RequireRole("Tesorero", "Tesorero y Finanzas", "Tesoreria"));
});
builder.Services.AddCascadingAuthenticationState();
builder.Services.AddHttpContextAccessor();
builder.Services.AddHttpClient();

//WebServices
    //Usuario
builder.Services.AddHttpClient("UsuarioWS", client => client.BaseAddress = new Uri(baseURL));
builder.Services.AddHttpClient<IUsuarioWS, UsuarioWS>(
    client => client.BaseAddress = new Uri(baseURL)
);

builder.Services.AddHttpClient<IAreaWS, AreaWSImpl>(
    client => client.BaseAddress = new Uri(baseURL)
);

builder.Services.AddHttpClient<IRolWS, RolWSImpl>(
    client => client.BaseAddress = new Uri(baseURL)
);

builder.Services.AddHttpClient<IMonedaWS, MonedaWSImpl>(
    client => client.BaseAddress = new Uri(baseURL)
);

builder.Services.AddHttpClient<ITipoCambioWS, TipoCambioWSImpl>(
    client => client.BaseAddress = new Uri(baseURL)
);

builder.Services.AddHttpClient<IEmpleadoWS, EmpleadoWSImpl>(
    client => client.BaseAddress = new Uri(baseURL)
);

builder.Services.AddHttpClient<ICajaChicaWS, CajaChicaWSImpl>(
    client => client.BaseAddress = new Uri(baseURL)
);

builder.Services.AddHttpClient<ICuentaBancariaWS, CuentaBancariaWSImpl>(
    client => client.BaseAddress = new Uri(baseURL)
);

builder.Services.AddHttpClient<ISolicitudGastoWS, SolicitudGastoWSImpl>(
    client => client.BaseAddress = new Uri(baseURL)
);

builder.Services.AddHttpClient<IComprobantePagoWS, ComprobantePagoWSImpl>(
    client => client.BaseAddress = new Uri(baseURL)
);

builder.Services.AddHttpClient<ITransaccionWS, TransaccionWSImpl>(
    client => client.BaseAddress = new Uri(baseURL)
);

builder.Services.AddHttpClient<ICicloCajaWS, CicloCajaWSImpl>(
    client => client.BaseAddress = new Uri(baseURL)
);

builder.Services.AddHttpClient<IRendicionWS, RendicionWSImpl>(
    client => client.BaseAddress = new Uri(baseURL)
);

builder.Services.AddHttpClient<IPermisoEdicionWS, PermisoEdicionWSImpl>(
    client => client.BaseAddress = new Uri(baseURL)
);

builder.Services.AddHttpClient<IAuditoriaWS, AuditoriaWSImpl>(
    client => client.BaseAddress = new Uri(baseURL)
);

var app = builder.Build();

// Configure the HTTP request pipeline.
if (!app.Environment.IsDevelopment())
{
    app.UseExceptionHandler("/Error", createScopeForErrors: true);
    // The default HSTS value is 30 days. You may want to change this for production scenarios, see https://aka.ms/aspnetcore-hsts.
    app.UseHsts();
}
app.UseStatusCodePagesWithReExecute("/not-found", createScopeForStatusCodePages: true);
app.UseHttpsRedirection();

app.UseAuthentication();
app.UseAuthorization();

app.UseAntiforgery();

app.MapStaticAssets();

app.MapPost("/auth/login", async (HttpContext context, IUsuarioWS usuarioWS) =>
{
    var form = await context.Request.ReadFormAsync();
    string usuarioInput = form["usuario"].ToString();
    string passwordInput = form["password"].ToString();
    string? returnUrl = form["returnUrl"].ToString();

    var request = new LoginRequest { Correo = usuarioInput, Password = passwordInput };
    Usuario? usuarioEncontrado;
    try
    {
        usuarioEncontrado = await usuarioWS.ValidarCredencialesAsync(request);
    }
    catch (LoginException ex) when (ex.StatusCode == System.Net.HttpStatusCode.Forbidden
        && ex.Message.Contains("inactivo", StringComparison.OrdinalIgnoreCase))
    {
        return Results.Redirect("/?error=inactive" + (string.IsNullOrEmpty(returnUrl) ? "" : $"&returnUrl={Uri.EscapeDataString(returnUrl)}"));
    }
    catch (LoginException ex) when (ex.StatusCode == System.Net.HttpStatusCode.Forbidden)
    {
        return Results.Redirect("/?error=blocked" + (string.IsNullOrEmpty(returnUrl) ? "" : $"&returnUrl={Uri.EscapeDataString(returnUrl)}"));
    }
    catch (LoginException ex) when (ex.StatusCode == System.Net.HttpStatusCode.Unauthorized)
    {
        return Results.Redirect("/?error=1" + (string.IsNullOrEmpty(returnUrl) ? "" : $"&returnUrl={Uri.EscapeDataString(returnUrl)}"));
    }

    if (usuarioEncontrado == null)
    {
        return Results.Redirect("/?error=1" + (string.IsNullOrEmpty(returnUrl) ? "" : $"&returnUrl={Uri.EscapeDataString(returnUrl)}"));
    }

    var claims = new List<Claim>
    {
        new (ClaimTypes.NameIdentifier, usuarioEncontrado.UsuarioID.ToString()),
        new (ClaimTypes.Email, usuarioEncontrado.Correo),
        new ("Nombre", usuarioEncontrado.Nombres),
        new ("ApellidoPaterno", usuarioEncontrado.ApellidoPaterno),
        new("ApellidoMaterno", usuarioEncontrado.ApellidoMaterno),
    };

    if (usuarioEncontrado is Administrador)
    {
        claims.Add(new Claim(ClaimTypes.Role, "Administrador"));
    }
    else if (usuarioEncontrado is Empleado emp)
    {
        string rol = DeterminarRolEmpleado(emp);
        claims.Add(new Claim(ClaimTypes.Role, rol));
        claims.Add(new Claim("RolFlujo", emp.RolFlujo.ToString()));
    }

    var identidad = new ClaimsIdentity(claims, CookieAuthenticationDefaults.AuthenticationScheme);
    var principal = new ClaimsPrincipal(identidad);

    var authProperties = new AuthenticationProperties
    {
        IsPersistent = true,
        ExpiresUtc = DateTimeOffset.UtcNow.AddHours(8)
    };

    await context.SignInAsync(
        CookieAuthenticationDefaults.AuthenticationScheme,
        principal,
        authProperties);

    var redirectUrl = string.IsNullOrEmpty(returnUrl) ? "/dashboard" : returnUrl;
    return Results.Redirect(redirectUrl);
});

app.MapGet("/auth/logout", async (HttpContext context) =>
{
    await context.SignOutAsync(CookieAuthenticationDefaults.AuthenticationScheme);
    return Results.Redirect("/");
});

static string DeterminarRolEmpleado(Empleado emp)
{
    // Treasury area detection by name
    if (emp.Area != null && !string.IsNullOrEmpty(emp.Area.Nombre))
    {
        string nombreArea = emp.Area.Nombre;
        if (nombreArea.Contains("Tesorer", StringComparison.OrdinalIgnoreCase) ||
            nombreArea.Contains("Tes", StringComparison.OrdinalIgnoreCase) ||
            nombreArea.Contains("Tesorero", StringComparison.OrdinalIgnoreCase) ||
            nombreArea.Contains("Finanza", StringComparison.OrdinalIgnoreCase))
        {
            return "Tesoreria";
        }
    }

    // Boss detection: no direct boss, or explicitly marked as area boss, or area has no boss assigned
    bool esJefeArea = emp.RolFlujo == RolFlujo.JEFE_AREA;
    bool sinJefeDirecto = emp.JefeDirecto == null;
    bool areaSinJefe = emp.Area?.Jefe == null;
    bool esElJefeDelArea = emp.Area?.Jefe?.UsuarioID == emp.UsuarioID;

    if (esJefeArea || sinJefeDirecto || areaSinJefe || esElJefeDelArea)
    {
        return "Jefe";
    }

    return "Empleado";
}

app.MapRazorComponents<App>()
    .AddInteractiveServerRenderMode();

app.Run();
