package com.example.calculator111

import Calculator.FirebaseDb
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.firestore

class HistoryActivity : AppCompatActivity() {
    private lateinit var adapter: HistoryAdapter
    private lateinit var calchistory: MutableList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_history)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Инициализация списка и адаптера
        calchistory = mutableListOf()
        adapter = HistoryAdapter(calchistory)

        // Настройка RecyclerView
        val recyclerViewHistory: RecyclerView = findViewById(R.id.recyclerViewHistory)
        recyclerViewHistory.layoutManager = LinearLayoutManager(this)
        recyclerViewHistory.adapter = adapter

        // Загрузка данных из Firestore
        loadHistoryFromFirestore()
    }

    private fun loadHistoryFromFirestore() {
        val deviceId = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
        val db = Firebase.firestore
        val historyDB = db.collection("devices").document(deviceId)

        historyDB.get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    // Получаем список calculatorHistory из документа
                    val calculatorHistory = document.get("calculatorHistory") as? MutableList<String>
                    if (calculatorHistory != null) {
                        // Очищаем текущий список и добавляем новые данные
                        calchistory.clear()
                        calchistory.addAll(calculatorHistory)
                        // Уведомляем адаптер об изменениях
                        adapter.notifyDataSetChanged()
                    } else {
                        // Обработка ошибки: данные не найдены
                        calchistory.add("Error: History not found")
                        adapter.notifyItemInserted(calchistory.size - 1)
                    }
                } else {
                    // Обработка ошибки: документ не существует
                    calchistory.add("Error: Document does not exist")
                    adapter.notifyItemInserted(calchistory.size - 1)
                }
            }
            .addOnFailureListener { e ->
                // Обработка ошибки при загрузке данных
                calchistory.add("Error: ${e.message}")
                adapter.notifyItemInserted(calchistory.size - 1)
            }
    }
}

class HistoryAdapter(private val historyList: MutableList<String>) :
    RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewHistoryItem: TextView = itemView.findViewById(R.id.textViewHistoryItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item, parent, false)
        return HistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val historyItem = historyList[position]
        holder.textViewHistoryItem.text = historyItem
    }

    override fun getItemCount(): Int {
        return historyList.size
    }

    fun addItem(item: String) {
        historyList.add(item)
        notifyItemInserted(historyList.size - 1)
    }


    fun updateItem(position: Int, newItem: String) {
        if (position in 0 until historyList.size) {
            historyList[position] = newItem
            notifyItemChanged(position)
        }
    }

    fun removeItem(position: Int) {
        if (position in 0 until historyList.size) {
            historyList.removeAt(position)
            notifyItemRemoved(position)
        }
    }
}