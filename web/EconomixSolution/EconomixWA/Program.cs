using EconomixWA.Components;
using EconomixWS.UsuarioWS;
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
builder.Services.AddHttpClient<IUsuarioWS, UsuarioWS>(
    client => client.BaseAddress = new Uri(baseURL)    
);
builder.Services.AddHttpClient<IAreaWS, AreaWSImpl>(
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
app.MapRazorComponents<App>()
    .AddInteractiveServerRenderMode();

app.Run();
