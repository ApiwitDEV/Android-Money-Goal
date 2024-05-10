package com.overshoot.data.datasource.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.overshoot.data.datasource.local.goal.GoalDao
import com.overshoot.data.datasource.local.goal.GoalEntity
import com.overshoot.data.datasource.local.transaction.CategoryDao
import com.overshoot.data.datasource.local.transaction.CategoryEntity
import com.overshoot.data.datasource.local.transaction.TransactionDao
import com.overshoot.data.datasource.local.transaction.TransactionEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [GoalEntity::class, TransactionEntity::class, CategoryEntity::class],
    version = 1,
    exportSchema = false
)
abstract class GoalDatabase : RoomDatabase() {

    abstract fun goalDao(): GoalDao
    abstract fun transactionDao(): TransactionDao
    abstract fun categoryDao(): CategoryDao

    companion object {
        @Volatile
        var INSTANCE: GoalDatabase? = null
        fun getDatabase(context: Context): GoalDatabase {
            return INSTANCE?: synchronized(this) {
                val instance =
                Room.databaseBuilder(
                    context = context,
                    GoalDatabase::class.java,
                    "goal_database"
                )
                    .addCallback(
                        callback = object : Callback() {
                            override fun onCreate(db: SupportSQLiteDatabase) {
                                super.onCreate(db)
                                INSTANCE?.let {
                                    CoroutineScope(Dispatchers.IO).launch {
                                        //INSTANCE?.categoryDao()?.addCategory()
                                    }
                                }
                            }

                            override fun onOpen(db: SupportSQLiteDatabase) {
                                super.onOpen(db)
                            }

                            override fun onDestructiveMigration(db: SupportSQLiteDatabase) {
                                super.onDestructiveMigration(db)
                            }
                        }
                    )
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

}