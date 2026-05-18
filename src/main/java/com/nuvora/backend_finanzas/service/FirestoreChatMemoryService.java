package com.nuvora.backend_finanzas.service;

import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.nuvora.backend_finanzas.dto.BudgetProfileMemoryDTO;
import com.nuvora.backend_finanzas.dto.BudgetSummaryMemoryDTO;
import com.nuvora.backend_finanzas.dto.ChatMessageMemoryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class FirestoreChatMemoryService {
    @Autowired
    private Firestore firestore;

    public void saveProfile(Long userId, BudgetProfileMemoryDTO profile) throws Exception {
        firestore.collection("users")
                .document(String.valueOf(userId))
                .collection("profile")
                .document("main")
                .set(profile)
                .get();
    }

    public BudgetProfileMemoryDTO getProfile(Long userId) throws Exception {
        DocumentSnapshot snapshot = firestore.collection("users")
                .document(String.valueOf(userId))
                .collection("profile")
                .document("main")
                .get()
                .get();

        if (!snapshot.exists()) {
            return null;
        }

        return snapshot.toObject(BudgetProfileMemoryDTO.class);
    }

    public void saveSummary(Long userId, BudgetSummaryMemoryDTO summary) throws Exception {
        firestore.collection("users")
                .document(String.valueOf(userId))
                .collection("budget_summary")
                .document("main")
                .set(summary)
                .get();
    }

    public BudgetSummaryMemoryDTO getSummary(Long userId) throws Exception {
        DocumentSnapshot snapshot = firestore.collection("users")
                .document(String.valueOf(userId))
                .collection("budget_summary")
                .document("main")
                .get()
                .get();

        if (!snapshot.exists()) {
            return null;
        }

        return snapshot.toObject(BudgetSummaryMemoryDTO.class);
    }

    public void saveMessage(Long userId, String sessionId, ChatMessageMemoryDTO message) throws Exception {
        firestore.collection("users")
                .document(String.valueOf(userId))
                .collection("sessions")
                .document(sessionId)
                .collection("messages")
                .add(message)
                .get();
    }

    public void createSessionIfNotExists(Long userId, String sessionId, String title) throws Exception {
        Map<String, Object> sessionData = new HashMap<>();
        sessionData.put("title", title);
        sessionData.put("active", true);
        sessionData.put("createdAt", java.time.LocalDateTime.now().toString());

        firestore.collection("users")
                .document(String.valueOf(userId))
                .collection("sessions")
                .document(sessionId)
                .set(sessionData)
                .get();
    }
}
