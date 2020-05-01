package com.example.sqlessons;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    // --- Константы для: -------------- //
    public static final int DATABASE_VERSION = 1;              // ... версии БД
    public static  final String DATABASE_NAME = "contactDb";  // ... имени БД
    public static  final String TABLE_CONTACTS = "contacts"; // ... имени таблицы

    public static  final String KEY_ID = "_id";            //...заголовка столбца Id
    /* ЗАМЕЧАНИЕ! Идентификатор должен быть именно _id, т.к. такое название используется в
       Android для работы с курсорами. Это особенность данной платформы  */

    public static  final String KEY_NAME = "name";     //...заголовка столбца Name
    public static  final String KEY_MAIL = "mail";    //...заголовка столбца Email
    // --------------------------------- //

    // _____________Конструктор_____________

    public DBHelper(@Nullable Context context)
    {
           super(context,               // контекст БД
                 DATABASE_NAME,        // имя БД
                 null,         //объект класса  CursorFactory, расширяющий стандартный курсор (не будем использовать, поэтому его мы просто занулим)
                 DATABASE_VERSION);  //версия БД
    }
    // ____________________________________

    //метод, который вызывается при первом создании базы данных
    @Override
    public void onCreate(SQLiteDatabase db) {

        /* _________ Логика создания таблиц и _________   *\
        \*         заполнения их начальными данными       */

        db.execSQL("create table " + TABLE_CONTACTS + "(" + KEY_ID +    // метод, выполняющий SQL запрос.
                   "integer primary key," + KEY_NAME + " text, " +      // В данный момент используем его для создания таблицы CONTACTS
                    KEY_MAIL + " text" + ")");                          // с полями ID, NAME и MAIL

        /* ВНИМАНИЕ! Тут нужно учитывать пробелы и знаки препинания между ключами,
           чтобы получилась корректная строка SQL команды                          */


    }



    // метод, который вызывается при изменении базы данных
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("drop table if exists " + TABLE_CONTACTS); // реализуем запрос в БД для уничтожения таблицы
        onCreate(db);                                        // вызываем метод onCreate() для повторногго создания обновленной БД
    }

                                                                                                     /*                            -=ДОПОЛНИТЕЛЬНО=-
                                                                                                      *
                                                                                                      * Есть ещё 4 необязательных метода в SQLiteOpenHelper:
                                                                                                      *
                                                                                                      * onDowngrade() - вызывается в ситуации, обратной onUpgrade (номер версии БД понижается)
                                                                                                      * onOpen() - вызывается при открытии базы данных
                                                                                                      * getReadableDatabase() - возвращает БД для чтения
                                                                                                      * getWritableDatabase() - возвращает БД для чтения и записи
                                                                                                      */
}


