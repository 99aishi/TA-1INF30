# Economix - UI Standards & Patterns

> Documentación de estándares visuales, componentes y patrones de diseño utilizados en el proyecto Economix.

---

## 1. Infrastructure

| Item | Value |
|------|-------|
| **Framework** | Blazor Server (.NET 10) |
| **CSS Framework** | Bootstrap 5.3.3 (local) |
| **Icons** | Bootstrap Icons (`bi-*`) |
| **Font** | `'Helvetica Neue', Helvetica, Arial, sans-serif` (system fonts, no external imports) |
| **Page background** | `rgb(248,250,252)` (slate-50) |
| **Link color** | `#006bb7` |

---

## 2. Layout Architecture

### Page Shell (used by ~12 pages)

```
<div class="h-100 d-flex flex-column">
    <!-- Header -->
    <div class="d-flex flex-row align-items-center mb-4">
        <div class="flex-grow-1">
            <p class="h4 mb-1">Title</p>
            <p class="text-muted mb-0">Subtitle</p>
        </div>
        <LiveClock />
        <div class="flex-grow-1 text-end">
            <button class="btn btn-primary">+ Agregar X</button>
        </div>
    </div>

    <!-- Error alert (conditional) -->

    <!-- SearchBar component -->

    <!-- Content -->
    <div class="d-flex flex-column flex-fill overflow-hidden">
        <div class="row flex-fill mt-3 mb-3 overflow-hidden d-flex">
            <!-- List + Detail columns -->
        </div>
    </div>
</div>
```

### Master-Detail Split

| State | List column | Detail column |
|-------|-------------|---------------|
| List only | `col-12` | hidden |
| Detail open | `col-6` | `col-6` |

Dynamic class: `@(MostrarDetalle ? "col-6" : "col-12")`

### Fixed Split (standalone detail pages)

- `MiSolicitudDeGastoDetalle`: `col-8` (form) + `col-4` (seguimiento sidebar)
- `ComprobanteDePagoDetalle`: `col-4` (sustento) + `col-8` (datos)

---

## 3. Cards

### Global

All cards use `rounded-4` (maps to `border-radius: 1rem` via `app.css` override).

### Card Types

| Type | Classes | Usage |
|------|---------|-------|
| **List container** | `card rounded-4 p-3 h-100 overflow-scroll` | Page list panels |
| **Detail panel** | `card rounded-4 h-100 d-flex flex-column overflow-hidden` | Detail views |
| **Item row** | `card rounded-4 p-3 mb-3 bg-light` | Clickable list items |
| **Search bar** | `card rounded-4 p-3 mb-3` | Filter bars |
| **Dashboard stat** | `card rounded-4 h-100 cursor-pointer hover-shadow` | KPI tiles |
| **Inner card** | `card rounded-4 p-3 mb-3` | Nested sections |

### Detail Card Structure

```html
<div class="card rounded-4 h-100 d-flex flex-column overflow-hidden">
    <div class="p-3 pb-2 d-flex gap-4 align-items-center">
        <p class="h3 mb-0">Title</p>
        <span class="badge rounded-pill px-2 ...">Status</span>
    </div>
    <hr class="my-0" />
    <div class="flex-fill overflow-scroll p-3">
        <!-- Form content -->
    </div>
    <hr class="my-0" />
    <div class="p-3 d-flex flex-wrap-nowrap justify-content-end gap-3">
        <!-- Action buttons -->
    </div>
</div>
```

### Dividers

- **Standard:** `<hr class="my-0" />` (between header/content/footer)
- Always between card header and body, never inside body

### Scroll Behavior

| Element | Class |
|---------|-------|
| List containers | `overflow-scroll` |
| Detail content areas | `overflow-scroll` + `flex-fill` |
| Structural wrappers | `overflow-hidden` |

> **Rule:** All scrollable containers use `overflow-scroll`. Structural wrappers use `overflow-hidden`.

---

## 4. Typography

### Page Header

```html
<p class="h4 mb-1">Page Title</p>
<p class="text-muted mb-0">Subtitle / description</p>
```

### Detail Page Title

```html
<p class="h3 mb-0">Entity Name</p>
<span class="badge rounded-pill px-2 text-bg-success">ACTIVO</span>
```

### Heading Scale

| Level | Pattern | Context |
|-------|---------|---------|
| `h4` | `<p class="h4 mb-1">` | Page titles |
| `h3` | `<p class="h3 mb-0">` | Detail page titles |
| `h5` | `<p class="h5 mb-0">` | Card/section titles, item primary text |
| `h6` | `<p class="h6 mb-0">` | Compact item titles, dashboard subtitles |
| `h2` | `<p class="card-text h2">` | Dashboard KPI values |
| `small` | `<small class="text-muted">` | Metadata, helper text |

### Text Colors

| Class | Usage |
|-------|-------|
| `text-muted` | Subtitles, metadata, empty states, labels |
| `text-danger` | Validation errors, negative financial values |
| `text-success` | Positive financial values |
| `text-warning` | Urgent indicators |
| `text-primary` | Info callouts |
| `text-body-primary` | Dashboard KPI subtitles |

### Font Weight

| Class | Usage |
|-------|-------|
| `fw-bold` | KPI values, financial amounts, section titles |
| `fw-semibold` | Entity names in tables |

### Text Alignment

| Class | Usage |
|-------|-------|
| `text-end` | Action button columns, financial amounts, page header action area |
| `text-center` | Empty states, loading states |

---

## 5. Buttons

### Core Rule

**Only "Agregar"/"Crear" header buttons are SOLID.** All other buttons use outline variants.

| Button Type | Style | Example |
|-------------|-------|---------|
| **"+ Agregar X" (page header)** | `btn btn-primary` (solid) | "+ Agregar Usuario", "+ Agregar Solicitud" |
| **All other action buttons** | `btn btn-outline-*` (outline) | "Guardar Cambios", "Eliminar", "Ver", etc. |
| **SearchBar Limpiar** | `btn btn-outline-secondary rounded-4` | "Limpiar" |
| **Login submit** | `btn btn-primary` (solid) | "Iniciar Sesión" (main CTA only) |

### Color Semantics

| Action | Variant | Example |
|--------|---------|---------|
| **Create / Primary CTA** | `btn-primary` (solid) | "+ Agregar", "+ Agregar Usuario" |
| **Register (in detail footer)** | `btn-outline-primary` | "Registrar" |
| **Save changes** | `btn-outline-warning` | "Guardar Cambios" |
| **Close / Dismiss** | `btn-outline-info` | "Cerrar" |
| **Cancel / Volver** | `btn-outline-secondary` | "Cancelar", "Volver" |
| **Delete** | `btn-outline-danger` | "Eliminar" |
| **Recover/Reactivate** | `btn-outline-success` | "Recuperar" |
| **Approve** | `btn-outline-success` | "Aprobar", "Aceptar" |
| **Reject** | `btn-outline-danger` | "Rechazar", "Denegar" |
| **Observe** | `btn-outline-warning` or `btn-outline-info` | "Observar" |
| **Edit (inline)** | `btn-sm btn-outline-warning` | "Editar" |
| **View (inline)** | `btn-sm btn-outline-primary` | "Ver" |
| **Limpiar (filters)** | `btn-outline-secondary rounded-4` | "Limpiar" |

### Sizes

| Size | Context |
|------|---------|
| `btn-sm` | Inline table/card row actions only |
| Default | Detail footer buttons, page header buttons |
| `btn-lg` | Rare (Reportes "Generar Reporte" only) |

### Detail Footer Button Ordering

**Creation mode:**
```
[Registrar] btn-outline-primary  |  [Cerrar] btn-outline-info  |  [Cancelar] btn-outline-secondary
```

**Edit mode:**
```
[Guardar Cambios] btn-outline-warning  |  [Cerrar] btn-outline-info  |  [Eliminar] btn-outline-danger
```

**Inactive entity:**
```
[Guardar Cambios] btn-outline-warning  |  [Cerrar] btn-outline-info  |  [Recuperar] btn-outline-success
```

### Button Container

```html
<div class="p-3 d-flex flex-wrap-nowrap justify-content-end gap-3">
    <!-- buttons -->
</div>
```

### Loading State (Procesando)

Every action button uses `disabled="@Procesando"`:

```html
<button class="btn btn-outline-primary" disabled="@Procesando" @onclick="Crear">
    @(Procesando ? "Procesando..." : "Registrar")
</button>
```

Pattern: `private bool Procesando { get; set; } = false;` set to `true` at method entry, `false` in `finally`.

### Login Loading State

The login form uses a JavaScript-based loading animation because the form POST causes a full page navigation (Blazor Server disconnects during POST). A Blazor `Procesando` pattern cannot be used here.

```html
<form action="/auth/login" method="post" onsubmit="showLoginLoading(event)">
    <!-- ... form fields ... -->
    <button id="login-btn" type="submit" class="btn btn-primary">
        Iniciar sesion
    </button>
</form>

<script>
    function showLoginLoading(e) {
        e.preventDefault();
        var btn = document.getElementById('login-btn');
        btn.disabled = true;
        btn.innerHTML = '<span class="spinner-border spinner-border-sm me-2"></span>Iniciando sesión...';
        var form = e.target;
        setTimeout(function () { form.submit(); }, 60);
    }
</script>
```

The `e.preventDefault()` stops the immediate submit, changes the button state, then `setTimeout` with 60ms delay forces a browser repaint before `form.submit()` actually sends the POST. Without this delay, the browser blocks UI updates during the navigation.

### Approval Workflow Buttons

Approval actions use **outline** variants:

```html
<button class="btn btn-outline-success" disabled="@Procesando">Aprobar</button>
<button class="btn btn-outline-warning" disabled="@Procesando">Observar</button>
<button class="btn btn-outline-danger" disabled="@Procesando">Rechazar</button>
```

---

## 6. Forms

### Input Standard

```html
<input class="form-control rounded-4 bg-light" />
```

### Select Standard

```html
<select class="form-select rounded-4 bg-light" @bind="Value">
    <option value="0">Seleccione...</option>
    @foreach (var item in Items) {
        <option value="@item.Id">@item.Display</option>
    }
</select>
```

> **Rule:** `<input>` always uses `form-control`. `<select>` always uses `form-select`. Both always include `rounded-4` and `bg-light`.

### Input Sizes

| Size | Classes | Context |
|------|---------|---------|
| **Large** | `form-control-lg rounded-4 bg-light` | Entity detail/edit forms |
| **Default** | `form-control rounded-4 bg-light` | Search bars, inline editors |
| **Small** | `form-control-sm rounded-4 bg-light` | Compact forms (TransaccionDetalle) |

### Labels

**Standard:** `form-label small text-muted` — used on all form labels across search bars, detail forms, and editors.

```html
<label class="form-label small text-muted">Label text</label>
```

### Validation

**Field-level:**
```html
<input class="form-control rounded-4 bg-light @(Errores.ContainsKey("Field") ? "is-invalid" : "")" />
<div class="text-danger small mt-1">@Errores["Field"]</div>
```

**Form-level:**
```html
<div class="alert alert-danger alert-dismissible fade show mt-3" role="alert">
    @ErrorMessage
    <button type="button" class="btn-close" @onclick="() => ErrorMessage = string.Empty" aria-label="close"></button>
</div>
```

### Form Grid

**Search bars:**
```html
<div class="row g-2 align-items-end">
    <div class="col-12 col-md-3">
        <label class="form-label small text-muted">Filter</label>
        <input class="form-control bg-light rounded-4" />
    </div>
</div>
```

**Detail forms:**
```html
<div class="row">
    <div class="col-6 mt-3">...</div>
</div>
```

### Disabled States

```html
<input class="form-control form-control-lg rounded-4 bg-light" value="..." disabled="true" />
```

> **Rule:** Disabled inputs always keep `bg-light` background — no visual change.

---

## 7. Badges & Status

### Badge Shape

```html
<span class="badge rounded-pill px-2 ...">Status</span>
```

Almost all badges use `rounded-pill`. Detail page entity badges add `px-2`.

### Solicitud de Gasto Status Colors

| Status | Classes |
|--------|---------|
| `PENDIENTE` | `bg-warning-subtle text-warning-emphasis` |
| `APROBADO` | `bg-success-subtle text-success-emphasis` |
| `PAGADO` | `bg-primary-subtle text-primary-emphasis` |
| `RENDIDO` | `bg-info-subtle text-info-emphasis` |
| `RECHAZADO` | `bg-danger-subtle text-danger-emphasis` |
| `ANULADO` | `bg-secondary-subtle text-secondary-emphasis` |
| `OBSERVADO` | `bg-info-subtle text-info-emphasis` |

### Rendicion Status Colors

| Status | Classes |
|--------|---------|
| `EN_ESPERA` | `bg-warning-subtle text-warning-emphasis` |
| `OBSERVADO` | `bg-info-subtle text-info-emphasis` |
| `ACEPTADO` | `bg-success-subtle text-success-emphasis` |
| `DENEGADO` | `bg-danger-subtle text-danger-emphasis` |
| `ANULADO` | `bg-secondary-subtle text-secondary-emphasis` |

### Ciclo de Caja Chica Status Colors

| Status | Classes |
|--------|---------|
| `ABIERTO` | `bg-success-subtle text-success-emphasis` |
| `CERRADO` | `bg-secondary-subtle text-secondary-emphasis` |
| `LIQUIDADO` | `bg-info-subtle text-info-emphasis` |
| `EN_EXCEPCION` | `bg-warning-subtle text-warning-emphasis` |

### Entity Active/Inactive Status

**Detail pages (solid):**
| State | Classes |
|-------|---------|
| Active | `text-bg-success` |
| Inactive | `text-bg-secondary` |

**Item lists (subtle):**
| State | Classes |
|-------|---------|
| Active | `bg-success-subtle text-success-emphasis` |
| Inactive | `bg-secondary-subtle text-secondary-emphasis` |

### Transaccion Status/Type (Solid)

| Value | Classes |
|-------|---------|
| `DESEMBOLSO` | `bg-primary` |
| `DEVOLUCION_SOBRANTE` | `bg-success` |
| `REEMBOLSO_DEFICIT` | `bg-warning` |
| `REPOSICION_FONDO` | `bg-info` |
| `REGISTRADA` | `bg-warning` |
| `COMPLETADA` | `bg-success` |
| `ANULADA` | `bg-danger` |

---

## 8. List Items

### Item Card Pattern

```html
<div class="card rounded-4 p-3 mb-3 bg-light hover-shadow cursor-pointer" @onclick="AbrirDetalle">
    <div class="d-flex flex-row justify-content-between align-items-start">
        <div class="d-flex flex-column">
            <div class="d-flex justify-content-start align-items-center gap-3 mb-1">
                <p class="h5 mb-0">ID</p>
                <span class="badge rounded-pill ...">Status</span>
            </div>
            <p class="text-muted m-0 small">Details...</p>
        </div>
        <div class="d-flex flex-row align-items-center gap-3">
            <p class="h4 mb-0">S/ 1,234.56</p>
            <button class="btn btn-outline-primary">Ver</button>
        </div>
    </div>
</div>
```

### Item Spacing

All list items use `mb-3` as the standard margin.

| Type | Margin |
|------|--------|
| All items | `mb-3` |

### Hover Effects

| Effect | Classes | Defined in |
|--------|---------|------------|
| Card lift | `hover-shadow cursor-pointer` | `app.css` |
| Table row | `table-hover` | Bootstrap |
| Selection border | `border-primary` | Conditional class |

### Hover Shadow CSS

```css
.hover-shadow { transition: box-shadow 0.2s ease, transform 0.1s ease; }
.hover-shadow:hover { box-shadow: 0 0.5rem 1rem rgba(0,0,0,0.1); transform: translateY(-1px); }
```

---

## 9. Tables

### Table Pattern

```html
<div class="table-responsive">
    <table class="table table-hover align-middle mb-0">
        <thead class="table-light">
            <tr>
                <th>Column</th>
                <th class="text-end">Acciones</th>
            </tr>
        </thead>
        <tbody>
            <tr>
                <td>Data</td>
                <td class="text-end">
                    <button class="btn btn-sm btn-outline-warning me-1">Editar</button>
                    <button class="btn btn-sm btn-outline-danger">Eliminar</button>
                </td>
            </tr>
        </tbody>
    </table>
</div>
```

### Expandable Rows

Only in `CuentaBancariaListado.razor`:

```html
<td>
    <button class="btn btn-sm btn-link p-0" @onclick="Toggle">
        @(expanded ? "▼" : "▶")
    </button>
</td>
```

Expanded content uses `colspan` with `class="bg-light"`.

---

## 10. Search Bars

### Structure

All SearchBars render in a **single row** using `row g-2 align-items-end`. No multi-row layouts.

```html
<div class="card rounded-4 p-3 mb-3">
    <div class="row g-2 align-items-end">
        <div class="col-12 col-md-3">
            <label class="form-label small text-muted">Filter Label</label>
            <input class="form-control bg-light rounded-4" @bind="Value" />
        </div>
        <!-- More filters — all in the same row -->
        <div class="col-12 col-md-2 d-grid">
            <label class="form-label small text-muted">&nbsp;</label>
            <button class="btn btn-outline-secondary rounded-4" @onclick="Limpiar">Limpiar</button>
        </div>
    </div>
</div>
```

### Rules

- All filters in **one line** — use responsive `col-12 col-md-X` to stack on mobile
- "Limpiar" button is **always the last column**, wrapped in `d-grid`
- Labels use `form-label small text-muted`
- Inputs use `form-control bg-light rounded-4`
- Selects use `form-select bg-light rounded-4`
- Never use `form-control-sm` / `form-select-sm` in SearchBars (use default size)

### State Management

- Parent page owns all filter state
- SearchBar receives values via `[Parameter]` + `EventCallback<T>`
- Parent applies LINQ `Where()` chains in-memory
- `HayFiltrosActivos` computed property for empty-state messaging

### Limpiar Button

Always: `btn btn-outline-secondary rounded-4` with `@onclick="Limpiar"`.

---

## 11. Empty & Loading States

### Empty State

```html
<div class="d-flex justify-content-center align-items-center h-100">
    <p class="text-muted">
        @(HayFiltrosActivos ? "Ningun X coincide con los filtros" : "No se encontraron X")
    </p>
</div>
```

### Loading State

```html
<div class="d-flex justify-content-center align-items-center h-100">
    <p class="text-muted">Cargando X...</p>
</div>
```

### Error Alert

```html
<div class="alert alert-danger alert-dismissible fade show mt-3" role="alert">
    @ErrorMessage
    <button type="button" class="btn-close" @onclick="() => ErrorMessage = string.Empty" aria-label="close"></button>
</div>
```

### Warning Alert

```html
<div class="alert alert-warning d-flex align-items-center justify-content-between">
    <span><i class="bi bi-exclamation-triangle me-2"></i> Warning text</span>
    <button class="btn btn-sm btn-outline-warning">Action</button>
</div>
```

### Info Alert

```html
<div class="alert alert-info" role="alert">
    <i class="bi bi-info-circle me-2"></i> Informational text
</div>
```

---

## 12. Navigation

### Top Tab Bar

Horizontal tabs in `NavMenu.razor`, styled in `NavMenu.razor.css`:
- Active: `color: #2563eb; border-bottom: solid #2563eb`
- Hover: same blue border effect
- Font: `0.9rem`, height `3rem`
- Responsive: hamburger on mobile (`< 641px`)

### LiveClock

Appears in every page header (12 pages):

```html
<LiveClock />
```

Displays: `"lunes, 26 jun 2026 · 14:30:15"` (Spanish locale, updates every second).

### Role-Based Menu

| Role | Visible Pages |
|------|---------------|
| Administrador | Dashboard, Areas, Roles, Usuarios, Cuentas Bancarias, Caja Chica, Solicitudes de Gasto, Transacciones, Comprobantes de Pago, Rendiciones, Monedas |
| Jefe | Dashboard, Solicitudes de Gasto, Comprobantes de Pago, Rendiciones, Transacciones |
| Empleado | Dashboard, Mis Solicitudes de Gasto, Mis Comprobantes de Pago |
| Tesoreria | Dashboard, Caja Chica, Transacciones, Solicitudes de Gasto, Comprobantes de Pago, Rendiciones |

### Routing Conventions

- Routes use spaces: `/Caja Chica`, `/Solicitudes De Gasto`
- Detail pages share components with dual `@page` routes: `/Crear` and `/detalle`
- Query params for cross-entity linking: `?empleadoId=X`, `?areaId=X`

---

## 13. CSS Color Palette

### Primary Colors (from `app.css`)

| Token | Value | Usage |
|-------|-------|-------|
| Primary | `rgb(54, 98, 227)` | `.btn-primary` background |
| Primary hover | `rgb(40, 78, 195)` | `.btn-primary:hover` |
| Secondary | `rgb(225, 237, 254)` | `.btn-secondary` background (light blue) |
| Link | `#006bb7` | `a`, `.btn-link` |
| Nav active | `#2563eb` | Active nav tab |
| Nav inactive | `#717171` | Default nav text |
| Page bg | `rgb(248,250,252)` | `.page` background |
| Validation error | `#e50000` | `.validation-message` |
| Dropzone border | `#3b82f6` | `.dropzone` border |

### Payment Method Colors (`TransaccionItem.razor.css`)

| Method | Color |
|--------|-------|
| YAPE | `rgb(116, 70, 171)` (purple) |
| Efectivo | `rgb(49, 160, 58)` (green) |
| Transferencia | `rgb(110, 131, 222)` (blue) |

---

## 14. Responsive Breakpoints

| Breakpoint | Behavior |
|------------|----------|
| `< 641px` | Mobile: stacked layout, hamburger nav |
| `>= 641px` | Desktop: sidebar full-width, sticky header, horizontal tabs always visible |

Bootstrap grid breakpoints used in components: `col-md-*`, `col-lg-*` (standard Bootstrap 5.3.3).

---

## 15. Animations

| Animation | Location | Effect |
|-----------|----------|--------|
| `.hover-shadow` | `app.css` | Card lift on hover (shadow + translateY) |
| `.dropzone` | `app.css` | Background-color transition 0.2s ease |
| Reconnect modal | `ReconnectModal.razor.css` | Slide up + fade in/out + ripple |

---

## 16. Dashboard Patterns

### KPI Stat Card

```html
<div class="card rounded-4 h-100 cursor-pointer hover-shadow">
    <div class="card-body">
        <h6 class="card-subtitle mb-2 text-body-primary">Label</h6>
        <p class="card-text h2">123</p>
        <p class="card-text text-warning">Detail text</p>
    </div>
</div>
```

### Dashboard List Panel

```html
<div class="card rounded-4 h-100 d-flex flex-column overflow-hidden">
    <div class="p-3 pb-2">
        <p class="h5 mb-0">Section Title</p>
    </div>
    <hr class="my-0" />
    <div class="overflow-scroll flex-fill">
        <!-- Items -->
    </div>
</div>
```

### Progress Bar

```html
<div class="progress rounded-4 mt-2" style="height: 8px">
    <div class="progress-bar rounded-4 @(value >= 90 ? "bg-warning" : "bg-primary")"
         style="width: @(value)%"></div>
</div>
```

### Activity Item Icon Classes

Custom CSS classes for `ActividadRecienteItem` icon circles:

```css
.actividad-card {
    border-left: 4px solid transparent;
}

.actividad-icon {
    width: 40px;
    height: 40px;
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 1rem;
}

.actividad-icon-warning {
    background-color: #fff3cd;
    color: #ffc107;
}

.actividad-icon-primary {
    background-color: #cfe2ff;
    color: #0d6efd;
}

.actividad-icon-info {
    background-color: #cff4fc;
    color: #0dcaf0;
}

.actividad-icon-success {
    background-color: #d1e7dd;
    color: #198754;
}

.actividad-icon-secondary {
    background-color: #e2e3e5;
    color: #6c757d;
}

.actividad-icon-danger {
    background-color: #f8d7da;
    color: #dc3545;
}
```

---

## 17. Unified Design Rules

These rules summarize the **canonical standards** for the project. All components should follow these conventions:

| Rule | Standard | Notes |
|------|----------|-------|
| **Border radius** | `rounded-4` | Applied to all cards, inputs, selects, buttons (Limpiar). Never `rounded-3`. |
| **Input element** | `form-control` | For `<input>`, `<textarea>`, `<type="date">`, `<type="number">`. Always with `rounded-4 bg-light`. |
| **Select element** | `form-select` | For `<select>` only. Always with `rounded-4 bg-light`. Never `form-control` on `<select>`. |
| **Input background** | `bg-light` | Always present on all form inputs, including disabled and readonly. |
| **Form label** | `form-label small text-muted` | Unified across search bars, detail forms, and editors. |
| **List item margin** | `mb-3` | All list item cards use `mb-3`. |
| **Scroll behavior** | `overflow-scroll` | All scrollable containers (list panels, detail content areas). Structural wrappers use `overflow-hidden`. |
| **Card border-radius** | `rounded-4` + `app.css` override | `app.css` sets `.card { border-radius: 1rem; }` globally. `rounded-4` is applied inline for consistency. |
| **Button loading** | `disabled="@Procesando"` | Every action button must include this guard. Text changes to "Procesando..." when active. |
| **Empty state** | Context-aware message | Show different text based on `HayFiltrosActivos`. |
| **Font consistency** | Same component type = same font styling | All item cards (`*Item.razor`) must use the same heading level and text-muted pattern. All detail pages (`*Detalle.razor`) must use the same title size (`h3 mb-0`) and badge styling. All SearchBars must use identical label and input sizing. |
| **Search bar layout** | Single row, all filters inline | Every SearchBar must render all filters in one `row g-2 align-items-end` — no multi-row layouts. Use responsive `col-X col-md-Y` to stack on mobile. The "Limpiar" button is always the last column. |
| **Item card consistency** | Same structure across all `*Item.razor` | All item cards must follow: `card rounded-4 p-3 mb-3 bg-light` with a `d-flex flex-row justify-content-between align-items-start` inner layout. Left column = title + metadata. Right column = amount + action button. Badge pill inline with title. |
| **Detail page consistency** | Same structure across all `*Detalle.razor` | All detail pages must follow: card header with `h3 mb-0` + badge, `<hr class="my-0" />`, scrollable content area, `<hr class="my-0" />`, footer with `d-flex flex-wrap-nowrap justify-content-end gap-3`. |
| **Button style** | Agregar/Crear = solid, all others = outline | Only page header "+ Agregar X" buttons use `btn btn-primary`. All other buttons (Guardar, Eliminar, Ver, Aprobar, Rechazar, Limpiar, etc.) use `btn btn-outline-*`. |
| **Button text consistency** | Same action = same label | "Registrar" for create, "Guardar Cambios" for update, "Cerrar" for close, "Cancelar" for cancel, "Eliminar" for delete, "Recuperar" for restore. Never mix synonyms (e.g., "Crear" vs "Registrar"). |
| **Currency display** | `S/ ` prefix with 2 decimals | All monetary amounts display as `S/ 1,234.56` using `.ToString("N2")`. |
| **Date format** | `dd/MM/yyyy` | All dates displayed in day/month/year format. Times: `HH:mm`. |

---

## 18. Good Practices

### Component Organization

- **One component per file.** Each `*Item.razor`, `*Detalle.razor`, `*SearchBar.razor` is a single component. Never nest multiple components in one file.
- **Naming convention:** `EntityPage.razor` (list), `EntityItem.razor` (row), `EntityDetalle.razor` (detail/edit), `EntitySearchBar.razor` (filters), `EntityEditor.razor` (inline editor).
- **Shared components** go in `Components/Shared/`. Domain-specific components stay in their folder under `Components/Pages/Entity/`.

### State Management

- **Page owns state.** Parent pages hold all data and filter state. Child components receive data via `[Parameter]` and communicate back via `EventCallback`.
- **Never duplicate API calls.** If a parent already loaded data, pass it down — don't call the service again in a child component.
- **Use `OnInitializedAsync`** for initial data loading. Use `OnParametersSetAsync` only when re-rendering on parameter change is needed.
- **LINQ filtering in memory.** All search/filter operations run client-side on the already-fetched collection. No server-side queries per keystroke.

### API Calls & Error Handling

- **Wrap every `await` in `try/catch`.** Set `ErrorMessage` on catch. Never let exceptions bubble to the UI unhandled.
- **Use the `Procesando` pattern.** Set `Procesando = true` at method entry, `false` in `finally`. Apply `disabled="@Procesando"` on every action button.
- **Refresh after mutation.** After any insert/update/delete, re-fetch the list to ensure consistency: `await ListarItems();`.
- **Soft-delete pattern.** Never hard-delete. Use `EstaActivo` / `Activa` flag. Show "Eliminar" when active, "Recuperar" when inactive — both in the same button slot.

### Blazor-Specific

- **Use `@rendermode InteractiveServer`** on all pages that handle user interaction.
- **Avoid `StateHasChanged()` abuse.** Only call it explicitly after async void methods or event handlers that don't go through the Blazor lifecycle.
- **Use `@bind` for two-way binding** on forms. Use `value` + `@onchange` only when you need custom conversion logic.
- **Use `@onclick:stopPropagation`** on buttons inside clickable cards to prevent double-triggering.
- **Always inject services with `@inject`**, never use `new` or manual instantiation.
- **`IDisposable` for timers.** Any component using `System.Threading.Timer` or `PeriodicTimer` must implement `IDisposable` and cancel/dispose the timer.

### CSS & Styling

- **Prefer Bootstrap utility classes** over custom CSS. Use `d-flex`, `gap-3`, `mb-3`, `text-muted`, etc. before writing custom styles.
- **Scoped CSS files.** Component-specific styles go in `ComponentName.razor.css`. Use `::deep` for child component penetration.
- **No inline `style=` for colors.** Extract hardcoded colors (like `rgb(188, 94, 53)`) into a CSS class or reuse an existing Bootstrap utility.
- **No inline `style=` for sizing.** Use Bootstrap sizing utilities (`w-100`, `h-100`, `p-3`, etc.) instead of pixel values.
- **Consistent hover effects.** Use `hover-shadow cursor-pointer` on clickable cards. Use `table-hover` on tables. Never mix multiple hover mechanisms.
- **Progress bars** use `rounded-4` and 8px height: `style="height: 8px"`.

### Forms & Validation

- **Validate before API call.** Always call `Validar()` before any `await` to the backend. Return early if validation fails.
- **Dictionary-based validation.** Use `Dictionary<string, string> Errores` with `is-invalid` class binding. Show errors with `<div class="text-danger small mt-1">`.
- **Default option text:** `"Seleccione..."` for required fields, `"Todas"` / `"Todos"` for optional filters.
- **Disabled state on creation fields.** When editing an existing entity, lock fields that shouldn't change (e.g., owner on CuentaBancaria, Area on Rol) using `disabled="@(CuentaEnEdicion.IdCuenta > 0)"`.

### Accessibility & UX

- **Always provide `aria-label`** on `btn-close` dismissible alerts: `aria-label="close"`.
- **Use semantic HTML.** Use `<label>` for form labels, `<button>` for actions, `<a>` for navigation. Never use `<div @onclick>` for buttons.
- **Loading feedback.** Every async operation must show visual feedback: either button text changes ("Procesando...") or a spinner. Never leave the user staring at a frozen UI.
- **Confirm before destructive actions.** Use `JSRuntime.InvokeAsync<bool>("confirm", "...")` before any delete operation.
- **Empty states must be helpful.** Show different messages for "no data yet" vs "no results for current filters". Never show a blank screen.

### Performance

- **Lazy-load expandable content.** Only fetch nested data (e.g., CajasChicas of a CuentaBancaria) when the user expands the row — not on page load.
- **Avoid unnecessary re-renders.** Don't call `StateHasChanged()` in tight loops. Let Blazor's diffing handle most updates.
- **Paginate large lists** if a list exceeds ~50 items. (Not yet implemented — future consideration.)
