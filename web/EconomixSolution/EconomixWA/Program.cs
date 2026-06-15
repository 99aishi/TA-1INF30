using System.Security.Claims;
using EconomixModel.Model;
using EconomixWA.Components;
using EconomixWS.TesoreriaWS;
using EconomixWS.UsuarioWS;
using Microsoft.AspNetCore.Authentication;
using Microsoft.AspNetCore.Authentication.Cookies;
using IUsuarioWS = EconomixWS.UsuarioWS.IUsuarioWS;

var builder = WebApplication.CreateBuilder(args);

var baseURL = builder.Configuration["URLServices:BaseWSPath"];

// Add services to the container.
builder.Services.AddRazorComponents()
    .AddInteractiveServerComponents();

// Add authentication services
builder.Services.AddAuthentication(CookieAuthenticationDefaults.AuthenticationScheme)
    .AddCookie(options =>
    {
        options.LoginPath = "/";
        options.LogoutPath = "/logout";
        options.ExpireTimeSpan = TimeSpan.FromHours(8);
        options.SlidingExpiration = true;
    });

builder.Services.AddAuthorization();
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
    Usuario? usuarioEncontrado = usuarioWS.ValidarCredenciales(request);

    if (usuarioEncontrado == null)
    {
        return Results.Redirect("/?error=1" + (string.IsNullOrEmpty(returnUrl) ? "" : $"&returnUrl={Uri.EscapeDataString(returnUrl)}"));
    }

    var claims = new List<Claim>
    {
        new Claim(ClaimTypes.NameIdentifier, usuarioEncontrado.UsuarioID.ToString()),
        new Claim(ClaimTypes.Email, usuarioEncontrado.Correo),
        new Claim("Nombre", usuarioEncontrado.Nombres),
        new Claim("ApellidoPaterno", usuarioEncontrado.ApellidoPaterno),
        new Claim("ApellidoMaterno", usuarioEncontrado.ApellidoMaterno)
    };

    if (usuarioEncontrado is Administrador)
    {
        claims.Add(new Claim(ClaimTypes.Role, "Administrador"));
    }
    else if (usuarioEncontrado is Empleado emp)
    {
        claims.Add(new Claim(ClaimTypes.Role, emp.Rol?.Titulo ?? "Empleado"));
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

app.MapRazorComponents<App>()
    .AddInteractiveServerRenderMode();

app.Run();
