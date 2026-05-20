package com.nuvora.backend_finanzas.service.implement;

import com.nuvora.backend_finanzas.dto.*;
import com.nuvora.backend_finanzas.entity.Usuario;
import com.nuvora.backend_finanzas.service.FinancialSnapshotService;
import com.nuvora.backend_finanzas.service.FirestoreChatMemoryService;
import com.nuvora.backend_finanzas.service.GeminiBudgetService;
import com.nuvora.backend_finanzas.service.PresupuestoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//actualizado
@Service
public class PresupuestoServiceImp implements PresupuestoService {

    private final Map<Long, BudgetDTO.Request> sesiones = new HashMap<>();

    @Autowired
    private GeminiBudgetService geminiBudgetService;

    @Autowired
    private FinancialSnapshotService financialSnapshotService;

    @Autowired
    private FirestoreChatMemoryService firestoreChatMemoryService;

    @Override
    public BudgetDTO.Response generarPresupuesto(BudgetDTO.Request request, Usuario usuario) {
        if (request == null) {
            throw new RuntimeException("La solicitud no puede ser nula");
        }

        if (request.getIngreso() == null || request.getIngreso() <= 0) {
            throw new RuntimeException("Ingreso invalido");
        }

        inicializarListas(request);

        double ingreso = request.getIngreso();
        double deudas = request.getDeudas() != null ? request.getDeudas() : 0.0;

        double esenciales = request.getGastosFijos().stream()
                .mapToDouble(GastoFijoDTO::getMonto)
                .sum();

        double variables = request.getGastosVariables().stream()
                .mapToDouble(GastoFijoDTO::getMonto)
                .sum();

        double ahorro = request.getAhorroDeseado() != null ? request.getAhorroDeseado() : ingreso * 0.10;
        double imprevistos = ingreso * 0.05;

        double totalGastos = deudas + esenciales + variables;
        if (totalGastos > ingreso) {
            throw new RuntimeException(
                    "Tus gastos y deudas superan tus ingresos. Debes reducir gastos antes de generar un presupuesto."
            );
        }

        double totalComprometido = totalGastos + ahorro + imprevistos;
        if (totalComprometido > ingreso) {
            throw new RuntimeException(
                    "Con ahorro e imprevistos incluidos, el presupuesto supera tus ingresos. Ajusta los valores para continuar."
            );
        }

        if ("estricto".equalsIgnoreCase(request.getEstiloVida())) {
            ahorro = ahorro * 1.20;
            totalComprometido = totalGastos + ahorro + imprevistos;

            if (totalComprometido > ingreso) {
                throw new RuntimeException(
                        "Con estilo estricto, ahorro e imprevistos, el presupuesto supera tus ingresos."
                );
            }
        }

        double restante = ingreso - totalComprometido;
        double estiloVida = Math.max(restante, 0);

        if ("flexible".equalsIgnoreCase(request.getEstiloVida())) {
            estiloVida = estiloVida * 1.10;
        }

        double disponibleSemanal = estiloVida / 4.0;

        Map<String, Double> quincenas = new HashMap<>();
        if ("quincenal".equalsIgnoreCase(request.getTipoIngreso())) {
            quincenas = calcularQuincena(request, ingreso, deudas, ahorro, imprevistos);
        }

        BudgetDTO.Response response = new BudgetDTO.Response();
        response.setEsenciales(redondear(esenciales));
        response.setVariables(redondear(variables));
        response.setDeudas(redondear(deudas));
        response.setAhorro(redondear(ahorro));
        response.setEstiloVida(redondear(estiloVida));
        response.setImprevistos(redondear(imprevistos));
        response.setDisponibleSemanal(redondear(disponibleSemanal));
        response.setDisponibleQuincena(quincenas);

        return response;
    }


    @Override
    public BudgetGenerateAiResponseDTO generarPresupuestoIA(BudgetDTO.Request request, Usuario usuario) {
        BudgetDTO.Response presupuestoBase = generarPresupuesto(request, usuario);

        try {
            FinancialSnapshotDTO snapshot = financialSnapshotService.buildSnapshot(usuario);
            BudgetProfileMemoryDTO profile = firestoreChatMemoryService.getProfile(usuario.getUsuarioId());
            BudgetSummaryMemoryDTO summary = firestoreChatMemoryService.getSummary(usuario.getUsuarioId());

            String prompt = "Genera una explicacion y recomendaciones para este presupuesto personal. "
                    + "Esenciales: " + presupuestoBase.getEsenciales()
                    + ", Variables: " + presupuestoBase.getVariables()
                    + ", Deudas: " + presupuestoBase.getDeudas()
                    + ", Ahorro: " + presupuestoBase.getAhorro()
                    + ", Imprevistos: " + presupuestoBase.getImprevistos()
                    + ", Estilo de vida: " + presupuestoBase.getEstiloVida()
                    + ", Disponible semanal: " + presupuestoBase.getDisponibleSemanal()
                    + ", Disponible por quincena: " + presupuestoBase.getDisponibleQuincena()
                    + ". Contexto financiero del usuario: " + snapshot.getResumenGeneral()
                    + ". Responde en espanol con un resumen claro, recomendaciones practicas y alertas si aplican.";

            String respuesta = geminiBudgetService.generarRespuestaPresupuesto(prompt, snapshot, profile, summary);

            BudgetGenerateAiResponseDTO dto = new BudgetGenerateAiResponseDTO();
            dto.setBudget(presupuestoBase);
            dto.setResumen(respuesta);
            dto.setRecomendaciones(new ArrayList<>());
            dto.setAlertas(new ArrayList<>());
            dto.setSource("gemini");

            return dto;

        } catch (Exception e) {
            e.printStackTrace();

            BudgetGenerateAiResponseDTO dto = new BudgetGenerateAiResponseDTO();
            dto.setBudget(presupuestoBase);
            dto.setResumen("Presupuesto generado correctamente con la logica base del sistema.");
            dto.setRecomendaciones(new ArrayList<>());
            dto.setAlertas(new ArrayList<>());
            dto.setSource("fallback");

            return dto;
        }
    }


    @Override
    public BudgetChatResponseDTO chatIA(String msj, Usuario usuario) {
        if (usuario == null || usuario.getUsuarioId() == null) {
            throw new RuntimeException("Usuario no autenticado");
        }

        if (msj == null || msj.trim().isEmpty()) {
            return new BudgetChatResponseDTO(
                    "Escribe una pregunta o solicitud relacionada con presupuesto o finanzas personales.",
                    "fallback"
            );
        }

        try {
            FinancialSnapshotDTO snapshot = financialSnapshotService.buildSnapshot(usuario);
            BudgetProfileMemoryDTO profile = firestoreChatMemoryService.getProfile(usuario.getUsuarioId());
            BudgetSummaryMemoryDTO summary = firestoreChatMemoryService.getSummary(usuario.getUsuarioId());

            String sessionId = "presupuesto-main";

            firestoreChatMemoryService.createSessionIfNotExists(
                    usuario.getUsuarioId(),
                    sessionId,
                    "Sesion principal de presupuesto"
            );

            firestoreChatMemoryService.saveMessage(
                    usuario.getUsuarioId(),
                    sessionId,
                    new ChatMessageMemoryDTO("user", msj, LocalDateTime.now().toString())
            );

            String respuesta = geminiBudgetService.generarRespuestaPresupuesto(msj, snapshot, profile, summary);

            if (respuesta == null || respuesta.isBlank()) {
                return new BudgetChatResponseDTO(chatIAReglas(msj, usuario), "fallback");
            }

            firestoreChatMemoryService.saveMessage(
                    usuario.getUsuarioId(),
                    sessionId,
                    new ChatMessageMemoryDTO("model", respuesta, LocalDateTime.now().toString())
            );

            BudgetProfileMemoryDTO nuevoProfile = construirProfileDesdeUsuario(usuario, snapshot);
            BudgetSummaryMemoryDTO nuevoSummary = construirSummaryDesdeRespuesta(respuesta);

            firestoreChatMemoryService.saveProfile(usuario.getUsuarioId(), nuevoProfile);
            firestoreChatMemoryService.saveSummary(usuario.getUsuarioId(), nuevoSummary);

            return new BudgetChatResponseDTO(respuesta, "gemini");

        } catch (Exception e) {
            e.printStackTrace();
            return new BudgetChatResponseDTO(chatIAReglas(msj, usuario), "fallback");
        }
    }


    private String chatIAReglas(String msj, Usuario usuario) {
        String mensaje = normalizarTexto(msj);
        Long userId = usuario.getUsuarioId();

        BudgetDTO.Request req = sesiones.getOrDefault(userId, new BudgetDTO.Request());
        inicializarListas(req);
        sesiones.put(userId, req);

        if (mensaje.matches(".*\\b(hola|buenas|hey|holi)\\b.*")) {
            return "Hola, soy Nuvy, tu asistente financiera. Puedo ayudarte con presupuestos, ahorro y organización de gastos.";
        }

        if (mensaje.startsWith("gano") || mensaje.startsWith("ingreso")) {
            double ingreso = parseNumero(mensaje);
            if (ingreso > 0) {
                req.setIngreso(ingreso);
                sesiones.put(userId, req);
                return "Perfecto, guardé tu ingreso: $" + (long) ingreso
                        + "\nAhora, si tienes deudas, escríbelas. Si no, escribe deuda 0.";
            }
            return "No pude entender tu ingreso. Ejemplo: gano 2 millones 500 mil";
        }

        if (mensaje.startsWith("deuda") || mensaje.startsWith("deudas")) {
            double deudas = parseNumero(mensaje);
            req.setDeudas(deudas);
            sesiones.put(userId, req);

            if (req.getIngreso() != null && req.getIngreso() > 0) {
                try {
                    generarPresupuesto(req, usuario);
                } catch (RuntimeException e) {
                    return e.getMessage();
                }
            }

            return "Deudas guardadas: $" + (long) deudas
                    + "\nAhora dime cuánto dinero deseas ahorrar, o escribe ahorro 0.";
        }

        if (mensaje.startsWith("ahorro")) {
            double ahorro = parseNumero(mensaje);
            if (ahorro < 0) {
                return "No pude entender el ahorro. Ejemplo: ahorro 300000";
            }
            req.setAhorroDeseado(ahorro);
            sesiones.put(userId, req);
            return "Ahorro deseado guardado: $" + (long) ahorro
                    + "\nAhora escoge el estilo de vida que más te identifique: estricto o flexible.";
        }

        if (mensaje.contains("estricto")) {
            req.setEstiloVida("estricto");
            sesiones.put(userId, req);
            return "Listo, usaré un estilo de vida estricto.\n¿Tu tipo de ingreso es semanal o quincenal?";
        }

        if (mensaje.contains("flexible")) {
            req.setEstiloVida("flexible");
            sesiones.put(userId, req);
            return "Listo, usaré un estilo de vida flexible.\n¿Tu tipo de ingreso es semanal o quincenal?";
        }

        if (mensaje.contains("quincenal")) {
            req.setTipoIngreso("quincenal");
            sesiones.put(userId, req);
            return "Perfecto, registré tu ingreso como quincenal.\nAhora puedes ingresar tus gastos.";
        }

        if (mensaje.contains("semanal")) {
            req.setTipoIngreso("semanal");
            sesiones.put(userId, req);
            return "Perfecto, registré tu ingreso como semanal.\nAhora puedes ingresar tus gastos.";
        }

        if (mensaje.startsWith("gasto")) {
            registrarGastosDesdeTexto(msj, req);
            sesiones.put(userId, req);

            if (req.getIngreso() != null && req.getIngreso() > 0) {
                try {
                    BudgetDTO.Response res = generarPresupuesto(req, usuario);
                    return "Tu presupuesto está listo:\n\n"
                            + "Esenciales: $" + (long) res.getEsenciales() + "\n"
                            + "Variables: $" + (long) res.getVariables() + "\n"
                            + "Deudas: $" + (long) res.getDeudas() + "\n"
                            + "Ahorro: $" + (long) res.getAhorro() + "\n"
                            + "Dinero para ocio: $" + (long) res.getEstiloVida() + "\n"
                            + "Imprevistos: $" + (long) res.getImprevistos() + "\n"
                            + "Disponible por semana: $" + (long) res.getDisponibleSemanal() + "\n"
                            + "Disponible por quincena: " + res.getDisponibleQuincena();
                } catch (RuntimeException e) {
                    return e.getMessage();
                }
            }

            return "Gastos guardados. Ahora dime tu ingreso para calcular el presupuesto.";
        }

        if (mensaje.contains("presupuesto") || mensaje.contains("calcular")) {
            if (req.getIngreso() == null || req.getIngreso() <= 0) {
                return "Aún no tengo tu ingreso. Ejemplo: gano 2 millones 500 mil";
            }

            try {
                BudgetDTO.Response res = generarPresupuesto(req, usuario);
                return "Tu presupuesto actual es:\n\n"
                        + "Esenciales: $" + (long) res.getEsenciales() + "\n"
                        + "Variables: $" + (long) res.getVariables() + "\n"
                        + "Deudas: $" + (long) res.getDeudas() + "\n"
                        + "Ahorro: $" + (long) res.getAhorro() + "\n"
                        + "Dinero para ocio: $" + (long) res.getEstiloVida() + "\n"
                        + "Imprevistos: $" + (long) res.getImprevistos() + "\n"
                        + "Disponible por semana: $" + (long) res.getDisponibleSemanal() + "\n"
                        + "Disponible por quincena: " + res.getDisponibleQuincena();
            } catch (RuntimeException e) {
                return e.getMessage();
            }
        }

        return "Puedo ayudarte con presupuesto, ahorro, gastos e ingresos.\n"
                + "Prueba con ejemplos como:\n"
                + "- gano 2 millones 500 mil\n"
                + "- deuda 400000\n"
                + "- ahorro 300000\n"
                + "- estilo estricto\n"
                + "- tipo quincenal\n"
                + "- gasto arriendo 800000 dia 5, mercado 250000 dia 15, transporte 120000 dia 20";
    }

    private void registrarGastosDesdeTexto(String mensaje, BudgetDTO.Request req) {
        String contenido = mensaje.replaceFirst("^gasto\\s+", "").trim();

        if (contenido.isEmpty()) {
            return;
        }

        List<GastoFijoDTO> gastosFijos = req.getGastosFijos();
        List<GastoFijoDTO> gastosVariables = req.getGastosVariables();

        String[] bloques = contenido.split(",");

        for (String bloque : bloques) {
            String item = bloque.trim();
            if (item.isEmpty()) {
                continue;
            }

            String[] tokens = item.split("\\s+");
            if (tokens.length < 2) {
                continue;
            }

            String nombre = tokens[0];
            int diaPago = 1;

            StringBuilder montoTexto = new StringBuilder();

            for (int i = 1; i < tokens.length; i++) {
                if ("dia".equalsIgnoreCase(tokens[i])) {
                    if (i + 1 < tokens.length && tokens[i + 1].matches("\\d+")) {
                        diaPago = Integer.parseInt(tokens[i + 1]);
                    }
                    break;
                }
                montoTexto.append(tokens[i]).append(" ");
            }

            double monto = parseNumero(montoTexto.toString().trim());
            if (monto <= 0) {
                continue;
            }

            GastoFijoDTO gasto = new GastoFijoDTO();
            gasto.setNombre(nombre);
            gasto.setMonto(monto);
            gasto.setDiaPago(diaPago);

            if (esGastoFijo(nombre)) {
                gastosFijos.add(gasto);
            } else {
                gastosVariables.add(gasto);
            }
        }
    }

    private boolean esGastoFijo(String nombre) {
        String gasto = nombre.toLowerCase();
        return gasto.equals("arriendo")
                || gasto.equals("servicios")
                || gasto.equals("internet")
                || gasto.equals("luz")
                || gasto.equals("agua")
                || gasto.equals("gas")
                || gasto.equals("colegio")
                || gasto.equals("universidad")
                || gasto.equals("credito")
                || gasto.equals("prestamo")
                || gasto.equals("seguro")
                || gasto.equals("telefono");
    }

    private Map<String, Double> calcularQuincena(BudgetDTO.Request request,
                                                 double ingreso,
                                                 double deudas,
                                                 double ahorro,
                                                 double imprevistos) {
        double quincena1 = ingreso / 2.0;
        double quincena2 = ingreso / 2.0;

        double gastosQ1 = 0.0;
        double gastosQ2 = 0.0;

        for (GastoFijoDTO g : request.getGastosFijos()) {
            if (g.getDiaPago() <= 15) {
                gastosQ1 += g.getMonto();
            } else {
                gastosQ2 += g.getMonto();
            }
        }

        for (GastoFijoDTO g : request.getGastosVariables()) {
            if (g.getDiaPago() <= 15) {
                gastosQ1 += g.getMonto();
            } else {
                gastosQ2 += g.getMonto();
            }
        }

        gastosQ1 += (deudas / 2.0) + (ahorro / 2.0) + (imprevistos / 2.0);
        gastosQ2 += (deudas / 2.0) + (ahorro / 2.0) + (imprevistos / 2.0);

        Map<String, Double> quincenas = new HashMap<>();
        quincenas.put("Quincena 1", redondear(Math.max(quincena1 - gastosQ1, 0)));
        quincenas.put("Quincena 2", redondear(Math.max(quincena2 - gastosQ2, 0)));
        return quincenas;
    }

    private double parseNumero(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            return 0;
        }

        texto = normalizarTexto(texto);

        String[] tokens = texto.split("\\s+");
        double total = 0;
        double actual = 0;

        Map<String, Long> palabras = new HashMap<>();
        palabras.put("uno", 1L);
        palabras.put("dos", 2L);
        palabras.put("tres", 3L);
        palabras.put("cuatro", 4L);
        palabras.put("cinco", 5L);
        palabras.put("seis", 6L);
        palabras.put("siete", 7L);
        palabras.put("ocho", 8L);
        palabras.put("nueve", 9L);
        palabras.put("diez", 10L);
        palabras.put("once", 11L);
        palabras.put("doce", 12L);
        palabras.put("trece", 13L);
        palabras.put("catorce", 14L);
        palabras.put("quince", 15L);
        palabras.put("veinte", 20L);
        palabras.put("treinta", 30L);
        palabras.put("cuarenta", 40L);
        palabras.put("cincuenta", 50L);
        palabras.put("sesenta", 60L);
        palabras.put("setenta", 70L);
        palabras.put("ochenta", 80L);
        palabras.put("noventa", 90L);
        palabras.put("cien", 100L);
        palabras.put("ciento", 100L);
        palabras.put("doscientos", 200L);
        palabras.put("trescientos", 300L);
        palabras.put("cuatrocientos", 400L);
        palabras.put("quinientos", 500L);
        palabras.put("seiscientos", 600L);
        palabras.put("setecientos", 700L);
        palabras.put("ochocientos", 800L);
        palabras.put("novecientos", 900L);

        for (String token : tokens) {
            if (token.matches("\\d+")) {
                actual += Double.parseDouble(token);
                continue;
            }

            if (palabras.containsKey(token)) {
                actual += palabras.get(token);
                continue;
            }

            if ("mil".equals(token)) {
                if (actual == 0) {
                    actual = 1;
                }
                actual *= 1000;
                total += actual;
                actual = 0;
                continue;
            }

            if ("millon".equals(token) || "millones".equals(token)) {
                if (actual == 0) {
                    actual = 1;
                }
                actual *= 1_000_000;
                total += actual;
                actual = 0;
            }
        }

        return total + actual;
    }

    private void inicializarListas(BudgetDTO.Request request) {
        if (request.getGastosFijos() == null) {
            request.setGastosFijos(new ArrayList<>());
        }
        if (request.getGastosVariables() == null) {
            request.setGastosVariables(new ArrayList<>());
        }
    }

    private String normalizarTexto(String texto) {
        return texto.toLowerCase()
                .replace("á", "a")
                .replace("é", "e")
                .replace("í", "i")
                .replace("ó", "o")
                .replace("ú", "u")
                .replace("millón", "millon")
                .replaceAll("[^a-z0-9\\s]", " ")
                .replaceAll("\\s+", " ")
                .trim();
    }

    private double redondear(double valor) {
        return Math.round(valor * 100.0) / 100.0;
    }

    private BudgetProfileMemoryDTO construirProfileDesdeUsuario(Usuario usuario, FinancialSnapshotDTO snapshot) {
        BudgetProfileMemoryDTO profile = new BudgetProfileMemoryDTO();
        profile.setPreferredStyle("no definido");
        profile.setIncomeType("no definido");
        profile.setSavingPriority(snapshot.getTotalAhorradoMetas() > 0 ? "media" : "baja");
        profile.setDebtLevel(snapshot.getSaldoEstimado() < 0 ? "alta" : "media");
        profile.setMainExpenseCategories(new ArrayList<>(snapshot.getGastosPorCategoria().keySet()));
        profile.setLastUpdated(LocalDateTime.now().toString());
        return profile;
    }

    private BudgetSummaryMemoryDTO construirSummaryDesdeRespuesta(String respuesta) {
        BudgetSummaryMemoryDTO summary = new BudgetSummaryMemoryDTO();
        summary.setSummary(respuesta);
        summary.setLastBudgetRecommendation(respuesta);
        summary.setUpdatedAt(LocalDateTime.now().toString());
        return summary;
    }


}
