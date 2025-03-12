package Calculator

import android.content.ContentValues.TAG
import android.provider.Settings
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.firestore

public class ThemeSaving {
    public fun saveDeviceData(db: FirebaseFirestore, theme: Boolean, deviceId:String) {
        val deviceData = hashMapOf(
            "themeIsNight" to theme
        )
        db.collection("devices").document(deviceId)
            .set(deviceData, SetOptions.merge())
            .addOnSuccessListener {
                Log.d(TAG, "DocumentSnapshot successfully written!") }
            .addOnFailureListener {
                    e -> Log.w(TAG, "Error writing document", e) }
    }
    public fun loadDeviceData(db: FirebaseFirestore, deviceId: String, callback: (Boolean) -> Unit) {
        val historyDB = db.collection("devices").document(deviceId)
        historyDB.get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val themeBoolean = document.get("themeIsNight") as? Boolean ?: false
                    callback(themeBoolean) // Вызываем callback с результатом
                } else {
                    callback(false) // Если документ не существует, возвращаем false
                }
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error loading document", e)
                callback(false) // В случае ошибки возвращаем false
            }
    }

}