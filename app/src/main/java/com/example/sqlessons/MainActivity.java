package com.example.sqlessons;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.sql.SQLData;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnAdd, btnRead, btnClear, btnUpdate;                                                                // объявляем кнопки и поля ввода из xml файла с разметкой
    EditText etName, etEmail;

    DBHelper dbHelper;                                                                               // объявляем переменную класса DBHelper (загляни в этот класс, чтобы разобраться)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* _____________Привязываем кнопки и поля ввода____________ */

        btnAdd = (Button)findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);
        btnClear = (Button)findViewById(R.id.btnClear);
        btnClear.setOnClickListener(this);
        btnRead = (Button)findViewById(R.id.btnRead);
        btnRead.setOnClickListener(this);
        btnUpdate = (Button)findViewById(R.id.btnUpdate);
        btnUpdate.setOnClickListener(this);

        etName = (EditText)findViewById(R.id.etName);
        etEmail = (EditText)findViewById(R.id.etEmail);
        /* ________________________________________________________ */

        dbHelper = new DBHelper(this);                                                        // создаем экземпляр класса DBHelper

    }

    @Override
    public void onClick(View v) {

        String name = etName.getText().toString();                                                    // строки, считанные с полей ввода, записываем в
        String email = etEmail.getText().toString();                                                  // соответствующие переменные

        SQLiteDatabase database = dbHelper.getWritableDatabase();                                    //создаем экземпляр класса SQLiteDatabase
        // вызываем метод вспомогательного класса DBHelper.GetWritableDatabase(), чтобы открыть и вернуть экземпляр базы данных, с которой мы будем работать

        // cм. ДОПОЛНИТЕЛЬНО (1)


        ContentValues contentValues = new ContentValues();                                           // создаем экземпляр класса ContentValues

        /* ДОПОЛНЕНИЕ: класс ContentValues используется для добавления новых строк в таблицу. Каждый объект класса - отдельная строка.
           Выглядит он как массив с именами столбцов и значениями, которые им соответствуют. (увидишь, как это выглядит в Switch-е)  */




        /* ________ Конструкцией Switch распределим действия по кнопкам ________ */

        switch (v.getId()) {                                                                         // в скобках условие. Мы проверяем, какое значение вернет ф-ия v.getId()

            case R.id.btnAdd:                      // если нажата кнопка Add
                                             // _________Запись в таблицу_________

                contentValues.put(DBHelper.KEY_NAME, name);                                           // заполняем объект ContentValues парами имя_поля/значение
                contentValues.put(DBHelper.KEY_MAIL, email);                                         // в указанные поля будут вставлены указанные значения

                database.insert(DBHelper.TABLE_CONTACTS, null, contentValues);         // методом insert() вставляем подготовленные строки в таблицу.
                // второй аргумент используется для вставки пустой строки, но нам это сейчас не нужно, поэтому null

                break;

            case R.id.btnRead:                       // если нажата кнопка Read
                                        // _________Чтение всех записей из таблицы_________

                /*  P.S. Как выяснилось, код ниже выводит данные только в меню Logs в AndroidStudio ,
                    но все ок, я добавил поле с кнопкой Update, которое показывает, что сейчас лежит в БД (см. btnUpdate) */

                Cursor cursor = database.query (DBHelper.TABLE_CONTACTS,                                            // для чтения используется метод query(). На вход подается имя таблицы,
                        null, null, null, null, null, null);   // список запрашиваемых полей, условие выборки, группировка и сортировка
                // см. ДОПОЛНИТЕЛЬНО (2)


                if(cursor.moveToFirst()){    //  метод moveToFirst() - первая запись в Cursor становится активной + проверяется, ести ли вообще запись в нем(выбиралось ли что-то в методе query())

                    int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID);                            // получаем порядковые номера столбцов cursor по их именам
                    int nameIndex = cursor.getColumnIndex(DBHelper.KEY_NAME);
                    int emailIndex = cursor.getColumnIndex(DBHelper.KEY_MAIL);

                    do {                                                                             // полученные номера затем используются для чтения данных в методах getInt() и getString()
                        Log.d("mLog", "ID = " + cursor.getInt(idIndex) +
                                ", name = " + cursor.getString(nameIndex) +
                                ", email = " + cursor.getString(emailIndex));
                    }while (cursor.moveToNext());                                                    // перебираем все строки в курсоре, пока не добираемся до последней (moveToNext())

                } else
                    Log.d("mLog", "0 rows");                                               // если записи не было, то выводим в Log соответствующее сообщение

                cursor.close();                                                                      // закрываем курсор, освобождая занимаемые им ресурсы. Далее мы его не используем
                //ЗАМЕЧАНИЕ: курсор всегда следует закрывать, чтобы он не занимал память

                break;

            case R.id.btnClear:                     // если нажата кнопка Clear
                                          //_______________Очистка таблицы_______________

                database.delete(DBHelper.TABLE_CONTACTS, null, null);            // метод delete() удаляет записи из БД. На вход подаются имя таблицы и null в качестве
                                                                                                     // условий для удаления. Это значит, что будут удаляться все данные из таблицы.
                break;

            case R.id.btnUpdate:               // если нажата кнопка Update
                                        //_________Вывод содержимого БД___________

                Cursor cursor2 = database.query (DBHelper.TABLE_CONTACTS,                            // создадим новый курсор. Можно было бы просто вынести старый курсор за пределы Switch, но мне лень
                        null, null, null, null, null, null);

                int idIndex = cursor2.getColumnIndex(DBHelper.KEY_ID);
                int nameIndex = cursor2.getColumnIndex(DBHelper.KEY_NAME);
                int emailIndex = cursor2.getColumnIndex(DBHelper.KEY_MAIL);

                LinearLayout DB_line = (LinearLayout)findViewById(R.id.DB_line);                     // находим Layout, который лежит в ScrollView
                DB_line.removeAllViews();                                                            // при нажатии на кнопку Update мы удаляем все записи из Layout-а, и записываем их заново

                if(cursor2.moveToFirst()) {
                    do {                                                                             // при помощи цикла
                        TextView DB_line_txt = new TextView(this);
                        DB_line_txt.setText("\n"  + "name: " + cursor2.getString(nameIndex) +        // заполняем TextView данными из полей name и email
                                            "\n" + "email: "+ cursor2.getString(emailIndex) + "\n"); //

                        DB_line.addView(DB_line_txt);                                                // и затем добавляем этот TextView в Layout
                    } while (cursor2.moveToNext());                                                  // и так будет повторяться до конца таблицы
                }
                else {
                    TextView DB_line_txt = new TextView(this);                                // если курсор не найдет ни одной записи в таблице, то выведет соответствующее сообщение
                    DB_line_txt.setText("Нет записей");
                    DB_line.addView(DB_line_txt);
                }
                cursor2.close();                                                                     // не забываем закрыть второй курсор

                break;

        }


        dbHelper.close();                                                                            //закрываем соединение с БД

    }
}

/*                        -= ДОПОЛНИТЕЛЬНО (1) =-
 * Данный класс предназначен для управления БД SQLite.
 * В нем определены такие методы:
 *   query() - для чтения данных БД
 *   insert() - для добавления данных в БД
 *   delete() - для удаления данных из БД
 *   update() - для изменения данных в БД
 *
 *  (+) метод execSQL() позволяет выполнить любой код на языке SQL
 */

/*                      -= ДОПОЛНИТЕЛЬНО (2) =-
 * Объект класса Cursor - набор строк с данными.
 * Класс Cursor обладает такими методами:
 * - moveToFirst() - перемещает курсор на первую строку
 * - moveToNext() - перемещает курсор на следующую строку
 * - moveToLast() - перемещает курсор на последнюю строку
 * - moveToPrevious() - перемещает курсор на предыдущую строку
 * - getCount() - возвращает кол-во строк в наборе данных
 * - getColumnIndexOrThrow() - возвращает индекс для столбца с указанным именем
 * - getColumnName() - возвращает имя столбца с указанным индексом
 * - getColumnNames() - возвращает массив имен всех столбцов в объекте Cursor
 * - moveToPosition() - перемещает курсор на указанную строку
 * - getPosition() - возвращает текущую позицию курсора
 * - isBeforeFirst() - возвращает, указывает ли курсор на позицию перед первой строкой
 * - isAfterLast() - полезный метод, сигнализирующий о достижении конца запроса
 * - isClosed() - возвращает значение true , если курсор закрыт
 */
