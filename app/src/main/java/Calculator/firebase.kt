package Calculator

import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions


public class FirebaseDb {
    public fun saveDeviceData(db: FirebaseFirestore, calculatorHistory: MutableList<String>, deviceId:String) {
        val deviceData = hashMapOf(
            "calculatorHistory" to calculatorHistory
        )
        db.collection("devices").document(deviceId)
            .set(deviceData, SetOptions.merge())
            .addOnSuccessListener {
                Log.d(TAG, "DocumentSnapshot successfully written!") }
            .addOnFailureListener {
                e -> Log.w(TAG, "Error writing document", e) }
    }

    public fun loadHistoryData(db: FirebaseFirestore, deviceId: String): String {
        val history = db.collection("devices").document(deviceId)
        lateinit var fieldValue: String
        history.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    fieldValue = document.getString("calculatorHistory").toString()
                    Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                } else {
                    Log.d(TAG, "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
            }
        return fieldValue
    }

}