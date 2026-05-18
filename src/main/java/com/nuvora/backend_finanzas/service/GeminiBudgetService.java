package com.nuvora.backend_finanzas.service;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import com.nuvora.backend_finanzas.dto.BudgetProfileMemoryDTO;
import com.nuvora.backend_finanzas.dto.BudgetSummaryMemoryDTO;
import com.nuvora.backend_finanzas.dto.FinancialSnapshotDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class GeminiBudgetService {

    private final Client client;
    private final String model;

    public GeminiBudgetService(@Value("${gemini.api.key}") String apiKey,
                               @Value("${gemini.model}") String model) {
        this.client = Client.builder()
                .apiKey(apiKey)
                .build();
        this.model = model;
    }

    public String generarRespuestaPresupuesto(String mensajeUsuario,
                                              FinancialSnapshotDTO snapshot,
                                              BudgetProfileMemoryDTO profile,
                                              BudgetSummaryMemoryDTO summary) {

        String prompt = construirPrompt(mensajeUsuario, snapshot, profile, summary);

        GenerateContentResponse response = client.models.generateContent(model, prompt, null);
        return response.text();
    }

    private String construirPrompt(String mensajeUsuario,
                                   FinancialSnapshotDTO snapshot,
                                   BudgetProfileMemoryDTO profile,
                                   BudgetSummaryMemoryDTO summary) {

        String profileText = profile != null
                ? "Perfil aprendido: estilo=" + profile.getPreferredStyle()
                + ", tipoIngreso=" + profile.getIncomeType()
                + ", prioridadAhorro=" + profile.getSavingPriority()
                + ", nivelDeuda=" + profile.getDebtLevel()
                + ", categoriasPrincipales=" + profile.getMainExpenseCategories()
                : "Perfil aprendido: no disponible.";

        String summaryText = summary != null
                ? "Resumen previo: " + summary.getSummary()
                : "Resumen previo: no disponible.";

        return """
                Eres una asistente financiera especializada exclusivamente en finanzas personales y presupuestos personalizados.
                
                Reglas obligatorias:
                1. Solo puedes responder sobre presupuesto, ahorro, gastos, ingresos, deudas, metas financieras y organización financiera personal.
                2. Si el usuario pregunta algo fuera de finanzas personales, responde brevemente que solo puedes ayudar con presupuestos y temas financieros personales.
                3. Debes responder en español claro y práctico.
                4. Debes basarte en la información real del usuario.
                5. Si falta información, puedes decirlo, pero intenta dar una recomendación útil con lo disponible.
                
                Contexto financiero actual del usuario:
                Usuario ID: """ + snapshot.getUsuarioId() + """
                Ingreso mensual base: """ + snapshot.getMontoMensualUsuario() + """
                Ingresos registrados: """ + snapshot.getTotalIngresos() + """
                Gastos registrados: """ + snapshot.getTotalGastos() + """
                Ahorro actual en metas: """ + snapshot.getTotalAhorradoMetas() + """
                Objetivo total en metas: """ + snapshot.getTotalObjetivoMetas() + """
                Saldo estimado: """ + snapshot.getSaldoEstimado() + """
                Gastos por categoria: """ + snapshot.getGastosPorCategoria() + """
                Resumen financiero: """ + snapshot.getResumenGeneral() + """
                
                """ + profileText + """
                """ + summaryText + """
                
                Mensaje del usuario:
                """ + mensajeUsuario + """
                
                Si el usuario pide generar o mejorar un presupuesto, responde con:
                - un resumen corto
                - recomendaciones claras
                - alertas si aplica
                
                Si el usuario solo hace una pregunta financiera, responde de forma breve, útil y concreta.
                """;
    }

}
