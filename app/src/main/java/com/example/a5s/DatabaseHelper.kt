package com.example.a5s

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "app_database.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_GESTOR = "gestores"
        private const val TABLE_COLABORADOR = "colaboradores"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createGestorTable = ("CREATE TABLE $TABLE_GESTOR (id INTEGER PRIMARY KEY AUTOINCREMENT, matricula INTEGER, senha INTEGER)")
        val createColaboradorTable = ("CREATE TABLE $TABLE_COLABORADOR (id INTEGER PRIMARY KEY AUTOINCREMENT, gestor_id INTEGER, nome TEXT, cargo TEXT, maquina TEXT, matricula INTEGER, senha INTEGER, FOREIGN KEY(gestor_id) REFERENCES $TABLE_GESTOR(id))")
        db.execSQL(createGestorTable)
        db.execSQL(createColaboradorTable)

        val defaultGestorValues = ContentValues().apply {
            put("matricula", 120)
            put("senha", 1234)
        }
        db.insert(TABLE_GESTOR, null, defaultGestorValues)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_GESTOR")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_COLABORADOR")
        onCreate(db)
    }

    fun deleteColaborador(matricula: Int): Boolean {
        val db = this.writableDatabase
        val result = db.delete(TABLE_COLABORADOR, "matricula = ?", arrayOf(matricula.toString()))
        return result > 0
    }

    fun deleteAllColaboradores(): Boolean {
        val db = this.writableDatabase
        val result = db.delete(TABLE_COLABORADOR, null, null)
        return result > 0
    }

    fun verificarLoginGestor(matricula: Int, senha: Int): Boolean {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_GESTOR WHERE matricula = ? AND senha = ?", arrayOf(matricula.toString(), senha.toString()))
        val isValid = cursor.count > 0
        cursor.close()
        return isValid
    }

    fun getGestorIdByMatricula(matricula: Int): Int {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT id FROM $TABLE_GESTOR WHERE matricula = ?", arrayOf(matricula.toString()))
        val id = if (cursor.moveToFirst()) cursor.getInt(0) else -1
        cursor.close()
        return id
    }

    fun cadastrarColaborador(gestorId: Int, nome: String, cargo: String, maquina: String, matricula: Int, senha: Int): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put("gestor_id", gestorId)
            put("nome", nome)
            put("cargo", cargo)
            put("maquina", maquina)
            put("matricula", matricula)
            put("senha", senha)
        }
        val result = db.insert(TABLE_COLABORADOR, null, contentValues)
        return result != -1L
    }

    fun getColaboradores(): List<Colaborador> {
        val colaboradores = mutableListOf<Colaborador>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT nome, matricula, cargo FROM $TABLE_COLABORADOR", null)
        if (cursor.moveToFirst()) {
            do {
                val nome = cursor.getString(cursor.getColumnIndexOrThrow("nome"))
                val matricula = cursor.getInt(cursor.getColumnIndexOrThrow("matricula"))
                val cargo = cursor.getString(cursor.getColumnIndexOrThrow("cargo"))
                colaboradores.add(Colaborador(nome, matricula, cargo))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return colaboradores
    }

    fun verificarLoginColaborador(matricula: Int, senha: Int): Boolean {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_COLABORADOR WHERE matricula = ? AND senha = ?", arrayOf(matricula.toString(), senha.toString()))
        val isValid = cursor.count > 0
        cursor.close()
        return isValid
    }
}
