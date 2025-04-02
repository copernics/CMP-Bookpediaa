package com.plcoding.bookpedia.book.data.database

import androidx.room.Room
import androidx.room.RoomDatabase
import java.io.File

actual class DatabaseFactory {
    actual fun create(): RoomDatabase.Builder<BookDatabase> {
        val os = System.getProperty("os.name").lowercase()
        val userHome = System.getProperty("user.home")
        val appDataDir = when {
            os.contains("win") -> File(System.getenv("APPDATA") + "Bookpedia")
            os.contains("nix") || os.contains("nux") || os.contains("aix") -> File(userHome + "/.local/share/Bookpedia")
            os.contains("mac") -> File(userHome + "/Library/Application Support/Bookpedia")
            else -> throw RuntimeException("Unsupported OS")
        }

        if (!appDataDir.exists()) {
            appDataDir.mkdirs()
        }
        val dbFile = File(appDataDir, BookDatabase.DATABASE_NAME)
        return Room.databaseBuilder(dbFile.absolutePath)
    }
}