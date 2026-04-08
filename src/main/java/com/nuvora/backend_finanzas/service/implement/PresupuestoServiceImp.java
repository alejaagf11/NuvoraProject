package com.nuvora.backend_finanzas.service.implement;

import com.nuvora.backend_finanzas.dto.BudgetDTO;
import com.nuvora.backend_finanzas.dto.GastoFijoDTO;
import com.nuvora.backend_finanzas.entity.Usuario;
import com.nuvora.backend_finanzas.service.PresupuestoService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PresupuestoServiceImp implements PresupuestoService {

    private final Map<Long, BudgetDTO.Request> sesiones = new HashMap<>();

    @Override
    public BudgetDTO.Response generarPresupuesto(BudgetDTO.Request request, Usuario usuario) {
        if (request == null) {
            throw new RuntimeException("La solicitud no puede ser nula");
        }

        if (request.getIngreso() == null || request.getIngreso() <= 0) {
            throw new RuntimeException("Ingreso invalido");
        }

        if (request.getGastosFijos() == null) {
            request.setGastosFijos(new ArrayList<>());
        }

        if (request.getGastosVariables() == null) {
            request.setGastosVariables(new ArrayList<>());
        }

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

        double restante = ingreso - (deudas + esenciales + variables + ahorro + imprevistos);
        double estiloVida = Math.max(restante, 0);

        if ("estricto".equalsIgnoreCase(request.getEstiloVida())) {
            ahorro = ahorro * 1.20;
            estiloVida = Math.max(ingreso - (deudas + esenciales + variables + ahorro + imprevistos), 0);
        } else if ("flexible".equalsIgnoreCase(request.getEstiloVida())) {
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
    public String chatIA(String msj, Usuario usuario) {
        if (usuario == null || usuario.getUsuarioId() == null) {
            throw new RuntimeException("Usuario no autenticado");
        }

        if (msj == null || msj.trim().isEmpty()) {
            return "Escribe un mensaje valido. Ejemplo: gano 2 millones 500 mil";
        }

        String mensaje = normalizarTexto(msj);
        Long userId = usuario.getUsuarioId();

        BudgetDTO.Request req = sesiones.getOrDefault(userId, new BudgetDTO.Request());
        inicializarListas(req);
        sesiones.put(userId, req);

        if (mensaje.matches(".*\\b(hola|buenas|hey|holi)\\b.*")) {
            return "Hola 👋, soy tu asistente financiero 💰.\n"
                    + "Puedes escribir cosas como:\n"
                    + "- gano 2 millones 500 mil\n"
                    + "- deuda 500000\n"
                    + "- ahorro 300000\n"
                    + "- estilo estricto\n"
                    + "- tipo quincenal\n"
                    + "- gasto arriendo 800000, servicios 150000, mercado 30000 ";
        }

        if (mensaje.startsWith("gano") || mensaje.startsWith("ingreso")) {
            double ingreso = parseNumero(mensaje);
            if (ingreso > 0) {
                req.setIngreso(ingreso);
                sesiones.put(userId, req);
                return "Perfecto, guarde tu ingreso ✍️✍: $" + (long) ingreso
                        + "\nAhora si tienes deudas ingresalas sino ingresa deuda 0 ❌.";
            }
            return "No pude entender tu ingreso. Ejemplo: gano 2 millones 500 mil";
        }

        if (mensaje.startsWith("deuda") || mensaje.startsWith("deudas")) {
            double deudas = parseNumero(mensaje);
            req.setDeudas(deudas);
            sesiones.put(userId, req);
            return "Deudas guardadas: $" + (long) deudas
                    + "\n 💸 Ahora dime cuando dinero deseas ahorrar sino ahorro 0.";
        }

            if (mensaje.startsWith("ahorro")) {
                double ahorro = parseNumero(mensaje);
                if (ahorro < 0) {
                    return "No pude entender el ahorro. Ejemplo: ahorro 300000";
                }
                req.setAhorroDeseado(ahorro);
                sesiones.put(userId, req);
                return "Ahorro deseado guardado: $" + (long) ahorro
                        + "\n ahora escoje el estilo de vida que mas te identifique (estricto o flexible) 🫠";


            }


        if (mensaje.contains("estricto")) {
            req.setEstiloVida("estricto");
            sesiones.put(userId, req);
            return "Listo, usare un estilo de vida estricto ✔️."
            +"\n ¿el tipo de ingreso es semanal o quincenal? 🤔";
        }

        if (mensaje.contains("flexible")) {
            req.setEstiloVida("flexible");
            sesiones.put(userId, req);
            return "Listo, usare un estilo de vida flexible ✔️."
                    +"\n ¿el tipo de ingreso es semanal o quincenal? 🤔";
        }

        if (mensaje.contains("quincenal")) {
            req.setTipoIngreso("quincenal");
            sesiones.put(userId, req);
            return "Perfecto, registrare tu ingreso como quincenal 🤐."+
                    "\n ahora puedes ingresar tus gastos 😥";

        }

        if (mensaje.contains("semanal")) {
            req.setTipoIngreso("semanal");
            sesiones.put(userId, req);
            return "Perfecto, registrare tu ingreso como semanal 🤐."+
                    "\n ahora puedes ingresar tus gastos 😥";
        }

        if (mensaje.startsWith("gasto")) {
            registrarGastosDesdeTexto(msj, req);
            sesiones.put(userId, req);

            if (req.getIngreso() != null && req.getIngreso() > 0) {
                BudgetDTO.Response res = generarPresupuesto(req, usuario);
                return "Tu presupuesto esta listo:\n\n"
                        + "Esenciales: $" + (long) res.getEsenciales() + "\n"
                        + "Variables: $" + (long) res.getVariables() +"\n"
                        + "Deudas: $" + (long) res.getDeudas() + "\n"
                        + "Ahorro: $" + (long) res.getAhorro() + "\n"
                        + "Dinero para Ocio: $" + (long) res.getEstiloVida() + "\n"
                        + "Imprevistos: $" + (long) res.getImprevistos() + "\n"
                        + "Disponible por semana: $" + (long) res.getDisponibleSemanal() + "\n"
                        + "Disponible por quincena: " + res.getDisponibleQuincena();
            }

            return "Gastos guardados. Ahora dime tu ingreso para calcular el presupuesto. ✉️";
        }

        if (mensaje.contains("presupuesto") || mensaje.contains("calcular")) {
            if (req.getIngreso() == null || req.getIngreso() <= 0) {
                return "Aun no tengo tu ingreso. Ejemplo: gano 2 millones 500 mil";
            }

            BudgetDTO.Response res = generarPresupuesto(req, usuario);
            return "Tu presupuesto actual es:\n\n"
                    + "Esenciales: $" + (long) res.getEsenciales() + "\n"
                    + "Deudas: $" + (long) res.getDeudas() + "\n"
                    + "Ahorro: $" + (long) res.getAhorro() + "\n"
                    + "Dinero para Ocio: $" + (long) res.getEstiloVida() + "\n"
                    + "Imprevistos: $" + (long) res.getImprevistos() + "\n"
                    + "Disponible por semana: $" + (long) res.getDisponibleSemanal() + "\n"
                    + "Disponible por quincena: " + res.getDisponibleQuincena();
        }

        return "No entendi el mensaje.\n"
                + "Prueba con ejemplos como:\n"
                + "- gano 2 millones 500 mil\n"
                + "- deuda 400000\n"
                + "- ahorro 300000\n"
                + "- estilo estricto\n"
                + "- tipo quincenal\n"
                + "- gasto arriendo 800000 dia 5 mercado 250000 dia 15 transporte 120000 dia 20";
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


    private boolean esInicioDeNuevoGasto(String[] palabras, int index) {
        if ("dia".equals(palabras[index])) {
            return true;
        }

        if (index + 1 >= palabras.length) {
            return false;
        }

        return esNombreDeGasto(palabras[index]) && contieneNumero(palabras[index + 1]);
    }

    private boolean esNombreDeGasto(String palabra) {
        return palabra.matches("[a-zA-Záéíóúñ]+");
    }

    private boolean contieneNumero(String texto) {
        return texto.matches(".*\\d.*")
                || texto.equals("mil")
                || texto.equals("millon")
                || texto.equals("millones")
                || texto.equals("uno")
                || texto.equals("dos")
                || texto.equals("tres")
                || texto.equals("cuatro")
                || texto.equals("cinco")
                || texto.equals("seis")
                || texto.equals("siete")
                || texto.equals("ocho")
                || texto.equals("nueve")
                || texto.equals("diez")
                || texto.equals("cien")
                || texto.equals("ciento")
                || texto.equals("doscientos")
                || texto.equals("trescientos")
                || texto.equals("cuatrocientos")
                || texto.equals("quinientos")
                || texto.equals("seiscientos")
                || texto.equals("setecientos")
                || texto.equals("ochocientos")
                || texto.equals("novecientos");
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
        quincenas.put("Quincena 1 ", redondear(Math.max(quincena1 - gastosQ1, 0)));
        quincenas.put("Quincena 2 ", redondear(Math.max(quincena2 - gastosQ2, 0)));
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
}
